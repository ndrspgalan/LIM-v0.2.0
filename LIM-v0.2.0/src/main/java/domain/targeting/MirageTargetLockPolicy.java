package domain.targeting;

/**
 * Política de fijación de MIRAGE.
 * El afectado es el personaje que mantiene fijado como blanco al usuario de MIRAGE:
 * pierde temporalmente esa fijación y la recupera automáticamente al concluir el desfase.
 */
public final class MirageTargetLockPolicy {
    private MirageTargetLockPolicy() {}

    public static MirageTargetLockResult whenTargetActivatesMirage(boolean observerHadTargetLocked) {
        if (!observerHadTargetLocked) {
            return new MirageTargetLockResult(false, false);
        }
        return new MirageTargetLockResult(true, true);
    }
}
