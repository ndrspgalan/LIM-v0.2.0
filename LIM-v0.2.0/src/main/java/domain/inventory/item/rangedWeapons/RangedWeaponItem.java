package domain.inventory.item.rangedWeapons;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.InventoryState;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.firearms.CoupDeGracePolicy;
import java.util.*;

/** Arma a distancia convencional  con manejo, cadencia, munición y desgaste propios. */
public final class RangedWeaponItem extends InventoryEntry {
    public static final double MAX_RANGE_LOSS_FRACTION = 0.15;
    private final RangedWeaponType type;
    private final double lengthMeters;
    private final int strengthRequirement;
    private final int dexterityRequirement;
    private final RangedWeaponGrip grip;
    private final double originalEffectiveRangeMeters;
    private final double recoverySeconds;
    private final RangedWeaponWearProfile wearProfile;
    private final double piercingBonus;
    private final double slashingBonus;
    private double wearFraction;
    private double nextReadyAtSeconds;

    public RangedWeaponItem(String name,String description,double weightKg,InventoryFootprint footprint,
                            RangedWeaponType type,double lengthMeters,int strengthRequirement,int dexterityRequirement,
                            RangedWeaponGrip grip,double effectiveRangeMeters,double recoverySeconds,
                            RangedWeaponWearProfile wearProfile,double piercingBonus,double slashingBonus,
                            List<ItemProperty> properties){
        super(name,description,weightKg,footprint,stats(type,lengthMeters,strengthRequirement,dexterityRequirement,grip,effectiveRangeMeters,recoverySeconds,wearProfile,piercingBonus,slashingBonus),properties);
        if(lengthMeters<=0||effectiveRangeMeters<=0||recoverySeconds<=0)throw new IllegalArgumentException("Longitud, alcance y recuperación deben ser positivos.");
        this.type=Objects.requireNonNull(type);this.lengthMeters=lengthMeters;this.strengthRequirement=strengthRequirement;this.dexterityRequirement=dexterityRequirement;
        this.grip=Objects.requireNonNull(grip);this.originalEffectiveRangeMeters=effectiveRangeMeters;this.recoverySeconds=recoverySeconds;
        this.wearProfile=Objects.requireNonNull(wearProfile);this.piercingBonus=piercingBonus;this.slashingBonus=slashingBonus;
    }
    private static List<String> stats(RangedWeaponType type,double length,int str,int dex,RangedWeaponGrip grip,double range,double recovery,RangedWeaponWearProfile wear,double p,double c){
        List<String>s=new ArrayList<>();s.add("Longitud funcional | "+length+" m");s.add("FUERZA | "+str);s.add("DESTREZA | "+dex);
        s.add("Agarre | "+(grip==RangedWeaponGrip.ONE_HANDED?"Una mano":"Dos manos"));s.add("Alcance efectivo | "+range+" m");
        s.add("Cadencia | 1A · 1 disparo cada "+recovery+" s");s.add("Desgaste | "+(wear.degrades()?"Máximo 15 % · ritmo ×"+wear.rateMultiplier():"Ninguno"));
        if(p>0||c>0)s.add("Bonificador de letalidad | +"+p+" P / +"+c+" C");
        return List.copyOf(s);
    }
    public RangedWeaponType type(){return type;} public double lengthMeters(){return lengthMeters;} public int strengthRequirement(){return strengthRequirement;}
    public int dexterityRequirement(){return dexterityRequirement;} public RangedWeaponGrip grip(){return grip;} public boolean supportsAiming(){return true;}
    public double originalEffectiveRangeMeters(){return originalEffectiveRangeMeters;} public double currentEffectiveRangeMeters(){return originalEffectiveRangeMeters*(1.0-wearFraction);}
    public double minimumEffectiveRangeMeters(){return originalEffectiveRangeMeters*(1.0-MAX_RANGE_LOSS_FRACTION);} public double wearFraction(){return wearFraction;}
    public RangedWeaponWearProfile wearProfile(){return wearProfile;} public boolean isDegraded(){return wearFraction>0;} public double recoverySeconds(){return recoverySeconds;}
    public boolean readyAt(double nowSeconds){return nowSeconds>=nextReadyAtSeconds;}
    public void restoreWearFraction(double value){if(!Double.isFinite(value)||value<0||value>MAX_RANGE_LOSS_FRACTION)throw new IllegalArgumentException("Desgaste persistido inválido.");wearFraction=value;}
        public double registerUse(){if(!wearProfile.degrades())return 0;double before=currentEffectiveRangeMeters();wearFraction=Math.min(MAX_RANGE_LOSS_FRACTION,wearFraction+0.001*wearProfile.rateMultiplier());return before-currentEffectiveRangeMeters();}
    public boolean repairWithResin(){if(!wearProfile.degrades()||!isDegraded())return false;wearFraction=0;return true;}
    public boolean accepts(AmmunitionDescriptor d){if(d==null)return false;return switch(type){case SLING -> d.family()==AmmunitionFamily.PEBBLE || isPneumaticLead46(d);case SIMPLE_RECURVE_BOW,COMPOSITE_BOW -> d.family()==AmmunitionFamily.ARROW;};}
    private boolean isPneumaticLead46(AmmunitionDescriptor d){return d.family()==AmmunitionFamily.CARTRIDGE&&d.caliber().equalsIgnoreCase(".46")&&d.material().equalsIgnoreCase("Plomo");}
    public LethalityProfile lethalityFor(AmmunitionDescriptor d){if(!accepts(d))throw new IllegalArgumentException("Munición incompatible.");
        if(type==RangedWeaponType.SLING)return d.family()==AmmunitionFamily.PEBBLE?new LethalityProfile(0,0,35):new LethalityProfile(0,0,60);
        ArrowVariant v=ArrowVariant.fromDescriptor(d);return new LethalityProfile(v.piercing()+piercingBonus,v.slashing()+slashingBonus,0);
    }
    public double burnFor(AmmunitionDescriptor d){return type==RangedWeaponType.SLING?0:ArrowVariant.fromDescriptor(d).burn();}
    public RangedWeaponShotResult fire(AmmunitionDescriptor required,InventoryState inventory,double nowSeconds){
        if(!readyAt(nowSeconds))return RangedWeaponShotResult.rejected("El arma todavía está recuperándose.");
        if(!accepts(required))return RangedWeaponShotResult.rejected("La munición no es compatible.");
        AmmunitionLoadResult load=requestAmmunitionFromInventory(required,inventory);if(!load.loaded())return RangedWeaponShotResult.rejected(load.message());
        registerUse();nextReadyAtSeconds=nowSeconds+recoverySeconds;return RangedWeaponShotResult.fired(lethalityFor(required),burnFor(required),recoverySeconds);
    }
    public AmmunitionLoadResult requestAmmunitionFromInventory(AmmunitionDescriptor required,InventoryState inventory){return new AmmunitionInventoryPolicy().consumeSingleShotForEquippedWeapon(this,required,inventory);}
    public boolean hasProperty(ItemPropertyId id){return properties().stream().anyMatch(p->p.id()==id);}
    public boolean isCoupDeGrace(boolean headImpact,double headCoveragePercent,double headPiercingProtection,AmmunitionDescriptor ammunition){
        return hasProperty(ItemPropertyId.COUP_DE_GRACE) && CoupDeGracePolicy.isCoupDeGrace(headImpact,headCoveragePercent,headPiercingProtection,lethalityFor(ammunition).piercing());
    }
}
