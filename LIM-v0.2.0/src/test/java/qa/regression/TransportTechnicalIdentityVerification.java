package qa.regression;

import domain.inventory.logistics.*;
import java.util.*;

public final class TransportTechnicalIdentityVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(PersonalTransportType.values()).allMatch(t -> t.technicalDescription()!=null && t.technicalDescription().length()>80), "Todo transporte debe explicar técnicamente qué es y por qué se comporta así.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportType.MOTORCYCLE_CARDAN_V881.technicalDescription().contains("Zündapp KS 750"), "La Cardán debe declarar su inspiración KS 750.");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportType.BICYCLE_MILITARY_V881.technicalDescription().contains("Truppenfahrrad "), "La bicicleta militar debe declarar su inspiración .");
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportType.BICYCLE_FOLDING_V881.technicalDescription().contains("Bianchi"), "La plegable debe declarar su inspiración Bianchi.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_HORSE_RACING.label().startsWith("Alforjas"), "Caballos usan alforjas.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY.label().startsWith("Bolsas de Portaequipajes"), "Bicicleta usa bolsas de portaequipajes.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN.label().startsWith("Maletas Laterales"), "Motocicleta usa maletas laterales.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_HORSE_RACING.grid().capacity()==24 && InventoryCompartmentType.SADDLEBAGS_HORSE_RACING.maximumWeightKg().orElseThrow()==6.0, "Carrera: 24 slots / 6 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE.grid().capacity()==48 && InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE.maximumWeightKg().orElseThrow()==15.0, "Monta: 48 slots / 15 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT.grid().capacity()==162 && InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT.maximumWeightKg().orElseThrow()==30.0, "Carga: 108 slots / 30 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY.grid().capacity()==32 && InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY.maximumWeightKg().orElseThrow()==10.0, "Bolsas militares: 32 slots / 10 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN.grid().capacity()==72 && InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN.maximumWeightKg().orElseThrow()==20.0, "Maletas Cardán: 72 slots / 20 kg.");
    }
    
}
