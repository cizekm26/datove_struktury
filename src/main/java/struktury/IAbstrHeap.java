package struktury;

import enums.eTypProhl;
import java.util.Iterator;

public interface IAbstrHeap<T extends Comparable>  {
    void vybuduj(T[] pole);
    void prebuduj(String gps);
    void zrus();
    boolean jePrazdny();
    void vloz(T prvek);
    T odeberMax();
    T zpristupniMax();
    Iterator vytvorIterator(eTypProhl typ);
}
