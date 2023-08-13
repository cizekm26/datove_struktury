package struktury;

import java.util.Iterator;

public class AbstrFifo<T> implements IAbstrFifo<T>{

    private final AbstrDoubleList<T> fronta = new AbstrDoubleList<>();
    
    @Override
    public void zrus() {
        fronta.zrus();
    }

    @Override
    public boolean jePrazdny() {
        return fronta.jePrazdny();
    }

    @Override
    public T odeber() {
        return fronta.odeberPrvni();
    }

    @Override
    public void vloz(T data) {
        if(data != null){
            fronta.vlozPosledni(data);
        }
    }

    @Override
    public Iterator vytvorIterator() {
        return fronta.iterator();
    }

    
    
}
