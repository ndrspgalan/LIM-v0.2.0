package qa.domain;

import domain.economy.*;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.CurrencyType;
import java.util.*;

/**  — cobertura económica exhaustiva de FEET sin ejecutar en el ciclo normal. */
public final class FeetArmorEconomyVerification {
    private FeetArmorEconomyVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        coverage();
        classificationAndTender();
        economics();
        layeringIdentity();
        pairAndOverlapContinuity();
        electricalContinuity();
        integratedFootwearContinuity();
    }

    private static void coverage(){
        var all=ArmorCatalog.allFeetArmor();
        org.junit.jupiter.api.Assertions.assertTrue(all.size()==18," espera 18 identidades FEET.");
        Set<String> names=new LinkedHashSet<>();
        for(var a:all){
            org.junit.jupiter.api.Assertions.assertTrue(a.inventoryCategory().orElseThrow()==ArmorInventoryCategory.FEET,"Identidad no FEET en : "+a.name());
            org.junit.jupiter.api.Assertions.assertTrue(names.add(a.name()),"Identidad FEET duplicada: "+a.name());
        }
        org.junit.jupiter.api.Assertions.assertTrue(names.equals(FeetArmorEconomicCatalog.all().keySet()),"Cobertura  debe ser 1:1 entre ArmorCatalog y economía.");
    }

    private static void classificationAndTender(){
        for(var v:FeetArmorEconomicCatalog.all().values()){
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.SOCIAL_INTEREST,"Todo FEET  debe ser SOCIAL_INTEREST: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.PRICED," no contiene piezas OGC ni provenance: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.acceptedCurrencies().equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO,CurrencyType.BERYLARE)),
                    "SOCIAL_INTEREST debe admitir valerita, sueldo y berylare: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(!v.acceptedCurrencies().contains(CurrencyType.REAL_A5),"FEET SOCIAL_INTEREST no debe admitir real A5: "+v.objectName());
        }
    }

    private static void economics(){
        org.junit.jupiter.api.Assertions.assertTrue(price("Vendas de pie V881")==6,"Precio basal de vendas inesperado.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Calcetines gruesos de trabajo V881")>price("Calcetines V881"),"Más fibra y refuerzo laboral deben elevar el coste.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Botines de cuero V881")>price("Zapatos de trabajo de cuero V881"),"Caña y superficie de cuero adicionales deben elevar el coste.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Botas cortas de campo V881")>price("Botines de cuero V881"),"Construcción de campo robusta debe superar al botín ordinario.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Botas altas de montar y campo V881")>price("Botas cortas de campo V881"),"Caña ecuestre larga debe elevar materia y trabajo.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Botas de trabajo pesado e industria V881")>price("Botas altas de montar y campo V881"),"Puntera de acero y construcción industrial deben elevar la manufactura.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Zapatos Oxford/Brogue V881")>price("Mocasines V881"),"Patronaje, hormado y acabado formal deben elevar las horas de taller.");
        for(var v:FeetArmorEconomicCatalog.all().values()){
            org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>150,"Justificación económica insuficiente: "+v.objectName());
        }
    }

    private static void layeringIdentity(){
        Set<String> inner=new LinkedHashSet<>();
        ArmorCatalog.allInnerFeetGarments().forEach(a->{
            org.junit.jupiter.api.Assertions.assertTrue(a.feetLayer().orElseThrow()==FeetLayer.INNER,"INNER FEET sin estrato correcto: "+a.name());
            inner.add(a.name());
        });
        Set<String> outer=new LinkedHashSet<>();
        ArmorCatalog.allOuterFeetGarments().forEach(a->{
            org.junit.jupiter.api.Assertions.assertTrue(a.feetLayer().orElseThrow()==FeetLayer.OUTER,"OUTER FEET sin estrato correcto: "+a.name());
            outer.add(a.name());
        });
        org.junit.jupiter.api.Assertions.assertTrue(inner.size()==7 && outer.size()==11,"Distribución canónica esperada: 7 INNER + 11 OUTER.");
        Set<String> union=new LinkedHashSet<>(inner); union.addAll(outer);
        org.junit.jupiter.api.Assertions.assertTrue(union.equals(FeetArmorEconomicCatalog.all().keySet())," debe unir exactamente INNER y OUTER FEET.");
    }

    private static void pairAndOverlapContinuity(){
        close(ArmorCatalog.leatherHighRidingBootsV881().weightKg(),1.600,"Peso canónico del par de botas altas");
        close(ArmorCatalog.leatherHeavyWorkBootsV881().weightKg(),1.800,"Peso canónico del par de botas industriales");
        close(ArmorCatalog.outerLeatherWorkShoesV881().weightKg(),0.780,"Peso canónico del par de zapatos de trabajo");
        close(ArmorCatalog.leatherHighRidingBootsV881().bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS),.12,"Las botas altas deben conservar LEGGINGS 12%");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.leatherHighRidingBootsV881().inventoryCategory().orElseThrow()==ArmorInventoryCategory.FEET,
                "Invadir LEGGINGS no convierte la bota alta en identidad LEGGINGS.");
    }

    private static void electricalContinuity(){
        EquipmentState moccasins=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.outerMoccasinsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(moccasins)==FeetElectricalContact.EARTH_COUPLED,"Mocasín sin caucho debe conservar acoplamiento a tierra.");
        EquipmentState workShoes=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.outerLeatherWorkShoesV881()));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(workShoes)==FeetElectricalContact.INSULATED,"Zapato laboral con caucho debe aislar.");
        EquipmentState industrial=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.leatherHeavyWorkBootsV881()));
        org.junit.jupiter.api.Assertions.assertTrue(FeetElectricalContactPolicy.resolve(industrial)==FeetElectricalContact.INSULATED,"Bota industrial debe aislar por caucho pese a la puntera de acero.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.leatherHeavyWorkBootsV881().materialClass()==ArmorMaterialClass.HEAVY,"Bota industrial debe conservar clase HEAVY emergente.");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.leatherHeavyWorkBootsV881().hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR),"La puntera de acero no debe convertir la bota en conductor a tierra.");
    }

    private static void integratedFootwearContinuity(){
        var historical=ArmorCatalog.historicalKnightLeggings();
        org.junit.jupiter.api.Assertions.assertTrue(historical.hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR),"Caballero histórico debe conservar sabatones integrados.");
        org.junit.jupiter.api.Assertions.assertTrue(!FeetArmorEconomicCatalog.all().containsKey(historical.name()),"La pieza LEGGINGS con sabatones no debe duplicarse económicamente en FEET.");
    }

    private static long price(String n){return FeetArmorEconomicCatalog.valuation(n).priceValeritas().orElseThrow();}
    private static void close(double a,double b,String m){org.junit.jupiter.api.Assertions.assertTrue(Math.abs(a-b)<1e-9,m+": "+a+" != "+b);}
    
}
