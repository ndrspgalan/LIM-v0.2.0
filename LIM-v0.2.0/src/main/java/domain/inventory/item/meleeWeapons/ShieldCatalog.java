package domain.inventory.item.meleeWeapons;

import domain.character.sheet.Attribute;
import domain.combat.ShieldCombatPolicy;
import domain.combat.ShieldSpecification;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

/** único escudo dedicado V881. */
public final class ShieldCatalog {
    private ShieldCatalog(){}

    public static WeaponItem pavesinaCementadaDeAsaltoV881(){
        ShieldSpecification s=ShieldCombatPolicy.PAVESINA_V881;
        Set<WeaponTrait> traits=Set.of(WeaponTrait.SHIELD,WeaponTrait.ERGONOMIA_INTRINCADA);
        var req=List.<AttributeRequirement>of();
        String narrative="Hubo un momento en que el proyectil pareció haber condenado definitivamente al escudo. Valerian llegó a la conclusión contraria: no había fracasado el principio de interponer materia entre el cuerpo y aquello que pretendía atravesarlo; había fracasado la materia que se estaba interponiendo. La Pavesina Cementada de Asalto V881 parte de una placa continua de acero al níquel-cromo, cementada y endurecida en superficie sin sacrificar la tenacidad del núcleo. Su convexidad distribuye la carga sobre una masa única de metal; cuero, fieltro prensado, abrazadera y suspensión interior permiten gobernarla con una sola extremidad, aunque sus 8,8 kg y más de medio metro de eje mayor hacen que esa ergonomía dependa por completo de la capacidad física del portador. No admite agarre bimanual útil: la segunda mano destruiría precisamente la posibilidad de combatir detrás de ella. Al alzarse se convierte en la capa exterior del cuerpo; puede proteger la cabeza o, mediante la postura alternativa, el torso. El acero conduce electricidad con la misma indiferencia con la que detiene una hoja, y su cementación reduce el deterioro de una estructura que puede volver al taller, recibir acero y recuperar continuidad metalúrgica. V881 no devolvió el escudo al campo de batalla. Construyó uno que todavía tenía derecho a estar allí.";
        WeaponItem item=new WeaponItem(
                s.name(),narrative,s.weightKg(),domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor(s.name()),0.55,
                List.of(new WeaponMode("Arrollamiento",new LethalityProfile(0,0,s.weightKg()))),
                req,List.of(),
                List.of(
                        "DIMENSIONES | 55 × 42 cm · placa 5 mm · convexidad aprox. 5,5 cm",
                        "PESO | 8,8 kg",
                        "PROTECCIÓN | 100% perforante / 100% cortante / 100% contundente",
                        "COBERTURA ALZADA | +15 pp · HEAD o BODY, nunca ambas a la vez",
                        "POSTURA INICIAL | HEAD · MOUSE WHEEL alterna HEAD/BODY",
                        "DESGASTE | ×0,5 por cementación",
                        "REPARACIÓN | Acero + reacondicionamiento metalúrgico",
                        "LETALIDAD DE ARROLLAMIENTO | FUERZA + 1 contundente por kg del escudo",
                        "CONFIGURACIÓN | Exclusivamente 1H · dual wielding permitido",
                        "ATAQUES | Arrollamiento · Golpe desestabilizador · Bloqueo · Sin PARRY"
                ), OptionalDouble.empty(),0,false,WeaponConfigurationPolicy.shield(),traits
        ).withCombatPolicy(new WeaponCombatPolicy(Set.of(WeaponCombatAction.LIGHT_ATTACK,WeaponCombatAction.DESTABILIZE,WeaponCombatAction.BLOCK)));
        item.withCombatActionsFor(WeaponActionMode.PRIMARY,Set.of(WeaponCombatAction.LIGHT_ATTACK,WeaponCombatAction.DESTABILIZE));
        item.withCombatActionsFor(WeaponActionMode.ALTERNATIVE,Set.of(WeaponCombatAction.BLOCK));
        item.allowLeftHandLimitException();
        return item.withProperties(List.of(
                ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_CONDUCTOR,"CONDUCTOR ELÉCTRICO","El acero al níquel-cromo conserva continuidad conductora y transmite una descarga dentro del circuito corporal si no existe una derivación protectora completa.","ELECTRICIDAD | Conductor"),
                ItemProperty.alwaysActive(ItemPropertyId.INTRICATE_ERGONOMICS,"ERGONOMÍA INTRINCADA","La pavesina sólo admite una mano pese a su masa y tamaño; gobernarla exige que FUERZA y AGUANTE sostengan conjuntamente la carga de manejo.","MANEJO 1H | Exige capacidad conjunta de FUERZA + AGUANTE")
        ));
    }
}
