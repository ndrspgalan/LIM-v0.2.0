package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.misc.CurrencyType;
import domain.inventory.logistics.InventoryCompartmentType;
import java.util.*;

/** patrimonio inicial físico de Maestro. */
public final class TeacherStartingEquipmentCatalog {
    private TeacherStartingEquipmentCatalog(){}
    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s); Objects.requireNonNull(c);
        if(s.profession()!=Profession.TEACHER)throw new IllegalArgumentException("No es Maestro: "+s);
        if(TeacherCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Combinación deprecated: "+s+"/"+c);
        boolean high=highContinuity(s);
        List<String> inv=new ArrayList<>();
        inv.add("Pan"); inv.add("Fruta"); inv.add("Odre"); inv.add("Emplasto de milenrama"); inv.add("Caja de Herramientas");
        switch(s){
            case FREQUENCY_PHYSICIAN,VETERINARIAN,REGENERATIONIST -> {inv.add("Emplasto de milenrama");inv.add("Apósito de musgo de turbera");}
            case FREQUENCY_RESEARCHER,ELECTROATMOSPHERIC_NETWORK_ENGINEER,ELECTROATMOSPHERIC_CAPTATION_ENGINEER,
                 ELECTROATMOSPHERIC_SAFETY_ENGINEER,ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER,
                 RAILWAY_INFRASTRUCTURE_ENGINEER,ELECTROMAGNETIC_TRANSPORT_PLANNER -> inv.add("MAGNETLAMPE");
            case SURGEON -> {inv.add("Emplasto de milenrama");inv.add("Apósito de musgo de turbera");inv.add("MAGNETLAMPE");}
            case KINGDOM_MESSENGER,CYCLIST_MESSENGER,FORESTRY_MANAGER,PROSPECTOR -> {}
            case SANITARY_MASTER -> {inv.add("MAGNETLAMPE");inv.add("Botella de Líquido Refrigerante");}
            default -> { if(high) inv.add("MAGNETLAMPE"); }
        }
        inv.add("Esencia de lucidez");
        if(high) inv.add("Frasco de I-RND");

        Optional<domain.inventory.logistics.PersonalTransportType> transport =
                (s==Subprofession.PROSPECTOR || s==Subprofession.FORESTRY_MANAGER)
                        ? Optional.of(domain.inventory.logistics.PersonalTransportType.HORSE_DRAFT)
                        : Optional.empty();

        List<InventoryCompartmentType> ex=new ArrayList<>();
        ex.add(InventoryCompartmentType.BACKPACK);
        if(transport.isPresent())ex.add(InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT);
        if(high)ex.add(InventoryCompartmentType.BANDOLIER);

        int money=s.monthlyReferenceValeritas()>=2200?150:110;
        if(s==Subprofession.KINGDOM_MESSENGER)money=80;
        if(s==Subprofession.CYCLIST_MESSENGER)money=65;
        if(high)money=180;

        List<ArmorMaterial> mats=new ArrayList<>();
        switch(s){
            case ELECTROATMOSPHERIC_NETWORK_ENGINEER,ELECTROATMOSPHERIC_CAPTATION_ENGINEER,
                 ELECTROATMOSPHERIC_SAFETY_ENGINEER,ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER,
                 RAILWAY_INFRASTRUCTURE_ENGINEER,ELECTROMAGNETIC_TRANSPORT_PLANNER -> {
                    mats.add(ArmorMaterial.DIELECTRIC_CLOTH); mats.add(ArmorMaterial.VULCANIZED_RUBBER);
            }
            case PROSPECTOR -> mats.add(ArmorMaterial.STEEL);
            case REGENERATIONIST,CONTINUITY_EPIGENETICIST,NEUROARCHITECT,SOUL_RESEARCHER,
                 SOUL_TRANSFUSIONIST,SILICIC_METAMORPHOSIS_RESEARCHER,PERMANENCE_RESEARCHER,ENLIGHTENED -> {
                    mats.add(ArmorMaterial.LAMINATED_GLASS); mats.add(ArmorMaterial.VULCANIZED_RUBBER);
            }
            default -> {}
        }
        if(high)mats.add(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE);

        var a=domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
        return CanonicalStartingEquipmentPackingPolicy.requireValid(new CanonicalStartingEquipment(
                garments(c),List.copyOf(inv),Optional.of(a),List.of(),List.of(),transport,List.copyOf(ex),
                List.of(new CurrencyStack(CurrencyType.VALERITA,money)),List.copyOf(mats)));
    }

    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){
        return CivilianStartingEquipmentSupport.placement(equipment(s,c));
    }

    private static List<ArmorPiece> garments(CharacterClass c){
        boolean female=c==CharacterClass.ESPECIALISTA||c==CharacterClass.APODERADO||c==CharacterClass.HERALDO;
        if(female)return List.of(ArmorCatalog.innerBlouse(),ArmorCatalog.middleWaistcoat(),ArmorCatalog.outerMorningCoatV881(),
                ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),
                ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerLeatherAnkleBootsV881());
        return List.of(ArmorCatalog.innerUndershirt(),ArmorCatalog.innerShirt(),ArmorCatalog.middleLongWaistcoat(),
                ArmorCatalog.outerMorningCoatV881(),ArmorCatalog.innerLongDrawersV881(),ArmorCatalog.middleFormalTrousersV881(),
                ArmorCatalog.innerFeetSocksV881(),ArmorCatalog.outerLeatherAnkleBootsV881());
    }

    private static boolean highContinuity(Subprofession s){
        return switch(s){
            case REGENERATIONIST,CONTINUITY_EPIGENETICIST,NEUROARCHITECT,SOUL_RESEARCHER,
                 SOUL_TRANSFUSIONIST,SILICIC_METAMORPHOSIS_RESEARCHER,PERMANENCE_RESEARCHER,ENLIGHTENED -> true;
            default -> false;
        };
    }
}
