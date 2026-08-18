package domain.save.snapshot;
import java.io.Serializable;
public record ProfileReference(String profileId,long revision) implements Serializable { public ProfileReference { if(profileId==null||profileId.isBlank()) throw new IllegalArgumentException("El perfil es obligatorio."); } }
