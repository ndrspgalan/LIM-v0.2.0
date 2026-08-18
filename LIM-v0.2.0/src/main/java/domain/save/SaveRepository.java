package domain.save;
import java.time.Instant; import java.util.List;
public interface SaveRepository { void write(SaveSlot slot,GameSaveSnapshot snapshot); GameSaveSnapshot read(String slotId); List<SaveSlot> listForPersona(String personaId); void deleteForPersona(String personaId); void delete(String slotId); void deleteWakeSavesAfter(String personaId,Instant instant); }
