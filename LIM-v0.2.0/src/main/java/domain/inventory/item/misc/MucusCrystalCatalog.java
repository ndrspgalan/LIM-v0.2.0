package domain.inventory.item.misc;

import domain.runic.EffectImmunity;
import domain.runic.EffectImmunitySet;

/** Catálogo narrativo, geométrico y mecánico de los cristales de Transposición. */
public final class MucusCrystalCatalog {
    private MucusCrystalCatalog() {}

    public static MucusCrystalItem yellow() {
        return crystal(
                "Cristal de Mucus AMARILLENTO",
                "Toda respuesta defensiva exige reconocer primero aquello que debe permanecer separado del organismo. " +
                "El mucus amarillento aparece cuando esa frontera comienza a construirse mediante procesos inflamatorios y celulares " +
                "destinados a contener la agresión. La transposición inmoviliza ese instante de separación dentro de una estructura " +
                "cristalina permanente. Desde entonces, ninguna sustancia capaz de degradar los tejidos encuentra una frontera biológica que todavía pueda romper.",
                MucusCrystalGeometry.TETRAEDRO,
                "Tetraedro amarillo translúcido, de cuatro caras regulares y aristas nítidas. En su núcleo se distinguen inclusiones " +
                "opacas semejantes a materia celular inmovilizada antes de alcanzar la superficie.",
                EffectImmunitySet.of(EffectImmunity.POISON, EffectImmunity.VIRULENT_TOXICITY),
                "INMUNIDAD | Veneno · Toxicidad Virulenta");
    }

    public static MucusCrystalItem greenish() {
        return crystal(
                "Cristal de Mucus VERDOSO",
                "El organismo no combate el calor imponiendo frío, sino recuperando continuamente el equilibrio perdido entre la energía " +
                "que recibe y la que consigue disipar. El mucus verdoso conserva el recuerdo de esa adaptación extrema. Su cristal no " +
                "enfría la materia. La obliga a reorganizarse constantemente alrededor de un estado compatible con la permanencia.",
                MucusCrystalGeometry.OCTAEDRO,
                "Octaedro verdoso semitransparente, formado por dos pirámides enfrentadas en equilibrio perfecto. Una condensación tenue " +
                "recorre sus caras desde los vértices hacia el centro sin llegar a desprenderse.",
                EffectImmunitySet.of(EffectImmunity.SUFFOCATING_HEAT),
                "INMUNIDAD | Calor Asfixiante");
    }

    public static MucusCrystalItem brown() {
        return crystal(
                "Cristal de Mucus MARRÓN",
                "Ningún esfuerzo destruye una estructura de forma inmediata. Primero aparecen deformaciones microscópicas que, acumuladas, " +
                "terminan convirtiéndose en agotamiento. El mucus marrón conserva la memoria del último instante anterior a esa deformación " +
                "irreversible. La transposición cristaliza dicho estado y obliga al organismo a regresar continuamente a él antes de que el cansancio llegue a consolidarse.",
                MucusCrystalGeometry.CUBO,
                "Cubo marrón oscuro, translúcido solo en sus aristas. Sus seis caras parecen compactadas en capas sucesivas, como si cada una " +
                "soportara la presión de la anterior sin deformarse.",
                EffectImmunitySet.of(EffectImmunity.STAMINA_REGEN_DELAY),
                "LATENCIA DE ACTIVACIÓN DE PA REGEN | 0 s");
    }

    public static MucusCrystalItem bloodied() {
        return crystal(
                "Cristal de Mucus ENSANGRENTADO",
                "Toda reparación comienza recordando cuál era la forma que debía conservarse. El mucus ensangrentado aparece allí donde el " +
                "organismo intenta reconstruir tejidos sometidos a un deterioro persistente. La transposición convierte ese recuerdo en una " +
                "esfera cristalina cuya estructura devuelve incesantemente a la materia hacia la configuración previa al daño, impidiendo que " +
                "el deterioro llegue a convertirse en el nuevo equilibrio del cuerpo.",
                MucusCrystalGeometry.ESFERA,
                "Esfera carmesí de superficie continua, sin aristas ni punto de apoyo estable. Bajo su envoltura transparente circulan filamentos " +
                "rojizos que convergen y vuelven a separarse sin abandonar nunca la forma esférica.",
                EffectImmunitySet.of(EffectImmunity.CURSE_DAMAGE),
                "INMUNIDAD | Daño de Maldición · Energía Maldita");
    }

    public static MucusCrystalItem blackish() {
        return crystal(
                "Cristal de Mucus NEGRUZCO",
                "Existen alteraciones que no lesionan la carne, sino la continuidad con la que la conciencia permanece siendo ella misma. " +
                "El mucus negruzco aparece cuando esa continuidad ha comenzado a fracturarse. La transposición detiene ese proceso antes de " +
                "que alcance estabilidad suficiente para convertirse en una nueva realidad mental. El cristal no expulsa el frenesí. Impide " +
                "que encuentre una estructura sobre la que llegar a establecerse.",
                MucusCrystalGeometry.DODECAEDRO,
                "Dodecaedro negruzco de doce caras pentagonales, casi opaco. Cada cara devuelve un reflejo ligeramente distinto del entorno, " +
                "pero ninguno parece prolongarse hasta el interior del cristal.",
                EffectImmunitySet.of(EffectImmunity.FRENZY),
                "INMUNIDAD | Frenesí");
    }

    private static MucusCrystalItem crystal(String name, String narrative, MucusCrystalGeometry geometry, String form,
                                            EffectImmunitySet immunities, String statistic) {
        return new MucusCrystalItem(name, narrative, geometry, form, immunities, statistic, 1.0);
    }
}
