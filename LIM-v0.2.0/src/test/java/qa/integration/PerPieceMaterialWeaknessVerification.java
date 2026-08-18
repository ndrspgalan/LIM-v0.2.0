package qa.integration;

import domain.combat.*;
import domain.inventory.equipment.*;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.*;

import java.util.*;

public final class PerPieceMaterialWeaknessVerification {
    private PerPieceMaterialWeaknessVerification(){}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        NonConventionalDamageResolver resolver=new NonConventionalDamageResolver();

        // Acero: cada pieza aplicable aporta su propio x2, con independencia del contacto con tierra.
        EquipmentState historicalKnight=new EquipmentState(Map.of(
                EquipmentSlot.HEAD,ArmorCatalog.historicalKnightHelmet(),
                EquipmentSlot.CHEST,ArmorCatalog.historicalKnightChest(),
                EquipmentSlot.BRACERS,ArmorCatalog.historicalKnightBracers(),
                EquipmentSlot.LEGGINGS,ArmorCatalog.historicalKnightLeggings()
        ));
        org.junit.jupiter.api.Assertions.assertTrue(!GroundingPolicy.fullBodyGroundingPath(historicalKnight),
                "Sabatones conductores no constituyen una derivación protectora.");
        var knightBody=resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.BODY,historicalKnight,0,false);
        close(knightBody.materialAdjustedDamage(),60,"Tres piezas BODY de acero => x6");
        org.junit.jupiter.api.Assertions.assertTrue(knightBody.amplifiedArmor().size()==3,"BODY debe registrar tres piezas amplificadoras.");
        var knightHead=resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.HEAD,historicalKnight,0,false);
        close(knightHead.materialAdjustedDamage(),20,"Casco histórico de acero => x2");
        org.junit.jupiter.api.Assertions.assertTrue(knightHead.amplifiedArmor().contains("Casco de Caballero"),"HEAD debe registrar el casco.");

        EquipmentState v881Knight=new EquipmentState(Map.of(
                EquipmentSlot.CHEST,ArmorCatalog.knightV881Chest(),
                EquipmentSlot.BRACERS,ArmorCatalog.knightV881Bracers(),
                EquipmentSlot.LEGGINGS,ArmorCatalog.knightV881Leggings()
        ));
        close(resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.BODY,v881Knight,0,false)
                .materialAdjustedDamage(),60,"Caballero V881: tres piezas BODY de acero => x6");

        // Bronce: misma debilidad eléctrica material.
        EquipmentState bronze=new EquipmentState(Map.of(EquipmentSlot.HEAD,ArmorCatalog.spartanHelmetV881()));
        close(resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.HEAD,bronze,0,false)
                .materialAdjustedDamage(),20,"Casco de bronce => x2");

        // Excepciones expresas: no son una inmunidad global, sólo suprimen el x2 de esa propia pieza.
        ArmorPiece aeronaut=ArmorCatalog.retractableAeronautHelmet();
        ArmorPiece panopticon=ArmorCatalog.enlightenedPanopticonCanonical();
        org.junit.jupiter.api.Assertions.assertTrue(aeronaut.hasProperty(ItemPropertyId.ELECTRICAL_WEAKNESS_SUPPRESSED),
                "Aeronauta debe declarar la excepción eléctrica local.");
        org.junit.jupiter.api.Assertions.assertTrue(panopticon.hasProperty(ItemPropertyId.ELECTRICAL_WEAKNESS_SUPPRESSED),
                "Panóptico debe declarar la excepción eléctrica local.");
        close(resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.HEAD,
                new EquipmentState(Map.of(EquipmentSlot.HEAD,aeronaut)),0,false).materialAdjustedDamage(),
                10,"Aeronauta no aporta x2");
        close(resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.HEAD,
                new EquipmentState(Map.of(EquipmentSlot.HEAD,panopticon)),0,false).materialAdjustedDamage(),
                10,"Panóptico no aporta x2");

        // Una pieza principalmente de cuero no pasa a ser "armadura de acero" por una puntera localizada.
        EquipmentState industrialBoots=new EquipmentState(Map.of(EquipmentSlot.FEET,ArmorCatalog.leatherHeavyWorkBootsV881()));
        close(resolver.resolve(DamageType.ELECTRICITY,10,ArmorHitLocation.BODY,industrialBoots,0,false)
                .materialAdjustedDamage(),10,"Puntera secundaria de acero no convierte la bota en pieza de acero x2");

        // Ébano histórico: INFLAMABLE es igualmente una debilidad por pieza.
        EquipmentState historicalEbony=new EquipmentState(Map.of(
                EquipmentSlot.CHEST,ArmorCatalog.historicalEbonyWarriorChest(),
                EquipmentSlot.BRACERS,ArmorCatalog.historicalEbonyWarriorBracers(),
                EquipmentSlot.LEGGINGS,ArmorCatalog.historicalEbonyWarriorLeggings()
        ));
        var ebonyBurn=resolver.resolve(DamageType.BURN,10,ArmorHitLocation.BODY,historicalEbony,0,false);
        close(ebonyBurn.materialAdjustedDamage(),60,"Tres piezas BODY de ébano histórico => x6 quemadura");
        org.junit.jupiter.api.Assertions.assertTrue(ebonyBurn.amplifiedArmor().size()==3,"Las tres piezas históricas de ébano deben amplificar.");
        for(ArmorPiece p:List.of(ArmorCatalog.historicalEbonyWarriorChest(),
                                 ArmorCatalog.historicalEbonyWarriorBracers(),
                                 ArmorCatalog.historicalEbonyWarriorLeggings())) {
            org.junit.jupiter.api.Assertions.assertTrue(p.hasProperty(ItemPropertyId.FLAMMABLE),p.name()+" debe conservar INFLAMABLE.");
        }

        // Ébano V881: la mineralización/encapsulado suprime esa debilidad en sus piezas actuales.
        EquipmentState ebonyV881=new EquipmentState(Map.of(
                EquipmentSlot.CHEST,ArmorCatalog.ebonyWarriorV881Chest(),
                EquipmentSlot.BRACERS,ArmorCatalog.ebonyWarriorV881LeftBracer()
        ));
        close(resolver.resolve(DamageType.BURN,10,ArmorHitLocation.BODY,ebonyV881,0,false)
                .materialAdjustedDamage(),10,"Ébano V881 no debe ser inflamable.");
    }

    private static void close(double a,double b,String m){
        if(Math.abs(a-b)>1e-9) throw new AssertionError(m+": "+a+" != "+b);
    }
    
}
