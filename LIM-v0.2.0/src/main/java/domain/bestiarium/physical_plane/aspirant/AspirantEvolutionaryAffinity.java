package domain.bestiarium.physical_plane.aspirant;

/**
 * Familiaridad evolutiva operacional para ASPIRANT. No expresa porcentajes de ADN:
 * ordena el grado de continuidad anatómica/genómica utilizable por CAMBIAFORMAS.
 */
public enum AspirantEvolutionaryAffinity {
    PRIMATE_NEAR(2),
    PLACENTAL_MAMMAL(3),
    OTHER_VERTEBRATE(5),
    DISTANT_METAZOAN(8);

    private final int minimumCambiaformasHumanDeviation;

    AspirantEvolutionaryAffinity(int minimumCambiaformasHumanDeviation) {
        this.minimumCambiaformasHumanDeviation = minimumCambiaformasHumanDeviation;
    }

    public int minimumCambiaformasHumanDeviation() {
        return minimumCambiaformasHumanDeviation;
    }
}
