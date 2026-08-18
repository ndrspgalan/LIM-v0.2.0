package qa.architecture;

import domain.economy.*;
import domain.inventory.catalog.PhysicalObjectCatalog;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.firearmAccessories.*;
import domain.inventory.item.meleeWeapons.ShieldCatalog;
import domain.inventory.item.misc.*;
import java.nio.file.*;
import java.util.*;

public final class WeaponCatalogAndPeripheralEconomyVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        firearmAccessoryCategory();
        migratedMiscellaneous();
        pavesinaRemainsMeleeWeapon();
        economics();
        noDeprecatedWeaponAliases();
    }

    private static void firearmAccessoryCategory(){
        org.junit.jupiter.api.Assertions.assertTrue(FirearmAccessoryCatalog.all().size()==5,"Deben existir exactamente cinco firearm accessories.");
        for(FirearmAccessoryItem item:FirearmAccessoryCatalog.all()){
            var d=PhysicalObjectCatalog.definitionFor(item);
            org.junit.jupiter.api.Assertions.assertTrue(d.family().equals("firearmAccessory"),"Categoría física incorrecta: "+item.name());
            org.junit.jupiter.api.Assertions.assertTrue(FirearmAccessoryEconomicCatalog.valuation(item.name()).goodType()==EconomicGoodType.PRIVATE_USE,
                    "Los firearm accessories son de uso privativo: "+item.name());
        }
        org.junit.jupiter.api.Assertions.assertTrue(FirearmAccessoryCatalog.fiedlerSightV881().mount()==FirearmAccessoryMount.OPTIC,"Fiedler debe seguir siendo óptica.");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmAccessoryCatalog.bipodV881().mount()==FirearmAccessoryMount.BIPOD,"Bípode debe conservar montaje.");
    }

    private static void migratedMiscellaneous(){
        var battery=new ElectromagneticPortableBatteryItem();
        var charger=new PortableElectromagneticBatteryCharger();
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.definitionFor(battery).family().equals("misc"),"La batería debe estar en misc.");
        org.junit.jupiter.api.Assertions.assertTrue(PhysicalObjectCatalog.definitionFor(charger).family().equals("misc"),"El cargador debe estar en misc.");
        org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousEconomicCatalog.valuation(battery.name()).priceValeritas().orElseThrow()==900,
                "Tasación de batería .");
        org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousEconomicCatalog.valuation(charger.name()).priceValeritas().orElseThrow()==650,
                "Tasación de cargador .");
        org.junit.jupiter.api.Assertions.assertTrue(MiscellaneousEconomicCatalog.valuation(charger.name()).priceRationale().contains("se tasa por separado"),
                "El cargador no debe incorporar el precio de la batería.");
    }

    private static void pavesinaRemainsMeleeWeapon(){
        WeaponItem p=ShieldCatalog.pavesinaCementadaDeAsaltoV881();
        org.junit.jupiter.api.Assertions.assertTrue(p.name().equals(WeaponEconomicCatalog.PAVESINA),"Identidad canónica de Pavesina.");
        org.junit.jupiter.api.Assertions.assertTrue(p.getClass()==WeaponItem.class,"La Pavesina debe seguir materializándose como WeaponItem melee, no como armor.");
        org.junit.jupiter.api.Assertions.assertTrue(p.modes().stream().anyMatch(m->m.name().equals("Arrollamiento")),"La Pavesina conserva modo ofensivo melee.");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponEconomicCatalog.valuation(p.name()).priceValeritas().orElseThrow()==4800,
                "Tasación  de Pavesina.");
    }

    private static void economics(){
        org.junit.jupiter.api.Assertions.assertTrue(FirearmAccessoryEconomicCatalog.all().size()==5,"Deben tasarse cinco accesorios.");
        org.junit.jupiter.api.Assertions.assertTrue(WeaponEconomicCatalog.all().size()==1,"Debe tasarse la Pavesina.");
        org.junit.jupiter.api.Assertions.assertTrue(FirearmAccessoryEconomicCatalog.valuation("Mirilla Winchester A5 V881").priceValeritas().orElseThrow()
                > FirearmAccessoryEconomicCatalog.valuation("Mirilla Fiedler V881").priceValeritas().orElseThrow(),
                "La complejidad óptica debe separar A5 y Fiedler.");
        for(var v:FirearmAccessoryEconomicCatalog.all().values())
            org.junit.jupiter.api.Assertions.assertTrue(v.priceRationale().length()>150,"Justificación económica insuficiente: "+v.objectName());
        org.junit.jupiter.api.Assertions.assertTrue(WeaponEconomicCatalog.valuation(WeaponEconomicCatalog.PAVESINA).priceRationale().contains("níquel-cromo"),
                "La Pavesina debe justificar aleación y tratamiento.");
    }

    private static void noDeprecatedWeaponAliases() throws Exception {
        Path src=Path.of("src/main/java/domain/inventory/item");
        List<Path> relevant=new ArrayList<>();
        for(String folder:List.of("firearms","ammunition","firearmAccessories")){
            try(var s=Files.walk(src.resolve(folder))){
                s.filter(p->p.toString().endsWith(".java")).forEach(relevant::add);
            }
        }
        for(Path p:relevant){
            String t=Files.readString(p);
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("selectiveAutoloadingPistolV881"),"Alias selectivo no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("automaticRifleV881()"),"Alias de fusil automático no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("selectivePistol45Magazine"),"Alias de cargador selectivo no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("intermediateV881Magazine"),"Alias intermedio no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("tungsten46Pouch"),"Alias bolsa tungsteno no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("mediumRangeSightV881"),"Alias óptico medio no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("precisionSightV881"),"Alias óptico precisión no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("SELECTIVE_AUTOLOADING_PISTOL_NARRATIVE"),"Alias narrativo selectivo no debe existir.");
            org.junit.jupiter.api.Assertions.assertTrue(!t.contains("AUTOMATIC_RIFLE_NARRATIVE"),"Alias narrativo automático no debe existir.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(src.resolve("firearms/SelectivePistolFirearmItem.java")),"Clase alias SelectivePistol debe eliminarse.");
        org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(src.resolve("firearms/AutomaticRifleFirearmItem.java")),"Clase alias AutomaticRifle debe eliminarse.");
        org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(src.resolve("weaponAttachments")),"La categoría weaponAttachments antigua debe desaparecer.");
    }

    
}
