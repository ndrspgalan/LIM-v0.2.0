package domain.social;
import domain.character.CharacterClass;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;

/**  — equipamiento canónico de MERCENARY. */
public final class MercenaryStartingEquipmentCatalog {
    private MercenaryStartingEquipmentCatalog(){}
    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        if(s.profession()!=Profession.MERCENARY)throw new IllegalArgumentException("Profesión incorrecta.");
        if(MercenaryCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil deprecated: "+s+" / "+c);
        CanonicalStartingEquipment e=switch(s){
            case COMPANY_CONTRACTOR -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.workshopLeatherApronV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Martillo de bola","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),90,"Cápsula de Gas Amonio V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.paperLeggingsV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.crusaderHelmetV881(),
                List.of("Boathook","Hoz"),PersonalTransportType.HORSE_DRAFT,List.of("Caja del Artesano"),
                List.of(ArmorMaterial.WOOD),90,"Granada Incendiaria de Terracota V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Guadaña","Horca"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),95,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case CONTRACTUAL_SHOCK_COMBATANT -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Maza Electro-mecánica V881","Pavesina Cementada de Asalto V881"),null,List.of(),
                List.of(),110,"Cuchillo Arrojadizo V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Espada Helicoidal","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),110,"Cápsula de Gas Amonio V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Espadón de Rotor","Daga"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),120,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case CONVOY_ESCORT -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Fusil de Repetición V881","Cimitarra"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),90,"Granada de Huevo con Fósforo y Azufre V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Rifle Neumático de Repetición V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),95,"Cuchillo Arrojadizo V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Arco Compuesto","Cuchillo de Carnicero"),PersonalTransportType.HORSE_LEISURE,List.of("Tarro de Resina"),
                List.of(),95,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case EXCEPTIONAL_ASSET_RECOVERER -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Cañón Antimaterial V881","Daga"),PersonalTransportType.HORSE_DRAFT,List.of("Monocular de Reconocimiento V881"),
                List.of(),135,"Granada Incendiaria de Terracota V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Lanza-Arcos Electrodinámico V881","Espada Helicoidal"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("Monocular de Reconocimiento V881"),
                List.of(),140,"Granada de Huevo con Fósforo y Azufre V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Fusil Bifilar Electromagnético V881","Maza Electro-mecánica V881"),PersonalTransportType.HORSE_DRAFT,List.of("Monocular de Reconocimiento V881"),
                List.of(),140,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case MERCENARY_COMPANY_DIRECTOR -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Pistola Autocargadora V881","Cimitarra"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),180,"Cápsula de Gas Amonio V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Pistola Autocargadora V881","Espada Helicoidal"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),190,"Granada Incendiaria de Terracota V881"); case INDOMITO -> make(true,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Subfusil Automático V881","Cimitarra"),PersonalTransportType.HORSE_RACING,List.of(),
                List.of(),180,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case MOTORCYCLE_COURIER -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Pistola Autocargadora V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),85,"Cuchillo Arrojadizo V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),90,"Cápsula de Gas Amonio V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Pistola Autocargadora V881","Bō"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),85,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case FRONTIER_SKIRMISHER -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Arco Compuesto","Daga"),PersonalTransportType.HORSE_RACING,List.of("Tarro de Resina"),
                List.of(),80,"Granada de Huevo con Fósforo y Azufre V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.paperBracersV881(),ArmorCatalog.paperLeggingsV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.crusaderHelmetV881(),
                List.of("Arco Simple Recurvo","Cuchillo de Carnicero"),PersonalTransportType.HORSE_LEISURE,List.of("Tarro de Resina"),
                List.of(),80,"Cuchillo Arrojadizo V881"); case HERALDO -> make(false,
                ArmorCatalog.paperChestV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Honda","Daga"),PersonalTransportType.HORSE_RACING,List.of(),
                List.of(),80,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case MOBILE_ESCORT -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Rifle Neumático de Repetición V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_RACING,List.of(),
                List.of(),90,"Granada Incendiaria de Terracota V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),95,"Granada de Huevo con Fósforo y Azufre V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Cimitarra","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),90,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case TECHNICAL_RECOVERY_OPERATOR -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Lanza-Arcos Electrodinámico V881","Pistola Autocargadora V881"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("Caja de Herramientas","Monocular de Reconocimiento V881"),
                List.of(ArmorMaterial.DIELECTRIC_CLOTH),125,"Cápsula de Gas Amonio V881"); case APODERADO -> make(false,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Fusil Bifilar Electromagnético V881","Daga"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas","Botella de Líquido Refrigerante"),
                List.of(ArmorMaterial.STEEL,ArmorMaterial.LAMINATED_GLASS),130,"Granada Incendiaria de Terracota V881"); case HERALDO -> make(false,
                ArmorCatalog.workshopLeatherApronV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Martillo de bola","Pistola Autocargadora V881"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.VULCANIZED_RUBBER),120,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case SABOTAGE_DENIAL_SPECIALIST -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.fireproofSuit(),null,null,null,null,
                List.of("Pistola Autocargadora V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("MAGNETLAMPE"),
                List.of(),110,"Cuchillo Arrojadizo V881"); case APODERADO -> make(false,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of("KNIJPKAT"),
                List.of(),110,"Cápsula de Gas Amonio V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.paperBracersV881(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Rociador de Cal Viva V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of("MAGNETLAMPE"),
                List.of(),115,"Granada Incendiaria de Terracota V881");
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
        if(MercenaryCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil deprecated.");
        return switch(s){
            case COMPANY_CONTRACTOR -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.workshopLeatherApronV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Martillo de bola","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),90,"Cápsula de Gas Amonio V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.paperLeggingsV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.crusaderHelmetV881(),
                List.of("Boathook","Hoz"),PersonalTransportType.HORSE_DRAFT,List.of("Caja del Artesano"),
                List.of(ArmorMaterial.WOOD),90,"Granada Incendiaria de Terracota V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Guadaña","Horca"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.WOOD,ArmorMaterial.STEEL),95,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case CONTRACTUAL_SHOCK_COMBATANT -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Maza Electro-mecánica V881","Pavesina Cementada de Asalto V881"),null,List.of(),
                List.of(),110,"Cuchillo Arrojadizo V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Espada Helicoidal","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),110,"Cápsula de Gas Amonio V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Espadón de Rotor","Daga"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),120,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case CONVOY_ESCORT -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Fusil de Repetición V881","Cimitarra"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),90,"Granada de Huevo con Fósforo y Azufre V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.paperChestV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Rifle Neumático de Repetición V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_DRAFT,List.of(),
                List.of(),95,"Cuchillo Arrojadizo V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Arco Compuesto","Cuchillo de Carnicero"),PersonalTransportType.HORSE_LEISURE,List.of("Tarro de Resina"),
                List.of(),95,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case EXCEPTIONAL_ASSET_RECOVERER -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Cañón Antimaterial V881","Daga"),PersonalTransportType.HORSE_DRAFT,List.of("Monocular de Reconocimiento V881"),
                List.of(),135,"Granada Incendiaria de Terracota V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Lanza-Arcos Electrodinámico V881","Espada Helicoidal"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("Monocular de Reconocimiento V881"),
                List.of(),140,"Granada de Huevo con Fósforo y Azufre V881"); case INDOMITO -> make(true,
                ArmorCatalog.historicalHeavyLamellarChest(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Fusil Bifilar Electromagnético V881","Maza Electro-mecánica V881"),PersonalTransportType.HORSE_DRAFT,List.of("Monocular de Reconocimiento V881"),
                List.of(),140,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case MERCENARY_COMPANY_DIRECTOR -> switch(c){
            case LUCHADOR -> make(true,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Pistola Autocargadora V881","Cimitarra"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),180,"Cápsula de Gas Amonio V881"); case INTELECTUAL -> make(true,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Pistola Autocargadora V881","Espada Helicoidal"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),190,"Granada Incendiaria de Terracota V881"); case INDOMITO -> make(true,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.paperBracersV881(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Subfusil Automático V881","Cimitarra"),PersonalTransportType.HORSE_RACING,List.of(),
                List.of(),180,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case MOTORCYCLE_COURIER -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.leatherOrnamentedHispanicGaitersV881(),ArmorCatalog.leatherHighRidingBootsV881(),ArmorCatalog.hardenedLeatherJetHelmet(),
                List.of("Pistola Autocargadora V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),85,"Cuchillo Arrojadizo V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherShotgunChapsV881(),ArmorCatalog.leatherHeavyWorkBootsV881(),ArmorCatalog.workshopGoggles(),
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),90,"Cápsula de Gas Amonio V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Pistola Autocargadora V881","Bō"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),85,"Granada Incendiaria de Terracota V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case FRONTIER_SKIRMISHER -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherCharroChapsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.beardedHelmetV881(),
                List.of("Arco Compuesto","Daga"),PersonalTransportType.HORSE_RACING,List.of("Tarro de Resina"),
                List.of(),80,"Granada de Huevo con Fósforo y Azufre V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.paperBracersV881(),ArmorCatalog.paperLeggingsV881(),ArmorCatalog.outerMoccasinsV881(),ArmorCatalog.crusaderHelmetV881(),
                List.of("Arco Simple Recurvo","Cuchillo de Carnicero"),PersonalTransportType.HORSE_LEISURE,List.of("Tarro de Resina"),
                List.of(),80,"Cuchillo Arrojadizo V881"); case HERALDO -> make(false,
                ArmorCatalog.paperChestV881(),ArmorCatalog.historicalHeavyLamellarBracers(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Honda","Daga"),PersonalTransportType.HORSE_RACING,List.of(),
                List.of(),80,"Cápsula de Gas Amonio V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case MOBILE_ESCORT -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.hardenedLeatherAviatorJacketV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.hardenedLeatherLeggings(),ArmorCatalog.outerLeatherWorkShoesV881(),ArmorCatalog.retractableAeronautHelmet(),
                List.of("Rifle Neumático de Repetición V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_RACING,List.of(),
                List.of(),90,"Granada Incendiaria de Terracota V881"); case APODERADO -> make(false,
                ArmorCatalog.hardenedLeatherCrossedMotorcycleJacketV881(),ArmorCatalog.hardenedLeatherFingerlessGloves(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.outerLeatherAnkleBootsV881(),ArmorCatalog.integralRespirator(),
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of(),
                List.of(),95,"Granada de Huevo con Fósforo y Azufre V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.workshopBracers(),ArmorCatalog.leatherRigidSideClosureGaitersV881(),ArmorCatalog.outerShortFieldBootsV881(),ArmorCatalog.paperHelmetV881(),
                List.of("Cimitarra","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of(),
                List.of(),90,"Cuchillo Arrojadizo V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case TECHNICAL_RECOVERY_OPERATOR -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Lanza-Arcos Electrodinámico V881","Pistola Autocargadora V881"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("Caja de Herramientas","Monocular de Reconocimiento V881"),
                List.of(ArmorMaterial.DIELECTRIC_CLOTH),125,"Cápsula de Gas Amonio V881"); case APODERADO -> make(false,
                ArmorCatalog.engineerSuit(),null,null,null,null,
                List.of("Fusil Bifilar Electromagnético V881","Daga"),PersonalTransportType.HORSE_DRAFT,List.of("Caja de Herramientas","Botella de Líquido Refrigerante"),
                List.of(ArmorMaterial.STEEL,ArmorMaterial.LAMINATED_GLASS),130,"Granada Incendiaria de Terracota V881"); case HERALDO -> make(false,
                ArmorCatalog.workshopLeatherApronV881(),ArmorCatalog.hardenedLeatherBracers(),ArmorCatalog.leatherBatwingChapsV881(),ArmorCatalog.leatherOxfordBrogueShoesV881(),ArmorCatalog.normalVisionGlassesV881(),
                List.of("Martillo de bola","Pistola Autocargadora V881"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("Caja de Herramientas"),
                List.of(ArmorMaterial.VULCANIZED_RUBBER),120,"Granada de Huevo con Fósforo y Azufre V881");
            default -> throw new IllegalArgumentException("Clase deprecated para "+s+": "+c);
        };
case SABOTAGE_DENIAL_SPECIALIST -> switch(c){
            case ESPECIALISTA -> make(false,
                ArmorCatalog.fireproofSuit(),null,null,null,null,
                List.of("Pistola Autocargadora V881","Daga"),PersonalTransportType.MOTORCYCLE_CARDAN_V881,List.of("MAGNETLAMPE"),
                List.of(),110,"Cuchillo Arrojadizo V881"); case APODERADO -> make(false,
                ArmorCatalog.insulatingSuit(),null,null,null,null,
                List.of("Subfusil Automático V881","Daga"),PersonalTransportType.BICYCLE_FOLDING_V881,List.of("KNIJPKAT"),
                List.of(),110,"Cápsula de Gas Amonio V881"); case HERALDO -> make(false,
                ArmorCatalog.hardenedLeatherChest(),ArmorCatalog.paperBracersV881(),ArmorCatalog.historicalHeavyLamellarLeggings(),ArmorCatalog.outerBabouchesV881(),ArmorCatalog.spartanHelmetV881(),
                List.of("Rociador de Cal Viva V881","Pistola Autocargadora V881"),PersonalTransportType.HORSE_LEISURE,List.of("MAGNETLAMPE"),
                List.of(),115,"Granada Incendiaria de Terracota V881");
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