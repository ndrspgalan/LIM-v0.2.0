package qa.architecture;

import domain.economy.*;
import domain.inventory.item.armor.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.misc.CurrencyType;
import java.nio.file.*;
import java.util.*;

/**
 * QA . Se añade como cobertura acumulada, pero no se ejecuta durante la iteración normal.
 */
public final class BracersArmorEconomyVerification {
    private static final Set<String> SOCIAL=Set.of(
            "Guantes de Precisión V881",
            "Guantes de cuero endurecido con los dedos al aire V881",
            "Guantes de Taller V881"
    );
    private static final Set<String> OGC=Set.of(
            "Brazales del Guerrero de Ébano",
            "Brazal izquierdo del Guerrero de Ébano V881"
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        coverage();
        categories();
        ogcContinuity();
        economics();
        improvisedShieldDoesNotSetPrice();
        noHistoricalAlias();
    }

    private static void coverage(){
        List<ArmorPiece> bracers=ArmorCatalog.allBracers();
        org.junit.jupiter.api.Assertions.assertTrue(bracers.size()==9,"Deben existir nueve BRACERS canónicos.");
        Set<String> names=new LinkedHashSet<>();
        for(ArmorPiece a:bracers){
            org.junit.jupiter.api.Assertions.assertTrue(names.add(a.name()),"Identidad BRACERS duplicada: "+a.name());
        }
        org.junit.jupiter.api.Assertions.assertTrue(BracersArmorEconomicCatalog.all().keySet().equals(names),
                "La autoridad económica debe cubrir exactamente allBracers().");
    }

    private static void categories(){
        for(String n:SOCIAL){
            var v=BracersArmorEconomicCatalog.valuation(n);
            org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.SOCIAL_INTEREST,"Equipo profesional debe ser interés social: "+n);
            org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.PRICED,"Guante profesional debe tener precio: "+n);
            org.junit.jupiter.api.Assertions.assertTrue(v.acceptedCurrencies().equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO,CurrencyType.BERYLARE)),
                    "Interés social no admite Real A5: "+n);
        }
        for(var e:BracersArmorEconomicCatalog.all().entrySet()){
            if(!SOCIAL.contains(e.getKey()))
                org.junit.jupiter.api.Assertions.assertTrue(e.getValue().goodType()==EconomicGoodType.PRIVATE_USE,
                        "Armadura dedicada/Ébano debe ser uso privativo: "+e.getKey());
        }
    }

    private static void ogcContinuity(){
        for(String n:OGC){
            var direct=EbonyWarriorArmorEconomicPolicy.valuation(n);
            var bracers=BracersArmorEconomicCatalog.valuation(n);
            org.junit.jupiter.api.Assertions.assertTrue(direct.equals(bracers)," debe reutilizar exactamente la autoridad OGC: "+n);
            org.junit.jupiter.api.Assertions.assertTrue(bracers.status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING,"Falta estado OGC: "+n);
            org.junit.jupiter.api.Assertions.assertTrue(bracers.priceValeritas().isEmpty() && !bracers.ordinarilySellable(),
                    "No puede fingirse un precio para "+n);
        }
        long pending=BracersArmorEconomicCatalog.all().values().stream()
                .filter(v->v.status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING).count();
        org.junit.jupiter.api.Assertions.assertTrue(pending==2,"Sólo las dos piezas BRACERS del Guerrero de Ébano deben quedar pendientes OGC.");
    }

    private static void economics(){
        org.junit.jupiter.api.Assertions.assertTrue(BracersArmorEconomicCatalog.valuation("Guantes de Taller V881").priceValeritas().orElseThrow()==38,
                "Guantes de Taller deben conservar una tasación profesional moderada.");
        org.junit.jupiter.api.Assertions.assertTrue(BracersArmorEconomicCatalog.valuation("Brazales de Papel V881").priceValeritas().orElseThrow()
                > BracersArmorEconomicCatalog.valuation("Guantes de Precisión V881").priceValeritas().orElseThrow(),
                "La manufactura multicapa de papel debe superar unos guantes técnicos.");
        org.junit.jupiter.api.Assertions.assertTrue(BracersArmorEconomicCatalog.valuation("Brazales de Caballero incluidos codera y nudillos V881")
                .priceValeritas().orElseThrow()
                > BracersArmorEconomicCatalog.valuation("Brazales de Caballero").priceValeritas().orElseThrow(),
                "Acero/wolframio, codera, nudillos y articulación V881 deben elevar la manufactura.");
        org.junit.jupiter.api.Assertions.assertTrue(BracersArmorEconomicCatalog.valuation("Brazales Lamelares Históricos Pesados")
                .priceRationale().toLowerCase().contains("lamelas"),
                "La tasación lamelar debe justificarse por su construcción física.");
        for(var v:BracersArmorEconomicCatalog.all().values()){
            if(v.status()==EconomicValuationStatus.PRICED){
                org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
                org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>150,"Justificación insuficiente: "+v.objectName());
            }
        }
    }

    private static void improvisedShieldDoesNotSetPrice(){
        for(ArmorPiece piece:ArmorCatalog.allBracers()){
            if(piece.hasActiveProperty(ItemPropertyId.IMPROVISED_SHIELD)){
                var v=BracersArmorEconomicCatalog.valuation(piece.name());
                String rationale=v.priceRationale().toLowerCase();
                org.junit.jupiter.api.Assertions.assertTrue(!rationale.contains("multiplicador por escudo")
                                && (!rationale.contains("prima de gameplay") || rationale.contains("no una prima de gameplay")),
                        "La capacidad de escudo no debe convertirse en una tarifa abstracta: "+piece.name());
            }
        }
    }

    private static void noHistoricalAlias() throws Exception {
        String source=Files.readString(Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java"));
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("ArmorPiece ebonyWarriorBracers()"),
                "El alias histórico ebonyWarriorBracers() debe desaparecer.");
    }

    
}
