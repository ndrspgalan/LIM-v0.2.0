package domain.throwing;

import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.UtilityObjectItem;
import domain.inventory.item.throwingWeapons.ThrowingWeaponItem;

/** Ejecuta la extracción atómica de una unidad apilada después de validar y calcular el lanzamiento. */
public final class StackThrowPolicy {
    private final ThrowPolicy throwPolicy;

    public StackThrowPolicy() {
        this(new ThrowPolicy());
    }

    public StackThrowPolicy(ThrowPolicy throwPolicy) {
        if (throwPolicy == null) throw new IllegalArgumentException("La política de lanzamiento no puede ser nula.");
        this.throwPolicy = throwPolicy;
    }

    public ThrowResult throwOne(UtilityObjectItem item, ThrowRequest request) {
        if (item == null) throw new IllegalArgumentException("El objeto no puede ser nulo.");
        ThrowProfile profile = item.throwProfile().orElseThrow(
                () -> new IllegalArgumentException("El objeto no posee perfil de lanzamiento."));
        if (item.isDepleted()) throw new IllegalStateException("No quedan unidades que lanzar.");
        ThrowResult result = throwPolicy.resolve(request, ThrownPayload.item(item.name(), profile));
        if (!item.removeUnits(1)) throw new IllegalStateException("No se pudo extraer la unidad lanzada.");
        return result;
    }


    public ThrowResult throwOne(ThrowingWeaponItem item, ThrowRequest request) {
        if (item == null) throw new IllegalArgumentException("El arma arrojadiza no puede ser nula.");
        if (item.isDepleted()) throw new IllegalStateException("No quedan unidades que lanzar.");
        ThrowResult result = throwPolicy.resolve(request, ThrownPayload.item(item.name(), item.throwProfile()));
        if (!item.removeUnits(1)) throw new IllegalStateException("No se pudo extraer la unidad lanzada.");
        return result;
    }

    public ThrowResult throwOne(CurrencyStack currency, ThrowRequest request) {
        if (currency == null) throw new IllegalArgumentException("El stack monetario no puede ser nulo.");
        if (currency.isDepleted()) throw new IllegalStateException("No quedan monedas que lanzar.");
        ThrowResult result = throwPolicy.resolve(request, ThrownPayload.currency(currency.currencyType()));
        if (!currency.removeUnits(1)) throw new IllegalStateException("No se pudo extraer la moneda lanzada.");
        return result;
    }
}
