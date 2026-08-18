package domain.ability;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Colección de un personaje construida a partir del catálogo universal. */
public final class CharacterMasteryCollection {
    private final List<MasteryId> owned;
    private final EnumMap<MasteryId, MasteryKnowledgeState> knowledge = new EnumMap<>(MasteryId.class);
    private final Set<String> activeManifestations = new java.util.HashSet<>();
    private final Set<String> activePassiveManifestations = new java.util.HashSet<>();
    private final Set<String> runtimeActiveEffects = new java.util.HashSet<>();
    private final Set<String> unlockedStructuredStages = new java.util.HashSet<>();
    private final EnumSet<TransmutationNodeId> unlockedNodes = EnumSet.noneOf(TransmutationNodeId.class);
    private final Gender ownerGender;
    private int selectedActiveIndex;
    private int selectedSustainedIndex;

    public CharacterMasteryCollection(List<MasteryId> initiallyUnlocked) { this(initiallyUnlocked, null); }

    public CharacterMasteryCollection(List<MasteryId> initiallyUnlocked, Gender ownerGender) {
        this.ownerGender = ownerGender;
        Objects.requireNonNull(initiallyUnlocked, "La colección no puede ser nula.");
        EnumSet<MasteryId> supplied = EnumSet.noneOf(MasteryId.class);
        for (MasteryId id : initiallyUnlocked) {
            Objects.requireNonNull(id, "Una maestría no puede ser nula.");
            if (!supplied.add(id)) throw new IllegalArgumentException("Maestría duplicada: " + id);
        }
        this.owned = List.of(MasteryId.values());
        for (MasteryId id : MasteryId.values()) {
            Mastery mastery = MasteryCatalog.require(id);
            knowledge.put(id, supplied.contains(id) ? MasteryKnowledgeState.UNLOCKED : MasteryKnowledgeState.UNKNOWN);
        }
        if (supplied.contains(MasteryId.TRANSMUTACION)) unlockedNodes.add(TransmutationNodeId.OVERCLOCK);
    }

    public static CharacterMasteryCollection kenanCanonical() {
        return forClass(CharacterClass.INDOMITO);
    }

    public static CharacterMasteryCollection jacobCanonical() {
        return forClass(CharacterClass.MAESTRO);
    }

    /** Crea una colección donde las maestrías afines son visibles, pero aún no utilizables. */
    public static CharacterMasteryCollection forClass(CharacterClass characterClass) {
        Gender inferred = switch (characterClass) {
            case LUCHADOR, INTELECTUAL, INDOMITO -> Gender.HOMBRE;
            case ESPECIALISTA, APODERADO, HERALDO -> Gender.MUJER;
            case MAESTRO -> Gender.HOMBRE;
        };
        return forClass(characterClass, inferred);
    }

    public static CharacterMasteryCollection forClass(CharacterClass characterClass, Gender gender) {
        List<MasteryId> resonant = java.util.Arrays.stream(MasteryId.values())
                .filter(id -> MasteryCatalog.require(id).category() != MasteryCategory.EVOLUTIVE)
                .filter(id -> MasteryResonancePolicy.resonates(id, characterClass, gender))
                .toList();
        CharacterMasteryCollection collection = new CharacterMasteryCollection(resonant, gender);
        for (MasteryId id : resonant) collection.knowledge.put(id, MasteryKnowledgeState.REVEALED);
        collection.unlockedNodes.clear(); collection.unlockedStructuredStages.clear(); collection.activePassiveManifestations.clear();
        return collection;
    }

    /** estado real al comienzo CHILD: afinidades de clase REVEALED, dos maestrías infantiles UNLOCKED y evolutivas ocultas. */
    /** Materializa directamente el estado CHILD de cualquier personaje canónico del timelapse. */
    public static CharacterMasteryCollection forCanonicalChild(domain.character.canonical.CanonicalCharacterStageProfile profile) {
        Objects.requireNonNull(profile,"El perfil canónico no puede ser nulo.");
        if(profile.stage()!=domain.character.canonical.CanonicalLifeStage.CHILD)
            throw new IllegalArgumentException("Sólo los perfiles CHILD reciben las maestrías infantiles forzadas.");
        CharacterMasteryCollection collection=forCanonicalChild(profile.characterClass(),profile.gender());
        for(MasteryId forced:profile.forcedMasteries()) collection.unlock(forced);
        return collection;
    }

