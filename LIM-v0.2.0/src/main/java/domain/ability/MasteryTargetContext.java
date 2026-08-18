package domain.ability;

import domain.character.Gender;
import domain.social.RelationshipType;
import domain.status.VitalResourceState;

import java.util.Objects;
import java.util.Optional;

/** Blanco fijado y sus datos relevantes para técnicas activas. */
public final class MasteryTargetContext {
    private final String id;
    private final Gender gender;
    private final int strength;
    private final int vitality;
    private int endurance;
    private final VitalResourceState resources;
    private RelationshipType relationship;
    private final boolean locked;
    private final double distanceMeters;
    private final boolean healthRegenerationInhibited;
    private boolean allied;
    private boolean attackingActorMelee;
    private boolean helicalReleaseActive;
    private double staminaRegenPerSecond;
    private String equippedAccessoryName;
    private NullificationDeliveryPolicy.Delivery nullificationDelivery;
    private NullificationPolicy.SuppressionState accessorySuppression = NullificationPolicy.SuppressionState.none();

    public MasteryTargetContext(String id, Gender gender, int strength, int vitality,
                                VitalResourceState resources, RelationshipType relationship,
                                boolean locked, double distanceMeters, boolean healthRegenerationInhibited) {
        this.id = Objects.requireNonNull(id); this.gender = Objects.requireNonNull(gender);
        this.strength = strength; this.vitality = vitality; this.endurance = 1; this.resources = Objects.requireNonNull(resources);
        this.relationship = Objects.requireNonNull(relationship); this.locked = locked;
        if (!Double.isFinite(distanceMeters) || distanceMeters < 0) throw new IllegalArgumentException("Distancia inválida.");
        this.distanceMeters = distanceMeters; this.healthRegenerationInhibited = healthRegenerationInhibited;
    }
    public String id(){return id;} public Gender gender(){return gender;} public int strength(){return strength;}
    public int vitality(){return vitality;} public int endurance(){return endurance;} public MasteryTargetContext endurance(int value){if(value<1)throw new IllegalArgumentException("AGUANTE inválido.");endurance=value;return this;} public VitalResourceState resources(){return resources;}
    public RelationshipType relationship(){return relationship;} public void setRelationship(RelationshipType value){relationship=Objects.requireNonNull(value);}
    public boolean locked(){return locked;} public double distanceMeters(){return distanceMeters;}
    public boolean healthRegenerationInhibited(){return healthRegenerationInhibited;}
    public boolean allied(){return allied;} public void setAllied(boolean value){allied=value;}
    public boolean attackingActorMelee(){return attackingActorMelee;} public MasteryTargetContext attackingActorMelee(boolean value){attackingActorMelee=value;return this;}
    public boolean helicalReleaseActive(){return helicalReleaseActive;} public MasteryTargetContext helicalReleaseActive(boolean value){helicalReleaseActive=value;return this;}
    public double staminaRegenPerSecond(){return staminaRegenPerSecond;} public MasteryTargetContext staminaRegenPerSecond(double value){if(!Double.isFinite(value)||value<0)throw new IllegalArgumentException("PA REGEN inválido.");staminaRegenPerSecond=value;return this;}
    public String equippedAccessoryName(){return equippedAccessoryName;} public MasteryTargetContext equippedAccessoryName(String value){equippedAccessoryName=value==null?null:value.trim();return this;}
    public NullificationDeliveryPolicy.Delivery nullificationDelivery(){return nullificationDelivery;} public MasteryTargetContext nullificationDelivery(NullificationDeliveryPolicy.Delivery value){nullificationDelivery=value;return this;}
    public NullificationPolicy.SuppressionState accessorySuppression(){return accessorySuppression;} public void accessorySuppression(NullificationPolicy.SuppressionState value){accessorySuppression=Objects.requireNonNull(value);}
    public Optional<Double> missingHealth(){return Optional.of(resources.maximumHealth()-resources.currentHealth());}
}
