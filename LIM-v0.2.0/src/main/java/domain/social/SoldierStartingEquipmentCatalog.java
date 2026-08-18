package domain.social;
import domain.character.CharacterClass;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;

/**  — equipamiento canónico de SOLDIER. */
public final class SoldierStartingEquipmentCatalog {
    private SoldierStartingEquipmentCatalog(){}
    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        if(s.profession()!=Profession.SOLDIER)throw new IllegalArgumentException("Profesión incorrecta.");
        if(SoldierCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil deprecated: "+s+" / "+c);
        CanonicalStartingEquipment e=switch(s){
            case V881_RIFLEMAN -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Fusil de Repetición V881","Daga"),null,List.of(),
                List.of(),65,"Cápsula de Gas Amonio V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Fusil Bifilar Electromagnético V881","Pistola Autocargadora V881"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of(),
                List.of(),70,"Granada Incendiaria de Terracota V881"); case INDOMITO -> make(true,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Rifle Neumático de Repetición V881","Hacha de Leñador"),null,List.of(),
                List.of(),65,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case V881_CAMPAIGN_SAPPER -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.workshopLeatherApronV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Zapapico","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas","Emplasto de milenrama"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),70,"Cuchillo Arrojadizo V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Piqueta","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas","Botella de Líquido Refrigerante"),
                List.of(ArmorMaterial.VULCANIZED_RUBBER,ArmorMaterial.DIELECTRIC_CLOTH),75,"Cápsula de Gas Amonio V881"); case INDOMITO -> make(true,
                ArmorCatalog.fireproofSuit(),null,null,null,null,
                List.of("Pico","Hacha de Leñador"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),70,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case V881_HEAVY_WEAPONS_SPECIALIST -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Cañón Antimaterial V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),85,"Granada de Huevo con Fósforo y Azufre V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Cañón de Racimo V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Botella de Líquido Refrigerante"),
                List.of(),90,"Cuchillo Arrojadizo V881"); case INDOMITO -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Rociador de Cal Viva V881","Pavesina Cementada de Asalto V881"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),85,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case INSTITUTIONAL_SHOCK_COMBATANT -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Maza Electro-mecánica V881","Daga"),null,List.of(),
                List.of(),80,"Granada Incendiaria de Terracota V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Katana Termo-mecánica V881","Pistola Autocargadora V881"),null,List.of(),
                List.of(),80,"Granada de Huevo con Fósforo y Azufre V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Espadón de Rotor","Cimitarra"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),90,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case KINGDOM_AGENT -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Pistola Autocargadora V881","Bō"),PersonalTransportType.BICYCLE_MILITARY_V881,List.of(),
                List.of(),55,"Cápsula de Gas Amonio V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of(),
                List.of(),60,"Granada Incendiaria de Terracota V881"); case HERALDO -> make(false,
                ArmorCatalog.paperChestV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Pistola Autocargadora V881","Pavesina Cementada de Asalto V881"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),60,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case V881_SUPPORT_MARKSWOMAN -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Fusil de Repetición V881","Daga"),PersonalTransportType.HORSE_RACING,List.of("Monocular de Reconocimiento V881"),
                List.of(),70,"Cuchillo Arrojadizo V881"); case APODERADO -> make(false,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.paperLeggingsV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.crusaderHelmetV881(),
                List.of("Rifle Neumático de Repetición V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of("Monocular de Reconocimiento V881"),
                List.of(),70,"Cápsula de Gas Amonio V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Fusil Bifilar Electromagnético V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_RACING,List.of("Monocular de Reconocimiento V881"),
                List.of(),75,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case STRATEGIC_INSTALLATION_CUSTODIAN -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Lanza-Arcos Electrodinámico V881","Pistola Autocargadora V881"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("MAGNETLAMPE"),
                List.of(),85,"Granada de Huevo con Fósforo y Azufre V881"); case APODERADO -> make(false,
                ArmorCatalog.fireproofSuit(),null,null,null,null,
                List.of("Rociador de Cal Viva V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("MAGNETLAMPE"),
                List.of(),85,"Cuchillo Arrojadizo V881"); case HERALDO -> make(false,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.BICYCLE_MILITARY_V881,List.of("MAGNETLAMPE"),
                List.of(),90,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case RAILWAY_GUARD -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Subfusil Automático V881","Bō"),PersonalTransportType.BICYCLE_MILITARY_V881,List.of(),
                List.of(),60,"Granada Incendiaria de Terracota V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Rifle Neumático de Repetición V881","Daga"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),60,"Granada de Huevo con Fósforo y Azufre V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Pistola Autocargadora V881","Cimitarra"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of(),
                List.of(),60,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
            default -> throw new IllegalArgumentException("Subprofesión fuera de : "+s);
        };
        CanonicalCombatWeaponSlotPolicy.validate(e);
        placement(s,c).validateAgainst(e);
        e = withNarrativeAccessory(e,s,c);
        return CanonicalStartingEquipmentPackingPolicy.requireValid(e);
    }
    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){
        // build() evita recursión de equipment()->placement().
        CanonicalStartingEquipment e=rawEquipment(s,c);
        return CombatStartingEquipmentSupport.placement(e);
    }
    private static CanonicalStartingEquipment rawEquipment(Subprofession s,CharacterClass c){
        if(SoldierCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil deprecated.");
        return switch(s){
            case V881_RIFLEMAN -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Fusil de Repetición V881","Daga"),null,List.of(),
                List.of(),65,"Cápsula de Gas Amonio V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Fusil Bifilar Electromagnético V881","Pistola Autocargadora V881"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of(),
                List.of(),70,"Granada Incendiaria de Terracota V881"); case INDOMITO -> make(true,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Rifle Neumático de Repetición V881","Hacha de Leñador"),null,List.of(),
                List.of(),65,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case V881_CAMPAIGN_SAPPER -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.workshopLeatherApronV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Zapapico","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas","Emplasto de milenrama"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),70,"Cuchillo Arrojadizo V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Piqueta","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas","Botella de Líquido Refrigerante"),
                List.of(ArmorMaterial.VULCANIZED_RUBBER,ArmorMaterial.DIELECTRIC_CLOTH),75,"Cápsula de Gas Amonio V881"); case INDOMITO -> make(true,
                ArmorCatalog.fireproofSuit(),null,null,null,null,
                List.of("Pico","Hacha de Leñador"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),70,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case V881_HEAVY_WEAPONS_SPECIALIST -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Cañón Antimaterial V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),85,"Granada de Huevo con Fósforo y Azufre V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Cañón de Racimo V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Botella de Líquido Refrigerante"),
                List.of(),90,"Cuchillo Arrojadizo V881"); case INDOMITO -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Rociador de Cal Viva V881","Pavesina Cementada de Asalto V881"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),85,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case INSTITUTIONAL_SHOCK_COMBATANT -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Maza Electro-mecánica V881","Daga"),null,List.of(),
                List.of(),80,"Granada Incendiaria de Terracota V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Katana Termo-mecánica V881","Pistola Autocargadora V881"),null,List.of(),
                List.of(),80,"Granada de Huevo con Fósforo y Azufre V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Espadón de Rotor","Cimitarra"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),90,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case KINGDOM_AGENT -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Pistola Autocargadora V881","Bō"),PersonalTransportType.BICYCLE_MILITARY_V881,List.of(),
                List.of(),55,"Cápsula de Gas Amonio V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of(),
                List.of(),60,"Granada Incendiaria de Terracota V881"); case HERALDO -> make(false,
                ArmorCatalog.paperChestV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Pistola Autocargadora V881","Pavesina Cementada de Asalto V881"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),60,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case V881_SUPPORT_MARKSWOMAN -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Fusil de Repetición V881","Daga"),PersonalTransportType.HORSE_RACING,List.of("Monocular de Reconocimiento V881"),
                List.of(),70,"Cuchillo Arrojadizo V881"); case APODERADO -> make(false,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.paperLeggingsV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.crusaderHelmetV881(),
                List.of("Rifle Neumático de Repetición V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of("Monocular de Reconocimiento V881"),
                List.of(),70,"Cápsula de Gas Amonio V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Fusil Bifilar Electromagnético V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_RACING,List.of("Monocular de Reconocimiento V881"),
                List.of(),75,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case STRATEGIC_INSTALLATION_CUSTODIAN -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Lanza-Arcos Electrodinámico V881","Pistola Autocargadora V881"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("MAGNETLAMPE"),
                List.of(),85,"Granada de Huevo con Fósforo y Azufre V881"); case APODERADO -> make(false,
                ArmorCatalog.fireproofSuit(),null,null,null,null,
                List.of("Rociador de Cal Viva V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("MAGNETLAMPE"),
                List.of(),85,"Cuchillo Arrojadizo V881"); case HERALDO -> make(false,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.BICYCLE_MILITARY_V881,List.of("MAGNETLAMPE"),
                List.of(),90,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case RAILWAY_GUARD -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Subfusil Automático V881","Bō"),PersonalTransportType.BICYCLE_MILITARY_V881,List.of(),
                List.of(),60,"Granada Incendiaria de Terracota V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Rifle Neumático de Repetición V881","Daga"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),60,"Granada de Huevo con Fósforo y Azufre V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Pistola Autocargadora V881","Cimitarra"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of(),
                List.of(),60,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
            default -> throw new IllegalArgumentException("Subprofesión fuera de : "+s);
        };
    }
    private static CanonicalStartingEquipment make(boolean male,ArmorPiece chest,ArmorPiece bracers,ArmorPiece legs,ArmorPiece feet,ArmorPiece head,
            List<String> weapons,PersonalTransportType transport,List<String> extras,List<ArmorMaterial> materials,int cash,String throwable){
        boolean rotor=weapons.contains("Espadón de Rotor");
        boolean bow=weapons.contains("Arco Simple Recurvo")||weapons.contains("Arco Compuesto");
        ArrayList<String> inv=new ArrayList<>(CombatStartingEquipmentSupport.commonInventory(throwable));
        inv.addAll(extras);
        inv.addAll(CombatStartingEquipmentSupport.weaponAccessoriesFor(weapons));
        List<InventoryCompartmentType> ex=CombatStartingEquipmentSupport.expanders(transport,rotor,bow);
        return new CanonicalStartingEquipment(
            CombatStartingEquipmentSupport.armor(male,chest,bracers,legs,feet,head),
            List.copyOf(inv),Optional.empty(),weapons,CombatStartingEquipmentSupport.ammoFor(weapons),
            CombatStartingEquipmentSupport.transport(transport),ex,
            List.of(new CurrencyStack(CurrencyType.VALERITA,cash)),materials);
    }

    private static CanonicalStartingEquipment withNarrativeAccessory(CanonicalStartingEquipment e,Subprofession s,CharacterClass c){
        var a=domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
        return new CanonicalStartingEquipment(e.wornGarments(),e.inventoryObjectNames(),Optional.of(a),e.weaponNames(),
                e.ammunitionNames(),e.personalTransport(),e.inventoryExpanders(),e.currencyStacks(),e.materialUnits());
    }
}