package domain.worldmemory;
import java.util.*;
public record WorldObjectInstanceId(String value){public WorldObjectInstanceId{if(value==null||value.isBlank())throw new IllegalArgumentException("ID de instancia vacío.");}public static WorldObjectInstanceId create(){return new WorldObjectInstanceId(UUID.randomUUID().toString());}}
