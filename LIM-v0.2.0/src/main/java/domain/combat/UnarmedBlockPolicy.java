package domain.combat;
import domain.character.sheet.*;
import domain.inventory.item.LethalityProfile;
import java.util.Objects;

/** guardia desarmada mantenida con LEFT CLICK. Sólo cubre HEAD. */
public final class UnarmedBlockPolicy {
 public static final double HEAD_COVERAGE_BONUS = 0.50;
 public BlockProfile resolve(CharacterSheet sheet){ Objects.requireNonNull(sheet); return new BlockProfile(HEAD_COVERAGE_BONUS,new LethalityProfile(0,0,sheet.valueOf(Attribute.AGUANTE))); }
 public record BlockProfile(double addedHeadCoverage, LethalityProfile protection){}
}
