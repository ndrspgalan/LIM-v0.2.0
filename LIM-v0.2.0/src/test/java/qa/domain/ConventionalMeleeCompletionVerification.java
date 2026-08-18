package qa.domain;

import domain.combat.*;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.misc.MeleeResinRepairPolicy;
import domain.inventory.item.misc.ResinJarItem;

public final class ConventionalMeleeCompletionVerification {
    
    private static void close(double a,double b,String m){ if(Math.abs(a-b)>1e-9) throw new AssertionError(m+" -> "+a+" != "+b); }
    private static int strength(WeaponItem w){return w.requirements().stream().filter(r->r.attribute()==domain.character.sheet.Attribute.FUERZA).findFirst().orElseThrow().minimumValue();}
    private static int dex(WeaponItem w){return w.requirements().stream().filter(r->r.attribute()==domain.character.sheet.Attribute.DESTREZA).findFirst().orElseThrow().minimumValue();}
    private static boolean prop(WeaponItem w, ItemPropertyId id){return w.properties().stream().anyMatch(p->p.id()==id);}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var hammer=MeleeWeaponCatalog.martilloDeBola();
        close(hammer.weightKg(),.65,"Martillo peso"); close(hammer.reachMeters(),.35,"Martillo longitud");
        org.junit.jupiter.api.Assertions.assertTrue(strength(hammer)==7&&dex(hammer)==4,"Martillo requisitos"); org.junit.jupiter.api.Assertions.assertTrue(prop(hammer,ItemPropertyId.SUFFICIENT_ERGONOMICS),"Martillo ergonomía");
        org.junit.jupiter.api.Assertions.assertTrue(prop(hammer,ItemPropertyId.COPILOT)&&prop(hammer,ItemPropertyId.EQUESTRIAN)&&prop(hammer,ItemPropertyId.BICYCLAR)&&prop(hammer,ItemPropertyId.MOTORCYCLAR),"Martillo transporte");
        org.junit.jupiter.api.Assertions.assertTrue(hammer.modes().getFirst().lethality().blunt()==30,"Martillo 0/0/30");

