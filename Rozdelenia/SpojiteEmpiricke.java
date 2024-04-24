package Rozdelenia;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpojiteEmpiricke implements IRozdelenie{
    private Random random;
    private List<Double[]> hranice;
    private List<Double> kumutativnePravdepodobnosti;
    private int seed = 1;

    public SpojiteEmpiricke(List<Double[]> hranice, List<Double> pravdepodobnosti) {
        this.random = new Random(this.seed);
        this.hranice = hranice;
        this.kumutativnePravdepodobnosti = new ArrayList<>();

        double kumutativnaPravdepodobnost = 0.0;
        for (Double aDouble : pravdepodobnosti) {
            kumutativnaPravdepodobnost += aDouble;
            this.kumutativnePravdepodobnosti.add(kumutativnaPravdepodobnost);
        }
    }

    public SpojiteEmpiricke(List<Double[]> hranice, List<Double> pravdepodobnosti, int seed) {
        this.random = new Random(seed);
        this.hranice = hranice;
        this.kumutativnePravdepodobnosti = new ArrayList<>();

        double kumutativnaPravdepodobnost = 0.0;
        for (Double aDouble : pravdepodobnosti) {
            kumutativnaPravdepodobnost += aDouble;
            this.kumutativnePravdepodobnosti.add(kumutativnaPravdepodobnost);
        }
    }

    private boolean skontrolujPravdepodobnosti(List<Double> pravdepodobnosti) {
        double sum = 0.0;
        for (double cislo : pravdepodobnosti) {
            if (cislo <= 0.0) {
                return false;
            }
            sum += cislo;
        }
        return sum == 1.0;
    }

    public double sample() {
        double nahodneCislo = this.random.nextDouble();
        int indexIntervalu = 0;
        //porovnávanie vygenerovanej pravdepodobnosti s komutatívnymi pravdepobnostami
        //vdaka tomuto ziskame hranice z ktorych sa ma vygenerovat urok
        for (int i = 0; i < this.kumutativnePravdepodobnosti.size(); i++) {
            if (nahodneCislo <= this.kumutativnePravdepodobnosti.get(i)) {
                indexIntervalu = i;
                break;
            }
        }
        Double[] vybranyInterval = this.hranice.get(indexIntervalu);
        return vybranyInterval[0] + (vybranyInterval[1] - vybranyInterval[0]) * this.random.nextDouble();
    }
}