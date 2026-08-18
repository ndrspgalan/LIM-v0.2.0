package domain.save;

import domain.ability.CharacterMasteryCollection;
import domain.animation.CharacterAnimationState;
import domain.character.CharacterDefinition;
import domain.character.CharacterTitle;
import domain.character.progression.CharacterProgressionState;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.CurrentCharacterStats;
import domain.combat.HostileEncounterState;
import domain.communication.CommunicationPairingState;
import domain.communication.NearbyCommunicationRegistry;
import domain.environment.time.EnvironmentalCycle;
import domain.inventory.InventoryState;
import domain.rest.SleepState;
import domain.status.VitalResourceState;
import domain.survival.HungerState;
import domain.survival.ThirstState;
import domain.worldmemory.WorldMemory;

import java.util.Objects;

public final class GameSessionState {
    private CharacterDefinition character;
    private CharacterTitle currentTitle;
    private CharacterProgressionState progression;
    private CurrentCharacterStats currentStats;
    private InventoryState currentInventory;
    
    private HostileEncounterState hostileEncounterState;
    private CharacterAnimationState animationState;
    private CharacterMasteryCollection masteries;
    private WorldMemory worldMemory;
    private EnvironmentalCycle environmentalCycle;
    private VitalResourceState vitalResources;
    private SleepState sleepState;
    private HungerState hungerState;
    private ThirstState thirstState;
    private final CommunicationPairingState communicationPairingState = new CommunicationPairingState();
    private final NearbyCommunicationRegistry nearbyCommunicationRegistry = new NearbyCommunicationRegistry();
    private boolean sleepProgressionActive;
    private boolean unarmedGuardDrawn = true;
    private boolean unarmedRightLead = true;

    public GameSessionState(CharacterDefinition character, CharacterTitle currentTitle,
                     CharacterProgressionState progression, CurrentCharacterStats currentStats,
                     InventoryState currentInventory) {
        this(character, currentTitle, progression, currentStats, currentInventory,
                new HostileEncounterState(), new CharacterAnimationState(),
                CharacterMasteryCollection.forClass(character.identity().characterClass(), character.identity().gender()), new WorldMemory(),
                new EnvironmentalCycle(), defaultResources(currentStats), null, HungerState.initiallySatiated(), new ThirstState());
    }

    public GameSessionState(CharacterDefinition character, CharacterTitle currentTitle,
                     CharacterProgressionState progression, CurrentCharacterStats currentStats,
                     InventoryState currentInventory,
                     HostileEncounterState hostileEncounterState, CharacterAnimationState animationState,
                     CharacterMasteryCollection masteries, WorldMemory worldMemory,
                     EnvironmentalCycle environmentalCycle, VitalResourceState vitalResources) {
        this(character,currentTitle,progression,currentStats,currentInventory,hostileEncounterState,animationState,masteries,worldMemory,environmentalCycle,vitalResources,null,HungerState.initiallySatiated(),new ThirstState());
    }

    public GameSessionState(CharacterDefinition character, CharacterTitle currentTitle,
                     CharacterProgressionState progression, CurrentCharacterStats currentStats,
                     InventoryState currentInventory, HostileEncounterState hostileEncounterState, CharacterAnimationState animationState,
                     CharacterMasteryCollection masteries, WorldMemory worldMemory, EnvironmentalCycle environmentalCycle,
                     VitalResourceState vitalResources, SleepState restoredSleepState, HungerState hungerState, ThirstState thirstState) {
        this.character = Objects.requireNonNull(character, "El personaje guardado no puede ser nulo.");
        this.currentTitle = Objects.requireNonNull(currentTitle, "El título actual no puede ser nulo.");
        this.progression = Objects.requireNonNull(progression, "La progresión no puede ser nula.");
        this.currentStats = Objects.requireNonNull(currentStats, "Las estadísticas derivadas no pueden ser nulas.");
        this.currentInventory = Objects.requireNonNull(currentInventory, "El inventario actual no puede ser nulo.");
        this.hostileEncounterState = Objects.requireNonNull(hostileEncounterState, "El encuentro hostil no puede ser nulo.");
        this.animationState = Objects.requireNonNull(animationState, "El estado de animación no puede ser nulo.");
        this.masteries = Objects.requireNonNull(masteries, "La colección de maestrías no puede ser nula.");
        this.currentInventory.equipment().synchronizeRunicProgress(character.identity().characterClass(), this.masteries, progression.sheet());
        this.worldMemory = Objects.requireNonNull(worldMemory, "La Memoria del Mundo no puede ser nula.");
        this.environmentalCycle = Objects.requireNonNull(environmentalCycle, "El ciclo ambiental no puede ser nulo.");
        this.vitalResources = Objects.requireNonNull(vitalResources, "Los recursos vitales no pueden ser nulos.");
        this.sleepState = restoredSleepState == null ? new SleepState(this.environmentalCycle) : restoredSleepState;
        this.hungerState = Objects.requireNonNull(hungerState);
        this.thirstState = Objects.requireNonNull(thirstState);
    }

