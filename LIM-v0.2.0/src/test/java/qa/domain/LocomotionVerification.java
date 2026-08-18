package qa.domain;

import domain.ability.CharacterMasteryCollection;
import domain.character.sheet.CharacterSheet;
import domain.movement.LocomotionMode;
import domain.movement.LocomotionPolicy;
import domain.movement.MobilityPolicy;
import domain.movement.SlopeBand;
import domain.movement.SwimmingPolicy;
import domain.movement.SwimmingState;
import domain.movement.TerrainSurface;

public final class LocomotionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        LocomotionPolicy terrain = new LocomotionPolicy();

        var flat = terrain.resolve(new TerrainSurface(9.0, false));
        org.junit.jupiter.api.Assertions.assertTrue(flat.slopeBand() == SlopeBand.RUN_ALLOWED, "0-9 grados debe permitir correr.");
        org.junit.jupiter.api.Assertions.assertTrue(flat.allows(LocomotionMode.RUNNING), "Correr debe estar disponible a 9 grados.");

        var moderate = terrain.resolve(new TerrainSurface(10.0, false));
        org.junit.jupiter.api.Assertions.assertTrue(moderate.slopeBand() == SlopeBand.TROT_MAXIMUM, "10-14 grados debe limitar a trote.");
        org.junit.jupiter.api.Assertions.assertTrue(!moderate.allows(LocomotionMode.RUNNING), "No se puede correr a 10 grados.");
        org.junit.jupiter.api.Assertions.assertTrue(moderate.allows(LocomotionMode.TROTTING), "Se puede trotar a 10 grados.");

        var steep = terrain.resolve(new TerrainSurface(74.0, false));
        org.junit.jupiter.api.Assertions.assertTrue(steep.slopeBand() == SlopeBand.WALK_MAXIMUM, "15-74 grados debe limitar a caminar.");
        org.junit.jupiter.api.Assertions.assertTrue(steep.allows(LocomotionMode.WALKING), "Se puede caminar a 74 grados.");
        org.junit.jupiter.api.Assertions.assertTrue(!steep.allows(LocomotionMode.TROTTING), "No se puede trotar a 74 grados.");

        var unclimbable = terrain.resolve(new TerrainSurface(75.0, false));
        org.junit.jupiter.api.Assertions.assertTrue(!unclimbable.traversable(), "75-120 grados no escalable debe ser intransitable.");

        var climbable = terrain.resolve(new TerrainSurface(120.0, true));
        org.junit.jupiter.api.Assertions.assertTrue(climbable.requiresClimbing(), "75-120 grados escalable debe exigir escalada.");

        var impossible = terrain.resolve(new TerrainSurface(120.01, true));
        org.junit.jupiter.api.Assertions.assertTrue(impossible.slopeBand() == SlopeBand.IMPASSABLE && !impossible.traversable(),
                ">120 grados debe ser intransitable incluso si se marca escalable.");

        var kenanMobility = new MobilityPolicy().resolve(1.72,
                CharacterSheet.of(75, 40, 75, 50, 70, 30, 60, 25, 75),
                CharacterMasteryCollection.kenanCanonical());
        org.junit.jupiter.api.Assertions.assertTrue(kenanMobility.feintAnimationFrames() == 20, "La finta dura 20 frames.");
        org.junit.jupiter.api.Assertions.assertTrue(kenanMobility.invulnerabilityFrames() == 0,
                "La finta genérica no concede invulnerabilidad.");

        SwimmingPolicy swimming = new SwimmingPolicy();
        var swimmingTick = swimming.tick(10.0, 3.0, false);
        org.junit.jupiter.api.Assertions.assertTrue(swimmingTick.state() == SwimmingState.SWIMMING, "Con PA restante continúa nadando.");
        requireClose(swimmingTick.staminaAfter(), 7.0);

        var drowning = swimming.tick(1.0, 1.0, false);
        org.junit.jupiter.api.Assertions.assertTrue(drowning.state() == SwimmingState.DEAD_BY_DROWNING,
                "Al llegar a 0 PA sin hacer pie muere inmediatamente.");

        var grounded = swimming.tick(0.0, 10.0, true);
        org.junit.jupiter.api.Assertions.assertTrue(grounded.state() == SwimmingState.GROUNDED,
                "Hacer pie impide la muerte por ahogamiento.");
    }

    

    private static void requireClose(double actual, double expected) {
        if (Math.abs(actual - expected) > 0.0001) {
            throw new AssertionError("Esperado " + expected + ", obtenido " + actual);
        }
    }
}
