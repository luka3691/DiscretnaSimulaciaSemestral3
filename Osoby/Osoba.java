package Osoby;

import OSPABA.Entity;
import OSPABA.Simulation;
import OSPRNG.UniformContinuousRNG;
import simulation.MySimulation;

import java.util.ArrayList;

public class Osoba extends Entity {

    private double casPrichodu;
    private StavyOsoby stav;

    private boolean nechalTovarNaVydajni;

    private TypZakaznika typZakaznika;

    private int idPokladne;
    private int idObsluzneho;



    public Osoba(Simulation sim) {
        super(sim);

        this.stav = StavyOsoby.PRICHOD;
        this.casPrichodu = sim.currentTime();

    }


    public void setStav(StavyOsoby stav) {
        this.stav = stav;
        ((MySimulation)mySim()).setStavyOsob(this.toArray());
    }

    public double getCasPrichodu() {
        return casPrichodu;
    }

    public boolean isNechalTovarNaVydajni() {
        return nechalTovarNaVydajni;
    }

    public void setNechalTovarNaVydajni(boolean nechalTovarNaVydajni) {
        this.nechalTovarNaVydajni = nechalTovarNaVydajni;
    }

    public TypZakaznika getTypZakaznika() {
        return typZakaznika;
    }

    public int getIdPokladne() {
        return idPokladne;
    }

    public void setIdPokladne(int idPokladne) {
        this.idPokladne = idPokladne;
    }

    public int getIdObsluzneho() {
        return idObsluzneho;
    }

    public void setIdObsluzneho(int idObsluzneho) {
        this.idObsluzneho = idObsluzneho;
    }


    public void setNadrozmernaObjednavka(boolean nadrozmernaObjednavka) {
        this.nechalTovarNaVydajni = nadrozmernaObjednavka;
    }

    public void nechalTovarNaVydajni(double random) {
        this.nechalTovarNaVydajni = random < 0.6;
    }

    public void setTypZakaznika(double random) {
        if (random < 0.5) {
            this.typZakaznika = TypZakaznika.BEZNY;
        } else if (random < 0.65) {
            this.typZakaznika = TypZakaznika.ZMLUVNY;
        } else {
            this.typZakaznika = TypZakaznika.ONLINE;
        }
    }
    public void setTypZakaznika(TypZakaznika typ) {
        this.typZakaznika = typ;
    }

    public int getID() {
        return super._id;
    }
    public ArrayList<String> toArray() {
        ArrayList<String> infoOZakaz = new ArrayList<>() ;
        infoOZakaz.add(String.valueOf(super._id));
        infoOZakaz.add(String.valueOf(typZakaznika));
        if (stav == StavyOsoby.V_RADE_PRED_AUTOMATOM) {
            infoOZakaz.add("Pred automatom");
        } else if (stav == StavyOsoby.ZADAVANIE_DO_AUTOMATU) {
            infoOZakaz.add("Zdava do automatu");
        } else if (stav == StavyOsoby.V_RADE_PRED_OSBLUHOU) {
            infoOZakaz.add("V rade pred obsluhov");
        } else if (stav == StavyOsoby.JE_OBSLUHOVANY) {
            infoOZakaz.add("Obsluhovany u :" + idObsluzneho);
        } else if (stav == StavyOsoby.V_RADE_PRED_POKLADNOU) {
            infoOZakaz.add("V rade pred pokladnou :" + idPokladne);
        } else if (stav == StavyOsoby.JE_OBSLUHOVANY_V_POKLADNI) {
            infoOZakaz.add("Platba u pokladne :" + idPokladne);
        } else if (stav == StavyOsoby.ODCHADZA) {
            infoOZakaz.add("Odišiel");
        } else if (stav == StavyOsoby.IDE_SI_PRE_NADROZMERNY_TOVAR) {
            infoOZakaz.add("Spätné prevzatie nadrozmerného");
        } else {
            infoOZakaz.add("Koniec zadavania do automatu");
        }
        return infoOZakaz;
    }

}
