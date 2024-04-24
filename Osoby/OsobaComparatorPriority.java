package Osoby;

import java.util.Comparator;

public class OsobaComparatorPriority implements Comparator<Osoba> {
    @Override
    public int compare(Osoba o1, Osoba o2) {
        int stavComparison = o1.getTypZakaznika().compareTo(o2.getTypZakaznika());
        if (stavComparison != 0) {
            return stavComparison;
        } else {
            if (o1.getCasPrichodu() < o2.getCasPrichodu()) {
                return -1;
            } else if (o1.getCasPrichodu() > o2.getCasPrichodu()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
