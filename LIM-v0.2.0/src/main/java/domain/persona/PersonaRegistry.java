package domain.persona;

import java.util.*;

/** Registro de cardinalidad uno para el único avatar jugable: Kenan. */
public final class PersonaRegistry {
    public static final int MAX_PERSONAS=1;
    private final List<PersonaProfile> personas=new ArrayList<>();
    private PersonaProfile lastSavedPersona;
    private final domain.save.SaveRepository saveRepository;

    public PersonaRegistry(List<PersonaProfile> initialPersonas){this(initialPersonas,null);}
    public PersonaRegistry(List<PersonaProfile> initialPersonas,domain.save.SaveRepository saveRepository){
        this.saveRepository=saveRepository;
        Objects.requireNonNull(initialPersonas).forEach(this::register);
        lastSavedPersona=personas.isEmpty()?null:personas.get(0);
    }
    public List<PersonaProfile> personas(){return Collections.unmodifiableList(personas);}
    public boolean isFull(){return !personas.isEmpty();}
    public Optional<PersonaProfile> findById(String id){return personas.stream().filter(p->p.id().equals(id)).findFirst();}
    public Optional<PersonaProfile> lastSavedPersona(){return Optional.ofNullable(lastSavedPersona);}
    public void markLastSaved(PersonaProfile persona){if(!personas.contains(Objects.requireNonNull(persona)))throw new IllegalArgumentException("Kenan no pertenece al registro.");lastSavedPersona=persona;}
    public void register(PersonaProfile persona){Objects.requireNonNull(persona);if(isFull())throw new IllegalStateException("Kenan ya ha sido encarnado.");personas.add(persona);}
    public domain.save.SaveRepository saveRepository(){if(saveRepository==null)throw new IllegalStateException("No hay repositorio de guardado asociado.");return saveRepository;}
}
