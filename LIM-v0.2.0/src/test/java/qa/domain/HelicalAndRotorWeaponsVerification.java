package qa.domain;

import domain.character.sheet.Attribute;
import domain.combat.*;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.*;

import java.util.Set;

public final class HelicalAndRotorWeaponsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        WeaponItem helical = MeleeWeaponCatalog.espadaHelicoidal();
        org.junit.jupiter.api.Assertions.assertTrue(helical.name().equals("Espada Helicoidal"), "Debe existir la Espada Helicoidal canónica.");
        close(helical.weightKg(), 1.16, "Peso helicoidal");
        close(helical.reachMeters(), 1.10, "Alcance helicoidal");
        org.junit.jupiter.api.Assertions.assertTrue(helical.footprint().verticalSlots() == 2 && helical.footprint().horizontalSlots() == 11,
                "La Espada Helicoidal debe ocupar 11 x 1.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(helical, Attribute.FUERZA) == 9, "La Espada Helicoidal requiere FUERZA 9.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(helical, Attribute.DESTREZA) == 11, "La Espada Helicoidal requiere DESTREZA 11.");
        org.junit.jupiter.api.Assertions.assertTrue(helical.isExclusivelyTwoHanded() && helical.availableConfigurations().size() == 1,
                "La Espada Helicoidal debe ser exclusivamente bimanual y principal.");
        org.junit.jupiter.api.Assertions.assertTrue(helical.hasTrait(WeaponTrait.HELICOIDAL_CONTROL), "Debe declarar HELICOIDAL_CONTROL.");
        org.junit.jupiter.api.Assertions.assertTrue(!helical.combatActionsFor(WeaponActionMode.PRIMARY).contains(WeaponCombatAction.PARRY),
                "Mirror Parry no debe convertirse en PARRY manual.");
        org.junit.jupiter.api.Assertions.assertTrue(helical.combatActionsFor(WeaponActionMode.PRIMARY).equals(fullRepertoire()),
                "La Helicoidal debe admitir ligero, fuerte, cargado, salto y desestabilizador.");
        LethalityProfile h = helical.modes().getFirst().lethality();
        close(h.piercing(), 65, "Helicoidal perforante");
        close(h.slashing(), 65, "Helicoidal cortante");
        close(h.blunt(), 20, "Helicoidal contundente");
        org.junit.jupiter.api.Assertions.assertTrue(helical.statistics().stream().anyMatch(v -> v.contains("12°")),
                "La ficha debe distinguir la torsión helicoidal de una flamígera.");

        WeaponItem ordinary = MeleeWeaponCatalog.cimitarra();
        MirrorParryPolicy mirror = new MirrorParryPolicy();
        ParryResolution mirrorResult = mirror.resolveMirrorParry(
                helical, GripMode.TWO_HANDED, WeaponCombatAction.LIGHT_ATTACK,
                ordinary, GripMode.ONE_HANDED, WeaponCombatAction.JUMP_ATTACK, true);
        org.junit.jupiter.api.Assertions.assertTrue(mirrorResult.successful() && mirrorResult.attackInterrupted(),
                "Mirror Parry debe resolverse automáticamente al colisionar hitboxes elegibles.");
        close(mirrorResult.stunDurationSeconds(), ParryResolution.PARRY_STUN_SECONDS,
                "Mirror Parry debe aplicar 2 s de aturdimiento");

        WeaponItem rotor = MeleeWeaponCatalog.espadonDeRotor();
        close(rotor.weightKg(), 3.80, "Peso rotor");
        close(rotor.reachMeters(), 1.30, "Alcance rotor");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(rotor, Attribute.FUERZA) == 38, "El Rotor 2H PRIMARY requiere FUERZA 38.");
        org.junit.jupiter.api.Assertions.assertTrue(requirement(rotor, Attribute.DESTREZA) == 13, "El Rotor requiere DESTREZA 13.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.hasTrait(WeaponTrait.DE_ROTOR), "El Rotor debe declarar DE_ROTOR.");
        org.junit.jupiter.api.Assertions.assertTrue(!rotor.canCurrentAttackBeParried(), "DE_ROTOR debe impedir PARRY y Mirror Parry.");
        org.junit.jupiter.api.Assertions.assertTrue(!rotor.isExclusivelyTwoHanded() && rotor.availableConfigurations().size() == 2,
                ": el Rotor dispone de 2H PRIMARY y 1H ALTERNATIVE.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.combatActionsFor(WeaponActionMode.PRIMARY).equals(Set.of(
                        WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.HEAVY_ATTACK,
                        WeaponCombatAction.CHARGED_ATTACK, WeaponCombatAction.JUMP_ATTACK)),
                ": Rotor no tiene DESTABILIZE propio; esa entrada redirige a CHARGED.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.modes().size() == 1 && rotor.modes().getFirst().lethality().equals(new LethalityProfile(65,65,100)),
                "El Rotor debe usar un único perfil 65 / 65 / 100.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.isSheathed(), "El Rotor se entrega retraído para transporte dorsal.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.footprint().verticalSlots() == 2 && rotor.footprint().horizontalSlots() == 9,
                "Retraído debe ocupar 9 x 2.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.deployedFootprint().verticalSlots() == 2 && rotor.deployedFootprint().horizontalSlots() == 13,
                "Desplegado debe ocupar 13 x 2.");

        RotorRetractionPolicy retraction = new RotorRetractionPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(!retraction.canAttack(rotor, retraction.stateOf(rotor)), "Retraído no puede atacar.");
        org.junit.jupiter.api.Assertions.assertTrue(retraction.beginDeployment(rotor, false) == RotorRetractionState.DEPLOYING,
                "Debe iniciar despliegue fuera de una acción ofensiva.");
        org.junit.jupiter.api.Assertions.assertTrue(retraction.completeDeployment(rotor) == RotorRetractionState.DEPLOYED,
                "Debe completar despliegue.");
        org.junit.jupiter.api.Assertions.assertTrue(retraction.canAttack(rotor, RotorRetractionState.DEPLOYED), "Desplegado debe poder atacar.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.footprint().horizontalSlots() == 13, "Desplegado debe recuperar la huella completa.");
        org.junit.jupiter.api.Assertions.assertTrue(!new ParryTargetEligibilityPolicy().isEligible(rotor), "El Rotor no es objetivo elegible de PARRY.");

        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.allCanonical().size() == 18,
                "El catálogo completo debe contener siete ordinarias, cinco especializadas y seis herramientas convencionales.");
    }

    private static int requirement(WeaponItem item, Attribute attribute) {
        return item.requirements().stream().filter(r -> r.attribute() == attribute)
                .findFirst().orElseThrow().minimumValue();
    }

    private static Set<WeaponCombatAction> fullRepertoire() {
        return Set.of(WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.HEAVY_ATTACK,
                WeaponCombatAction.CHARGED_ATTACK, WeaponCombatAction.JUMP_ATTACK,
                WeaponCombatAction.DESTABILIZE);
    }

    private static void close(double actual, double expected, String label) {
        if (Math.abs(actual - expected) > 1e-9) throw new AssertionError(label + ": " + actual + " != " + expected);
    }

    
}
