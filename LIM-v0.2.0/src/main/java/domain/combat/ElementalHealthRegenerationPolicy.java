package domain.combat;

import java.util.EnumSet;
import java.util.Objects;

/**
 * El daño directo de veneno, quemadura, congelación o electricidad inhibe PV REGEN
 * hasta que concluye el encuentro hostil.  añade una inhibición temporizada independiente
 * para efectos no elementales como el Huevo de Fósforo y Azufre.
 */
public final class ElementalHealthRegenerationPolicy {
    private static final EnumSet<DamageType> INHIBITORS = EnumSet.of(
            DamageType.POISON, DamageType.BURN, DamageType.FROST, DamageType.ELECTRICITY);
    private boolean inhibitedForEncounter;
    private double timedInhibitionSecondsRemaining;

    public void registerDirectDamage(DamageType type, double resolvedDamage, HostileEncounterState encounter) {
        Objects.requireNonNull(type, "El tipo de daño no puede ser nulo.");
        Objects.requireNonNull(encounter, "El encuentro no puede ser nulo.");
        if (resolvedDamage < 0) throw new IllegalArgumentException("El daño no puede ser negativo.");
        if (encounter.isActive() && resolvedDamage > 0 && INHIBITORS.contains(type)) inhibitedForEncounter = true;
    }

    /** daño ambiental continuo. Toxicidad Virulenta sólo drena PV; Quemadura Asfixiante además inhibe PV REGEN. */
    public void registerEnvironmentalDamage(domain.environment.EnvironmentalAdversity adversity, double resolvedDamage, HostileEncounterState encounter) {
        Objects.requireNonNull(adversity, "La adversidad no puede ser nula.");
        Objects.requireNonNull(encounter, "El encuentro no puede ser nulo.");
        if (resolvedDamage < 0) throw new IllegalArgumentException("El daño no puede ser negativo.");
        if (encounter.isActive() && resolvedDamage > 0 && adversity == domain.environment.EnvironmentalAdversity.SUFFOCATING_HEAT) inhibitedForEncounter = true;
    }

    public void inhibitForSeconds(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("La duración debe ser finita y no negativa.");
        timedInhibitionSecondsRemaining = Math.max(timedInhibitionSecondsRemaining, seconds);
    }

    public void advanceTime(double elapsedSeconds) {
        if (!Double.isFinite(elapsedSeconds) || elapsedSeconds < 0) throw new IllegalArgumentException("El tiempo debe ser finito y no negativo.");
        timedInhibitionSecondsRemaining = Math.max(0.0, timedInhibitionSecondsRemaining - elapsedSeconds);
        if (timedInhibitionSecondsRemaining < 1.0e-9) timedInhibitionSecondsRemaining = 0.0;
    }

    public boolean healthRegenerationAllowed(HostileEncounterState encounter, boolean explicitException) {
        Objects.requireNonNull(encounter, "El encuentro no puede ser nulo.");
        if (!encounter.isActive()) { onEncounterConcluded(); return true; }
        return explicitException || (!inhibitedForEncounter && timedInhibitionSecondsRemaining <= 0.0);
    }

    /** una inmunidad explícita a inhibición de PV REGEN prevalece sobre cualquier fuente inhibitoria. */
    public boolean healthRegenerationAllowed(HostileEncounterState encounter, domain.runic.EffectImmunitySet immunities) {
        Objects.requireNonNull(immunities, "Las inmunidades no pueden ser nulas.");
        return healthRegenerationAllowed(encounter, immunities.contains(domain.runic.EffectImmunity.HEALTH_REGEN_PENALTIES));
    }

    /** Integra el contrato de abalorios y ANULACIÓN sin filtrar detalles de equipamiento al llamador. */
    public boolean healthRegenerationAllowed(HostileEncounterState encounter,
                                             domain.character.sheet.CharacterSheet sheet,
                                             domain.inventory.equipment.EquipmentState equipment,
                                             domain.ability.NullificationPolicy.SuppressionState suppression) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Objects.requireNonNull(suppression, "El estado de supresión no puede ser nulo.");
        return healthRegenerationAllowed(encounter, equipment.effectImmunities(sheet, suppression));
    }

    public void onEncounterConcluded() {
        inhibitedForEncounter = false;
        timedInhibitionSecondsRemaining = 0.0;
    }
    public boolean inhibited() { return inhibitedForEncounter || timedInhibitionSecondsRemaining > 0.0; }
    public double timedInhibitionSecondsRemaining() { return timedInhibitionSecondsRemaining; }
}
