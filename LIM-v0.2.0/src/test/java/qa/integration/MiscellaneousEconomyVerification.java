package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.economy.*;
import domain.inventory.catalog.PhysicalObjectCatalog;
import domain.inventory.item.misc.*;
import domain.worldmemory.WorldMemory;
import domain.worldmemory.spatial.WorldCoordinate;

import java.util.Set;
import java.util.stream.Collectors;

public final class MiscellaneousEconomyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        coverage();
        tender();
        specialValuation();
        newObjects();
        fuelFallback();
        persistentSpecialObject();
    }

    private static void coverage() {
        Set<String> misc=PhysicalObjectCatalog.all().stream()
                .filter(d->d.family().equals("misc"))
                .map(d->d.displayName()).collect(Collectors.toSet());
        org.junit.jupiter.api.Assertions.assertTrue(!misc.isEmpty(),"La familia misc debe conservar tipos canónicos.");
        org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousEconomicCatalog.all().keySet().equals(misc),
                "Todo misceláneo y sólo un misceláneo debe tener autoridad económica .");
        long priced=MiscellaneousEconomicCatalog.all().values().stream()
                .filter(v->v.status()==EconomicValuationStatus.PRICED).count();
        org.junit.jupiter.api.Assertions.assertTrue(priced==MiscellaneousEconomicCatalog.all().size()-1,"Todos los misc salvo la tasación OGC pendiente deben tener precio.");
        for(var v:MiscellaneousEconomicCatalog.all().values()) {
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>80,"Justificación económica insuficiente: "+v.objectName());
            if(v.status()==EconomicValuationStatus.PRICED) org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
        }
    }

    private static void tender() {
        org.junit.jupiter.api.Assertions.assertTrue(EconomicTenderPolicy.VALERITAS_PER_SUELDO==1000,"1 Sueldo = 1.000 V.");
        org.junit.jupiter.api.Assertions.assertTrue(EconomicTenderPolicy.VALERITAS_PER_BERYLARE==210000,"1 Berylare = 210 Sueldos.");
        org.junit.jupiter.api.Assertions.assertTrue(EconomicTenderPolicy.VALERITAS_PER_REAL_A5==420000,"1 Real A5 = 2 Berylares.");
        org.junit.jupiter.api.Assertions.assertTrue(EconomicTenderPolicy.acceptedCurrencies(EconomicGoodType.FIRST_NECESSITY)
                .equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO)),"Primera necesidad sólo V/S.");
        org.junit.jupiter.api.Assertions.assertTrue(!EconomicTenderPolicy.denominationAllowed(EconomicGoodType.FIRST_NECESSITY,CurrencyType.REAL_A5),
                "Vender fruta en Reales A5 debe ser denominación comercial ilegítima.");
        org.junit.jupiter.api.Assertions.assertTrue(EconomicTenderPolicy.acceptedCurrencies(EconomicGoodType.SOCIAL_INTEREST).size()==3,"Interés social admite tres monedas.");
        org.junit.jupiter.api.Assertions.assertTrue(EconomicTenderPolicy.acceptedCurrencies(EconomicGoodType.PRIVATE_USE).size()==4,"Uso privativo admite cuatro monedas.");
    }

    private static void specialValuation() {
        var lab=MiscellaneousEconomicCatalog.valuation("Maletín profesional de Alicia e Iván");
        org.junit.jupiter.api.Assertions.assertTrue(lab.status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING,"Maletín: tasación OGC pendiente.");
        org.junit.jupiter.api.Assertions.assertTrue(lab.priceValeritas().isEmpty()&&!lab.ordinarilySellable(),"Tasación pendiente no puede inventar precio ni venta.");
        var irnd=MiscellaneousEconomicCatalog.valuation("Frasco de I-RND");
        org.junit.jupiter.api.Assertions.assertTrue(irnd.goodType()==EconomicGoodType.PRIVATE_USE,"I-RND debe ser uso privativo.");
        org.junit.jupiter.api.Assertions.assertTrue(irnd.priceValeritas().orElseThrow()==480,"I-RND conserva tasación .");
    }

    private static void newObjects() {
        var potato=MiscellaneousItemCatalog.rawPotato();
        org.junit.jupiter.api.Assertions.assertTrue(potato.name().equals("Patata cruda")&&Math.abs(potato.weightKg()-.180)<1e-9,"Patata cruda: masa canónica.");
        org.junit.jupiter.api.Assertions.assertTrue(!FoodItem.class.isAssignableFrom(potato.getClass()),"La patata cruda no debe heredar FoodItem ni ser consumible.");
        org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousEconomicCatalog.valuation(potato.name()).goodType()==EconomicGoodType.FIRST_NECESSITY,
                "Patata = primera necesidad.");
        var converter=MiscellaneousItemCatalog.improvisedFuelConverter();
        org.junit.jupiter.api.Assertions.assertTrue(converter.name().equals("Conversor de combustible improvisado"),"Nombre canónico exacto del conversor.");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(converter.weightKg()-4.80)<1e-9,"Masa canónica del conversor.");
    }

    private static void fuelFallback() {
        int required=ImprovisedFuelConversionPolicy.wholePotatoesRequiredForOneLiter();
        org.junit.jupiter.api.Assertions.assertTrue(required==56,"10 kg / 0,180 kg requieren 56 patatas enteras.");
        var ethanol=ImprovisedFuelConversionPolicy.produceOneLiter(required,true);
        org.junit.jupiter.api.Assertions.assertTrue(ethanol.name().equals("Bidón de Etanol"),"El conversor produce el combustible alternativo: etanol.");
        boolean failed=false;
        try { ImprovisedFuelConversionPolicy.produceOneLiter(required-1,true); } catch(IllegalArgumentException e){ failed=true; }
        org.junit.jupiter.api.Assertions.assertTrue(failed,"No puede fabricarse etanol sin materia suficiente.");
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.containsName("Bidón de Queroseno Ligero"),"El queroseno comercial permanece intacto.");
    }

    private static void persistentSpecialObject() {
        var lab=MiscellaneousItemCatalog.portableLaboratory();
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.PersistentDropPolicy.requiresWorldMemoryTracking(lab),"El maletín debe conservar tracking al tirarse.");
        var memory=new WorldMemory();
        var coordinate=new WorldCoordinate(12.5,-3.0,1.2);
        var type=PhysicalObjectCatalog.typeIdOf(lab);
        memory.knowledge().rememberPersistentDroppedObject(type,coordinate);
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().persistentDroppedObjectLocation(type).orElseThrow().equals(coordinate),
                "La Memoria del Mundo debe conservar la última ubicación del maletín.");
    }

    
}
