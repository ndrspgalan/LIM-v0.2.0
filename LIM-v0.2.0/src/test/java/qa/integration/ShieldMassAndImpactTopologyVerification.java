package qa.integration;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.combat.*;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.UnarmedWeaponFactory;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.firearms.*;
import domain.inventory.item.meleeWeapons.ShieldCatalog;
import domain.inventory.item.armor.ArmorHitLocation;
import java.util.Map;

public final class ShieldMassAndImpactTopologyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        var shield=ShieldCatalog.pavesinaCementadaDeAsaltoV881();
        org.junit.jupiter.api.Assertions.assertTrue(shield.name().equals("Pavesina Cementada de Asalto V881"),"nombre pavesina");
        close(shield.weightKg(),8.8,"peso pavesina");
        org.junit.jupiter.api.Assertions.assertTrue(shield.footprint().verticalSlots()==5&&shield.footprint().horizontalSlots()==6,": slots XYZ pavesina");
        org.junit.jupiter.api.Assertions.assertTrue(shield.hasTrait(WeaponTrait.SHIELD)&&shield.hasTrait(WeaponTrait.ERGONOMIA_INTRINCADA),"traits pavesina");
        org.junit.jupiter.api.Assertions.assertTrue(shield.properties().stream().anyMatch(p->p.id()==ItemPropertyId.ELECTRICAL_CONDUCTOR),"conductor eléctrico");
        org.junit.jupiter.api.Assertions.assertTrue(shield.properties().stream().anyMatch(p->p.id()==ItemPropertyId.INTRICATE_ERGONOMICS),"ergonomía intrincada");
        var spec=ShieldCombatPolicy.PAVESINA_V881;
        org.junit.jupiter.api.Assertions.assertTrue(spec.protection().piercing()==100&&spec.protection().slashing()==100&&spec.protection().blunt()==100,"protección 100/100/100");
        close(spec.raisedCoverageRatio(),0.15,"cobertura 15 pp");
        close(spec.wearMultiplier(),0.5,"desgaste x0.5");
        org.junit.jupiter.api.Assertions.assertTrue(spec.repairableWithSteel(),"reparable con acero");
        org.junit.jupiter.api.Assertions.assertTrue(!shield.supportsTwoHandedUse()&&shield.supportsOneHandedUse(),"exclusivamente 1H");
        org.junit.jupiter.api.Assertions.assertTrue(!shield.combatActionsFor(domain.inventory.item.WeaponActionMode.ALTERNATIVE).contains(domain.inventory.item.WeaponCombatAction.PARRY),"sin parry");
        org.junit.jupiter.api.Assertions.assertTrue(shield.combatActionsFor(domain.inventory.item.WeaponActionMode.PRIMARY).contains(domain.inventory.item.WeaponCombatAction.DESTABILIZE),"golpe desestabilizador");
        org.junit.jupiter.api.Assertions.assertTrue(shield.shieldGuardPosition()==ShieldGuardPosition.HEAD,"guardia inicial HEAD");
        org.junit.jupiter.api.Assertions.assertTrue(shield.toggleShieldGuardPosition()==ShieldGuardPosition.BODY,"wheel a BODY");

        var strong=CharacterSheet.of(20,45,12,45,20,20,3,20,11);
        org.junit.jupiter.api.Assertions.assertTrue(ShieldCombatPolicy.canWieldIntricateOneHanded(strong,shield),"FUERZA+AGUANTE 90 empuña");
        var weak=CharacterSheet.of(20,40,12,40,20,20,3,20,11);
        org.junit.jupiter.api.Assertions.assertTrue(!ShieldCombatPolicy.canWieldIntricateOneHanded(weak,shield),"FUERZA+AGUANTE 80 no empuña");
        close(ShieldCombatPolicy.lightAttackLethality(strong,shield).blunt(),53.8,"arrollamiento fuerza+kg");

        var right=ShieldCatalog.pavesinaCementadaDeAsaltoV881();
        var left=ShieldCatalog.pavesinaCementadaDeAsaltoV881();
        var eq=new EquipmentState(Map.of(EquipmentSlot.RIGHT_HAND,right,EquipmentSlot.LEFT_HAND,left));
        var handling=domain.inventory.item.WeaponHandlingResolver.resolve(eq,true,strong);
        org.junit.jupiter.api.Assertions.assertTrue(handling.wieldingState()==domain.inventory.item.WieldingState.DUAL_WIELD,"dual pavesina+pavesina válido");

        var male=UnarmedWeaponFactory.create(CharacterSheet.of(20,20,12,30,20,20,3,20,11),1.72,Gender.HOMBRE);
        var female=UnarmedWeaponFactory.create(CharacterSheet.of(20,20,12,30,20,20,3,20,11),1.72,Gender.MUJER);
        close(male.weightKg(),1.0,"masa desarmado hombre"); close(female.weightKg(),0.5,"masa desarmado mujer");
        close(male.modes().getFirst().lethality().blunt(),31.0,"letalidad desarmado hombre");
        close(female.modes().getFirst().lethality().blunt(),30.5,"letalidad desarmado mujer");
        close(new CombatStaminaCostPolicy().cost(male,domain.inventory.item.WeaponCombatAction.LIGHT_ATTACK),1.0,"PA desarmado hombre");
        close(new CombatStaminaCostPolicy().cost(female,domain.inventory.item.WeaponCombatAction.LIGHT_ATTACK),0.5,"PA desarmado mujer");

        var split=AreaBodyDistributionPolicy.split(100);
        close(split.head(),9,"HEAD 9%"); close(split.body(),91,"BODY 91%");

        var anti=new AntiMaterielCannonFirearmItem("Prueba",new FirearmCartridge("20mm","20 mm",1));
        org.junit.jupiter.api.Assertions.assertTrue(anti.fulminatingPropertyPresent(),"antimaterial FULMINANTE");
        close(anti.radialLethalityAt(0).piercing(),100,"anti centro P100");
        close(anti.radialLethalityAt(0).blunt(),100,"anti centro B100");
        close(anti.radialLethalityAt(0.5).piercing(),1,"anti borde P1");
        close(anti.radialLethalityAt(0.5).blunt(),1,"anti borde B1");
        org.junit.jupiter.api.Assertions.assertTrue(anti.radialStaggerAt(0.5)!=null,"anti radial aplica staggering");

        var cluster=new ClusterCannonImpactProfile(25,100,100,true);
        close(cluster.atDistance(0).slashing(),100,"racimo centro C100");
        close(cluster.atDistance(25).slashing(),1,"racimo borde C1");
        close(cluster.atDistance(25).burn(),1,"racimo borde Q1");
        org.junit.jupiter.api.Assertions.assertTrue(!cluster.appliesStaggering(),"racimo sin staggering");

        var improvised=RaisedShieldCoveragePolicy.improvisedHead(new domain.inventory.item.armor.ArmorProtectionProfile(75,100,75),1.0);
        org.junit.jupiter.api.Assertions.assertTrue(improvised.location()==ArmorHitLocation.HEAD,"escudo improvisado solo HEAD");
        close(improvised.coverageRatio(),0.025,"escudo improvisado +2.5pp");
    }
     
    private static void close(double a,double b,String m){if(Math.abs(a-b)>1e-6)throw new AssertionError(m+": "+a+" != "+b);} 
}
