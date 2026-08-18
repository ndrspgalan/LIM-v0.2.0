package domain.status;

public record TherapeuticUseResult(
        HealthState healthState,
        ActiveTherapeuticEffects activeEffects,
        boolean consumed,
        double healthRestored
) {}
