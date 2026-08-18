package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — perfiles canónicos explícitos de Maestro, sin affinityGain ni matriz automática de seis clases. */
public final class TeacherCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private TeacherCanonicalProfiles(){}
    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null) throw new IllegalArgumentException("subprofesión sin catálogo Maestro: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("combinación deprecated/no canónica: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s));
        if(p==null) throw new IllegalArgumentException("subprofesión sin catálogo Maestro: "+s);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){return profiles(s);}
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.TEACHER)throw new IllegalArgumentException("Profesión incorrecta.");
        return !DATA.get(s).containsKey(c);
    }
    public static String deprecationReason(Subprofession s,CharacterClass c){
        return isDeprecated(s,c)?"La combinación no representa una biografía canónica suficientemente diferenciada.":"";
    }

    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m=new EnumMap<>(Subprofession.class);
        put(m,Subprofession.KINGDOM_MESSENGER,CharacterClass.INTELECTUAL,Gender.HOMBRE,31,39,28,24,38,48,14,34,30,
                "Custodia rutas, autenticidad, protocolos y continuidad de información entre instituciones.");
        put(m,Subprofession.CYCLIST_MESSENGER,CharacterClass.INDOMITO,Gender.HOMBRE,37,50,28,31,43,31,12,30,24,
                "Convierte su propio organismo en el último tramo de una red física y vive de la repetición, la carga y la custodia.");
        put(m,Subprofession.FREQUENCY_PHYSICIAN,CharacterClass.ESPECIALISTA,Gender.MUJER,27,31,35,18,52,48,20,34,36,
                "Integra anatomía, fisiología y firmas frecuenciales mediante una práctica clínica de precisión.");
        put(m,Subprofession.SURGEON,CharacterClass.LUCHADOR,Gender.HOMBRE,34,37,30,29,58,54,18,31,35,
                "Interviene directamente sobre tejido y estructuras anatómicas bajo presión y decisiones irreversibles.");
        put(m,Subprofession.VETERINARIAN,CharacterClass.APODERADO,Gender.MUJER,32,35,34,24,47,48,21,43,31,
                "Alterna entre especies, propietarios y patrimonio animal, integrando medicina comparada y gestión del riesgo.");
        put(m,Subprofession.FREQUENCY_RESEARCHER,CharacterClass.INTELECTUAL,Gender.HOMBRE,30,31,36,19,40,59,18,27,45,
                "Diseña controles, reproduce patrones y separa coincidencia de regularidad frecuencial.");

        put(m,Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER,CharacterClass.INTELECTUAL,Gender.HOMBRE,31,35,31,24,39,60,17,29,42,
                "Diseña redes de captación electroatmosférica y calcula energía, ionización, geometría y materiales.");
        put(m,Subprofession.ELECTROATMOSPHERIC_CAPTATION_ENGINEER,CharacterClass.ESPECIALISTA,Gender.MUJER,29,33,35,22,55,48,16,28,40,
                "Ajusta superficies captadoras, sensores y conexiones de campo para convertir el fenómeno atmosférico en potencia utilizable.");
        pair(m,Subprofession.ELECTROATMOSPHERIC_SAFETY_ENGINEER,
                new int[]{32,36,34,26,44,56,22,31,51},new int[]{29,34,39,21,45,55,31,45,54},
                "El Maestro hombre supervisa rutas de descarga, aislamiento y zonas de exclusión y decide cuándo detener una red.",
                "La Maestra mujer audita consecuencias territoriales, coordinación institucional y protocolos de reapertura tras una anomalía.");

        put(m,Subprofession.ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER,CharacterClass.INTELECTUAL,Gender.HOMBRE,32,35,31,27,42,60,17,30,41,
                "Diseña locomotoras y sistemas de alimentación, guiado y frenado electromagnético.");
        pair(m,Subprofession.RAILWAY_INFRASTRUCTURE_ENGINEER,
                new int[]{34,38,34,27,43,57,24,31,50},new int[]{30,35,40,20,45,56,32,46,53},
                "El Maestro hombre diseña corredores, estaciones, alimentación y mantenimiento de una red ferroviaria.",
                "La Maestra mujer coordina continuidad territorial, seguridad de estaciones y expansión de la red hacia nuevos asentamientos.");
        put(m,Subprofession.ELECTROMAGNETIC_TRANSPORT_PLANNER,CharacterClass.HERALDO,Gender.MUJER,29,34,38,21,45,55,29,48,47,
                "Planifica qué aparece alrededor de una línea estable: almacenes, talleres, población, suministro y autoridad.");

        pair(m,Subprofession.SANITARY_MASTER,
                new int[]{33,36,31,23,43,56,21,39,47},new int[]{30,34,36,20,45,55,29,45,48},
                "El Maestro hombre supervisa redes de agua, tratamiento y contaminación y decide cuándo aislar un sistema.",
                "La Maestra mujer coordina inspección sanitaria, prevención poblacional y seguimiento de usuarios tras una anomalía.");
        put(m,Subprofession.FORESTRY_MANAGER,CharacterClass.APODERADO,Gender.MUJER,33,38,34,25,39,49,23,46,32,
                "Administra extracción, regeneración, incendios, suelo, agua y accesos sin confundir rentabilidad inmediata con continuidad.");
        put(m,Subprofession.PROSPECTOR,CharacterClass.INDOMITO,Gender.HOMBRE,36,45,35,30,42,48,16,29,38,
                "Explora terreno, toma muestras e interpreta indicios antes de que exista una explotación.");

        pair(m,Subprofession.REGENERATIONIST,
                new int[]{36,40,39,21,48,62,24,31,47},new int[]{31,37,43,18,49,58,29,43,51},
                "El Maestro hombre interviene sobre organismos con historias biológicas acumuladas y distingue lesión de adaptación útil.",
                "La Maestra mujer sigue la continuidad clínica durante años y vigila que la restauración no borre adaptaciones estables.");
        pair(m,Subprofession.CONTINUITY_EPIGENETICIST,
                new int[]{35,40,41,19,43,65,24,30,51},new int[]{31,37,45,17,44,61,30,41,54},
                "El Maestro hombre analiza expresión, memoria epigenética y linajes celulares como una única historia material.",
                "La Maestra mujer sigue la continuidad generacional y compara desviaciones pequeñas cuya importancia aparece tras muchos ciclos.");
        pair(m,Subprofession.NEUROARCHITECT,
                new int[]{34,38,43,18,44,68,25,32,55},new int[]{30,36,47,16,45,63,31,45,58},
                "El Maestro hombre mapea conectividad, plasticidad, memoria y hábitos acumulados durante vidas extraordinariamente largas.",
                "La Maestra mujer estudia identidad autobiográfica y conducta cuando la arquitectura neuronal deja de admitir una normalidad estadística simple.");
        pair(m,Subprofession.SOUL_RESEARCHER,
                new int[]{33,37,45,17,42,69,29,32,59},new int[]{29,35,49,15,43,64,35,45,62},
                "El Maestro hombre compara firmas álmicas y desacoplamientos mediante protocolos experimentales.",
                "La Maestra mujer estudia continuidad e identidad cuando cuerpo y memoria dejan de explicar por sí solos la presencia de una persona.");
        pair(m,Subprofession.SOUL_TRANSFUSIONIST,
                new int[]{34,39,47,18,52,70,30,33,61},new int[]{30,37,51,16,50,65,36,46,64},
                "El Maestro hombre ejecuta trasvases y afronta compatibilidad biológica, memoria e identidad en procedimientos irreversibles.",
                "La Maestra mujer controla compatibilidad, estabilización posterior y continuidad de identidad después del cambio de soporte.");
        pair(m,Subprofession.SILICIC_METAMORPHOSIS_RESEARCHER,
                new int[]{36,41,49,20,47,72,31,34,64},new int[]{31,38,53,17,46,67,38,47,67},
                "El Maestro hombre investiga la transición desde carbono-12 hacia una arquitectura silícica estable.",
                "La Maestra mujer estudia la estabilidad de identidad durante la metamorfosis y las condiciones de continuidad personal.");
        pair(m,Subprofession.PERMANENCE_RESEARCHER,
                new int[]{37,42,51,19,46,73,32,35,68},new int[]{31,39,55,16,45,68,39,48,71},
                "El Maestro hombre investiga persistencia material cuando la identidad deja de depender de un único soporte.",
                "La Maestra mujer estudia persistencia de identidad y correlaciones externas que pueden permanecer en objetos o lugares.");
        pair(m,Subprofession.ENLIGHTENED,
                new int[]{38,43,53,20,47,74,35,37,73},new int[]{32,40,57,17,46,69,42,50,76},
                "El Maestro hombre explora Intersticio, líneas telúricas y hendiduras des-veladas desde una disciplina experimental de campo.",
                "La Maestra mujer estudia filtros de realidad, continuidad espacial y percepción anómala desde una práctica de observación prolongada.");

        if(m.size()!=Subprofession.forProfession(Profession.TEACHER).size())
            throw new IllegalStateException("faltan subprofesiones Maestro.");
        return Map.copyOf(m);
    }

    private static void put(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m,Subprofession s,CharacterClass c,Gender g,
                            int v,int a,int ad,int f,int d,int i,int fe,int car,int cl,String narrative){
        if(s.profession()!=Profession.TEACHER)throw new IllegalArgumentException("No es Maestro: "+s);
        var by=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
        by.put(c,new CanonicalSubprofessionProfile(s,c,Set.of(g),sheet(v,a,ad,f,d,i,fe,car,cl),
                narrative+" La clase expresa esta biografía concreta y no una fórmula de afinidad."));
        m.put(s,Map.copyOf(by));
    }

    private static void pair(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m,Subprofession s,int[] male,int[] female,String mn,String fn){
        if(s.profession()!=Profession.TEACHER)throw new IllegalArgumentException("No es Maestro: "+s);
        // CharacterClass.MAESTRO representa deliberadamente ambos sexos. La narrativa y el patrimonio
        // de  deben conservar dos biografías diferenciadas dentro de esta compatibilidad demográfica.
        String narrative=mn+" "+fn+" La clase MAESTRO no tiene afinidad de género: ambos sexos pueden ocupar esta subprofesión, pero no se consideran la misma biografía.";
        var by=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
        // Se utiliza la hoja masculina como hoja mecánica canónica de la combinación; la variante femenina
        // queda expresada como segunda biografía narrativa y será materializada en el patrimonio diferenciado.
        by.put(CharacterClass.MAESTRO,new CanonicalSubprofessionProfile(s,CharacterClass.MAESTRO,
                Set.of(Gender.HOMBRE,Gender.MUJER),sheet(male),narrative));
        m.put(s,Map.copyOf(by));
    }
    private static CharacterSheet sheet(int[] a){return CharacterSheet.of(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8]);}
    private static CharacterSheet sheet(int v,int a,int ad,int f,int d,int i,int fe,int car,int cl){return CharacterSheet.of(v,a,ad,f,d,i,fe,car,cl);}
}
