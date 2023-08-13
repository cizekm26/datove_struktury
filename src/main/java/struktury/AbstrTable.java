package struktury;

import enums.eTypProhl;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class AbstrTable<K extends Comparable<K>, V> implements IAbstrTable, Serializable {

    private class Prvek implements Serializable{

        Object value;
        Comparable key;
        Prvek levy;
        Prvek pravy;

        public Prvek(Object value, Comparable key) {
            this.value = value;
            this.key = key;
            this.levy = null;
            this.pravy = null;
        }

    }

    private Prvek koren;

    public AbstrTable() {
        koren = null;
    }

    @Override
    public void zrus() {
        koren = null;
    }

    @Override
    public boolean jePrazdny() {
        return koren == null;
    }

    @Override
    public Object najdi(Comparable key) {
        Prvek aktualni = this.koren;
        while (aktualni != null) {
            if (key.compareTo(aktualni.key) > 0) {
                aktualni = aktualni.pravy;
            } else if (key.compareTo(aktualni.key) < 0) {
                aktualni = aktualni.levy;
            } else {
                return aktualni.value;
            }
        }
        return null;
    }

    @Override
    public void vloz(Comparable key, Object value) {
        koren = vlozRekurze(koren, key, value);
    }

    private Prvek vlozRekurze(Prvek koren, Comparable key, Object value) {
        if (koren == null) {
            return new Prvek(value, key);
        }
        if (key.compareTo(koren.key) < 0) {
            koren.levy = vlozRekurze(koren.levy, key, value);
        } else if (key.compareTo(koren.key) > 0) {
            koren.pravy = vlozRekurze(koren.pravy, key, value);
        }else{
            return koren;
        }
        return koren;
    }

    @Override
    public Object odeber(Comparable key) {
        Object odebrany = najdi(key);
        if(odebrany != null)
            koren = odeberRekurze(koren, key);
        return odebrany;
    }

    private Prvek odeberRekurze(Prvek koren, Comparable key) {
        if (koren == null) {
            return null;
        }

        if (key.compareTo(koren.key) < 0) {
            koren.levy = odeberRekurze(koren.levy, key);
        } else if (key.compareTo(koren.key) > 0) {
            koren.pravy = odeberRekurze(koren.pravy, key);
        } else {
            //pokud má odebrany prvek jednoho potomka, tak ho nahradím tímto potomkem
            if (koren.pravy == null) {
                return koren.levy;
            } else if (koren.levy == null) {
                return koren.pravy;
            }
            //pokud má odebraný prvek 2 potomky, tak přesunu nejpravějšího potomka z levého podstromu na jeho pozici
            Prvek nejpravejsi = nejpravejsiPrvek(koren.levy);
            koren.key = nejpravejsi.key;
            koren.value = nejpravejsi.value;
            //odebere nejpravější prvek z původní pozice
            koren.levy = odeberRekurze(koren.levy, koren.key);
        }
        return koren;
    }

    private Prvek nejpravejsiPrvek(Prvek koren) {
        while (koren.pravy != null) {
            koren = koren.pravy;
        }
        return koren;
    }

    @Override
    public Iterator vytvorIterator(eTypProhl typ) {
        switch (typ) {
            case DO_SIRKY:
                IAbstrFifo<Prvek> fronta = new AbstrFifo();
                fronta.vloz(koren);
                return new Iterator() {

                    @Override
                    public boolean hasNext() {
                        return !fronta.jePrazdny();
                    }

                    @Override
                    public Object next() {
                        if(hasNext()){
                            Prvek odebrany = fronta.odeber();
                            if(odebrany.levy != null){
                                fronta.vloz(odebrany.levy);
                            }
                            if(odebrany.pravy != null){
                                fronta.vloz(odebrany.pravy);
                            }
                            return odebrany.value;
                        }else{
                            throw new NoSuchElementException();
                        }
                    }
                };
            case DO_HLOUBKY:
                IAbstrLifo<Prvek> zasobnik = new AbstrLifo<>();
                return new Iterator() {
                    Prvek aktualni = koren;

                    @Override
                    public boolean hasNext() {
                        return !zasobnik.jePrazdny() || aktualni != null;
                    }

                    @Override
                    public Object next() {
                        if (hasNext()) {
                            while (aktualni != null) {
                                zasobnik.vloz(aktualni);
                                aktualni = aktualni.levy;
                            }
                            //odebere nejlevější prvek
                            Prvek odebrany = zasobnik.odeber();
                            aktualni = odebrany.pravy;
                            return odebrany.value;
                        } else {
                            throw new NoSuchElementException();
                        }
                    }
                };
        }
        return null;
    }
}
