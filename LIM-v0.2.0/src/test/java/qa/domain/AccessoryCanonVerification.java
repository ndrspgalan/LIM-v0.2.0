package qa.domain;

import domain.ability.NullificationPolicy;
import domain.character.sheet.CharacterSheet;
import domain.combat.ElementalHealthRegenerationPolicy;
import domain.combat.HostileEncounterState;
import domain.combat.DamageType;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.AccessoryEffectType;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.runic.EffectImmunity;

import java.util.Map;

/** canon de abalorios románticos, Farolillo Lunar y Astilla del Maestro. */
public final class AccessoryCanonVerification {
    private AccessoryCanonVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyMasterSplinter();
        verifyLunarLantern();
        verifyRomanticImmunity();
    }

    private static void verifyMasterSplinter() {
        var splinter = AccessoryCatalog.masterSplinter();
        org.junit.jupiter.api.Assertions.assertTrue(splinter.name().equals("ASTILLA CON LA QUE CLAVARON A UN MAESTRO"), "La Astilla debe nombrar al Maestro.");
        org.junit.jupiter.api.Assertions.assertTrue(splinter.narrativeDescription().contains("clavado un maestro"), "La narrativa debe sustituir devoto por maestro.");
        org.junit.jupiter.api.Assertions.assertTrue(!splinter.narrativeDescription().toLowerCase().contains("devoto"), "La narrativa de la Astilla no debe conservar 'devoto'.");
    }

    private static void verifyLunarLantern() {
        var lunar = AccessoryCatalog.lunarLantern();
        org.junit.jupiter.api.Assertions.assertTrue(lunar.statistics().isEmpty(), "Farolillo Lunar no debe conservar estadísticas de FRÍO LUNAR.");
        org.junit.jupiter.api.Assertions.assertTrue(lunar.properties().size() == 1, "Farolillo Lunar debe tener exactamente una propiedad.");
        var anchor = lunar.properties().get(0);
        org.junit.jupiter.api.Assertions.assertTrue(anchor.id() == ItemPropertyId.EMBODIED_ANCHOR && anchor.hidden(), "La única propiedad debe ser ÁNCORA ENCARNADA oculta.");
        org.junit.jupiter.api.Assertions.assertTrue(lunar.effects().size() == 1 && lunar.effects().get(0).type() == AccessoryEffectType.VEIL_RIFT_NAVIGATION,
                "Farolillo Lunar sólo debe conservar el efecto de navegación del Áncora.");
    }

    private static void verifyRomanticImmunity() {
        CharacterSheet faith22 = sheet(22);
        CharacterSheet faith23 = sheet(23);
        var notebookProperty = AccessoryCatalog.kiaraNotebook().properties().stream()
                .filter(p -> p.id() == ItemPropertyId.GROW_OLD_TOGETHER).findFirst().orElseThrow();
        var braceletProperty = AccessoryCatalog.kenanBracelet().properties().stream()
                .filter(p -> p.id() == ItemPropertyId.QUEEN_WIFE_LOVE).findFirst().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(notebookProperty.hidden() && notebookProperty.activationRequirementHidden(), "¿ENVEJECEMOS JUNTOS? y FE 23 deben ser ocultos.");
        org.junit.jupiter.api.Assertions.assertTrue(braceletProperty.hidden() && braceletProperty.activationRequirementHidden(), "MI REINA... y FE 23 deben ser ocultos.");
        org.junit.jupiter.api.Assertions.assertTrue(notebookProperty.activationMinimum() == 23 && braceletProperty.activationMinimum() == 23,
                "FE 23 debe seguir gobernando mecánicamente ambas propiedades.");

        EquipmentState notebook = equipped(AccessoryCatalog.kiaraNotebook());
        EquipmentState bracelet = equipped(AccessoryCatalog.kenanBracelet());
        org.junit.jupiter.api.Assertions.assertTrue(!notebook.effectImmunities(faith22).contains(EffectImmunity.HEALTH_REGEN_PENALTIES), "Cuaderno no debe inmunizar con FE 22.");
        org.junit.jupiter.api.Assertions.assertTrue(!bracelet.effectImmunities(faith22).contains(EffectImmunity.HEALTH_REGEN_PENALTIES), "Pulsera no debe inmunizar con FE 22.");
        org.junit.jupiter.api.Assertions.assertTrue(notebook.effectImmunities(faith23).contains(EffectImmunity.HEALTH_REGEN_PENALTIES), "Cuaderno debe inmunizar con FE 23.");
        org.junit.jupiter.api.Assertions.assertTrue(bracelet.effectImmunities(faith23).contains(EffectImmunity.HEALTH_REGEN_PENALTIES), "Pulsera debe inmunizar con FE 23.");
        org.junit.jupiter.api.Assertions.assertTrue(!notebook.effectImmunities(faith23, NullificationPolicy.incidentalContact(60)).contains(EffectImmunity.HEALTH_REGEN_PENALTIES),
                "ANULACIÓN debe suprimir ¿ENVEJECEMOS JUNTOS?.");
        org.junit.jupiter.api.Assertions.assertTrue(!bracelet.effectImmunities(faith23, NullificationPolicy.incidentalContact(60)).contains(EffectImmunity.HEALTH_REGEN_PENALTIES),
                "ANULACIÓN debe suprimir MI REINA, MI ESPOSA, MI AMOR.");

        ElementalHealthRegenerationPolicy inhibition = new ElementalHealthRegenerationPolicy();
        HostileEncounterState encounter = new HostileEncounterState();
        encounter.begin();
        inhibition.registerDirectDamage(DamageType.BURN, 1.0, encounter);
        org.junit.jupiter.api.Assertions.assertTrue(!inhibition.healthRegenerationAllowed(encounter, faith22, notebook, NullificationPolicy.SuppressionState.none()),
                "Sin FE 23 la quemadura debe inhibir PV REGEN.");
        org.junit.jupiter.api.Assertions.assertTrue(inhibition.healthRegenerationAllowed(encounter, faith23, notebook, NullificationPolicy.SuppressionState.none()),
                "Con FE 23 ¿ENVEJECEMOS JUNTOS? debe impedir la inhibición de PV REGEN.");
        org.junit.jupiter.api.Assertions.assertTrue(!inhibition.healthRegenerationAllowed(encounter, faith23, notebook, NullificationPolicy.incidentalContact(60)),
                "ANULACIÓN debe restaurar la posibilidad de inhibir PV REGEN.");
    }

    private static EquipmentState equipped(domain.inventory.item.AccessoryItem item) {
        return new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, item));
    }

    private static CharacterSheet sheet(int faith) {
        return CharacterSheet.of(20,20,20,20,20,20,faith,20,40);
    }

    
}
