package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — Noble deja la matriz automática: cada combinación activa representa una trayectoria de cúspide distinta. */
public final class NobleCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private NobleCanonicalProfiles(){}
    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null) throw new IllegalArgumentException("subprofesión sin catálogo Noble: "+s);
        var p=by.get(c); if(p==null) throw new IllegalArgumentException("combinación deprecated/no canónica: "+s+" / "+c); return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s)); if(p==null) throw new IllegalArgumentException("subprofesión sin catálogo Noble: "+s); return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s); Objects.requireNonNull(c); if(s.profession()!=Profession.NOBLE) throw new IllegalArgumentException("Profesión incorrecta."); return !DATA.get(s).containsKey(c);
    }
    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m=new EnumMap<>(Subprofession.class);
        put(m,Subprofession.DYNASTIC_NOBLE,CharacterClass.HERALDO,Gender.MUJER,75,30,75,28,56,62,58,75,67,
                "Nació dentro de una casa que ya poseía nombre, pero tuvo que aprender a convertir herencia en continuidad. Su vida está hecha de audiencias, pactos, genealogías, concesiones familiares y decisiones cuyo resultado se mide décadas después. Su autoridad procede de sostener relaciones que ya existían antes de ella.");
        putBoth(m,Subprofession.DYNASTIC_NOBLE,CharacterClass.MAESTRO,75,30,75,30,61,69,57,40,75,
                "Su trayectoria dentro de la casa fue distinta según el lugar que ocupó: uno de sus recorridos se apoyó en la representación visible de la continuidad y el otro en la arquitectura discreta de archivos, sucesiones y acuerdos privados. Ambos escalaron hasta convertir la continuidad dinástica en una capacidad integradora y no en un privilegio heredado. ");

        put(m,Subprofession.CONCESSIONARY_NOBLE,CharacterClass.APODERADO,Gender.MUJER,75,30,75,30,58,68,75,40,70,
                "Su casa obtuvo derechos sobre rutas, instalaciones y recursos, pero una concesión sólo existe mientras alguien la hace funcionar. Aprendió a coordinar operadores, juristas, comunidades y capital para que el territorio concedido produjera continuidad sin depender de su presencia diaria.");
        putBoth(m,Subprofession.CONCESSIONARY_NOBLE,CharacterClass.MAESTRO,75,30,75,30,63,70,60,40,75,
                "Una trayectoria llegó a dominar la infraestructura desde la obra y otra desde la continuidad institucional de concesiones que sobrevivieron a sus primeros titulares. Ambos aprendieron a intervenir antes de que una comunidad, una ruta o una instalación obligara a rehacer todo el sistema. ");

        putBoth(m,Subprofession.ENLIGHTENED_PATRON,CharacterClass.MAESTRO,75,30,75,25,61,70,60,40,75,
                "Uno aprendió a financiar campos científicos desde la lectura de sus estructuras y otro a decidir qué investigación debía sobrevivir a la moda intelectual y convertirse en institución. El Panóptico del Ilustrado resume la trayectoria compartida: observar perspectivas incompatibles antes de aceptar una explicación. ");

        put(m,Subprofession.PATRIMONIAL_WARLORD,CharacterClass.LUCHADOR,Gender.HOMBRE,75,40,75,75,64,61,48,50,68,
                "Su casa convirtió patrimonio en fuerza organizada. Aprendió a evaluar hombres, armas, rutas, crédito y suministros porque una guerra patrimonial no se gana con valentía individual: se gana evitando que una cadena logística se rompa antes de que llegue el enemigo.");
        putBoth(m,Subprofession.PATRIMONIAL_WARLORD,CharacterClass.MAESTRO,75,30,75,30,64,70,58,40,75,
                "Una trayectoria nació de la planificación de arsenales y otra de la administración de las consecuencias de guerras ajenas. Ambos escalaron hasta comprender abastecimiento, disciplina, inteligencia y desgaste como un único sistema de violencia financiada. ");

        put(m,Subprofession.PERMANENCE_PRETENDER,CharacterClass.INTELECTUAL,Gender.HOMBRE,75,40,75,23,55,75,55,49,72,
                "Escaló cada peldaño de la investigación sobre continuidad sin convertirse en un especialista aislado. Su patrimonio le permitió sobrevivir a décadas de hipótesis fallidas y aprender a reconocer qué parte del problema pertenecía a la ciencia y cuál a la identidad.");
        putBoth(m,Subprofession.PERMANENCE_PRETENDER,CharacterClass.MAESTRO,75,30,75,25,62,70,60,40,75,
                "Una trayectoria se construyó organizando investigadores, laboratorios y recuperadores; otra administró la continuidad cuando las teorías, cuerpos o patrimonios dejaron de coincidir. Ambos escalaron hasta integrar regeneración, epigenética, arquitectura neuronal, alma y trasvase sin confundir integración con posesión de la solución. ");
        put(m,Subprofession.STRATEGIC_COMMUNICATIONS_OFFICER,CharacterClass.INTELECTUAL,Gender.HOMBRE,75,40,75,24,61,75,48,50,72,
                "Llegó a la cúspide humana haciendo de la comunicación militar una disciplina territorial: líneas de visión, meteorología, tiempos de transmisión, puestos de observación y decisiones cuyo valor desaparece si llegan tarde.");
        put(m,Subprofession.FORENSIC_INVESTIGATOR,CharacterClass.INTELECTUAL,Gender.HOMBRE,75,40,75,22,62,75,52,50,75,
                "Aprendió a reconstruir hechos sin pedir al escenario que los explique. Fotografía, cronología nocturna y toxicología le permiten conservar indicios y enfrentar versiones incompatibles sin confundir evidencia con interpretación.");
        put(m,Subprofession.INTELLIGENCE_AGENT,CharacterClass.ESPECIALISTA,Gender.MUJER,75,30,75,24,75,68,48,40,75,
                "Convirtió observación, infiltración y contraespionaje en una carrera de élite. Trabaja con información privada: localizar una vibración o reconocer una condición oculta vale más cuando el observado todavía ignora que ha sido descubierto.");
        put(m,Subprofession.FIELD_ELECTROATMOSPHERIC_SPECIALIST,CharacterClass.INTELECTUAL,Gender.HOMBRE,75,40,75,25,62,75,46,48,75,
                "Su especialidad consiste en trabajar con fenómenos electroatmosféricos allí donde no existen las protecciones de un laboratorio. El entorno forma parte del circuito y saber cuándo no descargar es tan importante como saber hacerlo.");

        return Map.copyOf(m);
    }
    private static void putBoth(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m,Subprofession s,CharacterClass c,
                                int v,int a,int ad,int f,int d,int i,int fe,int car,int cl,String n){
        if(s.profession()!=Profession.NOBLE) throw new IllegalArgumentException("No Noble: "+s);
        m.computeIfAbsent(s,k->new EnumMap<>(CharacterClass.class)).put(c,
                new CanonicalSubprofessionProfile(s,c,Set.of(Gender.HOMBRE,Gender.MUJER),CharacterSheet.of(v,a,ad,f,d,i,fe,car,cl),n));
    }
    private static void put(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m,Subprofession s,CharacterClass c,Gender g,int v,int a,int ad,int f,int d,int i,int fe,int car,int cl,String n){
        if(s.profession()!=Profession.NOBLE) throw new IllegalArgumentException("No Noble: "+s);
        m.computeIfAbsent(s,k->new EnumMap<>(CharacterClass.class)).put(c,new CanonicalSubprofessionProfile(s,c,Set.of(g),CharacterSheet.of(v,a,ad,f,d,i,fe,car,cl),n));
    }
}