        var sickle=MeleeWeaponCatalog.hoz(); org.junit.jupiter.api.Assertions.assertTrue(strength(sickle)==3&&dex(sickle)==4,"Hoz requisitos"); org.junit.jupiter.api.Assertions.assertTrue(prop(sickle,ItemPropertyId.HOOK),"Hoz engancha");
        org.junit.jupiter.api.Assertions.assertTrue(sickle.allowsCombatAction(WeaponCombatAction.HEAVY_ATTACK),"Hoz HEAVY");
        var scythe=MeleeWeaponCatalog.guadana(); org.junit.jupiter.api.Assertions.assertTrue(strength(scythe)==10&&dex(scythe)==16,"Guadaña requisitos"); org.junit.jupiter.api.Assertions.assertTrue(prop(scythe,ItemPropertyId.COPILOT),"Guadaña copiloto");
        var fork=MeleeWeaponCatalog.horca(); org.junit.jupiter.api.Assertions.assertTrue(strength(fork)==14&&dex(fork)==16,"Horca requisitos"); org.junit.jupiter.api.Assertions.assertTrue(fork.hasTrait(WeaponTrait.RESIN_REPAIR),"Horca resina");
        org.junit.jupiter.api.Assertions.assertTrue(fork.isExclusivelyTwoHanded()
                && fork.allowsCombatAction(WeaponCombatAction.LIGHT_ATTACK)
                && fork.allowsCombatAction(WeaponCombatAction.JUMP_ATTACK)
                && fork.allowsCombatAction(WeaponCombatAction.DESTABILIZE)
                && !fork.allowsCombatAction(WeaponCombatAction.HEAVY_ATTACK)
                && !fork.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK),
                ": Horca = LIGHT×2 + JUMP + patada frontal DESTABILIZE.");
        var bo=MeleeWeaponCatalog.bo(); org.junit.jupiter.api.Assertions.assertTrue(strength(bo)==8&&dex(bo)==18,"Bō requisitos"); org.junit.jupiter.api.Assertions.assertTrue(bo.hasTrait(WeaponTrait.NON_DEGRADING)&&prop(bo,ItemPropertyId.COPILOT),"Bō no desgaste + copiloto");
        var hook=MeleeWeaponCatalog.boathook(); org.junit.jupiter.api.Assertions.assertTrue(strength(hook)==7&&dex(hook)==18,"Boathook requisitos"); org.junit.jupiter.api.Assertions.assertTrue(prop(hook,ItemPropertyId.HOOK)&&prop(hook,ItemPropertyId.DISMOUNT)&&prop(hook,ItemPropertyId.COPILOT),"Boathook propiedades");

        var hr=new HookingPolicy().resolve(scythe,WeaponCombatAction.HEAVY_ATTACK,10,0,0); org.junit.jupiter.api.Assertions.assertTrue(hr.triggered(),"Guadaña engancha por perforante real"); close(hr.pullDistanceMeters(),1.60*2/3,"Tiro guadaña");
        org.junit.jupiter.api.Assertions.assertTrue(!new HookingPolicy().resolve(hook,WeaponCombatAction.HEAVY_ATTACK,20,0,0).triggered(),"Boathook no engancha por perforante");
        org.junit.jupiter.api.Assertions.assertTrue(new HookingPolicy().resolve(hook,WeaponCombatAction.HEAVY_ATTACK,0,10,0).triggered(),"Boathook engancha por contundente real");
        org.junit.jupiter.api.Assertions.assertTrue(new DismountPolicy().resolve(hook,WeaponCombatAction.LIGHT_ATTACK,true,10,0).dismounted(),"Boathook desmonta");

        var parryWeapon=MeleeWeaponCatalog.daga();
        var p20=new ParryResolutionPolicy().resolve(parryWeapon,true,20,20); var p70=new ParryResolutionPolicy().resolve(parryWeapon,true,70,20);
        close(p20.stunDurationSeconds(),2.0,"Desviar DEX20"); close(p70.stunDurationSeconds(),2.7,"Desviar DEX70"); org.junit.jupiter.api.Assertions.assertTrue(p20.recoilDistanceMeters()>0,"Parry usa StaggerPolicy para retroceso");

        var charged=new ChargedAttackPreparationState(); charged.start(); charged.advance(1.19); org.junit.jupiter.api.Assertions.assertTrue(!new ChargedAttackPreparationPolicy().ready(MeleeWeaponCatalog.guadana(),charged),": Guadaña no ejecuta antes de 1,20 s"); charged.advance(.01); org.junit.jupiter.api.Assertions.assertTrue(new ChargedAttackPreparationPolicy().ready(MeleeWeaponCatalog.guadana(),charged),": Guadaña queda lista a 1,20 s");
        var c1=new ChargedAttackImpactPolicy().resolve(scythe,scythe.modes().getFirst()); close(c1.blunt(),30*1.30,"Charged normal x1,30");
        var rotor=MeleeWeaponCatalog.espadonDeRotor(); var cr=new ChargedAttackImpactPolicy().resolve(rotor,rotor.modes().getFirst()); close(cr.blunt(),(100+3.8)*1.30,": Rotor charged usa política CHARGED ordinaria sobre masa Rotor");
        close(new CombatStaminaCostPolicy().cost(rotor,WeaponCombatAction.CHARGED_ATTACK),rotor.weightKg()*1.30,"PA charged x1,30");

        var worn=fork.modes().getFirst(); org.junit.jupiter.api.Assertions.assertTrue(fork.applyHeavyArmorWear(worn,new domain.inventory.item.armor.ArmorProtectionProfile(75,75,75)).any(),"Horca se desgasta contra HEAVY"); var resin=new ResinJarItem(1); org.junit.jupiter.api.Assertions.assertTrue(new MeleeResinRepairPolicy().repair(resin,fork),"Horca se repara con resina");
        org.junit.jupiter.api.Assertions.assertTrue(!bo.applyHeavyArmorWear(bo.modes().getFirst(),new domain.inventory.item.armor.ArmorProtectionProfile(75,75,75)).any(),"Bō NON_DEGRADING no se desgasta"); org.junit.jupiter.api.Assertions.assertTrue(!hook.applyHeavyArmorWear(hook.modes().getFirst(),new domain.inventory.item.armor.ArmorProtectionProfile(75,75,75)).any(),"Boathook NON_DEGRADING no se desgasta");

        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.piqueta().currentConfiguration().actionMode()==WeaponActionMode.PRIMARY,"Piqueta principal");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.cuchilloDeCarnicero().currentConfiguration().actionMode()==WeaponActionMode.PRIMARY,"Carnicero principal");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponCatalog.hachaDeLenador().currentConfiguration().actionMode()==WeaponActionMode.PRIMARY,"Hacha principal");
        var dagger=MeleeWeaponCatalog.daga(); dagger.selectActionMode(WeaponActionMode.ALTERNATIVE); org.junit.jupiter.api.Assertions.assertTrue(dagger.currentConfiguration().actionMode()==WeaponActionMode.ALTERNATIVE,"Daga conserva modo alternativo");
    }
}
