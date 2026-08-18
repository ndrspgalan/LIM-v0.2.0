package domain.inventory.item.misc;

/**
 * Contrato material del Conversor de combustible improvisado.
 * El proceso no pretende ser instantáneo: esta slice fija balance de masa y autoridad de receta;
 * el level design puede resolver el tiempo de fermentación sin alterar la economía.
 */
public final class ImprovisedFuelConversionPolicy {
    public static final double POTATO_KG_PER_ETHANOL_LITER = 10.0;
    public static final double ETHANOL_LITERS_PER_BATCH = 1.0;

    private ImprovisedFuelConversionPolicy(){}

    public static int wholePotatoesRequiredForOneLiter() {
        return (int)Math.ceil(POTATO_KG_PER_ETHANOL_LITER / RawPotatoItem.UNIT_WEIGHT_KG);
    }

    public static PortableFuelCanItem produceOneLiter(int availableWholePotatoes, boolean converterAvailable) {
        if(!converterAvailable) throw new IllegalStateException("Hace falta el Conversor de combustible improvisado.");
        int required=wholePotatoesRequiredForOneLiter();
        if(availableWholePotatoes < required)
            throw new IllegalArgumentException("Materia insuficiente: hacen falta "+required+" patatas crudas (al menos 10 kg).");
        return new PortableFuelCanItem(domain.inventory.logistics.MotorcycleFuelType.ETHANOL);
    }
}
