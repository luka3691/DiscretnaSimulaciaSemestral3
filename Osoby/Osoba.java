package Osoby;

import OSPABA.Entity;
import OSPABA.Simulation;

import java.util.ArrayList;

public class Osoba extends Entity {
    private double casPrichodu;
    private StavyOsoby stav;
    private int ID;
    private boolean nechalTovarNaVydajni = false;

    private TypZakaznika typZakaznika;

    private int idPokladne;
    private int idObsluzneho;



    public Osoba(Simulation sim) {
        this.stav = stav;
        this.casPrichodu = casPrichodu;
        this.ID = ID;
        this.typZakaznika = typZakaznika;
    }

    public void setStav(StavyOsoby stav) {
        this.stav = stav;
    }

    public double getCasPrichodu() {
        return casPrichodu;
    }

    public boolean isNechalTovarNaVydajni() {
        return nechalTovarNaVydajni;
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
        return ID;
    }
    public ArrayList<String> toArray() {
        ArrayList<String> infoOZakaz = new ArrayList<>() ;
        infoOZakaz.add(String.valueOf(ID));
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
