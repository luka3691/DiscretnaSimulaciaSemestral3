package Osoby;

import java.util.Comparator;

public class OsobaComparatorNoPriority implements Comparator<Osoba> {
    @Override
    public int compare(Osoba o1, Osoba o2) {
        if (o1.getCasPrichodu() < o2.getCasPrichodu()) {
            return -1;
        } else if (o1.getCasPrichodu() > o2.getCasPrichodu()) {
            return 1;
        } else {
            return 0;
        }
    }
}
