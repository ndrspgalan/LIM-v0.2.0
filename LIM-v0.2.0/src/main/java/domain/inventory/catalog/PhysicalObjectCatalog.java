package domain.inventory.catalog;

import domain.inventory.InventoryEntry;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.misc.MucusCrystalCatalog;
import domain.inventory.item.misc.MucusTearItem;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.item.firearmAccessories.FirearmAccessoryCatalog;
import domain.inventory.logistics.InventoryCompartmentType;

import java.text.Normalizer;
import java.util.*;

/**
 * : autoridad neutral de identidad para TODO tipo físico no-armadura/no-arma principal
 * que puede existir como objeto de inventario. Los constructores concretos pueden vivir en
 * catálogos especializados, pero nombre, identidad estable, familia y semántica física se
 * resuelven aquí.
 */
public final class PhysicalObjectCatalog {
    private static final Map<String, PhysicalObjectDefinition> BY_NAME = build();
    private static final Map<CanonicalObjectTypeId, PhysicalObjectDefinition> BY_ID =
            BY_NAME.values().stream().collect(java.util.stream.Collectors.toUnmodifiableMap(
                    PhysicalObjectDefinition::typeId, d->d));

    private PhysicalObjectCatalog(){}

    public static List<PhysicalObjectDefinition> all() {
        return BY_ID.values().stream()
                .sorted(Comparator.comparing(d->d.typeId().value()))
                .toList();
    }

    public static PhysicalObjectDefinition definitionFor(InventoryEntry entry) {
        Objects.requireNonNull(entry);
        return definitionForName(entry.name());
    }

    public static PhysicalObjectDefinition definitionForName(String name) {
        Objects.requireNonNull(name);
        PhysicalObjectDefinition d=BY_NAME.get(normalizeDisplay(name));
        if(d==null) throw new IllegalArgumentException("Tipo físico no registrado en el catálogo canónico: "+name);
        return d;
    }

    public static CanonicalObjectTypeId typeIdOf(InventoryEntry entry) {
        return definitionFor(entry).typeId();
    }

    public static boolean containsName(String name) {
        return name!=null && BY_NAME.containsKey(normalizeDisplay(name));
    }

