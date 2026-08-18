package domain.ability.progression;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Estado persistente de las condiciones narrativas que revelan maestrías no afines. */
public final class MasteryProgressState {
    private int validBitingFrostExposures;
    private double consecutiveAwakeGameMinutes;
    private boolean hungerAndThirstFunctionalThroughoutAwakeWindow = true;
    private boolean attemptedRunning;
    private boolean attemptedClimbing;
    private boolean attemptedSwimming;
    private int unarmedLightAttacks;
    private boolean sufferedRealFrenzyDamage;
    private boolean sleptAtMaximumHungerAndThirstPenalty;
    private boolean sleptAfterFortyEightAwakeHours;
    private final Set<String> heldFeareTrophies = new HashSet<>();
    private final Set<String> pettedCharismaFerae = new HashSet<>();

    public int validBitingFrostExposures() { return validBitingFrostExposures; }
    public double consecutiveAwakeHours() { return consecutiveAwakeGameMinutes / 60.0; }
    public double consecutiveAwakeGameMinutes() { return consecutiveAwakeGameMinutes; }
    public boolean attemptedRunning() { return attemptedRunning; }
    public boolean attemptedClimbing() { return attemptedClimbing; }
    public boolean attemptedSwimming() { return attemptedSwimming; }
    public int unarmedLightAttacks() { return unarmedLightAttacks; }
    public boolean sufferedRealFrenzyDamage() { return sufferedRealFrenzyDamage; }
    public boolean sleptAtMaximumHungerAndThirstPenalty() { return sleptAtMaximumHungerAndThirstPenalty; }
    public boolean sleptAfterFortyEightAwakeHours() { return sleptAfterFortyEightAwakeHours; }
    public Set<String> heldFeareTrophies() { return Set.copyOf(heldFeareTrophies); }
    public Set<String> pettedCharismaFerae() { return Set.copyOf(pettedCharismaFerae); }

    public void registerValidBitingFrostExposure(boolean realDamage, boolean hungerFunctionalOrBetter, boolean thirstFunctionalOrBetter) {
        if (realDamage && hungerFunctionalOrBetter && thirstFunctionalOrBetter) validBitingFrostExposures++;
    }
    public void advanceAwakeGameMinutes(double minutes, boolean hungerFunctionalOrBetter, boolean thirstFunctionalOrBetter) {
        if (minutes < 0) throw new IllegalArgumentException("Los minutos no pueden ser negativos.");
        if (!hungerFunctionalOrBetter || !thirstFunctionalOrBetter) {
            consecutiveAwakeGameMinutes = 0;
            hungerAndThirstFunctionalThroughoutAwakeWindow = false;
            return;
        }
        if (!hungerAndThirstFunctionalThroughoutAwakeWindow) hungerAndThirstFunctionalThroughoutAwakeWindow = true;
        consecutiveAwakeGameMinutes += minutes;
    }
    /** Compatibilidad: las horas son horas internas del mundo, no tiempo real. */
    public void advanceAwakeHours(double hours) {
        if (hours < 0) throw new IllegalArgumentException("Las horas no pueden ser negativas.");
        advanceAwakeGameMinutes(hours * 60.0, true, true);
    }
    public void registerSleep(boolean maximumHungerPenalty, boolean maximumThirstPenalty) {
        sleptAtMaximumHungerAndThirstPenalty |= maximumHungerPenalty && maximumThirstPenalty;
        double twoGameDaysMinutes = domain.environment.time.EnvironmentalCycle.DAY_DURATION.toMinutes() * 2.0;
        sleptAfterFortyEightAwakeHours |= hungerAndThirstFunctionalThroughoutAwakeWindow
                && consecutiveAwakeGameMinutes >= twoGameDaysMinutes;
        consecutiveAwakeGameMinutes = 0;
        hungerAndThirstFunctionalThroughoutAwakeWindow = true;
    }
    public void registerRunningAttempt() { attemptedRunning = true; }
    public void registerClimbingAttempt() { attemptedClimbing = true; }
    public void registerSwimmingAttempt() { attemptedSwimming = true; }
    public void registerUnarmedLightAttack() { unarmedLightAttacks++; }
    public void registerRealFrenzyDamage() { sufferedRealFrenzyDamage = true; }
    public void addFeareTrophy(String trophy) { heldFeareTrophies.add(normalize(trophy)); }
    public void removeFeareTrophy(String trophy) { heldFeareTrophies.remove(normalize(trophy)); }
    public void registerCharismaFeraPet(String species) { pettedCharismaFerae.add(normalize(species)); }

    private static String normalize(String value) {
        Objects.requireNonNull(value, "El identificador no puede ser nulo.");
        return value.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
