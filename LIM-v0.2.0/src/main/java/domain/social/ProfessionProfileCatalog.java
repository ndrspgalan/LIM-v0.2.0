package domain.social;

import java.util.*;

/**
 * : contexto canónico de las 19 profesiones después de la Primera Marcha Exaltada.
 * El verdadero detalle histórico se reserva a las subprofesiones.
 *
 * Ancla práctica: 1 Sueldo = 1.000 Valeritas = remuneración mensual de un puesto medio.
 * 1 Berylare = 210 Sueldos = 210.000 Valeritas.
 */
public final class ProfessionProfileCatalog {
    public static final int VALERITAS_PER_SUELDO=1_000;
    public static final int SUELDOS_PER_BERYLARE=210;
    public static final int VALERITAS_PER_BERYLARE=VALERITAS_PER_SUELDO*SUELDOS_PER_BERYLARE;

    private static final EnumMap<Profession,ProfessionProfile> DATA=build();
    private ProfessionProfileCatalog(){}

    public static ProfessionProfile profile(Profession profession){
        Profession canonical=Profession.canonicalOrBeggar(profession);
        ProfessionProfile p=DATA.get(canonical);
        if(p==null) throw new IllegalArgumentException("Profesión sin perfil : "+canonical);
        return p;
    }

    public static Map<Profession,ProfessionProfile> all(){ return Map.copyOf(DATA); }

