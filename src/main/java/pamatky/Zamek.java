package pamatky;

import java.io.Serializable;

public class Zamek implements Serializable, Comparable<Zamek>{
    private final String id;
    private final String nazev;
    private final String gps;
    private double vzdalenost;

    public Zamek(String id, String nazev, String gps) {
        this.id = id;
        this.nazev = nazev;
        this.gps = gps;
    }

    public String getId() {
        return id;
    }

    public String getNazev() {
        return nazev;
    }

    public String getGps() {
        return gps;
    }

    public double getVzdalenost() {
        return vzdalenost;
    }

    public void setVzdalenost(double vzdalenost) {
        this.vzdalenost = vzdalenost;
    }
    
    public void setVzdalenost(String gpsPolohy) {
        this.vzdalenost = Math.abs(prevedNaStupne(this.gps) - prevedNaStupne(gpsPolohy));
    }
    
    private double prevedNaStupne(String gps) {
        String[] souradnice = gps.split(" ");
        //souradnice[0] - stupně N, souradnice[1] - minuty N 
        double stupne = Double.valueOf(souradnice[0].substring(1));
        double minuty = Double.valueOf(souradnice[1]);
        return stupne + minuty / 60;
    }
    
    
    @Override
    public String toString(){
        return id + ", název: " + nazev + ", GPS: " + gps;
    }

    @Override
    public int compareTo(Zamek arg0) {
        if(vzdalenost < arg0.vzdalenost)
            return -1;
        else if(vzdalenost > arg0.vzdalenost)
            return 1;
        else
            return 0;
    }
    
}
