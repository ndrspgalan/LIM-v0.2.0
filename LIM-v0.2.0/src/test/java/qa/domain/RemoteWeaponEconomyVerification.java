package qa.domain;

import domain.economy.*;
import domain.inventory.item.firearms.*;
import domain.inventory.item.rangedWeapons.*;
import domain.inventory.item.throwingWeapons.*;
import domain.inventory.item.misc.CurrencyType;
import java.util.*;

public final class RemoteWeaponEconomyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        coverage();
        throwingEconomy();
        rangedEconomy();
        firearmEconomy();
        independentConsumablesAndAccessories();
        tender();
    }

    private static void coverage(){
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.throwingWeapons().size()==4,"Deben tasarse cuatro arrojadizas.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.rangedWeapons().size()==3,"Deben tasarse tres armas a distancia.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.firearms().size()==9,"Deben tasarse nueve firearms.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.all().size()==16," debe contener 16 armas.");
        for(var v:RemoteWeaponEconomicCatalog.all().values()){
            org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.PRICED,"Toda arma  debe estar tasada: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.PRIVATE_USE,"Toda arma  es de uso privativo: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>180,"Justificación insuficiente: "+v.objectName());
        }
    }

    private static void throwingEconomy(){
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Cuchillo Arrojadizo V881").priceValeritas().orElseThrow()
                < RemoteWeaponEconomicCatalog.valuation("Cápsula de Gas Amonio V881").priceValeritas().orElseThrow(),
                "La pieza de acero seriable debe costar menos que la unidad química especializada.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Granada de Huevo con Fósforo y Azufre V881")
                .priceRationale().toLowerCase().contains("reactiv"),
                "La granada de huevo debe justificarse por preparación química real.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Cuchillo Arrojadizo V881")
                .priceRationale().toLowerCase().contains("recuperable"),
                "El cuchillo debe explicitar su naturaleza durable/recuperable.");
    }

    private static void rangedEconomy(){
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Honda").priceValeritas().orElseThrow()==12,
                "Honda: tasación de manufactura mínima.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Arco Compuesto").priceValeritas().orElseThrow()
                > RemoteWeaponEconomicCatalog.valuation("Arco Simple Recurvo").priceValeritas().orElseThrow(),
                "Materiales heterogéneos y curado deben encarecer el compuesto.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Arco Compuesto").priceRationale().contains("cuerno"),
                "El compuesto debe justificar materiales reales, no alcance.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Arco Simple Recurvo").priceRationale().contains("flechas no"),
                "El arco debe tasarse sin munición.");
    }

    private static void firearmEconomy(){
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Fusil Bifilar Electromagnético V881").priceValeritas().orElseThrow()
                > RemoteWeaponEconomicCatalog.valuation("Fusil de Repetición V881").priceValeritas().orElseThrow(),
                "La arquitectura electromagnética debe reflejar más infraestructura que un fusil mecánico.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Cañón Antimaterial V881").priceValeritas().orElseThrow()
                > RemoteWeaponEconomicCatalog.valuation("Subfusil Automático V881").priceValeritas().orElseThrow(),
                "Gran calibre y mecanizado industrial deben separar antimaterial y subfusil.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Pistola Autocargadora V881").priceValeritas().orElseThrow()
                < RemoteWeaponEconomicCatalog.valuation("Subfusil Automático V881").priceValeritas().orElseThrow(),
                "La plataforma automática larga debe superar a la pistola seriada.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Lanza-Arcos Electrodinámico V881").priceRationale().contains("Batería"),
                "Lanza-Arcos debe excluir expresamente la batería independiente.");
        org.junit.jupiter.api.Assertions.assertTrue(RemoteWeaponEconomicCatalog.valuation("Rociador de Cal Viva V881").priceRationale().contains("cartuchos"),
                "Rociador debe excluir su consumible.");
    }

    private static void independentConsumablesAndAccessories(){
        for(var v:RemoteWeaponEconomicCatalog.firearms().values()){
            String r=v.priceRationale().toLowerCase();
            org.junit.jupiter.api.Assertions.assertTrue(!r.contains("precio por daño") && !r.contains("dps"),
                    "La potencia mecánica no puede ser criterio monetario: "+v.objectName());
        }
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.fullReferenceValueValeritas("Cargador de 9 mm V881")>0,
                "La munición conserva autoridad económica independiente.");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmAccessoryEconomicCatalog.all().size()==5,
                "Los cinco firearm accessories permanecen tasados fuera del arma.");
        org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousEconomicCatalog.valuation("Batería Portátil Electromagnética V881").priceValeritas().orElseThrow()>0,
                "La batería permanece como misc independiente.");
    }

    private static void tender(){
        for(var v:RemoteWeaponEconomicCatalog.all().values())
            org.junit.jupiter.api.Assertions.assertTrue(v.acceptedCurrencies().equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO,
                            CurrencyType.BERYLARE,CurrencyType.REAL_A5)),
                    "Uso privativo debe admitir las cuatro denominaciones: "+v.objectName());
    }

    
}
