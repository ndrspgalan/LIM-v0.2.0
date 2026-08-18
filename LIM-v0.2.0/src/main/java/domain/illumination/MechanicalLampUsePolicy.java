package domain.illumination;

import domain.inventory.InventoryState;
import domain.inventory.QuickAccessUsePolicy;
import domain.inventory.item.misc.MechanicalLampItem;
import java.util.Objects;

public final class MechanicalLampUsePolicy {
    private MechanicalLampUsePolicy(){}
    public record Result(boolean activated,double lightSeconds,IlluminationProfile profile,String reason){}
    public static Result activate(MechanicalLampItem lamp, InventoryState inventory){
        Objects.requireNonNull(lamp); Objects.requireNonNull(inventory);
        var authorization=QuickAccessUsePolicy.authorize(lamp,inventory);
        if(!authorization.allowed()) return new Result(false,0,new IlluminationProfile(0,IlluminationColor.YELLOW,false),authorization.message());
        return new Result(true,lamp.lightSecondsPerAction(),IlluminationPolicy.mechanicalProfile(lamp,true),"");
    }
}