    private static EnumMap<Profession,ProfessionProfile> build(){
        EnumMap<Profession,ProfessionProfile> m=new EnumMap<>(Profession.class);

        putSalary(m,Profession.EBONY_WARRIOR,2.00,
                "Después de la Primera Marcha Exaltada la antigua profesión dejó de existir como institución. Su reaparición bajo estándar V881 comienza con Kenan y vuelve a definirla como una casta militar excepcionalmente pequeña, limitada tanto por selección como por la casi extinción del ébano y por una manufactura que no puede reproducirse en masa. Su referencia económica corresponde a una élite profesional, no a una tropa ordinaria.");

        putVariable(m,Profession.MERCHANT,1.35,
                "La reconstrucción posterior a la Primera Marcha Exaltada multiplicó las diferencias entre lugares que producen y lugares que necesitan. Comerciante abarca desde el tendero que divide mercancía para una familia hasta industriales, armadores, financistas y concesionarios capaces de orientar rutas, inversión y desarrollo territorial. La referencia de 1,35 Sueldos describe al comerciante común: cuando capital, logística y derechos de explotación se concentran, la profesión se convierte en una de las bases de la burguesía V881.");

        putVariable(m,Profession.COURTESAN,1.30,
                "La recuperación de los núcleos urbanos devolvió rápidamente valor económico a la compañía, la representación y la sociabilidad privada. Una cortesana puede vivir desde una posición modesta hasta frecuentar clientelas extraordinariamente ricas; reputación, educación, apariencia, discreción y red social explican una dispersión de ingresos mucho mayor que la de un asalariado ordinario.");

        putVariable(m,Profession.MERCENARY,1.50,
                "El final de los Guerreros de Ébano y el boom tecnológico posterior a la Primera Marcha Exaltada revalorizaron de forma radical al profesional independiente. Mercenario no significa únicamente combatiente: puede vender seguridad, campaña, escolta, cantería, carpintería, recuperación, construcción, mantenimiento u otra capacidad contractual. Las grandes compañías llegaron incluso a producir gestores capaces de actuar como auténticos señores de la guerra; la referencia base, sin embargo, describe al profesional común a sueldo.");

        putNone(m,Profession.BEGGAR,
                "Categoría residual para quien no participa todavía, ya no participa o no consigue participar de una profesión remunerada. Incluye mendigos propiamente dichos, desempleados, presos sin oficio ejerciente, niños y adolescentes, dependientes, incapacitados, personas expulsadas de su actividad y también a quienes rehúsan o no logran asumir una responsabilidad productiva estable. Después de la Primera Marcha Exaltada esta población constituye el recordatorio más directo de que reconstrucción y prosperidad no alcanzaron a todos por igual.");

        putRent(m,Profession.NOBLE,VALERITAS_PER_BERYLARE,"1 Berylare/mes",
                "La nobleza no se define por recibir un salario, sino por controlar patrimonio, derechos, tierras, capital, conocimiento financiable, fuerza delegable y redes políticas. La Primera Marcha Exaltada destruyó fortunas y linajes, pero quienes conservaron suficiente estructura ascendieron junto al boom V881 hasta operar en una escala que para la población ordinaria puede parecer casi divina. Un Berylare mensual sigue siendo sólo la referencia mínima de una profesión cuya verdadera ventaja es poder acumular patrimonio, influencia y tiempo durante períodos que la economía laboral no puede imitar. Noble tampoco constituye una facción ni presupone alianza, ideología o linaje compartidos: designa la cúspide a la que llega una trayectoria humana excepcional. Algunos convierten herencia en continuidad, otros capital en territorio o fuerza, y otros una especialidad en una capacidad que muy pocos pueden reproducir. Dos nobles pueden perseguir fines incompatibles y seguir perteneciendo a la misma categoría porque lo común no es aquello que desean, sino haber escalado hasta el límite humano de lo posible.");

        putSalary(m,Profession.SOLDIER,1.10,
                "La Primera Marcha Exaltada obligó a rehacer por completo armamento, doctrina y organización. El soldado contemporáneo es el agente armado ordinario del Reino: patrulla, trata con civiles, mantiene el orden y puede marchar a la guerra cuando corresponde. Dispone de una renta algo superior al trabajo medio por disciplina, disponibilidad y exposición al riesgo, pero sigue siendo un profesional institucional común, no una élite.");

        putSalary(m,Profession.BLACKSMITH,1.20,
                "El boom tecnológico convirtió al herrero cualificado en una pieza central de talleres, infraestructura, armas, mecanismos y reparación. Acero, bronce, herramientas y control térmico elevan la barrera de entrada y permiten una renta superior a la media. La profesión base abarca desde forja convencional hasta talleres capaces de colaborar con manufacturas V881, cuya verdadera especialización se define después.");

        putSalary(m,Profession.CARPENTER,1.00,
                "La reconstrucción mantuvo una demanda estable de estructuras, mobiliario, soportes, mecanismos de madera y reparación. Es uno de los oficios que mejor representan el centro económico del Reino: cualificado, necesario y suficientemente extendido para que su salario mensual coincida con el Sueldo de referencia.");

        putVariable(m,Profession.FAIRGROUND_WORKER,0.80,
                "Ferias, mercados móviles, espectáculos y pequeños servicios reaparecieron allí donde la reconstrucción consiguió generar excedentes. El feriante depende mucho más que otros trabajadores del tránsito de personas, la temporada y la capacidad de atraer público, por lo que su ingreso medio es modesto y variable aunque determinados negocios puedan prosperar con rapidez.");

        putSalary(m,Profession.TEACHER,1.20,
                "Maestro designa el trabajo intelectual especializado del Reino: profesores, académicos, instructores, preceptores, médicos, cirujanos, veterinarios, investigadores, determinados ingenieros y técnicos superiores, especialistas de un oficio e incluso mensajeros reconocidos. Tras la Primera Marcha Exaltada, conservar, aplicar y transportar conocimiento se volvió parte de la reconstrucción material. La renta base es cómoda, aunque la subprofesión puede desplazarla desde estratos modestos hasta instituciones privilegiadas.");

        putSalary(m,Profession.JURIST,1.60,
                "Jurista reúne profesiones que convierten hechos, derechos, patrimonio y obligaciones en decisiones formalmente reconocidas: jueces, políticos, administradores, abogados, escribanos, contables, auditores y muchas otras especialidades. Herencias, desaparecidos, propiedades abandonadas, contratos y nuevas concesiones posteriores a la Marcha elevaron mucho su demanda y su barrera de formación.");

        putVariable(m,Profession.HUNTER,1.05,
                "La perturbación territorial posterior a la Primera Marcha Exaltada mantuvo valioso a quien puede obtener alimento, pieles, trofeos y recursos lejos de circuitos urbanos. Su renta fluctúa con capturas y encargos, pero la autosuficiencia y la utilidad de su conocimiento compensan parcialmente esa irregularidad.");

        putSalary(m,Profession.SAILOR,1.05,
                "El comercio marítimo continúa uniendo regiones cuya reconstrucción y producción avanzaron a ritmos distintos. El marinero percibe algo más que el trabajo medio por ausencias prolongadas, riesgo físico y dependencia de rutas, puertos y propietarios. El desarrollo V881 no eliminó el mar como infraestructura económica; simplemente cambió qué tecnologías compiten alrededor de él.");

        putSalary(m,Profession.TANNER,0.85,
                "Cuero, correajes, calzado, recipientes y capas protectoras mantienen al curtidor dentro de una cadena productiva constante. Su oficio es útil y estable, pero físicamente desagradable y con poco prestigio social, de modo que su posición económica suele quedar por debajo de otros artesanos igualmente especializados.");

        putVariable(m,Profession.DRESSMAKER,0.90,
                "Vestir continúa siendo una necesidad universal y, a la vez, un mercado de diferenciación social. La confección ordinaria deja márgenes modestos; trabajar a medida para militares, comerciantes, cortesanas o nobles puede transformar completamente el ingreso del mismo oficio. La referencia base representa un taller común, no su techo económico.");

        putVariable(m,Profession.HAIRDRESSER,0.80,
                "El peluquero vende mantenimiento personal, apariencia y presentación social, bienes cuya demanda regresó con la normalización de la vida urbana. La barrera material del oficio es baja y eso contiene el ingreso medio, aunque clientela estable, reputación y proximidad a estratos acomodados pueden elevarlo notablemente.");

        putSalary(m,Profession.STONEMASON,1.00,
                "Fortificaciones, edificios, caminos, talleres e infraestructura mantuvieron al cantero ocupado durante la reconstrucción. Es un oficio físicamente exigente y difícil de improvisar, pero suficientemente extendido para situarse alrededor del Sueldo mensual de referencia. Sus especialidades posteriores explican las diferencias entre obra ordinaria, ingeniería y contratación mercenaria.");

        putSalary(m,Profession.DAY_LABORER,0.55,
                "El jornalero vende fuerza de trabajo donde haga falta sin disponer necesariamente de un oficio que eleve su poder de negociación. La reconstrucción posterior a la Primera Marcha Exaltada garantiza demanda frecuente, pero no continuidad, patrimonio ni protección frente a temporadas malas. Es el estrato remunerado más próximo a caer en la categoría de Mendigo cuando el trabajo desaparece.");

        if(m.size()!=Profession.values().length)
            throw new IllegalStateException(" debe perfilar exactamente las 19 profesiones.");
        return m;
    }

