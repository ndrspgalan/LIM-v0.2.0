package domain.combat.stamina;
/** OPTIMIZACIÓN HELICOIDAL neutraliza cualquier fuente que intente inhibir PA REGEN; no borra modificadores de velocidad/latencia. */
public final class StaminaRegenerationInhibitionPolicy {
 public boolean inhibited(boolean sourceInhibits,boolean optimizationHelicoidalActive){return sourceInhibits&&!optimizationHelicoidalActive;}
 public boolean canRegenerate(boolean sourceInhibits,boolean optimizationHelicoidalActive){return !inhibited(sourceInhibits,optimizationHelicoidalActive);}
}
