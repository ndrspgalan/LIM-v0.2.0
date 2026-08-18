package domain.inventory.item.armor;

import domain.combat.DamageType;
import java.util.Objects;

/** Materiales defensivos canónicos  y su política de desgaste contundente. */
public enum ArmorMaterial {
    CLOTH("Tela", new ArmorProtectionProfile(2, 5, 2), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.LIGHT),
    HARDENED_LEATHER("Cuero endurecido", new ArmorProtectionProfile(25, 45, 35), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.MEDIUM),
    WOOD("Madera", new ArmorProtectionProfile(25, 20, 15), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.MEDIUM),
    BRONZE("Bronce", new ArmorProtectionProfile(70, 60, 60), ArmorWearPolicy.DEGRADABLE, true, 1.0, ArmorMaterialClass.MEDIUM),
    STEEL("Acero de placas", new ArmorProtectionProfile(75, 100, 75), ArmorWearPolicy.DEGRADABLE, true, 1.0, ArmorMaterialClass.HEAVY),
    EBONY_WOOD("Madera de ébano", new ArmorProtectionProfile(75, 55, 60), ArmorWearPolicy.NON_DEGRADING, false, 0.0, ArmorMaterialClass.HEAVY),
    ELECTROMECHANICAL_COMPOSITE("Compuesto Electromecánico", new ArmorProtectionProfile(75, 85, 80), ArmorWearPolicy.DEGRADABLE, true, 2.0, ArmorMaterialClass.HEAVY),
    /** el perfil defensivo del papel se define por construcción. */
    PAPER("Papel", new ArmorProtectionProfile(1, 3, 1), ArmorWearPolicy.DEGRADABLE, false, 2.0, ArmorMaterialClass.MEDIUM),
    LAMINATED_GLASS("Vidrio laminado", new ArmorProtectionProfile(40, 85, 35), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.MEDIUM),
    MINERAL_MULTILAYER_FABRIC("Tejido mineral multicapa", new ArmorProtectionProfile(38, 85, 38), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.MEDIUM),
    RUBBER("Caucho", new ArmorProtectionProfile(10, 25, 20), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.MEDIUM),
    VULCANIZED_RUBBER("Caucho vulcanizado", new ArmorProtectionProfile(15, 30, 15), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.MEDIUM),
    DIELECTRIC_CLOTH("Tela dieléctrica", new ArmorProtectionProfile(5, 15, 5), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.LIGHT),
    MINERALIZED_WOOD("Madera mineralizada", new ArmorProtectionProfile(25, 20, 15), ArmorWearPolicy.DEGRADABLE, false, 1.0, ArmorMaterialClass.MEDIUM),
    MINERALIZED_EBONY("Ébano mineralizado", new ArmorProtectionProfile(75, 55, 60), ArmorWearPolicy.NON_DEGRADING, false, 0.0, ArmorMaterialClass.HEAVY),
    TUNGSTEN_PLATES_2_5_MM("Placas de wolframio de 2,5 mm", new ArmorProtectionProfile(20, 45, 25), ArmorWearPolicy.DEGRADABLE, true, 0.5, ArmorMaterialClass.HEAVY),
    TUNGSTEN("Wolframio", new ArmorProtectionProfile(15, 15, 5), ArmorWearPolicy.DEGRADABLE, true, 0.5, ArmorMaterialClass.HEAVY);

    private final String label;
    private final ArmorProtectionProfile canonicalProtection;
    private final ArmorWearPolicy wearPolicy;
    private final boolean metallic;
    private final double bluntWearMultiplier;
    private final ArmorMaterialClass materialClass;

    ArmorMaterial(String label, ArmorProtectionProfile canonicalProtection,
                  ArmorWearPolicy wearPolicy, boolean metallic, double bluntWearMultiplier, ArmorMaterialClass materialClass) {
        this.label = Objects.requireNonNull(label);
        this.canonicalProtection = Objects.requireNonNull(canonicalProtection);
        this.wearPolicy = Objects.requireNonNull(wearPolicy);
        this.metallic = metallic;
        if (bluntWearMultiplier < 0) throw new IllegalArgumentException("El multiplicador de desgaste no puede ser negativo.");
        this.bluntWearMultiplier = bluntWearMultiplier;
        this.materialClass = Objects.requireNonNull(materialClass, "La clase material no puede ser nula.");
    }

    public String label() { return label; }
    public ArmorProtectionProfile canonicalProtection() { return canonicalProtection; }
    public ArmorWearPolicy wearPolicy() { return wearPolicy; }
    public boolean isMetallic() { return metallic; }
    public double wearMultiplier() { return bluntWearMultiplier; }
    /** Alias obsoleto: desde  el multiplicador se aplica a P/C/B, no sólo a B. */
    public double bluntWearMultiplier() { return wearMultiplier(); }
    public ArmorMaterialClass materialClass() { return materialClass; }

    /** Multiplicador material frente a daño no convencional. No altera la fórmula de desgaste contundente. */
    public double incomingDamageMultiplier(DamageType type) {
        Objects.requireNonNull(type, "El tipo de daño no puede ser nulo.");
        return switch (this) {
            case WOOD, EBONY_WOOD, MINERALIZED_EBONY -> type == DamageType.BURN ? 2.0 : 1.0;
            case BRONZE -> switch (type) {
                case POISON -> 0.75;
                case ELECTRICITY -> 2.0;
                default -> 1.0;
            };
            case PAPER -> switch (type) {
                case POISON -> 0.75;
                case BURN -> 2.0;
                default -> 1.0;
            };
            case STEEL -> type == DamageType.ELECTRICITY ? 2.0 : 1.0;
            default -> 1.0;
        };
    }
}
