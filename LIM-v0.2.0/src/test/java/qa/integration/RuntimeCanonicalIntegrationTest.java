package qa.integration;

import application.start.CanonicalGameStartFactory;
import application.save.InventorySnapshotCodec;
import application.save.InventorySnapshotHydrator;
import domain.ability.MasteryCatalog;
import domain.ability.MasteryCategory;
import domain.ability.MasteryId;
import domain.ability.MasteryKnowledgeState;
import domain.character.canonical.CanonicalCharacterTimelineCatalog;
import domain.character.canonical.CanonicalChildLoadoutCatalog;
import domain.character.canonical.CanonicalLifeStage;
import domain.character.progression.MucusType;
import domain.control.ControlAction;
import domain.control.InputGesture;
import domain.control.PcControlScheme;
import domain.inventory.equipment.EquipmentSlot;
import domain.save.snapshot.TransportSnapshot;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Tag("gold-smoke")
@Tag("integration")
final class RuntimeCanonicalIntegrationTest {
    @Test
    void canonicalStartUsesBackendAuthoritiesInsteadOfBootstrapConstants() {
        var start = CanonicalGameStartFactory.kenanChild();
        var game = start.game();
        assertEquals(6, domain.character.KenanCanonicalProfile.AGE_YEARS);
        assertEquals(9, game.level());
        for (var type : MucusType.values()) assertEquals(0.0, game.progression().mucusWallet().quantityMlOf(type));
        assertFalse(game.currentInventory().armorLayout().layers().isEmpty());
        assertTrue(game.currentInventory().equipment().itemAt(EquipmentSlot.CHEST).isPresent());
        assertTrue(game.currentInventory().equipment().itemAt(EquipmentSlot.LEGGINGS).isPresent());
        assertTrue(game.currentInventory().equipment().itemAt(EquipmentSlot.FEET).isPresent());
        assertEquals(java.util.Set.of("REGENERACION_THETA", "ESPIRITU_INFATIGABLE"), game.masteries().unlockedMasteryIds());
        assertFalse(game.masteries().revealedMasteryIds().isEmpty(), "Las afinidades de clase deben estar reveladas, no desbloqueadas.");
        assertTrue(game.masteries().visibleIds().stream().anyMatch(id -> game.masteries().knowledgeState(id) == MasteryKnowledgeState.REVEALED));
        for (var id : MasteryId.values()) {
            if (MasteryCatalog.require(id).category() == MasteryCategory.EVOLUTIVE)
                assertEquals(MasteryKnowledgeState.UNKNOWN, game.masteries().knowledgeState(id));
        }
    }

    @Test
    void everyCanonicalChildIsDressedLightlyAndOnlyReceivesProvisionsThatFit() {
        var children = CanonicalCharacterTimelineCatalog.all().stream()
                .filter(p -> p.stage() == CanonicalLifeStage.CHILD).toList();
        assertEquals(8, children.size());
        for (var child : children) {
            var loadout = CanonicalChildLoadoutCatalog.forProfile(child);
            assertEquals(4, loadout.armorLayout().layers().size(), child.name());
            double clothingKg = loadout.armorLayout().layers().stream().mapToDouble(l -> l.piece().weightKg()).sum();
            assertTrue(clothingKg > 0 && clothingKg <= 1.0, child.name() + " ropa=" + clothingKg);
            assertTrue(loadout.inventory().equipment().itemAt(EquipmentSlot.RIGHT_HAND).isEmpty());
            assertTrue(loadout.inventory().equipment().itemAt(EquipmentSlot.LEFT_HAND).isEmpty());
            assertTrue(loadout.inventory().equipment().itemAt(EquipmentSlot.ACCESSORY).isEmpty());
            assertTrue(loadout.inventory().equipment().itemAt(EquipmentSlot.RUNIC_MARK).isEmpty());
            assertFalse(loadout.provisionNames().isEmpty(), child.name() + " debe llevar al menos una provisión si cabe");
        }
    }

    @Test
    void personalTransportBindingsAndPauseFallbackRemainExposed() throws Exception {
        assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.input().equals("B") && b.gesture()== InputGesture.PRESS && b.action()== ControlAction.CALL_PERSONAL_TRANSPORT));
        assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.input().equals("B") && b.gesture()== InputGesture.HOLD && b.action()== ControlAction.OPEN_PERSONAL_TRANSPORT_WHEEL));
        String gameplay = Files.readString(Path.of("src/main/java/presentation/console/GameplayConsole.java"));
        assertTrue(gameplay.contains("CARGAR ÚLTIMO PUNTO DE GUARDADO"));
        assertFalse(gameplay.contains("CARGAR PERSONA"));
        assertTrue(gameplay.contains("inventoryScreen.open()"));
    }
    @Test
    void layeredChildClothingSurvivesInventorySnapshotRoundTrip() {
        var original = CanonicalGameStartFactory.kenanChild().game().currentInventory();
        var snapshot = InventorySnapshotCodec.snapshot(original);
        assertEquals(original.armorLayout().layers().size(), snapshot.armorLayers().size());
        var restored = InventorySnapshotHydrator.restore(snapshot, new TransportSnapshot("", java.util.List.of(), 0.0));
        assertEquals(original.armorLayout().layers().size(), restored.armorLayout().layers().size());
        assertTrue(restored.armorLayout().layers().stream().anyMatch(l -> l.slot() == EquipmentSlot.FEET && l.position().name().equals("INNER")));
        assertEquals(original.totalCarriedWeightKg(), restored.totalCarriedWeightKg(), 1e-9);
    }

}
