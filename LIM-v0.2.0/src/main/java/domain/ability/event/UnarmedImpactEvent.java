package domain.ability.event;

import domain.inventory.item.armor.ArmorMaterial;

public final class UnarmedImpactEvent implements MasteryEvent {
 private final int vitality; private final int adaptability; private final ArmorMaterial contactedMaterial; private final boolean intersticeTarget;
 private int perforating; private int cutting; private int blunt; private int electricity; private int burning; private int curse; private double bluntMultiplier=1.0;
 public UnarmedImpactEvent(int vitality,int adaptability,ArmorMaterial contactedMaterial,boolean intersticeTarget,int perforating,int cutting,int blunt){this.vitality=vitality;this.adaptability=adaptability;this.contactedMaterial=contactedMaterial==null?ArmorMaterial.CLOTH:contactedMaterial;this.intersticeTarget=intersticeTarget;this.perforating=perforating;this.cutting=cutting;this.blunt=blunt;}
 /** Forma compacta: true representa acero; false, material no apto. */
 public UnarmedImpactEvent(int vitality,int adaptability,boolean touchingSteel,boolean intersticeTarget,int perforating,int cutting,int blunt){this(vitality,adaptability,touchingSteel?ArmorMaterial.STEEL:ArmorMaterial.CLOTH,intersticeTarget,perforating,cutting,blunt);}
 public int vitality(){return vitality;} public int adaptability(){return adaptability;} public ArmorMaterial contactedMaterial(){return contactedMaterial;} public boolean touchingSteel(){return contactedMaterial==ArmorMaterial.STEEL;} public boolean intersticeTarget(){return intersticeTarget;}
 public int perforating(){return perforating;} public int cutting(){return cutting;} public int blunt(){return (int)Math.round(blunt*bluntMultiplier);} public int electricity(){return electricity;} public int burning(){return burning;} public int curse(){return curse;}
 public void multiplyBlunt(double value){if(!Double.isFinite(value)||value<0)throw new IllegalArgumentException("Multiplicador inválido.");bluntMultiplier*=value;}
 public void addElectricity(int value){electricity+=Math.max(0,value);} public void addBurning(int value){burning+=Math.max(0,value);}
 public void convertPhysicalToCurse(){int total=perforating+cutting+blunt();perforating=cutting=blunt=0;bluntMultiplier=1.0;curse+=total;}
}
