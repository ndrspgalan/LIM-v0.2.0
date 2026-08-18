package qa.architecture;

import domain.inventory.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.logistics.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**  sanea la verificación  contra la autoridad BACK_HAND introducida. */
public final class DorsalRotorThirdWeaponVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        WeaponItem rotor=MeleeWeaponCatalog.espadonDeRotor();
        EnumMap<InventoryCompartmentType,InventoryCompartment> compartments=new EnumMap<>(InventoryCompartmentType.class);
        for(InventoryCompartmentType type:InventoryCompartmentType.values())
            compartments.put(type,InventoryCompartment.empty(type,type==InventoryCompartmentType.DORSAL_ROTOR_SYSTEM));
        LogisticsState logistics=new LogisticsState(compartments,PersonalTransportState.none());
        InventoryState state=new InventoryState(EquipmentState.empty(),QuickAccessBar.empty(),logistics);
        state=RotorBackHandService.equipRetractedRotor(state,rotor);
        org.junit.jupiter.api.Assertions.assertTrue(RotorBackHandService.equippedRotor(state).orElseThrow()==rotor,"BACK_HAND conserva la misma instancia del Rotor.");
        org.junit.jupiter.api.Assertions.assertTrue(rotor.isSheathed(),"El Rotor equipado en BACK_HAND debe estar retraído.");
        state=RotorBackHandService.deploy(state);
        org.junit.jupiter.api.Assertions.assertTrue(RotorBackHandService.active(state)&&!rotor.isSheathed(),"El despliegue debe activar la misma instancia.");
        state=RotorBackHandService.retract(state);
        org.junit.jupiter.api.Assertions.assertTrue(rotor.isSheathed()&&RotorBackHandService.equippedRotor(state).orElseThrow()==rotor,"La retracción vuelve a BACK_HAND sin mover la instancia.");
        String console=Files.readString(Path.of("src/main/java/presentation/console/GameplayConsole.java"));
        org.junit.jupiter.api.Assertions.assertTrue(console.contains("RotorBackHandService.deploy"),"GameplayConsole debe usar la autoridad  de despliegue.");
        org.junit.jupiter.api.Assertions.assertTrue(console.contains("RotorBackHandService.retract"),"GameplayConsole debe usar la autoridad  de retracción.");
        org.junit.jupiter.api.Assertions.assertTrue(console.contains("case \"TAB\" -> toggleActiveWeapon()"),"TAB envaina/desenvaina en el canon +.");
    }
    
}
