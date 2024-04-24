package Rozdelenia;

public class Deterministicke implements IRozdelenie{
    private final double pravdepodobnost;
    public Deterministicke(double pravdepodobnost) {
        this.pravdepodobnost = pravdepodobnost;
    }

    public double sample() {
        return this.pravdepodobnost;
    }
}
