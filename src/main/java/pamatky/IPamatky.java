package pamatky;

import enums.eTypKey;
import enums.eTypProhl;
import java.util.Iterator;

public interface IPamatky {

    int importDatZTXT();
    
    Zamek najdiZamek(String klic);

    int vlozZamek(Zamek zamek);
    
    Zamek odeberZamek(String klic);

    void zrus();

    void prebuduj();

    void nastavKlic(eTypKey typ);

    Zamek najdiNejbliz(String klic);

    Iterator VytvorIterator(eTypProhl typ);
}
