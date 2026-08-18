package domain.inventory.equipment;

import domain.inventory.item.armor.ArmorInventoryCategory;
import domain.inventory.item.armor.ArmorLayerPosition;
import domain.inventory.item.armor.ArmorMaterialClass;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.InnerChestLayer;
import domain.inventory.item.armor.InnerLeggingsLayer;
import domain.inventory.item.armor.FeetLayer;
import domain.inventory.item.armor.HeadLayer;
import domain.inventory.item.ItemPropertyId;

import java.util.List;
import java.util.Objects;

/**
 * Guardarraíles físicos del modelo canónico por pieza, material y posición.
 * CHEST: INNER/MIDDLE/OUTER; MIDDLE admite exactamente una prenda, sea LIGHT, MEDIUM o HEAVY; BRACERS: 1; LEGGINGS: INNER BASE/COVER + MIDDLE LIGHT + OUTER MEDIUM/HEAVY;
 * FEET: INNER textil + OUTER calzado, salvo OUTER integrado; HEAD: TACTICAL/LOWER/UPPER, máximo 3 piezas y 3,5 kg cervicales.
 */
public final class ArmorEquipPolicy {
    public static final int MAX_HEAD_PIECES = 3;
    public static final double MAX_HEAD_WEIGHT_KG = 3.5;
    /** Tres subestratos INNER + MIDDLE + OUTER. */
    public static final int MAX_CHEST_PIECES = 5;
    public static final int MAX_INNER_CHEST_PIECES = 3;
    public static final int MAX_BRACER_PIECES = 1;
    /** Dos subestratos INNER + MIDDLE + OUTER. */
    public static final int MAX_LEGGING_PIECES = 4;
    public static final int MAX_INNER_LEGGING_PIECES = 2;
    public static final int MAX_FEET_PIECES = 2;

    public void validate(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        Objects.requireNonNull(equipped, "El equipamiento existente no puede ser nulo.");
        Objects.requireNonNull(candidate, "La pieza candidata no puede ser nula.");
        validateSlotCategory(candidate);
        switch (candidate.slot()) {
            case HEAD -> validateHead(equipped, candidate);
            case CHEST -> validateChest(equipped, candidate);
            case BRACERS -> validateBracers(equipped, candidate);
            case LEGGINGS -> validateLeggings(equipped, candidate);
            case FEET -> validateFeet(equipped, candidate);
            default -> throw new IllegalArgumentException("La política de armadura no admite la ranura " + candidate.slot() + ".");
        }
        validateInnerLeggingsReservation(equipped, candidate);
        validateIntegratedFootwear(equipped, candidate);
        validateIntegralSuitLayering(equipped, candidate);
        validateCervicalMass(equipped, candidate);
    }

    private static void validateHead(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        List<EquippedArmorLayer> head = equipped.stream().filter(e -> e.slot() == EquipmentSlot.HEAD).toList();
        if (head.size() >= MAX_HEAD_PIECES) throw new IllegalArgumentException("HEAD admite como máximo tres piezas funcionales.");
        if (candidate.position() != ArmorLayerPosition.UNSPECIFIED) {
            throw new IllegalArgumentException("HEAD utiliza TACTICAL/LOWER_ACCESSORY/UPPER_ACCESSORY, no INNER/MIDDLE/OUTER.");
        }
        HeadLayer layer = candidate.piece().headLayer().orElse(HeadLayer.TACTICAL);
        boolean occupied = head.stream().map(e -> e.piece().headLayer().orElse(HeadLayer.TACTICAL)).anyMatch(l -> l == layer);
        if (occupied) throw new IllegalArgumentException("La posición HEAD " + layer + " ya está ocupada.");

        boolean candidateEyewear = candidate.piece().hasActiveProperty(ItemPropertyId.EYEWEAR);
        boolean candidateBlockingTactical = layer == HeadLayer.TACTICAL && !candidateEyewear
                && candidate.piece().material().materialClass() != ArmorMaterialClass.LIGHT;
        boolean existingBlockingTactical = head.stream().map(EquippedArmorLayer::piece).anyMatch(p ->
                p.headLayer().orElse(HeadLayer.TACTICAL) == HeadLayer.TACTICAL
                        && !p.hasActiveProperty(ItemPropertyId.EYEWEAR)
                        && p.material().materialClass() != ArmorMaterialClass.LIGHT);
        boolean hasAccessory = head.stream().map(e -> e.piece().headLayer().orElse(HeadLayer.TACTICAL))
                .anyMatch(l -> l == HeadLayer.LOWER_ACCESSORY || l == HeadLayer.UPPER_ACCESSORY);
        boolean candidateAccessory = layer == HeadLayer.LOWER_ACCESSORY || layer == HeadLayer.UPPER_ACCESSORY;
        if (candidateBlockingTactical && hasAccessory) {
            throw new IllegalArgumentException("Una pieza TACTICAL HEAD MEDIUM/HEAVY impide accesorios de cabeza.");
        }
        if (candidateAccessory && existingBlockingTactical) {
            throw new IllegalArgumentException("Los accesorios HEAD no pueden coexistir con una pieza TACTICAL MEDIUM/HEAVY, salvo gafas.");
        }
    }