    public static CharacterMasteryCollection forCanonicalChild(CharacterClass characterClass, Gender gender) {
        CharacterMasteryCollection collection = new CharacterMasteryCollection(
                java.util.List.of(MasteryId.REGENERACION_THETA, MasteryId.ESPIRITU_INFATIGABLE), gender);
        for (MasteryId id : MasteryId.values()) {
            Mastery mastery=MasteryCatalog.require(id);
            if (mastery.category()==MasteryCategory.EVOLUTIVE) { collection.knowledge.put(id,MasteryKnowledgeState.UNKNOWN); continue; }
            if (collection.knowledgeState(id)==MasteryKnowledgeState.UNLOCKED) continue;
            if (MasteryResonancePolicy.resonates(id,characterClass,gender)) collection.knowledge.put(id,MasteryKnowledgeState.REVEALED);
        }
        collection.unlockedNodes.clear(); collection.unlockedStructuredStages.clear(); collection.activePassiveManifestations.clear();
        return collection;
    }

    public static CharacterMasteryCollection allCanonical() {
        CharacterMasteryCollection collection = new CharacterMasteryCollection(List.of(MasteryId.values()));
        for (MasteryId id : MasteryId.values()) {
            Mastery mastery = MasteryCatalog.require(id);
            if (mastery instanceof StructuredMastery structured) {
                for (MasteryStage stage : structured.stages()) {
                    collection.unlockedStructuredStages.add(id.name() + ":" + stage.name().toUpperCase(java.util.Locale.ROOT));
                }
            } else if (mastery instanceof TransmutationMastery transmutation) {
                for (TransmutationNode node : transmutation.orderedNodes()) collection.unlockedNodes.add(node.id());
            }
        }
        return collection;
    }

    public Set<String> revealedMasteryIds() {
        return knowledge.entrySet().stream().filter(e -> e.getValue() == MasteryKnowledgeState.REVEALED)
                .map(e -> e.getKey().name()).collect(java.util.stream.Collectors.toUnmodifiableSet());
    }
    public Set<String> unlockedMasteryIds() {
        return knowledge.entrySet().stream().filter(e -> e.getValue() == MasteryKnowledgeState.UNLOCKED)
                .map(e -> e.getKey().name()).collect(java.util.stream.Collectors.toUnmodifiableSet());
    }
    public java.util.Optional<Gender> ownerGender() { return java.util.Optional.ofNullable(ownerGender); }
    public List<MasteryId> ownedIds() { return visibleIds(); }
    public List<MasteryId> visibleIds() { return owned.stream().filter(id -> knowledge.get(id).isVisible()).toList(); }
    public List<MasteryId> unlockedIds() { return owned.stream().filter(id -> knowledge.get(id).isUsable()).toList(); }
    public MasteryKnowledgeState knowledgeState(MasteryId id) { return knowledge.getOrDefault(Objects.requireNonNull(id), MasteryKnowledgeState.UNKNOWN); }
    public boolean reveal(MasteryId id) {
        Objects.requireNonNull(id);
        if (!owned.contains(id)) throw new IllegalArgumentException("La maestría no pertenece todavía a la colección progresiva: " + id);
        if (knowledgeState(id) != MasteryKnowledgeState.UNKNOWN) return false;
        knowledge.put(id, MasteryKnowledgeState.REVEALED); return true;
    }
    public boolean unlock(MasteryId id) {
        Objects.requireNonNull(id);
        if (!owned.contains(id)) throw new IllegalArgumentException("La maestría no pertenece todavía a la colección progresiva: " + id);
        if (knowledgeState(id) == MasteryKnowledgeState.UNLOCKED) return false;
        knowledge.put(id, MasteryKnowledgeState.UNLOCKED);
        if (id == MasteryId.TRANSMUTACION) unlockedNodes.add(TransmutationNodeId.OVERCLOCK);
        return true;
    }


