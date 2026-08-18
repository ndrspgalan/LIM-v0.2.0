package domain.character.canonical;

import domain.character.Gender;
import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.BasicProvisionCatalog;
import domain.inventory.logistics.LogisticsState;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.InventoryStorageModule;
import domain.inventory.logistics.InventoryPhysicalDimensions;
import java.util.*;

/** loadout real de los ocho personajes canónicos a los seis años.
 * Sin armas, armadura bélica, profesión ni abalorio; ropa civil ligera y provisiones sólo si caben. */
public final class CanonicalChildLoadoutCatalog {
    private static final Set<String> CANONICAL_CHILDREN=Set.of("Kenan","Kiara","Jacob","Iván","Alicia","Rhoy","Sofía","Elena");
    private CanonicalChildLoadoutCatalog() {}

    public static CanonicalChildLoadout forProfile(CanonicalCharacterStageProfile profile) {
        Objects.requireNonNull(profile);
        if (profile.stage() != CanonicalLifeStage.CHILD) throw new IllegalArgumentException("El loadout infantil sólo acepta perfiles CHILD.");
        return forCharacter(profile.name(), profile.gender());
    }

    /** Factory de persistencia para las piezas/provisiones CHILD, sin depender de bootstrap. */
    public static java.util.Optional<InventoryEntry> freshEntryByName(String name) {
        Objects.requireNonNull(name);
        return java.util.Optional.ofNullable(switch (name) {
            case "Camisa infantil V881" -> childShirt();
            case "Blusa infantil V881" -> childBlouse();
            case "Pantalón infantil V881" -> childTrousers();
            case "Falda infantil V881" -> childSkirt();
            case "Calcetines infantiles V881" -> childSocks();
            case "Medias infantiles V881" -> childStockings();
            case "Alpargatas infantiles V881" -> childEspadrilles();
            case "Odre" -> BasicProvisionCatalog.childWaterskin();
            case "Uva deshidratada" -> BasicProvisionCatalog.driedGrapes();
            default -> null;
        });
    }

    public static CanonicalChildLoadout forCharacter(String name, Gender gender) {
        Objects.requireNonNull(name); Objects.requireNonNull(gender);
        if(CANONICAL_CHILDREN.stream().noneMatch(n->n.equalsIgnoreCase(name)))
            throw new IllegalArgumentException("No es un personaje canónico CHILD: "+name);
        ArmorPiece torso = gender==Gender.HOMBRE ? childShirt() : childBlouse();
        ArmorPiece legs = gender==Gender.HOMBRE ? childTrousers() : childSkirt();
        ArmorPiece innerFeet = gender==Gender.HOMBRE ? childSocks() : childStockings();
        ArmorPiece shoes = childEspadrilles();
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.INNER,torso)
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.MIDDLE,legs)
                .equip(EquipmentSlot.FEET,ArmorLayerPosition.INNER,innerFeet)
                .equip(EquipmentSlot.FEET,ArmorLayerPosition.OUTER,shoes);
        EquipmentState active=new EquipmentState(Map.of(
                EquipmentSlot.CHEST,torso, EquipmentSlot.LEGGINGS,legs, EquipmentSlot.FEET,shoes));
        // La capacidad no se hardcodea aquí: se deriva de las prendas realmente equipadas mediante el catálogo de almacenamiento.
        LogisticsState logistics=LogisticsState.emptyWithoutPersonalTransport().synchronizeGarmentStorage(layout);
        InventoryState inventory=new InventoryState(active,QuickAccessBar.empty(),logistics,layout);
        InventoryAutoPlacementService placement=new InventoryAutoPlacementService();
        List<String> provisions=new ArrayList<>();
        var water=BasicProvisionCatalog.childWaterskin();
        var wr=placement.admit(inventory,water,InventoryAdmissionSource.SYSTEM_REWARD);
        if(wr.accepted()){ inventory=wr.inventory(); provisions.add(water.name()); }
        var food=BasicProvisionCatalog.driedGrapes();
        var fr=placement.admit(inventory,food,InventoryAdmissionSource.SYSTEM_REWARD);
        if(fr.accepted()){ inventory=fr.inventory(); provisions.add(food.name()); }
        return new CanonicalChildLoadout(inventory,layout,provisions);
    }


    /** Piezas CHILD canónicas frescas para persistencia/hidratación y QA. */
    public static List<ArmorPiece> allCanonicalClothing() {
        return List.of(childShirt(), childBlouse(), childTrousers(), childSkirt(), childSocks(), childStockings(), childEspadrilles());
    }

    private static ArmorPiece childShirt(){ return body("Camisa infantil V881",0.180,ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,.45,BodyArmorRegion.BRACERS,.08),new InventoryFootprint(2,2)).withInnerChestLayer(InnerChestLayer.BASE); }
    private static ArmorPiece childBlouse(){ return body("Blusa infantil V881",0.180,ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,.45,BodyArmorRegion.BRACERS,.08),new InventoryFootprint(2,2)).withInnerChestLayer(InnerChestLayer.BASE); }
    private static ArmorPiece childTrousers(){ return body("Pantalón infantil V881",0.280,ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,.28),new InventoryFootprint(2,2)); }
    private static ArmorPiece childSkirt(){ return body("Falda infantil V881",0.300,ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,.28),new InventoryFootprint(2,2)); }
    private static ArmorPiece childSocks(){ return body("Calcetines infantiles V881",0.050,ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,.04),new InventoryFootprint(1,1)).withFeetLayer(FeetLayer.INNER); }
    private static ArmorPiece childStockings(){ return body("Medias infantiles V881",0.060,ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,.04),new InventoryFootprint(1,1)).withFeetLayer(FeetLayer.INNER); }
    private static ArmorPiece childEspadrilles(){ return body("Alpargatas infantiles V881",0.180,ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,.05),new InventoryFootprint(2,1)).withFeetLayer(FeetLayer.OUTER); }
    private static ArmorPiece body(String name,double weight,ArmorInventoryCategory category,Map<BodyArmorRegion,Double> coverage,InventoryFootprint footprint){
        return new ArmorPiece(name,"Prenda civil dimensionada para un niño de seis años. Prioriza movilidad, reparación sencilla y una masa muy baja; no es armadura bélica.",weight,footprint,category,coverage,new ArmorProtectionProfile(2,5,2),ArmorMaterial.CLOTH,Set.of(ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("ETAPA | CHILD · 6 años","MATERIAL | TELA","PESO | "+weight+" kg"),List.of());
    }
}
