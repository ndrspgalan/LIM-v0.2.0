package qa.domain;

import domain.economy.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.item.misc.CurrencyType;
import java.util.*;

public final class MeleeWeaponEconomyVerification {
    private static final Set<String> SOCIAL=Set.of(
            "Pico","Zapapico","Piqueta","Cuchillo de Carnicero","Hacha de Leñador",
            "Martillo de bola","Hoz","Guadaña","Horca","Boathook"
    );
    private static final Set<String> PRIVATE=Set.of(
            "Daga","Cimitarra","Bō","Espada Helicoidal","Espadón de Rotor",
            "Katana Termo-mecánica V881","Maza Electro-mecánica V881","Pavesina Cementada de Asalto V881"
    );

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        coverage();
        categories();
        conventionalEconomy();
        specializedEconomy();
        pavesinaContinuity();
        tender();
    }

    private static void coverage(){
        Set<String> canonical=new LinkedHashSet<>();
        MeleeWeaponCatalog.allCanonical().forEach(w->canonical.add(w.name()));
        org.junit.jupiter.api.Assertions.assertTrue(canonical.size()==18,"Deben existir 18 melee canónicas incluida Pavesina.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.all().keySet().equals(canonical),
                "La autoridad económica debe coincidir exactamente con allCanonical().");
        org.junit.jupiter.api.Assertions.assertTrue(SOCIAL.size()==10 && PRIVATE.size()==8 && Collections.disjoint(SOCIAL,PRIVATE),
                "La partición económica debe cubrir 10 herramientas sociales + 8 armas privativas.");
        Set<String> union=new HashSet<>(SOCIAL); union.addAll(PRIVATE);
        org.junit.jupiter.api.Assertions.assertTrue(union.equals(canonical),"La clasificación debe abarcar todas las melee.");
        for(var v:MeleeWeaponEconomicCatalog.all().values()){
            org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.PRICED,"Toda melee debe tener precio: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().orElseThrow()>0,"Precio inválido: "+v.objectName());
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>180,"Justificación insuficiente: "+v.objectName());
        }
    }

    private static void categories(){
        for(String n:SOCIAL)
            org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation(n).goodType()==EconomicGoodType.SOCIAL_INTEREST,
                    "Una herramienta laboral no se vuelve privativa por ser WeaponItem: "+n);
        for(String n:PRIVATE)
            org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation(n).goodType()==EconomicGoodType.PRIVATE_USE,
                    "Arma dedicada/especializada debe ser uso privativo: "+n);
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Pico").acceptedCurrencies().size()==3,
                "Interés social sólo admite V/S/Berylare.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Daga").acceptedCurrencies().size()==4,
                "Uso privativo admite las cuatro monedas.");
    }

    private static void conventionalEconomy(){
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Guadaña").priceValeritas().orElseThrow()
                > MeleeWeaponEconomicCatalog.valuation("Hoz").priceValeritas().orElseThrow(),
                "La hoja larga y el asta deben encarecer la Guadaña frente a la Hoz.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Cimitarra").priceValeritas().orElseThrow()
                > MeleeWeaponEconomicCatalog.valuation("Daga").priceValeritas().orElseThrow(),
                "Una espada larga debe exigir más material y control metalúrgico que una daga.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Bō").priceValeritas().orElseThrow()==30,
                "El Bō debe reflejar su manufactura material extremadamente simple.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Cuchillo de Carnicero").goodType()==EconomicGoodType.SOCIAL_INTEREST,
                "Carnicero sigue siendo herramienta profesional aunque tenga letalidad.");
    }

    private static void specializedEconomy(){
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Espadón de Rotor").priceValeritas().orElseThrow()
                > MeleeWeaponEconomicCatalog.valuation("Espada Helicoidal").priceValeritas().orElseThrow(),
                "Rotor debe reflejar masa, mecanismo retráctil y mecanizado.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Katana Termo-mecánica V881").priceRationale().contains("Amadou"),
                "Katana debe excluir explícitamente consumibles ya tasados.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Espadón de Rotor").priceRationale().contains("Sistema Dorsal"),
                "Espadón no debe incorporar el expansor dorsal tasado por separado.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Maza Electro-mecánica V881").priceRationale().contains("celda"),
                "Maza debe justificarse por su arquitectura eléctrica material.");
        org.junit.jupiter.api.Assertions.assertTrue(MeleeWeaponEconomicCatalog.valuation("Espada Helicoidal").priceRationale().contains("doce grados"),
                "La Espada Helicoidal debe justificar su geometría de manufactura.");
    }

    private static void pavesinaContinuity(){
        var old=WeaponEconomicCatalog.valuation(WeaponEconomicCatalog.PAVESINA);
        var unified=MeleeWeaponEconomicCatalog.valuation(WeaponEconomicCatalog.PAVESINA);
        org.junit.jupiter.api.Assertions.assertTrue(old.equals(unified)," debe integrar la tasación de Pavesina sin reescribirla.");
        org.junit.jupiter.api.Assertions.assertTrue(unified.priceValeritas().orElseThrow()==4800,"La Pavesina conserva 4.800 V.");
    }

    private static void tender(){
        var social=MeleeWeaponEconomicCatalog.valuation("Hacha de Leñador");
        org.junit.jupiter.api.Assertions.assertTrue(!social.acceptedCurrencies().contains(CurrencyType.REAL_A5),
                "Una herramienta de interés social no puede denominarse en Real A5.");
        var priv=MeleeWeaponEconomicCatalog.valuation("Espadón de Rotor");
        org.junit.jupiter.api.Assertions.assertTrue(priv.acceptedCurrencies().contains(CurrencyType.REAL_A5),
                "Una plataforma privativa sí admite Real A5.");
    }

    
}
