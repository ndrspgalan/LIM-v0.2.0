package qa.domain;

import domain.ability.*;
import domain.combat.stamina.StaminaRegenerationDelayPolicy;

import java.util.EnumSet;

public final class VerticalSliceIntegrityVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        TransmutationCombatPolicy policy = new TransmutationCombatPolicy();
        EnumSet<TransmutationNodeId> all = EnumSet.allOf(TransmutationNodeId.class);
        if (policy.resolve(all, new TransmutationCombatContext(true, 100, 100, false, true, 5, 6))
                != TransmutationActivation.OVERDRIVE) fail("OVERDRIVE debe cubrir la siguiente acción inmediata comprometida.");
        if (policy.resolve(all, new TransmutationCombatContext(true, 50, 100, true, true, 0, 10))
                != TransmutationActivation.MIRAGE) fail("MIRAGE debe prevalecer antes de fintar.");
        if (policy.resolve(all, new TransmutationCombatContext(false, 50, 100, false, false, 10, 0))
                != TransmutationActivation.OVERCLOCK) fail("OVERCLOCK debe sanar fuera de combate.");
        StaminaRegenerationDelayPolicy regen = new StaminaRegenerationDelayPolicy();
        if (regen.canRegenerate(1.199)) fail("PA REGEN no debe comenzar antes de 1,20 s.");
        if (!regen.canRegenerate(1.20)) fail("PA REGEN debe comenzar a los 1,20 s.");
    }
    private static void fail(String message) { throw new AssertionError(message); }
}