    private static void validateCervicalMass(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        double mass = equipped.stream().mapToDouble(e -> e.piece().headSupportedWeightKg()).sum()
                + candidate.piece().headSupportedWeightKg();
        if (mass > MAX_HEAD_WEIGHT_KG + 1.0e-9) {
            throw new IllegalArgumentException("HEAD no puede superar 3,5 kg de masa física soportada por cabeza/cuello.");
        }
    }

    private static void validateChest(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        List<EquippedArmorLayer> chest = equipped.stream().filter(e -> e.slot() == EquipmentSlot.CHEST).toList();
        if (chest.size() >= MAX_CHEST_PIECES) throw new IllegalArgumentException("CHEST admite hasta tres prendas INNER más MIDDLE y OUTER.");
        if (candidate.position() == ArmorLayerPosition.UNSPECIFIED) {
            throw new IllegalArgumentException("Una pieza CHEST debe declarar INNER, MIDDLE u OUTER.");
        }
        ArmorMaterialClass cls = candidate.piece().materialClass();
        if (candidate.position() == ArmorLayerPosition.INNER) {
            if (cls != ArmorMaterialClass.LIGHT) throw new IllegalArgumentException("INNER CHEST está reservado a prendas LIGHT.");
            InnerChestLayer layer = candidate.piece().innerChestLayer().orElse(InnerChestLayer.BASE); // compatibilidad obsoleto
            long innerCount = chest.stream().filter(e -> e.position() == ArmorLayerPosition.INNER).count();
            if (innerCount >= MAX_INNER_CHEST_PIECES) throw new IllegalArgumentException("INNER CHEST admite como máximo BASE, STRUCTURAL y COVER.");
            boolean occupied = chest.stream().filter(e -> e.position() == ArmorLayerPosition.INNER)
                    .map(e -> e.piece().innerChestLayer().orElse(InnerChestLayer.BASE)).anyMatch(existing -> existing == layer);
            if (occupied) throw new IllegalArgumentException("El subestrato INNER CHEST " + layer + " ya está ocupado.");
            return;
        }
        if (chest.stream().anyMatch(e -> e.position() == candidate.position())) {
            if (candidate.position() == ArmorLayerPosition.MIDDLE)
                throw new IllegalArgumentException("MIDDLE CHEST ya está ocupado: LIGHT, MEDIUM y HEAVY son alternativas mutuamente excluyentes.");
            throw new IllegalArgumentException("La posición " + candidate.position() + " de CHEST ya está ocupada.");
        }
        if (cls == ArmorMaterialClass.MEDIUM || cls == ArmorMaterialClass.HEAVY) {
            if (candidate.position() != ArmorLayerPosition.MIDDLE) {
                throw new IllegalArgumentException("Las piezas MEDIUM/HEAVY sólo pueden ocupar MIDDLE en CHEST.");
            }
            boolean existing = chest.stream().map(e -> e.piece().materialClass())
                    .anyMatch(c -> c == ArmorMaterialClass.MEDIUM || c == ArmorMaterialClass.HEAVY);
            if (existing) throw new IllegalArgumentException("CHEST sólo admite una pieza MEDIUM o HEAVY.");
        }
    }

    private static void validateBracers(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        long count = equipped.stream().filter(e -> e.slot() == EquipmentSlot.BRACERS).count();
        if (count >= MAX_BRACER_PIECES) throw new IllegalArgumentException("BRACERS admite como máximo una pieza.");
        if (candidate.position() != ArmorLayerPosition.UNSPECIFIED) {
            throw new IllegalArgumentException("BRACERS no utiliza estratificación.");
        }
    }

