package domain.economy;

import domain.inventory.item.armor.ArmorCatalog;
import java.util.*;

/**
 *  — autoridad económica CHEST.
 * INNER + MIDDLE (LIGHT/MEDIUM/HEAVY) + OUTER. MEDIUM/HEAVY no constituye una cuarta capa:
 * es una subfamilia material de MIDDLE y compite por la misma ranura con cualquier MIDDLE LIGHT.
 */
public final class ChestArmorEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private ChestArmorEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("CHEST sin tasación : "+name);
        return v;
    }
    public static Map<String,EconomicValuation> all(){return DATA;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        // INNER — confección cotidiana y estructural.
        s(m,"Camiseta interior de punto V881",12,"Prenda interior ligera de punto, con poca materia, geometría simple y producción repetible. El precio remunera fibra, tejido, corte y costuras básicas.");
        s(m,"Camisa V881",24,"Camisería ordinaria con más paneles, cuello, mangas, puños y cierres que una camiseta. Tejido, patronaje y costuras dominan el coste.");
        s(m,"Camisa de trabajo V881",30,"Camisa reforzada para desgaste laboral, con tejido más denso, costuras resistentes y mayor masa. La prima procede de durabilidad y materia adicional.");
        s(m,"Camisa modular V881",42,"Camisa con construcción modular y uniones adicionales que exige más piezas, cierres y precisión de ensamblaje que una camisa ordinaria.");
        s(m,"Blusa V881",28,"Prenda de confección cotidiana con patronaje de torso y mangas, cierres y acabados. Su valor es principalmente textil y de mano de obra.");
        s(m,"Blusa regional V881",40,"Blusa con patronaje y acabado regional más elaborado que una prenda básica; la prima corresponde a confección, detalles y menor estandarización.");
        s(m,"Chemise V881",25,"Prenda interior amplia y sencilla que consume una cantidad moderada de tejido, con costuras largas pero escasa estructura.");
        s(m,"Gömlek V881",30,"Camisa tradicional de corte amplio cuyo precio deriva de tejido, mangas, cuello y confección completa, sin componentes estructurales costosos.");
        s(m,"Camisola V881",15,"Prenda interior ligera de poca masa y construcción simple, con bajo consumo de tejido y pocas operaciones de costura.");
        s(m,"Chemisette V881",9,"Pieza interior mínima de 0,10 kg; poca materia, corte simple y acabado básico la mantienen entre las prendas CHEST más baratas.");
        s(m,"Pechera desmontable V881",10,"Pieza frontal pequeña pero específicamente patronada para fijarse y retirarse de otras prendas. Consume poca tela y algo más de trabajo de borde y sujeción.");
        s(m,"Corsé V881",95,"Prenda estructural de 0,65 kg con múltiples paneles, refuerzos, ojales, cordonería y ajuste corporal. La mano de obra y la estructura dominan sobre el mero peso textil.");
        s(m,"Corsé masculino V881",88,"Corsetería estructural de torso con paneles reforzados, cierres y ajuste preciso. Algo menos masivo que el corsé ordinario, pero igualmente intensivo en confección.");
        s(m,"Cubrecorsé V881",18,"Cubierta textil ligera destinada a interponerse sobre el corsé; consume poca materia y su patronaje es sencillo.");
        s(m,"Combinación interior V881",34,"Prenda interior extensa de 0,45 kg con mayor superficie corporal y varias costuras de unión. El coste procede de tejido y confección, no de protección.");

        // MIDDLE LIGHT — misma ranura lógica que las piezas protectoras MEDIUM/HEAVY.
        s(m,"Chaleco V881",35,"Chaleco de paño con delanteros, espalda, forro y cierres; requiere más patronaje y acabado que una camisa aunque carezca de mangas.");
        s(m,"Chaleco largo V881",44,"Versión de mayor longitud y masa, con más superficie de paño, forro y costuras; mantiene una arquitectura de sastrería relativamente sencilla.");
        s(m,"Chaleco de trabajo V881",38,"Chaleco utilitario de tejido resistente y costuras reforzadas, concebido para uso laboral prolongado más que para acabado formal.");
        s(m,"Chaleco acolchado V881",72,"Prenda de 0,9 kg con capas textiles, relleno y costuras que estabilizan el acolchado. El volumen de material y la confección multicapa elevan el precio.");
        s(m,"Chaleco de montar V881",52,"Chaleco ajustado para movilidad ecuestre, con patronaje más preciso, refuerzos y cierres capaces de mantenerse estables durante el movimiento.");
        s(m,"Corpiño V881",62,"Prenda ceñida y estructurada que exige patronaje corporal, varios paneles, cierres y ajuste más fino que un chaleco utilitario.");
        s(m,"Corpiño regional V881",78,"Corpiño de construcción ceñida con acabado regional y menor estandarización. Mano de obra, detalles y ajuste explican la prima.");
        s(m,"Spencer V881",58,"Chaqueta corta de sastrería con mangas, cuello, forro y cierres; su precio refleja patronaje y acabado pese a su longitud reducida.");
        s(m,"Chaqueta interior acolchada V881",105,"Prenda interior de 1,4 kg con varias capas, relleno distribuido y costuras de retención. La intensidad material y de costura domina el coste.");
        s(m,"Jubón V881",68,"Prenda ajustada de 0,8 kg con numerosos paneles y cierres, concebida para mantener forma y movilidad. Requiere sastrería más intensa que una camisa.");
        s(m,"Cardigan V881",42,"Prenda de punto de 0,55 kg con mangas y apertura frontal. El tejido de punto y los cierres explican un coste moderado y seriable.");
        s(m,"Jersey de punto V881",38,"Prenda de punto cerrada de 0,65 kg: consume bastante fibra pero tiene geometría repetible y pocos componentes adicionales.");
        s(m,"Gambesón V881",260,"Protección textil de 4,2 kg formada por muchas capas y relleno, cosidos densamente para evitar desplazamientos y patronados para conservar movilidad. Materia, horas de costura y ensamblaje multicapa explican el precio; sus estadísticas defensivas no lo calculan.");

        // OUTER
        s(m,"Levita V881",110,"Abrigo de sastrería largo con faldones, forro, cuello, mangas y numerosos paneles. La longitud y el acabado formal exigen bastante paño y trabajo especializado.");
        s(m,"Frac V881",135,"Prenda formal de patronaje complejo, delanteros recortados y faldones posteriores, forro y ajuste preciso. La sastrería especializada pesa más que su masa absoluta.");
        s(m,"Chaqué V881",125,"Chaqueta formal de faldones y líneas estructuradas, con forro, cierres y ajuste corporal. Requiere trabajo de sastrería fino y poca tolerancia a errores.");
        s(m,"Americana V881",85,"Chaqueta urbana estructurada con solapas, forro, mangas, bolsillos y cierres. Es sastrería común pero considerablemente más compleja que una camisa.");
        s(m,"Chaqueta Norfolk V881",92,"Chaqueta de exterior con cinturón, pliegues y bolsillos aplicados; añade piezas, costuras y refuerzos a una chaqueta ordinaria.");
        s(m,"Blusón de trabajo V881",42,"Prenda exterior laboral de corte amplio y tejido durable, con construcción sencilla y reparable. Prima la resistencia antes que el acabado.");
        s(m,"Gabán V881",145,"Abrigo largo y pesado de 2,8 kg que consume gran cantidad de paño, forro y cierres. Materia y confección de una prenda extensa dominan el coste.");
        s(m,"Sobretodo V881",130,"Abrigo exterior de 2,35 kg, largo, forrado y preparado para cubrir otras capas. Gran superficie textil y costuras largas justifican el precio.");
        s(m,"Ulster V881",165,"Abrigo de 3,1 kg particularmente voluminoso, con mucha superficie de paño, estructura, forro y protección frente a intemperie. Es la prenda exterior ordinaria más intensiva en materia.");
        s(m,"Guardapolvo V881",48,"Prenda larga pero ligera destinada a polvo y suciedad, de tejido relativamente fino y patronaje simple. La superficie es grande, pero la estructura mínima.");
        s(m,"Gabardina V881",115,"Abrigo de 1,65 kg con tejido tratado frente al agua, solapas, cinturón, cierres y construcción estratificada. Tratamiento y sastrería explican la prima.");
        s(m,"Chaqueta de montar V881",90,"Chaqueta estructurada para equitación, con corte que permite movilidad sentada, cierres firmes y acabado resistente al viento y al roce.");
        s(m,"Bolero V881",48,"Chaqueta muy corta y ligera cuya menor materia se compensa parcialmente por patronaje, mangas y acabado de sastrería.");
        s(m,"Capa del Caballero",95,"Capa amplia de 2 kg con mucha superficie de paño, cierre robusto y acabado durable. Su coste deriva de materia y confección, no de su asociación con una profesión.");
        s(m,"Capa del Viajero V881",72,"Capa de 1,2 kg concebida para viaje, con paño resistente, cierre y acabado frente a intemperie. Arquitectura sencilla pero gran superficie.");
        s(m,"Capa Inverness V881",140,"Prenda de 2,1 kg que combina abrigo y sobrecapa, aumentando paneles, superficie, costuras y patronaje respecto a una capa ordinaria.");
        s(m,"Poncho V881",42,"Pieza exterior de geometría simple y 1,1 kg de tejido; consume material pero exige muy poco patronaje y pocos cierres.");
        s(m,"Burnús V881",78,"Prenda amplia de 1,65 kg con capucha y gran superficie de tejido. Su construcción es relativamente sencilla pese al consumo material.");
        s(m,"Dolman V881",115,"Chaqueta de sastrería con cierres y ornamentación característica, múltiples detalles y ajuste estructurado. La mano de obra eleva el precio.");
        s(m,"Manteleta V881",52,"Prenda corta de hombros y torso superior, de 0,75 kg, con patronaje curvo, cierres y acabado más elaborado que un paño simple.");

        // MIDDLE protector: MEDIUM y HEAVY compiten exactamente por la misma ranura que el bloque MIDDLE LIGHT.
        s(m,"Chaqueta de Viaje V881",210,"Chaqueta protectora de 2,8 kg que combina cuero endurecido y textil. Curtido, endurecimiento, paneles gruesos, forro, costuras resistentes y patronaje para viaje justifican el coste.");
        s(m,"Chaqueta de Aeronauta V881",240,"Chaqueta protectora de 1,8 kg de cuero endurecido y tejido, ajustada para movilidad y uso técnico del Aeronauta. Selección de cuero, endurecimiento, paneles, forro, cierres y confección especializada explican el precio.");
        s(m,"Chaqueta cruzada de motorista V881",280,"Chaqueta de 3,4 kg con abundante cuero endurecido, cierre cruzado, refuerzos y forro. Masa de cuero, curtido, endurecimiento y costuras capaces de atravesar material grueso dominan el coste.");
        s(m,"Delantal de Taller V881",95,"Equipo profesional de cuero endurecido de 0,65 kg destinado a proteger el torso durante trabajo de taller. Curtido, espesor seleccionado, tratamiento superficial, correas y remaches justifican el precio.");
        p(m,"Coraza de Papel V881",420,"Coraza de 2,5 kg formada por papel grueso multicapa, compactado, prensado y tratado sobre soporte textil. El coste procede de gran cantidad de fibra, conformado, secado, barnizado y montaje; no de sus valores defensivos.");
        m.put("Coraza del Guerrero de Ébano",EbonyWarriorArmorEconomicPolicy.valuation("Coraza del Guerrero de Ébano"));
        m.put("Coraza del Guerrero de Ébano V881",EbonyWarriorArmorEconomicPolicy.valuation("Coraza del Guerrero de Ébano V881"));
        p(m,"Coraza de Caballero",5200,"Coraza histórica de 11,25 kg con gran masa de acero conformado, uniones, correas y soporte textil. Forja, tratamiento, ajuste tridimensional y ensamblaje especializado explican una inversión elevada.");
        p(m,"Coraza de Caballero incluido hombro V881",10500,"Sistema V881 de 17,361 kg que combina acero, wolframio y textil e integra protección de hombro en una arquitectura de placas compleja. Materiales densos, mecanizado, conformado, uniones y ajuste individual dominan el coste.");
        p(m,"Coraza Lamelar Histórica Pesada",6800,"Coraza pesada de 22,388 kg formada por numerosas láminas de acero enlazadas sobre soporte textil. La enorme cantidad de piezas repetidas, perforado, tratamiento, cordaje y horas de ensamblaje explican su coste.");

        Set<String> canonical=new LinkedHashSet<>();
        ArmorCatalog.allInnerChestGarments().forEach(a->canonical.add(a.name()));
        ArmorCatalog.allMiddleChest().forEach(a->canonical.add(a.name()));
        ArmorCatalog.allOuterChestGarments().forEach(a->canonical.add(a.name()));
        if(canonical.size()!=58) throw new IllegalStateException(" espera 58 CHEST canónicas.");
        if(!m.keySet().equals(canonical)) throw new IllegalStateException(" debe cubrir exactamente INNER + MIDDLE + OUTER CHEST.");
        return Map.copyOf(m);
    }

    private static void s(Map<String,EconomicValuation> m,String n,long v,String r){m.put(n,EconomicValuation.priced(n,EconomicGoodType.SOCIAL_INTEREST,v,r));}
    private static void p(Map<String,EconomicValuation> m,String n,long v,String r){m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));}
}
