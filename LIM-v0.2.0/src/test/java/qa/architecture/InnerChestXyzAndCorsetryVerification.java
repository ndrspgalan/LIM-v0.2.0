package qa.architecture;

import domain.inventory.item.armor.*;
import domain.inventory.logistics.ArmorPhysicalDimensionsCatalog;
import domain.inventory.logistics.InventoryVolumeProjectionPolicy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class InnerChestXyzAndCorsetryVerification {
    private InnerChestXyzAndCorsetryVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        var garments=ArmorCatalog.allInnerChestGarments();
        org.junit.jupiter.api.Assertions.assertTrue(garments.size()==15,"INNER CHEST debe conservar 15 piezas canónicas.");
        org.junit.jupiter.api.Assertions.assertTrue(ArmorPhysicalDimensionsCatalog.innerChestProfileCount()==15,
                "Cada pieza INNER CHEST debe declarar XYZ.");

        for(ArmorPiece piece:garments){
            var dims=ArmorPhysicalDimensionsCatalog.innerChestDimensionsFor(piece.name());
            var expected=InventoryVolumeProjectionPolicy.footprint(dims);
            org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().equals(expected),
                    "El footprint debe derivarse de XYZ: "+piece.name());
        }

        assertFootprint(ArmorCatalog.innerUndershirt(),2,2);
        assertFootprint(ArmorCatalog.innerShirt(),2,2);
        assertFootprint(ArmorCatalog.innerWorkShirt(),3,2);
        assertFootprint(ArmorCatalog.innerModularShirtV881(),2,2);
        assertFootprint(ArmorCatalog.innerBlouse(),2,2);
        assertFootprint(ArmorCatalog.innerRegionalBlouse(),3,2);
        assertFootprint(ArmorCatalog.innerChemise(),3,2);
        assertFootprint(ArmorCatalog.innerGomlek(),3,2);
        assertFootprint(ArmorCatalog.innerCamisoleV881(),2,2);
        assertFootprint(ArmorCatalog.innerChemisette(),2,1);
        assertFootprint(ArmorCatalog.innerDickey(),2,1);
        assertFootprint(ArmorCatalog.innerCorset(),3,4);
        assertFootprint(ArmorCatalog.innerMaleCorset(),3,4);
        assertFootprint(ArmorCatalog.innerCorsetCover(),2,2);
        assertFootprint(ArmorCatalog.innerCombinationV881(),3,3);

        StructuredCorsetryProfile corsetry=StructuredCorsetryProfile.canonicalV881();
        org.junit.jupiter.api.Assertions.assertTrue(corsetry.protection().equals(new ArmorProtectionProfile(4,10,6)),
                "La corsetería conserva 4/10/6 como perfil del ensamblaje.");
        org.junit.jupiter.api.Assertions.assertTrue(!corsetry.continuousConductivePath(),
                "Rigidizadores discontinuos no deben crear automáticamente CONDUCTOR ELÉCTRICO.");

        ArmorPiece corset=ArmorCatalog.innerCorset();
        org.junit.jupiter.api.Assertions.assertTrue(corset.innerChestLayer().orElseThrow()==InnerChestLayer.STRUCTURAL,
                "Corsé debe ocupar STRUCTURAL.");
        org.junit.jupiter.api.Assertions.assertTrue(corset.statistics().stream().anyMatch(s->s.contains("RIGIDIZADORES LONGITUDINALES")),
                "La ficha debe formalizar el ensamblaje estructurado.");
        org.junit.jupiter.api.Assertions.assertTrue(corset.statistics().stream().anyMatch(s->s.equals("CONTINUIDAD CONDUCTORA | No")),
                "Debe explicitar ausencia de vía conductora continua.");

        ArmorPiece camisole=ArmorCatalog.innerCamisoleV881();
        org.junit.jupiter.api.Assertions.assertTrue(camisole.innerChestLayer().orElseThrow()==InnerChestLayer.COVER,
                "Camisola permanece determinísticamente en COVER.");
        org.junit.jupiter.api.Assertions.assertTrue(!camisole.narrativeDescription().contains("primera prenda"),
                "La narrativa no debe prometer una posición BASE que el dominio no permite.");

        ArmorPiece combination=ArmorCatalog.innerCombinationV881();
        org.junit.jupiter.api.Assertions.assertTrue(combination.innerChestLayer().orElseThrow()==InnerChestLayer.BASE,
                "Combinación reserva BASE de INNER CHEST.");
        org.junit.jupiter.api.Assertions.assertTrue(combination.innerLeggingsLayer().orElseThrow()==InnerLeggingsLayer.BASE,
                "Combinación debe seguir reservando INNER LEGGINGS BASE.");

        // No quedan footprints manuales dentro del bloque INNER CHEST.
        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        int start=source.indexOf("// ----------------  · INNER CHEST ----------------");
        int end=source.indexOf("// ----------------  · INNER LEGGINGS ----------------");
        String block=source.substring(start,end);
        org.junit.jupiter.api.Assertions.assertTrue(!block.contains("new InventoryFootprint("),
                "INNER CHEST no debe volver a declarar footprints 2D manuales.");
    }

    private static void assertFootprint(ArmorPiece piece,int vertical,int horizontal){
        org.junit.jupiter.api.Assertions.assertTrue(piece.footprint().verticalSlots()==vertical && piece.footprint().horizontalSlots()==horizontal,
                piece.name()+" footprint inesperado: "+piece.footprint());
    }
    
}
