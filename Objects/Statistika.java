package Objects;

public class Statistika {

    private int pocetZaznamov;
    private double maxVaha;
    private double maxVahaSquared;
    private double celkovaSumaVazenychZaznamov;
    private double poslednyCasZaznamu;
    private double poslednaDlzkaRadu;

    private final boolean isWeighted;

    public Statistika(boolean isWeighted) {
        this.isWeighted = isWeighted;
        this.pocetZaznamov = 0;
        this.maxVaha = 0;
        this.maxVahaSquared = 0;
        this.celkovaSumaVazenychZaznamov = 0;
    }

    public void pridajZaznam(double hodnota) {
        maxVaha += hodnota;
        maxVahaSquared += Math.pow(hodnota, 2);
        pocetZaznamov++;
    }

    public void pridajZaznam(double hodnota, double vaha) {
        double tempVaha = vaha - (9*60);
        if (maxVaha < tempVaha) {
            maxVaha = tempVaha;
        }
        pocetZaznamov++;
        if (poslednyCasZaznamu != 0) {
            celkovaSumaVazenychZaznamov += poslednaDlzkaRadu * (tempVaha - poslednyCasZaznamu);
        }
        poslednaDlzkaRadu = hodnota;
        poslednyCasZaznamu = tempVaha;
    }
    public double vypocitaj() {
        if (isWeighted) {
            if (maxVaha == 0) {
                return 0.0;
            }
            return celkovaSumaVazenychZaznamov / maxVaha ;
        } else {
            if (pocetZaznamov == 0) {
                return 0;
            }
            return maxVaha / pocetZaznamov;
        }

    }

    public double[] getIntervalSpolahlivosti() {
        double s = Math.sqrt((maxVahaSquared - (Math.pow(maxVaha,2)/pocetZaznamov))/(pocetZaznamov-1));
        double priemer = vypocitaj();
        double rozdiel = (s * 1.96) / Math.sqrt(pocetZaznamov);
        return new double[]{priemer - rozdiel, priemer + rozdiel};
    }

    public double getVytazenie(double trvanieSim) {
        return maxVaha / trvanieSim;
    }

}
