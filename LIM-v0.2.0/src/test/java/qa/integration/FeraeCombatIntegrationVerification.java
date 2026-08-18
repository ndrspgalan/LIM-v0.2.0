package qa.integration;

import domain.bestiarium.physical_plane.ferae.*;
import domain.bestiarium.physical_plane.ferae.intelligence.*;
import domain.character.sheet.CharacterSheet;
import domain.combat.CombatTechnique;
import domain.combat.CombatTechniqueUnlockPolicy;
import domain.combat.ai.declarative.*;
import domain.combat.ai.loadout.*;
import domain.combat.ai.threat.CombatantPresence;
import domain.combat.natural.*;
import domain.inventory.item.*;
import domain.ability.ConvergentTrajectoryPolicy;
import java.util.*;

/** una sola IA; INTELIGENCIA aporta cuerpo/anatomía, no doctrina paralela. */
public final class FeraeCombatIntegrationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        catalogAndMasses(); universalLoadout(); noAnimalDefense(); offensiveReuse(); feintContract(); comboFinisherThreshold();
    }

    private static void catalogAndMasses(){
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceFeraeCombatProfiles.definitionCount()==17,"Deben existir 17 anatomías ofensivas INTELIGENCIA tras formalizar los tres caballos.");
        for(FeraeProfile f:IntelligenceFeraeProfiles.all()){
            NaturalCombatProfile p=IntelligenceFeraeCombatProfiles.of(f);
            org.junit.jupiter.api.Assertions.assertTrue(p.offensiveActions().equals(Set.of(WeaponCombatAction.LIGHT_ATTACK,WeaponCombatAction.HEAVY_ATTACK,WeaponCombatAction.JUMP_ATTACK)),"Sólo LIGHT/HEAVY/JUMP: "+f.species());
            org.junit.jupiter.api.Assertions.assertTrue(!p.canBlock()&&!p.canDestabilize(),"La Ferae no bloquea ni desestabiliza por perfil corporal: "+f.species());
        }
        close(IntelligenceFeraeCombatProfiles.of(IntelligenceFeraeProfiles.of(FeraeSpecies.RATA,FeraeSex.MACHO)).effectiveUnarmedMassKg(),.15,"Rata macho");
        close(IntelligenceFeraeCombatProfiles.of(IntelligenceFeraeProfiles.of(FeraeSpecies.OSO,FeraeSex.MACHO)).effectiveUnarmedMassKg(),14,"Oso macho");
        close(IntelligenceFeraeCombatProfiles.of(IntelligenceFeraeProfiles.of(FeraeSpecies.RINOCERONTE,FeraeSex.HEMBRA)).effectiveUnarmedMassKg(),20,"Rinoceronte hembra");
        close(IntelligenceFeraeCombatProfiles.of(IntelligenceFeraeProfiles.of(FeraeSpecies.AGUILA,FeraeSex.HEMBRA)).effectiveUnarmedMassKg(),1.2,"Águila hembra");
    }

    private static void universalLoadout(){
        FeraeProfile wolf=IntelligenceFeraeProfiles.of(FeraeSpecies.LOBO,FeraeSex.MACHO);
        NaturalCombatProfile body=IntelligenceFeraeCombatProfiles.of(wolf);
        CombatantPresence presence=new CombatantPresence(wolf.attributes(),.80,body);
        ResolvedCombatLoadout r=new CombatLoadoutResolver().resolve(VisibleLoadout.of(null,null),presence);
        org.junit.jupiter.api.Assertions.assertTrue(r.handling().wieldingState()==WieldingState.UNARMED,"El lobo sigue siendo DESARMADO.");
        close(r.attackingWeapon().weightKg(),3.5,"DESARMADO lobo usa masa natural, no 1 kg humano.");
        close(domain.combat.StaggerPolicy.meleeForceEquivalent(wolf.attributes().valueOf(domain.character.sheet.Attribute.FUERZA),r.attackingWeapon().weightKg()),
                wolf.attributes().valueOf(domain.character.sheet.Attribute.FUERZA)+3.5,"Stagger natural = FUERZA + masa efectiva.");
        org.junit.jupiter.api.Assertions.assertTrue(r.attackingWeapon().allowsCombatAction(WeaponCombatAction.LIGHT_ATTACK),"LIGHT común");
        org.junit.jupiter.api.Assertions.assertTrue(r.attackingWeapon().allowsCombatAction(WeaponCombatAction.HEAVY_ATTACK),"HEAVY común");
        org.junit.jupiter.api.Assertions.assertTrue(r.attackingWeapon().allowsCombatAction(WeaponCombatAction.JUMP_ATTACK),"JUMP común");
        org.junit.jupiter.api.Assertions.assertTrue(!r.attackingWeapon().allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK),"No se inventa CHARGED animal.");
        org.junit.jupiter.api.Assertions.assertTrue(body.presentationFor(WeaponCombatAction.LIGHT_ATTACK).equals("Mordisco"),"LIGHT del lobo se representa como mordisco.");
        org.junit.jupiter.api.Assertions.assertTrue(body.presentationFor(WeaponCombatAction.JUMP_ATTACK).equals("Mordisco en salto"),"JUMP del lobo cambia sólo de animación.");
    }

    private static void noAnimalDefense(){
        FeraeProfile bear=IntelligenceFeraeProfiles.of(FeraeSpecies.OSO,FeraeSex.MACHO);
        NaturalCombatProfile body=IntelligenceFeraeCombatProfiles.of(bear);
        org.junit.jupiter.api.Assertions.assertTrue(!body.canBlock()&&!body.canDestabilize(),"El cuerpo Ferae veta BLOCK y DESTABILIZE como capacidades anatómicas.");
    }

    private static void offensiveReuse(){
        FeraeProfile wolf=IntelligenceFeraeProfiles.of(FeraeSpecies.LOBO,FeraeSex.MACHO);
        NaturalCombatProfile body=IntelligenceFeraeCombatProfiles.of(wolf);
        WeaponItem natural=NaturalCombatWeaponFactory.create(body,wolf.attributes(),.80);
        var actor=new CombatActorDecisionState("wolf",domain.character.Gender.HOMBRE,wolf.attributes(),.80,30,30);
        var state=MeleeDecisionState.initial(natural.currentConfiguration().actionMode(),natural.currentConfiguration().gripMode());
        var candidates=new MeleeActionCandidateResolver().resolve(actor,natural,state);
        org.junit.jupiter.api.Assertions.assertTrue(candidates.stream().anyMatch(c->c.action()==WeaponCombatAction.JUMP_ATTACK),
                "LIM debe declarar JUMP_ATTACK natural sin que una IA local lo seleccione.");
    }

    private static void feintContract(){
        CombatTechniqueUnlockPolicy p=new CombatTechniqueUnlockPolicy();
        org.junit.jupiter.api.Assertions.assertTrue(!p.isUnlocked(CombatTechnique.FEINT,IntelligenceFeraeProfiles.of(FeraeSpecies.LINCE,FeraeSex.MACHO).attributes()),"El lince canónico aún no alcanza DES35.");
        CharacterSheet synthetic=CharacterSheet.of(10,10,10,10,35,10,1,1,1);
        org.junit.jupiter.api.Assertions.assertTrue(p.isUnlocked(CombatTechnique.FEINT,synthetic),"Un cuerpo natural con DES35 reutiliza el mismo requisito universal de FINTAR.");
    }

    private static void comboFinisherThreshold(){
        org.junit.jupiter.api.Assertions.assertTrue(!new LightAttackComboProfile(1).hasFinisherBonus(),"Combo de 1 sin bonificación final.");
        org.junit.jupiter.api.Assertions.assertTrue(!new LightAttackComboProfile(2).hasFinisherBonus(),"Combo de 2 sin bonificación final.");
        org.junit.jupiter.api.Assertions.assertTrue(new LightAttackComboProfile(3).hasFinisherBonus()&&new LightAttackComboProfile(5).hasFinisherBonus(),"Combo >=3 sí tiene bonificación final.");
        ConvergentTrajectoryPolicy c=new ConvergentTrajectoryPolicy();
        close(c.onLightAttack(true,1,1,false,false),1.0,"Trayectoria no trata combo 1 como remate");
        close(c.onLightAttack(true,2,2,false,false),1.0,"Trayectoria no trata combo 2 como remate");
        org.junit.jupiter.api.Assertions.assertTrue(!c.unarmedChainOpen(),"Combos <3 no abren Flow.");
        close(c.onLightAttack(true,3,3,false,true),1.4,"Combo 3 conserva remate canónico");
        // DualWieldLightComboPolicy no se modifica: su ordinal global interdependiente sigue siendo autoridad.
    }

    private static void close(double a,double b,String m){org.junit.jupiter.api.Assertions.assertTrue(Math.abs(a-b)<1e-9,m+" ["+a+" != "+b+"]");}
    
}
