package Objects;

import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.OsobaComparatorNoPriority;
import MonteCarlo.Osoby.OsobaComparatorPriority;
import MonteCarlo.Osoby.TypZakaznika;

import java.util.PriorityQueue;
import java.util.Queue;

public class ObsluzneMiesta {
    private Queue<Osoba> osobyQueue;
    private Queue<Osoba> onlineQueue;
    private boolean[] normalneObsluzne;
    private boolean[] onlineObsluzne;
    private int pocetObsluznych;
    public ObsluzneMiesta(int pocetObsluznychMiest) {
        int pocetOnlineObsluznych =  pocetObsluznychMiest / 3;
        int poceNormalnychObsluznych = pocetObsluznychMiest - pocetOnlineObsluznych;
        normalneObsluzne = new boolean[poceNormalnychObsluznych];
        onlineObsluzne = new boolean[pocetOnlineObsluznych];
        for (int i = 0; i < poceNormalnychObsluznych; i++) {
            normalneObsluzne[i] = true;
        }
        for (int i = 0; i < pocetOnlineObsluznych; i++) {
            onlineObsluzne[i] = true;
        }
        osobyQueue = new PriorityQueue<>(new OsobaComparatorPriority());
        onlineQueue = new PriorityQueue<>(new OsobaComparatorNoPriority());
        this.pocetObsluznych = pocetObsluznychMiest;

    }

    public boolean zmestiSa(boolean jeNiektoVAutomate) {
        int plusJeden;
        if (jeNiektoVAutomate) {
            plusJeden = 1;
        } else {
            plusJeden = 0;
        }
        if (osobyQueue.size() + onlineQueue.size() + plusJeden <= 9) {
            return true;
        }
        return false;
    }

    public int getIDVolnaPokladna(Osoba osoba) {
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE){
            return getVolneOnline();
        } else {
            return getVolneNormalne();
        }
    }

    private int getVolneNormalne() {
        for (int i = 0; i < normalneObsluzne.length; i++) {
            if (normalneObsluzne[i]) {
                return i;
            }
        }
        return -1;
    }
    private int getVolneOnline() {
        for (int i = 0; i < onlineObsluzne.length; i++) {
            if (onlineObsluzne[i]) {
                return i;
            }
        }
        return -1;
    }



    public boolean[] getNormalneObsluzne() {
        return normalneObsluzne;
    }

    public boolean[] getOnlineObsluzne() {
        return onlineObsluzne;
    }

    public void zaradDoRadu(Osoba osoba) {
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            onlineQueue.add(osoba);
        } else {
            osobyQueue.add(osoba);
        }
    }

    public Queue<Osoba> getOsobyQueue() {
        return osobyQueue;
    }

    public Queue<Osoba> getOnlineQueue() {
        return onlineQueue;
    }
}
