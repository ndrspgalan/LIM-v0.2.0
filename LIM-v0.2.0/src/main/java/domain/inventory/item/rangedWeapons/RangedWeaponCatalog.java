package domain.inventory.item.rangedWeapons;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;
import java.util.*;

/** Catálogo canónico : las únicas armas a distancia convencionales supervivientes. */
public final class RangedWeaponCatalog {
 private RangedWeaponCatalog(){}
 public static final String SLING_NARRATIVE="""
La Honda sobrevivió al estándar V881 precisamente porque nunca intentó competir con él. La aparición de la pólvora sin humo, las armas neumáticas y los sistemas electromagnéticos convirtió cualquier intento de aumentar artificialmente su potencia en un ejercicio inútil, pero ninguna de esas tecnologías consiguió eliminar sus ventajas elementales: una masa prácticamente despreciable, ausencia de mecanismos, funcionamiento silencioso, inexistencia de desgaste apreciable y la posibilidad de utilizar como munición simples guijarros recogidos del entorno.

La estandarización de la bala de plomo del Rifle Neumático terminó proporcionándole además una segunda munición sin necesidad de desarrollar nada específicamente para ella. Un guijarro lanzado mediante la Honda produce 35 puntos de daño contundente; sustituirlo por la misma bala de plomo eleva ese valor a 60, no porque la Honda reproduzca las prestaciones del Rifle Neumático, sino porque utiliza un proyectil mucho más denso y regular sin disponer de la velocidad necesaria para convertirlo en una amenaza perforante comparable.

Su permanencia no responde por tanto a una función militar principal. La Honda continúa existiendo porque puede transportarse casi sin coste, apenas exige mantenimiento y proporciona una capacidad ofensiva allí donde gastar sistemas más complejos resulta innecesario. Sus 65 metros de alcance efectivo y su cadencia de un disparo cada diez segundos representan el límite aceptado de una tecnología que V881 decidió conservar precisamente porque no necesitaba perfeccionarla para seguir siendo útil.
""";
 public static final String RECURVE_NARRATIVE="""
El Arco Simple Recurvo es el superviviente de una familia de armas a distancia mucho mayor. El arco simple recto perdió progresivamente su función cuando la recurvatura permitió almacenar y devolver energía de forma más eficiente sin abandonar una construcción esencialmente sencilla. El arco largo y el gran arco largo siguieron el camino contrario: aumentar sus prestaciones exigía incrementar longitud, espacio de manejo y, en el segundo caso, recurrir a una plataforma tan grande que uno de sus extremos podía apoyarse contra el suelo para disparar proyectiles proporcionalmente mayores. La aparición de sistemas de proyección más compactos terminó haciendo injustificable esa escalada dimensional.

Las ballestas y arbalestas alcanzaron su propio límite intentando resolver el mismo problema mediante energía mecánica almacenada. Incrementar su potencia exigía mecanismos de armado, mayor masa y ciclos de recarga progresivamente más lentos justo cuando las armas neumáticas y, posteriormente, la pólvora sin humo proporcionaban una solución mucho más escalable. El Chu Ko Nu sacrificaba potencia individual para conseguir repetición; cuando Valerian dispuso de armas automáticas, esa ventaja dejó de constituir un nicho. El Dan Nu trasladaba masa, complejidad y energía mecánica a una escala que tampoco podía competir con las nuevas tecnologías de proyección.

La selección resultante no conservó el arco más grande ni el mecanismo mecánico más potente. Conservó aquel que seguía justificando su existencia después de aceptar que nunca volvería a dominar el campo de batalla. El Arco Simple Recurvo proporciona 120 metros de alcance efectivo, utiliza munición recuperable, funciona silenciosamente y conserva una arquitectura suficientemente sencilla para resultar reparable y longeva. Su desgaste puede reducir sus prestaciones hasta un máximo del 15 %, pero progresa a la mitad de velocidad que en un Arco Compuesto sometido al mismo uso.

V881 no convirtió así un arma antigua en una firearm sin pólvora. Conservó exactamente aquello que todavía hacía valioso al arco: silencio, simplicidad, independencia de propulsantes y capacidad para escoger el efecto terminal mediante la flecha utilizada.
""";
 public static final String COMPOSITE_NARRATIVE="""
El Arco Compuesto representa el límite práctico que Valerian encontró para la arquería portátil. La combinación artesanal de materiales con comportamientos mecánicos diferentes permitió concentrar en una plataforma corta una eficiencia que un arco simple solo podía perseguir mediante otras concesiones. Madera, cuerno y tendón —con ébano reservado para aquellos elementos estructurales donde su densidad y rigidez resultaban ventajosas— permiten alcanzar 180 metros de alcance efectivo y añadir diez puntos tanto al componente perforante como al cortante de cualquier flecha utilizada.

Esa eficiencia tiene un coste material. Adhesiones, materiales heterogéneos y elementos sometidos a solicitaciones diferentes hacen que el Arco Compuesto acumule desgaste aproximadamente al doble de velocidad que el Arco Simple Recurvo. Ambos comparten un deterioro máximo del 15 %, pero el compuesto alcanza ese límite antes. Valerian aceptó esta desventaja porque el arma seguía conservando aquello que justificaba la supervivencia de la arquería: silencio, movilidad, munición recuperable y ausencia de propulsante.

La evolución de las armas a distancia produjo, sin embargo, una selección mucho más severa entre los sistemas defensivos. Aspis, pelta, clípeo, scutum, celcio, cometa, targe, lágrima, pavés, adarga, tarja y rodela habían intercambiado históricamente superficie, masa y movilidad para proporcionar protección direccional. La pólvora sin humo alteró esa relación. Cuando los proyectiles alcanzaron suficiente penetración y precisión a distancias tácticas, ocupar una mano y transportar varios kilogramos para proteger solamente determinados ángulos dejó de constituir una inversión defensiva suficientemente fiable.

Valerian tampoco respondió haciendo los escudos progresivamente más gruesos. Aumentar su protección hasta competir con las nuevas amenazas habría incrementado masa y volumen mientras seguía dejando al combatiente dependiente de la orientación del escudo y privándolo de una mano que muchas armas modernas exigían. Incluso el pavés, que había llevado el principio hasta una cobertura portátil de posición, terminó perdiendo su nicho frente al terreno, los parapetos y otras coberturas dedicadas que no necesitaban acompañar permanentemente al combatiente.

La desaparición de los escudos y la supervivencia del Arco Compuesto son, por ello, consecuencias distintas de una misma selección tecnológica. V881 no conservó una tecnología porque fuese antigua ni la eliminó porque existiese otra más moderna. Conservó aquellas cuya relación entre masa, función y logística seguía proporcionando una ventaja propia. El Arco Compuesto todavía la tenía. Los escudos personales tradicionales, frente al nuevo ecosistema ofensivo, dejaron de tenerla.
""";
 public static RangedWeaponItem sling(){return new RangedWeaponItem("Honda",SLING_NARRATIVE,0.18,domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Honda"),RangedWeaponType.SLING,0.75,2,8,RangedWeaponGrip.ONE_HANDED,65,10,RangedWeaponWearProfile.NON_DEGRADING,0,0,withCoup(PersonalTransportUseProperties.all()));}
 public static RangedWeaponItem simpleRecurveBow(){return new RangedWeaponItem("Arco Simple Recurvo",RECURVE_NARRATIVE,0.80,domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Arco Simple Recurvo"),RangedWeaponType.SIMPLE_RECURVE_BOW,1.20,6,12,RangedWeaponGrip.TWO_HANDED,120,1,RangedWeaponWearProfile.BOW_STANDARD,0,0,withCoup(List.of(
  ItemProperty.alwaysActive(ItemPropertyId.COPILOT,"COPILOTO","Puede utilizarse desde el asiento del pasajero.","Uso desde asiento de copiloto"),
  ItemProperty.alwaysActive(ItemPropertyId.EQUESTRIAN,"ECUESTRE","Puede utilizarse mientras se cabalga.","Uso como conductor de caballo")
 )));}
 public static RangedWeaponItem compositeBow(){return new RangedWeaponItem("Arco Compuesto",COMPOSITE_NARRATIVE,0.75,domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Arco Compuesto"),RangedWeaponType.COMPOSITE_BOW,1.20,6,12,RangedWeaponGrip.TWO_HANDED,180,1,RangedWeaponWearProfile.BOW_DOUBLE,10,10,withCoup(List.of(
  ItemProperty.alwaysActive(ItemPropertyId.COPILOT,"COPILOTO","Puede utilizarse desde el asiento del pasajero.","Uso desde asiento de copiloto"),
  ItemProperty.alwaysActive(ItemPropertyId.EQUESTRIAN,"ECUESTRE","Puede utilizarse mientras se cabalga.","Uso como conductor de caballo")
 )));}
 private static List<ItemProperty> withCoup(List<ItemProperty> base){List<ItemProperty> all=new ArrayList<>(base);all.add(PersonalTransportUseProperties.coupDeGrace());return List.copyOf(all);}
 public static List<RangedWeaponItem> all(){return List.of(sling(),simpleRecurveBow(),compositeBow());}
}
