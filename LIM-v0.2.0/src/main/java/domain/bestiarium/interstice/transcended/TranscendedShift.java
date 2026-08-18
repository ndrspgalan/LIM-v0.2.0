package domain.bestiarium.interstice.transcended;

/** Un único hecho causal sólo puede desplazar una ley una décima, dejarla neutra o retraerla una décima. */
public enum TranscendedShift {
    TOWARD_ZERO(-1),
    NEUTRAL(0),
    TOWARD_ONE(1);

    private final int steps;
    TranscendedShift(int steps){this.steps=steps;}
    public int steps(){return steps;}
}