    /** Restaura en esta sesión mutable el último snapshot para que las pantallas conserven la misma referencia de sesión. */
    public void replaceFrom(GameSessionState restored) {
        Objects.requireNonNull(restored, "La sesión restaurada no puede ser nula.");
        this.character = restored.character;
        this.currentTitle = restored.currentTitle;
        this.progression = restored.progression;
        this.currentStats = restored.currentStats;
        this.currentInventory = restored.currentInventory;
        this.hostileEncounterState = restored.hostileEncounterState;
        this.animationState = restored.animationState;
        this.masteries = restored.masteries;
        this.worldMemory = restored.worldMemory;
        this.environmentalCycle = restored.environmentalCycle;
        this.vitalResources = restored.vitalResources;
        this.sleepState = restored.sleepState;
        this.hungerState = restored.hungerState;
        this.thirstState = restored.thirstState;
        this.sleepProgressionActive = restored.sleepProgressionActive;
        this.unarmedGuardDrawn = restored.unarmedGuardDrawn;
        this.unarmedRightLead = restored.unarmedRightLead;
    }

    public CharacterDefinition character() { return character; }
    public CharacterTitle currentTitle() { return currentTitle; }
    public int level() { return progression.level(); }
    public CharacterSheet characterSheet() { return progression.sheet(); }
    public CharacterProgressionState progression() { return progression; }
    public void replaceProgression(CharacterProgressionState progression, CurrentCharacterStats stats) {
        this.progression = Objects.requireNonNull(progression);
        this.currentStats = Objects.requireNonNull(stats);
    }
    public CurrentCharacterStats currentStats() { return currentStats; }
    public void replaceCurrentStats(CurrentCharacterStats stats) { this.currentStats = Objects.requireNonNull(stats); }
    public InventoryState currentInventory() { return currentInventory; }
    public void replaceCurrentInventory(InventoryState inventory) { this.currentInventory = Objects.requireNonNull(inventory); }
    public HostileEncounterState hostileEncounterState() { return hostileEncounterState; }
    public CharacterAnimationState animationState() { return animationState; }
    public CharacterMasteryCollection masteries() { return masteries; }
    public WorldMemory worldMemory() { return worldMemory; }
    public EnvironmentalCycle environmentalCycle() { return environmentalCycle; }
    public VitalResourceState vitalResources() { return vitalResources; }
    public SleepState sleepState() { return sleepState; }
    public HungerState hungerState() { return hungerState; }
    public ThirstState thirstState() { return thirstState; }
    public void replaceSurvivalStates(HungerState hunger, ThirstState thirst) { this.hungerState=Objects.requireNonNull(hunger); this.thirstState=Objects.requireNonNull(thirst); }
    public void restoreUnarmedGuard(boolean drawn, boolean rightLead) { this.unarmedGuardDrawn=drawn; this.unarmedRightLead=rightLead; }
    public boolean sleepProgressionActive() { return sleepProgressionActive; }
    public void beginSleepProgression() { if (sleepProgressionActive) throw new IllegalStateException("La progresión de sueño ya está activa."); sleepProgressionActive=true; }
    public void endSleepProgression() { sleepProgressionActive=false; }
    public void addMucus(domain.character.progression.MucusType type,double amount) {
        var p=progression; var next=new domain.character.progression.CharacterProgressionState(p.level(),p.sheet(),p.mucusWallet().add(type,amount));
        replaceProgression(next,currentStats);
    }
    public CommunicationPairingState communicationPairingState() { return communicationPairingState; }
    public NearbyCommunicationRegistry nearbyCommunicationRegistry() { return nearbyCommunicationRegistry; }
    public boolean unarmedGuardDrawn() { return unarmedGuardDrawn; }
    public boolean unarmedRightLead() { return unarmedRightLead; }
    public boolean toggleUnarmedLeadGuard() { unarmedRightLead = !unarmedRightLead; return unarmedRightLead; }
    public boolean toggleUnarmedGuard() {
        unarmedGuardDrawn = !unarmedGuardDrawn;
        return unarmedGuardDrawn;
    }

    private static VitalResourceState defaultResources(CurrentCharacterStats stats) {
        double health = stats.totalHealth().orElse(1.0);
        double stamina = stats.totalStamina().orElse(1.0);
        return new VitalResourceState(health, stamina);
    }
}
