package pamatky;

import enums.eTypKey;
import enums.eTypProhl;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import struktury.AbstrTable;
import struktury.IAbstrTable;

public class Pamatky implements Serializable, IPamatky {

    private final IAbstrTable<String, Zamek> tabulka;
    private eTypKey typKlice;
    
    private final String SOUBOR_TXT = "data.txt";
    private final int ZACATEK_SOUBORU = 6;
    private final int ZACATEK_RADKU = 3;
    private final int ODDELOVAC = 25;

    public Pamatky() {
        tabulka = new AbstrTable<>();
        typKlice = eTypKey.NAZEV;
    }

    @Override
    public int importDatZTXT() {
        int pocetZaznamu = 0;
        String id, gps, nazev;
        try ( BufferedReader br = new BufferedReader(new FileReader(SOUBOR_TXT))) {
            String radek;
            int i = ZACATEK_SOUBORU;
            int zacatek = i;
            while ((radek = br.readLine()) != null) {
                while (radek.charAt(i) != ' ' || radek.charAt(i + 1) != ' ') {
                    i++;
                }
                id = radek.substring(zacatek, i).trim();
                while (radek.charAt(i) == ' ') {
                    i++;
                }
                zacatek = i;
                i += ODDELOVAC;
                gps = radek.substring(zacatek, i);
                i += ODDELOVAC;
                zacatek = i;
                while (radek.charAt(i) != '^') {
                    i++;
                }
                nazev = radek.substring(zacatek, i).trim();
                vlozZamek(new Zamek(id, nazev, gps));
                pocetZaznamu++;
                i = ZACATEK_RADKU;
                zacatek = i;
            }
            return pocetZaznamu;
        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
            return 0;
        }
    }

    @Override
    public int vlozZamek(Zamek zamek) {
        switch (typKlice) {
            case NAZEV:
                tabulka.vloz(zamek.getNazev(), zamek);
                return 1;
            case GPS:
                tabulka.vloz(zamek.getGps(), zamek);
                return 1;
        }
        return 0;
    }

    @Override
    public Zamek odeberZamek(String klic) {
        return tabulka.odeber(klic);
    }

    @Override
    public void zrus() {
        tabulka.zrus();
    }

    @Override
    public void prebuduj() {
        Iterator<Zamek> it = tabulka.vytvorIterator(eTypProhl.DO_SIRKY);
        int pocet = 0;
        while (it.hasNext()) {
            it.next();
            pocet++;
        }
        //uloží zámky ze stromu do pole
        Zamek[] zamky = new Zamek[pocet];
        it = tabulka.vytvorIterator(eTypProhl.DO_HLOUBKY);
        int i = 0;
        while (it.hasNext()) {
            zamky[i] = it.next();
            i++;
        }
        tabulka.zrus();
        seradPole(zamky, 0, zamky.length - 1);
        prebudujRekurze(tabulka, zamky, 0, pocet - 1);

    }

    private void seradPole(Zamek[] zamky, int levy, int pravy){
        if(levy < pravy){
            int pivot = rozdeleni(zamky, levy, pravy);
            seradPole(zamky, levy, pivot-1);
            seradPole(zamky, pivot+1, pravy);
        }
    }
    
    private int rozdeleni(Zamek[] zamky, int levy, int pravy){
        Zamek pivot = zamky[pravy];
        int i = levy-1;
        for(int j = levy;j<pravy;j++){
            if(hodnotaKlice(zamky[j]).compareTo(hodnotaKlice(pivot)) <= 0){
                i++;
                Zamek pom = zamky[i];
                zamky[i] = zamky[j];
                zamky[j] = pom;
            }
        }
        Zamek pom = zamky[i+1];
        zamky[i+1] = zamky[pravy];
        zamky[pravy] = pom;
        return i+1;
    }
    
    private void prebudujRekurze(IAbstrTable tabulka, Zamek[] pole, int zacatek, int konec) {
        if (zacatek <= konec && konec >= 0) {
            //vypočítá index prostředního prvku, který bude kořenem
            int stred = (int) Math.round((konec - zacatek) / 2.0) + zacatek;
            Zamek koren = pole[stred];
            vlozZamek(koren);
            //vloží prvky z levé části pole
            prebudujRekurze(tabulka, pole, zacatek, stred - 1);
            //vloží prvky z pravé části pole
            prebudujRekurze(tabulka, pole, stred + 1, konec);
        }
    }

    @Override
    public void nastavKlic(eTypKey typ) {
        typKlice = typ;
    }

    @Override
    public Zamek najdiNejbliz(String klic) {
        if (typKlice == eTypKey.GPS) {
            //hodnoty jsou seřazeny od nejnižší do nejvyšší
            Iterator<Zamek> it = tabulka.vytvorIterator(eTypProhl.DO_HLOUBKY);
            Zamek nejblizsiZamek = null;
            Zamek aktualni;
            double nejkratsiVzdalenost = Double.MAX_VALUE;
            double vzdalenost = 0;
            //probíhá dokud je vzálenost od aktuálního prvku menší nebo rovna než vzdálenost od předchozího
            while (it.hasNext() && vzdalenost <= nejkratsiVzdalenost) {
                aktualni = it.next();
                vzdalenost = Math.abs(prevedNaStupne(aktualni.getGps()) - prevedNaStupne(klic));
                if (vzdalenost < nejkratsiVzdalenost) {
                    nejkratsiVzdalenost = vzdalenost;
                    nejblizsiZamek = aktualni;
                }
            }
            return nejblizsiZamek;
        } else {
            return null;
        }
    }

    private String hodnotaKlice(Zamek zamek) {
        if (typKlice == eTypKey.GPS) {
            return zamek.getGps();
        } else if (typKlice == eTypKey.NAZEV) {
            return zamek.getNazev();
        }
        return null;
    }

    private double prevedNaStupne(String gps) {
        String[] souradnice = gps.split(" ");
        //souradnice[0] - stupně N, souradnice[1] - minuty N 
        double stupne = Double.parseDouble(souradnice[0].substring(1));
        double minuty = Double.parseDouble(souradnice[1]);
        return stupne + minuty / 60.0;
    }

    @Override
    public Zamek najdiZamek(String klic) {
        return tabulka.najdi(klic);
    }

    @Override
    public Iterator VytvorIterator(eTypProhl typ) {
        return tabulka.vytvorIterator(typ);
    }
}
