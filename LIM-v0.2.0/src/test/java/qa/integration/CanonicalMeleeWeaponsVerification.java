package qa.integration;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;

import java.util.List;
import java.util.Set;

public final class CanonicalMeleeWeaponsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        List<WeaponItem> weapons = MeleeWeaponCatalog.all();
        org.junit.jupiter.api.Assertions.assertTrue(weapons.size() == 7, "Deben existir exactamente siete armas cuerpo a cuerpo ordinarias.");
        org.junit.jupiter.api.Assertions.assertTrue(weapons.stream().noneMatch(w -> w.name().toLowerCase().contains("estilete")),
                "El Estilete no debe existir en el catálogo.");

        assertWeapon(MeleeWeaponCatalog.pico(), 2.40, 0.80, 4, 8, 24, 8, 90, 65, 65,
                GripMode.TWO_HANDED, WeaponActionMode.PRIMARY, rotorTwoHanded());
        assertWeapon(MeleeWeaponCatalog.zapapico(), 2.40, 0.80, 3, 8, 24, 8, 95, 65, 60,
                GripMode.TWO_HANDED, WeaponActionMode.PRIMARY, rotorTwoHanded());
        assertWeapon(MeleeWeaponCatalog.piqueta(), 0.80, 0.40, 3, 4, 8, 4, 0, 65, 30,
                GripMode.ONE_HANDED, WeaponActionMode.PRIMARY, ordinaryOneHanded());
        assertWeapon(MeleeWeaponCatalog.cuchilloDeCarnicero(), 0.70, 0.40, 2, 4, 7, 4, 0, 65, 15,
                GripMode.ONE_HANDED, WeaponActionMode.PRIMARY, ordinaryOneHanded());
        assertWeapon(MeleeWeaponCatalog.hachaDeLenador(), 1.40, 0.60, 3, 6, 14, 6, 0, 65, 15,
                GripMode.ONE_HANDED, WeaponActionMode.PRIMARY, ordinaryOneHanded());
        assertWeapon(MeleeWeaponCatalog.cimitarra(), 0.95, 1.00, 1, 10, 10, 10, 0, 65, 15,
                GripMode.ONE_HANDED, WeaponActionMode.PRIMARY, ordinaryOneHanded());

        WeaponItem dagger = MeleeWeaponCatalog.daga();
        org.junit.jupiter.api.Assertions.assertTrue(dagger.availableConfigurations().size() == 2, "La Daga debe admitir principal y alternativo.");
        org.junit.jupiter.api.Assertions.assertTrue(dagger.modes().stream().map(WeaponMode::name).toList().equals(List.of("Oscilatorio", "Invertido")),
                "Los modos canónicos de la Daga deben ser Oscilatorio e Invertido.");
        for (WeaponActionMode mode : WeaponActionMode.values()) {
            org.junit.jupiter.api.Assertions.assertTrue(dagger.combatActionsFor(mode).equals(ordinaryOneHanded()),
                    "Ambos modos de la Daga deben admitir ligero, salto y desestabilizador.");
        }

        WeaponItem axe = MeleeWeaponCatalog.hachaDeLenador();
        WeaponItem scimitar = MeleeWeaponCatalog.cimitarra();
        org.junit.jupiter.api.Assertions.assertTrue(axe.hasTrait(WeaponTrait.ERGONOMIA_SUFICIENTE), "El Hacha debe tener ERGONOMÍA SUFICIENTE.");
        org.junit.jupiter.api.Assertions.assertTrue(scimitar.hasTrait(WeaponTrait.ERGONOMIA_SUFICIENTE), "La Cimitarra debe tener ERGONOMÍA SUFICIENTE.");
        org.junit.jupiter.api.Assertions.assertTrue(axe.gripEligibilityForStrength(0) == WeaponGripEligibility.ONE_OR_TWO_HANDED,
                "ERGONOMÍA SUFICIENTE debe permitir blandir a una mano sin FUERZA ideal.");
        for (ItemPropertyId id : List.of(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN,
                ItemPropertyId.BICYCLAR, ItemPropertyId.MOTORCYCLAR)) {
            org.junit.jupiter.api.Assertions.assertTrue(scimitar.properties().stream().anyMatch(p -> p.id() == id),
                    "La Cimitarra debe declarar " + id + ".");
        }

        CharacterSheet kenan = CharacterSheet.of(27,40,12,30,20,30,3,25,11);
        WeaponItem unarmed = UnarmedWeaponFactory.create(kenan, 1.72);
        close(unarmed.reachMeters(), 0.86, "El alcance DESARMADO debe ser ALTURA ×0,5.");
        LethalityProfile unarmedLethality = unarmed.modes().getFirst().lethality();
        close(unarmedLethality.piercing(), 0, "DESARMADO no perfora.");
        close(unarmedLethality.slashing(), 0, "DESARMADO no corta.");
        close(unarmedLethality.blunt(), 31, ": DESARMADO masculino usa FUERZA + 1 kg equivalente como contundencia.");

        WeaponInputResolution jump = new WeaponInputResolutionPolicy().resolve(
                WeaponInput.JUMP_PRESS, MeleeWeaponCatalog.cimitarra(), null, false, false);
        org.junit.jupiter.api.Assertions.assertTrue(jump.allowed() && jump.action().orElseThrow() == WeaponCombatAction.JUMP_ATTACK,
                "El ataque con salto debe resolverse como acción propia.");
    }

    private static void assertWeapon(WeaponItem item, double weight, double reach, int vertical, int horizontal,
                                     int strength, int dexterity, double piercing, double slashing, double blunt,
                                     GripMode grip, WeaponActionMode mode, Set<WeaponCombatAction> actions) {
        close(item.weightKg(), weight, item.name()+" peso");
        close(item.reachMeters(), reach, item.name()+" alcance");
        org.junit.jupiter.api.Assertions.assertTrue(item.footprint().verticalSlots()==vertical && item.footprint().horizontalSlots()==horizontal,
                item.name()+" slots incorrectos.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(item, Attribute.FUERZA)==strength, item.name()+" FUERZA incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(item, Attribute.DESTREZA)==dexterity, item.name()+" DESTREZA incorrecta.");
        LethalityProfile lethality=item.modes().getFirst().lethality();
        close(lethality.piercing(),piercing,item.name()+" perforante");
        close(lethality.slashing(),slashing,item.name()+" cortante");
        close(lethality.blunt(),blunt,item.name()+" contundente");
        WeaponConfiguration configuration=item.availableConfigurations().getFirst();
        org.junit.jupiter.api.Assertions.assertTrue(configuration.gripMode()==grip && configuration.actionMode()==mode,item.name()+" configuración incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(item.combatActionsFor(mode).equals(actions),item.name()+" repertorio incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(item.statistics().stream().anyMatch(s->s.equals("ARROJADIZA | No")),item.name()+" no debe ser arrojadiza.");
    }

    private static int requirement(WeaponItem item, Attribute attribute) {
        return item.requirements().stream().filter(r->r.attribute()==attribute).findFirst().orElseThrow().minimumValue();
    }
    private static Set<WeaponCombatAction> rotorTwoHanded(){return Set.of(WeaponCombatAction.LIGHT_ATTACK,WeaponCombatAction.HEAVY_ATTACK,WeaponCombatAction.JUMP_ATTACK,WeaponCombatAction.DESTABILIZE);}
    private static Set<WeaponCombatAction> ordinaryOneHanded(){return Set.of(WeaponCombatAction.LIGHT_ATTACK,WeaponCombatAction.JUMP_ATTACK,WeaponCombatAction.DESTABILIZE);}
    private static void close(double actual,double expected,String label){if(Math.abs(actual-expected)>1e-9)throw new AssertionError(label+": "+actual+" != "+expected);}
    
}
