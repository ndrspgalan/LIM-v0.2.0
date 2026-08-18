package domain.combat.ai.state;

/** Sustituye el presupuesto por confianza: decide con PA reales, reservas y drenajes sostenidos. */
public final class StaminaCommitmentPolicy {
    public record Commitment(double current,double maximum,double reserved,double sustainedDrainPerSecond){
        public Commitment{
            if(maximum<=0||current<0||current>maximum||reserved<0||sustainedDrainPerSecond<0) throw new IllegalArgumentException("Compromiso de PA inválido.");
        }
        public double immediatelyAvailable(){return Math.max(0.0,current-reserved);}
        public double projectedAvailable(double seconds){if(seconds<0)throw new IllegalArgumentException("Tiempo inválido.");return Math.max(0.0,immediatelyAvailable()-sustainedDrainPerSecond*seconds);}
    }
    public Commitment resolve(double current,double maximum,double reserved,double drain){return new Commitment(current,maximum,Math.min(maximum,reserved),drain);}
    public boolean canAfford(Commitment c,double immediateCost,double horizonSeconds){
        if(immediateCost<0)throw new IllegalArgumentException("Coste inválido.");
        return c.projectedAvailable(horizonSeconds)+1e-9>=immediateCost;
    }
}
