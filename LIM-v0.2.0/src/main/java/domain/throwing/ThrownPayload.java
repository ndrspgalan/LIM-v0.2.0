package domain.throwing;

import domain.inventory.item.misc.CurrencyType;

import java.util.Objects;
import java.util.Optional;

/** Identidad mínima de la unidad que abandona el inventario al lanzarse. */
public record ThrownPayload(String name, ThrowProfile profile, Optional<CurrencyType> currencyType) {
    public ThrownPayload {
        name = Objects.requireNonNull(name, "El nombre no puede ser nulo.").trim();
        if (name.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        profile = Objects.requireNonNull(profile, "El perfil de lanzamiento no puede ser nulo.");
        currencyType = Objects.requireNonNull(currencyType, "El tipo monetario opcional no puede ser nulo.");
    }

    public static ThrownPayload item(String name, ThrowProfile profile) {
        return new ThrownPayload(name, profile, Optional.empty());
    }

    public static ThrownPayload currency(CurrencyType type) {
        Objects.requireNonNull(type, "El tipo monetario no puede ser nulo.");
        return new ThrownPayload(type.label(), ThrowProfile.improvised(0.001, true), Optional.of(type));
    }
}
