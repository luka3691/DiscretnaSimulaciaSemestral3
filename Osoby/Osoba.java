package Osoby;

import OSPABA.Entity;
import OSPABA.Simulation;
import OSPRNG.UniformContinuousRNG;
import simulation.MySimulation;

import java.util.ArrayList;

public class Osoba extends Entity {
    private UniformContinuousRNG typZakaznikaGenerator = new UniformContinuousRNG(0.0, 1.0);
    private UniformContinuousRNG nechaTovarNaObsluznom = new UniformContinuousRNG(0.0, 1.0);
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
        this.typZakaznika = this.generateTypZakaznika();
        this.nechalTovarNaVydajni = this.generateNechaToavrNaObsluznom();

    }
    private boolean generateNechaToavrNaObsluznom() {
        double p = nechaTovarNaObsluznom.sample();
        if (p < 0.6) {
            return true;
        }else {
            return false;
        }
    }

    private TypZakaznika generateTypZakaznika() {
        double p = typZakaznikaGenerator.sample();
        if (p < 0.5) {
            return TypZakaznika.BEZNY;
        } else if (p < 0.65) {
            return TypZakaznika.ZMLUVNY;
        } else {
            return TypZakaznika.ONLINE;
        }
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
