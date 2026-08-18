package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.*;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.firearms.CoupDeGracePolicy;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.CurrencyType;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.logistics.PersonalTransportType;
import java.util.List;

/** Verificación : uso general desde transporte y GOLPE DE GRACIA compartido. */
public final class MountedThrowingAndCoupDeGraceVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        var policy = new PersonalTransportItemUsePolicy();
        var throwing = ThrowingWeaponCatalog.all();
        org.junit.jupiter.api.Assertions.assertTrue(throwing.size() == 4, "Catálogo arrojadizo canónico.");
        throwing.forEach(item -> verifyAllTransportProperties(item, policy));

        List.of(MiscellaneousItemCatalog.stimulantInjection(), MiscellaneousItemCatalog.willowBark(),
                MiscellaneousItemCatalog.mead(), MiscellaneousItemCatalog.lucidityEssence(),
                MiscellaneousItemCatalog.pebble(), new CurrencyStack(CurrencyType.VALERITA, 10))
                .forEach(item -> verifyAllTransportProperties(item, policy));

        var knife = ThrowingWeaponCatalog.throwingKnifeV881();
        org.junit.jupiter.api.Assertions.assertTrue(knife.hasCoupDeGraceProperty(), "Cuchillo conserva GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(CoupDeGracePolicy.totalHealthAfterImpact(100, true, 99, 24, 25) == 0,
                "GOLPE DE GRACIA reduce PV TOTALES a cero.");
        org.junit.jupiter.api.Assertions.assertTrue(CoupDeGracePolicy.totalHealthAfterImpact(100, true, 100, 0, 100) == 100,
                "Cobertura total no debe bloquear GOLPE DE GRACIA si la perforación supera la protección.");

        var pebble = AmmunitionCatalog.pebble().ammunitionDescriptor();
        var arrow = AmmunitionCatalog.piercingArrow().ammunitionDescriptor();
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.sling().hasProperty(ItemPropertyId.COUP_DE_GRACE), "Honda implementa GOLPE DE GRACIA aunque su munición actual no aporte P.");
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.simpleRecurveBow().isCoupDeGrace(true, 99, 59, arrow), "Arco recurvo implementa GOLPE DE GRACIA.");
        org.junit.jupiter.api.Assertions.assertTrue(RangedWeaponCatalog.compositeBow().isCoupDeGrace(true, 99, 69, arrow), "Arco compuesto implementa GOLPE DE GRACIA.");
    }

    private static void verifyAllTransportProperties(domain.inventory.InventoryEntry item, PersonalTransportItemUsePolicy policy) {
        for (ItemPropertyId id : List.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN, ItemPropertyId.BICYCLAR, ItemPropertyId.MOTORCYCLAR))
            org.junit.jupiter.api.Assertions.assertTrue(item.properties().stream().anyMatch(p -> p.id() == id), item.name()+" incluye "+id);
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsPassenger(item), item.name()+" permite COPILOTO.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsDriver(item, PersonalTransportType.HORSE_LEISURE), item.name()+" permite ECUESTRE.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsDriver(item, PersonalTransportType.BICYCLE_FOLDING_V881), item.name()+" permite BICICLAR.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsDriver(item, PersonalTransportType.MOTORCYCLE_CARDAN_V881), item.name()+" permite MOTOCICLAR.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.forcedSlotWhileDriving(item, PersonalTransportType.MOTORCYCLE_CARDAN_V881) == EquipmentSlot.LEFT_HAND,
                item.name()+" usa LEFT HAND al motociclar.");
    }
    
}
