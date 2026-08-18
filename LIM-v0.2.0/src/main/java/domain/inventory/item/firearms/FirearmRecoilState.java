package domain.inventory.item.firearms;

/**
 * Acumulador físico de retroceso. No existe recuperación automática:
 * solo una corrección explícita del jugador, originada por movimiento del ratón,
 * puede reducir la magnitud pendiente que consumirá el frontend.
 */
public final class FirearmRecoilState {
    private double accumulatedVelocityMps;

    public double accumulatedVelocityMps() { return accumulatedVelocityMps; }

    public double registerShot(double recoilVelocityPerShotMps) {
        if (!Double.isFinite(recoilVelocityPerShotMps) || recoilVelocityPerShotMps < 0) {
            throw new IllegalArgumentException("El retroceso por disparo debe ser finito y no negativo.");
        }
        accumulatedVelocityMps += recoilVelocityPerShotMps;
        return accumulatedVelocityMps;
    }

    public double stabilizeByPlayer(double compensatedVelocityMps) {
        if (!Double.isFinite(compensatedVelocityMps) || compensatedVelocityMps < 0) {
            throw new IllegalArgumentException("La compensación del jugador debe ser finita y no negativa.");
        }
        accumulatedVelocityMps = Math.max(0.0, accumulatedVelocityMps - compensatedVelocityMps);
        return accumulatedVelocityMps;
    }

    public void resetForUnequip() {
        accumulatedVelocityMps = 0.0;
    }
}
