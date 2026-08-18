package domain.bestiarium.physical_plane.aspirant;

/**
 * Referente zoológico abierto, no catálogo de FERAE ni ingrediente quimérico. Describe
 * la solución animal hacia la que la historia somática del individuo hace converger CAMBIAFORMAS.
 */
public record ConvergentAnimalReference(String commonName, String scientificName) {
    public ConvergentAnimalReference {
        if(commonName==null||commonName.isBlank()) throw new IllegalArgumentException("El referente animal necesita nombre.");
        scientificName=scientificName==null?"":scientificName.trim();
    }
}
