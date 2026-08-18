package domain.inventory.logistics;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;

/**
 * : presupuesto de carga de las alforjas después de reservar la plaza física y la carga
 * de un copiloto. La reserva canónica de auditoría es 75 kg; el resultado no sustituye los
 * límites de carga dinámica del personaje ni pretende modelar homologaciones reales V881.
 */
public final class PersonalTransportPayloadPolicy {
    public static final double COPILOT_RESERVE_KG = 75.0;
    private static final Map<PersonalTransportType, Double> GROSS_AUXILIARY_BUDGET_KG = build();
    private PersonalTransportPayloadPolicy() {}

    private static Map<PersonalTransportType,Double> build(){
        EnumMap<PersonalTransportType,Double> m=new EnumMap<>(PersonalTransportType.class);
        m.put(PersonalTransportType.HORSE_LEISURE,90.0);      // 15 kg tras reservar copiloto
        m.put(PersonalTransportType.HORSE_RACING,81.0);       // 6 kg: prioridad absoluta a ligereza
        m.put(PersonalTransportType.HORSE_DRAFT,105.0);       // 30 kg: mayor capacidad sin tratarlo como animal de carga
        m.put(PersonalTransportType.BICYCLE_MILITARY_V881,85.0); // 10 kg manteniendo margen de control y plaza trasera
        m.put(PersonalTransportType.MOTORCYCLE_CARDAN_V881,95.0); // 20 kg: 10 kg por maleta lateral
        return Map.copyOf(m);
    }

    public static OptionalDouble saddlebagContentsLimitKgWithCopilot(PersonalTransportType type){
        Objects.requireNonNull(type);
        Double gross=GROSS_AUXILIARY_BUDGET_KG.get(type);
        return gross==null ? OptionalDouble.empty() : OptionalDouble.of(gross-COPILOT_RESERVE_KG);
    }

    public static OptionalDouble grossAuxiliaryBudgetKg(PersonalTransportType type){
        Objects.requireNonNull(type); Double gross=GROSS_AUXILIARY_BUDGET_KG.get(type);
        return gross==null ? OptionalDouble.empty() : OptionalDouble.of(gross);
    }
}
