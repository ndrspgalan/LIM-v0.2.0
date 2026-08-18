package domain.inventory.item.firearms;

import domain.combat.ArmorCoverageResolver;
import domain.combat.CorrosiveWearPolicy;
import domain.combat.DamageType;
import domain.combat.ElementalHealthRegenerationPolicy;
import domain.combat.HostileEncounterState;
import domain.combat.NonConventionalDamageResolver;
import domain.combat.NonConventionalImpactResult;
import domain.environment.EnvironmentalAdversity;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.ammunition.LimeCartridgeCase;
import domain.inventory.item.armor.ArmorHitLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Rociador de Cal Viva V881. Arma química de contacto y negación temporal del terreno.
 * Un cartucho completo contiene 3 L y se abstrae como 56 ticks de 0,5 s durante 28 s.
 */
public final class LimeSprayerItem extends FirearmItem {
    public static final double RANGE_METERS = 2.5;
    public static final double CAPACITY_LITERS = 3.0;
    public static final double FULL_SPRAY_SECONDS = 28.0;
    public static final double HIT_INTERVAL_SECONDS = 0.5;
    public static final int BURN_DAMAGE = 67;
    public static final int POISON_DAMAGE = 100;
    public static final double CONTAMINATION_SECONDS = 30.0;
    public static final double BASE_EMPTY_WEIGHT_KG = 1.35;
    public static final double FULL_CARTRIDGE_WEIGHT_KG = LimeCartridgeCase.FULL_CARTRIDGE_WEIGHT_KG;

    private final NonConventionalDamageResolver damageResolver = new NonConventionalDamageResolver();
    private final ArmorCoverageResolver coverageResolver = new ArmorCoverageResolver();

    public LimeSprayerItem(String narrativeDescription) {
        super(
                "Rociador de Cal Viva V881",
                narrativeDescription,
                BASE_EMPTY_WEIGHT_KG,
                0.48,
                0.28,
                RANGE_METERS,
                "Cal Viva V881",
                new FirearmCartridge(
                        "Cartucho de Cal Viva V881",
                        "Cal Viva V881",
                        "Agente de Cal Viva V881",
                        "Cartucho de 3 L para rociado presurizado",
                        LimeCartridgeCase.SPRAY_TICKS_PER_CARTRIDGE,
                        FULL_CARTRIDGE_WEIGHT_KG
                ),
                new LethalityProfile(0, 0, 0),
                0.0,
                List.of(FireMode.AUTO_A),
                true,
                false,
                Set.<WeaponTrait>of()
        );
    }

    /** Masa dinámica: cuerpo + accesorios + fracción del cartucho de 3 L todavía disponible. */
    @Override
    public double weightKg() {
        double fraction = ammunitionRemaining() / (double) cartridgeDefinition().capacity();
        return super.weightKg() + FULL_CARTRIDGE_WEIGHT_KG * fraction;
    }

    public double capacityLiters() { return CAPACITY_LITERS; }
    public double fullSpraySeconds() { return FULL_SPRAY_SECONDS; }
    public double hitIntervalSeconds() { return HIT_INTERVAL_SECONDS; }
    public int burnDamage() { return BURN_DAMAGE; }
    public int poisonDamage() { return POISON_DAMAGE; }
    public double contaminationSeconds() { return CONTAMINATION_SECONDS; }
    @Override
    public boolean supportsAiming() { return false; }

    public boolean corrosivePropertyPresent() { return true; }
    public boolean requiresAssistedOneHandedForOperation() { return true; }
    public boolean operationalForSpraying() { return assistedOneHandedActive(); }

    @Override
    protected boolean canConsumeShot() {
        return operationalForSpraying() && super.canConsumeShot();
    }

    /** una sola exposición extensa; no duplica daño, lo reparte 9 % HEAD / 91 % BODY. */
    public DistributedLimeSprayerImpactResult resolveAreaHit(
            EquipmentState equipment, double burnResistancePercent, double poisonResistancePercent, boolean soaked,
            ElementalHealthRegenerationPolicy regenerationPolicy, HostileEncounterState encounter) {
        var burnSplit = domain.combat.AreaBodyDistributionPolicy.split(BURN_DAMAGE);
        var poisonSplit = domain.combat.AreaBodyDistributionPolicy.split(POISON_DAMAGE);
        LimeSprayerImpactResult head = resolveHitWithRaw(ArmorHitLocation.HEAD, burnSplit.head(), poisonSplit.head(), equipment, burnResistancePercent, poisonResistancePercent, soaked, regenerationPolicy, encounter);
        LimeSprayerImpactResult body = resolveHitWithRaw(ArmorHitLocation.BODY, burnSplit.body(), poisonSplit.body(), equipment, burnResistancePercent, poisonResistancePercent, soaked, regenerationPolicy, encounter);
        return new DistributedLimeSprayerImpactResult(head, body);
    }

    public LimeSprayerImpactResult resolveHit(
            ArmorHitLocation hitbox,
            EquipmentState equipment,
            double burnResistancePercent,
            double poisonResistancePercent,
            boolean soaked,
            ElementalHealthRegenerationPolicy regenerationPolicy,
            HostileEncounterState encounter
    ) {
        return resolveHitWithRaw(hitbox, BURN_DAMAGE, POISON_DAMAGE, equipment, burnResistancePercent, poisonResistancePercent, soaked, regenerationPolicy, encounter);
    }

    private LimeSprayerImpactResult resolveHitWithRaw(ArmorHitLocation hitbox, double rawBurn, double rawPoison,
            EquipmentState equipment, double burnResistancePercent, double poisonResistancePercent, boolean soaked,
            ElementalHealthRegenerationPolicy regenerationPolicy, HostileEncounterState encounter) {
        NonConventionalImpactResult burn = damageResolver.resolve(
                DamageType.BURN, rawBurn, hitbox, equipment, burnResistancePercent, false);
        NonConventionalImpactResult poison = damageResolver.resolve(
                DamageType.POISON, rawPoison, hitbox, equipment, poisonResistancePercent, false);

        regenerationPolicy.registerDirectDamage(DamageType.BURN, burn.netDamage(), encounter);
        regenerationPolicy.registerDirectDamage(DamageType.POISON, poison.netDamage(), encounter);

        double corrosiveLoss = soaked ? 2.0 : 1.0;
        List<String> corroded = new ArrayList<>();
        coverageResolver.applicableArmor(hitbox, equipment).forEach(piece -> {
            if (piece.currentBluntProtection() > 0 && new CorrosiveWearPolicy().apply(piece, corrosiveLoss) > 0) {
                corroded.add(piece.name());
            }
        });
        return new LimeSprayerImpactResult(burn, poison, corroded, corrosiveLoss);
    }

    public LimeContaminatedSurface contaminateSurface() {
        return new LimeContaminatedSurface(
                CONTAMINATION_SECONDS,
                Set.of(EnvironmentalAdversity.VIRULENT_TOXICITY, EnvironmentalAdversity.SUFFOCATING_HEAT)
        );
    }

    public List<String> environmentalStateLabels() {
        return List.of("Toxicidad Virulenta", "Quemadura Asfixiante");
    }

    public String pressurePrinciple() {
        return "Intercambio de aire exterior activado por resorte; parámetros constructivos: CONFIDENCIAL.";
    }

    public String confidentialCalibration() {
        return "CONFIDENCIAL — masa del agente, presurización, caudal, dispersión y parámetros fisicoquímicos constructivos.";
    }

    @Override
    public String destabilizingTechniqueDescription() {
        return "Golpe desestabilizador mediante patada frontal.";
    }
}
