package qa.domain;

import domain.character.sheet.Attribute;
import domain.combat.ProjectileMirrorParryPolicy;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.InventoryState;
import domain.inventory.RotorBackHandService;
import domain.inventory.QuickAccessBar;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.logistics.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class CompactSpecialWeaponsAndDorsalSystemVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        WeaponItem helical = MeleeWeaponCatalog.espadaHelicoidal();
        close(helical.reachMeters(), 1.10, "Longitud helicoidal");
        close(helical.weightKg(), 1.16, "Peso helicoidal");
        org.junit.jupiter.api.Assertions.assertTrue(helical.footprint().equals(new domain.inventory.InventoryFootprint(2, 11)), "La Helicoidal ocupa 11 x 1.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(helical, Attribute.FUERZA) == 9, "La Helicoidal requiere FUERZA 9.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(helical, Attribute.DESTREZA) == 11, "La Helicoidal requiere DESTREZA 11.");
        org.junit.jupiter.api.Assertions.assertTrue(helical.statistics().stream().anyMatch(v -> v.contains("Cinto reforzado")), "La Helicoidal se transporta en el cinto.");
        var projectile = new ProjectileMirrorParryPolicy().resolve(
                helical, WeaponCombatAction.LIGHT_ATTACK, FirearmCatalog.repeatingPneumaticRifleV881(), true);
        org.junit.jupiter.api.Assertions.assertTrue(projectile.successful() && projectile.originalTrajectoryCancelled() && !projectile.redirectedTowardShooter(),
                "Mirror Parry debe desviar proyectiles sin devolverlos automáticamente.");

        WeaponItem rotor = MeleeWeaponCatalog.espadonDeRotor();
        close(rotor.reachMeters(), 1.30, "Longitud rotor");
        close(rotor.weightKg(), 3.80, "Peso rotor");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(rotor, Attribute.FUERZA) == 38, "El Rotor 2H PRIMARY requiere FUERZA 38.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(rotor, Attribute.DESTREZA) == 13, "El Rotor requiere DESTREZA 13.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.footprint().equals(new domain.inventory.InventoryFootprint(2, 9)), "El Rotor retraído ocupa 9 x 2.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.deployedFootprint().equals(new domain.inventory.InventoryFootprint(2, 13)), "El Rotor desplegado ocupa 13 x 2.");

        DorsalRotorTransportPolicy dorsalPolicy = new DorsalRotorTransportPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(dorsalPolicy.combinedWeightKg(rotor) - 7.0) < 1e-9, "Sistema dorsal + Rotor deben pesar 7 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(dorsalPolicy.canDock(rotor), "El Rotor retraído debe poder acoplarse.");

        Map<InventoryCompartmentType, InventoryCompartment> compartments = new EnumMap<>(InventoryCompartmentType.class);
        for (InventoryCompartmentType type : InventoryCompartmentType.values()) {
            boolean available = type == InventoryCompartmentType.BODY || type == InventoryCompartmentType.DORSAL_ROTOR_SYSTEM;
            compartments.put(type, InventoryCompartment.empty(type, available));
        }
        LogisticsState logistics = new LogisticsState(compartments, PersonalTransportState.none());
        InventoryState inventory = new InventoryState(EquipmentState.empty(), QuickAccessBar.empty(), logistics);
        inventory = RotorBackHandService.equipRetractedRotor(inventory, rotor);
        org.junit.jupiter.api.Assertions.assertTrue(RotorBackHandService.equippedRotor(inventory).orElseThrow() == rotor,
                ": el sistema dorsal habilita BACK_HAND; no almacena el Rotor en su grid.");

        compartments.put(InventoryCompartmentType.BACKPACK, InventoryCompartment.empty(InventoryCompartmentType.BACKPACK, true));
        boolean rejected = false;
        try { new LogisticsState(compartments, PersonalTransportState.none()); }
        catch (IllegalArgumentException expected) { rejected = true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected, "Mochila y sistema dorsal deben ser incompatibles.");
    }

    private static int requirement(WeaponItem item, Attribute attribute) {
        return item.requirements().stream().filter(r -> r.attribute() == attribute)
                .findFirst().orElseThrow().minimumValue();
    }
    private static void close(double actual, double expected, String label) {
        if (Math.abs(actual - expected) > 1e-9) throw new AssertionError(label + ": " + actual + " != " + expected);
    }
    
}
