package domain.inventory.item;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Configuraciones intrínsecas de un arma, independientes de la mano que la porte. */
public final class WeaponConfigurationPolicy {
    private final List<WeaponConfiguration> orderedConfigurations;

    public WeaponConfigurationPolicy(List<WeaponConfiguration> orderedConfigurations) {
        Objects.requireNonNull(orderedConfigurations, "Las configuraciones no pueden ser nulas.");
        this.orderedConfigurations = orderedConfigurations.stream().distinct().toList();
        if (this.orderedConfigurations.isEmpty()) {
            throw new IllegalArgumentException("Un arma debe admitir al menos una configuración.");
        }
    }

    public static WeaponConfigurationPolicy shield() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
        ));
    }

    public static WeaponConfigurationPolicy unarmed() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY),
                new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.ALTERNATIVE)
        ));
    }

    /** Espadón de Rotor, 2H PRIMARY y 1H ALTERNATIVE. */
    public static WeaponConfigurationPolicy rotorGreatsword() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY),
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
        ));
    }


    /** Bō, 2H PRIMARY y 1H ALTERNATIVE sin incremento especial de requisitos. */
    public static WeaponConfigurationPolicy boStaff() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY),
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
        ));
    }

    public static WeaponConfigurationPolicy twoHandedPrimaryOnly() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.TWO_HANDED, WeaponActionMode.PRIMARY)
        ));
    }

    public static WeaponConfigurationPolicy oneHandedAlternativeOnly() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
        ));
    }

    public static WeaponConfigurationPolicy oneHandedPrimaryAndAlternative() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY),
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE)
        ));
    }



    public static WeaponConfigurationPolicy oneHandedPrimaryOnly() {
        return new WeaponConfigurationPolicy(List.of(
                new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY)
        ));
    }

    public List<WeaponConfiguration> configurations() {
        return orderedConfigurations;
    }

    public boolean supports(WeaponConfiguration configuration) {
        return orderedConfigurations.contains(configuration);
    }

    public boolean supports(GripMode gripMode, WeaponActionMode actionMode) {
        return orderedConfigurations.contains(new WeaponConfiguration(gripMode, actionMode));
    }

    public Optional<WeaponConfiguration> preferredFor(WeaponActionMode actionMode, GripMode preferredGrip) {
        Objects.requireNonNull(actionMode, "El modo de acción no puede ser nulo.");
        Objects.requireNonNull(preferredGrip, "El agarre preferente no puede ser nulo.");
        return orderedConfigurations.stream()
                .filter(configuration -> configuration.actionMode() == actionMode
                        && configuration.gripMode() == preferredGrip)
                .findFirst()
                .or(() -> orderedConfigurations.stream()
                        .filter(configuration -> configuration.actionMode() == actionMode)
                        .findFirst());
    }

    public WeaponConfiguration nextAfter(WeaponConfiguration current) {
        int index = orderedConfigurations.indexOf(current);
        if (index < 0) {
            return orderedConfigurations.getFirst();
        }
        return orderedConfigurations.get((index + 1) % orderedConfigurations.size());
    }
}
