package application.simulation.combat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/** Fingerprint estable para detectar deriva de replay sin introducir JSON antes. */
public final class CombatScenarioFingerprint {
    private CombatScenarioFingerprint() {}
    public static String sha256(DeterministicCombatScenario s) {
        StringBuilder b = new StringBuilder().append(s.scenarioId()).append('|').append(s.seed().value())
                .append('|').append(s.scenarioIndex()).append('|').append(s.tick()).append('|').append(s.kind()).append('|').append(s.environment());
        for (var force : s.forces()) {
            b.append("|FORCE:").append(force.forceId());
            for (var squad : force.squads()) {
                b.append("|SQUAD:").append(squad.squadId()).append(':').append(squad.mission()).append(':').append(squad.compositionKind());
                for (var a : squad.members()) {
                    b.append("|ACTOR:").append(a.actorId())
                            .append(':').append(a.subprofession().map(Enum::name).orElse("-"))
                            .append(':').append(a.feraeSpecies().map(Enum::name).orElse("-"))
                            .append(':').append(a.feraeSex().map(Enum::name).orElse("-"))
                            .append(':').append(a.gender()).append(':').append(a.sheet())
                            .append(':').append(Double.toHexString(a.heightMeters()))
                            .append(':').append(Double.toHexString(a.currentPa()))
                            .append(':').append(Double.toHexString(a.totalPa()));
                }
            }
        }
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(b.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
}
