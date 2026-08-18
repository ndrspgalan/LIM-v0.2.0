package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.ability.*;
import domain.ability.progression.*;
import domain.bestiarium.*;
import domain.character.CharacterClass;
import domain.character.sheet.*;
import domain.combat.DamageType;
import domain.combat.coating.*;
import domain.environment.time.EnvironmentalCycle;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.milestone.ClassMasteryMilestonePolicy;

public final class MercuryBestiaryAndProgressionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        provokeStages(); thetaUsesWorldCycle(); milestones(); mercury(); interstice();
    }
    private static void provokeStages() {
        CharacterMasteryCollection c = CharacterMasteryCollection.forClass(CharacterClass.LUCHADOR);
        MasteryProgressionPolicy p = new MasteryProgressionPolicy();
        p.evaluate(c, sheet(25,1,1), new MasteryProgressState(), domain.character.Gender.HOMBRE);
        org.junit.jupiter.api.Assertions.assertTrue(c.isStageUnlocked(MasteryId.INCITAR,"PROVOCAR"), "PROVOCAR FUERZA 25");
        org.junit.jupiter.api.Assertions.assertTrue(!c.isStageUnlocked(MasteryId.INCITAR,"GRITO DE GUERRA"), "Grito no antes de 50/50");
        p.evaluate(c, sheet(50,1,1), new MasteryProgressState(), domain.character.Gender.HOMBRE);
        org.junit.jupiter.api.Assertions.assertTrue(c.isStageUnlocked(MasteryId.INCITAR,"GRITO DE GUERRA"), "GRITO FUERZA 50");
    }
    private static void thetaUsesWorldCycle() {
        MasteryProgressState p = new MasteryProgressState();
        p.advanceAwakeGameMinutes(EnvironmentalCycle.DAY_DURATION.toMinutes()*2, true, true);
        p.registerSleep(false,false);
        org.junit.jupiter.api.Assertions.assertTrue(p.sleptAfterFortyEightAwakeHours(), "Dos días del ciclo canónico revelan Theta");
        MasteryProgressState invalid = new MasteryProgressState();
        invalid.advanceAwakeGameMinutes(100,true,true);
        invalid.advanceAwakeGameMinutes(1,false,true);
        invalid.advanceAwakeGameMinutes(180,true,true);
        invalid.registerSleep(false,false);
        org.junit.jupiter.api.Assertions.assertTrue(invalid.sleptAfterFortyEightAwakeHours(), "Tras reiniciar debe completar de nuevo dos días válidos");
    }
    private static void milestones() {
        CharacterMasteryCollection all = CharacterMasteryCollection.allCanonical();
        String title = new ClassMasteryMilestonePolicy().evaluate(CharacterClass.MAESTRO, all).title();
        org.junit.jupiter.api.Assertions.assertTrue("[MAESTRO]".equals(title), "Maestro sin artículo");
    }
    private static void mercury() {
        MercuryCoatingService service = new MercuryCoatingService();
        var stone = MiscellaneousItemCatalog.mercuryStone();
        var knife = ThrowingWeaponCatalog.throwingKnifeV881();
        org.junit.jupiter.api.Assertions.assertTrue(service.rub(stone, knife, 1), "La piedra impregna arrojadizas");
        var physical = new BestiaryDescriptor("Lobo", ExistencePlane.PHYSICAL_PLANE);
        var interstice = new BestiaryDescriptor("Faerie", ExistencePlane.INTERSTICE);
        org.junit.jupiter.api.Assertions.assertTrue(service.resolveImpact(knife, physical, sheet(1,1,1)).type()==DamageType.POISON, "Veneno físico");
        org.junit.jupiter.api.Assertions.assertTrue(service.resolveImpact(knife, interstice, sheet(1,1,10)).cosmeticOnly(), "Intersticio cosmético bajo C11");
        org.junit.jupiter.api.Assertions.assertTrue(service.resolveImpact(knife, interstice, sheet(1,1,11)).type()==DamageType.CURSE, "Maldición intersticial C11");
    }
    private static void interstice() {
        PlaneDamagePolicy p = new PlaneDamagePolicy();
        var faerie = new BestiaryDescriptor("Faerie", ExistencePlane.INTERSTICE);
        org.junit.jupiter.api.Assertions.assertTrue(!p.canReceive(faerie, DamageType.PIERCING) && !p.canInflict(faerie, DamageType.BLUNT), "Intersticio sin P/C/Ct");
        org.junit.jupiter.api.Assertions.assertTrue(p.canReceive(faerie, DamageType.CURSE), "Intersticio admite Maldición");
    }
    private static CharacterSheet sheet(int fuerza,int carisma,int clarividencia) {
        return CharacterSheet.of(1,1,1,fuerza,1,1,1,carisma,clarividencia);
    }
    
}
