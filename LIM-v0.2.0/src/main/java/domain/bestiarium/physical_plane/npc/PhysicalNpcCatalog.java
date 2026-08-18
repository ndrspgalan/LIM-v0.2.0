package domain.bestiarium.physical_plane.npc;
import domain.ability.CharacterMasteryCollection; import domain.bestiarium.BestiaryEntity; import java.util.List;
public final class PhysicalNpcCatalog { private PhysicalNpcCatalog(){} public static List<BestiaryEntity> canonical(){return CanonicalNpcCatalog.all().stream().map(n->new BestiaryEntity(n.name()+", "+n.profession(),n.characterClass(),CharacterMasteryCollection.forClass(n.characterClass()))).toList();}}
