package domain.ability;

import domain.character.Gender;

/** Token de GRITO DE GUERRA: se prepara con PA completos y se consume al siguiente impacto melee. */
public final class WarCryStaminaPolicy {
    public record Prepared(boolean accepted, boolean opportunityArmed, String reason) {}
    public record HitResolution(boolean opportunityConsumed, double staminaCost, boolean freeHit) {}

    public Prepared prepare(Gender userGender, double currentPa, double totalPa) {
        if (userGender != Gender.HOMBRE) return new Prepared(false, false, "GRITO DE GUERRA pertenece al origen masculino.");
        if (!Double.isFinite(currentPa) || !Double.isFinite(totalPa) || totalPa < 0) throw new IllegalArgumentException("PA inválidos.");
        if (Math.abs(currentPa - totalPa) > 1e-9) return new Prepared(false, false, "GRITO DE GUERRA exige PA completos.");
        return new Prepared(true, true, "La siguiente conexión cuerpo a cuerpo queda preparada.");
    }

    public HitResolution resolveNextConnectedMelee(Gender targetGender, double nominalStaminaCost, boolean opportunityArmed) {
        if (!Double.isFinite(nominalStaminaCost) || nominalStaminaCost < 0) throw new IllegalArgumentException("Coste de PA inválido.");
        if (!opportunityArmed) return new HitResolution(false, nominalStaminaCost, false);
        boolean free = targetGender == Gender.HOMBRE;
        return new HitResolution(true, free ? 0.0 : nominalStaminaCost, free);
    }
}
