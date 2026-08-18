package application.save;
import domain.save.*; import java.util.Objects;
/** Ruta canónica read -> hydrate para continuar una partida. */
public final class GameLoadService { private final SaveRepository repository; public GameLoadService(SaveRepository r){repository=Objects.requireNonNull(r);} public GameSnapshotHydrator.LoadedGame load(SaveSlot slot){Objects.requireNonNull(slot);return GameSnapshotHydrator.restore(repository.read(slot.id()),slot);} }
