package domain.inventory.equipment;

/**
 * Relación física entre el cuerpo y el terreno a través de FEET.
 * EARTH_COUPLED no significa inmunidad eléctrica universal; sólo existe una vía al terreno.
 */
public enum FeetElectricalContact {
    EARTH_COUPLED,
    INSULATED,
    INTEGRATED_ISOLATED,
    INTEGRATED_CONDUCTIVE
}
