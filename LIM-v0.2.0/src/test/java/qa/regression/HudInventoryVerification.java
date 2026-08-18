package qa.regression;

import domain.hud.EngineerSpineIndicator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/** Verificación canónica: el HUD provisional fue retirado. */
public final class HudInventoryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        Set<String> components = Arrays.stream(EngineerSpineIndicator.class.getRecordComponents())
                .map(component -> component.getName())
                .collect(Collectors.toSet());
        org.junit.jupiter.api.Assertions.assertTrue(components.equals(Set.of("visible", "levelRatio", "chromaticVariant")),
                "La única proyección persistente debe ser el indicador vertebral diegético.");
    }

    
}
