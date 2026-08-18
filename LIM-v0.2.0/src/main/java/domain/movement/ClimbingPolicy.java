package domain.movement;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Objects;

/**
 * La escalada se desbloquea con FUERZA 20 y DESTREZA 20.
 * Desde el mínimo canónico de 75 grados, FUERZA amplía linealmente
 * la pendiente máxima hasta 120 grados al alcanzar FUERZA 75.
 */
public final class ClimbingPolicy {
    public static final double MINIMUM_CLIMB_ANGLE_DEGREES = 75.0;
    public static final double ABSOLUTE_MAXIMUM_CLIMB_ANGLE_DEGREES = 120.0;

    private final ExplorationTechniqueUnlockPolicy unlockPolicy = new ExplorationTechniqueUnlockPolicy();

    public boolean canClimb(CharacterSheet sheet, TerrainSurface surface) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(surface, "La superficie no puede ser nula.");
        if (!surface.climbable() || !unlockPolicy.isUnlocked(ExplorationTechnique.CLIMB, sheet)) {
            return false;
        }
        double angle = surface.slopeDegrees();
        return angle >= MINIMUM_CLIMB_ANGLE_DEGREES && angle <= maximumClimbAngleDegrees(sheet);
    }

    public double maximumClimbAngleDegrees(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        if (!unlockPolicy.isUnlocked(ExplorationTechnique.CLIMB, sheet)) {
            return 0.0;
        }
        int strength = sheet.valueOf(Attribute.FUERZA);
        double progress = (strength - ExplorationTechniqueUnlockPolicy.CLIMB_STRENGTH_REQUIREMENT)
                / (double) (CharacterSheet.structuralMaximum(Attribute.FUERZA)
                - ExplorationTechniqueUnlockPolicy.CLIMB_STRENGTH_REQUIREMENT);
        return MINIMUM_CLIMB_ANGLE_DEGREES
                + progress * (ABSOLUTE_MAXIMUM_CLIMB_ANGLE_DEGREES - MINIMUM_CLIMB_ANGLE_DEGREES);
    }
}