    public boolean unlockStage(MasteryId id, String stageName) {
        Objects.requireNonNull(id); Objects.requireNonNull(stageName);
        Mastery mastery = MasteryCatalog.require(id);
        if (!(mastery instanceof StructuredMastery structured)
                || structured.stages().stream().noneMatch(stage -> stage.name().equalsIgnoreCase(stageName))) {
            throw new IllegalArgumentException("La etapa no pertenece a la maestría: " + stageName);
        }
        reveal(id);
        boolean changed = unlockedStructuredStages.add(id.name() + ":" + stageName.toUpperCase(java.util.Locale.ROOT));
        if (changed) knowledge.put(id, MasteryKnowledgeState.UNLOCKED);
        return changed;
    }
    public boolean isStageUnlocked(MasteryId id, String stageName) {
        return unlockedStructuredStages.contains(id.name() + ":" + stageName.toUpperCase(java.util.Locale.ROOT));
    }

    /** Sincroniza automáticamente las pasivas: REVEALED no aplica; UNLOCKED sí. */
    public Set<String> synchronizePassiveManifestations(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        activePassiveManifestations.clear();
        for (MasteryId id : unlockedIds()) {
            Mastery mastery = MasteryCatalog.require(id);
            if (mastery instanceof PairMastery pair) {
                addPassiveVariant(pair.original(), sheet);
                addPassiveVariant(pair.refined(), sheet);
            } else if (mastery instanceof StructuredMastery structured) {
                for (MasteryStage stage : structured.stages()) {
                    if (stage.natures().contains(MasteryType.PASSIVE)
                            && stage.isAccessibleTo(sheet)
                            && stageVisibleAsUnlocked(structured.id(), stage)) {
                        activePassiveManifestations.add(stage.name());
                    }
                }
            } else if (mastery instanceof TransmutationMastery transmutation) {
                for (TransmutationNode node : transmutation.orderedNodes()) {
                    if (node.type() == MasteryType.PASSIVE && unlockedNodes.contains(node.id())
                            && node.meetsAttributeRequirement(sheet)) activePassiveManifestations.add(node.name());
                }
            } else if (mastery instanceof EvolutiveMastery evolutive
                    && sheet.valueOf(evolutive.progressionAttribute()) >= 76) {
                activePassiveManifestations.add(evolutive.name());
            }
        }
        return Set.copyOf(activePassiveManifestations);
    }

    private void addPassiveVariant(MasteryVariant variant, CharacterSheet sheet) {
        if (variant.type() == MasteryType.PASSIVE && variant.isAccessibleTo(sheet)) {
            activePassiveManifestations.add(variant.name());
        }
    }

    public boolean isPassiveActive(String name, CharacterSheet sheet) {
        synchronizePassiveManifestations(sheet);
        return activePassiveManifestations.contains(name);
    }

    public Set<String> activePassiveManifestations(CharacterSheet sheet) {
        return synchronizePassiveManifestations(sheet);
    }

    private boolean stageVisibleAsUnlocked(MasteryId id, MasteryStage stage) {
        if (id == MasteryId.INCITAR) return isStageUnlocked(id, stage.name());
        if (id == MasteryId.SANAR) return isStageUnlocked(id, stage.name());
        return knowledgeState(id) == MasteryKnowledgeState.UNLOCKED;
    }

