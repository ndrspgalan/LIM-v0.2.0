package qa.domain;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.accessory.AccessoryCatalog;

import java.util.LinkedHashMap;
import java.util.Map;

/** sincroniza los bonificadores de CARISMA de Trofeos de Caza con la ficha canónica ferae. */
public final class HuntingTrophyCharismaVerification {
    private HuntingTrophyCharismaVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        Map<AccessoryItem, Integer> expected = new LinkedHashMap<>();
        expected.put(AccessoryCatalog.ratTail(), 1);
        expected.put(AccessoryCatalog.crowFeather(), 2);
        expected.put(AccessoryCatalog.pigHoof(), 3);
        expected.put(AccessoryCatalog.horseHair(), 4);
        expected.put(AccessoryCatalog.armadilloShell(), 5);
        expected.put(AccessoryCatalog.deerAntler(), 6);
        expected.put(AccessoryCatalog.bullEar(), 7);
        expected.put(AccessoryCatalog.snakeSkin(), 8);
        expected.put(AccessoryCatalog.boarTusk(), 9);
        expected.put(AccessoryCatalog.lynxEye(), 10);
        expected.put(AccessoryCatalog.eagleClaws(), 11);
        expected.put(AccessoryCatalog.wolfSkull(), 12);
        expected.put(AccessoryCatalog.lionMane(), 13);
        expected.put(AccessoryCatalog.bearPaw(), 14);
        expected.put(AccessoryCatalog.rhinocerosHorn(), 15);

        CharacterSheet base = CharacterSheet.of(20, 20, 20, 20, 20, 20, 20, 20, 20);
        expected.forEach((item, bonus) -> {
            EquipmentState equipment = new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, item));
            org.junit.jupiter.api.Assertions.assertTrue(equipment.attributeBonus(Attribute.CARISMA, base) == bonus,
                    item.name() + " debe aportar +" + bonus + " CARISMA.");
            org.junit.jupiter.api.Assertions.assertTrue(item.statistics().contains("+" + bonus + " CARISMA"),
                    item.name() + " debe mostrar +" + bonus + " CARISMA.");
        });
    }

    
}