    private static Map<String, PhysicalObjectDefinition> build() {
        LinkedHashMap<String, PhysicalObjectDefinition> m=new LinkedHashMap<>();

        // Misceláneos ordinarios y persistentes existentes.
        registerMany(m,"misc",PhysicalStorageSemantics.INDIVIDUAL,false,
                "Pan","Cecina","Bizcocho","Fruta",
                "Inyección estimulante","Emplasto de milenrama","Apósito de musgo de turbera","Corteza de sauce",
                "Esencia de lucidez","Frasco de I-RND","MAGNETLAMPE","KNIJPKAT",
                "Monocular de Reconocimiento V881","Maletín profesional de Alicia e Iván",
                "Caja del Artesano","Caja de Herramientas","Piedra de afilar","Piedra de Mercurio",
                "Amadou","Pedernal","Patata cruda","Conversor de combustible improvisado",
                "Bidón de Etanol","Bidón de Queroseno Ligero",
                "Batería Portátil Electromagnética V881","Cargador portátil de Batería Electromagnética V881",
                "Cámara fotográfica V881","Contenedor toxicológico Stas-Otto V881","Aparato de Marsh V881");
        register(m,"misc","Frutos secos",PhysicalStorageSemantics.PERSISTENT_CONTAINER,false);
        register(m,"misc","Uva deshidratada",PhysicalStorageSemantics.PERSISTENT_CONTAINER,false);
        register(m,"misc","Odre",PhysicalStorageSemantics.PERSISTENT_CONTAINER,false);
        register(m,"misc","Petaca de hidromiel",PhysicalStorageSemantics.PERSISTENT_CONTAINER,false);
        register(m,"misc","Tarro de Resina",PhysicalStorageSemantics.PERSISTENT_CONTAINER,false);
        register(m,"misc","Botella de Líquido Refrigerante",PhysicalStorageSemantics.PERSISTENT_CONTAINER,false);
        for(String currency : List.of("Valeritas","Sueldos","Berylares","Reales A5"))
            register(m,"currency",currency,PhysicalStorageSemantics.CURRENCY_STACK,false);

        // Materiales canónicos : cada unidad es un objeto físico individual.
        for (domain.inventory.item.armor.MaterialItem material : domain.inventory.item.armor.MaterialCatalog.allCanonicalUnits()) {
            register(m,"material",material.name(),PhysicalStorageSemantics.INDIVIDUAL,false);
        }

        // Mucus: Lágrima es masa homogénea fusionable; cristales son unidades físicas dinámicas.
        register(m,"mucus","Lágrima de Mucus Blanco",PhysicalStorageSemantics.PERSISTENT_CONTAINER,true);
        for (InventoryEntry e : List.of(MucusCrystalCatalog.yellow(), MucusCrystalCatalog.greenish(),
                MucusCrystalCatalog.brown(), MucusCrystalCatalog.bloodied(), MucusCrystalCatalog.blackish())) {
            register(m,"mucus",e.name(),PhysicalStorageSemantics.INDIVIDUAL,true);
        }

        // Todos los abalorios, incluidos los 15 trofeos Ferae.
        for (InventoryEntry e : AccessoryCatalog.all()) {
            register(m,"accessory",e.name(),PhysicalStorageSemantics.INDIVIDUAL,false);
        }

        // Munición y proyectiles: cargadores/cajas son recipientes persistentes; proyectiles unitarios son individuales.
        for (InventoryEntry e : List.of(
                AmmunitionCatalog.pneumaticLead46Cartridge(),
                AmmunitionCatalog.autoloadingPistol45Magazine(),
                AmmunitionCatalog.submachineGun9mmMagazine(),
                AmmunitionCatalog.repeatingRifle792x57Clip(),
                AmmunitionCatalog.bifilar46Magazine(),
                AmmunitionCatalog.antiMateriel20mmCartridge(),
                AmmunitionCatalog.limeCartridgeCase())) {
            register(m,"ammunition",e.name(),PhysicalStorageSemantics.PERSISTENT_CONTAINER,false);
        }
        for (InventoryEntry e : List.of(
                AmmunitionCatalog.clusterRocket85mm(),
                AmmunitionCatalog.pebble(),
                AmmunitionCatalog.piercingArrow(),
                AmmunitionCatalog.barbedArrow(),
                AmmunitionCatalog.bladedArrow(),
                AmmunitionCatalog.tinderArrow())) {
            register(m,"ammunition",e.name(),PhysicalStorageSemantics.INDIVIDUAL,false);
        }

        for (InventoryEntry e : ThrowingWeaponCatalog.all()) {
            register(m,"throwing",e.name(),PhysicalStorageSemantics.INDIVIDUAL,false);
        }

        // accesorios desmontables de firearm con identidad física propia.
        for (InventoryEntry e : FirearmAccessoryCatalog.all()) {
            register(m,"firearmAccessory",e.name(),PhysicalStorageSemantics.INDIVIDUAL,false);
        }

        // Expansores físicos. Garment-storage/BODY son capacidades, no objetos almacenables autónomos.
        for (InventoryCompartmentType t : List.of(
                InventoryCompartmentType.LEG_POUCH, InventoryCompartmentType.BANDOLIER,
                InventoryCompartmentType.BACKPACK, InventoryCompartmentType.DORSAL_ROTOR_SYSTEM,
                InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE, InventoryCompartmentType.SADDLEBAGS_HORSE_RACING,
                InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT, InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY,
                InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN)) {
            register(m,"inventory_expander",t.label(),PhysicalStorageSemantics.SPECIALIZED_CONTAINER,false);
        }
        register(m,"inventory_expander",InventoryCompartmentType.ARROW_QUIVER.label(),
                PhysicalStorageSemantics.SPECIALIZED_CONTAINER,false);

        return Collections.unmodifiableMap(m);
    }

    private static void registerMany(Map<String,PhysicalObjectDefinition> m,String family,
                                     PhysicalStorageSemantics semantics,boolean dynamic,String... names){
        for(String name:names) register(m,family,name,semantics,dynamic);
    }

    private static void register(Map<String,PhysicalObjectDefinition> m,String family,String name,
                                 PhysicalStorageSemantics semantics,boolean dynamic){
        String key=normalizeDisplay(name);
        PhysicalObjectDefinition d=new PhysicalObjectDefinition(stableId(family,name),name,family,semantics,dynamic);
        PhysicalObjectDefinition old=m.putIfAbsent(key,d);
        if(old!=null && !old.typeId().equals(d.typeId()))
            throw new IllegalStateException("Nombre físico duplicado con identidad distinta: "+name);
    }

    private static CanonicalObjectTypeId stableId(String family,String name){
        String ascii=Normalizer.normalize(name,Normalizer.Form.NFD)
                .replaceAll("\\p{M}+","").toLowerCase(Locale.ROOT)
                .replace("×","x").replaceAll("[^a-z0-9]+","_")
                .replaceAll("^_+|_+$","");
        return CanonicalObjectTypeId.of(family+"_"+ascii);
    }

    private static String normalizeDisplay(String name){
        return Normalizer.normalize(name.trim(),Normalizer.Form.NFD)
                .replaceAll("\\p{M}+","").toLowerCase(Locale.ROOT)
                .replaceAll("\\s+"," ");
    }
}
