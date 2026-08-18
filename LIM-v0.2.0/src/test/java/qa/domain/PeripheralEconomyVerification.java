package qa.domain;

import domain.economy.*;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import domain.inventory.catalog.PhysicalObjectCatalog;
import java.util.*;
import java.util.stream.Collectors;

public final class PeripheralEconomyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        scope();
        mucus();
        ammunition();
        expanders();
        transport();
        tender();
    }

    private static void scope(){
        org.junit.jupiter.api.Assertions.assertTrue(EconomicScope.TOTAL_ECONOMIC_ENTITIES==35,"El alcance  debe conservar 35 entidades.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusEconomicCatalog.crystals().size()==5,"Cinco cristales + Lágrima dinámica = seis mucus.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.persistentNames().size()==7,"Siete recipientes/cargadores persistentes.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.unitaryNames().size()==6,"Seis proyectiles unitarios.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderEconomicCatalog.all().size()==10,"Diez expansores físicos.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportEconomicCatalog.all().size()==6,"Seis transportes personales.");
    }

    private static void mucus(){
        MucusTearItem t17=new MucusTearItem(17), t100=new MucusTearItem(100);
        org.junit.jupiter.api.Assertions.assertTrue(MucusEconomicCatalog.tearValueValeritas(t17)==68,"17mL deben valer 17 veces la tarifa por mL.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusEconomicCatalog.tearValueValeritas(t100)==400,"100mL deben valer 400 V.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusEconomicCatalog.tearValuation(t17).structuralValueValeritas()==0,
                "La Lágrima es materia, no recipiente recuperable.");
        for(var c:List.of(MucusCrystalCatalog.yellow(),MucusCrystalCatalog.greenish(),MucusCrystalCatalog.brown(),
                MucusCrystalCatalog.bloodied(),MucusCrystalCatalog.blackish())){
            var v=MucusEconomicCatalog.crystal(c.name());
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.PRIVATE_USE && v.priceValeritas().orElseThrow()>0,
                    "Cristal de mucus debe tener tasación privativa: "+c.name());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>130,"Justificación insuficiente: "+c.name());
        }
        org.junit.jupiter.api.Assertions.assertTrue(MucusEconomicCatalog.crystal("Cristal de Mucus NEGRUZCO").priceRationale().toLowerCase().contains("rareza"),
                "El negruzco se tasa por rareza material, no por Frenesí.");
    }

    private static void ammunition(){
        AmmunitionCartridge nine=AmmunitionCatalog.submachineGun9mmMagazine();
        long full=AmmunitionEconomicCatalog.current(nine).currentValueValeritas();
        org.junit.jupiter.api.Assertions.assertTrue(nine.consumeShots(20),"Debe poder consumir 20 disparos.");
        var partial=AmmunitionEconomicCatalog.current(nine);
        org.junit.jupiter.api.Assertions.assertTrue(partial.currentValueValeritas()<full,"Un cargador parcialmente vacío debe valer menos.");
        org.junit.jupiter.api.Assertions.assertTrue(partial.currentValueValeritas()>=partial.structuralValueValeritas(),
                "El cargador nunca pierde su valor estructural.");
        org.junit.jupiter.api.Assertions.assertTrue(nine.consumeShots(5),"Debe vaciarse.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.current(nine).currentValueValeritas()
                ==AmmunitionEconomicCatalog.emptyReferenceValueValeritas(nine.name()),
                "Cargador vacío = valor del continente.");

        LimeCartridgeCase lime=new LimeCartridgeCase(2);
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.current(lime).currentValueValeritas()
                ==AmmunitionEconomicCatalog.emptyReferenceValueValeritas(lime.name())
                +2*AmmunitionEconomicCatalog.unitContentValueValeritas(lime.name()),
                "Cal Viva debe tasarse estuche + cartuchos restantes.");

        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.unitary("Flecha de Yesca").priceRationale().contains("Amadou"),
                "La Flecha de Yesca no debe incluir gratis sus recursos de ignición.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.unitary("Guijarro").priceValeritas().orElseThrow()==1,
                "Guijarro debe conservar valor comercial mínimo.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.goodType("Cohete de Racimo V881 de 85 mm")==EconomicGoodType.PRIVATE_USE,
                "Cohete de racimo debe ser uso privativo.");
        org.junit.jupiter.api.Assertions.assertTrue(AmmunitionEconomicCatalog.goodType("Flecha perforante")==EconomicGoodType.SOCIAL_INTEREST,
                "Flecha ordinaria debe ser interés social.");
    }

    private static void expanders(){
        Set<InventoryCompartmentType> expected=EnumSet.of(
                InventoryCompartmentType.LEG_POUCH,InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.BACKPACK,
                InventoryCompartmentType.DORSAL_ROTOR_SYSTEM,
                InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE,InventoryCompartmentType.SADDLEBAGS_HORSE_RACING,
                InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT,InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY,
                InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN,InventoryCompartmentType.ARROW_QUIVER);
        org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderEconomicCatalog.all().keySet().equals(expected),"Deben tasarse exactamente los diez expansores.");
        for(var e:InventoryExpanderEconomicCatalog.all().entrySet()){
            org.junit.jupiter.api.Assertions.assertTrue(e.getValue().priceValeritas().orElseThrow()>0,"Expansor sin precio: "+e.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(e.getValue().priceRationale().length()>120,"Expansor sin justificación suficiente: "+e.getKey());
        }
        org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderEconomicCatalog.valuation(InventoryCompartmentType.ARROW_QUIVER).priceRationale().contains("no"),
                "El precio del carcaj debe excluir su contenido.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderEconomicCatalog.valuation(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).goodType()==EconomicGoodType.PRIVATE_USE,
                "Sistema dorsal del Rotor es uso privativo.");
    }

    private static void transport(){
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportEconomicCatalog.all().size()==PersonalTransportType.values().length,
                "Cada transporte personal debe tener tasación.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportEconomicCatalog.valuation(PersonalTransportType.HORSE_RACING).priceValeritas().orElseThrow()
                > PersonalTransportEconomicCatalog.valuation(PersonalTransportType.HORSE_LEISURE).priceValeritas().orElseThrow(),
                "Carreras debe reflejar selección de crianza.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportEconomicCatalog.valuation(PersonalTransportType.MOTORCYCLE_CARDAN_V881).priceValeritas().orElseThrow()
                > PersonalTransportEconomicCatalog.valuation(PersonalTransportType.BICYCLE_MILITARY_V881).priceValeritas().orElseThrow(),
                "La motocicleta debe reflejar mucha mayor complejidad industrial.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportEconomicCatalog.valuation(PersonalTransportType.MOTORCYCLE_CARDAN_V881).priceRationale().contains("independiente"),
                "Motocicleta no debe incorporar maletas ni combustible en su precio.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportEconomicCatalog.valuation(PersonalTransportType.HORSE_LEISURE).priceRationale().contains("no incluye"),
                "Caballo debe separar montura/equipaje/manutención.");
    }

    private static void tender(){
        org.junit.jupiter.api.Assertions.assertTrue(MucusEconomicCatalog.crystal("Cristal de Mucus AMARILLENTO").acceptedCurrencies().size()==4,
                "Mucus privativo admite cuatro monedas.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderEconomicCatalog.valuation(InventoryCompartmentType.LEG_POUCH).acceptedCurrencies().size()==3,
                "Expansor de interés social admite V/S/Berylare.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportEconomicCatalog.valuation(PersonalTransportType.MOTORCYCLE_CARDAN_V881)
                .acceptedCurrencies().contains(domain.inventory.item.misc.CurrencyType.REAL_A5),
                "Transporte privativo puede denominarse en Real A5.");
    }

    
}
