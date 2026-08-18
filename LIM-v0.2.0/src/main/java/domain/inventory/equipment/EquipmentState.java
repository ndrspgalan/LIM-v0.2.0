package domain.inventory.equipment;

import domain.character.sheet.CharacterSheet;
import domain.inventory.InventoryEntry;
import domain.inventory.item.AccessoryContext;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.rangedWeapons.RangedWeaponItem;
import domain.inventory.item.throwingWeapons.ThrowingWeaponItem;
import domain.inventory.item.firearms.FirearmItem;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.ArmorInventoryCategory;
import domain.runic.EffectImmunitySet;
import domain.runic.RunicMarkId;
import domain.runic.RunicMarkItem;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class EquipmentState {
    private final Map<EquipmentSlot, InventoryEntry> equippedItems;
    private domain.runic.RunicMarkId selectedRunicMarkOverride;
    private java.util.Set<domain.runic.RunicMarkId> awakenedRunicMarks;

    public EquipmentState(Map<EquipmentSlot, InventoryEntry> equippedItems) {
        Objects.requireNonNull(equippedItems, "El equipamiento no puede ser nulo.");
        EnumMap<EquipmentSlot, InventoryEntry> copy = new EnumMap<>(EquipmentSlot.class);
        equippedItems.forEach((slot, item) -> {
            Objects.requireNonNull(slot, "La ranura de equipamiento no puede ser nula.");
            Objects.requireNonNull(item, "El objeto equipado no puede ser nulo.");
            copy.put(slot, item);
        });
        validateSlotCompatibility(copy);
        validateLeftHandWeapon(copy);
        validateLeftHandFirearm(copy);
        validateRangedWeaponHands(copy);
        validateBackHand(copy);
        this.equippedItems = Map.copyOf(copy);
        // Compatibilidad para consumidores aislados; GameSessionState sincroniza inmediatamente el progreso canónico.
        this.awakenedRunicMarks = equippedRunicMarkIds(copy);
    }


    private static java.util.Set<domain.runic.RunicMarkId> equippedRunicMarkIds(Map<EquipmentSlot, InventoryEntry> items) {
        java.util.EnumSet<domain.runic.RunicMarkId> result = java.util.EnumSet.noneOf(domain.runic.RunicMarkId.class);
        InventoryEntry item = items.get(EquipmentSlot.RUNIC_MARK);
        if (item instanceof domain.runic.RunicMarkItem mark) result.add(mark.id());
        return result;
    }

    private static void validateSlotCompatibility(Map<EquipmentSlot, InventoryEntry> items) {
        items.forEach((slot, item) -> {
            boolean valid = switch (slot) {
                case RIGHT_HAND, LEFT_HAND -> item instanceof WeaponItem || item instanceof RangedWeaponItem || item instanceof ThrowingWeaponItem || item instanceof FirearmItem;
                case BACK_HAND -> item instanceof WeaponItem weapon && weapon.hasTrait(domain.inventory.item.WeaponTrait.DORSAL_ROTOR_COMPATIBLE);
                case ACCESSORY -> item instanceof AccessoryItem;
                case RUNIC_MARK -> item instanceof RunicMarkItem;
                case HEAD -> isArmorCategory(item, ArmorInventoryCategory.HEAD);
                case CHEST -> isArmorCategory(item, ArmorInventoryCategory.CHEST)
                        || isArmorCategory(item, ArmorInventoryCategory.INTEGRAL_SUIT);
                case BRACERS -> isArmorCategory(item, ArmorInventoryCategory.BRACERS);
                case LEGGINGS -> isArmorCategory(item, ArmorInventoryCategory.LEGGINGS);
                case FEET -> isArmorCategory(item, ArmorInventoryCategory.FEET);
            };
            if (!valid) {
                throw new IllegalArgumentException("El objeto " + item.name() + " no es compatible con la ranura " + slot.label() + ".");
            }
        });
    }

    private static void validateBackHand(Map<EquipmentSlot, InventoryEntry> items) {
        InventoryEntry back=items.get(EquipmentSlot.BACK_HAND);
        if(back==null) return;
        if(!(back instanceof WeaponItem rotor) || !rotor.hasTrait(domain.inventory.item.WeaponTrait.DORSAL_ROTOR_COMPATIBLE))
            throw new IllegalArgumentException("BACK_HAND sólo admite el Espadón de Rotor.");
        long weaponCount=java.util.stream.Stream.of(EquipmentSlot.RIGHT_HAND,EquipmentSlot.LEFT_HAND,EquipmentSlot.BACK_HAND)
                .filter(items::containsKey).count();
        if(weaponCount>3) throw new IllegalArgumentException("No pueden equiparse más de tres armas.");
    }

    /** BACK_HAND no es una tercera mano física: hereda RIGHT_HAND como mano dominante efectiva. */
    public static EquipmentSlot effectiveDominantHand(EquipmentSlot slot) {
        return slot==EquipmentSlot.BACK_HAND ? EquipmentSlot.RIGHT_HAND : slot;
    }

    private static boolean isArmorCategory(InventoryEntry item, ArmorInventoryCategory expected) {
        if (!(item instanceof ArmorPiece armor)) return false;
        return armor.inventoryCategory().map(category -> category == expected).orElseGet(() -> switch (expected) {
            case HEAD -> armor.hitLocation() == domain.inventory.item.armor.ArmorHitLocation.HEAD;
            case CHEST, BRACERS, LEGGINGS, FEET -> armor.hitLocation() == domain.inventory.item.armor.ArmorHitLocation.BODY;
            case INTEGRAL_SUIT -> false;
        });
    }



    private static void validateRangedWeaponHands(Map<EquipmentSlot, InventoryEntry> items) {
        InventoryEntry right = items.get(EquipmentSlot.RIGHT_HAND);
        InventoryEntry left = items.get(EquipmentSlot.LEFT_HAND);
        if (right instanceof RangedWeaponItem ranged && ranged.grip() == domain.inventory.item.rangedWeapons.RangedWeaponGrip.TWO_HANDED && left != null) {
            throw new IllegalArgumentException("Un arco bimanual exige la mano izquierda libre.");
        }
        if (left instanceof RangedWeaponItem ranged && ranged.grip() == domain.inventory.item.rangedWeapons.RangedWeaponGrip.TWO_HANDED) {
            throw new IllegalArgumentException("Un arco bimanual debe equiparse como arma principal en RIGHT HAND.");
        }
    }


    private static void validateLeftHandFirearm(Map<EquipmentSlot, InventoryEntry> items) {
        InventoryEntry leftHandItem = items.get(EquipmentSlot.LEFT_HAND);
        if (!(leftHandItem instanceof FirearmItem firearm)) {
            return;
        }
        if (!firearm.supportsOneHanded()) {
            throw new IllegalArgumentException("Un arma de fuego exclusivamente bimanual no puede equiparse como arma independiente en la mano izquierda.");
        }
    }

    private static void validateLeftHandWeapon(Map<EquipmentSlot, InventoryEntry> items) {
        InventoryEntry leftHandItem = items.get(EquipmentSlot.LEFT_HAND);
        if (!(leftHandItem instanceof WeaponItem weapon)) {
            return;
        }
        boolean exceptional = weapon.leftHandLimitException();
        if (!exceptional && weapon.reachMeters() > 0.5) {
            throw new IllegalArgumentException("Un arma ordinaria equipada en la mano izquierda no puede superar 0,5 m de longitud.");
        }
        if (!exceptional && weapon.weightKg() > 1.0) {
            throw new IllegalArgumentException("Un arma ordinaria equipada en la mano izquierda no puede superar 1 kg de peso.");
        }
    }


    public Map<EquipmentSlot, InventoryEntry> equippedItems() { return equippedItems; }

    public EquipmentState withItem(EquipmentSlot slot, InventoryEntry item) {
        Objects.requireNonNull(slot); Objects.requireNonNull(item);
        EnumMap<EquipmentSlot,InventoryEntry> copy=new EnumMap<>(EquipmentSlot.class); copy.putAll(equippedItems); copy.put(slot,item);
        return new EquipmentState(copy);
    }

    public EquipmentState withoutItem(EquipmentSlot slot) {
        Objects.requireNonNull(slot); EnumMap<EquipmentSlot,InventoryEntry> copy=new EnumMap<>(EquipmentSlot.class); copy.putAll(equippedItems); copy.remove(slot);
        return new EquipmentState(copy);
    }

    public static EquipmentState empty() { return new EquipmentState(Map.of()); }

    public Optional<InventoryEntry> itemAt(EquipmentSlot slot) {
        Objects.requireNonNull(slot, "La ranura no puede ser nula.");
        return Optional.ofNullable(equippedItems.get(slot));
    }

    public Optional<ArmorPiece> armorAt(EquipmentSlot slot) {
        Objects.requireNonNull(slot, "La ranura no puede ser nula.");
        return itemAt(slot).filter(ArmorPiece.class::isInstance).map(ArmorPiece.class::cast);
    }

    public double totalWeightKg() {
        return equippedItems.values().stream().mapToDouble(item -> {
            if (item instanceof WeaponItem weapon) return weapon.effectiveWeightKg();
            if (item instanceof FirearmItem firearm) return firearm.effectiveHandlingWeightKg();
            return item.weightKg();
        }).sum();
    }

    /** peso equipado contextual cuando el personaje está EMPAPADO. */
    public double totalWeightKg(boolean soaked) {
        return equippedItems.values().stream().mapToDouble(item -> {
            if (item instanceof domain.inventory.item.armor.ArmorPiece armor) return armor.effectiveWeightKg(soaked);
            if (item instanceof WeaponItem weapon) return weapon.effectiveWeightKg();
            if (item instanceof FirearmItem firearm) return firearm.effectiveHandlingWeightKg();
            return item.weightKg();
        }).sum();
    }


    public double healthRegenerationMultiplier() {
        return equippedItems.values().stream().filter(AccessoryItem.class::isInstance).map(AccessoryItem.class::cast)
                .mapToDouble(AccessoryItem::healthRegenerationMultiplier).reduce(1.0, (left, right) -> left * right);
    }

    public double healthRegenerationMultiplier(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return equippedItems.values().stream().filter(AccessoryItem.class::isInstance).map(AccessoryItem.class::cast)
                .mapToDouble(item -> item.healthRegenerationMultiplier(sheet)).reduce(1.0, (left, right) -> left * right);
    }

    public double attributeBonus(domain.character.sheet.Attribute attribute, CharacterSheet sheet) {
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return equippedItems.values().stream().filter(AccessoryItem.class::isInstance).map(AccessoryItem.class::cast)
                .mapToDouble(item -> item.attributeBonus(attribute, sheet)).sum();
    }

    public int effectiveAttributeValue(domain.character.sheet.Attribute attribute, CharacterSheet sheet) {
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        int effective = (int) Math.round(sheet.valueOf(attribute) + attributeBonus(attribute, sheet));
        if (attribute == domain.character.sheet.Attribute.DESTREZA
                && hasArmorProperty(domain.inventory.item.ItemPropertyId.BIOMECHANICAL_RIGIDITY)) {
            return Math.min(effective, 20);
        }
        return effective;
    }

    public domain.character.sheet.DamageResistanceProfile accessoryResistanceBonus(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        double bonus = equippedItems.values().stream().filter(AccessoryItem.class::isInstance)
                .map(AccessoryItem.class::cast).mapToDouble(item -> item.allResistancesBonus(sheet)).sum();
        return domain.character.sheet.DamageResistanceProfile.uniform(bonus);
    }

    public double sanityBonus(CharacterSheet sheet) {
        return sanityBonus(sheet, AccessoryContext.day());
    }

    public double sanityBonus(CharacterSheet sheet, AccessoryContext context) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        Objects.requireNonNull(context, "El contexto del abalorio no puede ser nulo.");
        return equippedItems.values().stream()
                .filter(AccessoryItem.class::isInstance)
                .map(AccessoryItem.class::cast)
                .mapToDouble(item -> item.sanityBonus(sheet, context))
                .sum();
    }

    public boolean canNavigateVeilRifts(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return equippedItems.values().stream()
                .filter(AccessoryItem.class::isInstance)
                .map(AccessoryItem.class::cast)
                .anyMatch(item -> item.enablesVeilRiftNavigation(sheet));
    }



    public java.util.List<ArmorPiece> equippedArmor() {
        return equippedItems.values().stream()
                .filter(ArmorPiece.class::isInstance)
                .map(ArmorPiece.class::cast)
                .distinct()
                .toList();
    }

    public boolean hasArmorProperty(domain.inventory.item.ItemPropertyId id) {
        Objects.requireNonNull(id, "La propiedad no puede ser nula.");
        if (id == domain.inventory.item.ItemPropertyId.GROUNDING && GroundingPolicy.groundedByFeet(this)) return true;
        return equippedArmor().stream().anyMatch(armor -> armor.hasActiveProperty(id));
    }


    /** el intercom del Aeronauta exige su casco, TOMA A TIERRA global y FEET realmente equipado. */
    public boolean terrestrialIntercomAvailable() {
        boolean intercomHead = armorAt(EquipmentSlot.HEAD)
                .map(a -> a.hasActiveProperty(domain.inventory.item.ItemPropertyId.TERRESTRIAL_INTERCOM))
                .orElse(false);
        boolean outerFeetEquipped = armorAt(EquipmentSlot.FEET)
                .map(a -> a.feetLayer().orElse(domain.inventory.item.armor.FeetLayer.OUTER) == domain.inventory.item.armor.FeetLayer.OUTER)
                .orElse(false)
                || equippedArmor().stream().anyMatch(a -> a.hasActiveProperty(domain.inventory.item.ItemPropertyId.INTEGRATED_FOOTWEAR));
        return intercomHead && outerFeetEquipped && hasArmorProperty(domain.inventory.item.ItemPropertyId.GROUNDING);
    }

    public Optional<RunicMarkItem> equippedRunicMark() {
        return itemAt(EquipmentSlot.RUNIC_MARK)
                .filter(RunicMarkItem.class::isInstance)
                .map(RunicMarkItem.class::cast);
    }

    public boolean hasAwakenedRunicMark(RunicMarkId id, CharacterSheet sheet) {
        Objects.requireNonNull(id, "La marca rúnica no puede ser nula.");
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        if (selectedRunicMarkOverride != null) return selectedRunicMarkOverride == id;
        return awakenedRunicMarks.contains(id) && equippedRunicMark().filter(mark -> mark.id() == id).isPresent();
    }

    public boolean hasAwakenedRunicMark(RunicMarkId id, CharacterSheet sheet, domain.ability.NullificationPolicy.SuppressionState suppression) {
        return domain.ability.NullificationPolicy.runicMarkUsable(suppression) && hasAwakenedRunicMark(id, sheet);
    }

    private String equippedAccessoryName() {
        return itemAt(EquipmentSlot.ACCESSORY).filter(AccessoryItem.class::isInstance)
                .map(InventoryEntry::name).orElse(null);
    }

    public double healthRegenerationMultiplier(CharacterSheet sheet, domain.ability.NullificationPolicy.SuppressionState suppression) {
        return domain.ability.NullificationPolicy.accessoryPropertyUsable(suppression, equippedAccessoryName()) ? healthRegenerationMultiplier(sheet) : 1.0;
    }

    public double attributeBonus(domain.character.sheet.Attribute attribute, CharacterSheet sheet, domain.ability.NullificationPolicy.SuppressionState suppression) {
        return domain.ability.NullificationPolicy.accessoryPropertyUsable(suppression, equippedAccessoryName()) ? attributeBonus(attribute, sheet) : 0.0;
    }

    public domain.character.sheet.DamageResistanceProfile accessoryResistanceBonus(CharacterSheet sheet, domain.ability.NullificationPolicy.SuppressionState suppression) {
        return domain.ability.NullificationPolicy.accessoryPropertyUsable(suppression, equippedAccessoryName()) ? accessoryResistanceBonus(sheet) : domain.character.sheet.DamageResistanceProfile.uniform(0.0);
    }

    public double sanityBonus(CharacterSheet sheet, AccessoryContext context, domain.ability.NullificationPolicy.SuppressionState suppression) {
        return domain.ability.NullificationPolicy.accessoryPropertyUsable(suppression, equippedAccessoryName()) ? sanityBonus(sheet, context) : 0.0;
    }

    public EffectImmunitySet effectImmunities(CharacterSheet sheet, domain.ability.NullificationPolicy.SuppressionState suppression) {
        return domain.ability.NullificationPolicy.accessoryPropertyUsable(suppression, equippedAccessoryName()) ? effectImmunities(sheet) : EffectImmunitySet.none();
    }

    /** la rama afín manifiesta la Marca; sólo completar todas las maestrías habilita sus efectos. */
    public void synchronizeRunicProgress(domain.character.CharacterClass characterClass,
                                          domain.ability.CharacterMasteryCollection masteries, CharacterSheet sheet) {
        Objects.requireNonNull(characterClass); Objects.requireNonNull(masteries); Objects.requireNonNull(sheet);
        domain.runic.RunicMarkProgressState state = new domain.runic.RunicMarkProgressPolicy().resolve(characterClass, masteries, sheet);
        java.util.EnumSet<domain.runic.RunicMarkId> next = java.util.EnumSet.noneOf(domain.runic.RunicMarkId.class);
        if (state == domain.runic.RunicMarkProgressState.AWAKENED) {
            domain.runic.RunicMarkCatalog.all().stream().filter(mark -> mark.affinity() == characterClass).findFirst().ifPresent(mark -> next.add(mark.id()));
        }
        this.awakenedRunicMarks = next;
    }

    /** Sincroniza la marca elegida en la Hoja tras [VOLUNTAD MAYOR] con todos los consumidores rúnicos. */
    public void synchronizeRunicSelection(domain.persona.PersonaProfile persona) {
        Objects.requireNonNull(persona, "La PERSONA no puede ser nula.");
        this.selectedRunicMarkOverride = persona.allRunicMarksUnlocked()
                ? persona.equippedRunicMark().orElse(null) : null;
    }

    /** tras [VOLUNTAD MAYOR], la selección de PersonaProfile sustituye la ranura física. */
    public boolean hasActiveRunicMark(RunicMarkId id, CharacterSheet sheet, domain.persona.PersonaProfile persona) {
        return domain.runic.RunicMarkActivationPolicy.isActive(id, sheet, this, persona);
    }

    public EffectImmunitySet effectImmunities(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return EffectImmunitySet.combine(equippedItems.values().stream()
                .filter(AccessoryItem.class::isInstance)
                .map(AccessoryItem.class::cast)
                .map(item -> item.activeImmunities(sheet))
                .toList());
    }

}
