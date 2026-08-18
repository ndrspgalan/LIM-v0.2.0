package qa.domain;

import domain.ability.*;

/** el Grimorio separa narrativa doctrinal y contrato técnico en todas las unidades visibles. */
public final class MasteryGrimoireVerification {
    private MasteryGrimoireVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        for (Mastery mastery : MasteryCatalog.canonical().values()) {
            org.junit.jupiter.api.Assertions.assertTrue(!mastery.narrativeDescription().isBlank(), mastery.name() + " carece de narrativa general.");
            if (mastery instanceof PairMastery pair) {
                org.junit.jupiter.api.Assertions.assertTrue(!pair.original().narrativeDescription().isBlank(), pair.original().name() + " carece de narrativa propia.");
                org.junit.jupiter.api.Assertions.assertTrue(!pair.refined().narrativeDescription().isBlank(), pair.refined().name() + " carece de narrativa propia.");
                org.junit.jupiter.api.Assertions.assertTrue(!pair.original().mechanicalDescription().isBlank(), pair.original().name() + " carece de ficha técnica.");
                org.junit.jupiter.api.Assertions.assertTrue(!pair.refined().mechanicalDescription().isBlank(), pair.refined().name() + " carece de ficha técnica.");
            }
        }

        PairMastery refinement = (PairMastery) MasteryCatalog.require(MasteryId.EXPLOSION_CINETICA);
        org.junit.jupiter.api.Assertions.assertTrue(refinement.narrativeDescription().contains("obligada a alcanzar"), "REFINAMIENTO debe conservar la formulación canónica del gradiente.");
        org.junit.jupiter.api.Assertions.assertTrue(refinement.original().mechanicalDescription().contains("2,5 veces la altura")
                        && refinement.original().mechanicalDescription().contains("AGUANTE")
                        && refinement.original().mechanicalDescription().contains("StaggerPolicy"),
                "EXPLOSIÓN CINÉTICA debe documentar radio, daño y stagger.");
        org.junit.jupiter.api.Assertions.assertTrue(refinement.refined().mechanicalDescription().contains("refleja al atacante")
                        && refinement.refined().mechanicalDescription().contains("cuerpo a cuerpo"),
                "ENDURECIMIENTO POTENCIAL debe documentar su reflexión melee.");

        PairMastery nullification = (PairMastery) MasteryCatalog.require(MasteryId.ANULACION);
        org.junit.jupiter.api.Assertions.assertTrue(nullification.narrativeDescription().contains("frame breaking"), "ANULACIÓN debe explicitar frame breaking.");
        org.junit.jupiter.api.Assertions.assertTrue(nullification.original().narrativeDescription().contains("significado"), "INCIDENTAL debe explicar contaminación simbólica.");
        org.junit.jupiter.api.Assertions.assertTrue(nullification.refined().narrativeDescription().contains("actuando"), "FUNDACIONAL debe explicar ruptura del performance.");

        StructuredMastery healing = (StructuredMastery) MasteryCatalog.require(MasteryId.SANAR);
        MasteryStage restore = healing.stages().stream().filter(s -> s.name().equals("RESTAURAR")).findFirst().orElseThrow();
        MasteryStage custody = healing.stages().stream().filter(s -> s.name().equals("CUSTODIA")).findFirst().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(restore.narrativeDescriptionOptional().orElse("").contains("polarización inversa"), "RESTAURAR debe explicar la inversión.");
        org.junit.jupiter.api.Assertions.assertTrue(custody.narrativeDescriptionOptional().orElse("").contains("amortiguación social"), "CUSTODIA debe explicar social buffering.");
        org.junit.jupiter.api.Assertions.assertTrue(custody.narrativeDescriptionOptional().orElse("").contains("otros crean"), "CUSTODIA debe cerrar la ironía de FE.");

        StructuredMastery invisibility = (StructuredMastery) MasteryCatalog.require(MasteryId.INVISIBILIDAD);
        org.junit.jupiter.api.Assertions.assertTrue(invisibility.narrativeDescription().contains("desapego total del yo"), "INVISIBILIDAD debe fundamentar la desnudez en desapego del yo.");
        org.junit.jupiter.api.Assertions.assertTrue(invisibility.stages().get(0).mechanicalDescription().contains("desenvainar/equipar"), "INVISIBILIDAD debe documentar qué rompe el estado.");

        org.junit.jupiter.api.Assertions.assertTrue(MasteryCatalog.require(MasteryId.HOMEOSTASIS_TERMICA).narrativeDescription().contains("microclimática"),
                "HOMEOSTASIS debe usar la interfaz microclimática.");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryCatalog.require(MasteryId.ESPIRITU_INFATIGABLE).narrativeDescription().contains("segunda infancia"),
                "ESPÍRITU INFATIGABLE debe explicitar la segunda infancia.");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryCatalog.require(MasteryId.TRAYECTORIA_CONVERGENTE).narrativeDescription().contains("Flow"),
                "TRAYECTORIA debe explicar el Flow.");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryCatalog.require(MasteryId.TRIBOGENESIS).narrativeDescription().contains("CONFIGURATIO ORIGINALIS")
                        && MasteryCatalog.require(MasteryId.ELECTROGENESIS).narrativeDescription().contains("CONFIGURATIO ORIGINALIS"),
                "Las evolutivas deben compartir CONFIGURATIO ORIGINALIS.");
    }

    
}
