package domain.environment;
/** Ejecutor compartible de oscilación corporal: frío, hidromiel y descarga del Lanza Arcos difieren en causa, no en representación. */
public final class PhysiologicalTremorPolicy {
 public enum Source{BITING_FROST,MEAD,ARC_LAUNCHER}
 public record Tremor(boolean active,double frequencyHz,double amplitude){}
 public Tremor resolve(Source s){return switch(s){case BITING_FROST->new Tremor(true,8.0,0.35);case MEAD->new Tremor(true,5.0,0.20);case ARC_LAUNCHER->new Tremor(true,10.0,0.45);};}
}
