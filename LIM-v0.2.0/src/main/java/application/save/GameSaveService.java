package application.save;
import domain.save.*; import java.time.Instant; import java.util.Objects; import java.util.Optional; import java.util.Comparator;
public final class GameSaveService {
 private final SaveRepository repository; public GameSaveService(SaveRepository repository){this.repository=Objects.requireNonNull(repository);}
 public SaveSlot saveWake(String personaId,long wakeCount,GameSaveSnapshot snapshot){String id=personaId+"-wake-"+String.format("%06d",wakeCount);SaveSlot slot=new SaveSlot(id,personaId,SaveKind.WAKE,SaveTrigger.WAKE_UP,new SaveMetadata("Despertar "+wakeCount,"Continuidad guardada al despertar.",Instant.now(),""));repository.write(slot,snapshot);return slot;}
 public SaveSlot saveQuick(String personaId,GameSaveSnapshot snapshot){SaveSlot slot=new SaveSlot(personaId+"-quicksave",personaId,SaveKind.QUICKSAVE,SaveTrigger.QUICK_SAVE,new SaveMetadata("Guardado rápido","Único guardado rápido; cada uso de 5 sobrescribe este instante.",Instant.now(),""));repository.write(slot,snapshot);return slot;}

 public Optional<SaveSlot> latestForPersona(String personaId){return repository.listForPersona(personaId).stream().max(Comparator.comparing(slot->slot.metadata().createdAt()));}
 public GameSnapshotHydrator.LoadedGame load(SaveSlot slot){Objects.requireNonNull(slot);return GameSnapshotHydrator.restore(repository.read(slot.id()),slot);}
 public void forgetAfterWake(String personaId,SaveSlot selected){repository.deleteWakeSavesAfter(personaId,selected.metadata().createdAt());}
}
