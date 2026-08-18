package domain.persona;

import domain.ability.CharacterMasteryCollection;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.KenanCanonicalProfile;
import domain.milestone.PersonaMilestone;
import domain.runic.RunicMarkId;
import domain.save.SaveKind;
import domain.save.SaveSlot;
import domain.social.Profession;

import java.util.List;
import java.util.Objects;

/**
 * Perfil persistente del único avatar jugable de LIM: Kenan.
 *
 * GOLD : deja de representar una familia genérica de PERSONAS/orígenes.
 * La identidad canónica (sexo, clase, antropometría y profesión sentinel CHILD)
 * pertenece a KenanCanonicalProfile; aquí sólo vive estado mutable de jugador.
 */
public final class PersonaProfile {
    private final String id;
    private final String name;
    private final int level;
    private final java.util.ArrayList<SaveSlot> saveSlots;
    private final java.util.ArrayList<PersonaMilestone> milestones;
    private boolean gameCompleted;
    private boolean allRunicMarksUnlocked;
    private RunicMarkId equippedRunicMark;
    private CharacterMasteryCollection masteryCollection;

    public PersonaProfile(String id, String name, int level, List<SaveSlot> savePoints, List<PersonaMilestone> milestones) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El identificador de Kenan es obligatorio.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("El nombre de Kenan es obligatorio.");
        if (!KenanCanonicalProfile.NAME.equalsIgnoreCase(name.trim())) {
            throw new IllegalArgumentException("Kenan es la única PERSONA jugable de LIM.");
        }
        if (level < 1 || level > 999) throw new IllegalArgumentException("El nivel debe estar entre 1 y 999.");
        this.id=id;
        this.name=KenanCanonicalProfile.NAME;
        this.level=level;
        this.saveSlots=new java.util.ArrayList<>(Objects.requireNonNull(savePoints));
        this.milestones=new java.util.ArrayList<>(Objects.requireNonNull(milestones));
        this.masteryCollection=CharacterMasteryCollection.forCanonicalChild(KenanCanonicalProfile.CHARACTER_CLASS,KenanCanonicalProfile.GENDER);
    }

    /** Compatibilidad de llamadas históricas: sólo admite la identidad canónica de Kenan. */
    public PersonaProfile(String id, String name, Gender gender, CharacterClass characterClass, int level,
                          List<SaveSlot> savePoints, List<PersonaMilestone> milestones) {
        this(id,name,level,savePoints,milestones);
        if (gender != KenanCanonicalProfile.GENDER || characterClass != KenanCanonicalProfile.CHARACTER_CLASS) {
            throw new IllegalArgumentException("La PERSONA jugable debe ser Kenan: HOMBRE / INDOMITO.");
        }
    }

    public String id(){return id;}
    public String name(){return name;}
    public Gender gender(){return KenanCanonicalProfile.GENDER;}
    public CharacterClass characterClass(){return KenanCanonicalProfile.CHARACTER_CLASS;}
    public Profession profession(){return KenanCanonicalProfile.PROFESSION;}
    public String narrativeProfession(){return KenanCanonicalProfile.PROFESSION.label();}
    public String appearance(){return "Aspecto canónico de Kenan";}
    public double heightMeters(){return KenanCanonicalProfile.HEIGHT_METERS;}
    public double weightKilograms(){return KenanCanonicalProfile.WEIGHT_KILOGRAMS;}
    public int level(){return level;}
    public List<SaveSlot> savePoints(){return List.copyOf(saveSlots);}
    public List<PersonaMilestone> milestones(){return List.copyOf(milestones);}
    public boolean addMilestone(PersonaMilestone milestone){Objects.requireNonNull(milestone);if(milestones.stream().anyMatch(existing->existing.id().equals(milestone.id())))return false;milestones.add(milestone);return true;}
    public List<SaveSlot> saveSlotsIn(SaveKind kind){return saveSlots.stream().filter(s->s.kind()==kind).sorted(java.util.Comparator.comparing(s->s.metadata().createdAt())).toList();}
    public java.util.Optional<SaveSlot> latestSaveOptional(){return saveSlots.stream().max(java.util.Comparator.comparing(s->s.metadata().createdAt()));}
    public SaveSlot latestSave(){return latestSaveOptional().orElseThrow(()->new IllegalStateException("Kenan todavía no dispone de guardado."));}
    public java.util.Optional<SaveSlot> continuitySaveOptional(){return latestSaveOptional();}
    public SaveSlot continuitySave(){return latestSave();}
    public void registerSave(SaveSlot slot){Objects.requireNonNull(slot);saveSlots.removeIf(s->s.id().equals(slot.id()));saveSlots.add(slot);}
    public void forgetWakeSavesAfter(java.time.Instant instant){saveSlots.removeIf(s->s.kind()==SaveKind.WAKE&&s.metadata().createdAt().isAfter(instant));}
    public boolean gameCompleted(){return gameCompleted;}
    public void markGameCompleted(){gameCompleted=true;}
    public void unlockAllRunicMarks(){allRunicMarksUnlocked=true;}
    public boolean allRunicMarksUnlocked(){return allRunicMarksUnlocked;}
    public java.util.Optional<RunicMarkId> equippedRunicMark(){return java.util.Optional.ofNullable(equippedRunicMark);}
    public void equipRunicMark(RunicMarkId mark){Objects.requireNonNull(mark);if(!allRunicMarksUnlocked)throw new IllegalStateException("Explorar Marcas Rúnicas requiere [VOLUNTAD MAYOR].");equippedRunicMark=mark;}
    public CharacterMasteryCollection masteryCollectionOrEmpty(){return masteryCollection;}
    public void replaceMasteryCollection(CharacterMasteryCollection collection){masteryCollection=Objects.requireNonNull(collection);}
}
