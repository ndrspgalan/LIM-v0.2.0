package domain.inventory.catalog;

/**
 * Ontología física del almacenamiento. No equivale a "stack".
 * INDIVIDUAL: cada unidad existe como rectángulo propio.
 * PERSISTENT_CONTAINER: una carcasa/recipiente persiste y su contenido interno varía.
 * SPECIALIZED_CONTAINER: el propio objeto agrega internamente una familia concreta.
 */
public enum PhysicalStorageSemantics {
    INDIVIDUAL,
    PERSISTENT_CONTAINER,
    SPECIALIZED_CONTAINER,
    CURRENCY_STACK
}
