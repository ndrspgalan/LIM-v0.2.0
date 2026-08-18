package domain.combat.ai.declarative;

/** Operaciones materiales de inventario; no expresan preferencia. */
public enum InventoryActionType {
    DROP,
    EQUIP_ACTIVE,
    EQUIP_QUICK_ACCESS,
    UNEQUIP,
    USE,
    INSPECT,
    ROTATE_90,
    PICK_UP,
    DEPLOY_DORSAL_ROTOR,
    RETRACT_DORSAL_ROTOR
}
