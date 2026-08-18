package domain.save;
public final class SaveMigrationRegistry { private SaveMigrationRegistry(){} public static GameSaveSnapshot migrate(GameSaveSnapshot snapshot){ if(snapshot.schemaVersion()==GameSaveSnapshot.CURRENT_SCHEMA_VERSION)return snapshot; throw new IllegalArgumentException("No existe migración para el esquema "+snapshot.schemaVersion()); } }
