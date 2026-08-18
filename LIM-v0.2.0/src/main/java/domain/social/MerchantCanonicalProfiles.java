package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — perfiles canónicos explícitos de Comerciante. */
public final class MerchantCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private MerchantCanonicalProfiles(){}
    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null) throw new IllegalArgumentException("subprofesión sin catálogo Comerciante: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("combinación deprecated/no canónica: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s));
        if(p==null) throw new IllegalArgumentException("subprofesión sin catálogo Comerciante: "+s);
        return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s); Objects.requireNonNull(c);
        if(s.profession()!=Profession.MERCHANT) throw new IllegalArgumentException("Profesión incorrecta.");
        return !DATA.get(s).containsKey(c);
    }
    public static String deprecationReason(Subprofession s,CharacterClass c){
        return isDeprecated(s,c)?"La combinación no representa una biografía comercial canónica suficientemente diferenciada.":"";
    }
    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m=new EnumMap<>(Subprofession.class);
        put(m,Subprofession.SHOPKEEPER,CharacterClass.APODERADO,Gender.MUJER,32,28,34,16,44,46,31,39,28,
                "Mantiene un comercio de proximidad: compra, almacena, repone y sostiene relaciones repetidas con proveedores y familias. Su vida depende de leer demanda cotidiana y convertir capital pequeño en disponibilidad estable.");
        put(m,Subprofession.TAVERN_KEEPER,CharacterClass.APODERADO,Gender.MUJER,34,30,36,17,45,48,34,40,31,
                "Sostiene producto, espacio y convivencia. Compra y conserva existencias, resuelve conflictos antes de que destruyan la jornada y mantiene una red de clientes cuya confianza vale tanto como la mercancía.");
        put(m,Subprofession.TAVERN_KEEPER,CharacterClass.HERALDO,Gender.MUJER,31,29,38,15,42,46,29,55,35,
                "Su oficio consiste en convertir una taberna en un lugar social reconocible: recuerda nombres, negocia tensiones, atrae clientela y consigue que personas incompatibles compartan espacio sin que el negocio deje de funcionar.");
        put(m,Subprofession.BOOKSELLER,CharacterClass.INTELECTUAL,Gender.HOMBRE,30,28,36,14,37,58,25,42,46,
                "Vende objetos cuyo valor depende de contenido, procedencia y lector. Clasifica catálogos, reconoce intereses y conserva memoria de volúmenes que pueden tardar años en encontrar comprador.");
        put(m,Subprofession.RURAL_AGGREGATOR,CharacterClass.INDOMITO,Gender.HOMBRE,38,43,36,25,38,44,17,34,32,
                "Recorre explotaciones, estima calidad y volumen, adelanta recursos y organiza recogidas antes de conocer el precio final. Su comercio exige soportar distancia, carga y temporadas inciertas.");
        put(m,Subprofession.RURAL_AGGREGATOR,CharacterClass.APODERADO,Gender.MUJER,34,30,39,18,37,47,28,40,35,
                "Coordina productores, almacenes y compradores desde una posición de gestión. Su patrimonio depende de mantener confianza y continuidad entre muchas pequeñas entregas que por separado apenas tendrían valor comercial.");

        put(m,Subprofession.V881_INDUSTRIAL_BROKER,CharacterClass.INTELECTUAL,Gender.HOMBRE,34,30,38,18,39,59,22,47,45,
                "Conecta productores, talleres, repuestos y compradores V881. Su especialidad es comprender qué pieza o capacidad puede sostener una instalación antes de que una venta aislada se convierta en una interrupción industrial.");
        put(m,Subprofession.V881_INDUSTRIAL_CONTRACT_AGENT,CharacterClass.APODERADO,Gender.MUJER,32,29,39,16,40,50,35,40,39,
                "Convierte requisitos técnicos en contratos comprensibles y sostenibles. Negocia plazos, garantías, proveedores y penalizaciones sin administrar directamente la fábrica que contrata.");
        put(m,Subprofession.V881_INDUSTRIAL_CONSULTANT,CharacterClass.ESPECIALISTA,Gender.MUJER,30,30,43,18,58,52,24,40,44,
                "Entra en instalaciones que ya funcionan para localizar cuellos de botella, tolerancias y errores de coordinación. Su valor está en precisión diagnóstica y en pequeñas correcciones que evitan grandes pérdidas.");

        put(m,Subprofession.V881_INDUSTRIALIST,CharacterClass.INTELECTUAL,Gender.HOMBRE,36,34,43,20,42,63,25,50,49,
                "Coordina talleres, maquinaria, especialistas, materiales y contratos para que una capacidad productiva pueda repetirse con calidad. Administra sistemas de producción, no sólo mercancías.");
        put(m,Subprofession.V881_INDUSTRIALIST,CharacterClass.APODERADO,Gender.MUJER,34,30,45,18,40,56,39,40,45,
                "Sostiene la continuidad de una organización industrial mediante proveedores, personal, contratos y decisiones de patrimonio. Su trabajo consiste en mantener la dirección cuando la producción atraviesa años difíciles.");

        put(m,Subprofession.SHIPOWNER,CharacterClass.INTELECTUAL,Gender.HOMBRE,36,36,44,20,41,62,24,48,50,
                "Transforma buques en rutas sostenibles. Calcula carga, mantenimiento, calendarios, puertos y riesgo para que un activo móvil siga generando red económica incluso cuando permanece inmovilizado.");
        put(m,Subprofession.SHIPOWNER,CharacterClass.APODERADO,Gender.MUJER,34,30,45,18,40,54,41,40,45,
                "Administra capital, tripulación, contratos y relaciones portuarias. Su vida comercial consiste en sostener la flota como patrimonio que debe seguir siendo útil a través de ciclos largos.");

        put(m,Subprofession.FINANCIER,CharacterClass.INTELECTUAL,Gender.HOMBRE,31,29,42,12,36,67,32,49,56,
                "Comercia con tiempo: presta, adelanta, asegura y decide qué proyecto puede comenzar utilizando riqueza que todavía no ha producido. Su principal herramienta es convertir incertidumbre en decisiones comparables.");
        put(m,Subprofession.FINANCIER,CharacterClass.APODERADO,Gender.MUJER,30,28,43,11,35,58,48,40,49,
                "Administra patrimonio, garantías y relaciones de confianza. Su trabajo consiste en sostener compromisos económicos cuando el futuro aún no permite comprobar si la decisión fue correcta.");

        put(m,Subprofession.INFRASTRUCTURE_CONCESSIONAIRE,CharacterClass.APODERADO,Gender.MUJER,35,30,45,18,39,58,50,40,48,
                "Administra derechos que pueden reorganizar territorio durante generaciones. Coordina autoridad, capital, juristas y operadores para que una concesión siga siendo viable sin convertirse en propiedad arbitraria del territorio.");
        put(m,Subprofession.INFRASTRUCTURE_CONCESSIONAIRE,CharacterClass.HERALDO,Gender.MUJER,33,30,44,16,38,55,42,61,49,
                "Su trabajo se apoya en legitimidad social e institucional: negocia con comunidades, autoridades y usuarios para que una infraestructura sea aceptada además de rentable.");

        put(m,Subprofession.GRAND_MERCHANT,CharacterClass.INTELECTUAL,Gender.HOMBRE,35,34,48,19,41,67,31,50,58,
                "Conecta varias cadenas económicas sin necesitar poseer cada fábrica, flota o mina que utiliza. Su ventaja central es reconocer dónde una mercancía sigue siendo producción y dónde comienza a convertirse en escasez.");
        put(m,Subprofession.GRAND_MERCHANT,CharacterClass.APODERADO,Gender.MUJER,33,30,49,17,40,58,50,40,51,
                "Mantiene redes de crédito, proveedores, agentes y clientes a gran escala. Su vida consiste en sostener relaciones económicas que pueden sobrevivir a personas, rutas y ciclos de mercado.");

        put(m,Subprofession.RESTRICTED_MATERIALS_BROKER,CharacterClass.INTELECTUAL,Gender.HOMBRE,34,32,50,15,39,69,34,50,62,
                "Administra materiales cuya escasez no se resuelve simplemente con precio. Debe conocer procedencia, autorización, usuario legítimo y condiciones de intercambio cuando una pieza puede condicionar años de investigación.");
        put(m,Subprofession.RESTRICTED_MATERIALS_BROKER,CharacterClass.APODERADO,Gender.MUJER,32,30,51,13,38,60,55,40,57,
                "Sostiene las relaciones institucionales necesarias para mover materiales restringidos sin perder custodia ni legitimidad. Su trabajo está en garantizar que cada intercambio pueda continuar existiendo mañana.");
        if(m.size()!=Subprofession.forProfession(Profession.MERCHANT).size()) throw new IllegalStateException("catálogo Comerciante incompleto.");
        return Map.copyOf(m);
    }
    private static void put(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> m,Subprofession s,CharacterClass c,Gender g,
                            int v,int a,int ad,int f,int d,int i,int fe,int car,int cl,String n){
        if(s.profession()!=Profession.MERCHANT) throw new IllegalArgumentException("No es Comerciante: "+s);
        var by=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
        var existing=m.get(s);
        if(existing!=null) by.putAll(existing);
        if(by.containsKey(c)) throw new IllegalStateException("Perfil duplicado: "+s+"/"+c);
        by.put(c,new CanonicalSubprofessionProfile(s,c,Set.of(g),CharacterSheet.of(v,a,ad,f,d,i,fe,car,cl),n+" La clase expresa esta biografía concreta y no una fórmula de afinidad."));
        m.put(s,Map.copyOf(by));
    }
}
