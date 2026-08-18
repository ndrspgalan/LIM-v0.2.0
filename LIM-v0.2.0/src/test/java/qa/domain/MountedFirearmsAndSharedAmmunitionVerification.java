package qa.domain;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.PersonalTransportItemUsePolicy;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.logistics.PersonalTransportType;
import java.util.Set;

/** Verificación : matriz de Transporte Personal, munición .46 compartida y ópticas canónicas. */
public final class MountedFirearmsAndSharedAmmunitionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var policy = new PersonalTransportItemUsePolicy();
        var pneumatic = FirearmCatalog.repeatingPneumaticRifleV881();
        var bifilar = FirearmCatalog.bifilarElectromagneticRifleV881();
        var pistol = FirearmCatalog.autoloadingPistolV881();
        var submachine = FirearmCatalog.submachineGunV881();
        var repeating = FirearmCatalog.repeatingRifleV881();
        var antiMateriel = FirearmCatalog.antiMaterielCannonV881();
        var cluster = FirearmCatalog.clusterCannonV881();
        var arc = FirearmCatalog.arcInductionLanceV881();
        var lime = FirearmCatalog.limeSprayerV881();

        verify(pneumatic, Set.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN), policy);
        verify(bifilar, Set.of(), policy);
        verify(pistol, Set.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN, ItemPropertyId.BICYCLAR, ItemPropertyId.MOTORCYCLAR), policy);
        verify(submachine, Set.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN), policy);
        verify(repeating, Set.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN), policy);
        verify(antiMateriel, Set.of(), policy);
        verify(cluster, Set.of(), policy);
        verify(arc, Set.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN), policy);
        verify(lime, Set.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN, ItemPropertyId.BICYCLAR, ItemPropertyId.MOTORCYCLAR), policy);

        org.junit.jupiter.api.Assertions.assertTrue(policy.forcedSlotWhileDriving(pistol, PersonalTransportType.MOTORCYCLE_CARDAN_V881) == EquipmentSlot.LEFT_HAND,
                "MOTOCICLAR fuerza LEFT_HAND.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canUseAsDriver(submachine, PersonalTransportType.BICYCLE_MILITARY_V881),
                "El Subfusil no admite BICICLAR.");
        org.junit.jupiter.api.Assertions.assertTrue(!policy.canUseAsDriver(submachine, PersonalTransportType.MOTORCYCLE_CARDAN_V881),
                "El Subfusil no admite MOTOCICLAR.");

        var cartridge = AmmunitionCatalog.pneumaticLead46Cartridge();
        org.junit.jupiter.api.Assertions.assertTrue(cartridge.ammunitionDescriptor().compatibleWith(pneumatic.ammunitionRequirement()),
                "Honda y Rifle Neumático comparten el descriptor .46 de plomo.");
        double fullWeight = cartridge.weightKg();
        org.junit.jupiter.api.Assertions.assertTrue(cartridge.consumeShots(1), "La Honda puede extraer una bala.");
        org.junit.jupiter.api.Assertions.assertTrue(cartridge.roundsRemaining() == 19, "Extraer para Honda deja 19 balas.");
        org.junit.jupiter.api.Assertions.assertTrue(cartridge.weightKg() < fullWeight, "El peso del cartucho refleja las balas restantes.");
        org.junit.jupiter.api.Assertions.assertTrue(cartridge.consumeShots(19) && cartridge.depleted(), "El cartucho se agota por unidades internas.");

        org.junit.jupiter.api.Assertions.assertTrue(FirearmCatalog.REPEATING_RIFLE_NARRATIVE.contains("PRECISIÓN ASISTIDA"),
                "La narrativa documenta precisión asistida.");
        org.junit.jupiter.api.Assertions.assertTrue(!FirearmCatalog.REPEATING_RIFLE_NARRATIVE.contains("multiplican únicamente el alcance"),
                "La narrativa ya no multiplica alcance mediante ópticas.");
        org.junit.jupiter.api.Assertions.assertTrue(repeating.effectiveRangeMeters() == 1500.0, "El Fusil de Repetición conserva 1.500 m.");
    }

    private static void verify(domain.inventory.item.firearms.FirearmItem firearm, Set<ItemPropertyId> expected,
                               PersonalTransportItemUsePolicy policy) {
        for (ItemPropertyId id : Set.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN,
                ItemPropertyId.BICYCLAR, ItemPropertyId.MOTORCYCLAR)) {
            boolean present = firearm.properties().stream().anyMatch(property -> property.id() == id);
            org.junit.jupiter.api.Assertions.assertTrue(present == expected.contains(id), firearm.name() + " matriz incorrecta para " + id);
        }
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsPassenger(firearm) == expected.contains(ItemPropertyId.COPILOT), firearm.name()+" COPILOTO.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsDriver(firearm, PersonalTransportType.HORSE_LEISURE) == expected.contains(ItemPropertyId.EQUESTRIAN), firearm.name()+" ECUESTRE.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsDriver(firearm, PersonalTransportType.BICYCLE_MILITARY_V881) == expected.contains(ItemPropertyId.BICYCLAR), firearm.name()+" BICICLAR.");
        org.junit.jupiter.api.Assertions.assertTrue(policy.canUseAsDriver(firearm, PersonalTransportType.MOTORCYCLE_CARDAN_V881) == expected.contains(ItemPropertyId.MOTORCYCLAR), firearm.name()+" MOTOCICLAR.");
    }

    
}
