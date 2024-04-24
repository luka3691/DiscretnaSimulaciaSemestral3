package Rozdelenia;

import java.util.Random;

public class Triangularne implements IRozdelenie{
    double min;
    double max;
    double stred;

    private int seed = 1 ;

    private Random random;
    public Triangularne(double min, double max, double stred) {
        this.max = max;
        this.min = min;
        this.stred = stred;
        this.random = new Random(seed);
    }

    public Triangularne(double min, double max, double stred, int seed) {
        this.max = max;
        this.min = min;
        this.stred = stred;
        this.seed = seed;
        this.random = new Random(seed);
    }

    public double sample() {
        double randomNumber =  random.nextDouble();
        if (randomNumber <= (stred - min) / (max - min)) {
            return min + Math.sqrt((max-min) * (stred-min) * randomNumber);
        } else {
            return max - Math.sqrt((max-min) * (max-stred) * (1.0 - randomNumber));
        }
    }
}
