package domain.save;
import java.io.Serializable; import java.util.Objects;
public record SaveSlot(String id,String personaId,SaveKind kind,SaveTrigger trigger,SaveMetadata metadata) implements Serializable {
 public SaveSlot { if(id==null||id.isBlank()) throw new IllegalArgumentException("El identificador del slot es obligatorio."); if(personaId==null||personaId.isBlank()) throw new IllegalArgumentException("La PERSONA es obligatoria."); Objects.requireNonNull(kind); Objects.requireNonNull(trigger); Objects.requireNonNull(metadata); }
 public SaveSlot(String id,String title,String description,SaveKind kind){this(id,inferPersonaId(id),kind,kind==SaveKind.WAKE?SaveTrigger.WAKE_UP:SaveTrigger.QUICK_SAVE,SaveMetadata.of(title,description));}
 public String title(){return metadata.title();}
 public String description(){return metadata.description();}
 private static String inferPersonaId(String id){int dash=id.indexOf('-');return dash>0?id.substring(0,dash):id;}
}

