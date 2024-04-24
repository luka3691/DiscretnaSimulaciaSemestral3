package Rozdelenia;

import java.util.Random;

public class DiskretneRovnomerne implements IRozdelenie{
    private Random random;
    private final int min;
    private final int max;
    private int seed = 1;
    public DiskretneRovnomerne(int min, int max) {
        this.min = min;
        this.max = max;
        this.random = new Random(this.seed);
    }

    public DiskretneRovnomerne(int min, int max, int seed) {
        this.min = min;
        this.max = max;
        this.random = new Random(seed);
    }

    public double sample() {
        return this.random.nextInt(this.max - this.min + 1) + this.min ;
    }
}