    /** Maestrías que este personaje conoce. */
    public List<Mastery> knownMasteries(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return visibleIds().stream().map(MasteryCatalog::require).toList();
    }
    public List<MasteryManifestation> selectableManifestations(MasteryType requestedType, CharacterSheet sheet) {
        if (requestedType == MasteryType.PASSIVE) return List.of();
        List<MasteryManifestation> result = new ArrayList<>();
        for (MasteryId id : unlockedIds()) {
            Mastery mastery = MasteryCatalog.require(id);
            if (mastery instanceof PairMastery pair) {
                addVariant(result, pair, pair.original(), requestedType, sheet);
                addVariant(result, pair, pair.refined(), requestedType, sheet);
            } else if (mastery instanceof StructuredMastery structured) {
                for (MasteryStage stage : structured.stages()) {
                    if (stage.natures().contains(requestedType) && stage.isAccessibleTo(sheet)
                            && stageVisibleAsUnlocked(structured.id(), stage)) {
                        result.add(new MasteryManifestation(id, mastery.name(), stage.name(), requestedType));
                    }
                }
            } else if (mastery instanceof TransmutationMastery transmutation) {
                for (TransmutationNode node : transmutation.orderedNodes()) {
                    if (node.type() == requestedType && unlockedNodes.contains(node.id()) && node.meetsAttributeRequirement(sheet)) {
                        result.add(new MasteryManifestation(id, mastery.name(), node.name(), requestedType));
                    }
                }
            }
        }
        return List.copyOf(result);
    }

    private void addVariant(List<MasteryManifestation> result, PairMastery family, MasteryVariant variant,
                            MasteryType requestedType, CharacterSheet sheet) {
        boolean accessible=variant.isAccessibleTo(sheet);
        if (accessible && ownerGender != null && family.id() == MasteryId.EXPLOSION_CINETICA) {
            int endurance=sheet.valueOf(domain.character.sheet.Attribute.AGUANTE);
            accessible = variant.refined()
                    ? MasteryGenderUnlockPolicy.potentialHardeningUnlocked(ownerGender,endurance)
                    : MasteryGenderUnlockPolicy.kineticExplosionUnlocked(ownerGender,endurance);
        }
        if (variant.type() == requestedType && accessible) {
            result.add(new MasteryManifestation(family.id(), family.name(), variant.name(), requestedType));
        }
    }

    public MasteryManifestation selectedActive(CharacterSheet sheet) {
        return selected(selectableManifestations(MasteryType.ACTIVE, sheet), true);
    }

    public MasteryManifestation selectedSustained(CharacterSheet sheet) {
        return selected(selectableManifestations(MasteryType.SUSTAINED, sheet), false);
    }

    private MasteryManifestation selected(List<MasteryManifestation> candidates, boolean active) {
        if (candidates.isEmpty()) return null;
        int index = active ? selectedActiveIndex : selectedSustainedIndex;
        index = Math.floorMod(index, candidates.size());
        if (active) selectedActiveIndex = index; else selectedSustainedIndex = index;
        return candidates.get(index);
    }

    public MasteryManifestation select(MasteryType type, int index, CharacterSheet sheet) {
        List<MasteryManifestation> candidates = selectableManifestations(type, sheet);
        if (candidates.isEmpty()) return null;
        int normalized = Math.floorMod(index, candidates.size());
        if (type == MasteryType.ACTIVE) selectedActiveIndex = normalized;
        else if (type == MasteryType.SUSTAINED) selectedSustainedIndex = normalized;
        return candidates.get(normalized);
    }

    public void synchronizeStaminaMasteryFlags(MasteryExecutionContext context) {
        Objects.requireNonNull(context, "El contexto de ejecución no puede ser nulo.");
        context.world().spiritInfatigableActive(isPassiveActive("ESPÍRITU INFATIGABLE", context.sheet()));
        context.world().helicalReleaseActive(isPassiveActive("LIBERACIÓN HELICOIDAL", context.sheet()));
        context.world().helicalOptimizationActive(isPassiveActive("OPTIMIZACIÓN HELICOIDAL", context.sheet()));
    }

    public MasteryExecutionResult executeSelectedActive(MasteryExecutionContext context) {
        Objects.requireNonNull(context, "El contexto de ejecución no puede ser nulo.");
        synchronizeStaminaMasteryFlags(context);
        MasteryManifestation selected = selectedActive(context.sheet());
        return selected == null ? MasteryExecutionResult.rejected("", "No hay ninguna maestría activa seleccionada.")
                : MasteryExecutionDispatcher.executeActive(selected, context);
    }

