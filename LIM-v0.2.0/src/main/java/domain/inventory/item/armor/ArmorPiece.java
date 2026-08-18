package domain.inventory.item.armor;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ArmorPiece extends InventoryEntry {
    private static final double EPSILON = 1.0e-9;

    private final ArmorHitLocation hitLocation;
    private final double bodyCoverageRatio;
    private final double headCoverageRatio;
    private java.util.Map<BodyArmorRegion, Double> bodyRegionCoverage;
    private final ArmorProtectionProfile protection;
    private final ArmorMaterial material;
    private final Set<ArmorMaterial> materials;
    private final ArmorForm form;
    private final ArmorInventoryCategory inventoryCategory;
    private final ArmorBlockCapability blockCapability;
    private double currentPiercingProtection;
    private double currentSlashingProtection;
    private double currentBluntProtection;
    private final java.util.EnumSet<ArmorPersistentCondition> persistentConditions = java.util.EnumSet.noneOf(ArmorPersistentCondition.class);
    private boolean structurallyFailed;
    /** Masa realmente soportada por cabeza/cuello. HEAD usa su masa total; un traje integral puede declarar una fracción cervical. */
    private double headSupportedWeightKg;
    /** subestrato opcional dentro de INNER CHEST para prendas LIGHT. */
    private InnerChestLayer innerChestLayer;
    /** subestrato opcional dentro de INNER LEGGINGS para prendas LIGHT. */
    private InnerLeggingsLayer innerLeggingsLayer;
    /** estrato físico de FEET para distinguir prenda interior de calzado exterior. */
    private FeetLayer feetLayer;
    /** posición funcional exterior de HEAD. */
    private HeadLayer headLayer;

    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorHitLocation hitLocation, double coverageRatio, ArmorProtectionProfile protection,
                      List<String> statistics) {
        this(name, narrativeDescription, weightKg, footprint, hitLocation, coverageRatio, protection,
                ArmorMaterial.HARDENED_LEATHER, ArmorForm.STANDARD, statistics, List.of(), ArmorBlockCapability.NONE);
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorHitLocation hitLocation, double coverageRatio, ArmorProtectionProfile protection,
                      List<String> statistics, List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint, hitLocation, coverageRatio, protection,
                ArmorMaterial.HARDENED_LEATHER, ArmorForm.STANDARD, statistics, properties, ArmorBlockCapability.NONE);
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorHitLocation hitLocation, double coverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, ArmorForm form, List<String> statistics, List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint, hitLocation, coverageRatio, protection,
                material, form, statistics, properties, ArmorBlockCapability.NONE);
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorHitLocation hitLocation, double coverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, ArmorForm form, List<String> statistics, List<ItemProperty> properties,
                      ArmorBlockCapability blockCapability) {
        this(name, narrativeDescription, weightKg, footprint,
                hitLocation == ArmorHitLocation.BODY ? coverageRatio : 0.0,
                hitLocation == ArmorHitLocation.HEAD ? coverageRatio : 0.0,
                hitLocation, null, protection, material, form, statistics, properties, blockCapability);
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorHitLocation hitLocation, double coverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, Set<ArmorMaterial> materials, ArmorForm form, List<String> statistics,
                      List<ItemProperty> properties, ArmorBlockCapability blockCapability) {
        this(name, narrativeDescription, weightKg, footprint,
                hitLocation == ArmorHitLocation.BODY ? coverageRatio : 0.0,
                hitLocation == ArmorHitLocation.HEAD ? coverageRatio : 0.0,
                hitLocation, null, protection, material, materials, form, statistics, properties, blockCapability);
    }

    /** Constructor específico para una armadura integral que cubre simultáneamente cabeza y cuerpo. */
    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      double bodyCoverageRatio, double headCoverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, ArmorForm form, List<String> statistics, List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint, bodyCoverageRatio, headCoverageRatio,
                ArmorHitLocation.BODY, ArmorInventoryCategory.INTEGRAL_SUIT, protection, material, form,
                statistics, properties, ArmorBlockCapability.NONE);
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      double bodyCoverageRatio, double headCoverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, Set<ArmorMaterial> materials, ArmorForm form, List<String> statistics,
                      List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint, bodyCoverageRatio, headCoverageRatio,
                ArmorHitLocation.BODY, ArmorInventoryCategory.INTEGRAL_SUIT, protection, material, materials, form,
                statistics, properties, ArmorBlockCapability.NONE);
    }


    /** variante con footprint XYZ explícito conservando categoría de equipamiento. */
    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorInventoryCategory inventoryCategory, ArmorHitLocation hitLocation,
                      double coverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, ArmorForm form, List<String> statistics,
                      List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint,
                hitLocation == ArmorHitLocation.BODY ? coverageRatio : 0.0,
                hitLocation == ArmorHitLocation.HEAD ? coverageRatio : 0.0,
                hitLocation, Objects.requireNonNull(inventoryCategory), protection, material, form,
                statistics, properties, ArmorBlockCapability.NONE);
        if (inventoryCategory == ArmorInventoryCategory.INTEGRAL_SUIT) {
            throw new IllegalArgumentException("El traje integral debe construirse con sus dos coberturas explícitas.");
        }
        if (inventoryCategory == ArmorInventoryCategory.HEAD && hitLocation != ArmorHitLocation.HEAD) {
            throw new IllegalArgumentException("La armadura de cabeza debe proteger la cabeza.");
        }
        if (inventoryCategory != ArmorInventoryCategory.HEAD && hitLocation != ArmorHitLocation.BODY) {
            throw new IllegalArgumentException("Coraza, brazales, polainas y calzado deben proteger el cuerpo.");
        }
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg,
                      ArmorInventoryCategory inventoryCategory, ArmorHitLocation hitLocation,
                      double coverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, ArmorForm form, List<String> statistics,
                      List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, inventoryCategory, hitLocation, coverageRatio, protection,
                material, form, statistics, properties, ArmorBlockCapability.NONE);
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg,
                      ArmorInventoryCategory inventoryCategory, ArmorHitLocation hitLocation,
                      double coverageRatio, ArmorProtectionProfile protection,
                      ArmorMaterial material, ArmorForm form, List<String> statistics,
                      List<ItemProperty> properties, ArmorBlockCapability blockCapability) {
        this(name, narrativeDescription, weightKg,
                Objects.requireNonNull(inventoryCategory, "La categoría de armadura no puede ser nula.").footprint(),
                hitLocation == ArmorHitLocation.BODY ? coverageRatio : 0.0,
                hitLocation == ArmorHitLocation.HEAD ? coverageRatio : 0.0,
                hitLocation, inventoryCategory, protection, material, form, statistics, properties, blockCapability);
        if (inventoryCategory == ArmorInventoryCategory.INTEGRAL_SUIT) {
            throw new IllegalArgumentException("El traje integral debe construirse con sus dos coberturas explícitas.");
        }
        if (inventoryCategory == ArmorInventoryCategory.HEAD && hitLocation != ArmorHitLocation.HEAD) {
            throw new IllegalArgumentException("La armadura de cabeza debe proteger la cabeza.");
        }
        if (inventoryCategory != ArmorInventoryCategory.HEAD && hitLocation != ArmorHitLocation.BODY) {
            throw new IllegalArgumentException("Coraza, brazales, polainas y calzado deben proteger el cuerpo.");
        }
    }

    /** Pieza corporal capaz de repartir su cobertura entre varias subregiones sin ocupar sus ranuras. */
    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorInventoryCategory inventoryCategory,
                      java.util.Map<BodyArmorRegion, Double> bodyRegionCoverage,
                      ArmorProtectionProfile protection, ArmorMaterial material, Set<ArmorMaterial> materials,
                      ArmorForm form, List<String> statistics, List<ItemProperty> properties) {
        this(name, narrativeDescription, weightKg, footprint, inventoryCategory, bodyRegionCoverage,
                protection, material, materials, form, statistics, properties, ArmorBlockCapability.NONE);
    }

    public ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                      ArmorInventoryCategory inventoryCategory,
                      java.util.Map<BodyArmorRegion, Double> bodyRegionCoverage,
                      ArmorProtectionProfile protection, ArmorMaterial material, Set<ArmorMaterial> materials,
                      ArmorForm form, List<String> statistics, List<ItemProperty> properties,
                      ArmorBlockCapability blockCapability) {
        this(name, narrativeDescription, weightKg, footprint,
                validatedBodyCoverage(bodyRegionCoverage), 0.0, ArmorHitLocation.BODY,
                Objects.requireNonNull(inventoryCategory), protection, material, materials, form,
                statistics, properties, blockCapability);
        if (inventoryCategory == ArmorInventoryCategory.HEAD || inventoryCategory == ArmorInventoryCategory.INTEGRAL_SUIT) {
            throw new IllegalArgumentException("La cobertura corporal regional exige una pieza corporal ordinaria.");
        }
        validateBodyRegions(bodyRegionCoverage);
        this.bodyRegionCoverage = java.util.Map.copyOf(bodyRegionCoverage);
    }

    private ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                       double bodyCoverageRatio, double headCoverageRatio, ArmorHitLocation hitLocation,
                       ArmorInventoryCategory inventoryCategory, ArmorProtectionProfile protection,
                       ArmorMaterial material, ArmorForm form, List<String> statistics,
                       List<ItemProperty> properties, ArmorBlockCapability blockCapability) {
        this(name, narrativeDescription, weightKg, footprint, bodyCoverageRatio, headCoverageRatio,
                hitLocation, inventoryCategory, protection, material, Set.of(material), form, statistics,
                properties, blockCapability);
    }

    private ArmorPiece(String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
                       double bodyCoverageRatio, double headCoverageRatio, ArmorHitLocation hitLocation,
                       ArmorInventoryCategory inventoryCategory, ArmorProtectionProfile protection,
                       ArmorMaterial material, Set<ArmorMaterial> materials, ArmorForm form, List<String> statistics,
                       List<ItemProperty> properties, ArmorBlockCapability blockCapability) {
        super(name, narrativeDescription, weightKg, footprint, statistics, properties);
        validateCoverage(bodyCoverageRatio, "cuerpo");
        validateCoverage(headCoverageRatio, "cabeza");
        if (bodyCoverageRatio <= EPSILON && headCoverageRatio <= EPSILON) {
            throw new IllegalArgumentException("La armadura debe cubrir al menos una zona.");
        }
        this.bodyCoverageRatio = bodyCoverageRatio;
        this.headCoverageRatio = headCoverageRatio;
        this.bodyRegionCoverage = defaultBodyRegionCoverage(inventoryCategory, bodyCoverageRatio, headCoverageRatio);
        this.hitLocation = Objects.requireNonNull(hitLocation, "La zona principal no puede ser nula.");
        this.inventoryCategory = inventoryCategory;
        this.protection = Objects.requireNonNull(protection, "La protección no puede ser nula.");
        this.material = Objects.requireNonNull(material, "El material principal no puede ser nulo.");
        Objects.requireNonNull(materials, "Los materiales no pueden ser nulos.");
        if (materials.isEmpty() || materials.stream().anyMatch(Objects::isNull) || !materials.contains(material)) {
            throw new IllegalArgumentException("Los materiales deben incluir el material principal y no contener valores nulos.");
        }
        this.materials = Set.copyOf(materials);
        this.form = Objects.requireNonNull(form, "La forma de armadura no puede ser nula.");
        this.blockCapability = Objects.requireNonNull(blockCapability, "La capacidad de bloqueo no puede ser nula.");
        this.currentPiercingProtection = protection.piercing();
        this.currentSlashingProtection = protection.slashing();
        this.currentBluntProtection = protection.blunt();
        this.headSupportedWeightKg = headCoverageRatio > EPSILON && bodyCoverageRatio <= EPSILON ? weightKg : 0.0;
    }

    public java.util.Optional<ArmorInventoryCategory> inventoryCategory() {
        return java.util.Optional.ofNullable(inventoryCategory);
    }

    public ArmorHitLocation hitLocation() { return hitLocation; }
    public double bodyCoverageRatio() { return bodyCoverageRatio; }
    public double headCoverageRatio() { return headCoverageRatio; }
    public double bodyRegionCoverageRatio(BodyArmorRegion region) {
        return bodyRegionCoverage.getOrDefault(Objects.requireNonNull(region), 0.0);
    }
    public java.util.Map<BodyArmorRegion, Double> bodyRegionCoverage() { return bodyRegionCoverage; }
    public double coverageRatio(ArmorHitLocation location) {
        return location == ArmorHitLocation.HEAD ? headCoverageRatio : bodyCoverageRatio;
    }
    /** Cobertura global que esta pieza aporta a una hitbox bélica . */
    public double combatCoverageRatio(domain.combat.ArmorCombatHitbox hitbox) {
        Objects.requireNonNull(hitbox, "La hitbox bélica no puede ser nula.");
        if (hitbox.isHead()) return headCoverageRatio;
        return hitbox.bodyRegion().map(this::bodyRegionCoverageRatio).orElse(0.0);
    }
    public boolean protects(domain.combat.ArmorCombatHitbox hitbox) { return combatCoverageRatio(hitbox) > EPSILON; }
    public boolean protects(ArmorHitLocation location) { return coverageRatio(location) > EPSILON; }
    public ArmorProtectionProfile protection() { return protection; }
    public ArmorMaterial material() { return material; }
    public Set<ArmorMaterial> materials() { return materials; }
    /** clase derivada de los materiales reales; nunca del nombre de un conjunto. */
    public ArmorMaterialClass materialClass() { return ArmorMaterialClassificationPolicy.classify(materials); }
    public ArmorRegion armorRegion() { return headCoverageRatio > EPSILON && bodyCoverageRatio <= EPSILON ? ArmorRegion.HEAD : ArmorRegion.BODY; }
    public boolean containsMaterial(ArmorMaterial candidate) { return materials.contains(Objects.requireNonNull(candidate)); }
    public ArmorForm form() { return form; }
    public ArmorBlockCapability blockCapability() { return blockCapability; }
    public double headSupportedWeightKg() { return headSupportedWeightKg; }
    public java.util.Optional<InnerChestLayer> innerChestLayer() { return java.util.Optional.ofNullable(innerChestLayer); }
    public ArmorPiece withInnerChestLayer(InnerChestLayer layer) {
        this.innerChestLayer = Objects.requireNonNull(layer, "El subestrato INNER CHEST no puede ser nulo.");
        return this;
    }
    public java.util.Optional<InnerLeggingsLayer> innerLeggingsLayer() { return java.util.Optional.ofNullable(innerLeggingsLayer); }
    public ArmorPiece withInnerLeggingsLayer(InnerLeggingsLayer layer) {
        this.innerLeggingsLayer = Objects.requireNonNull(layer, "El subestrato INNER LEGGINGS no puede ser nulo.");
        return this;
    }
    public java.util.Optional<HeadLayer> headLayer() { return java.util.Optional.ofNullable(headLayer); }
    public ArmorPiece withHeadLayer(HeadLayer layer) {
        this.headLayer = Objects.requireNonNull(layer, "La posición HEAD no puede ser nula.");
        return this;
    }
    public java.util.Optional<FeetLayer> feetLayer() { return java.util.Optional.ofNullable(feetLayer); }
    public ArmorPiece withFeetLayer(FeetLayer layer) {
        this.feetLayer = Objects.requireNonNull(layer, "El estrato FEET no puede ser nulo.");
        return this;
    }
    /** Constructor fluido para piezas integrales cuya masa cervical no coincide con la masa total. */
    public ArmorPiece withHeadSupportedWeightKg(double kilograms) {
        if (!Double.isFinite(kilograms) || kilograms < 0) throw new IllegalArgumentException("La masa cervical debe ser finita y no negativa.");
        this.headSupportedWeightKg = kilograms;
        return this;
    }
    public boolean supportsImprovisedBlock() { return blockCapability == ArmorBlockCapability.IMPROVISED_LEFT_BRACER; }
    public boolean hasProperty(ItemPropertyId id) {
        Objects.requireNonNull(id);
        boolean declared = properties().stream().anyMatch(p -> p.id() == id);
        boolean intrinsic = materials.stream().anyMatch(m -> ArmorMaterialPropertyPolicy.has(m, id));
        if (id == ItemPropertyId.FLAMMABLE && declaredProperty(ItemPropertyId.MINERALIZED_TUNGSTEN_ENCASEMENT)) return false;
        if (id == ItemPropertyId.INSULATING && isWet() && !declaredProperty(ItemPropertyId.LACQUERED)) return false;
        return declared || intrinsic;
    }
    private boolean declaredProperty(ItemPropertyId id) { return properties().stream().anyMatch(p -> p.id() == id); }
    public boolean hasActiveProperty(ItemPropertyId id) { return !isDepleted() && !structurallyFailed && hasProperty(id); }
    public boolean isWet() { return persistentConditions.contains(ArmorPersistentCondition.WET); }
    public boolean structurallyFailed() { return structurallyFailed; }
    public java.util.Set<ArmorPersistentCondition> persistentConditions() { return java.util.Set.copyOf(persistentConditions); }
    @Override public double weightKg() { return isWet() && containsMaterial(ArmorMaterial.PAPER) ? super.weightKg() * 2.5 : super.weightKg(); }
    /** Masa contextual de la pieza bajo EMPAPADO; no altera la masa seca almacenada. */
    public double effectiveWeightKg(boolean soaked) { return SoakedEquipmentWeightPolicy.effectiveWeightKg(this, soaked); }

    /** Papel seco + EMPAPADO: el barniz evita el fallo estructural, pero no FRÁGIL ni su desgaste x2. */
    public SoakedArmorResult exposeToSoaked() {
        if (!containsMaterial(ArmorMaterial.PAPER)) return SoakedArmorResult.NOT_PAPER;
        if (isWet()) return SoakedArmorResult.ALREADY_WET;
        if (!declaredProperty(ItemPropertyId.VARNISHED)) {
            structurallyFailed = true;
            currentPiercingProtection = 0.0;
            currentSlashingProtection = 0.0;
            currentBluntProtection = 0.0;
            return SoakedArmorResult.STRUCTURAL_FAILURE;
        }
        persistentConditions.add(ArmorPersistentCondition.WET);
        return SoakedArmorResult.BECAME_WET;
    }

    /** La Caja del Artesano reacondiciona el papel y elimina WET. */
    public boolean clearWetCondition() { return persistentConditions.remove(ArmorPersistentCondition.WET); }
    public boolean isDegraded() {
        return currentPiercingProtection + EPSILON < protection.piercing()
                || currentSlashingProtection + EPSILON < protection.slashing()
                || currentBluntProtection + EPSILON < protection.blunt();
    }
    public boolean needsMaintenance() { return isDegraded() || isWet(); }

    /** Perfil contextual . C/B no cambian por WET. */
    public ArmorProtectionProfile currentProtection(domain.combat.ai.observation.AttackSourceType sourceType) {
        ArmorProtectionProfile base = currentProtection();
        if (!isWet() || !containsMaterial(ArmorMaterial.PAPER)) return base;
        double p = switch (Objects.requireNonNull(sourceType)) {
            case RANGED_PROJECTILE -> base.piercing() * 0.35;
            case FIREARM_PROJECTILE -> base.piercing() * 0.65;
            default -> base.piercing();
        };
        return new ArmorProtectionProfile(p, base.slashing(), base.blunt());
    }

    public boolean inhibitsHeadBluntMultiplier() {
        return headCoverageRatio >= 1.0 - EPSILON && hasProperty(ItemPropertyId.MATERIAL_SYNERGY);
    }
    public ArmorProtectionProfile currentProtection() {
        return new ArmorProtectionProfile(currentPiercingProtection, currentSlashingProtection, currentBluntProtection);
    }
    public ArmorProtectionProfile globalProtectionContribution() {
        return currentProtection().scaledBy(bodyCoverageRatio);
    }
    public ArmorProtectionProfile globalProtectionContribution(ArmorHitLocation location) {
        return currentProtection().scaledBy(coverageRatio(location));
    }
    public double currentPiercingProtection() { return currentPiercingProtection; }
    public double currentSlashingProtection() { return currentSlashingProtection; }
    public double currentBluntProtection() { return currentBluntProtection; }
    /** Una pieza sólo está completamente agotada cuando sus tres perfiles han llegado a cero. */
    public boolean isDepleted() {
        return currentPiercingProtection <= EPSILON
                && currentSlashingProtection <= EPSILON
                && currentBluntProtection <= EPSILON;
    }
    public void restoreCurrentProtection(double piercing,double slashing,double blunt){if(piercing<0||slashing<0||blunt<0)throw new IllegalArgumentException("Protección persistida inválida.");currentPiercingProtection=piercing;currentSlashingProtection=slashing;currentBluntProtection=blunt;}

    public void restoreProtectionFully() {
        currentPiercingProtection = protection.piercing();
        currentSlashingProtection = protection.slashing();
        currentBluntProtection = protection.blunt();
    }
    /** Compatibilidad : restaura únicamente el canal B cuando un consumidor obsoleto lo solicita. */
    public void restoreBluntProtectionFully() { currentBluntProtection = protection.blunt(); }

    /**
     * : degrada únicamente los perfiles estrictamente sobrepasados por la letalidad
     * que alcanza esta capa. La cobertura determina cuánto daño global intercepta la pieza,
     * pero un impacto que alcanza físicamente la pieza consume el desgaste nominal completo.
     */
    public domain.combat.ArmorProfileWearResult applyProfileWear(
            double nominalWear, double incomingPiercing, double incomingSlashing, double incomingBlunt) {
        return applyProfileWear(nominalWear, incomingPiercing, incomingSlashing, incomingBlunt, currentProtection());
    }

    public domain.combat.ArmorProfileWearResult applyProfileWear(
            double nominalWear, double incomingPiercing, double incomingSlashing, double incomingBlunt,
            ArmorProtectionProfile effectiveThreshold) {
        Objects.requireNonNull(effectiveThreshold, "El umbral defensivo efectivo no puede ser nulo.");
        if (!Double.isFinite(nominalWear) || nominalWear < 0) {
            throw new IllegalArgumentException("El desgaste nominal debe ser finito y no negativo.");
        }
        if (nominalWear == 0 || !material.wearPolicy().permitsWear() || isDepleted()) {
            return new domain.combat.ArmorProfileWearResult(0, 0, 0);
        }
        double multiplier = isWet() && containsMaterial(ArmorMaterial.PAPER) ? 1.0 : material.wearMultiplier();
        double requested = nominalWear * multiplier;
        double p = wearChannel(incomingPiercing, effectiveThreshold.piercing(), currentPiercingProtection, requested, 0);
        double c = wearChannel(incomingSlashing, effectiveThreshold.slashing(), currentSlashingProtection, requested, 1);
        double b = wearChannel(incomingBlunt, effectiveThreshold.blunt(), currentBluntProtection, requested, 2);
        return new domain.combat.ArmorProfileWearResult(p, c, b);
    }

    private double wearChannel(double incoming, double threshold, double current, double requested, int channel) {
        // Igualdad no perfora ni desgasta:  exige letalidad estrictamente superior.
        if (current <= EPSILON || threshold <= EPSILON || incoming <= threshold + EPSILON) return 0.0;
        double applied = Math.min(current, requested);
        double next = current - applied;
        if (next <= EPSILON) next = 0.0;
        switch (channel) {
            case 0 -> currentPiercingProtection = next;
            case 1 -> currentSlashingProtection = next;
            case 2 -> currentBluntProtection = next;
            default -> throw new IllegalArgumentException("Canal de desgaste desconocido.");
        }
        return applied;
    }

    /** API obsoleto: desgaste puramente contundente directo. */
    public double applyBluntWear(double nominalWear) { return applyBluntWear(nominalWear, hitLocation); }

    /** API obsoleto conservada para corrosión/verificaciones antiguas. */
    public double applyBluntWear(double nominalWear, ArmorHitLocation location) {
        if (!Double.isFinite(nominalWear) || nominalWear < 0) throw new IllegalArgumentException("El desgaste nominal no puede ser negativo.");
        if (!material.wearPolicy().permitsWear() || nominalWear == 0 || currentBluntProtection <= EPSILON) return 0;
        double multiplier = isWet() && containsMaterial(ArmorMaterial.PAPER) ? 1.0 : material.wearMultiplier();
        double requested = nominalWear * coverageRatio(location) * multiplier;
        double applied = Math.min(currentBluntProtection, requested);
        currentBluntProtection -= applied;
        if (currentBluntProtection <= EPSILON) currentBluntProtection = 0;
        return applied;
    }

    /**
     * : degradación química CORROSIVO. No utiliza la política de desgaste convencional del material:
     * representa pérdida directa de integridad contundente por el agente químico sobre la hitbox alcanzada.
     */
    public double applyCorrosiveBluntLoss(double points) {
        if (!Double.isFinite(points) || points < 0) {
            throw new IllegalArgumentException("La pérdida corrosiva debe ser finita y no negativa.");
        }
        if (points == 0 || isDepleted()) return 0;
        double applied = Math.min(currentBluntProtection, points);
        currentBluntProtection -= applied;
        if (currentBluntProtection <= EPSILON) currentBluntProtection = 0;
        return applied;
    }

    private static java.util.Map<BodyArmorRegion, Double> defaultBodyRegionCoverage(
            ArmorInventoryCategory category, double bodyCoverage, double headCoverage) {
        if (headCoverage > EPSILON && bodyCoverage <= EPSILON) return java.util.Map.of();
        if (category == null) return java.util.Map.of();
        if (category == ArmorInventoryCategory.INTEGRAL_SUIT) {
            return java.util.Map.of(
                    BodyArmorRegion.CHEST, BodyArmorRegion.CHEST.maximumCoverageRatio(),
                    BodyArmorRegion.BRACERS, BodyArmorRegion.BRACERS.maximumCoverageRatio(),
                    BodyArmorRegion.LEGGINGS, BodyArmorRegion.LEGGINGS.maximumCoverageRatio(),
                    BodyArmorRegion.FEET, BodyArmorRegion.FEET.maximumCoverageRatio());
        }
        if (bodyCoverage <= EPSILON) return java.util.Map.of();
        BodyArmorRegion region = switch (category) {
            case CHEST -> BodyArmorRegion.CHEST;
            case BRACERS -> BodyArmorRegion.BRACERS;
            case LEGGINGS -> BodyArmorRegion.LEGGINGS;
            case FEET -> BodyArmorRegion.FEET;
            default -> null;
        };
        if (region == null) return java.util.Map.of();
        return java.util.Map.of(region, bodyCoverage);
    }

    private static double validatedBodyCoverage(java.util.Map<BodyArmorRegion, Double> regions) {
        validateBodyRegions(regions);
        return regions.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    private static void validateBodyRegions(java.util.Map<BodyArmorRegion, Double> regions) {
        Objects.requireNonNull(regions, "Las coberturas regionales no pueden ser nulas.");
        for (var entry : regions.entrySet()) {
            Objects.requireNonNull(entry.getKey());
            double ratio = Objects.requireNonNull(entry.getValue());
            if (!Double.isFinite(ratio) || ratio < 0 || ratio > entry.getKey().maximumCoverageRatio() + EPSILON) {
                throw new IllegalArgumentException("La cobertura de " + entry.getKey().label()
                        + " no puede superar " + Math.round(entry.getKey().maximumCoverageRatio() * 100) + "%.");
            }
        }
    }

    private static void validateCoverage(double ratio, String zone) {
        if (ratio < 0 || ratio > 1) {
            throw new IllegalArgumentException("La cobertura de " + zone + " debe estar entre 0 y 1.");
        }
    }
}
