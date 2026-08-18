package domain.inventory.item;

import java.util.List;

/** Propiedades comunes de uso desde Transporte Personal. */
public final class PersonalTransportUseProperties {
    private PersonalTransportUseProperties() {}

    public static List<ItemProperty> all() {
        return List.of(
                ItemProperty.alwaysActive(ItemPropertyId.COPILOT, "COPILOTO",
                        "Puede utilizarse desde el asiento del pasajero.", "Uso desde asiento de copiloto"),
                ItemProperty.alwaysActive(ItemPropertyId.EQUESTRIAN, "ECUESTRE",
                        "Puede utilizarse mientras se cabalga.", "Uso como conductor de caballo"),
                ItemProperty.alwaysActive(ItemPropertyId.BICYCLAR, "BICICLAR",
                        "Puede utilizarse mientras se conduce una bicicleta.", "Uso como conductor de bicicleta"),
                ItemProperty.alwaysActive(ItemPropertyId.MOTORCYCLAR, "MOTOCICLAR",
                        "Puede utilizarse mientras se conduce una motocicleta. MOTOCICLAR fuerza cualquier acción compatible a ejecutarse con LEFT HAND, porque RIGHT HAND mantiene el acelerador.",
                        "Uso obligatorio con LEFT HAND durante conducción de motocicleta")
        );
    }

    public static ItemProperty coupDeGrace() {
        return ItemProperty.alwaysActive(ItemPropertyId.COUP_DE_GRACE, "GOLPE DE GRACIA",
                "Un impacto a la cabeza que no esté protegida al 100 % y cuyo daño bruto perforante sea superior al que otorga dicha protección provoca que el impacto sea de gracia, reduciendo los PV TOTALES del personaje impactado a cero.",
                "HEAD + cobertura < 100 % + P bruto > protección P => PV TOTALES = 0");
    }
}