    private static void validateLeggings(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        List<EquippedArmorLayer> legs = equipped.stream().filter(e -> e.slot() == EquipmentSlot.LEGGINGS).toList();
        if (legs.size() >= MAX_LEGGING_PIECES) throw new IllegalArgumentException("LEGGINGS admite INNER BASE/COVER, MIDDLE y OUTER.");
        if (candidate.position() == ArmorLayerPosition.UNSPECIFIED) {
            throw new IllegalArgumentException("Una pieza LEGGINGS debe declarar INNER, MIDDLE u OUTER.");
        }
        ArmorMaterialClass cls = candidate.piece().materialClass();
        if (candidate.position() == ArmorLayerPosition.INNER) {
            if (cls != ArmorMaterialClass.LIGHT) throw new IllegalArgumentException("INNER LEGGINGS está reservado a prendas LIGHT.");
            InnerLeggingsLayer layer = candidate.piece().innerLeggingsLayer().orElse(InnerLeggingsLayer.BASE);
            long innerCount = legs.stream().filter(e -> e.position() == ArmorLayerPosition.INNER).count();
            long reservedElsewhere = equipped.stream().filter(e -> e.slot() != EquipmentSlot.LEGGINGS)
                    .filter(e -> e.piece().innerLeggingsLayer().isPresent()).count();
            if (innerCount + reservedElsewhere >= MAX_INNER_LEGGING_PIECES) throw new IllegalArgumentException("INNER LEGGINGS admite como máximo BASE y COVER.");
            boolean occupied = equipped.stream().map(EquippedArmorLayer::piece)
                    .flatMap(p -> p.innerLeggingsLayer().stream()).anyMatch(existing -> existing == layer);
            if (occupied) throw new IllegalArgumentException("El subestrato INNER LEGGINGS " + layer + " ya está ocupado.");
            return;
        }
        if (legs.stream().anyMatch(e -> e.position() == candidate.position())) {
            throw new IllegalArgumentException("La posición " + candidate.position() + " de LEGGINGS ya está ocupada.");
        }
        if (candidate.position() == ArmorLayerPosition.MIDDLE && cls != ArmorMaterialClass.LIGHT) {
            throw new IllegalArgumentException("MIDDLE LEGGINGS está reservado a la prenda LIGHT principal.");
        }
        if (candidate.position() == ArmorLayerPosition.OUTER
                && cls != ArmorMaterialClass.MEDIUM && cls != ArmorMaterialClass.HEAVY) {
            throw new IllegalArgumentException("OUTER LEGGINGS está reservado a una única protección MEDIUM o HEAVY.");
        }
    }



    private static void validateInnerLeggingsReservation(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        var reserved = candidate.piece().innerLeggingsLayer();
        if (reserved.isEmpty()) return;
        boolean occupied = equipped.stream().map(EquippedArmorLayer::piece)
                .flatMap(p -> p.innerLeggingsLayer().stream()).anyMatch(existing -> existing == reserved.get());
        if (occupied) throw new IllegalArgumentException("El subestrato INNER LEGGINGS " + reserved.get() + " ya está reservado por otra prenda multirregional.");
    }

    private static void validateFeet(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        List<EquippedArmorLayer> feet = equipped.stream().filter(e -> e.slot() == EquipmentSlot.FEET).toList();
        if (feet.size() >= MAX_FEET_PIECES) throw new IllegalArgumentException("FEET admite una prenda INNER y un calzado OUTER.");
        if (candidate.position() != ArmorLayerPosition.INNER && candidate.position() != ArmorLayerPosition.OUTER) {
            throw new IllegalArgumentException("Una pieza FEET debe declarar INNER u OUTER.");
        }
        FeetLayer declared = candidate.piece().feetLayer().orElse(candidate.position() == ArmorLayerPosition.INNER ? FeetLayer.INNER : FeetLayer.OUTER);
        FeetLayer requested = candidate.position() == ArmorLayerPosition.INNER ? FeetLayer.INNER : FeetLayer.OUTER;
        if (declared != requested) throw new IllegalArgumentException("La pieza " + candidate.piece().name() + " pertenece a FEET " + declared + ".");
        if (requested == FeetLayer.INNER && candidate.piece().materialClass() != ArmorMaterialClass.LIGHT) {
            throw new IllegalArgumentException("INNER FEET está reservado a prendas LIGHT.");
        }
        if (feet.stream().anyMatch(e -> e.position() == candidate.position())) {
            throw new IllegalArgumentException("La posición " + candidate.position() + " de FEET ya está ocupada.");
        }
    }

