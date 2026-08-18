package qa.integration;



import domain.inventory.item.misc.PortableElectromagneticBatteryCharger;
import domain.inventory.item.misc.ElectromagneticPortableBatteryItem;
import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.combat.coating.MercuryCoatingService;
import domain.environment.time.EnvironmentalCycle;
import domain.inventory.item.ammunition.AmmunitionCatalog;
import domain.inventory.item.firearms.*;
import java.time.Duration;

/** Verificación ejecutable de la revisión integral . */
public final class BifilarElectromagneticVerification {
    private BifilarElectromagneticVerification() {}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        var rifle = FirearmCatalog.bifilarElectromagneticRifleV881();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.cartridgeDefinition().capacity() == 5, "Cargador bifilar de cinco cartuchos.");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(rifle.cartridgeDefinition().weightKg() - 0.300) < 0.0001, "Cargador lleno de 0,300 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(rifle.maximumShotProfile().lethality().piercing() == 90, "Máximo P90.");

        var input = new FirearmInputResolutionPolicy();
        input.resolve(FirearmInput.RELOAD_HOLD, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.handlingState() == FirearmHandlingState.ELECTROMAGNETIC_CHARGE_SELECTION, "HOLD R abre selector.");
        input.resolve(FirearmInput.LEFT_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.selectedSetting() == ElectromagneticChargeSetting.P60, "LEFT CLICK recorre umbrales.");
        input.resolve(FirearmInput.RELOAD_PRESS, rifle);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.handlingState() == FirearmHandlingState.NORMAL, "R confirma y sale.");

        var battery = new ElectromagneticPortableBatteryItem();
        org.junit.jupiter.api.Assertions.assertTrue(rifle.installBattery(battery), "Batería instalable.");
        rifle.advanceAutomaticCharge(ElectromagneticChargeSetting.P60.thermalLockSeconds(), true);
        org.junit.jupiter.api.Assertions.assertTrue(rifle.hasElectricalCharge(), "Batería y manivela completan el objetivo durante el bloqueo.");
        org.junit.jupiter.api.Assertions.assertTrue(battery.remainingEnergyJ() < ElectromagneticPortableBatteryItem.MAX_USEFUL_ENERGY_J, "La batería consume julios reales.");

        var charger = new PortableElectromagneticBatteryCharger();
        var cycle = new EnvironmentalCycle();
        var removed = rifle.removeBattery().orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(charger.begin(removed, cycle), "El cargador recibe un módulo.");
        cycle.advance(EnvironmentalCycle.DAY_DURATION);
        org.junit.jupiter.api.Assertions.assertTrue(charger.synchronize(cycle), "Un ciclo completo recarga la batería.");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(removed.chargeRatio() - 1.0) < 0.000001, "Batería recargada al 100 %.");

        var mercury = new MercuryCoatingService();
        var stone = MiscellaneousItemCatalog.mercuryStone();
        org.junit.jupiter.api.Assertions.assertTrue(mercury.rub(stone, AmmunitionCatalog.pneumaticLead46Cartridge(), 20), "El cartucho neumático admite mercurio.");
        var secondStone = MiscellaneousItemCatalog.mercuryStone();
        org.junit.jupiter.api.Assertions.assertTrue(!mercury.rub(secondStone, AmmunitionCatalog.bifilar46Magazine(), 5), "El cargador bifilar rechaza mercurio.");
    }
    
}
