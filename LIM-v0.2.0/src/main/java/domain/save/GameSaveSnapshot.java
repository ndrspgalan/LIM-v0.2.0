package domain.save;
import domain.save.snapshot.*; import java.io.Serializable; import java.util.Objects;
public record GameSaveSnapshot(int schemaVersion,CharacterSnapshot character,CombatSnapshot combat,InventorySnapshot inventory,MasterySnapshot mastery,WorldSnapshot world,SurvivalSnapshot survival,TransportSnapshot transport,NarrativeSnapshot narrative,ProfileReference profileReference) implements Serializable {
 public static final int CURRENT_SCHEMA_VERSION=3;
 public GameSaveSnapshot { if(schemaVersion<1||schemaVersion>CURRENT_SCHEMA_VERSION) throw new IllegalArgumentException("Versión de esquema no soportada: "+schemaVersion); Objects.requireNonNull(character);Objects.requireNonNull(combat);Objects.requireNonNull(inventory);Objects.requireNonNull(mastery);Objects.requireNonNull(world);Objects.requireNonNull(survival);Objects.requireNonNull(transport);Objects.requireNonNull(narrative);Objects.requireNonNull(profileReference); }
}
