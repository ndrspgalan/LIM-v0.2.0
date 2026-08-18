package domain.bestiarium.interstice.transcended;

/**
 * Valor discreto y reproducible de una ley: 0..10 representa 0.0..1.0.
 * Cinco es neutralidad absoluta; la magnitud conserva inercia/historial, pero las oportunidades sólo ven la tendencia.
 */
public record TranscendedValue(int step) {
    public static final int MIN_STEP=0;
    public static final int NEUTRAL_STEP=5;
    public static final int MAX_STEP=10;

    public TranscendedValue {
        if(step<MIN_STEP||step>MAX_STEP) throw new IllegalArgumentException("El valor TRANSCENDED debe estar entre 0 y 10.");
    }

    public static TranscendedValue neutral(){return new TranscendedValue(NEUTRAL_STEP);}
    public double normalized(){return step/10.0;}
    public TranscendedTendency tendency(){
        if(step<NEUTRAL_STEP)return TranscendedTendency.POLE_ZERO;
        if(step>NEUTRAL_STEP)return TranscendedTendency.POLE_ONE;
        return TranscendedTendency.NEUTRAL;
    }
    public TranscendedValue shifted(TranscendedShift shift){
        int next=Math.max(MIN_STEP,Math.min(MAX_STEP,step+java.util.Objects.requireNonNull(shift).steps()));
        return new TranscendedValue(next);
    }
}
