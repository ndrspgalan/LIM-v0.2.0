package qa.domain;

import domain.ability.ConvergentTrajectoryPolicy;

/** OVERDRIVE es compatible con el flow de Trayectoria Convergente. */
public final class OverdriveFlowVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        ConvergentTrajectoryPolicy p = new ConvergentTrajectoryPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true, 3, 3, false, true), 1.4), "El remate abre el flow");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true, 1, 3, false, true), 1.4), "OVERDRIVE no rompe el flow");
        p.onOverdriveActivated();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true, 2, 3, false, true), 1.4), "El callback de OVERDRIVE tampoco rompe el flow");
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true, 3, 3, false, true), 1.4), "Un nuevo remate sí cierra la cadena anterior");
        p.onStaminaRegenerationStarted();
        org.junit.jupiter.api.Assertions.assertTrue(close(p.onLightAttack(true, 1, 3, false, true), 1.0), "PA REGEN reinicia el flow");
    }
    private static boolean close(double a,double b){ return Math.abs(a-b)<1e-9; }
    
}
