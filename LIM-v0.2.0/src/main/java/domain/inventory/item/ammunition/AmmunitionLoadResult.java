package domain.inventory.item.ammunition;
public record AmmunitionLoadResult(boolean loaded,int shotsLoaded,String message){
    public static AmmunitionLoadResult loaded(int shots){return new AmmunitionLoadResult(true,shots,"Munición compatible cargada desde inventario.");}
    public static AmmunitionLoadResult rejected(String message){return new AmmunitionLoadResult(false,0,message);}
}
