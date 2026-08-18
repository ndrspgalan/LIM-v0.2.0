package domain.movement;
import domain.character.sheet.CharacterSheet;import domain.inventory.logistics.*;import java.util.Objects;
/** Montar exige técnica desbloqueada y un transporte personal ecuestre físicamente presente. */
public final class RidingPolicy {private final ExplorationTechniqueUnlockPolicy unlockPolicy=new ExplorationTechniqueUnlockPolicy();
 public boolean canRide(CharacterSheet sheet,PersonalTransportUnitState transport){Objects.requireNonNull(sheet);Objects.requireNonNull(transport);return unlockPolicy.isUnlocked(ExplorationTechnique.RIDE,sheet)&&transport.owned()&&transport.physicallyPresent()&&transport.type().family()==PersonalTransportFamily.HORSE;}}
