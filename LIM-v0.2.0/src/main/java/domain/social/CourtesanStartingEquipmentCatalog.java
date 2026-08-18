package domain.social;

import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;

/** patrimonio inicial individualizado de Cortesana. */
public final class CourtesanStartingEquipmentCatalog {
    private CourtesanStartingEquipmentCatalog(){}
    public static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s); Objects.requireNonNull(c);
        if(s.profession()!=Profession.COURTESAN)throw new IllegalArgumentException("Sólo Cortesana.");
        if(CourtesanCanonicalProfiles.isDeprecated(s,c))throw new IllegalArgumentException("Perfil Cortesana deprecated: "+s+" / "+c);
        List<ArmorPiece> garments=switch(s){
            case SEX_WORKER -> practical(false);
            case PROFESSIONAL_COMPANION -> salon(false);
            case SALON_COURTESAN -> salon(false);
            default -> throw new IllegalArgumentException();
        };
        List<String> inventory=switch(s){
            case SEX_WORKER -> List.of("Pan","Fruta","Odre","ESPEJO DE BOLSILLO");
            case PROFESSIONAL_COMPANION -> List.of("Pan","Bizcocho","Fruta","Odre","ESPEJO DE BOLSILLO");
            case SALON_COURTESAN -> List.of("Bizcocho","Fruta","Odre","ESPEJO DE BOLSILLO");
            default -> throw new IllegalArgumentException();
        };
        int cash=switch(s){case SEX_WORKER->75;case PROFESSIONAL_COMPANION->95;case SALON_COURTESAN->130;default->0;};
        var a=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),c.name());
        var e=new CanonicalStartingEquipment(garments,inventory,Optional.of(a),List.of(),List.of(),Optional.empty(),
                List.of(InventoryCompartmentType.BACKPACK),List.of(new CurrencyStack(CurrencyType.VALERITA,cash)),List.of(ArmorMaterial.CLOTH));
        return CanonicalStartingEquipmentPackingPolicy.requireValid(e);
    }
    public static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){
        return CivilianStartingEquipmentSupport.placement(equipment(s,c));
    }
    private static List<ArmorPiece> practical(boolean male){
        return List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.innerWomensDrawersV881(),
                ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),ArmorCatalog.innerFeetStockingsV881(),
                ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.outerSackCoatV881());
    }
    private static List<ArmorPiece> salon(boolean male){
        return List.of(ArmorCatalog.innerChemise(),ArmorCatalog.innerBlouse(),ArmorCatalog.middleRegionalBodice(),
                ArmorCatalog.innerWomensDrawersV881(),ArmorCatalog.innerPetticoatV881(),ArmorCatalog.middleWalkingSkirtV881(),
                ArmorCatalog.innerFeetStockingsV881(),ArmorCatalog.outerCourtShoesV881(),ArmorCatalog.outerSackCoatV881(),
                ArmorCatalog.walkingHatV881());
    }
}
