package domain.ability;

import java.text.Normalizer;
import java.util.Locale;

/** Identificador estable derivado del nombre canónico; nunca se usa el texto visible para despachar. */
public record MasteryManifestationKey(MasteryId familyId, String id) {
    public MasteryManifestationKey { if (familyId==null || id==null || id.isBlank()) throw new IllegalArgumentException("Clave inválida."); }
    public static MasteryManifestationKey of(MasteryManifestation manifestation) {
        String normalized=Normalizer.normalize(manifestation.name(),Normalizer.Form.NFD)
                .replaceAll("\\p{M}","").replace("'","").replaceAll("[^A-Za-z0-9]+","_")
                .replaceAll("^_+|_+$","").toUpperCase(Locale.ROOT);
        return new MasteryManifestationKey(manifestation.familyId(), normalized);
    }
    public String qualified(){return familyId.name()+":"+id;}
}
