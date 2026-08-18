package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;

import java.util.List;
import java.util.Set;

/** Arma de fuego impulsada por depósito neumático recargable por el propio usuario. */
public class PneumaticFirearmItem extends FirearmItem {
    private final int pneumaticCapacityShots;
    private int pressureRemaining;

    public PneumaticFirearmItem(
            String name,
            String narrativeDescription,
            double weightKg,
            double lengthMeters,
            double widthMeters,
            double effectiveRangeMeters,
            String caliber,
            FirearmCartridge cartridgeDefinition,
            LethalityProfile lethalityProfile,
            double recoilVelocityPerShotMps,
            List<FireMode> fireModes,
            boolean supportsOneHanded,
            boolean supportsTwoHanded,
            Set<WeaponTrait> traits,
            int pneumaticCapacityShots
    ) {
        super(name, narrativeDescription, weightKg, lengthMeters, widthMeters, effectiveRangeMeters,
                caliber, cartridgeDefinition, lethalityProfile, recoilVelocityPerShotMps, fireModes,
                supportsOneHanded, supportsTwoHanded, traits);
        if (pneumaticCapacityShots <= 0) {
            throw new IllegalArgumentException("La capacidad neumática debe ser positiva.");
        }
        this.pneumaticCapacityShots = pneumaticCapacityShots;
        this.pressureRemaining = pneumaticCapacityShots;
    }

    @Override public boolean coupDeGracePropertyPresent() { return true; }

    public int pneumaticCapacityShots() { return pneumaticCapacityShots; }
    public int pressureRemaining() { return pressureRemaining; }
    public boolean pressureGaugePresent() { return true; }
    public boolean hasPressure() { return pressureRemaining > 0; }
    public boolean isPressureFull() { return pressureRemaining == pneumaticCapacityShots; }
    /** Cada gesto completo de bombeo inspirado en la manipulación multi-stroke tipo Tihar restaura una unidad. */
    public double pressureStepDurationSeconds() { return timingProfile().pressureStepSeconds(); }
    public double secondsToFullPressure() { return timingProfile().fullPressureRestoreSeconds(pneumaticCapacityShots-pressureRemaining); }

    @Override
    protected boolean canConsumeShot() {
        return super.canConsumeShot() && hasPressure();
    }

    @Override
    protected void consumeShot() {
        if (!hasPressure()) throw new IllegalStateException("El arma no tiene presión neumática.");
        super.consumeShot();
        pressureRemaining--;
    }

    public boolean beginPressurization() {
        if (isPressureFull()) {
            leavePneumaticPressurizationState();
            return false;
        }
        enterPneumaticPressurizationState();
        return true;
    }

    /** Un RIGHT CLICK durante HOLD R introduce una unidad de presión. */
    public boolean pressurizeOneStep() {
        if (handlingState() != FirearmHandlingState.PNEUMATIC_PRESSURIZATION) {
            throw new IllegalStateException("El mecanismo neumático no está sujeto por el personaje.");
        }
        if (isPressureFull()) {
            leavePneumaticPressurizationState();
            return false;
        }
        pressureRemaining++;
        if (isPressureFull()) {
            leavePneumaticPressurizationState();
        }
        return true;
    }

    public void restorePressureRemaining(int units) { if(units<0||units>pneumaticCapacityShots) throw new IllegalArgumentException("Presión persistida inválida."); pressureRemaining=units; }

    public boolean cancelPressurization() {
        if (handlingState() != FirearmHandlingState.PNEUMATIC_PRESSURIZATION) return false;
        leavePneumaticPressurizationState();
        return true;
    }
}