    public MasteryExecutionResult toggleSelectedSustained(MasteryExecutionContext context) {
        Objects.requireNonNull(context, "El contexto de ejecución no puede ser nulo.");
        synchronizeStaminaMasteryFlags(context);
        MasteryManifestation selected = selectedSustained(context.sheet());
        return selected == null ? MasteryExecutionResult.rejected("", "No hay ninguna maestría sostenida seleccionada.")
                : MasteryExecutionDispatcher.toggleSustained(selected, context, activeManifestations);
    }

    public MasteryActionResult executeSelectedActive(MasteryRuntimeContext context) {
        return executeSelectedActive(MasteryExecutionContext.fromRuntimeContext(context)).toActionResult();
    }
    public MasteryActionResult toggleSelectedSustained(MasteryRuntimeContext context) {
        return toggleSelectedSustained(MasteryExecutionContext.fromRuntimeContext(context)).toActionResult();
    }

    /** Compatibilidad interna para verificaciones antiguas sin recursos de gameplay. */
    public MasteryActionResult executeSelectedActive(CharacterSheet sheet) {
        return new MasteryActionResult(false, "La ejecución activa exige MasteryRuntimeContext.");
    }
    public MasteryActionResult toggleSelectedSustained(CharacterSheet sheet) {
        return new MasteryActionResult(false, "La ejecución sostenida exige MasteryRuntimeContext.");
    }

    public List<TransmutationNode> visibleTransmutationNodes(CharacterSheet sheet) {
        if (knowledgeState(MasteryId.TRANSMUTACION) == MasteryKnowledgeState.UNKNOWN) return List.of();
        TransmutationMastery mastery = (TransmutationMastery) MasteryCatalog.require(MasteryId.TRANSMUTACION);
        return mastery.orderedNodes().stream()
                .filter(node -> unlockedNodes.contains(node.id()) && node.meetsAttributeRequirement(sheet))
                .toList();
    }

    public boolean isTransmutationNodeAvailable(TransmutationNode node, CharacterSheet sheet) {
        if (node.id() == TransmutationNodeId.OVERCLOCK) return unlockedNodes.contains(node.id()) && node.meetsAttributeRequirement(sheet);
        boolean prerequisiteUnlocked = node.prerequisiteOptional().map(unlockedNodes::contains).orElse(true);
        return prerequisiteUnlocked && node.meetsAttributeRequirement(sheet);
    }

    public MasteryActionResult unlockAvailableTransmutationNode(TransmutationNodeId id, CharacterSheet sheet) {
        TransmutationMastery mastery = (TransmutationMastery) MasteryCatalog.require(MasteryId.TRANSMUTACION);
        TransmutationNode node = mastery.node(id);
        if (!isTransmutationNodeAvailable(node, sheet)) {
            return new MasteryActionResult(false, node.name() + " todavía no cumple sus requisitos.");
        }
        boolean added = unlockedNodes.add(id);
        return new MasteryActionResult(added, added ? node.name() + " ha sido desbloqueada." : node.name() + " ya estaba desbloqueada.");
    }

    public List<MasteryVariant> visibleVariants(PairMastery pair, CharacterSheet sheet) {
        if (knowledgeState(pair.id()) != MasteryKnowledgeState.UNLOCKED) return List.of();
        return java.util.stream.Stream.of(pair.original(), pair.refined())
                .filter(variant -> variant.isAccessibleTo(sheet)).toList();
    }

    public List<MasteryStage> visibleStages(StructuredMastery structured, CharacterSheet sheet) {
        if (knowledgeState(structured.id()) != MasteryKnowledgeState.UNLOCKED) return List.of();
        return structured.stages().stream()
                .filter(stage -> stage.isAccessibleTo(sheet))
                .filter(stage -> stageVisibleAsUnlocked(structured.id(), stage))
                .toList();
    }

