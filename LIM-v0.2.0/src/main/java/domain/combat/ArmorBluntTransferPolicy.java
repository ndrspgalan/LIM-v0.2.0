package domain.combat;

/**
 * Separa trauma corporal y transferencia de impulso en impactos contra armadura.
 *
 * La protección contundente porcentual reduce el daño corporal, pero no elimina el
 * momentum del impacto: la carga detenida por la pieza sigue transmitiéndose al
 * portador. Mientras el dominio no declare propiedades específicas de disipación
 * de impulso, el stagger se calcula con la contundencia ajustada previa a mitigación.
 */
public final class ArmorBluntTransferPolicy {
    private ArmorBluntTransferPolicy() {}

    public static double transferredImpact(double adjustedGrossBlunt) {
        if (!Double.isFinite(adjustedGrossBlunt) || adjustedGrossBlunt < 0) {
            throw new IllegalArgumentException("La contundencia transferida debe ser finita y no negativa.");
        }
        return adjustedGrossBlunt;
    }
}
