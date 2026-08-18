package domain.inventory.item.meleeWeapons;

import java.util.List;

/**
 * Catálogo básico de herramientas de uso cotidiano que pueden emplearse como armas.
 *  fija aquí identidad y narrativa; masa, dimensiones, slots, requisitos,
 * perfiles y repertorios se concretarán.
 */
public final class ConventionalMeleeWeaponBasicCatalog {
    private ConventionalMeleeWeaponBasicCatalog() {}

    public static List<MeleeWeaponBasicDefinition> all() {
        return List.of(martilloDeBola(), hoz(), guadana(), horca(), varaDeMadera(), boathook());
    }

    public static MeleeWeaponBasicDefinition martilloDeBola() {
        return new MeleeWeaponBasicDefinition(
                "Martillo de bola",
                "Herramienta de metalistería",
                "Martillo de trabajo empleado principalmente en oficios relacionados con el metal. Una de sus caras presenta una superficie plana destinada a conformar, enderezar y remachar piezas, mientras que la opuesta termina en una cabeza semiesférica que permite concentrar el golpe sobre una superficie menor. Es habitual en herrerías, forjas, cerrajerías, talleres de carpintería metálica y trabajos de fontanería. Aunque no ha sido concebido como arma, su cabeza metálica y reducido tamaño permiten emplearlo eficazmente como instrumento contundente en caso de necesidad."
        );
    }

    public static MeleeWeaponBasicDefinition hoz() {
        return new MeleeWeaponBasicDefinition(
                "Hoz",
                "Herramienta agrícola",
                "Herramienta agrícola provista de una hoja curva de un solo filo, concebida para segar mediante movimientos de arrastre. En combate improvisado, la pronunciada curvatura de su hoja permite atacar alrededor de pequeños escudos y alcanzar zonas parcialmente protegidas que resultarían difíciles de acometer con una hoja recta. También puede utilizarse para enganchar extremidades, armas u otros elementos del adversario, aunque su geometría y corto alcance la mantienen muy alejada de un arma concebida expresamente para la guerra."
        );
    }

    public static MeleeWeaponBasicDefinition guadana() {
        return new MeleeWeaponBasicDefinition(
                "Guadaña",
                "Herramienta agrícola",
                "Herramienta agrícola de gran tamaño concebida para segar cereal y vegetación mediante amplios movimientos de barrido. Su larga asta proporciona un alcance extraordinario para un utensilio cotidiano y su hoja transversal permite acometer alrededor de determinadas defensas o realizar maniobras de enganche. Utilizada de forma improvisada contra personas o jinetes, puede alcanzar objetivos a considerable distancia y enganchar miembros o equipamiento, pero la disposición lateral de la hoja, su gran envergadura y su geometría de trabajo dejan al portador especialmente expuesto durante la recuperación de cada movimiento."
        );
    }

    public static MeleeWeaponBasicDefinition horca() {
        return new MeleeWeaponBasicDefinition(
                "Horca",
                "Herramienta agrícola y pesquera",
                "Utensilio de asta larga rematado por varias púas, empleado principalmente para levantar, desplazar y amontonar materiales agrícolas como heno, paja o mies. Determinadas variantes pueden desempeñar funciones semejantes en actividades pesqueras. En caso de necesidad, su longitud y sus puntas permiten utilizarla como una lanza improvisada, manteniendo al adversario a distancia y concentrando el impacto sobre una o varias púas, aunque carece de la robustez y geometría de un arma de asta concebida expresamente para el combate."
        );
    }

    public static MeleeWeaponBasicDefinition varaDeMadera() {
        return new MeleeWeaponBasicDefinition(
                "Bō",
                "Bastón largo de madera",
                "Vara larga de madera utilizada como instrumento auxiliar para transportar cargas suspendidas, sostener recipientes o proporcionar apoyo durante largos desplazamientos. Su construcción no incorpora hoja, punta metálica ni elemento ofensivo alguno; no es más que un asta resistente terminada en superficies romas. Precisamente por su sencillez constituye el antecedente funcional de numerosas armas de asta: empleada correctamente, su longitud permite mantener la distancia, golpear con cualquiera de sus extremos y aprovechar la palanca de todo el cuerpo para transmitir impactos contundentes."
        );
    }

    public static MeleeWeaponBasicDefinition boathook() {
        return new MeleeWeaponBasicDefinition(
                "Boathook",
                "Herramienta de navegación",
                "Herramienta de asta utilizada en navegación para aproximar, separar y gobernar embarcaciones durante las maniobras de atraque y desatraque. Su extremo incorpora una punta metálica roma destinada a empujar y un pequeño gancho afilado orientado en sentido contrario para aferrar cabos, bordas u otros puntos de apoyo. En caso de necesidad puede emplearse como arma improvisada de asta, utilizando la punta principal para mantener al adversario a distancia y transmitir impactos contundentes, o el gancho para perforar y ejecutar maniobras de enganche. Sin embargo, el reducido tamaño de este último y su orientación funcional dificultan utilizarlo como elemento perforante con la precisión de un arma concebida para ello."
        );
    }
}
