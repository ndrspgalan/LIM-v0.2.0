package domain.movement;

import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;

import java.util.EnumSet;
import java.util.Objects;

/**
 * Política ambiental de locomoción determinada por el ángulo y la propiedad escalable.
 * La sobrecarga con hoja de personaje integra la capacidad efectiva de escalada.
 */
public final class LocomotionPolicy {
    public LocomotionProfile resolve(TerrainSurface surface) {
        if (surface == null) throw new IllegalArgumentException("La superficie no puede ser nula.");
        double slope = surface.slopeDegrees();

        if (slope <= 9.0) {
            return new LocomotionProfile(SlopeBand.RUN_ALLOWED,
                    EnumSet.of(LocomotionMode.RUNNING, LocomotionMode.TROTTING,
                            LocomotionMode.WALKING, LocomotionMode.CROUCH_WALKING,
                            LocomotionMode.CRAWLING));
        }
        if (slope <= 14.0) {
            return new LocomotionProfile(SlopeBand.TROT_MAXIMUM,
                    EnumSet.of(LocomotionMode.TROTTING, LocomotionMode.WALKING,
                            LocomotionMode.CROUCH_WALKING, LocomotionMode.CRAWLING));
        }
        if (slope < ClimbingPolicy.MINIMUM_CLIMB_ANGLE_DEGREES) {
            return new LocomotionProfile(SlopeBand.WALK_MAXIMUM,
                    EnumSet.of(LocomotionMode.WALKING, LocomotionMode.CROUCH_WALKING,
                            LocomotionMode.CRAWLING));
        }
        if (slope <= ClimbingPolicy.ABSOLUTE_MAXIMUM_CLIMB_ANGLE_DEGREES) {
            return new LocomotionProfile(SlopeBand.CLIMB_REQUIRED,
                    surface.climbable() ? EnumSet.of(LocomotionMode.CLIMBING) : EnumSet.noneOf(LocomotionMode.class));
        }
        return new LocomotionProfile(SlopeBand.IMPASSABLE, EnumSet.noneOf(LocomotionMode.class));
    }

    public LocomotionProfile resolve(TerrainSurface surface, CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        LocomotionProfile environmental = resolve(surface);
        if (environmental.slopeBand() != SlopeBand.CLIMB_REQUIRED) {
            return environmental;
        }
        boolean canClimb = new ClimbingPolicy().canClimb(sheet, surface);
        return new LocomotionProfile(SlopeBand.CLIMB_REQUIRED,
                canClimb ? EnumSet.of(LocomotionMode.CLIMBING) : EnumSet.noneOf(LocomotionMode.class));
    }
    /** filtra la locomoción ambiental por restricciones de la armadura equipada. */
    public LocomotionProfile resolve(TerrainSurface surface, CharacterSheet sheet, EquipmentState equipment) {
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        LocomotionProfile base = resolve(surface, sheet);
        ArmorMobilityRestrictionPolicy armor = new ArmorMobilityRestrictionPolicy();
        EnumSet<LocomotionMode> allowed = EnumSet.noneOf(LocomotionMode.class);
        for (LocomotionMode mode : base.allowedModes()) if (armor.allows(equipment, mode)) allowed.add(mode);
        return new LocomotionProfile(base.slopeBand(), allowed);
    }

}
