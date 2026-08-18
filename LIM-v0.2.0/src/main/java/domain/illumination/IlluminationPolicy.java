package domain.illumination;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.misc.MechanicalLampItem;
import java.util.Objects;

/** fuente única de verdad para alcance, color y activación de iluminación portátil. */
public final class IlluminationPolicy {
    private IlluminationPolicy(){}
    public static IlluminationProfile accessoryProfile(EquipmentState equipment){
        Objects.requireNonNull(equipment);
        var item=equipment.itemAt(EquipmentSlot.ACCESSORY);
        if(item.isEmpty()) return new IlluminationProfile(0,IlluminationColor.WARM_ORANGE,false);
        return switch(item.get().name()){
            case "FAROLILLO PORTÁTIL" -> new IlluminationProfile(2.0,IlluminationColor.WARM_ORANGE,true);
            case "FAROLILLO LUNAR" -> new IlluminationProfile(2.0,IlluminationColor.CYAN_BLUE,true);
            default -> new IlluminationProfile(0,IlluminationColor.WARM_ORANGE,false);
        };
    }
    public static IlluminationProfile mechanicalProfile(MechanicalLampItem lamp, boolean actionActive){
        Objects.requireNonNull(lamp);
        return new IlluminationProfile(lamp.illuminationMeters(),IlluminationColor.YELLOW,actionActive);
    }
}
