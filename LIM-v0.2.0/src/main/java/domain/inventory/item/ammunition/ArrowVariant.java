package domain.inventory.item.ammunition;
public enum ArrowVariant {
    PIERCING("Perforante",60,30,0), BARBED("de Púas",30,60,0), BLADED("de Hoja",45,45,0), TINDER_UNLIT("de Yesca",30,30,0), TINDER_LIT("de Yesca Encendida",30,30,100);
    private final String label;private final double piercing,slashing,burn;
    ArrowVariant(String label,double p,double s,double b){this.label=label;this.piercing=p;this.slashing=s;this.burn=b;}
    public String label(){return label;}public double piercing(){return piercing;}public double slashing(){return slashing;}public double burn(){return burn;}
    public AmmunitionDescriptor descriptor(){return new AmmunitionDescriptor(AmmunitionFamily.ARROW,"FLECHA","Madera y acero",name(),true);}
    public static ArrowVariant fromDescriptor(AmmunitionDescriptor d){for(var v:values())if(d.variant().equalsIgnoreCase(v.name()))return v;throw new IllegalArgumentException("Variante de flecha desconocida: "+d.variant());}
}
