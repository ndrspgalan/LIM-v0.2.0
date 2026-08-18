package domain.inventory.item;

/**
 * Estado ordinal compartido por las cadenas LIGHT. Nació para dual wielding y desde 
 * también conserva la progresión al cambiar PRIMARY/ALTERNATIVE durante un combo activo.
 */
public final class DualWieldComboState {
    private int nextLightAttackOrdinal = 1;
    private boolean activeLightCombo;

    public int nextLightAttackOrdinal() { return nextLightAttackOrdinal; }
    public boolean hasActiveLightCombo() { return activeLightCombo; }

    public void registerExecutedOrdinal(int executedOrdinal) {
        if (executedOrdinal < 1) throw new IllegalArgumentException("El ordinal ejecutado debe ser positivo.");
        nextLightAttackOrdinal = executedOrdinal + 1;
        activeLightCombo = true;
    }

    /** Interrumpe la cadena LIGHT: el siguiente ligero comienza desde L1 sin herencia ordinal. */
    public void interrupt() { reset(); }

    public void reset() {
        nextLightAttackOrdinal = 1;
        activeLightCombo = false;
    }
}
