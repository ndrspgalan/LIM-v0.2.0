package qa.domain;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.meleeWeapons.ConventionalMeleeWeaponBasicCatalog;
import domain.inventory.item.meleeWeapons.MeleeWeaponBasicDefinition;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** narrativas de Katana/Maza y fichas básicas de seis herramientas convencionales. */
public final class ConventionalMeleeNarrativesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifySpecialNarratives();
        verifySixBasicDefinitions();
    }

    private static void verifySpecialNarratives() {
        WeaponItem katana = MeleeWeaponCatalog.katanaTermoMecanicaV881();
        String k = katana.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(k.contains("amadou") && k.contains("resina"), "La Katana debe explicar el material térmico.");
        org.junit.jupiter.api.Assertions.assertTrue(k.contains("vaina") && k.contains("desenfund"), "La Katana debe explicar la ignición por desenvaine.");
        org.junit.jupiter.api.Assertions.assertTrue(k.contains("cinco minutos"), "La Katana debe narrar la autonomía térmica de cinco minutos.");

        WeaponItem mace = MeleeWeaponCatalog.mazaElectroMecanicaV881();
        String m = mace.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(m.contains("celda galvánica") && m.contains("acumulador"), "La Maza debe explicar su fuente y acumulación eléctricas.");
        org.junit.jupiter.api.Assertions.assertTrue(m.contains("doce segundos") && m.contains("chispas"), "La Maza debe narrar recarga y señal visual.");
        org.junit.jupiter.api.Assertions.assertTrue(m.contains("ataque fuerte") || m.contains("golpe fuerte"), "La Maza debe ligar la descarga al ataque fuerte.");
    }

    private static void verifySixBasicDefinitions() {
        List<MeleeWeaponBasicDefinition> all = ConventionalMeleeWeaponBasicCatalog.all();
        org.junit.jupiter.api.Assertions.assertTrue(all.size() == 6, " debe definir exactamente seis fichas básicas nuevas.");
        Set<String> names = all.stream().map(MeleeWeaponBasicDefinition::name).collect(Collectors.toSet());
        org.junit.jupiter.api.Assertions.assertTrue(names.equals(Set.of("Martillo de bola", "Hoz", "Guadaña", "Horca", "Bō", "Boathook")),
                "Los seis nombres canónicos básicos no coinciden.");
        for (MeleeWeaponBasicDefinition definition : all) {
            org.junit.jupiter.api.Assertions.assertTrue(!definition.functionalCategory().isBlank(), definition.name() + " debe tener categoría funcional.");
            org.junit.jupiter.api.Assertions.assertTrue(definition.narrativeDescription().length() > 180, definition.name() + " debe tener una narrativa básica suficiente.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(ConventionalMeleeWeaponBasicCatalog.guadana().narrativeDescription().contains("segar"), "La Guadaña debe describirse como herramienta de siega.");
        org.junit.jupiter.api.Assertions.assertTrue(ConventionalMeleeWeaponBasicCatalog.boathook().narrativeDescription().contains("gancho"), "El Boathook debe explicar su gancho funcional.");
        org.junit.jupiter.api.Assertions.assertTrue(ConventionalMeleeWeaponBasicCatalog.hoz().narrativeDescription().contains("pequeños escudos"), "La Hoz debe conservar su utilidad alrededor de pequeños escudos.");
    }

    
}
