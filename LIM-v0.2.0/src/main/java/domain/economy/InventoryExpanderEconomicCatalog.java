package domain.economy;

import domain.inventory.logistics.InventoryCompartmentType;
import java.util.*;

/**  — tasación de los diez expansores físicos de inventario. */
public final class InventoryExpanderEconomicCatalog {
    private static final Map<InventoryCompartmentType,EconomicValuation> DATA=build();
    private InventoryExpanderEconomicCatalog(){}

    public static EconomicValuation valuation(InventoryCompartmentType type){
        EconomicValuation v=DATA.get(Objects.requireNonNull(type));
        if(v==null) throw new IllegalArgumentException("Compartimento no tasado como expansor : "+type);
        return v;
    }
    public static Map<InventoryCompartmentType,EconomicValuation> all(){return DATA;}

    private static Map<InventoryCompartmentType,EconomicValuation> build(){
        EnumMap<InventoryCompartmentType,EconomicValuation> m=new EnumMap<>(InventoryCompartmentType.class);
        p(m,InventoryCompartmentType.LEG_POUCH,EconomicGoodType.SOCIAL_INTEREST,42,
                "Pernera modular de cuero y tejido reforzado con correas independientes, cierres y compartimentación rígida. Su tamaño pequeño contiene material, pero el ajuste al muslo y el acceso en movimiento exigen más confección que una bolsa simple.");
        p(m,InventoryCompartmentType.BANDOLIER,EconomicGoodType.SOCIAL_INTEREST,68,
                "Bandolera profunda de cuero reforzado, herrajes y divisiones de servicio. Mayor superficie, arnés cruzado y acceso frontal justifican una inversión superior a la pernera sin entrar en equipamiento técnico especializado.");
        p(m,InventoryCompartmentType.BACKPACK,EconomicGoodType.SOCIAL_INTEREST,110,
                "Mochila de expedición con arnés dorsal, cuerpo profundo, refuerzos y soporte exterior de casco. Material textil/cuero, costuras de carga y estructura interna elevan el coste frente a contenedores personales menores.");
        p(m,InventoryCompartmentType.DORSAL_ROTOR_SYSTEM,EconomicGoodType.PRIVATE_USE,950,
                "Armazón de 3,2 kg diseñado exclusivamente para transportar y desplegar un Espadón de Rotor retraído. Bastidor, anclajes, guías y tolerancias deben soportar un arma singular sin interferir con su mecanismo; es equipamiento especializado, no una mochila sobredimensionada.");
        p(m,InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE,EconomicGoodType.SOCIAL_INTEREST,135,
                "Par de alforjas de cuero equilibradas por puente de silla, con fondos y cierres preparados para quince kilogramos de contenido. La construcción bilateral y el curtido dominan el precio.");
        p(m,InventoryCompartmentType.SADDLEBAGS_HORSE_RACING,EconomicGoodType.SOCIAL_INTEREST,105,
                "Alforjas ligeras de carrera con poca profundidad, materiales seleccionados y fijación muy ceñida para reducir balanceo. Usan menos materia que las de monta, aunque requieren una confección precisa para no penalizar la zancada.");
        p(m,InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT,EconomicGoodType.SOCIAL_INTEREST,210,
                "Alforjas de gran formato para treinta kilogramos, con puente ancho, fondos estructurados y refuerzos capaces de repartir masa sobre un caballo pesado. Material y resistencia de costuras explican el máximo precio entre equipajes ecuestres.");
        p(m,InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY,EconomicGoodType.SOCIAL_INTEREST,145,
                "Pareja de bolsas de lona reforzada con respaldo semirrígido y herrajes que mantienen el contenido lejos de radios, rueda y talón. La adaptación mecánica al portaequipajes importa tanto como el tejido.");
        p(m,InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN,EconomicGoodType.SOCIAL_INTEREST,360,
                "Pareja de maletas rígidas de servicio con anclajes al subchasis, tapas superiores y estructura capaz de soportar vibración y veinte kilogramos de carga. Metal, cierres y herrajes de precisión elevan claramente el coste.");
        p(m,InventoryCompartmentType.ARROW_QUIVER,EconomicGoodType.SOCIAL_INTEREST,65,
                "Carcaj rígido ligero para doce flechas, con boca estable, protección del emplumado y fijación corporal. Su precio corresponde al continente durable; no incluye ninguna de las flechas almacenadas, que conservan su propia tasación independiente.");
        return Map.copyOf(m);
    }
    private static void p(Map<InventoryCompartmentType,EconomicValuation> m,InventoryCompartmentType t,EconomicGoodType g,long v,String r){
        m.put(t,EconomicValuation.priced(t.label(),g,v,r));
    }
}
