package application.combat;
import domain.character.progression.*; import domain.character.sheet.CharacterSheet; import domain.save.GameSessionState; import java.util.*;
/** aplica automáticamente la recompensa universal de mucus al eliminar un enemigo. */
public final class EnemyDefeatProgressionService {
 private final UniversalMucusAcquisitionPolicy policy;
 public EnemyDefeatProgressionService(GenderSoftcapProfile softcaps){this.policy=new UniversalMucusAcquisitionPolicy(Objects.requireNonNull(softcaps));}
 public UniversalMucusAcquisitionPolicy.MucusAcquisitionResult onEnemyDefeated(GameSessionState game,CharacterSheet enemySheet){Objects.requireNonNull(game);var result=policy.onEnemyDefeated(game.character().identity().gender(),game.progression(),Objects.requireNonNull(enemySheet));var p=game.progression();game.replaceProgression(new CharacterProgressionState(p.level(),p.sheet(),result.wallet()),game.currentStats());return result;}
}
