package domain.ability;

/** Manifestación concreta seleccionable por los controles de maestrías. */
public record MasteryManifestation(MasteryId familyId, String familyName, String name, MasteryType type) {
    public MasteryManifestation {
        if (familyId == null || familyName == null || familyName.isBlank() || name == null || name.isBlank() || type == null) {
            throw new IllegalArgumentException("La manifestación de maestría debe estar completamente definida.");
        }
    }
}
