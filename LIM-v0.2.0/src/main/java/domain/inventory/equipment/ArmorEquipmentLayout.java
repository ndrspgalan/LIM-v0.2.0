package domain.inventory.equipment;

import domain.inventory.item.armor.ArmorLayerPosition;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.BodyArmorRegion;
import domain.inventory.item.armor.BodyArmorCoverageCompositionPolicy;
import domain.inventory.item.armor.ArmorProtectionProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Layout estratificado canónico de armadura y vestimenta. */
public final class ArmorEquipmentLayout {
    private final List<EquippedArmorLayer> layers;
    private final ArmorEquipPolicy policy;

    public ArmorEquipmentLayout() {
        this(List.of(), new ArmorEquipPolicy());
    }

    private ArmorEquipmentLayout(List<EquippedArmorLayer> layers, ArmorEquipPolicy policy) {
        this.layers = List.copyOf(layers);
        this.policy = Objects.requireNonNull(policy);
    }

    public static ArmorEquipmentLayout empty() { return new ArmorEquipmentLayout(); }

    public ArmorEquipmentLayout equip(EquipmentSlot slot, ArmorLayerPosition position, ArmorPiece piece) {
        EquippedArmorLayer candidate = new EquippedArmorLayer(slot, position, piece);
        policy.validate(layers, candidate);
        ArrayList<EquippedArmorLayer> updated = new ArrayList<>(layers);
        updated.add(candidate);
        return new ArmorEquipmentLayout(updated, policy);
    }

    public List<EquippedArmorLayer> layers() { return layers; }
    public List<EquippedArmorLayer> layersAt(EquipmentSlot slot) {
        Objects.requireNonNull(slot);
        return layers.stream().filter(e -> e.slot() == slot)
                .sorted(domain.combat.ArmorLayerOrderPolicy.outerToInner()).toList();
    }
    public ArmorEquipmentLayout unequip(ArmorPiece piece) {
        Objects.requireNonNull(piece);
        ArrayList<EquippedArmorLayer> updated = new ArrayList<>(layers);
        if (!updated.removeIf(layer -> layer.piece() == piece)) return this;
        return new ArmorEquipmentLayout(updated, policy);
    }
    public List<ArmorPiece> piecesAt(EquipmentSlot slot) {
        Objects.requireNonNull(slot);
        return layers.stream().filter(e -> e.slot() == slot).map(EquippedArmorLayer::piece).toList();
    }
    public double headWeightKg() {
        return layers.stream().mapToDouble(e -> e.piece().headSupportedWeightKg()).sum();
    }
    public List<ArmorPiece> bodyPieces() {
        return layers.stream().map(EquippedArmorLayer::piece).filter(p -> p.bodyCoverageRatio() > 0).toList();
    }
    public double effectiveCoverage(BodyArmorRegion region) {
        return BodyArmorCoverageCompositionPolicy.effectiveCoverage(bodyPieces(), region);
    }
    public ArmorProtectionProfile effectiveProtection(BodyArmorRegion region) {
        return BodyArmorCoverageCompositionPolicy.effectiveProtection(bodyPieces(), region);
    }
    public double totalBodyCoverage() {
        return BodyArmorCoverageCompositionPolicy.totalBodyCoverage(bodyPieces());
    }
}
