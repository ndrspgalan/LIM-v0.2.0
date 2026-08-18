package presentation.menu;

/** CLARIVIDENCIA emplea deliberadamente una única entrada del Grimorio en todo su recorrido. */
public final class ClairvoyanceNarrative {
    private ClairvoyanceNarrative() {}

    public static String descriptionFor(int clairvoyance) {
        if (clairvoyance < 1 || clairvoyance > 75) throw new IllegalArgumentException("CLARIVIDENCIA debe estar comprendida entre 1 y 75.");
        return GRIMOIRE_ENTRY;
    }

    private static final String GRIMOIRE_ENTRY = """
Toda senda conduce eventualmente hacia una fría y perpetua soledad.

Eventualmente, incluso eso es extenuante.

Solo el abismo permanece.

UNO...

INSONDABLE E INCOMPRENSIBLE ENTRE INNUMERABLES PLIEGUES.

Y a pesar de que nuestro cuerpo ha sentido el abatimiento de la voluntad, ya estaba frío.

Una larga erosión que solo mimetizaba la ondulación de una mente vacía.

El poder que resiste al tormento: la vitalidad.
""";
}
