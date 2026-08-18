package qa.architecture;

import domain.combat.ai.declarative.*;
import java.lang.reflect.RecordComponent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**  sanea la verificación histórica de la IA decisora: LIM ya no decide, sólo declara. */
public final class HostileAiBehaviorVerification {
    private HostileAiBehaviorVerification() {}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(Path.of("src/main/java/domain/combat/ai/execution/HostileCombatController.java")),"HostileCombatController obsoleto debe haber desaparecido.");
        org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(Path.of("src/main/java/domain/combat/ai/encounter/TargetSelectionPolicy.java")),"TargetSelectionPolicy obsoleto debe haber desaparecido.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(CombatDecisionContext.class.getRecordComponents()).map(RecordComponent::getName)
                .noneMatch(n->n.equalsIgnoreCase("score")||n.equalsIgnoreCase("priority")||n.equalsIgnoreCase("selectedTarget")),
                "El contrato declarativo no puede contener score, prioridad ni target seleccionado.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(DeclarativeCombatModel.class.getDeclaredMethods()).noneMatch(m->m.getName().equals("decide")),
                "DeclarativeCombatModel no debe decidir.");
    }
    
}
