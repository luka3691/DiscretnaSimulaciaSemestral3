package Rozdelenia;

import java.util.Random;

public class SpojiteRovnomerne implements IRozdelenie{
    private Random random;
    private final double min;
    private final double max;
    private int seed = 1 ;

    public SpojiteRovnomerne(double min, double max) {
        this.min = min;
        this.max = max;
        this.random = new Random(seed);
    }
    public SpojiteRovnomerne(double min, double max, int seed) {
        this.min = min;
        this.max = max;
        this.random = new Random(seed);
    }

    public double sample() {
        return this.random.nextDouble() * ( this.max - this.min ) + this.min;
    }
}