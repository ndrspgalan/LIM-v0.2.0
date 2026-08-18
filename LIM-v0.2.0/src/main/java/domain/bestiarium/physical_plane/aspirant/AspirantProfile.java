package domain.bestiarium.physical_plane.aspirant;

import domain.social.Subprofession;
import java.util.Objects;

/**
 * Perfil de individuo ASPIRANT. La profesión sigue perteneciendo a la persona en ambas formas;
 * CAMBIAFORMAS altera su anatomía, no crea una segunda identidad ocupacional.
 */
public record AspirantProfile(
        AspirantReferenceId referenceId,
        AspirantMorphology humanMorphology,
        AspirantMorphology cambiaformasMorphology,
        Subprofession subprofession,
        AspirantSomaticHistory somaticHistory) {

    public AspirantProfile {
        Objects.requireNonNull(referenceId);
        Objects.requireNonNull(humanMorphology);
        Objects.requireNonNull(cambiaformasMorphology);
        Objects.requireNonNull(subprofession);
        Objects.requireNonNull(somaticHistory);

        var ref = AspirantReferenceCatalog.profile(referenceId);
        if (!humanMorphology.convergentReference().equals(ref.animalReference())
                || !cambiaformasMorphology.convergentReference().equals(ref.animalReference()))
            throw new IllegalArgumentException("Las dos formas deben compartir el referente zoológico catalogado.");
        if (humanMorphology.region() != AspirantMorphologyRegion.HUMAN_DOMINANT)
            throw new IllegalArgumentException("La forma HUMANA ASPIRANT debe seguir siendo humano-dominante.");
        if (!cambiaformasMorphology.canonicalSynthesis())
            throw new IllegalArgumentException("CAMBIAFORMAS no admite aberración incoherente ni convergencia zoológica literal.");
        if (cambiaformasMorphology.fromHuman().steps() < ref.minimumCambiaformasHumanDeviation())
            throw new IllegalArgumentException("La familiaridad evolutiva exige mayor desviación para este referente.");
        if (cambiaformasMorphology.fromHuman().steps() < humanMorphology.fromHuman().steps())
            throw new IllegalArgumentException("CAMBIAFORMAS no puede acercarse más al humano que HUMANA.");
        if (cambiaformasMorphology.fromAnimal().steps() > humanMorphology.fromAnimal().steps())
            throw new IllegalArgumentException("CAMBIAFORMAS debe aproximarse al referente respecto de HUMANA.");
        if (!AspirantSubprofessionAffinityPolicy.compatible(referenceId, subprofession))
            throw new IllegalArgumentException("La historia ASPIRANT no justifica naturalmente esa subprofesión.");
    }

    public AspirantMorphology morphology(AspirantForm form) {
        return form == AspirantForm.HUMANA ? humanMorphology : cambiaformasMorphology;
    }

    public Subprofession subprofession(AspirantForm form) {
        Objects.requireNonNull(form);
        return subprofession;
    }

    public AspirantReferenceProfile referenceProfile() {
        return AspirantReferenceCatalog.profile(referenceId);
    }
}
