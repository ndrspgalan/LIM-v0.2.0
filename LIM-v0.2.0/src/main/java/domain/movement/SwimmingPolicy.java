package domain.movement;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;
import java.util.Objects;

/**  — natación normal, brazadas rápidas y buceo. */
public final class SwimmingPolicy {
    public static final double DIVING_STAMINA_COST_PER_SECOND = 1.0;
    private final ExplorationTechniqueUnlockPolicy unlockPolicy = new ExplorationTechniqueUnlockPolicy();
    private final LocomotionStaminaPolicy stamina = new LocomotionStaminaPolicy();
    private final LocomotionDistancePolicy distance = new LocomotionDistancePolicy();

    public boolean canSwim(CharacterSheet sheet) { Objects.requireNonNull(sheet); return unlockPolicy.isUnlocked(ExplorationTechnique.SWIM, sheet); }
    public boolean canSwim(CharacterSheet sheet, EquipmentState equipment) { Objects.requireNonNull(equipment); return canSwim(sheet) && new ArmorMobilityRestrictionPolicy().allowsSwimming(equipment); }

    public double speedMetersPerSecond(LocomotionMode mode, Gender gender, double heightMeters) {
        if (mode != LocomotionMode.SWIMMING && mode != LocomotionMode.FAST_SWIMMING && mode != LocomotionMode.DIVING)
            throw new IllegalArgumentException("Modo acuático requerido.");
        return distance.metersPerSecond(mode, gender, heightMeters);
    }

    public double staminaCostPerSecond(LocomotionMode mode, Gender gender, CharacterSheet sheet, double maximumStamina) {
        return switch (Objects.requireNonNull(mode)) {
            case SWIMMING -> 0.0;
            case FAST_SWIMMING -> stamina.fastSwimmingCostPerSecond(gender, sheet, maximumStamina);
            case DIVING -> DIVING_STAMINA_COST_PER_SECOND;
            default -> throw new IllegalArgumentException("Modo acuático requerido.");
        };
    }

    public SwimmingTickResult tickDiving(double currentStamina, double elapsedSeconds, CharacterSheet sheet, EquipmentState equipment) {
        if (!canSwim(sheet,equipment)) throw new IllegalStateException("El equipamiento o la hoja impiden nadar/bucear.");
        return consumeFixed(currentStamina, elapsedSeconds, SwimmingState.DIVING);
    }

    /** Forma compacta: fuera de fondo representa buceo/agotamiento fijo 1 PA/s. */
    public SwimmingTickResult tick(double currentStamina, double elapsedSeconds, boolean canTouchBottom, CharacterSheet sheet, EquipmentState equipment) {
        Objects.requireNonNull(equipment); if(!canTouchBottom && !canSwim(sheet,equipment)) throw new IllegalStateException("El equipamiento actual impide nadar.");
        return canTouchBottom ? grounded(currentStamina) : consumeFixed(currentStamina,elapsedSeconds,SwimmingState.SWIMMING);
    }
    public SwimmingTickResult tick(double currentStamina, double elapsedSeconds, boolean canTouchBottom, CharacterSheet sheet) {
        Objects.requireNonNull(sheet); if(!canTouchBottom && !canSwim(sheet)) throw new IllegalStateException("El personaje no ha desbloqueado la técnica de nadar.");
        return canTouchBottom ? grounded(currentStamina) : consumeFixed(currentStamina,elapsedSeconds,SwimmingState.SWIMMING);
    }
    public SwimmingTickResult tick(double currentStamina, double elapsedSeconds, boolean canTouchBottom) {
        return canTouchBottom ? grounded(currentStamina) : consumeFixed(currentStamina,elapsedSeconds,SwimmingState.SWIMMING);
    }
    private SwimmingTickResult grounded(double pa){ validate(pa,0); return new SwimmingTickResult(SwimmingState.GROUNDED,pa,pa,0); }
    private SwimmingTickResult consumeFixed(double pa,double seconds,SwimmingState active){
        validate(pa,seconds); if(pa==0) return new SwimmingTickResult(SwimmingState.DEAD_BY_DROWNING,0,0,0);
        double consumed=Math.min(pa,seconds*DIVING_STAMINA_COST_PER_SECOND), remaining=pa-consumed;
        return new SwimmingTickResult(remaining==0?SwimmingState.DEAD_BY_DROWNING:active,pa,remaining,consumed);
    }
    private static void validate(double pa,double seconds){ if(!Double.isFinite(pa)||pa<0||!Double.isFinite(seconds)||seconds<0) throw new IllegalArgumentException("PA/tiempo inválidos."); }
}
