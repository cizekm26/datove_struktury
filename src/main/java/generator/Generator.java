package generator;

import java.util.Locale;
import pamatky.Zamek;

public final class Generator {

    private static int idZamku = 1;
    
    private Generator(){
        
    }
    
    public static Zamek generujZamek(){
        String id = generujId();
        String nazev = generujNazev();
        String gps = generujGPS();
        idZamku += 1;
        return new Zamek(id,nazev,gps);
    }
    
    private static String generujId() {
        return String.format("ZAM%03d", idZamku);
    }

    private static String generujNazev() {
        return String.format("ZAMEK%03d", idZamku);
    }
    
    private static String generujGPS(){
        int stupneN = generujInt(40,50);
        double minutyN = generujDouble(0,60); 
        int stupneE = generujInt(10,20);
        double minutyE = generujDouble(0,60);
        String gps = "N" + stupneN + " ";
        gps += String.format(Locale.US, "%07.4f ", minutyN);
        gps += String.format(Locale.US, "E%03d ", stupneE);
        gps += String.format(Locale.US, "%07.4f", minutyE);
        return gps;
    }
    
    private static int generujInt(int minimum, int maximum) {
        return (int) (Math.random() * (maximum - minimum) + minimum);
    }
    
    private static double generujDouble(double minimum, double maximum){
        return Math.random() * (maximum - minimum) + minimum;
    }
}
