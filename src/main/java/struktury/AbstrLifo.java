package struktury;

import java.util.Iterator;

public class AbstrLifo<T> implements IAbstrLifo<T> {
    private final AbstrDoubleList<T> zasobnik = new AbstrDoubleList<>();
    
    @Override
    public void zrus() {
        zasobnik.zrus();
    }

    @Override
    public boolean jePrazdny() {
        return zasobnik.jePrazdny();
    }

    @Override
    public T odeber() {
        return zasobnik.odeberPosledni();
    }

    @Override
    public void vloz(T data) {
        if(data != null){
            zasobnik.vlozPosledni(data);
        }
    }

    @Override
    public Iterator vytvorIterator() {
        return zasobnik.iterator();
    }
    
}
