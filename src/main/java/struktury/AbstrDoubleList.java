package struktury;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstrDoubleList<T> implements IAbstrDoubleList<T> {

    private class Prvek{
        T data;
        Prvek predchudce;
        Prvek naslednik;

        public Prvek(T data, Prvek predchudce, Prvek naslednik) {
            this.data = data;
            this.predchudce = predchudce;
            this.naslednik = naslednik;
        }

    }
    
    private Prvek prvni;
    private Prvek posledni;
    private Prvek aktualni;
    private int pocet = 0;
    
    @Override
    public void zrus() {
        this.prvni = null;
        this.posledni = null;
        this.aktualni = null;
        this.pocet = 0;
    }

    @Override
    public boolean jePrazdny() {
        return prvni == null;
    }

    @Override
    public void vlozPrvni(T data) {
        if (data == null) {
            throw new NullPointerException();
        }
        if (jePrazdny()) {
            prvni = new Prvek(data, null, null);
            prvni.naslednik = prvni;
            prvni.predchudce = prvni;
            posledni = prvni;
        } else {
            prvni.predchudce = new Prvek(data, posledni, prvni);
            prvni = prvni.predchudce;
            posledni.naslednik = prvni;
        }
        pocet++;
    }

    @Override
    public void vlozPosledni(T data) {
        if (data == null) {
            throw new NullPointerException();
        }
        if (jePrazdny()) {
            prvni = new Prvek(data, null, null);
            prvni.naslednik = prvni;
            prvni.predchudce = prvni;
            posledni = prvni;
        } else {
            posledni.naslednik = new Prvek(data, posledni, prvni);
            posledni = posledni.naslednik;
            prvni.predchudce = posledni;
        }
        pocet++;
    }

    @Override
    public void vlozNaslednika(T data) {
        if (data == null || aktualni == null) {
            throw new NullPointerException();
        }
        if (aktualni == posledni) {
            posledni.naslednik = new Prvek(data, posledni, prvni);
            posledni = posledni.naslednik;
            prvni.predchudce = posledni;
        } else {
            Prvek novy = new Prvek(data, aktualni, aktualni.naslednik);
            aktualni.naslednik = novy;
            aktualni.naslednik.naslednik.predchudce = aktualni.naslednik;
        }
        pocet++;
    }

    @Override
    public void vlozPredchudce(T data) {
        if (data == null || aktualni == null) {
            throw new NullPointerException();
        }
        if (aktualni == prvni) {
            prvni.predchudce = new Prvek(data, posledni, prvni);
            prvni = prvni.predchudce;
            posledni.naslednik = prvni;
        } else {
            Prvek novy = new Prvek(data, aktualni.predchudce, aktualni);
            aktualni.predchudce = novy;
            aktualni.predchudce.predchudce.naslednik = aktualni.predchudce;
        }
        pocet++;
    }

    @Override
    public T zpristupniAktualni() {
        if (aktualni == null) {
            return null;
        }
        return aktualni.data;
    }

    @Override
    public T zpristupniPrvni() {
        if (prvni == null) {
            return null;
        }
        aktualni = prvni;
        return aktualni.data;
    }

    @Override
    public T zpristupniPosledni() {
        if (posledni == null) {
            return null;
        }
        aktualni = posledni;
        return aktualni.data;
    }

    @Override
    public T zpristupniNaslednika() {
        if (aktualni == null) {
            return null;
        }
        aktualni = aktualni.naslednik;
        return aktualni.data;
    }

    @Override
    public T zpristupniPredchudce() {
        if (aktualni == null) {
            return null;
        }
        aktualni = aktualni.predchudce;
        return aktualni.data;
    }
    
    @Override
    public T odeberAktualni(){
        if (aktualni == null) {
            return null;
        }
        Prvek odebrany = aktualni;
        if (prvni == posledni) {
            zrus();
        } else {
            aktualni.predchudce.naslednik = aktualni.naslednik;
            aktualni.naslednik.predchudce = aktualni.predchudce;
            if(odebrany == prvni){
                prvni = aktualni.naslednik;
            }else if(odebrany == posledni){
                posledni = aktualni.predchudce;
            }
            aktualni = prvni;
            pocet--;
        }
        return odebrany.data;
    }

    @Override
    public T odeberPrvni() {
        if (prvni == null) {
            return null;
        }
        Prvek odebrany = prvni;
        if (prvni == posledni) {
            zrus();
        } else {
            posledni.naslednik = prvni.naslednik;
            prvni.naslednik.predchudce = posledni;
            prvni = prvni.naslednik;
            if(odebrany == aktualni){
                aktualni = prvni;
            }
            pocet--;
        }
        return odebrany.data;
    }

    @Override
    public T odeberPosledni() {
        if (posledni == null) {
            return null;
        }
        Prvek odebrany = posledni;
        if (prvni == posledni) {
            zrus();
        } else {
            prvni.predchudce = posledni.predchudce;
            posledni.predchudce.naslednik = prvni;
            posledni = posledni.predchudce;
            if(odebrany == aktualni){
                aktualni = prvni;
            }
            pocet--;
        }
        return odebrany.data;
    }

    @Override
    public T odeberNaslednika() {
        if (aktualni == null) {
            return null;
        }
        Prvek odebrany = aktualni.naslednik;
        if (prvni == posledni) {
            zrus();
        } else {
            aktualni.naslednik = aktualni.naslednik.naslednik;
            aktualni.naslednik.predchudce = aktualni;
            if (odebrany == prvni) {
                prvni = aktualni.naslednik;
            } else if (odebrany == posledni) {
                posledni = aktualni;
            }
            pocet--;
        }
        return odebrany.data;
    }

    @Override
    public T odeberPredchudce() {
        if (aktualni == null) {
            return null;
        }
        Prvek odebrany = aktualni.predchudce;
        if (prvni == posledni) {
            zrus();
        } else {
            aktualni.predchudce = aktualni.predchudce.predchudce;
            aktualni.predchudce.naslednik = aktualni;
            if (odebrany == prvni) {
                prvni = aktualni;
            } else if (odebrany == posledni) {
                posledni = aktualni.predchudce;
            }
            pocet--;
        }
        return odebrany.data;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Prvek aktualni = prvni;
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < pocet && aktualni != null;
            }

            @Override
            public T next() {
                if (hasNext()) {
                    T data = aktualni.data;
                    aktualni = aktualni.naslednik;
                    i++;
                    return data;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
    
}