    private static void validateIntegratedFootwear(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        boolean integratedAlready = equipped.stream().anyMatch(e -> e.piece().hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR));
        boolean outerFeetAlready = equipped.stream().anyMatch(e -> e.slot() == EquipmentSlot.FEET && e.position() == ArmorLayerPosition.OUTER);
        boolean candidateIntegrated = candidate.piece().hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR);
        if (candidate.slot() == EquipmentSlot.FEET && candidate.position() == ArmorLayerPosition.OUTER && integratedAlready) {
            throw new IllegalArgumentException("El calzado integrado de una pieza equipada ya ocupa OUTER FEET.");
        }
        if (candidateIntegrated && outerFeetAlready) {
            throw new IllegalArgumentException("La pieza con calzado integrado no puede coexistir con un calzado independiente OUTER FEET.");
        }
    }


    /**
     * : un traje integral BODY envuelve todas las capas no-INNER.
     * Los dos monos especializados permiten INNER debajo; el Ingeniero además ocupa HEAD por construcción.
     */
    private static void validateIntegralSuitLayering(List<EquippedArmorLayer> equipped, EquippedArmorLayer candidate) {
        ArmorPiece candidatePiece = candidate.piece();
        boolean candidateIntegral = candidatePiece.form() == domain.inventory.item.armor.ArmorForm.INTEGRAL_SUIT;
        ArmorPiece existingIntegral = equipped.stream().map(EquippedArmorLayer::piece)
                .filter(p -> p.form() == domain.inventory.item.armor.ArmorForm.INTEGRAL_SUIT)
                .findFirst().orElse(null);

        if (candidateIntegral && existingIntegral != null) {
            throw new IllegalArgumentException("Sólo puede equiparse un traje integral a la vez.");
        }

        if (candidateIntegral) {
            for (EquippedArmorLayer layer : equipped) {
                if (!isAllowedUnderIntegral(layer)) {
                    throw new IllegalArgumentException("Un traje integral sólo admite prendas INNER debajo.");
                }
                if (candidatePiece.headCoverageRatio() > 0 && layer.slot() == EquipmentSlot.HEAD) {
                    throw new IllegalArgumentException("El módulo HEAD integrado del traje impide cualquier pieza HEAD independiente.");
                }
            }
        }

        if (existingIntegral != null) {
            if (candidate.slot() == EquipmentSlot.HEAD && existingIntegral.headCoverageRatio() > 0) {
                throw new IllegalArgumentException("El módulo HEAD integrado del traje impide cualquier pieza HEAD independiente.");
            }
            if (!isAllowedUnderIntegral(candidate)) {
                throw new IllegalArgumentException("Con un traje integral equipado sólo pueden añadirse prendas INNER.");
            }
        }
    }

    private static boolean isAllowedUnderIntegral(EquippedArmorLayer layer) {
        return switch (layer.slot()) {
            case CHEST, LEGGINGS, FEET -> layer.position() == ArmorLayerPosition.INNER;
            case HEAD, BRACERS -> false;
            default -> true;
        };
    }

    private static void validateSlotCategory(EquippedArmorLayer layer) {
        ArmorPiece armor = layer.piece();
        ArmorInventoryCategory category = armor.inventoryCategory().orElse(null);
        boolean valid = switch (layer.slot()) {
            case HEAD -> category == ArmorInventoryCategory.HEAD || (category == null && armor.headCoverageRatio() > 0 && armor.bodyCoverageRatio() <= 0);
            case CHEST -> category == ArmorInventoryCategory.CHEST || category == ArmorInventoryCategory.INTEGRAL_SUIT;
            case BRACERS -> category == ArmorInventoryCategory.BRACERS;
            case LEGGINGS -> category == ArmorInventoryCategory.LEGGINGS;
            case FEET -> category == ArmorInventoryCategory.FEET;
            default -> false;
        };
        if (!valid) throw new IllegalArgumentException("La pieza " + armor.name() + " no corresponde a " + layer.slot() + ".");
    }
}
