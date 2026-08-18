package domain.inventory.equipment;

/** Ubicaciones físicas del equipo. Las armas ya no se jerarquizan como principal/secundaria. */
public enum EquipmentSlot {
    HEAD("Cabeza"),
    RIGHT_HAND("Mano derecha"),
    LEFT_HAND("Mano izquierda"),
    BACK_HAND("Espalda / mano dominante derecha"),
    CHEST("Coraza"),
    BRACERS("Brazales"),
    LEGGINGS("Polainas"),
    FEET("Calzado"),
    ACCESSORY("Abalorio"),
    RUNIC_MARK("Marca Rúnica");

    private final String label;

    EquipmentSlot(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
