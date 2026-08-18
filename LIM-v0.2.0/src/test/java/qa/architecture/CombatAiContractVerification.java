package qa.architecture;

import domain.combat.ai.declarative.CombatDecisionContext;
import domain.combat.ai.declarative.CombatDecisionRequest;
import domain.combat.ai.declarative.DeclarativeCombatModel;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

/**  — guardia de consolidación: una sola fachada declarativa y ningún decisor heurístico obsoleto. */
public final class CombatAiContractVerification {
    private static final Set<String> FORBIDDEN_SOURCES = Set.of(
            "src/main/java/domain/combat/ai/encounter/TargetSelectionPolicy.java",
            "src/main/java/domain/combat/ai/execution/HostileCombatController.java",
            "src/main/java/domain/combat/ai/remote/RemoteTacticalPolicy.java",
            "src/main/java/domain/combat/ai/remote/RemoteTacticalScorePolicy.java",
            "src/main/java/domain/combat/ai/kit/CombatConsumablePolicy.java",
            "src/main/java/domain/combat/ai/kit/MasteryTacticalPolicy.java",
            "src/main/java/domain/combat/ai/kit/TransmutationTacticalPolicy.java",
            "src/main/java/domain/combat/ai/kit/ExternalResourceTacticalPolicy.java",
            "src/main/java/domain/combat/ai/transport/PersonalTransportTacticalPolicy.java"
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        Method[] publicSnapshots = Arrays.stream(DeclarativeCombatModel.class.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getName().equals("snapshot"))
                .toArray(Method[]::new);
        org.junit.jupiter.api.Assertions.assertTrue(publicSnapshots.length == 1, "DeclarativeCombatModel debe tener una sola fachada snapshot pública.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.equals(publicSnapshots[0].getParameterTypes(), new Class<?>[]{CombatDecisionRequest.class}),
                "La única fachada snapshot debe recibir CombatDecisionRequest.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(DeclarativeCombatModel.class.getDeclaredMethods()).noneMatch(method -> method.getName().equals("decide")),
                "LIM no debe conservar decide() en la fachada declarativa.");

        Set<String> forbiddenContextFields = Set.of("score", "priority", "selectedTarget", "targetSelection", "tacticalUtility");
        for (RecordComponent component : CombatDecisionContext.class.getRecordComponents()) {
            org.junit.jupiter.api.Assertions.assertTrue(!forbiddenContextFields.contains(component.getName()),
                    "CombatDecisionContext no debe contener selección/scoring local: " + component.getName());
        }

        for (String source : FORBIDDEN_SOURCES) {
            org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(Path.of(source)), "Debe eliminarse la fuente obsoleto: " + source);
        }

        try (var sources = Files.walk(Path.of("src/main/java"))) {
            boolean deprecatedMarker = sources.filter(path -> path.toString().endsWith(".java"))
                    .anyMatch(path -> {
                        try {
                            String text = Files.readString(path);
                            return text.contains("@" + "Deprecated") || text.contains("@" + "deprecated");
                        } catch (Exception exception) {
                            throw new RuntimeException(exception);
                        }
                    });
            org.junit.jupiter.api.Assertions.assertTrue(!deprecatedMarker, " no debe dejar APIs Java marcadas deprecated en src.");
        }
    }

    
}
