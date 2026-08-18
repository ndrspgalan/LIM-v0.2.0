package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — perfiles canónicos explícitos de Jurista. */
public final class JuristCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private JuristCanonicalProfiles(){}
    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null) throw new IllegalArgumentException("subprofesión sin catálogo Jurista: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("combinación deprecated/no canónica: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s));
        if(p==null) throw new IllegalArgumentException("subprofesión sin catálogo Jurista: "+s);
        return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s); Objects.requireNonNull(c);
        if(s.profession()!=Profession.JURIST) throw new IllegalArgumentException("Profesión incorrecta.");
        return !DATA.get(s).containsKey(c);
    }
    public static String deprecationReason(Subprofession s,CharacterClass c){
        return isDeprecated(s,c)?"La combinación no representa una biografía jurídica canónica suficientemente diferenciada.":"";
    }
    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m=new EnumMap<>(Subprofession.class);
        put(m,Subprofession.PUBLIC_SCRIBE,CharacterClass.INTELECTUAL,Gender.HOMBRE,31,28,35,12,42,60,23,39,44,
                "Convierte actos privados, poderes, deudas e inventarios en memoria demostrable. Redacta, fecha, comprueba identidad y conserva fórmulas para una autoridad futura.");
        put(m,Subprofession.PUBLIC_SCRIBE,CharacterClass.ESPECIALISTA,Gender.MUJER,29,27,39,11,58,51,24,38,42,
                "Su oficio se concentra en precisión documental: copiar sin introducir errores, cotejar identidades, ordenar anexos y mantener legible durante años aquello que otras personas necesitan demostrar.");
        put(m,Subprofession.MAGISTRATE,CharacterClass.INTELECTUAL,Gender.HOMBRE,34,30,39,15,39,68,29,50,57,
                "Transforma testimonios, documentos y normas en resoluciones ejecutables. Su vida exige separar alegación de prueba y autoridad legítima de mera fuerza.");
        put(m,Subprofession.MAGISTRATE,CharacterClass.HERALDO,Gender.MUJER,32,29,41,13,40,59,35,66,53,
                "Además de interpretar la norma, sostiene públicamente la legitimidad de sus decisiones: escucha, explica, contiene conflictos y mantiene una institución reconocible ante quienes deben obedecerla.");
        put(m,Subprofession.CONTINUITY_JURIST,CharacterClass.INTELECTUAL,Gender.HOMBRE,34,32,46,14,38,69,30,47,62,
                "Trabaja donde longevidad, regeneración, trasvase y alteración neuronal rompen los supuestos temporales del derecho ordinario. Decide qué consecuencias produce afirmar que una persona sigue siendo la misma.");
        put(m,Subprofession.CONTINUITY_JURIST,CharacterClass.APODERADO,Gender.MUJER,31,30,48,12,37,59,56,40,58,
                "Sostiene jurídicamente identidad, patrimonio y sucesión cuando cuerpo o memoria han cambiado. Su especialidad es mantener compromisos institucionales durante transformaciones que duran generaciones.");
        put(m,Subprofession.DOCTRINE_CUSTODIAN,CharacterClass.INTELECTUAL,Gender.HOMBRE,35,33,49,15,40,70,34,50,65,
                "Administra clasificación, archivo y publicación de conocimiento sobre alma, Intersticio y continuidad. Decide qué descripción puede entrar en una cadena documental verificable.");
        put(m,Subprofession.DOCTRINE_CUSTODIAN,CharacterClass.HERALDO,Gender.MUJER,32,30,51,13,39,61,45,40,61,
                "Custodia la frontera social del conocimiento: determina cómo se comunica, a quién se entrega y bajo qué lenguaje puede adquirir legitimidad pública sin convertir una restricción institucional en simple silencio.");
        if(m.size()!=Subprofession.forProfession(Profession.JURIST).size()) throw new IllegalStateException("catálogo Jurista incompleto.");
        return Map.copyOf(m);
    }
    private static void put(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m,Subprofession s,CharacterClass c,Gender g,
                            int v,int a,int ad,int f,int d,int i,int fe,int car,int cl,String n){
        if(s.profession()!=Profession.JURIST) throw new IllegalArgumentException("No es Jurista: "+s);
        var by=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
        var existing=m.get(s);
        if(existing!=null) by.putAll(existing);
        if(by.containsKey(c)) throw new IllegalStateException("Perfil duplicado: "+s+"/"+c);
        by.put(c,new CanonicalSubprofessionProfile(s,c,Set.of(g),CharacterSheet.of(v,a,ad,f,d,i,fe,car,cl),n+" La clase expresa esta biografía concreta y no una fórmula de afinidad."));
        m.put(s,Map.copyOf(by));
    }
}
