package domain.inventory;

/** Procedencia de una incorporación automática; todas comparten la misma política física. */
public enum InventoryAdmissionSource {
    TRANSPOSITION,
    QUICK_UNEQUIP,
    ACTIVE_UNEQUIP,
    WORLD_PICKUP,
    TRANSACTION,
    PILLAGE,
    CRAFTING,
    DIALOGUE_GRANT,
    SYSTEM_REWARD
}
