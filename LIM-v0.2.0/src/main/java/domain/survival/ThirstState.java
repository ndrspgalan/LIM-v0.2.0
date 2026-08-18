package domain.survival;

import domain.ability.HomeostasisThermalPolicy;

public final class ThirstState {
    public static final double HOURS_PER_LEVEL = 0.5;
    public static final double HYDRATED_HOURS = 0.5;
    public static final double SUPERIOR_REGULATION_HYDRATED_HOURS =
            HYDRATED_HOURS * HomeostasisThermalPolicy.POSITIVE_SURVIVAL_STAGE_MULTIPLIER;
    public static final int MAX_LEVEL = 6;

    private int level;
    private double hoursUntilNextLevel;
    private double hydratedHoursRemaining;

    public ThirstState() { this(0, HOURS_PER_LEVEL, 0.0); }

    public ThirstState(int level, double hoursUntilNextLevel, double hydratedHoursRemaining) {
        if (level < 0 || level > MAX_LEVEL) throw new IllegalArgumentException("El nivel de sed debe estar entre 0 y 6.");
        if (hoursUntilNextLevel < 0 || hydratedHoursRemaining < 0) throw new IllegalArgumentException("Los tiempos de sed no pueden ser negativos.");
        this.level = level;
        this.hoursUntilNextLevel = hoursUntilNextLevel;
        this.hydratedHoursRemaining = hydratedHoursRemaining;
    }

    public int level() { return level; }
    public int gameplayValue() { return status() == ThirstStatus.HYDRATED ? 1 : -level; }
    public double hoursUntilNextLevel() { return hoursUntilNextLevel; }
    public double hydratedHoursRemaining() { return hydratedHoursRemaining; }
    public boolean isDehydrated() { return level == MAX_LEVEL; }
    public ThirstStatus status() {
        if (hydratedHoursRemaining > 0) return ThirstStatus.HYDRATED;
        if (level == 0) return ThirstStatus.FUNCTIONAL;
        return level == MAX_LEVEL ? ThirstStatus.DEHYDRATED : ThirstStatus.THIRSTY;
    }

    public double healthRegenerationMultiplier() {
        if (hydratedHoursRemaining > 0) return 7.0 / 6.0;
        return (MAX_LEVEL - level) / 6.0;
    }

    public void advanceHours(double hours) { advanceHours(hours, false); }

    public void advanceHours(double hours, boolean superiorCaloricRegulationActive) {
        if (hours < 0) throw new IllegalArgumentException("No puede retrocederse la sed.");
        double remaining = hours;
        if (hydratedHoursRemaining > 0) {
            double consumed = Math.min(remaining, hydratedHoursRemaining);
            hydratedHoursRemaining -= consumed;
            remaining -= consumed;
            if (hydratedHoursRemaining == 0) hoursUntilNextLevel = HOURS_PER_LEVEL;
        }
        while (remaining > 0 && level < MAX_LEVEL) {
            if (remaining < hoursUntilNextLevel) {
                hoursUntilNextLevel -= remaining;
                remaining = 0;
            } else {
                remaining -= hoursUntilNextLevel;
                level++;
                hoursUntilNextLevel = HOURS_PER_LEVEL;
            }
        }
    }

    public boolean drinkWater() { return drinkWater(false); }

    public boolean drinkWater(boolean superiorCaloricRegulationActive) {
        if (level > 0) {
            level--;
            if (level == 0) hoursUntilNextLevel = HOURS_PER_LEVEL;
            return false;
        }
        hydratedHoursRemaining = HomeostasisThermalPolicy.positiveSurvivalStageHours(
                HYDRATED_HOURS, superiorCaloricRegulationActive);
        hoursUntilNextLevel = HOURS_PER_LEVEL;
        return true;
    }

    public void restoreOneWithoutHydratedBonus() { restoreOne(false, false); }
    public boolean restoreOne(boolean canActivateHydrated) { return restoreOne(canActivateHydrated, false); }

    public boolean restoreOne(boolean canActivateHydrated, boolean superiorCaloricRegulationActive) {
        if (level > 0) {
            level--;
            if (level == 0) hoursUntilNextLevel = HOURS_PER_LEVEL;
            return false;
        }
        if (canActivateHydrated) {
            hydratedHoursRemaining = HomeostasisThermalPolicy.positiveSurvivalStageHours(
                    HYDRATED_HOURS, superiorCaloricRegulationActive);
            hoursUntilNextLevel = HOURS_PER_LEVEL;
            return true;
        }
        return false;
    }

    public void addOneLevel() {
        hydratedHoursRemaining = 0;
        level = Math.min(MAX_LEVEL, level + 1);
        hoursUntilNextLevel = HOURS_PER_LEVEL;
    }
}
