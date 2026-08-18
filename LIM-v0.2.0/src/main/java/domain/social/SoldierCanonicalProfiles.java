package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — perfiles explícitos y minimizados de Soldado. */
public final class SoldierCanonicalProfiles {
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE = Map.ofEntries(
        Map.entry(Subprofession.V881_RIFLEMAN, Set.of(CharacterClass.LUCHADOR)),
        Map.entry(Subprofession.V881_CAMPAIGN_SAPPER, Set.of(CharacterClass.LUCHADOR, CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.V881_HEAVY_WEAPONS_SPECIALIST, Set.of(CharacterClass.LUCHADOR, CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.INSTITUTIONAL_SHOCK_COMBATANT, Set.of(CharacterClass.LUCHADOR, CharacterClass.INDOMITO)),
        Map.entry(Subprofession.KINGDOM_AGENT, Set.of(CharacterClass.HERALDO)),
        Map.entry(Subprofession.V881_SUPPORT_MARKSWOMAN, Set.of(CharacterClass.ESPECIALISTA)),
        Map.entry(Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN, Set.of(CharacterClass.APODERADO)),
        Map.entry(Subprofession.RAILWAY_GUARD, Set.of(CharacterClass.HERALDO, CharacterClass.ESPECIALISTA))
    );
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private SoldierCanonicalProfiles(){}

    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var p=profiles(s).get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("Perfil deprecated o ausente: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s));
        if(p==null) throw new IllegalArgumentException("No es Soldado : "+s);
        return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){return !ACTIVE.getOrDefault(s,Set.of()).contains(c);}
    public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){return profiles(s);}

    /** Compatibilidad nominal; devuelve el nivel del único perfil o el menor nivel activo. */
    public static int canonicalBaseLevel(Subprofession s){
        return profiles(s).values().stream().mapToInt(p->p.attributes().totalAttributeLevel()).min().orElseThrow();
    }

    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all=new EnumMap<>(Subprofession.class);
        put(all,Subprofession.V881_RIFLEMAN,CharacterClass.LUCHADOR,
            sheet(38,41,24,45,40,42,14,25,27),
            "Fusilero de línea: fuego reglamentario, lectura material del campo y capacidad para sostener una posición.",
            296);
        put(all,Subprofession.V881_CAMPAIGN_SAPPER,CharacterClass.LUCHADOR,
            sheet(43,45,35,46,36,41,15,24,25),
            "Zapador de campaña: fuerza de obra y resistencia para intervenir físicamente bajo amenaza.",
            310);
        put(all,Subprofession.V881_CAMPAIGN_SAPPER,CharacterClass.INTELECTUAL,
            sheet(41,43,36,39,38,48,15,25,26),
            "Zapador de campaña técnico: cálculo de estructuras, procedimientos y lectura de instalaciones.",
            311);
        put(all,Subprofession.V881_HEAVY_WEAPONS_SPECIALIST,CharacterClass.LUCHADOR,
            sheet(50,52,35,55,45,55,20,32,46),
            "Especialista de armas pesadas: masa corporal, control del sistema y cálculo de efectos.",
            390);
        put(all,Subprofession.V881_HEAVY_WEAPONS_SPECIALIST,CharacterClass.INTELECTUAL,
            sheet(50,48,32,45,45,60,22,30,58),
            "Especialista de armas pesadas técnico: balística, alimentación y empleo deliberado del sistema.",
            390);
        put(all,Subprofession.INSTITUTIONAL_SHOCK_COMBATANT,CharacterClass.LUCHADOR,
            sheet(50,52,31,55,45,37,16,23,25),
            "Combatiente de choque institucional: ruptura próxima, fuerza y disciplina de contacto.",
            334);
        put(all,Subprofession.INSTITUTIONAL_SHOCK_COMBATANT,CharacterClass.INDOMITO,
            sheet(48,55,34,50,47,36,16,22,25),
            "Combatiente de choque indómito: movilidad, aguante y supervivencia cuando la formación se rompe.",
            333);
        put(all,Subprofession.KINGDOM_AGENT,CharacterClass.HERALDO,
            sheet(34,36,27,27,42,38,18,49,27),
            "Agente del Reino: intervención armada, proporcionalidad, control y lectura social.",
            298);
        put(all,Subprofession.V881_SUPPORT_MARKSWOMAN,CharacterClass.ESPECIALISTA,
            sheet(34,37,31,27,55,43,17,30,39),
            "Tiradora de apoyo: precisión remota, discriminación de blancos y estabilidad de procedimiento.",
            313);
        put(all,Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN,CharacterClass.APODERADO,
            sheet(38,41,34,29,53,46,23,31,38),
            "Custodia de instalación estratégica: protocolos, compartimentación y respuesta ante anomalías.",
            333);
        put(all,Subprofession.RAILWAY_GUARD,CharacterClass.HERALDO,
            sheet(35,38,31,28,43,40,17,49,31),
            "Guardia ferroviaria de trato público: vigilancia, movilidad y respuesta en corredores.",
            312);
        put(all,Subprofession.RAILWAY_GUARD,CharacterClass.ESPECIALISTA,
            sheet(35,38,34,28,50,40,17,39,31),
            "Guardia ferroviaria de precisión: control de accesos, vigilancia técnica y respuesta discriminada.",
            312);
        return Map.copyOf(all);
    }

    private static void put(Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all,
                            Subprofession s,CharacterClass c,CharacterSheet sheet,String rationale,int expectedLevel){
        if(sheet.totalAttributeLevel()!=expectedLevel)
            throw new IllegalStateException("Nivel explícito incorrecto en "+s+"/"+c+": "+sheet.totalAttributeLevel()+" != "+expectedLevel);
        all.computeIfAbsent(s,k->new EnumMap<>(CharacterClass.class))
           .put(c,new CanonicalSubprofessionProfile(s,c,genders(c),sheet,rationale));
    }
    private static Set<Gender> genders(CharacterClass c){
        return switch(c){
            case LUCHADOR,INTELECTUAL,INDOMITO->Set.of(Gender.HOMBRE);
            case ESPECIALISTA,APODERADO,HERALDO->Set.of(Gender.MUJER);
            case MAESTRO->Set.of(Gender.HOMBRE,Gender.MUJER);
        };
    }
    private static CharacterSheet sheet(int a,int b,int c,int d,int e,int f,int g,int h,int i){
        return CharacterSheet.of(a,b,c,d,e,f,g,h,i);
    }
}
