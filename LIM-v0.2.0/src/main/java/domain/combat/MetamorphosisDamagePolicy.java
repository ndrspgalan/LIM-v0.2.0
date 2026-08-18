package domain.combat;
/** Conversión sostenida ; debe ejecutarse antes de resistencias e inmunidades. */
public final class MetamorphosisDamagePolicy {
 public DamageType transform(DamageType incoming,boolean active){if(!active)return incoming;return switch(incoming){case CURSE->DamageType.POISON;case POISON->DamageType.CURSE;default->incoming;};}
 public double magnitude(double incoming){if(!Double.isFinite(incoming)||incoming<0)throw new IllegalArgumentException("Daño inválido.");return incoming;}
}
