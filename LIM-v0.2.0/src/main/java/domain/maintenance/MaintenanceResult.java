package domain.maintenance;

import domain.inventory.item.misc.UseAnimation;
import java.util.Optional;

public record MaintenanceResult(boolean successful, String reason, MaintenanceAction action,
                                Optional<UseAnimation> animation) {
    public static MaintenanceResult rejected(String reason, MaintenanceAction action) {
        return new MaintenanceResult(false, reason, action, Optional.empty());
    }
    public static MaintenanceResult completed(MaintenanceAction action, UseAnimation animation) {
        return new MaintenanceResult(true, "", action, Optional.of(animation));
    }
}
