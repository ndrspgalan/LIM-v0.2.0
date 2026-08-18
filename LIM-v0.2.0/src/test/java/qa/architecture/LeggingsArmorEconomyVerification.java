package qa.architecture;

import domain.economy.*;
import domain.inventory.item.armor.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.misc.CurrencyType;
import java.nio.file.*;
import java.util.*;

/** QA . Cobertura acumulativa: no ejecutar durante la iteración normal. */
public final class LeggingsArmorEconomyVerification {
    private static final Set<String> PRIVATE=Set.of(
            "Polainas de Papel V881",
            "Polainas del Guerrero de Ébano",
            "Polainas de Caballero",
            "Polainas de Caballero hasta las rodillas V881",
            "Polainas Lamelares Históricas Pesadas"
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        coverage();
        categoriesAndTender();
        ogcContinuity();
        economics();
        layeringIdentity();
        integratedFootwearContinuity();
        noHistoricalAlias();
    }

    private static void coverage(){
        List<ArmorPiece> leggings=ArmorCatalog.allLeggings();
        org.junit.jupiter.api.Assertions.assertTrue(leggings.size()==40,"Deben existir cuarenta identidades LEGGINGS canónicas.");
        Set<String> names=new LinkedHashSet<>();
        for(ArmorPiece a:leggings){
            org.junit.jupiter.api.Assertions.assertTrue(a.inventoryCategory().orElseThrow()==ArmorInventoryCategory.LEGGINGS,"Pieza ajena a LEGGINGS: "+a.name());
            org.junit.jupiter.api.Assertions.assertTrue(names.add(a.name()),"Identidad LEGGINGS duplicada: "+a.name());
        }
        org.junit.jupiter.api.Assertions.assertTrue(!names.contains("Combinación interior V881"),"La combinación interior es identidad CHEST multirregional y no debe duplicarse.");
        org.junit.jupiter.api.Assertions.assertTrue(LeggingsArmorEconomicCatalog.all().keySet().equals(names),"La autoridad económica debe cubrir exactamente allLeggings().");
    }

    private static void categoriesAndTender(){
        for(var e:LeggingsArmorEconomicCatalog.all().entrySet()){
            var v=e.getValue();
            if(PRIVATE.contains(e.getKey())){
                org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.PRIVATE_USE,"Armadura dedicada debe ser uso privativo: "+e.getKey());
                org.junit.jupiter.api.Assertions.assertTrue(v.acceptedCurrencies().equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO,CurrencyType.BERYLARE,CurrencyType.REAL_A5)),
                        "Uso privativo debe admitir las cuatro divisas: "+e.getKey());
            } else {
                org.junit.jupiter.api.Assertions.assertTrue(v.goodType()==EconomicGoodType.SOCIAL_INTEREST,"Ropa/protección civil-profesional debe ser interés social: "+e.getKey());
                org.junit.jupiter.api.Assertions.assertTrue(v.acceptedCurrencies().equals(Set.of(CurrencyType.VALERITA,CurrencyType.SUELDO,CurrencyType.BERYLARE)),
                        "Interés social no admite Real A5: "+e.getKey());
            }
        }
        org.junit.jupiter.api.Assertions.assertTrue(LeggingsArmorEconomicCatalog.all().values().stream().noneMatch(v->v.goodType()==EconomicGoodType.FIRST_NECESSITY),
                " no clasifica ninguna identidad LEGGINGS como primera necesidad.");
    }

    private static void ogcContinuity(){
        String n="Polainas del Guerrero de Ébano";
        var direct=EbonyWarriorArmorEconomicPolicy.valuation(n);
        var legs=LeggingsArmorEconomicCatalog.valuation(n);
        org.junit.jupiter.api.Assertions.assertTrue(direct.equals(legs)," debe reutilizar exactamente la autoridad OGC de Ébano.");
        org.junit.jupiter.api.Assertions.assertTrue(legs.status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING,"Las polainas de Ébano deben quedar OGC pendientes.");
        org.junit.jupiter.api.Assertions.assertTrue(legs.priceValeritas().isEmpty() && !legs.ordinarilySellable(),"No puede fingirse precio para las polainas de Ébano.");
        long pending=LeggingsArmorEconomicCatalog.all().values().stream().filter(v->v.status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING).count();
        org.junit.jupiter.api.Assertions.assertTrue(pending==1,"Sólo las polainas del Guerrero de Ébano deben quedar pendientes OGC.");
    }

    private static void economics(){
        org.junit.jupiter.api.Assertions.assertTrue(price("Calzoncillos hasta la rodilla V881")==14,"Precio basal INNER inesperado.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Enagua acolchada V881")>price("Enagua V881"),"Acolchado y retícula de costura deben elevar el coste físico.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Pantalón formal V881")>price("Pantalón recto V881"),"Sastrería formal debe superar corte recto ordinario.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Falda ornamentada V881")>price("Falda de trabajo V881"),"Ornamentación intensiva debe elevar horas de trabajo.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Chaparreras ornamentadas de tradición charra V881")>price("Chaparreras cerradas (shotgun) V881"),"Ornamentación charra debe elevar la manufactura.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Polainas de Caballero hasta las rodillas V881")>price("Polainas de Caballero"),"Aleación y tolerancias V881 deben superar la manufactura histórica pese a menor masa.");
        org.junit.jupiter.api.Assertions.assertTrue(price("Polainas Lamelares Históricas Pesadas")==2800,"Tasación lamelar  inesperada.");
        for(var v:LeggingsArmorEconomicCatalog.all().values()){
            if(v.status()==EconomicValuationStatus.PRICED){
                org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
                org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>150,"Justificación económica insuficiente: "+v.objectName());
            }
        }
    }

    private static void layeringIdentity(){
        Set<String> inner=new HashSet<>();
        ArmorCatalog.allInnerLeggingsGarments().stream()
                .filter(a->a.inventoryCategory().orElseThrow()==ArmorInventoryCategory.LEGGINGS).forEach(a->inner.add(a.name()));
        Set<String> middle=new HashSet<>();
        ArmorCatalog.allMiddleLeggingsGarments().forEach(a->middle.add(a.name()));
        Set<String> outer=new HashSet<>();
        ArmorCatalog.allOuterLeggings().forEach(a->outer.add(a.name()));
        org.junit.jupiter.api.Assertions.assertTrue(inner.size()==8 && middle.size()==20 && outer.size()==12,"Distribución canónica esperada: 8 INNER + 20 MIDDLE + 12 OUTER.");
        Set<String> union=new HashSet<>(); union.addAll(inner); union.addAll(middle); union.addAll(outer);
        org.junit.jupiter.api.Assertions.assertTrue(union.equals(LeggingsArmorEconomicCatalog.all().keySet())," debe unir exactamente las tres capas físicas de LEGGINGS.");
    }

    private static void integratedFootwearContinuity(){
        org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.historicalKnightLeggings().hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR),
                "Las polainas históricas de Caballero deben conservar sabatones integrados.");
        org.junit.jupiter.api.Assertions.assertTrue(!ArmorCatalog.knightV881Leggings().hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR),
                "Las polainas V881 deben terminar antes de FEET.");
    }

    private static void noHistoricalAlias() throws Exception {
        Path p=Path.of("src/main/java/domain/inventory/item/armor/ArmorCatalog.java");
        String source=Files.readString(p);
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("ebonyWarriorLeggings()"),"Debe eliminarse el alias ebonyWarriorLeggings().");
    }

    private static long price(String n){return LeggingsArmorEconomicCatalog.valuation(n).priceValeritas().orElseThrow();}
    
}
