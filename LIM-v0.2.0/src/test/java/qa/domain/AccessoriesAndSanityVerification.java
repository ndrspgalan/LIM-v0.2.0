package qa.domain;

import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.AccessoryContext;
import domain.inventory.item.AccessoryEffectType;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.accessory.AccessoryCatalog;

import java.util.Map;

public final class AccessoriesAndSanityVerification {
    private AccessoriesAndSanityVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyNotebooks();
        verifyRomanticGifts();
        verifyLanterns();
    }

    private static void verifyNotebooks() {
        AccessoryItem sketchBook = AccessoryCatalog.sketchBook();
        org.junit.jupiter.api.Assertions.assertTrue(sketchBook.name().equals("CUADERNO DEL DIBUJANTE"), "Nombre incorrecto del Cuaderno del Dibujante.");
        org.junit.jupiter.api.Assertions.assertTrue(sketchBook.narrativeDescription().contains("Comenzó a garabatearlo mientras permanecía en el Taller"),
                "Falta la narrativa canónica del Cuaderno del Dibujante.");
        org.junit.jupiter.api.Assertions.assertTrue(sketchBook.narrativeDescription().contains("Continente de Las Tierras Lapsas"),
                "La narrativa debe conservar el ámbito de los viajes de Kiara.");
        org.junit.jupiter.api.Assertions.assertTrue(sketchBook.properties().stream().anyMatch(property ->
                        property.id() == ItemPropertyId.THOUGHT_OF_THOUGHT
                                && property.narrativeDescription().startsWith("Y así, nuestra moderna concepción del mundo")),
                "PENSAMIENTO DE PENSAMIENTO debe usar la descripción canónica.");

        AccessoryItem notebook = AccessoryCatalog.kiaraNotebook();
        org.junit.jupiter.api.Assertions.assertTrue(notebook.name().equals("CUADERNO DE KIARA"), "El objeto debe seguir llamándose Cuaderno de Kiara.");
        org.junit.jupiter.api.Assertions.assertTrue(notebook.narrativeDescription().equals("Cuaderno de Kiara, deteriorado por el uso, con páginas arrancadas, "
                        + "huellas de haber sido pisoteado y daños provocados por las inclemencias. En sus páginas está escrita "
                        + "la historia de Kenan, el último Guerrero de Ébano."),
                "Descripción narrativa incorrecta del Cuaderno de Kiara.");
        org.junit.jupiter.api.Assertions.assertTrue(close(equipped(notebook).sanityBonus(sheet(23), AccessoryContext.day()), 2.0),
                "El Cuaderno de Kiara sólo conserva +2 Cordura de PENSAMIENTO DE PENSAMIENTO.");
        org.junit.jupiter.api.Assertions.assertTrue(equipped(notebook).effectImmunities(sheet(23)).contains(domain.runic.EffectImmunity.HEALTH_REGEN_PENALTIES),
                "¿ENVEJECEMOS JUNTOS? debe inmunizar frente a inhibición de PV REGEN con FE 23.");
    }

    private static void verifyRomanticGifts() {
        AccessoryItem bracelet = AccessoryCatalog.kenanBracelet();
        org.junit.jupiter.api.Assertions.assertTrue(bracelet.name().equals("PULSERA DE KENAN"), "Debe existir la Pulsera de Kenan.");
        org.junit.jupiter.api.Assertions.assertTrue(bracelet.narrativeDescription().equals("Un regalo de Kenan. Confeccionada con lavanda."),
                "Descripción incorrecta de la Pulsera de Kenan.");
        org.junit.jupiter.api.Assertions.assertTrue(bracelet.properties().stream().anyMatch(property -> property.id() == ItemPropertyId.QUEEN_WIFE_LOVE
                        && property.hidden() && property.activationMinimum() == 23),
                "MI REINA, MI ESPOSA, MI AMOR debe permanecer oculta y requerir FE 23.");
        org.junit.jupiter.api.Assertions.assertTrue(close(equipped(bracelet).healthRegenerationMultiplier(sheet(1)), 1.5),
                "La Pulsera de Kenan debe multiplicar PV REGEN x1,5.");
        org.junit.jupiter.api.Assertions.assertTrue(!equipped(bracelet).effectImmunities(sheet(22)).contains(domain.runic.EffectImmunity.HEALTH_REGEN_PENALTIES),
                "MI REINA, MI ESPOSA, MI AMOR no debe activarse antes de FE 23.");
        org.junit.jupiter.api.Assertions.assertTrue(equipped(bracelet).effectImmunities(sheet(23)).contains(domain.runic.EffectImmunity.HEALTH_REGEN_PENALTIES),
                "MI REINA, MI ESPOSA, MI AMOR debe inmunizar frente a inhibición de PV REGEN con FE 23.");

        AccessoryItem locket = AccessoryCatalog.kiaraLocket();
        org.junit.jupiter.api.Assertions.assertTrue(locket.name().equals("GUARDAPELO DE KIARA"), "El Guardapelo de Kiara debe coexistir con la pulsera.");
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryCatalog.all().stream().anyMatch(item -> item.name().equals("PULSERA DE KENAN")),
                "La Pulsera de Kenan debe formar parte del catálogo completo.");
    }

    private static void verifyLanterns() {
        AccessoryItem lunar = AccessoryCatalog.lunarLantern();
        org.junit.jupiter.api.Assertions.assertTrue(lunar.name().equals("FAROLILLO LUNAR"), "El antiguo farol debe llamarse Farolillo Lunar.");
        org.junit.jupiter.api.Assertions.assertTrue(close(equipped(lunar).sanityBonus(sheet(40), AccessoryContext.day()), 0.0),
                "El Farolillo Lunar no debe aportar Cordura fuera del Intersticio.");
        org.junit.jupiter.api.Assertions.assertTrue(close(equipped(lunar).sanityBonus(sheet(40), AccessoryContext.night()), 0.0),
                "La noche ordinaria no activa el Farolillo Lunar.");
        org.junit.jupiter.api.Assertions.assertTrue(close(equipped(lunar).sanityBonus(sheet(40), AccessoryContext.interstice()), 0.0),
                "El Farolillo Lunar ya no debe aportar Cordura: sólo conserva ÁNCORA ENCARNADA.");
        org.junit.jupiter.api.Assertions.assertTrue(lunar.properties().size() == 1 && lunar.properties().get(0).id() == ItemPropertyId.EMBODIED_ANCHOR,
                "El Farolillo Lunar debe conservar exclusivamente ÁNCORA ENCARNADA.");

        AccessoryItem portable = AccessoryCatalog.portableLantern();
        org.junit.jupiter.api.Assertions.assertTrue(portable.name().equals("FAROLILLO PORTÁTIL"), "Debe existir el nuevo Farolillo portátil.");
        org.junit.jupiter.api.Assertions.assertTrue(portable.narrativeDescription().contains("Pequeño farol de queroseno diseñado para equiparse como abalorio"),
                "Falta la descripción narrativa del Farolillo portátil.");
        org.junit.jupiter.api.Assertions.assertTrue(close(equipped(portable).sanityBonus(sheet(40), AccessoryContext.day()), 0.0),
                "CALOR SOLAR no debe activarse durante el día.");
        org.junit.jupiter.api.Assertions.assertTrue(close(equipped(portable).sanityBonus(sheet(40), AccessoryContext.night()), 3.0),
                "CALOR SOLAR debe aportar +3 Cordura durante la noche.");
        org.junit.jupiter.api.Assertions.assertTrue(portable.effects().stream().anyMatch(effect -> effect.type() == AccessoryEffectType.NIGHT_SANITY_BONUS),
                "El Farolillo portátil debe usar una política nocturna explícita.");
    }

    private static EquipmentState equipped(AccessoryItem item) {
        return new EquipmentState(Map.of(EquipmentSlot.ACCESSORY, item));
    }

    private static CharacterSheet sheet(int faith) {
        return CharacterSheet.of(20, 20, 20, 20, 20, 20, faith, 20, 40);
    }

    private static boolean close(double left, double right) {
        return Math.abs(left - right) < 1e-9;
    }

    
}
