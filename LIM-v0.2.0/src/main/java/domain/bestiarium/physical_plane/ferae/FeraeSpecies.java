package domain.bestiarium.physical_plane.ferae;

import domain.social.RelationshipType;

import java.util.Optional;

/** Catálogo canónico  de Ferae y progresión de EMPATÍA ANIMAL. */
public enum FeraeSpecies {
    RATON("Ratón", FeraeBranch.CARISMA, 12, RelationshipType.FRIENDLY, null),
    PALOMA("Paloma", FeraeBranch.CARISMA, 13, RelationshipType.FRIENDLY, null),
    GALLINA("Gallina", FeraeBranch.CARISMA, 13, RelationshipType.FRIENDLY, null),
    GOLONDRINA("Golondrina", FeraeBranch.CARISMA, 14, RelationshipType.FRIENDLY, null),
    CONEJO("Conejo", FeraeBranch.CARISMA, 14, RelationshipType.FRIENDLY, null),
    GATO("Gato", FeraeBranch.CARISMA, 15, RelationshipType.FRIENDLY, null),
    PERRO("Perro", FeraeBranch.CARISMA, 15, RelationshipType.FRIENDLY, null),
    GALLO("Gallo", FeraeBranch.CARISMA, 16, RelationshipType.FRIENDLY, null),
    PATO("Pato", FeraeBranch.CARISMA, 16, RelationshipType.FRIENDLY, null),
    LIEBRE("Liebre", FeraeBranch.CARISMA, 17, RelationshipType.FRIENDLY, null),
    CABRA("Cabra", FeraeBranch.CARISMA, 18, RelationshipType.FRIENDLY, null),
    OVEJA("Oveja", FeraeBranch.CARISMA, 18, RelationshipType.FRIENDLY, null),
    BURRO("Burro", FeraeBranch.CARISMA, 19, RelationshipType.FRIENDLY, null),
    VACA("Vaca", FeraeBranch.CARISMA, 20, RelationshipType.FRIENDLY, null),
    YEGUA_PASEO("Yegua de Paseo", FeraeBranch.CARISMA, 16, RelationshipType.FRIENDLY, null),
    YEGUA_CARRERAS("Yegua de Carreras", FeraeBranch.CARISMA, 18, RelationshipType.FRIENDLY, null),
    YEGUA_TIRO("Yegua de Tiro", FeraeBranch.CARISMA, 17, RelationshipType.FRIENDLY, null),
    BUHO("Búho", FeraeBranch.CARISMA, 21, RelationshipType.FRIENDLY, null),
    CAMELLO("Camello", FeraeBranch.CARISMA, 22, RelationshipType.FRIENDLY, null),
    YAK("Yak", FeraeBranch.CARISMA, 23, RelationshipType.FRIENDLY, null),
    ELEFANTE("Elefante", FeraeBranch.CARISMA, 25, RelationshipType.FRIENDLY, null),

    RATA("Rata", FeraeBranch.INTELIGENCIA, 12, RelationshipType.DISTRUSTFUL, HuntingTrophy.COLA_DE_RATA),
    CUERVO("Cuervo", FeraeBranch.INTELIGENCIA, 13, RelationshipType.DISTRUSTFUL, HuntingTrophy.PLUMA_DE_CUERVO),
    CERDO("Cerdo", FeraeBranch.INTELIGENCIA, 14, RelationshipType.DISTRUSTFUL, HuntingTrophy.PEZUNA_DE_CERDO),
    ARMADILLO("Armadillo", FeraeBranch.INTELIGENCIA, 15, RelationshipType.DISTRUSTFUL, HuntingTrophy.CAPARAZON_DE_ARMADILLO),
    CABALLO_PASEO("Caballo de Paseo", FeraeBranch.INTELIGENCIA, 16, RelationshipType.DISTRUSTFUL, HuntingTrophy.CERDA_DE_CABALLO),
    CABALLO_CARRERAS("Caballo de Carreras", FeraeBranch.INTELIGENCIA, 18, RelationshipType.DISTRUSTFUL, HuntingTrophy.CERDA_DE_CABALLO),
    CABALLO_TIRO("Caballo de Tiro", FeraeBranch.INTELIGENCIA, 17, RelationshipType.DISTRUSTFUL, HuntingTrophy.CERDA_DE_CABALLO),
    CIERVO("Ciervo", FeraeBranch.INTELIGENCIA, 17, RelationshipType.DISTRUSTFUL, HuntingTrophy.CORNAMENTA_DE_CIERVO),
    TORO("Toro", FeraeBranch.INTELIGENCIA, 18, RelationshipType.DISTRUSTFUL, HuntingTrophy.OREJA_DE_TORO),
    AGUILA("Águila", FeraeBranch.INTELIGENCIA, 19, RelationshipType.DISTRUSTFUL, HuntingTrophy.GARRAS_DE_AGUILA),

    SERPIENTE("Serpiente", FeraeBranch.INTELIGENCIA, 20, RelationshipType.HOSTILE, HuntingTrophy.PIEL_DE_SERPIENTE),
    JABALI("Jabalí", FeraeBranch.INTELIGENCIA, 22, RelationshipType.HOSTILE, HuntingTrophy.COLMILLO_DE_JABALI),
    LINCE("Lince", FeraeBranch.INTELIGENCIA, 23, RelationshipType.HOSTILE, HuntingTrophy.OJO_DE_LINCE),
    LOBO("Lobo", FeraeBranch.INTELIGENCIA, 24, RelationshipType.HOSTILE, HuntingTrophy.CRANEO_DE_LOBO),
    LEON("León", FeraeBranch.INTELIGENCIA, 26, RelationshipType.HOSTILE, HuntingTrophy.CRIN_DE_LEON),
    OSO("Oso", FeraeBranch.INTELIGENCIA, 28, RelationshipType.HOSTILE, HuntingTrophy.ZARPA_DE_OSO),
    RINOCERONTE("Rinoceronte", FeraeBranch.INTELIGENCIA, 30, RelationshipType.HOSTILE, HuntingTrophy.CUERNO_DE_RINOCERONTE);

    private final String label;
    private final FeraeBranch branch;
    private final int empathyAttributeRequirement;
    private final RelationshipType naturalRelationship;
    private final HuntingTrophy trophy;

    FeraeSpecies(String label, FeraeBranch branch, int empathyAttributeRequirement,
                 RelationshipType naturalRelationship, HuntingTrophy trophy) {
        this.label = label;
        this.branch = branch;
        this.empathyAttributeRequirement = empathyAttributeRequirement;
        this.naturalRelationship = naturalRelationship;
        this.trophy = trophy;
    }

    public String label() { return label; }
    public FeraeBranch branch() { return branch; }
    public int empathyAttributeRequirement() { return empathyAttributeRequirement; }
    public RelationshipType naturalRelationship() { return naturalRelationship; }
    public Optional<HuntingTrophy> trophy() { return Optional.ofNullable(trophy); }
    public boolean isInitiallyHostile() { return naturalRelationship == RelationshipType.HOSTILE; }
    public boolean isCompanionEligibleSpecies() { return true; }

    /** Alias conservado para consumidores antiguos; desde  todas las Ferae son elegibles. */
    public boolean canonicalCompanion() { return isCompanionEligibleSpecies(); }
}
