package domain.ability;

import java.util.Objects;
import java.util.Set;

/** Política universal de uso automático de TRANSMUTACIÓN, agnóstica al personaje. */
public final class TransmutationCombatPolicy {
    public TransmutationActivation resolve(Set<TransmutationNodeId> unlocked, TransmutationCombatContext context) {
        Objects.requireNonNull(unlocked, "Los nodos desbloqueados no pueden ser nulos.");
        Objects.requireNonNull(context, "El contexto no puede ser nulo.");

        if (!context.inCombat()) {
            return available(unlocked, TransmutationNodeId.OVERCLOCK) && context.healthIsIncomplete()
                    ? TransmutationActivation.OVERCLOCK : TransmutationActivation.NONE;
        }
        if (context.nextActionIsFeint() && available(unlocked, TransmutationNodeId.MIRAGE)) {
            return TransmutationActivation.MIRAGE;
        }
        if (context.needsOverdriveForNextImmediateAction()
                && available(unlocked, TransmutationNodeId.OVERDRIVE)) {
            return TransmutationActivation.OVERDRIVE;
        }
        // OVERCLOCK ya no se autoenciende como efecto lateral de cualquier
        // acción de combate. Su coste metabólico (hambre y sed x2) requiere decisión táctica explícita.
        return TransmutationActivation.NONE;
    }

    public boolean metamorphosisAvailable(Set<TransmutationNodeId> unlocked) {
        return available(unlocked, TransmutationNodeId.METAMORPHOSIS);
    }

    public boolean mirrorsEdgePassivePresent(Set<TransmutationNodeId> unlocked) {
        return available(unlocked, TransmutationNodeId.MIRRORS_EDGE);
    }

    private boolean available(Set<TransmutationNodeId> unlocked, TransmutationNodeId node) {
        return unlocked.contains(node);
    }
}
