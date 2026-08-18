package domain.bestiarium.physical_plane.ancient;

import domain.character.sheet.CharacterSheet;
import java.util.Objects;

public final class AncientProfile {
    private final AncientArchetype archetype;
    private final CharacterSheet sheet;
    private final AncientMasteryProfile masteries;
    private final AncientRunicOverlay runicOverlay;
    AncientProfile(AncientArchetype archetype) {
        this.archetype=Objects.requireNonNull(archetype);
        this.sheet=AncientAttributePolicy.sheet(archetype);
        this.masteries=AncientMasteryPolicy.resolve(archetype);
        this.runicOverlay=new AncientRunicOverlay(archetype.originalRunicMark());
    }
    public AncientArchetype archetype(){return archetype;}
    public CharacterSheet sheet(){return sheet;}
    public int canonicalLevel(){return sheet.totalAttributeLevel();}
    public AncientMasteryProfile masteries(){return masteries;}
    public AncientRunicOverlay runicOverlay(){return runicOverlay;}
}
