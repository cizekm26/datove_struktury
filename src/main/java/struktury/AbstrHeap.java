package struktury;

import enums.eTypProhl;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import pamatky.Zamek;

public class AbstrHeap<T extends Comparable<T>> implements IAbstrHeap, Serializable {

    private T[] halda;
    private int pocet;
    private int maxPocet = 10;

    public AbstrHeap() {
        halda = (T[]) new Comparable[maxPocet];
    }

    @Override
    public void vybuduj(Comparable[] pole) {
        halda = (T[]) pole;
        pocet = pole.length;
        maxPocet = halda.length;
        //index posledního prvku, který má potomka
        int index = (halda.length / 2) - 1;
        for (int i = index; i >=0;i--) {
            vybudujRekurze(halda,i);
        }
    }
    
    private void vybudujRekurze(Comparable[] pole, int index){
        int rodic = index;
        int levy = 2*rodic+1;
        int pravy = 2*rodic+2;
        //pokud je levy nebo pravy potomek větší než jeho rodič tak se s nim prohodí
        if(levy < pole.length && pole[levy].compareTo(pole[rodic]) < 0)
            rodic = levy;
        if(pravy < pole.length && pole[pravy].compareTo(pole[rodic]) < 0)
            rodic = pravy;
        if(rodic != index){
            Comparable pom = pole[index];
            pole[index] = pole[rodic];
            pole[rodic] = pom;
            //pokud došlo k prohození prvků, vybuduje se podstrom
            vybudujRekurze(pole,rodic);
        }
    }

    @Override
    public void prebuduj(String gps) {
        for(int i = 0; i < pocet; i++){
            if(halda[i] instanceof Zamek)
                ((Zamek)halda[i]).setVzdalenost(gps);
        }
        T[] prvkyHaldy = (T[])new Comparable[pocet];
        System.arraycopy(halda, 0, prvkyHaldy, 0, pocet);
        vybuduj(prvkyHaldy);
    }

    @Override
    public void zrus() {
        halda = (T[]) new Comparable[maxPocet];
        pocet = 0;
    }

    @Override
    public boolean jePrazdny() {
        return pocet <= 0;
    }

    @Override
    public Comparable odeberMax() {
        //prohodí kořen s posledním prvkem
        Comparable odebrany = halda[0];
        halda[0] = halda[pocet - 1];
        halda[pocet - 1] = null;
        pocet--;
        //nový kořen se posune dolů, pokud má nižší prioritu než potomci
        posunDolu(0);
        return odebrany;
    }

    @Override
    public Comparable zpristupniMax() {
        if (pocet > 0) {
            return halda[0];
        }
        return null;
    }

    @Override
    public Iterator vytvorIterator(eTypProhl typ) {
        switch (typ) {
            case DO_SIRKY:
                return new Iterator() {
                    int i = 0;

                    @Override
                    public boolean hasNext() {
                        return i < pocet;
                    }

                    @Override
                    public Object next() {
                        if (hasNext()) {
                            return halda[i++];
                        } else {
                            throw new NoSuchElementException();
                        }
                    }
                };
            case DO_HLOUBKY:
                IAbstrLifo<Integer> zasobnik = new AbstrLifo<>();
                return new Iterator() {
                    int i = 0;
                    @Override
                    public boolean hasNext() {
                        return i<pocet || !zasobnik.jePrazdny();
                    }

                    @Override
                    public Object next() {
                        if(hasNext()){
                            while(i < pocet){
                                zasobnik.vloz(i);
                                i = i * 2 + 1;
                            }
                            int odebrany = zasobnik.odeber();
                            i = odebrany * 2 + 2;
                            return halda[odebrany]; 
                        }else {
                            throw new NoSuchElementException();
                        }
                    }

                };
        }
        return null;
    }

    @Override
    public void vloz(Comparable prvek) {
        //pokud pocet prvků přesahuje velikost pole, tak se zvětší 2krát
        if (pocet >= maxPocet) {
            maxPocet *= 2;
            T[] novaHalda = (T[]) new Comparable[maxPocet];
            System.arraycopy(halda, 0, novaHalda, 0, pocet);
            halda = novaHalda;
        }
        //prvek se vloží na poslední pozici
        halda[pocet++] = (T)prvek;
        //prvek se posune nahoru pokud má vyšší priotitu než jeho rodič
        posunNahoru(pocet - 1);
    }

    private void posunNahoru(int index) {
        int rodic = (index - 1) / 2;
        T pom;
        //pokud má potomek vyšší prioritu než rodič, tak se prohodí
        while (index > 0 && halda[index].compareTo(halda[rodic]) < 0) {
            pom = halda[index];
            halda[index] = halda[rodic];
            halda[rodic] = pom;

            index = rodic;
            rodic = (index - 1) / 2;
        }
    }

    private void posunDolu(int index) {
        int potomek = 2 * index + 1;
        T pom;
        while (potomek < pocet) {
            if (potomek + 1 < pocet) {
                //vybere se potomek s vyšší prioritou
                if (halda[potomek + 1].compareTo(halda[potomek]) < 0) {
                    potomek++;
                }
            }
            //pokud má rodič nižší prioritu než potomek, tak se prohodí 
            if (halda[index].compareTo(halda[potomek]) > 0) {
                pom = halda[index];
                halda[index] = halda[potomek];
                halda[potomek] = pom;
            }
            index = potomek;
            potomek = 2 * index + 1;
        }
    }

}
