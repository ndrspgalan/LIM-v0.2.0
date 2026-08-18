package qa.domain;

import domain.economy.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.CurrencyType;
import java.util.*;

public final class HeadArmorEconomyVerification {
    private static final Set<String> PRIVATE=Set.of(
            "Casco Replegable del Aeronauta","Panóptico del Ilustrado","Casco de Papel V881",
            "Casco Jet de cuero endurecido con vidrio laminado V881","Casco de Caballero",
            "Casco Barbudo V881","Casco del Cruzado V881","Casco Espartano V881"
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        coverage();
        prescriptionGlasses();
        categories();
        pricingLogic();
        tender();
    }

    private static void coverage(){
        Set<String> canonical=new LinkedHashSet<>();
        ArmorCatalog.allHeadArmor().forEach(a->canonical.add(a.name()));
        org.junit.jupiter.api.Assertions.assertTrue(canonical.size()==37,"HEAD debe conservar 37 piezas canónicas.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.all().keySet().equals(canonical),
                "La autoridad económica  debe coincidir exactamente con allHeadArmor().");
        for(var v:HeadArmorEconomicCatalog.all().values()){
            org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.PRICED,"Toda HEAD debe tener precio: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>170,"Justificación insuficiente: "+v.objectName());
        }
    }

    private static void prescriptionGlasses(){
        var g=ArmorCatalog.normalVisionGlassesV881();
        String narrative=g.narrativeDescription().toLowerCase();
        org.junit.jupiter.api.Assertions.assertTrue(narrative.contains("corrector") && narrative.contains("graduad"),
                "Gafas de visión deben explicitar que son correctoras y graduadas.");
        org.junit.jupiter.api.Assertions.assertTrue(narrative.contains("agudeza visual"),"Debe formalizarse la corrección de agudeza visual.");
        org.junit.jupiter.api.Assertions.assertTrue(g.statistics().stream().anyMatch(s->s.contains("prescripción individual") && s.contains("agudeza visual")),
                "Las estadísticas deben declarar la óptica graduada.");
        org.junit.jupiter.api.Assertions.assertTrue(g.hasActiveProperty(ItemPropertyId.EYEWEAR),"Debe conservar propiedad EYEWEAR.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation(g.name()).priceRationale().contains("prescripción individual"),
                "La tasación debe reflejar tallado óptico personalizado.");
    }

    private static void categories(){
        org.junit.jupiter.api.Assertions.assertTrue(PRIVATE.size()==8," debe tener ocho HEAD privativas.");
        for(var e:HeadArmorEconomicCatalog.all().entrySet()){
            boolean expectedPrivate=PRIVATE.contains(e.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(e.getValue().goodType()==(expectedPrivate?EconomicGoodType.PRIVATE_USE:EconomicGoodType.SOCIAL_INTEREST),
                    "Categoría económica incorrecta: "+e.getKey());
        }
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Respirador Integral V881").goodType()==EconomicGoodType.SOCIAL_INTEREST,
                "El respirador es equipo profesional de seguridad, no armamento privativo por su tipo Java.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Gafas para soldadura V881").goodType()==EconomicGoodType.SOCIAL_INTEREST,
                "Las gafas de soldadura son equipo profesional.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Gafas de visión V881").goodType()==EconomicGoodType.SOCIAL_INTEREST,
                "Las gafas correctoras son bien social/profesional.");
    }

    private static void pricingLogic(){
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Panóptico del Ilustrado").priceValeritas().orElseThrow()
                > HeadArmorEconomicCatalog.valuation("Casco Replegable del Aeronauta").priceValeritas().orElseThrow(),
                "La manufactura óptica celular del Panóptico debe superar al Aeronauta.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Casco Barbudo V881").priceValeritas().orElseThrow()
                > HeadArmorEconomicCatalog.valuation("Casco Espartano V881").priceValeritas().orElseThrow(),
                "Placas articuladas de acero deben superar a arquitectura de bronce más simple.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Gafas de visión V881").priceValeritas().orElseThrow()
                > HeadArmorEconomicCatalog.valuation("Gafas para soldadura V881").priceValeritas().orElseThrow(),
                "La prescripción/tallado individual debe añadir coste frente a protección ocular estándar.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Sombrero charro V881").priceValeritas().orElseThrow()
                > HeadArmorEconomicCatalog.valuation("Bandana V881").priceValeritas().orElseThrow(),
                "Sombrerería estructurada y ornamental debe superar un paño simple.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Casco de Papel V881").priceRationale().contains("martillado"),
                "Casco de papel debe justificar proceso multicapa y no tratarse como papel barato.");
    }

    private static void tender(){
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Bandana V881").acceptedCurrencies()
                .equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO,CurrencyType.BERYLARE)),
                "Interés social HEAD debe admitir V/S/Berylare.");
        org.junit.jupiter.api.Assertions.assertTrue(HeadArmorEconomicCatalog.valuation("Casco Barbudo V881").acceptedCurrencies()
                .contains(CurrencyType.REAL_A5),
                "HEAD privativa admite Real A5.");
    }

    
}
