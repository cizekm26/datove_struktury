package struktury;

import enums.eTypProhl;
import java.util.Iterator;

public interface IAbstrTable<K extends Comparable<K>, V> {

    void zrus();
    boolean jePrazdny();
    V najdi(K key);
    void vloz(K key, V value);
    V odeber(K key);
    Iterator vytvorIterator(eTypProhl typ);
}
