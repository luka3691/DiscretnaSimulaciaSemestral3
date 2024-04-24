package Objects;

import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.OsobaComparatorNoPriority;
import MonteCarlo.Rozdelenia.SpojiteRovnomerne;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Pokladne {
    private boolean[] pokladne;
    private PriorityQueue<Osoba>[] rady;
    private int pocetPokladni;
    public Pokladne(int pocetPokladni) {
        this.pocetPokladni = pocetPokladni;
        pokladne = new boolean[pocetPokladni];
        rady = (PriorityQueue<Osoba>[]) new PriorityQueue[pocetPokladni];
        for (int i = 0; i < pocetPokladni; i++) {
            rady[i] = new PriorityQueue<>(new OsobaComparatorNoPriority());
        }
        for (int i = 0; i < pocetPokladni; i++) {
            pokladne[i] = true;
        }
    }

    public int getIDPokladne(SpojiteRovnomerne random) {
        int pocetPrazdnych = 0;
        int prazdnaPokladna = 0;
        for (int i = 0; i < pocetPokladni; i++) {
            if (pokladne[i]) {
                pocetPrazdnych++;
                prazdnaPokladna = i;
            }
        }
        if (pocetPrazdnych == 0) {
            return -1;
        } else if (pocetPrazdnych == 1) {
            return prazdnaPokladna;
        } else {
            return this.zaradNahodneDoPokladne(random);
        }
    }
    public int zaradNahodneDoPokladne(SpojiteRovnomerne random) {
        ArrayList<Integer> prazdnePokladne = new ArrayList<>();
        for (int i = 0; i < pocetPokladni; i++) {
            if (pokladne[i]) {
                prazdnePokladne.add(i);
            }
        }
        double pravdepodobnost = random.sample();
        double krok = krok = 1.0 / prazdnePokladne.size();;
        double prav = krok;
        int poradieVPrazdnych = 0;
        while (prav <= 1.0) {
            if (pravdepodobnost < prav) {
                return prazdnePokladne.get(poradieVPrazdnych);
            }
            poradieVPrazdnych++;
            prav+= krok;
        }
        return poradieVPrazdnych;
    }
    public int getIDNajmensiehoRadu(SpojiteRovnomerne random) {
        int idNajmensiRad = 0;
        int pocetVNajmensomRade = Integer.MAX_VALUE;
        int pocetRovankychRadov = 1;
        for (int i = 0; i < pocetPokladni; i++) {
            if (rady[i].size() < pocetVNajmensomRade) {
                idNajmensiRad = i;
                pocetRovankychRadov = 1;
                pocetVNajmensomRade = rady[i].size();
            } else if (rady[i].size() == pocetVNajmensomRade) {
                pocetRovankychRadov++;
            }
        }
        if (pocetRovankychRadov >= 2) {
            return zaradNahodneDoRadu(random);
        } else {
            return idNajmensiRad;
        }
    }

    public int zaradNahodneDoRadu(SpojiteRovnomerne random) {
        ArrayList<Integer> prazdneRady = new ArrayList<>();
        int pocetVNajmensomRade = Integer.MAX_VALUE;
        for (int i = 0; i < pocetPokladni; i++) {
            if (rady[i].size() < pocetVNajmensomRade) {
                prazdneRady.clear();
                prazdneRady.add(i);
                pocetVNajmensomRade = rady[i].size();
            } else if (rady[i].size() == pocetVNajmensomRade) {
                prazdneRady.add(i);
            }
        }
        double pravdepodobnost = random.sample();
        double krok = 1.0 / prazdneRady.size();
        double prav = krok;
        int poradieVPrazdnych = 0;
        while (prav <= 1.0) {
            if (pravdepodobnost < prav) {
                return prazdneRady.get(poradieVPrazdnych);
            }
            poradieVPrazdnych++;
            prav+= krok;
        }
        return poradieVPrazdnych;
    }

    public boolean[] getPokladne() {
        return pokladne;
    }

    public PriorityQueue<Osoba>[] getRady() {
        return rady;
    }
}
