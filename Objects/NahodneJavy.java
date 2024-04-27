package Objects;

import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Rozdelenia.*;

import java.util.Arrays;
import java.util.Random;

public class NahodneJavy {

    private Exponencialne prichodLudi;
    private SpojiteRovnomerne typZakaznikaGenerator;
    private SpojiteRovnomerne casZadavaniaDoAutomatu;
    private SpojiteRovnomerne casNaNadiktovanieObjednavky;
    private Triangularne casNaOdovzadnieOnlineTovaru;
    private SpojiteRovnomerne nechaTovarNaObsluznom;
    private SpojiteRovnomerne spatnePrevzatieVelkehoTovaru;
    private SpojiteEmpiricke casPripravaJednoduchej;
    private SpojiteRovnomerne casPripravaMierneZlozitej;
    private SpojiteEmpiricke casPripravaZlozitej;
    private SpojiteRovnomerne generovanieZlozitosti;
    private SpojiteRovnomerne generovanieTypuPlatby;
    private DiskretneRovnomerne trvaniePlatbyHotovost;
    private DiskretneRovnomerne trvaniePlatbyKrata;
    private SpojiteRovnomerne nahodnePostavanieDoRadu;
    private SpojiteRovnomerne nahodnePostavenieDoPokladne;



    public NahodneJavy() {

        //prichodLudi = new Exponencialne((double)60/30);
        //typZakaznikaGenerator = new SpojiteRovnomerne(0.0, 1.0);
        //casZadavaniaDoAutomatu = new SpojiteRovnomerne(30.0/60, 120.0/60);
        //casNaNadiktovanieObjednavky = new SpojiteRovnomerne(60.0/60, 900.0/60);
        //casNaOdovzadnieOnlineTovaru = new Triangularne(60.0/60, 480.0/60, 120.0/60);
        //nechaTovarNaObsluznom = new SpojiteRovnomerne(0.0, 1.0);
        spatnePrevzatieVelkehoTovaru = new SpojiteRovnomerne(30.0/60, 70.0/60);

        generovanieZlozitosti = new SpojiteRovnomerne(0.0, 1.0);
        casPripravaJednoduchej = new SpojiteEmpiricke(Arrays.asList(new Double[]{2.0, 5.0}, new Double[]{5.0, 9.0}), Arrays.asList( 0.6, 0.4));
        casPripravaMierneZlozitej = new SpojiteRovnomerne(9.0, 11.0);
        casPripravaZlozitej = new SpojiteEmpiricke(Arrays.asList(new Double[]{11.0, 12.0}, new Double[]{12.0, 20.0}, new Double[]{20.0, 25.0}), Arrays.asList( 0.1, 0.6, 0.3));

        //generovanieTypuPlatby = new SpojiteRovnomerne(0.0, 1.0);
        //trvaniePlatbyHotovost = new DiskretneRovnomerne(180/60, 480/60);
        //trvaniePlatbyKrata = new DiskretneRovnomerne(180/60, 360/60);

        nahodnePostavanieDoRadu = new SpojiteRovnomerne(0.0, 1.0);
        nahodnePostavenieDoPokladne = new SpojiteRovnomerne(0.0, 1.0);
    }
    public NahodneJavy(Random random) {

        prichodLudi = new Exponencialne((double)60/30, random.nextInt());
        typZakaznikaGenerator = new SpojiteRovnomerne(0.0, 1.0, random.nextInt());
        casZadavaniaDoAutomatu = new SpojiteRovnomerne(30.0/60, 120.0/60, random.nextInt());
        casNaNadiktovanieObjednavky = new SpojiteRovnomerne(60.0/60, 900.0/60, random.nextInt());
        casNaOdovzadnieOnlineTovaru = new Triangularne(60.0/60, 480.0/60, 120.0/60, random.nextInt());
        nechaTovarNaObsluznom = new SpojiteRovnomerne(0.0, 1.0, random.nextInt());
        spatnePrevzatieVelkehoTovaru = new SpojiteRovnomerne(30.0/60, 70.0/60, random.nextInt());

        generovanieZlozitosti = new SpojiteRovnomerne(0.0, 1.0, random.nextInt());
        casPripravaJednoduchej = new SpojiteEmpiricke(Arrays.asList(new Double[]{2.0, 5.0}, new Double[]{5.0, 9.0}), Arrays.asList( 0.6, 0.4), random.nextInt());
        casPripravaMierneZlozitej = new SpojiteRovnomerne(9.0, 11.0, random.nextInt());
        casPripravaZlozitej = new SpojiteEmpiricke(Arrays.asList(new Double[]{11.0, 12.0}, new Double[]{12.0, 20.0}, new Double[]{20.0, 25.0}), Arrays.asList( 0.1, 0.6, 0.3), random.nextInt());

        generovanieTypuPlatby = new SpojiteRovnomerne(0.0, 1.0, random.nextInt());
        trvaniePlatbyHotovost = new DiskretneRovnomerne(180/60, 480/60, random.nextInt());
        trvaniePlatbyKrata = new DiskretneRovnomerne(180/60, 360/60, random.nextInt());

        nahodnePostavanieDoRadu = new SpojiteRovnomerne(0.0, 1.0, random.nextInt());
        nahodnePostavenieDoPokladne = new SpojiteRovnomerne(0.0, 1.0, random.nextInt());
    }

    public TypZakaznika getTypZakaznika() {
        double p = typZakaznikaGenerator.sample();
        if (p < 0.5) {
            return TypZakaznika.BEZNY;
        } else if (p < 0.65) {
            return TypZakaznika.ZMLUVNY;
        } else {
            return TypZakaznika.ONLINE;
        }
    }

    public double getPrichodLudi() {
        return prichodLudi.sample();
    }
    public double getCasZadavaniaDoAutomatu() {
        return casZadavaniaDoAutomatu.sample();
    }

    public double getCasNaNadiktovanieObjednavky() {
        return casNaNadiktovanieObjednavky.sample();
    }

    public boolean getNechaTovarNaObsluznom() {
        return nechaTovarNaObsluznom.sample() < 0.6;
    }

    public double getTravnieObjednavky() {
        double typObjednavky = generovanieZlozitosti.sample();
        if (typObjednavky < 0.3) {
            return casPripravaJednoduchej.sample();
        } else if (typObjednavky < 0.7) {
            return casPripravaMierneZlozitej.sample();
        } else {
            return casPripravaZlozitej.sample();
        }
    }

    public double getTravniePlatby() {
        double typPlatby = generovanieTypuPlatby.sample();
        if (typPlatby < 0.4) {
            return trvaniePlatbyHotovost.sample();
        } else {
            return trvaniePlatbyKrata.sample();
        }
    }

    public double getCasNaPripravenieOnline() {
        return casNaOdovzadnieOnlineTovaru.sample();
    }

    public SpojiteRovnomerne getNahodnePostavanieDoRadu() {
        return nahodnePostavanieDoRadu;
    }

    public SpojiteRovnomerne getNahodnePostavenieDoPokladne() {
        return nahodnePostavenieDoPokladne;
    }

    public double getSpatnePrevzatieTovaru() {
        return spatnePrevzatieVelkehoTovaru.sample();
    }
}
