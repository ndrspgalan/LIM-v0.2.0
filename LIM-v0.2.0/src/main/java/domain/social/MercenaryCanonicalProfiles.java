package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — perfiles explícitos y minimizados de Mercenario. */
public final class MercenaryCanonicalProfiles {
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE = Map.ofEntries(
        Map.entry(Subprofession.COMPANY_CONTRACTOR,Set.of(CharacterClass.LUCHADOR,CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.CONTRACTUAL_SHOCK_COMBATANT,Set.of(CharacterClass.LUCHADOR)),
        Map.entry(Subprofession.CONVOY_ESCORT,Set.of(CharacterClass.INDOMITO)),
        Map.entry(Subprofession.EXCEPTIONAL_ASSET_RECOVERER,Set.of(CharacterClass.LUCHADOR,CharacterClass.INDOMITO)),
        Map.entry(Subprofession.MERCENARY_COMPANY_DIRECTOR,Set.of(CharacterClass.INTELECTUAL)),
        Map.entry(Subprofession.MOTORCYCLE_COURIER,Set.of(CharacterClass.ESPECIALISTA)),
        Map.entry(Subprofession.FRONTIER_SKIRMISHER,Set.of(CharacterClass.ESPECIALISTA)),
        Map.entry(Subprofession.MOBILE_ESCORT,Set.of(CharacterClass.APODERADO,CharacterClass.HERALDO)),
        Map.entry(Subprofession.TECHNICAL_RECOVERY_OPERATOR,Set.of(CharacterClass.ESPECIALISTA,CharacterClass.APODERADO)),
        Map.entry(Subprofession.SABOTAGE_DENIAL_SPECIALIST,Set.of(CharacterClass.ESPECIALISTA,CharacterClass.APODERADO))
    );
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private MercenaryCanonicalProfiles(){}

    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var p=profiles(s).get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("Perfil deprecated o ausente: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s));
        if(p==null) throw new IllegalArgumentException("No es Mercenario : "+s);
        return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){return !ACTIVE.getOrDefault(s,Set.of()).contains(c);}
    public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){return profiles(s);}
    public static int canonicalBaseLevel(Subprofession s){
        return profiles(s).values().stream().mapToInt(p->p.attributes().totalAttributeLevel()).min().orElseThrow();
    }

    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all=new EnumMap<>(Subprofession.class);
        put(all,Subprofession.COMPANY_CONTRACTOR,CharacterClass.LUCHADOR,
            sheet(42,44,38,48,40,40,21,32,31),
            "Contratista de compañía: capacidad material completa para convertir una especialidad transportable en servicio.",
            336);
        put(all,Subprofession.COMPANY_CONTRACTOR,CharacterClass.INTELECTUAL,
            sheet(41,43,39,43,41,48,21,34,31),
            "Contratista técnico: presupuestos, procedimientos y resolución contractual de problemas materiales.",
            341);
        put(all,Subprofession.CONTRACTUAL_SHOCK_COMBATANT,CharacterClass.LUCHADOR,
            sheet(50,52,39,56,48,40,21,29,31),
            "Choque contractual: ruptura próxima y supervivencia bajo carga.",
            366);
        put(all,Subprofession.CONVOY_ESCORT,CharacterClass.INDOMITO,
            sheet(45,52,38,47,44,39,20,36,33),
            "Escolta de convoy: marcha, reacción armada y protección de sistemas móviles.",
            354);
        put(all,Subprofession.EXCEPTIONAL_ASSET_RECOVERER,CharacterClass.LUCHADOR,
            sheet(49,51,48,46,51,53,24,38,48),
            "Recuperador de campo: extracción física de activos excepcionales y autonomía extrema.",
            408);
        put(all,Subprofession.EXCEPTIONAL_ASSET_RECOVERER,CharacterClass.INDOMITO,
            sheet(47,55,50,44,52,52,24,38,49),
            "Recuperador de rastreo: localización, adaptación y extracción en terreno hostil.",
            411);
        put(all,Subprofession.MERCENARY_COMPANY_DIRECTOR,CharacterClass.INTELECTUAL,
            sheet(42,44,44,35,40,57,27,58,48),
            "Director de compañía: mando contractual, logística, negociación y criterio estratégico.",
            395);
        put(all,Subprofession.MOTORCYCLE_COURIER,CharacterClass.ESPECIALISTA,
            sheet(40,46,40,30,54,41,19,35,33),
            "Correo motociclista: movilidad, conducción, mantenimiento y custodia rápida.",
            338);
        put(all,Subprofession.FRONTIER_SKIRMISHER,CharacterClass.ESPECIALISTA,
            sheet(38,43,42,29,55,43,18,32,43),
            "Hostigadora de frontera: tiro remoto, reconocimiento y control de distancia.",
            343);
        put(all,Subprofession.MOBILE_ESCORT,CharacterClass.APODERADO,
            sheet(41,45,39,31,49,42,20,41,36),
            "Escolta móvil: protección inmediata y decisión práctica sobre personas y recursos.",
            344);
        put(all,Subprofession.MOBILE_ESCORT,CharacterClass.HERALDO,
            sheet(40,44,39,31,48,42,20,45,37),
            "Escolta móvil de representación: protección y gestión de la relación con el protegido.",
            346);
        put(all,Subprofession.TECHNICAL_RECOVERY_OPERATOR,CharacterClass.ESPECIALISTA,
            sheet(43,47,50,31,55,57,24,39,49),
            "Operadora de recuperación técnica: identificar, asegurar, manipular y extraer.",
            395);
        put(all,Subprofession.TECHNICAL_RECOVERY_OPERATOR,CharacterClass.APODERADO,
            sheet(43,47,48,31,53,58,26,40,48),
            "Operadora de recuperación técnica patrimonial: asegurar el activo, custodiarlo y gestionar su entrega.",
            394);
        put(all,Subprofession.SABOTAGE_DENIAL_SPECIALIST,CharacterClass.ESPECIALISTA,
            sheet(40,44,48,29,57,52,21,35,45),
            "Especialista de sabotaje y negación: precisión, adaptabilidad y conocimiento técnico.",
            371);
        put(all,Subprofession.SABOTAGE_DENIAL_SPECIALIST,CharacterClass.APODERADO,
            sheet(40,44,47,29,54,53,24,40,45),
            "Especialista de negación patrimonial: selección de objetivos, acceso y control de consecuencias.",
            376);
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