    private static void putSalary(EnumMap<Profession,ProfessionProfile> m,Profession p,double sueldos,String narrative){
        int valeritas=(int)Math.round(sueldos*VALERITAS_PER_SUELDO);
        put(m,p,narrative,ProfessionIncomeKind.SALARY,valeritas,formatSueldos(sueldos)+"/mes");
    }

    private static void putVariable(EnumMap<Profession,ProfessionProfile> m,Profession p,double sueldos,String narrative){
        int valeritas=(int)Math.round(sueldos*VALERITAS_PER_SUELDO);
        put(m,p,narrative,ProfessionIncomeKind.VARIABLE_INCOME,valeritas,formatSueldos(sueldos)+"/mes");
    }

    private static void putRent(EnumMap<Profession,ProfessionProfile> m,Profession p,int valeritas,String label,String narrative){
        put(m,p,narrative,ProfessionIncomeKind.PATRIMONIAL_RENT,valeritas,label);
    }

    private static void putNone(EnumMap<Profession,ProfessionProfile> m,Profession p,String narrative){
        put(m,p,narrative,ProfessionIncomeKind.NONE,0,"0 Valeritas/mes");
    }

    private static void put(EnumMap<Profession,ProfessionProfile> m,Profession p,String narrative,
                            ProfessionIncomeKind kind,int valeritas,String label){
        m.put(p,new ProfessionProfile(p,narrative,kind,valeritas,label));
    }

    private static String formatSueldos(double value){
        if(Math.abs(value-Math.rint(value))<1e-9) return ((int)Math.rint(value))+" Sueldo";
        return String.format(java.util.Locale.ROOT,"%.2f Sueldos",value).replace('.',',');
    }
}
