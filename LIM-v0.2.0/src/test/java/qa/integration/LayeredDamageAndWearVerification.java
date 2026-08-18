package qa.integration;

import domain.combat.*;
import domain.inventory.InventoryFootprint;
import domain.inventory.equipment.*;
import domain.inventory.item.*;
import domain.inventory.item.armor.*;

import java.util.*;

/** Regresión canónica : hitboxes por pieza, capas, P/C/B mutables y desgaste melee vs HEAVY. */
public final class LayeredDamageAndWearVerification {
    private static final double EPS=1e-9;
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        feetNeverArmorsMoreThanFivePercent();
        equalityDoesNotWearButStrictlyGreaterDoes();
        outerAndInnerLayersBothParticipate();
        zeroProfilePassesThroughWithoutWeaponWearInThatChannel();
        headKeepsBluntFragility();
        firearmHandlingStillUsesEffectiveWeight();
    }

    private static void feetNeverArmorsMoreThanFivePercent() {
        ArmorPiece boots=body("Botas",ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,.05),
                new ArmorProtectionProfile(100,100,100),ArmorMaterial.HARDENED_LEATHER).withFeetLayer(FeetLayer.OUTER);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.FEET,ArmorLayerPosition.OUTER,boots);
        var r=new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.FEET,layout,999);
        close(r.netDamage().piercing(),95,"FEET sólo interpone 5% global");
    }

    private static void equalityDoesNotWearButStrictlyGreaterDoes() {
        ArmorPiece steel=body("Acero",ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,.50),
                new ArmorProtectionProfile(65,40,20),ArmorMaterial.STEEL);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,steel);
        new ArmorDamageResolver().resolve(new PhysicalDamage(65,0,0),ArmorCombatHitbox.CHEST,layout,999);
        close(steel.currentPiercingProtection(),65,"Igualdad no desgasta");
        new ArmorDamageResolver().resolve(new PhysicalDamage(66,0,0),ArmorCombatHitbox.CHEST,layout,999);
        close(steel.currentPiercingProtection(),64,"Superioridad estricta desgasta x1");
    }

    private static void outerAndInnerLayersBothParticipate() {
        ArmorPiece coat=body("Gabardina",ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.LEGGINGS,.10),
                new ArmorProtectionProfile(2,5,2),ArmorMaterial.CLOTH);
        ArmorPiece greaves=body("Polainas",ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,.12),
                new ArmorProtectionProfile(75,75,75),ArmorMaterial.STEEL);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,coat)
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.OUTER,greaves);
        new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.LEGGINGS,layout,999);
        close(coat.currentPiercingProtection(),1,": toda capa alcanzada puede desgastarse según su umbral");
        close(greaves.currentPiercingProtection(),74,": el residual continúa hacia la siguiente capa");
    }

    private static void zeroProfilePassesThroughWithoutWeaponWearInThatChannel() {
        ArmorPiece steel=body("Placa agotada en P",ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,.50),
                new ArmorProtectionProfile(1,50,50),ArmorMaterial.STEEL);
        // Llevar P a cero mediante un impacto superior.
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,steel);
        new ArmorDamageResolver().resolve(new PhysicalDamage(100,0,0),ArmorCombatHitbox.CHEST,layout,999);
        close(steel.currentPiercingProtection(),0,"P agotado");

        WeaponItem sword=weapon(); WeaponMode mode=sword.modes().getFirst();
        double p=sword.currentLethality(mode).piercing();
        double c=sword.currentLethality(mode).slashing();
        new ArmorDamageResolver().resolveMelee(new PhysicalDamage(p,c,0),ArmorCombatHitbox.CHEST,layout,999,sword,mode);
        close(sword.currentLethality(mode).piercing(),p,"P del arma no se desgasta contra P0");
        close(sword.currentLethality(mode).slashing(),c-1,"C del arma sí se desgasta contra HEAVY con C>0");
    }

    private static void headKeepsBluntFragility() {
        ArmorPiece helmet=new ArmorPiece("Casco prueba","Casco de verificación .",1,new InventoryFootprint(1,1),ArmorHitLocation.HEAD,1,
                new ArmorProtectionProfile(0,0,0),ArmorMaterial.CLOTH,ArmorForm.STANDARD,List.of(),List.of()).withHeadLayer(HeadLayer.TACTICAL);
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.HEAD,ArmorLayerPosition.UNSPECIFIED,helmet);
        var r=new ArmorDamageResolver().resolve(new PhysicalDamage(0,0,10),ArmorCombatHitbox.HELMET,layout,999);
        close(r.netDamage().blunt(),15,"HEAD conserva B x1,5");
    }

    private static void firearmHandlingStillUsesEffectiveWeight() {
        var rifle=domain.inventory.item.firearms.FirearmCatalog.bifilarElectromagneticRifleV881();
        double base=rifle.effectiveHandlingWeightKg();
        // La verificación histórica  cubre la correa; aquí sólo garantizamos que requisitos consultan peso efectivo.
        int expected=domain.inventory.item.WeaponRequirementPolicy.calculate(
                rifle.lengthMeters(),base,domain.inventory.item.GripMode.TWO_HANDED,Set.of()).stream()
                .filter(r->r.attribute()==domain.character.sheet.Attribute.FUERZA).findFirst().orElseThrow().minimumValue();
        int actual=rifle.twoHandedRequirements().stream().filter(r->r.attribute()==domain.character.sheet.Attribute.FUERZA)
                .findFirst().orElseThrow().minimumValue();
        if(actual!=expected) throw new AssertionError("El requisito de FUERZA debe derivar del peso efectivo.");
    }

    private static ArmorPiece body(String name,ArmorInventoryCategory category,Map<BodyArmorRegion,Double> coverage,
                                   ArmorProtectionProfile p,ArmorMaterial material){
        return new ArmorPiece(name,name,1,new InventoryFootprint(1,1),category,coverage,p,material,Set.of(material),
                ArmorForm.STANDARD,List.of(),List.of());
    }
    private static WeaponItem weapon(){
        return new WeaponItem("Espada prueba","Arma de verificación .",1,new InventoryFootprint(10,1),1,
                List.of(new WeaponMode("Corte",new LethalityProfile(65,65,20))),List.of(),List.of(),List.of());
    }
    private static void close(double a,double e,String m){if(Math.abs(a-e)>EPS)throw new AssertionError(m+": "+a+" != "+e);}
}
