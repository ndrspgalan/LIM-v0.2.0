package domain.ability;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Objects;

/** Contacto eléctrico pasivo, bidireccional y no anulable por i-frames de MIRAGE. */
public final class ElectrogenesisPolicy {
    public ElectrogenesisResult resolveUnarmedContact(CharacterSheet source, boolean unarmedContact) {
        Objects.requireNonNull(source, "La hoja no puede ser nula.");
        if (!unarmedContact) return ElectrogenesisResult.none();
        int intensity = EvolutiveIntensityPolicy.intensity(source.valueOf(Attribute.VITALIDAD));
        if (intensity == 0) return ElectrogenesisResult.none();
        return new ElectrogenesisResult(intensity, true);
    }

    /** CUSTODIA y ANULACIÓN FUNDACIONAL prestan su radio; ELECTROGÉNESIS no crea uno propio. */
    public double inheritedRadialRangeMeters(CharacterSheet source, double activeFieldRadiusMeters) {
        Objects.requireNonNull(source, "La hoja no puede ser nula.");
        if (source.valueOf(Attribute.VITALIDAD) < EvolutiveIntensityPolicy.MINIMUM_ATTRIBUTE) return 0.0;
        if (!Double.isFinite(activeFieldRadiusMeters) || activeFieldRadiusMeters < 0) {
            throw new IllegalArgumentException("El radio debe ser finito y no negativo.");
        }
        return activeFieldRadiusMeters;
    }
}