    public Set<String> unlockedStructuredStageIds() { return Set.copyOf(unlockedStructuredStages); }
    public Set<String> runtimeActiveEffects() { return Set.copyOf(runtimeActiveEffects); }
    public void restoreKnowledge(java.util.Collection<String> revealed, java.util.Collection<String> unlocked) {
        for (MasteryId id : MasteryId.values()) knowledge.put(id, MasteryKnowledgeState.UNKNOWN);
        for (String id : revealed) knowledge.put(MasteryId.valueOf(id), MasteryKnowledgeState.REVEALED);
        for (String id : unlocked) knowledge.put(MasteryId.valueOf(id), MasteryKnowledgeState.UNLOCKED);
    }
    public void restoreUnlockedStructuredStageIds(java.util.Collection<String> ids) { unlockedStructuredStages.clear(); unlockedStructuredStages.addAll(ids); }
    public void restoreUnlockedTransmutationNodeIds(java.util.Collection<String> ids) { unlockedNodes.clear(); for(String id:ids) unlockedNodes.add(TransmutationNodeId.valueOf(id)); }
    public void restoreRuntimeActiveEffects(java.util.Collection<String> ids) { runtimeActiveEffects.clear(); runtimeActiveEffects.addAll(ids); }
    public Set<String> activeSustainedManifestationIds() { return Set.copyOf(activeManifestations); }
    public void restoreActiveSustainedManifestationIds(java.util.Collection<String> ids) { activeManifestations.clear(); activeManifestations.addAll(ids); }
    public void restoreRegisteredPassives(CharacterSheet sheet, MasteryEventBus bus, java.util.Collection<String> ids) {
        PassiveMasteryRegistrar.restore(this, sheet, bus, ids);
    }
    public Set<String> synchronizeRegisteredPassives(CharacterSheet sheet, MasteryEventBus bus) {
        return PassiveMasteryRegistrar.synchronize(this, sheet, bus);
    }
    public Set<String> synchronizeRegisteredPassives(CharacterSheet sheet, MasteryEventBus bus,
                                                      NullificationPolicy.SuppressionState suppression) {
        Objects.requireNonNull(suppression);
        if (!NullificationPolicy.masteryUsable(suppression)) {
            bus.clear();
            return Set.of();
        }
        return PassiveMasteryRegistrar.synchronize(this, sheet, bus);
    }

    public boolean isActive(String name) { return activeManifestations.contains(name) || activeManifestations.contains(name.toUpperCase(java.util.Locale.ROOT)); }
    public Set<TransmutationNodeId> unlockedTransmutationNodes() { return Set.copyOf(unlockedNodes); }

    /** Desbloquea en cascada todos los nodos de TRANSMUTACIÓN cuyo umbral y prerrequisito ya se cumplen. */
    public void unlockAvailableTransmutationNodes(CharacterSheet sheet) {
        if (knowledgeState(MasteryId.TRANSMUTACION) != MasteryKnowledgeState.UNLOCKED) return;
        TransmutationMastery mastery = (TransmutationMastery) MasteryCatalog.require(MasteryId.TRANSMUTACION);
        boolean changed;
        do {
            changed = false;
            for (TransmutationNode node : mastery.orderedNodes()) {
                if (unlockedNodes.contains(node.id())) continue;
                if (isTransmutationNodeAvailable(node, sheet)) {
                    unlockedNodes.add(node.id());
                    changed = true;
                }
            }
        } while (changed);
    }

    public MasteryStage highestAccessibleStage(StructuredMastery mastery, CharacterSheet sheet) {
        MasteryStage selected = null;
        for (MasteryStage stage : mastery.stages()) {
            if (stage.isAccessibleTo(sheet)) selected = stage;
        }
        return selected;
    }

    private TransmutationNode highestAvailableTransmutationNode(TransmutationMastery mastery, CharacterSheet sheet) {
        TransmutationNode selected = mastery.node(TransmutationNodeId.OVERCLOCK);
        for (TransmutationNode node : mastery.orderedNodes()) {
            if (unlockedNodes.contains(node.id()) && node.meetsAttributeRequirement(sheet)) selected = node;
        }
        return selected;
    }

}
