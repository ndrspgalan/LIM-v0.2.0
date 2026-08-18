package domain.save;
import java.io.Serializable; import java.time.Instant; import java.util.Objects;
public record SaveMetadata(String title,String description,Instant createdAt,String thumbnailReference) implements Serializable {
 public SaveMetadata { if(title==null||title.isBlank()) throw new IllegalArgumentException("El título es obligatorio."); description=Objects.requireNonNullElse(description,"").trim(); createdAt=Objects.requireNonNullElseGet(createdAt,Instant::now); thumbnailReference=Objects.requireNonNullElse(thumbnailReference,"").trim(); }
 public static SaveMetadata of(String title,String description){return new SaveMetadata(title,description,Instant.now(),"");}
}
