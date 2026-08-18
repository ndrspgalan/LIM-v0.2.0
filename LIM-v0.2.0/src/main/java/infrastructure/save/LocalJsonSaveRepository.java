package infrastructure.save;
import domain.save.SaveKind;

import domain.save.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

public final class LocalJsonSaveRepository implements SaveRepository {
    private final Path directory;
    private final Map<String, SaveSlot> slots = new HashMap<>();

    public LocalJsonSaveRepository(Path directory) {
        this.directory = Objects.requireNonNull(directory);
        try { Files.createDirectories(directory); loadSlotIndex(); }
        catch (IOException e) { throw new IllegalStateException("No se pudo preparar el directorio de guardados.", e); }
    }

    @Override public synchronized void write(SaveSlot slot, GameSaveSnapshot snapshot) {
        Objects.requireNonNull(slot); Objects.requireNonNull(snapshot);
        Path target = file(slot.id()); Path temp = target.resolveSibling(target.getFileName()+".tmp");
        byte[] payload = serialize(snapshot); String base64 = Base64.getEncoder().encodeToString(payload); String checksum = sha256(payload);
        String json = "{\n"+
                "  \"schemaVersion\": "+snapshot.schemaVersion()+",\n"+
                "  \"slotId\": \""+escape(slot.id())+"\",\n"+
                "  \"personaId\": \""+escape(slot.personaId())+"\",\n"+
                "  \"kind\": \""+slot.kind()+"\",\n"+
                "  \"trigger\": \""+slot.trigger()+"\",\n"+
                "  \"title\": \""+escape(slot.metadata().title())+"\",\n"+
                "  \"description\": \""+escape(slot.metadata().description())+"\",\n"+
                "  \"createdAt\": \""+slot.metadata().createdAt()+"\",\n"+
                "  \"thumbnailReference\": \""+escape(slot.metadata().thumbnailReference())+"\",\n"+
                "  \"checksum\": \""+checksum+"\",\n"+
                "  \"payload\": \""+base64+"\"\n"+
                "}\n";
        try {
            Files.writeString(temp,json,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            try { Files.move(temp,target,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException e) { Files.move(temp,target,StandardCopyOption.REPLACE_EXISTING); }
            slots.put(slot.id(),slot); writeSlotIndex();
        } catch(IOException e){ throw new IllegalStateException("No se pudo escribir el guardado de forma atómica.",e); }
    }

    @Override public synchronized GameSaveSnapshot read(String slotId) {
        try {
            String json=Files.readString(file(slotId),StandardCharsets.UTF_8);
            String checksum=value(json,"checksum"), encoded=value(json,"payload");
            byte[] payload=Base64.getDecoder().decode(encoded);
            if(!MessageDigest.isEqual(checksum.getBytes(StandardCharsets.UTF_8),sha256(payload).getBytes(StandardCharsets.UTF_8)))
                throw new CorruptSaveException("El guardado está corrupto: checksum no válido.");
            Object object=deserialize(payload);
            if(!(object instanceof GameSaveSnapshot snapshot)) throw new CorruptSaveException("El payload no contiene un snapshot compatible.");
            if(snapshot.schemaVersion()!=GameSaveSnapshot.CURRENT_SCHEMA_VERSION) return SaveMigrationRegistry.migrate(snapshot);
            return snapshot;
        } catch(CorruptSaveException e){throw e;} catch(Exception e){throw new CorruptSaveException("No se pudo leer el guardado.",e);}
    }

    @Override public synchronized List<SaveSlot> listForPersona(String personaId){ return slots.values().stream().filter(s->s.personaId().equals(personaId)).sorted(Comparator.comparing(s->s.metadata().createdAt())).toList(); }
    @Override public synchronized void deleteForPersona(String personaId){ for(SaveSlot slot:new ArrayList<>(slots.values())) if(slot.personaId().equals(personaId)){ try{Files.deleteIfExists(file(slot.id()));}catch(IOException e){throw new IllegalStateException(e);} slots.remove(slot.id()); } try{writeSlotIndex();}catch(IOException e){throw new IllegalStateException(e);} }

    @Override public synchronized void delete(String slotId){try{Files.deleteIfExists(file(slotId));slots.remove(slotId);writeSlotIndex();}catch(IOException e){throw new IllegalStateException(e);}}
    @Override public synchronized void deleteWakeSavesAfter(String personaId,Instant instant){for(SaveSlot slot:new ArrayList<>(slots.values())) if(slot.personaId().equals(personaId)&&slot.kind()==SaveKind.WAKE&&slot.metadata().createdAt().isAfter(instant)){try{Files.deleteIfExists(file(slot.id()));}catch(IOException e){throw new IllegalStateException(e);}slots.remove(slot.id());}try{writeSlotIndex();}catch(IOException e){throw new IllegalStateException(e);}}

    private Path file(String id){return directory.resolve(id+".json");}
    private static byte[] serialize(Serializable object){try(ByteArrayOutputStream out=new ByteArrayOutputStream();ObjectOutputStream o=new ObjectOutputStream(out)){o.writeObject(object);return out.toByteArray();}catch(IOException e){throw new IllegalStateException(e);}}
    private static Object deserialize(byte[] bytes)throws IOException,ClassNotFoundException{try(ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(bytes))){return in.readObject();}}
    private static String sha256(byte[] data){try{byte[] d=MessageDigest.getInstance("SHA-256").digest(data);return HexFormat.of().formatHex(d);}catch(Exception e){throw new IllegalStateException(e);}}
    private static String escape(String s){return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n");}
    private static String value(String json,String key){String marker="\""+key+"\"";int i=json.indexOf(marker);if(i<0)throw new CorruptSaveException("Falta "+key);int c=json.indexOf(':',i);int q1=json.indexOf('"',c+1);int q2=q1+1;while(true){q2=json.indexOf('"',q2);if(q2<0)throw new CorruptSaveException("JSON inválido");if(json.charAt(q2-1)!='\\')break;q2++;}return json.substring(q1+1,q2).replace("\\n","\n").replace("\\\"","\"").replace("\\\\","\\");}
    private void loadSlotIndex() throws IOException { Path idx=directory.resolve("slots.index"); if(!Files.exists(idx)) return; for(String line:Files.readAllLines(idx,StandardCharsets.UTF_8)){String[] p=line.split("\\|",8);if(p.length==8) slots.put(p[0],new SaveSlot(p[0],p[1],SaveKind.valueOf(p[2]),SaveTrigger.valueOf(p[3]),new SaveMetadata(p[4],p[5],Instant.parse(p[6]),p[7])));}}
    private void writeSlotIndex() throws IOException { List<String> lines=slots.values().stream().map(s->String.join("|",s.id(),s.personaId(),s.kind().name(),s.trigger().name(),s.metadata().title().replace("|","/"),s.metadata().description().replace("|","/"),s.metadata().createdAt().toString(),s.metadata().thumbnailReference().replace("|","/"))).toList();Files.write(directory.resolve("slots.index"),lines,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);}
}
