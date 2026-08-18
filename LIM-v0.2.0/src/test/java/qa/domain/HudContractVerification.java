package qa.domain;

import domain.hud.HudMode;
import domain.hud.HudModeCyclePolicy;

/** Verificación canónica:  sustituyó el ciclo ternario por pausa binaria. */
public final class HudContractVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        HudModeCyclePolicy cycle = new HudModeCyclePolicy();
        HudMode mode = HudMode.REALTIME;
        mode = cycle.next(mode);
        org.junit.jupiter.api.Assertions.assertTrue(mode == HudMode.PAUSED && mode.gameplayPaused() && !mode.hudVisible(),
                "La primera pulsación debe pausar sin mostrar HUD.");
        mode = cycle.next(mode);
        org.junit.jupiter.api.Assertions.assertTrue(mode == HudMode.REALTIME && !mode.gameplayPaused() && !mode.hudVisible(),
                "La segunda pulsación debe reanudar sin restaurar un HUD inexistente.");
    }

    
}
