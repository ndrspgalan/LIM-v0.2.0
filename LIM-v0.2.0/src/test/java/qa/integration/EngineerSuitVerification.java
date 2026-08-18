package qa.integration;

import domain.combat.*;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;

import java.util.Map;

public final class EngineerSuitVerification {
    private static final double EPS = 1.0e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        ArmorPiece suit = ArmorCatalog.engineerSuit();
        org.junit.jupiter.api.Assertions.assertTrue(suit.material() == ArmorMaterial.ELECTROMECHANICAL_COMPOSITE, "Material canónico incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(suit.inventoryCategory().orElseThrow() == ArmorInventoryCategory.INTEGRAL_SUIT, "Debe ser traje integral.");
        org.junit.jupiter.api.Assertions.assertTrue(close(suit.bodyCoverageRatio(), 1.0) && close(suit.headCoverageRatio(), 1.0), "Debe cubrir 100 % de cabeza y cuerpo.");
        org.junit.jupiter.api.Assertions.assertTrue(suit.footprint().equals(domain.inventory.logistics.ArmorPhysicalDimensionsCatalog.technicalSuitFootprintFor(suit.name())), "Geometría del Ingeniero debe derivar de XYZ.");

        for (ItemPropertyId id : new ItemPropertyId[]{
                ItemPropertyId.INTEGRAL_SEAL,
                ItemPropertyId.ELECTROMECHANICAL_STABILITY,
                ItemPropertyId.SERVOMOTOR_CAPACITY,
                ItemPropertyId.HYDROMECHANICAL_ASSISTANCE,
                ItemPropertyId.MATERIAL_SYNERGY,
                ItemPropertyId.INTRICATE_MANUFACTURE}) {
            org.junit.jupiter.api.Assertions.assertTrue(suit.hasProperty(id), "Falta la propiedad " + id);
        }

        EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.CHEST, suit));
        ArmorCoverageResolver coverage = new ArmorCoverageResolver();
        org.junit.jupiter.api.Assertions.assertTrue(coverage.applicableArmor(ArmorHitLocation.HEAD, equipment).contains(suit), "El traje debe cubrir cabeza desde una sola ranura.");
        org.junit.jupiter.api.Assertions.assertTrue(coverage.applicableArmor(ArmorHitLocation.BODY, equipment).contains(suit), "El traje debe cubrir cuerpo.");

        ArmorDamageResolver damageResolver = new ArmorDamageResolver();
        var layout = domain.inventory.equipment.ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST, ArmorLayerPosition.MIDDLE, suit);
        ArmorImpactResult head = damageResolver.resolve(new PhysicalDamage(0, 0, 100), ArmorCombatHitbox.HELMET, layout, 999);
        org.junit.jupiter.api.Assertions.assertTrue(close(head.netDamage().blunt(), 20.0), "SINERGIA MATERIAL debe inhibir el x1,5 contundente de cabeza.");
        org.junit.jupiter.api.Assertions.assertTrue(close(suit.currentBluntProtection(), 78.0), "MANUFACTURA INTRINCADA debe aplicar desgaste x2.");

        NonConventionalDamageResolver nonConventional = new NonConventionalDamageResolver();
        for (DamageType type : new DamageType[]{DamageType.POISON, DamageType.BURN, DamageType.FROST, DamageType.ELECTRICITY}) {
            org.junit.jupiter.api.Assertions.assertTrue(close(nonConventional.resolve(type, 50, ArmorHitLocation.BODY, equipment, 0, false).netDamage(), 0),
                    "Los subsistemas especializados del Ingeniero deben anular " + type);
        }
    }

    private static boolean close(double a, double b) { return Math.abs(a - b) < EPS; }
    
}
