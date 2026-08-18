package qa.domain;

import domain.combat.AeronautHelmetConfiguration;
import domain.combat.AeronautHelmetConfigurationPolicy;
import domain.combat.HostileEncounterState;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.logistics.ArmorPhysicalDimensionsCatalog;
import domain.inventory.logistics.InventoryVolumeProjectionPolicy;

public final class HeadArmorXyzAndAeronautStateVerification {
    private HeadArmorXyzAndAeronautStateVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var head = ArmorCatalog.allHeadArmor();
        org.junit.jupiter.api.Assertions.assertTrue(head.size() == 37, "HEAD canónico debe conservar 37 piezas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.headProfileCount() == 37, "Cada pieza HEAD debe declarar XYZ.");

        for (ArmorPiece piece : head) {
            var dims = ArmorPhysicalDimensionsCatalog.headDimensionsFor(piece.name());
            var expected = InventoryVolumeProjectionPolicy.footprint(dims);
            org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().equals(expected), "Footprint HEAD debe derivarse de XYZ: " + piece.name());
        }

        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.hardenedLeatherJetHelmet().footprint().verticalSlots() == 9
                && ArmorCatalog.hardenedLeatherJetHelmet().footprint().horizontalSlots() == 9,
                "Casco Jet rígido debe abandonar el antiguo 1x1.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.retractableAeronautHelmet().footprint().verticalSlots() == 9
                && ArmorCatalog.retractableAeronautHelmet().footprint().horizontalSlots() == 12,
                "Aeronauta usa envolvente XYZ rígida.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.enlightenedPanopticonCanonical().footprint().verticalSlots() == 12
                && ArmorCatalog.enlightenedPanopticonCanonical().footprint().horizontalSlots() == 12,
                "Panóptico usa envolvente celular rígida.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.charroHatV881().footprint().verticalSlots() == 36
                && ArmorCatalog.charroHatV881().footprint().horizontalSlots() == 18,
                "El sombrero charro conserva su gran volumen rígido real.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.beretV881().footprint().occupiedSlots() == 1,
                "La boina flexible debe seguir plegándose a volumen mínimo.");

        HostileEncounterState encounter = new HostileEncounterState();
        org.junit.jupiter.api.Assertions.assertTrue(AeronautHelmetConfigurationPolicy.resolve(encounter) == AeronautHelmetConfiguration.RETRACTED,
                "Fuera de encuentro hostil el Aeronauta debe estar replegado.");
        encounter.begin();
        org.junit.jupiter.api.Assertions.assertTrue(AeronautHelmetConfigurationPolicy.resolve(encounter) == AeronautHelmetConfiguration.DEPLOYED,
                "Durante encuentro hostil el Aeronauta debe desplegarse automáticamente.");
        org.junit.jupiter.api.Assertions.assertTrue(AeronautHelmetConfigurationPolicy.headProtectionOperational(encounter),
                "La protección integral del Aeronauta debe estar operativa durante combate hostil.");
        encounter.conclude();
        org.junit.jupiter.api.Assertions.assertTrue(AeronautHelmetConfigurationPolicy.resolve(encounter) == AeronautHelmetConfiguration.RETRACTED,
                "Al concluir el encuentro debe replegarse automáticamente.");

        var pano = ArmorCatalog.enlightenedPanopticonCanonical();
        org.junit.jupiter.api.Assertions.assertTrue(close(pano.protection().piercing(),40) && close(pano.protection().slashing(),100) && close(pano.protection().blunt(),100),
                " conserva el perfil asimétrico actualizado del Panóptico.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.paperHelmetV881().statistics().stream().anyMatch(s -> s.contains("Propiedad emergente")),
                "Casco de papel debe explicitar que su perfil pertenece al paquete multicapa.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.integralRespirator().narrativeDescription().contains("continuidad geométrica"),
                "Respirador debe justificar su cobertura HEAD 100%.");
    }

    private static boolean close(double a,double b){ return Math.abs(a-b)<1e-9; }
    
}
