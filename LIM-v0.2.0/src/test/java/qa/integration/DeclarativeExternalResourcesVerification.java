package qa.integration;

/** Guardia documental . La suite acumulada no se ejecuta durante iteraciones normales. */
public final class DeclarativeExternalResourcesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(classPresent("domain.combat.ai.declarative.ExternalResourceActionCandidateResolver"),"Resolver externo presente.");
        org.junit.jupiter.api.Assertions.assertTrue(classPresent("domain.combat.ai.declarative.ExternalResourceDecisionState"),"Estado externo presente.");
        org.junit.jupiter.api.Assertions.assertTrue(classPresent("domain.combat.ai.declarative.FeraeLootFact"),"Hecho de pillaje Ferae presente.");
        org.junit.jupiter.api.Assertions.assertTrue(!classPresent("domain.bestiarium.physical_plane.ferae.FeraeTrophyLootMemory"),"La memoria temporal de mucus/trofeo queda retirada.");
    }
    private static boolean classPresent(String n){try{Class.forName(n);return true;}catch(ClassNotFoundException e){return false;}}
    
}
