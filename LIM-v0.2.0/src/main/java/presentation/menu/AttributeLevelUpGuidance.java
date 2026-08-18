package presentation.menu;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/** Catálogo técnico del borrador de subida de nivel. No revela maestrías. */
public final class AttributeLevelUpGuidance {
    private static final Map<Attribute, String> DESCRIPTIONS = descriptions();

    private AttributeLevelUpGuidance() {}

    public static String descriptionOf(Attribute attribute) {
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        return DESCRIPTIONS.get(attribute);
    }

    public static String descriptionOf(Attribute attribute, CharacterSheet sheet) {
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        Objects.requireNonNull(sheet, "La hoja provisional no puede ser nula.");
        return DESCRIPTIONS.get(attribute);
    }

    private static Map<Attribute, String> descriptions() {
        EnumMap<Attribute, String> result = new EnumMap<>(Attribute.class);
        result.put(Attribute.VITALIDAD, """
Cada punto de VITALIDAD añade 10 PV TOTALES y 1 punto de ESTABILIDAD FÍSICA. Los PV REGEN base se calculan a partir de la raíz sexta de los PV TOTALES y, en condiciones ordinarias, su aplicación basal se produce cada 6 segundos en hombre y cada 5 segundos en mujer. Cualquier modificador externo de regeneración actúa después sobre ese valor base.

VITALIDAD no sólo aumenta cuánto daño puede soportar el personaje antes de morir: al elevar los PV TOTALES también incrementa indirectamente la magnitud de PV REGEN. Una reducción temporal o inhibición de PV REGEN no modifica los PV TOTALES ni la ESTABILIDAD FÍSICA.

Softcaps:
- Hombre: 15, 18, 25, 40 y 75.
- Mujer: 13, 16, 25, 30 y 75.

Tu corazón todavía recuerda: hubo una configuración anterior a ésta. No era más resistente porque soportase mejor la agresión, sino porque la materia misma de la que dependía su continuidad admitía otra forma de organizar la vida.
""");
        result.put(Attribute.AGUANTE, """
Cada punto de AGUANTE añade 1 PA TOTAL. También determina la capacidad teórica de carga, aunque la capacidad ordinaria efectiva está limitada a 40 kg en hombre y 30 kg en mujer. El valor utilizado por el sistema es el menor entre AGUANTE y ese límite ordinario.

La proporción entre masa transportada y capacidad máxima determina cuánto tarda en recuperarse por completo la barra de PA. Hasta un tercio de carga, la recuperación completa requiere 1 segundo; entre un tercio y dos tercios, 1,5 segundos; por encima de dos tercios, 3 segundos; y al alcanzar o superar la capacidad máxima, 5 segundos. Alcanzar o superar el 100 % de capacidad inmoviliza al personaje, pero no detiene PA REGEN.

Después de consumir PA existe además una latencia antes de que comience la regeneración. Su valor ordinario es 1,20 segundos menos 0,01 segundos por cada punto de AGUANTE, con un mínimo de 0.

Softcaps:
- Hombre: 20 y 40.
- Mujer: 15 y 30.
El último softcap constituye el límite ordinario para quien no posea afinidad con AGUANTE.

Tu corazón todavía recuerda: cuando la privación simultánea de agua y alimento alcanza su máxima penalización, disminuyen las reservas disponibles, cambia el balance hídrico, aumenta la movilización de sustratos energéticos y el organismo reorganiza su economía homeostática para preservar aquello que considera indispensable. Mantén hambre y sed en ese límite y, sólo después, duerme. Quizá al despertar el cuerpo haya aprendido algo que la conciencia todavía no sabe nombrar.
""");
        result.put(Attribute.ADAPTABILIDAD, """
ADAPTABILIDAD determina cuánto tarda en consolidarse una adversidad ambiental y cómo evolucionan las resistencias naturales del personaje.

Toxicidad Virulenta, Quemadura Asfixiante y Empapado necesitan un tiempo de build-up equivalente a 0,1 segundos por cada punto de ADAPTABILIDAD. Frío Escarchante requiere 1 segundo por punto. Cada adversidad mantiene su propia acumulación y varias pueden progresar simultáneamente.

Hasta ADAPTABILIDAD 75, la progresión ordinaria depende del sexo. Hombre: +0,2 puntos porcentuales por nivel frente a Perforante, Cortante y Contundente; +0,1 frente a Veneno, Quemadura, Congelación, Maldición y Frenesí; +0 frente a Electricidad. Mujer: +0,1 frente a Perforante, Cortante, Contundente, Veneno, Quemadura y Congelación; +0,25 frente a Maldición; +0,05 frente a Frenesí; +0 frente a Electricidad.

Originalmente, cada nuevo punto por encima de 75 añadía +0,7 puntos porcentuales a las nueve resistencias. Una resistencia reduce daño; no equivale por sí misma a una inmunidad ni impide necesariamente que una adversidad ambiental llegue a consolidarse.

Softcaps comunes: 12 y 75.

Tu corazón todavía recuerda: hubo una configuración anterior a ésta. No era más resistente porque soportase mejor la agresión, sino porque la materia misma de la que dependía su continuidad admitía otra forma de organizar la vida.
""");
        result.put(Attribute.FUERZA, """
FUERZA determina la capacidad para producir trabajo mecánico y transferirlo al entorno. Incrementa la contundencia de los ataques desarmados, satisface requisitos físicos de armas y participa en lanzamientos manuales.

En un lanzamiento, cada punto de FUERZA añade 1 punto de daño bruto Contundente. La masa del objeto lanzado añade además 1 punto de Contundente por kilogramo. Los componentes Perforante y Cortante propios del objeto se conservan sin escalar por FUERZA.

StaggerPolicy es universal. En melee, la entrada canónica es FUERZA + masa efectiva del ataque. Una magnitud equivalente de 1 genera 0,5 m de retroceso y 0,5 s de stagger; la progresión es lineal hasta 50, donde alcanza 2 m y 2 s. Los valores superiores saturan en ese máximo. Explosiones, presión mental y otros consumidores pueden alimentar la misma política mediante una magnitud equivalente ya resuelta.

NADAR requiere FUERZA 15 y DESTREZA 15. ESCALAR requiere FUERZA 20 y DESTREZA 20. GOLPE DESESTABILIZADOR requiere FUERZA 30. La pendiente máxima de escalada comienza en 75 grados y progresa hasta 120 grados conforme aumenta FUERZA.

Softcaps:
- Hombre: 25 y 50.
- Mujer: 21 y 30.
El último softcap constituye el límite ordinario para quien no posea afinidad con FUERZA.

Tu corazón todavía recuerda: la fuerza aplicada sobre otro cuerpo no sólo altera su equilibrio. Bajo determinadas condiciones, una diferencia suficiente entre dos hombres puede alterar primero aquello que gobierna el equilibrio desde dentro.
""");
        result.put(Attribute.DESTREZA, """
DESTREZA representa coordinación, control fino del movimiento, precisión de trayectoria y capacidad para ejecutar acciones físicas con poco margen de error.

En lanzamientos manuales, el alcance máximo horizontal con un ángulo óptimo de 35 grados equivale a DESTREZA metros. Otros ángulos generan trayectorias diferentes, pero no conceden más alcance horizontal que ese máximo.

DESVIAR requiere DESTREZA 20. FINTAR se desbloquea con DESTREZA 35. En ese punto su desplazamiento equivale al 35 % de la altura del personaje y progresa linealmente hasta el 50 % de la altura en DESTREZA 50, donde alcanza su máximo. Su coste basal es 5 PA.

NADAR requiere FUERZA 15 y DESTREZA 15. ESCALAR requiere FUERZA 20 y DESTREZA 20. CABALGAR requiere DESTREZA 20 y CARISMA 25.

Softcaps comunes: 20 y 70. Para quien no posea afinidad con DESTREZA, 70 constituye su límite ordinario.

Tu corazón todavía recuerda: una técnica ejecutada una vez puede ser accidente; mil técnicas distintas sólo producen repertorio. Repite el mismo ataque ligero, desarmado, hasta que el cuerpo deje de ejecutarlo y empiece a anticipar por sí mismo dónde termina. Y si después de haber extraído de la coordinación todo lo razonablemente útil todavía insistes en llevarla hasta 70, quizá descubras por qué la precisión absoluta empieza a confundirse con ausencia.
""");
        result.put(Attribute.INTELIGENCIA, """
Cada punto de INTELIGENCIA añade 1 punto de CORDURA antes de bonificaciones o penalizaciones externos.

CORDURA no reduce directamente el daño físico ni sustituye las resistencias. Frente a Maldición y Frenesí, primero se aplica la resistencia porcentual correspondiente. Sobre el daño mental neto resultante, CORDURA reduce porcentualmente la presión mental que alimenta StaggerPolicy, hasta un máximo efectivo del 100 %. Así, resistir el daño y conservar la estabilidad mental son dos etapas diferentes de la misma resolución.

INTELIGENCIA también puede determinar qué información técnica, propiedades, mecanismos o funcionalidades resulta capaz de interpretar el personaje cuando un objeto o sistema declara expresamente un requisito cognitivo.

Softcaps comunes: 30 y 70. El último softcap constituye el límite ordinario para quien no posea afinidad con INTELIGENCIA.

Tu corazón todavía recuerda: un cazador no se distingue de su presa por los dientes, sino por la posición que ocupa cuando el grupo se reorganiza. Reúne rastros de quienes persiguen a otros, aprende qué individuo inicia la persecución y cuál obliga al resto a ceder espacio, y no conviertas en presa a quienes nunca te reconocieron como depredador. En una jerarquía auténtica sólo puede haber un alfa.
""");
        result.put(Attribute.FE, """
FE no añade por sí misma PV, PA, resistencias ordinarias ni una estadística derivada fija. Determina hasta qué punto el personaje puede hacer operativas propiedades y fenómenos cuya eficacia depende de una convicción genuina y no únicamente de conocer intelectualmente su existencia.

Los requisitos de FE son específicos de cada objeto, propiedad o interacción que los declare. Conocer que algo es posible, reunir las condiciones para ello y creer realmente que esa posibilidad pertenece al personaje son estados distintos.

Cuando una propiedad depende de FE, se emplea el valor efectivo del atributo. FE no sustituye INTELIGENCIA ni CLARIVIDENCIA: comprender, creer y percibir son contratos diferentes.

Softcaps comunes: 3, 13, 32, 40 y 60. El último constituye el límite ordinario para quien no posea afinidad con FE.
""");
        result.put(Attribute.CARISMA, """
CARISMA representa la capacidad de modificar la disposición social de otros mediante presencia, conducta y lectura interpersonal. No sustituye el historial entre personajes: el Tipo de Relación continúa siendo un estado independiente y no cambia automáticamente porque CARISMA sea alto.

CABALGAR requiere CARISMA 25 y DESTREZA 20.

La progresión difiere por sexo. En hombre, los softcaps son 25 y 50, siendo 50 su límite ordinario. En mujer, los softcaps son 18, 21 y 40; 40 constituye su límite ordinario cuando no posee afinidad con CARISMA.

Cuando una política social o una acción ordinaria declara expresamente que depende del atributo, se utiliza su valor efectivo y no únicamente el nivel base de la hoja.

Tu corazón todavía recuerda: algunos animales huyen antes de que exista una amenaza visible; otros siguen a quien todavía no ha dado una orden. La jerarquía aparece antes que el lenguaje. Aprende a distinguir quién se aproxima por confianza, quién conserva distancia y quién reconoce espontáneamente un centro social; no conviertas en presa aquello que se acerca sin haberte tratado jamás como depredador.

Tu corazón todavía recuerda: algunos intercambios parecen obedecer al precio, otros a la confianza y otros a algo que nadie pone por escrito en la factura. Si empiezas a notar la diferencia, quizá el mercado lleve tiempo valorándote a ti también.
""");
        result.put(Attribute.CLARIVIDENCIA, """
CLARIVIDENCIA determina qué parte de una realidad ya existente puede reconocerse como información utilizable. No crea aquello que permite percibir, no sustituye requisitos de otros atributos y no convierte por sí sola una posibilidad en una interacción.

Puede actuar como requisito para revelar propiedades ocultas de objetos, interpretar fenómenos que permanecen fuera de la percepción ordinaria y reconocer información que existe en el mundo aunque el personaje todavía no disponga de un marco perceptivo suficiente para distinguirla.

Softcaps comunes: 11, 22, 33, 66 y 75. El máximo ordinario es 75 y la afinidad correspondiente no presenta preferencia de sexo.

Tu corazón todavía recuerda: al principio, cuando ves algo que nadie más ve, lo razonable es asumir que el problema eres tú. Lo verdaderamente incómodo empieza cuando aquello que estabas mirando parece haber llegado a la misma conclusión sobre ti. Continúa mirando dentro de ti.
""");
        return Map.copyOf(result);
    }
}
