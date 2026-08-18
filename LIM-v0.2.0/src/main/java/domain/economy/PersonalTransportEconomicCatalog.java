package domain.economy;

import domain.inventory.logistics.*;
import java.util.*;

/**
 *  — economía de transporte personal.
 * Caballos se tasan por crianza, edad útil, entrenamiento y selección; vehículos por materia,
 * mecanizado, montaje y complejidad industrial. Equipaje y combustible se valoran aparte.
 */
public final class PersonalTransportEconomicCatalog {
    private static final Map<PersonalTransportType,EconomicValuation> DATA=build();
    private PersonalTransportEconomicCatalog(){}

    public static EconomicValuation valuation(PersonalTransportType type){
        EconomicValuation v=DATA.get(Objects.requireNonNull(type));
        if(v==null) throw new IllegalArgumentException("Transporte sin tasación : "+type);
        return v;
    }
    public static Map<PersonalTransportType,EconomicValuation> all(){return DATA;}

    private static Map<PersonalTransportType,EconomicValuation> build(){
        EnumMap<PersonalTransportType,EconomicValuation> m=new EnumMap<>(PersonalTransportType.class);
        p(m,PersonalTransportType.HORSE_LEISURE,EconomicGoodType.SOCIAL_INTEREST,12000,
                "Caballo adulto de monta equilibrada, sano, domado y entrenado para jornadas largas, copiloto y alforjas moderadas. El precio acumula años de crianza, alimentación, manejo veterinario y adiestramiento; no incluye silla, alforjas ni manutención futura.");
        p(m,PersonalTransportType.HORSE_RACING,EconomicGoodType.PRIVATE_USE,30000,
                "Montura seleccionada por conformación longilínea, respuesta y velocidad, con una tasa de descarte reproductivo y de entrenamiento mayor que la de un caballo de paseo. La prima procede de selección, crianza y preparación deportiva, no de su estadística de velocidad en gameplay.");
        p(m,PersonalTransportType.HORSE_DRAFT,EconomicGoodType.SOCIAL_INTEREST,18000,
                "Caballo pesado de gran sección ósea criado durante años para fuerza sostenida, estabilidad y tolerancia de carga. Requiere más alimento y tiempo de desarrollo que una montura ligera; su valor responde a capacidad laboral y entrenamiento, no a una fórmula de masa.");
        p(m,PersonalTransportType.BICYCLE_FOLDING_V881,EconomicGoodType.SOCIAL_INTEREST,4500,
                "Bicicleta V881 de bastidor articulado, ruedas compactas y suspensión tolerante a firme irregular. Acero de calidad, articulación de plegado, rodamientos y ajuste preciso elevan mucho el coste sobre una bicicleta civil simple; no incluye equipaje lateral porque canónicamente no lo admite.");
        p(m,PersonalTransportType.BICYCLE_MILITARY_V881,EconomicGoodType.SOCIAL_INTEREST,5800,
                "Bicicleta de servicio robusta con cuadro de acero, portaequipajes, fijaciones utilitarias y componentes seleccionados para mantenimiento de campaña. La mayor resistencia y estandarización militar justifican su precio; las Bolsas de Portaequipajes se compran aparte.");
        p(m,PersonalTransportType.MOTORCYCLE_CARDAN_V881,EconomicGoodType.PRIVATE_USE,55000,
                "Motocicleta pesada de 198 kg con motor, transmisión por cardán, bastidor reforzado, suspensión, frenos, instalación eléctrica y tolerancias de mecanizado muy superiores a las de una bicicleta. Es una inversión de capital individual considerable: combustible y Maletas Laterales Cardán se valoran de forma independiente.");
        return Map.copyOf(m);
    }
    private static void p(Map<PersonalTransportType,EconomicValuation> m,PersonalTransportType t,EconomicGoodType g,long v,String r){
        m.put(t,EconomicValuation.priced(t.label(),g,v,r));
    }
}
