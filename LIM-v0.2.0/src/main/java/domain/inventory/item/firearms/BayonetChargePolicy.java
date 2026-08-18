package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.combat.StrengthMassBluntPolicy;

/** HOLD ataque cargado: avanza mientras haya PA y usa exactamente la tasa de PA de correr suministrada por locomoción. */
public final class BayonetChargePolicy {
    public BayonetChargeResult begin(BayonetChargeState state, double currentStamina) {
        if (currentStamina <= 0) return new BayonetChargeResult(false, false, 0, null, "Sin PA para iniciar la carga con bayoneta.");
        state.begin();
        return new BayonetChargeResult(true, false, 0, null, "Carga con bayoneta iniciada.");
    }

    public BayonetChargeResult advance(BayonetChargeState state, double seconds, double currentStamina,
                                       double runningStaminaPerSecond) {
        if (!state.charging()) return new BayonetChargeResult(false, false, 0, null, "La carga no está activa.");
        if (!Double.isFinite(seconds) || seconds < 0 || !Double.isFinite(runningStaminaPerSecond) || runningStaminaPerSecond < 0)
            throw new IllegalArgumentException("Tiempo o tasa de PA inválidos.");
        double cost = Math.min(currentStamina, seconds * runningStaminaPerSecond);
        state.spend(cost);
        if (cost < seconds * runningStaminaPerSecond || currentStamina - cost <= 0) state.stop();
        return new BayonetChargeResult(state.charging(), false, cost, null,
                state.charging() ? "La carga continúa." : "La carga termina por agotamiento de PA.");
    }

    public BayonetChargeResult impact(BayonetChargeState state, int strength, double rifleMassKg) {
        if (!state.charging()) return new BayonetChargeResult(false, false, 0, null, "No existe una carga activa.");
        state.stop();
        double blunt = StrengthMassBluntPolicy.blunt(strength, rifleMassKg);
        return new BayonetChargeResult(false, true, 0, new LethalityProfile(65, 65, blunt),
                "La carga termina en un impacto de bayoneta: FUERZA + masa del fusil alimentan el canal contundente.");
    }

    public void release(BayonetChargeState state) { state.stop(); }
}
