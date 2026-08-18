package qa.domain;

import domain.economy.*;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.misc.CurrencyType;

import java.util.*;
import java.util.stream.Collectors;

public final class AccessoryEconomyVerification {
    private static final Set<String> PERSONAL = Set.of(
            "CUADERNO DEL DIBUJANTE","GUARDAPELO DE KIARA","PULSERA DE KENAN","CUADERNO DE KIARA"
    );
    private static final List<String> TROPHIES = List.of(
            "COLA DE RATA","PLUMA DE CUERVO","PEZUÑA DE CERDO","CERDA DE CABALLO","CAPARAZÓN DE ARMADILLO",
            "CORNAMENTA DE CIERVO","OREJA DE TORO","PIEL DE SERPIENTE","COLMILLO DE JABALÍ","OJO DE LINCE",
            "GARRAS DE ÁGUILA","CRÁNEO DE LOBO","CRIN DE LEÓN","ZARPA DE OSO","CUERNO DE RINOCERONTE"
    );
    private static final Set<String> ALLEGED_RELICS = Set.of(
            "ASTILLA CON LA QUE CLAVARON A UN MAESTRO",
            "CENIZAS DE UN DEVOTO QUE SE ARROJÓ A LA HOGUERA",
            "CERA DE UN INTELECTUAL"
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        coverage();
        personalObjects();
        trophyEconomy();
        superstitionAndRelics();
        tenderAndNoNecessities();
    }

    private static void coverage() {
        Set<String> canonical=AccessoryCatalog.all().stream().map(a->a.name()).collect(Collectors.toCollection(LinkedHashSet::new));
        org.junit.jupiter.api.Assertions.assertTrue(canonical.size()>=32,"AccessoryCatalog debe conservar 32 abalorios.");
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.all().keySet().equals(canonical)," debe cubrir exactamente AccessoryCatalog.");
        long priced=AccessoryEconomicCatalog.all().values().stream()
                .filter(v->v.status()==EconomicValuationStatus.PRICED).count();
        long personal=AccessoryEconomicCatalog.all().values().stream()
                .filter(v->v.status()==EconomicValuationStatus.PERSONAL_PROVENANCE_NOT_FOR_ORDINARY_SALE).count();
        org.junit.jupiter.api.Assertions.assertTrue(personal==4 && priced==canonical.size()-4," debe contener 28 tasaciones y 4 objetos personales no fungibles.");
        for(var v:AccessoryEconomicCatalog.all().values()) {
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale()!=null&&!v.priceRationale().isBlank(),"Justificación monetaria ausente: "+v.objectName());
            if(v.status()==EconomicValuationStatus.PRICED) org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
            else org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().isEmpty()&&!v.ordinarilySellable(),"Objeto personal no debe fingir precio: "+v.objectName());
        }
    }

    private static void personalObjects() {
        for(String name:PERSONAL) {
            var v=AccessoryEconomicCatalog.valuation(name);
            org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.PERSONAL_PROVENANCE_NOT_FOR_ORDINARY_SALE,
                    name+" debe conservar procedencia personal no fungible.");
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.PRIVATE_USE,name+" debe permanecer fuera del mercado ordinario.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.valuation("FAROLILLO LUNAR").priceValeritas().orElseThrow()==1250,
                "Farolillo Lunar es tecnología tasada, no un objeto personal sin precio.");
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.valuation("FAROLILLO LUNAR").goodType()==EconomicGoodType.PRIVATE_USE,
                "Farolillo Lunar debe ser uso privativo.");
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.valuation("FAROLILLO PORTÁTIL").goodType()==EconomicGoodType.SOCIAL_INTEREST,
                "Farolillo portátil reproducible debe ser interés social.");
    }

    private static void trophyEconomy() {
        for(String name:TROPHIES) {
            var v=AccessoryEconomicCatalog.valuation(name);
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.SOCIAL_INTEREST,"Trofeo Ferae debe ser interés social: "+name);
            org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.PRICED,"Trofeo Ferae debe tener mercado ordinario: "+name);
        }
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.valuation("CUERNO DE RINOCERONTE").priceValeritas().orElseThrow()
                > AccessoryEconomicCatalog.valuation("COLA DE RATA").priceValeritas().orElseThrow(),
                "La escasez y conservación deben separar los extremos Ferae.");
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.valuation("CERDA DE CABALLO").priceValeritas().orElseThrow()
                < AccessoryEconomicCatalog.valuation("PEZUÑA DE CERDO").priceValeritas().orElseThrow(),
                "El precio Ferae no debe ser una fórmula lineal del +CARISMA.");
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.valuation("OJO DE LINCE").priceRationale().toLowerCase().contains("resina"),
                "Ojo de Lince debe justificar su conservación especializada.");
    }

    private static void superstitionAndRelics() {
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryCatalog.inertAccessories().size()==8,"Los ocho supersticiosos ordinarios deben seguir separados.");
        for(var a:AccessoryCatalog.inertAccessories()) {
            var v=AccessoryEconomicCatalog.valuation(a.name());
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.SOCIAL_INTEREST && v.status()==EconomicValuationStatus.PRICED,
                    "Superstición ordinaria debe tener mercado social: "+a.name());
        }
        for(String name:ALLEGED_RELICS) {
            org.junit.jupiter.api.Assertions.assertTrue(AccessoryCatalog.inertAccessories().stream().noneMatch(a->a.name().equals(name)),
                    "Reliquia alegada no debe confundirse con los ocho supersticiosos ordinarios: "+name);
            var v=AccessoryEconomicCatalog.valuation(name);
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.SOCIAL_INTEREST && v.status()==EconomicValuationStatus.PRICED,
                    "Reliquia alegada debe reflejar mercado de procedencia: "+name);
            String r=v.priceRationale().toLowerCase();
            org.junit.jupiter.api.Assertions.assertTrue(r.contains("proced") || r.contains("historia") || r.contains("relato"),
                    "La justificación debe explicar procedencia/credibilidad: "+name);
        }
    }

    private static void tenderAndNoNecessities() {
        org.junit.jupiter.api.Assertions.assertTrue(AccessoryEconomicCatalog.all().values().stream().noneMatch(v->v.goodType()==EconomicGoodType.FIRST_NECESSITY),
                "Ningún abalorio  es de primera necesidad.");
        var social=AccessoryEconomicCatalog.valuation("AMULETO DE COBRE");
        org.junit.jupiter.api.Assertions.assertTrue(social.acceptedCurrencies().equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO,CurrencyType.BERYLARE)),
                "Interés social debe reutilizar la denominación legítima .");
        var privateUse=AccessoryEconomicCatalog.valuation("FAROLILLO LUNAR");
        org.junit.jupiter.api.Assertions.assertTrue(privateUse.acceptedCurrencies().contains(CurrencyType.REAL_A5),
                "Uso privativo admite las cuatro denominaciones.");
    }

    
}
