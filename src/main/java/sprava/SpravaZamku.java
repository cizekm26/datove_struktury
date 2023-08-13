
package sprava;

import enums.eTypKey;
import enums.eTypProhl;
import enums.eTypStruktura;
import generator.Generator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Objects;
import pamatky.Pamatky;
import pamatky.Zamek;
import struktury.AbstrHeap;
import struktury.IAbstrHeap;

public class SpravaZamku {
    private Pamatky pamatky;
    private IAbstrHeap<Zamek> fronta;
    private String aktualniPoloha;
    
    public SpravaZamku(){
        pamatky = new Pamatky();
        fronta = new AbstrHeap<>();
    }
    
    public void zmenKlic(eTypKey typKlice){
        pamatky.nastavKlic(typKlice);
        pamatky.prebuduj();
    }
    
    public boolean vlozZamek(Zamek zamek){
        int vysledek = pamatky.vlozZamek(zamek);
        return vysledek != 0;
    }
    
    public void generujZamky(int pocet){
        for (int i = 0; i < pocet; i++) {
            pamatky.vlozZamek(Generator.generujZamek());
        }
    }
    
    public int importujData(){
        return pamatky.importDatZTXT();
    }
    
    public Zamek najdiZamek(String klic){
        return pamatky.najdiZamek(klic);
    }
    
    public Zamek odeberZamek(String klic){
        return pamatky.odeberZamek(klic);
    }
    
    public void prebuduj(){
        pamatky.prebuduj();
    }
    
    public void uloz(String souborPamatky, String souborFronta){
        try {
            Objects.requireNonNull(pamatky);
            ObjectOutputStream vystup
                    = new ObjectOutputStream(
                            new FileOutputStream(souborPamatky));
            vystup.writeObject(pamatky);
            vystup.close();
            Objects.requireNonNull(fronta);
            vystup = new ObjectOutputStream(
                    new FileOutputStream(souborFronta));
            vystup.writeObject(fronta);
            vystup.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void nacti(String souborPamatky, String souborFronta){
        try {
            Objects.requireNonNull(pamatky);
            ObjectInputStream vstup
                    = new ObjectInputStream(
                            new FileInputStream(souborPamatky));
            pamatky.zrus();
            pamatky = (Pamatky) vstup.readObject();
            vstup.close();
            vstup = new ObjectInputStream(
                    new FileInputStream(souborFronta));
            fronta.zrus();
            fronta = (AbstrHeap) vstup.readObject();
            vstup.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public Iterator<Zamek> vytvorIterator(
            eTypStruktura typStruktury, 
            eTypProhl typProhlidky
    ){
        switch (typStruktury) {
            case BINARNI_STROM:
                return pamatky.VytvorIterator(typProhlidky);
            case PRIORITNI_FRONTA:
                return fronta.vytvorIterator(typProhlidky);
            default:
                return null;
        }
    }
    
    public Zamek najdiNejblizsi(String gps){
        return pamatky.najdiNejbliz(gps);
    }
    
    public Zamek odeberZFronty(){
        return fronta.odeberMax();
    }
    
    public void vybuduj(String gps){
        int pocet = 0;
        Iterator<Zamek> it = pamatky.VytvorIterator(eTypProhl.DO_SIRKY);
        while (it.hasNext()) {
            it.next();
            pocet++;
        }
        Zamek[] pole = new Zamek[pocet];
        it = pamatky.VytvorIterator(eTypProhl.DO_HLOUBKY);
        int i = 0;
        // u každého zámku se nastaví vzdálenost od aktuální polohy
        aktualniPoloha = gps;
        while (it.hasNext()) {
            pole[i] = it.next();
            pole[i].setVzdalenost(aktualniPoloha);
            i++;
        }
        fronta.vybuduj(pole);
    }
    
    public void prebudujFrontu(String gps){
        aktualniPoloha = gps;
        fronta.prebuduj(aktualniPoloha);
    }
    
    public void zrusPamatky(){
        pamatky.zrus();
    }
    
    public void zrusFrontu(){
        fronta.zrus();
    }
}
