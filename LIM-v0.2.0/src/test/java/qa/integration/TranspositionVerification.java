package qa.integration;

import domain.character.progression.MucusType;
import domain.character.progression.MucusWallet;
import domain.character.sheet.CharacterSheet;
import domain.combat.WeaponDurabilityResolver;
import domain.combat.coating.WeaponCoatingService;
import domain.combat.runic.RunicCoatingDamagePolicy;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.*;
import domain.inventory.item.misc.MucusTearItem;
import domain.inventory.item.misc.UseAnimation;
import domain.inventory.item.misc.UseResourceKind;
import domain.inventory.item.misc.UtilityAction;
import domain.inventory.item.misc.UtilityObjectItem;
import domain.inventory.item.misc.WhetstonePolicy;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.item.misc.MucusCrystalItem;
import domain.runic.RunicMarkCatalog;
import domain.runic.RunicMarkId;
import domain.runic.transposition.TranspositionService;

import java.util.*;

public final class TranspositionVerification {
    private static final double EPS = 1e-9;

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        verifyLatentMarkDoesNothing();
        verifyWhiteFillsStacksWithoutLosingOverflow();
        verifySpecialMucusOperatesOneByOne();
        verifyCurseCoatingAndNonRecursiveChannel();
        verifyWearAndSharpeningRemoveCoating();
    }

    private static void verifyLatentMarkDoesNothing() {
        var service = new TranspositionService();
        var result = service.transposeWhite(MucusWallet.of(10,0,0,0,0,0), emptyBody(), sheet(20), transposition());
        org.junit.jupiter.api.Assertions.assertTrue(result.allowed(), "La Transposición no debe exigir FE 21.");
    }

    private static void verifyWhiteFillsStacksWithoutLosingOverflow() {
        MucusTearItem tear = new MucusTearItem(95);
        List<InventoryEntry> entries = new ArrayList<>();
        entries.add(tear);
        for (int i = 0; i < 13; i++) entries.add(dummy("D" + i));
        InventoryCompartment full = new InventoryCompartment(InventoryCompartmentType.BODY, true, entries);
        var result = new TranspositionService().transposeWhite(MucusWallet.of(20,0,0,0,0,0), full, sheet(33), transposition());
        org.junit.jupiter.api.Assertions.assertTrue(!result.allowed(), "Si el excedente necesita otra Lágrima y no hay espacio, la operación debe rechazarse.");
        org.junit.jupiter.api.Assertions.assertTrue(result.mucusConsumed() == 0 && result.wallet().quantityOf(MucusType.BLANCO) == 20, "No se consume mucus si no cabe el resultado completo.");
        org.junit.jupiter.api.Assertions.assertTrue(tear.currentUses() == 95 && result.itemsCreated() == 0, "La operación fallida debe ser atómica.");
    }

    private static void verifySpecialMucusOperatesOneByOne() {
        var service = new TranspositionService();
        var first = service.transposeOne(MucusType.NEGRUZCO, MucusWallet.of(0,0,0,0,0,1), emptyBody(), sheet(33), transposition());
        org.junit.jupiter.api.Assertions.assertTrue(first.allowed() && first.mucusConsumed() == 1 && first.compartment().entries().size() == 1, "Crea un cristal por operación.");
        org.junit.jupiter.api.Assertions.assertTrue(first.compartment().entries().getFirst() instanceof MucusCrystalItem crystal && crystal.sellable(), "El cristal es abalorio permanente y vendible.");
    }

    private static void verifyCurseCoatingAndNonRecursiveChannel() {
        WeaponItem weapon = weapon(3);
        MucusTearItem tear = new MucusTearItem(12);
        var result = new WeaponCoatingService().applyCurse(sheet(33), tear, weapon);
        org.junit.jupiter.api.Assertions.assertTrue(result.successful() && tear.currentUses() == 11, "Consume una única Lágrima por recubrimiento.");
        close(new RunicCoatingDamagePolicy().rawCurseDamage(weapon, weapon.modes().getFirst(), true), 3, "El daño maldito deriva de la contundencia vigente del perfil.");
        close(new RunicCoatingDamagePolicy().rawCurseDamage(weapon, weapon.modes().getFirst(), false), 0, "Un ataque fallido no aplica el canal.");
        org.junit.jupiter.api.Assertions.assertTrue(!new RunicCoatingDamagePolicy().origin().triggersRunicOffense(), "El recubrimiento no realimenta runas.");
    }

    private static void verifyWearAndSharpeningRemoveCoating() {
        WeaponItem worn = weapon(1);
        new WeaponCoatingService().applyCurse(sheet(33), new MucusTearItem(1), worn);
        worn.applyHeavyArmorWear(worn.modes().getFirst(), new domain.inventory.item.armor.ArmorProtectionProfile(1,1,1));
        org.junit.jupiter.api.Assertions.assertTrue(worn.coating().isEmpty(), "El desgaste completo elimina el recubrimiento.");

        WeaponItem sharpened = weapon(3);
        new WeaponCoatingService().applyCurse(sheet(33), new MucusTearItem(1), sharpened);
        UtilityObjectItem whetstone = new UtilityObjectItem("Piedra de afilar", "Piedra de verificación.", 1, 1,
                0.2, 0, UseResourceKind.DURABILITY, new InventoryFootprint(1,1),
                new UseAnimation(1, List.of("Afilar")), List.of(UtilityAction.SHARPEN));
        org.junit.jupiter.api.Assertions.assertTrue(new WhetstonePolicy().sharpen(whetstone, sharpened), "El afilado debe ser válido.");
        org.junit.jupiter.api.Assertions.assertTrue(sharpened.coating().isEmpty(), "Afilar elimina el recubrimiento.");
    }

    private static EquipmentState transposition() {
        return new EquipmentState(Map.of(EquipmentSlot.RUNIC_MARK, RunicMarkCatalog.require(RunicMarkId.TRANSPOSICION)));
    }

    private static InventoryCompartment emptyBody() {
        return InventoryCompartment.empty(InventoryCompartmentType.BODY, true);
    }

    private static InventoryEntry dummy(String name) {
        return new InventoryEntry(name, "Objeto de relleno.", 0, new InventoryFootprint(1,1), List.of());
    }

    private static WeaponItem weapon(int blunt) {
        return new WeaponItem("Arma", "Arma de verificación.", 1, new InventoryFootprint(1,1), 1,
                List.of(new WeaponMode("Modo", new LethalityProfile(0,1,blunt))), List.of(), List.of(), List.of(),
                OptionalDouble.empty(), 0, false,
                new WeaponConfigurationPolicy(List.of(new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY))), Set.of());
    }

    private static CharacterSheet sheet(int clairvoyance) { return CharacterSheet.of(30,30,30,30,30,30,30,30,clairvoyance); }
    private static void close(double a, double e, String m) { if (Math.abs(a-e) > EPS) throw new IllegalStateException(m + ": " + a + " != " + e); }
    
}
