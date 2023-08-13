package struktury;

import java.util.Iterator;

public interface IAbstrLifo<T> {
    void zrus();
    boolean jePrazdny();
    void vloz(T data);
    T odeber();
    Iterator vytvorIterator();
}
