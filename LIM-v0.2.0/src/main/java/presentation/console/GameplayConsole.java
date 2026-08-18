package presentation.console;

import application.InventoryAccessResult;
import application.InventoryAccessService;
import domain.ability.EvolutiveMastery;
import domain.ability.CharacterMasteryCollection;
import domain.ability.Mastery;
import domain.ability.MasteryActionResult;
import domain.ability.MasteryCatalog;
import domain.ability.MasteryVariant;
import domain.ability.PairMastery;
import domain.ability.StructuredMastery;
import domain.ability.MasteryStage;
import domain.ability.MasteryType;
import domain.ability.MasteryManifestation;
import domain.ability.MasteryKnowledgeState;
import domain.ability.TransmutationMastery;
import domain.ability.TransmutationNode;
import domain.character.sheet.CharacterSheet;
import domain.communication.*;
import domain.inventory.InventoryEntry;
import domain.inventory.QuickAccessPolicy;
import domain.inventory.RotorBackHandService;
import domain.inventory.item.misc.AstrolabeItem;
import domain.inventory.item.ArtifactAccessory;
import domain.inventory.item.AccessoryItem;
import domain.inventory.equipment.EquipmentSlot;
import domain.orientation.AstrolabeUsePolicy;
import domain.orientation.MovementState;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.*;
import domain.hud.GameplayTimeState;
import domain.hud.HudMode;
import domain.hud.HudModeCyclePolicy;
import domain.hud.PauseOrigin;
import domain.hud.EngineerSpineProjectionService;
import domain.save.GameSessionState;
import domain.persona.PersonaProfile;
import domain.control.ControlAction;
import domain.control.ControlMode;
import domain.settings.ConfigurationContext;
import domain.settings.GameSettings;
import domain.worldmemory.access.WorldMemoryService;
import domain.worldmemory.spatial.WorldCoordinate;
import domain.worldmemory.spatial.TerrainObservation;
import domain.worldmemory.spatial.TerrainSurface;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.KnowledgeReliability;
import presentation.menu.CharacterSheetScreen;
import presentation.menu.InventoryScreen;
import presentation.menu.WorldMemoryScreen;
import presentation.settings.ConfigurationScreen;

import java.io.PrintStream;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

public final class GameplayConsole {
    private final GameSessionState savedGame;
    private final InventoryScreen inventoryScreen;
    private final CharacterSheetScreen characterSheetScreen;
    private final ConsoleInput input;
    private final PrintStream output;
    private InventoryAccessService inventoryAccess;
    private WorldMemoryService worldMemory;
    private WorldMemoryScreen worldMemoryScreen;
    private final EngineerSpineProjectionService engineerSpineProjection = new EngineerSpineProjectionService();
    private final GameSettings settings;
    private final ConfigurationScreen configurationScreen;
    private final WeaponInputResolutionPolicy weaponInputPolicy = new WeaponInputResolutionPolicy();
    private final DualWieldComboState dualWieldComboState = new DualWieldComboState();
    private final AstrolabeUsePolicy astrolabeUsePolicy = new AstrolabeUsePolicy();
    private final CommunicationPairingService communicationPairingService = new CommunicationPairingService();
    private final application.save.GameSaveService gameSaveService;
    private final PersonaProfile persona;

    private boolean interactionOpen;
    private boolean airborne;
    private boolean targetLocked;
    private final HudModeCyclePolicy hudModeCyclePolicy = new HudModeCyclePolicy();
    private final GameplayTimeState gameplayTimeState = new GameplayTimeState();
    private HudMode hudMode = HudMode.REALTIME;
    private String stance = "TROTAR";
    private boolean dualWielding;
    private WorldCoordinate currentPosition = new WorldCoordinate(0, 0, 0);

    public GameplayConsole(
            GameSessionState savedGame,
            InventoryScreen inventoryScreen,
            CharacterSheetScreen characterSheetScreen,
            ConsoleInput input,
            PrintStream output,
            GameSettings settings,
            ConfigurationScreen configurationScreen,
            PersonaProfile persona,
            application.save.GameSaveService gameSaveService
    ) {
        this.savedGame = Objects.requireNonNull(savedGame);
        this.inventoryScreen = Objects.requireNonNull(inventoryScreen);
        this.characterSheetScreen = Objects.requireNonNull(characterSheetScreen);
        this.input = Objects.requireNonNull(input);
        this.output = Objects.requireNonNull(output);
        this.inventoryAccess = new InventoryAccessService(savedGame.hostileEncounterState());
        this.worldMemory = new WorldMemoryService(savedGame.hostileEncounterState(), savedGame.worldMemory());
        this.worldMemoryScreen = new WorldMemoryScreen(savedGame.worldMemory(), input, output);
        this.settings = Objects.requireNonNull(settings);
        this.configurationScreen = Objects.requireNonNull(configurationScreen);
        this.persona=Objects.requireNonNull(persona);
        this.gameSaveService=Objects.requireNonNull(gameSaveService);
    }

    public void play() {
        output.println();
        output.println("EL MUNDO HA SIDO CARGADO");
        output.println("Escriba AYUDA para consultar todas las órdenes o SALIR para desencarnar.");
        renderEngineerSpineProjection();
        boolean playing = true;
        while (playing) {
            refreshCommunicationPairing();
            String command = normalize(input.readText("> "));
            if (gameplayTimeState.isPaused() && !command.equals("P") && !command.equals("ESC") && !command.equals("OPTIONS")
                    && !command.equals("SHARE HOLD") && !command.equals("SHARE MANTENER")
                    && !command.equals("AYUDA") && !command.equals("SALIR")) {
                output.println("La partida está pausada. Use P o SHARE mantenido para reanudar, o ESC/OPTIONS para abrir el menú.");
                continue;
            }

            var customAction = settings.actionForCustomInput(command);
            if (customAction.isPresent()) {
                playing = executeRemapped(customAction.get(), playing);
                continue;
            }
            if (settings.controlMode() == ControlMode.PS4_CONTROLLER) {
                ControllerCommandResult result = executePs4Command(command, playing);
                if (result.recognized()) {
                    playing = result.playing();
                    continue;
                }
                if (!isUniversalCommand(command)) {
                    output.println("Orden no reconocida para MANDO PS4. Escriba AYUDA.");
                    continue;
                }
            } else if (isPs4SpecificCommand(command)) {
                output.println("Ese botón pertenece al modo MANDO PS4. Cambie el modo en CONFIGURAR.");
                continue;
            }

            switch (command) {
                case "AYUDA" -> showHelp();
                case "MOVER RATÓN", "MOUSE MOVE" -> output.println("La cámara sigue el movimiento del ratón.");
                case "STICK DERECHO", "RIGHT STICK" -> output.println("La cámara sigue el stick derecho.");
                case "STICK IZQUIERDO", "LEFT STICK" -> output.println("Kenan se mueve mediante el stick izquierdo.");
                case "L3" -> cycleStance();
            case "L3 HOLD", "L3 MANTENER" -> output.println("Kenan corre mientras L3 permanezca pulsado.");
                case "R3" -> toggleTargetLock();
                case "CÍRCULO", "CIRCLE" -> feint("según la dirección del stick izquierdo");
                case "R1" -> rightClick();
                case "R2" -> resolveWeaponInput(WeaponInput.HEAVY_PRESS, false);
                case "R2 HOLD", "R2 MANTENER" -> resolveWeaponInput(WeaponInput.CHARGED_HOLD, false);
                case "L1", "L1 HOLD", "L1 MANTENER" -> resolveWeaponInput(WeaponInput.LEFT_HOLD, false);
                case "L2" -> resolveWeaponInput(WeaponInput.LEFT_PRESS, false);
                case "TRIÁNGULO", "TRIANGLE" -> toggleInteraction();
                case "TRIÁNGULO HOLD", "TRIANGLE HOLD" -> output.println("Siguiente acción contextual o línea de diálogo.");
                case "CUADRADO", "SQUARE" -> toggleActiveWeapon();
                case "D-PAD DERECHA", "DPAD RIGHT" -> switchWeaponOrDualWield();
                case "D-PAD IZQUIERDA", "DPAD LEFT" -> cycleActiveWeaponConfiguration();
                case "D-PAD ARRIBA", "DPAD UP" -> executeActiveMastery();
                case "D-PAD ARRIBA HOLD", "DPAD UP HOLD" -> openMasteryWheel(MasteryType.ACTIVE);
                case "D-PAD ABAJO HOLD", "DPAD DOWN HOLD" -> openMasteryWheel(MasteryType.SUSTAINED);
                case "D-PAD ABAJO", "DPAD DOWN" -> toggleSustainedMastery();
                
                case "DESLIZAR TOUCHPAD IZQUIERDA", "TOUCHPAD IZQUIERDA" -> useQuickAccess(1);
                case "DESLIZAR TOUCHPAD ARRIBA", "TOUCHPAD ARRIBA" -> useQuickAccess(2);
                case "DESLIZAR TOUCHPAD DERECHA", "TOUCHPAD DERECHA" -> useQuickAccess(3);
                case "DESLIZAR TOUCHPAD ABAJO", "TOUCHPAD ABAJO" -> useQuickAccess(4);
                case "TOUCHPAD" -> openInventory();
                case "TOUCHPAD HOLD", "TOUCHPAD MANTENER" -> characterSheetScreen.open();
                case "SHARE" -> toggleWorldMemory();
                case "SHARE HOLD", "SHARE MANTENER" -> cycleHudState();
                case "OPTIONS" -> playing = openPauseMenu();
                case "W", "A", "S", "D" -> move(command);
                case "SHIFT HOLD", "SHIFT MANTENER" -> output.println("Kenan corre mientras SHIFT permanezca pulsado.");
                case "C" -> cycleStance();
                case "C HOLD", "C MANTENER" -> output.println("Kenan camina mientras C permanezca pulsado.");
                case "SPACE" -> feint("atrás");
                case "SPACE W" -> feint("hacia delante");
                case "SPACE A" -> feint("a la izquierda");
                case "SPACE D" -> feint("a la derecha");
                case "SPACE SPACE", "SPACE DOUBLE", "DOBLE SPACE" -> jump();
                case "E" -> activateEquippedArtifactOrInteract();
                case "Q" -> contextualQ();
                case "TAB" -> toggleActiveWeapon();
                case "B" -> callSelectedPersonalTransport();
                case "B HOLD", "B MANTENER" -> openPersonalTransportWheel();
                case "MOUSE WHEEL CLICK", "WHEEL CLICK", "RUEDA CLICK" -> cycleActiveWeaponConfiguration();
                case "RIGHT CLICK" -> rightClick();
                case "ALT", "ALT IZQ" -> resolveWeaponInput(WeaponInput.HEAVY_PRESS, false);
                case "ALT HOLD", "ALT MANTENER", "ALT IZQ HOLD", "ALT IZQ MANTENER" -> resolveWeaponInput(WeaponInput.CHARGED_HOLD, false);
                case "LEFT CLICK" -> resolveWeaponInput(WeaponInput.LEFT_PRESS, false);
                case "LEFT CLICK HOLD", "LEFT CLICK MANTENER" -> resolveWeaponInput(WeaponInput.LEFT_HOLD, false);
                case "MOUSE WHEEL FORWARD", "WHEEL FORWARD", "RUEDA ARRIBA", "MOUSE WHEEL BACKWARD", "WHEEL BACKWARD", "RUEDA ABAJO" -> output.println("La rueda del ratón ya no selecciona maestrías.");
                case "Z" -> executeActiveMastery();
                case "Z HOLD", "Z MANTENER" -> openMasteryWheel(MasteryType.ACTIVE);
                case "X" -> toggleSustainedMastery();
                case "X HOLD", "X MANTENER" -> openMasteryWheel(MasteryType.SUSTAINED);
                
                case "1", "2", "3", "4" -> useQuickAccess(Integer.parseInt(command));
                case "5" -> quickSave();
                case "I" -> openInventory();
                case "H" -> characterSheetScreen.open();
                case "M" -> toggleWorldMemory();
                case "P" -> cycleHudState();
                case "ESTADO TRAJE", "TRAJE" -> renderEngineerSpineProjection();
                case "F" -> toggleTargetLock();
                case "ESC" -> playing = openPauseMenu();
                case "SALIR" -> playing = false;
                default -> output.println("Orden no reconocida. Escriba AYUDA.");
            }
        }
    }

    private void quickSave() {
        var slot=gameSaveService.saveQuick(persona.id(), application.save.GameSnapshotFactory.from(savedGame,persona));
        persona.registerSave(slot); output.println("Guardado rápido actualizado.");
    }

    private String normalize(String raw) {
        return raw.trim().toUpperCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private void move(String direction) {
        if (airborne) airborne = false;
        currentPosition = switch (direction) {
            case "W" -> new WorldCoordinate(currentPosition.x(), currentPosition.y() + 10, currentPosition.elevation());
            case "S" -> new WorldCoordinate(currentPosition.x(), currentPosition.y() - 10, currentPosition.elevation());
            case "A" -> new WorldCoordinate(currentPosition.x() - 10, currentPosition.y(), currentPosition.elevation());
            case "D" -> new WorldCoordinate(currentPosition.x() + 10, currentPosition.y(), currentPosition.elevation());
            default -> currentPosition;
        };
        savedGame.worldMemory().knowledge().recordTerrain(new TerrainObservation(
                currentPosition,
                TerrainSurface.UNKNOWN,
                12,
                Instant.now(),
                WorldKnowledgeSource.now(
                        KnowledgeSourceType.DIRECT_EXPLORATION,
                        "recorrido-de-kenan",
                        KnowledgeReliability.OBSERVED)
        ));
        output.println("Movimiento " + direction + " en postura " + stance.toLowerCase(Locale.ROOT) + ".");
        renderEngineerSpineProjection();
    }

    private void renderEngineerSpineProjection() {
        var indicator = engineerSpineProjection.project(savedGame.currentInventory().equipment());
        if (!indicator.visible()) return;
        output.printf("[COLUMNA DEL INGENIERO] refrigerante cian %.0f%%%n", indicator.levelRatio() * 100.0);
    }

    private void cycleStance() {
        stance = switch (stance) {
            case "TROTAR" -> "AGACHARSE";
            case "AGACHARSE" -> "GATEAR";
            default -> "TROTAR";
        };
        output.println("Postura: " + stance + ".");
    }

    private void feint(String direction) {
        output.println("Finta " + direction + ".");
    }

    private void jump() {
        airborne = true;
        output.println("Kenan salta. RIGHT CLICK ejecutará un ataque con salto antes de aterrizar.");
    }

    private void activateEquippedArtifactOrInteract() {
        var equipped = savedGame.currentInventory().equipment().itemAt(EquipmentSlot.ACCESSORY);
        if (equipped.isPresent() && equipped.get() instanceof ArtifactAccessory artifact) {
            if (savedGame.characterSheet().valueOf(artifact.activationAttribute()) < artifact.activationMinimum()) {
                // la interfaz no revela que exista una función oculta ni su requisito.
                toggleInteraction();
                return;
            }
            if (artifact instanceof AstrolabeItem astrolabe) {
                var result = astrolabeUsePolicy.orient(astrolabe, savedGame.currentInventory(), savedGame.worldMemory(), currentPosition,
                        MovementState.standingOnFoot(), savedGame.animationState());
                output.println(result.message());
                if (result.successful()) {
                    output.println("ORIENTARSE | " + result.solution().targetTitle() + " | " + result.solution().direction() + " | "
                            + String.format(Locale.ROOT, "%.1f°", result.solution().headingDegrees()) + ".");
                }
                return;
            }
            output.println("Artefacto activado: " + ((AccessoryItem) equipped.get()).name() + ".");
            return;
        }
        toggleInteraction();
    }

    private void toggleInteraction() {
        interactionOpen = !interactionOpen;
        output.println(interactionOpen ? "Interacción contextual abierta." : "Interacción contextual cerrada.");
    }

    private void contextualQ() {
        if (interactionOpen) {
            output.println("Siguiente acción contextual o línea de diálogo.");
        } else {
            switchWeaponOrDualWield();
        }
    }

    private void rightClick() {
        if (airborne) {
            attemptOffensiveAction("Ataque con salto");
            airborne = false;
        } else {
            resolveWeaponInput(WeaponInput.RIGHT_PRESS, false);
        }
    }

    private void resolveWeaponInput(WeaponInput inputGesture, boolean withinParryWindow) {
        ResolvedWeaponHandling handling;
        try {
            handling = currentHandling();
        } catch (IllegalStateException | IllegalArgumentException exception) {
            output.println("Acción bloqueada: " + exception.getMessage());
            return;
        }
        if (handling.wieldingState() == WieldingState.UNARMED && !savedGame.unarmedGuardDrawn()) {
            output.println("Acción bloqueada: la guardia de DESARMADO está envainada.");
            return;
        }
        boolean improvisedBracerAvailable = new domain.combat.ImprovisedBracerBlockPolicy()
                .canBlock(savedGame.currentInventory().equipment());
        var result = weaponInputPolicy.resolve(
                inputGesture, handling, withinParryWindow, improvisedBracerAvailable, dualWieldComboState);
        if (!result.allowed()) {
            output.println("Acción bloqueada: " + result.reason());
            return;
        }
        output.println(result.reason());
    }

    private ResolvedWeaponHandling currentHandling() {
        var inventory = savedGame.currentInventory();
        if (RotorBackHandService.active(inventory)) {
            WeaponItem rotor = RotorBackHandService.equippedRotor(inventory).orElseThrow();
            var configuration = rotor.availableConfigurations().stream()
                    .filter(c -> c.gripMode() == GripMode.TWO_HANDED)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("El Espadón de Rotor no dispone de configuración bimanual."));
            rotor.selectConfiguration(configuration);
            return new ResolvedWeaponHandling(
                    ResolvedHand.active(EquipmentSlot.RIGHT_HAND, rotor, configuration),
                    ResolvedHand.empty(EquipmentSlot.LEFT_HAND),
                    WieldingState.SINGLE_WIELD
            );
        }
        return WeaponHandlingResolver.resolve(inventory.equipment(), dualWielding, savedGame.characterSheet());
    }

    private void refreshCommunicationPairing() {
        var profile = savedGame.environmentalCycle().weatherProfile();
        var candidates = savedGame.nearbyCommunicationRegistry().snapshot();
        InventoryEntry head = savedGame.currentInventory().equipment().itemAt(EquipmentSlot.HEAD).orElse(null);
        CommunicationDeviceType equipped = CommunicationDevicePolicy.deviceTypeOf(head);
        for (CommunicationDeviceType device : CommunicationDeviceType.values()) {
            communicationPairingService.refresh(
                    savedGame.communicationPairingState(), device, profile, candidates, device == equipped);
        }
    }

    private void openInventory() {
        InventoryAccessResult result = inventoryAccess.requestAccess();
        if (!result.allowed()) { output.println(result.message()); return; }
        inventoryScreen.open();
    }

    private void toggleWorldMemory() {
        var result = worldMemory.toggle();
        if (!result.allowed()) {
            if (!result.message().isBlank()) output.println(result.message());
            return;
        }
        output.println("[Animación: recordar]");
        if (!result.message().isBlank()) output.println(result.message());
        if (result.open()) worldMemoryScreen.display();
    }

    private void openMasteryWheel(MasteryType type) {
        CharacterSheet sheet = savedGame.characterSheet();
                var options = savedGame.masteries().selectableManifestations(type, sheet);
        if (options.isEmpty()) return;
        output.println(type == MasteryType.ACTIVE
                ? "RUEDA DE MAESTRÍAS ACTIVAS" : "RUEDA DE MAESTRÍAS SOSTENIDAS");
        for (int i = 0; i < options.size(); i++) {
            MasteryManifestation option = options.get(i);
            output.printf("%d. %s — %s%n", i + 1, option.familyName(), option.name());
        }
        int chosen = input.readIntegerBetween("Desplace el cursor radial y confirme el sector: ", 1, options.size());
        MasteryManifestation selected = savedGame.masteries().select(type, chosen - 1, sheet);
        if (selected != null) output.println("Maestría seleccionada: " + selected.name() + ".");
    }

    private void executeActiveMastery() {
                MasteryActionResult result = savedGame.masteries().executeSelectedActive(masteryRuntimeContext());
        if (!result.message().isBlank()) output.println(result.message());
    }

    private void toggleSustainedMastery() {
                MasteryActionResult result = savedGame.masteries().toggleSelectedSustained(masteryRuntimeContext());
        if (!result.message().isBlank()) output.println(result.message());
    }

    private domain.ability.MasteryRuntimeContext masteryRuntimeContext() {
        return new domain.ability.MasteryRuntimeContext(
                savedGame.characterSheet(),
                savedGame.character().identity().gender(),
                savedGame.vitalResources(),
                savedGame.hostileEncounterState(),
                true
        );
    }

    private void toggleActiveWeapon() {
        ResolvedWeaponHandling handling = currentHandling();
        WeaponItem active = activeWeapon(handling);
        if (active != null) {
            if (!active.toggleSheathing(savedGame.characterSheet())) {
                output.println("El objeto activo no puede cambiar de estado.");
                return;
            }
            dualWielding = false;
            output.println(active.isSheathed() ? "Objeto guardado." : "Objeto desenvainado.");
            return;
        }

        WeaponItem candidate = equippedWeaponAt(EquipmentSlot.RIGHT_HAND);
        if (candidate == null) candidate = equippedWeaponAt(EquipmentSlot.LEFT_HAND);
        if (candidate == null) {
            boolean drawn = savedGame.toggleUnarmedGuard();
            output.println(drawn ? "Guardia de DESARMADO adoptada." : "Guardia de DESARMADO envainada.");
            return;
        }
        if (!candidate.isSheathed()) {
            output.println("No hay un objeto guardado que desenvainar.");
            return;
        }
        candidate.drawForHandlingTransition();
        output.println("Objeto desenvainado: " + candidate.name() + ".");
    }

    private WeaponItem equippedWeaponAt(EquipmentSlot hand) {
        return savedGame.currentInventory().equipment().itemAt(hand)
                .filter(WeaponItem.class::isInstance)
                .map(WeaponItem.class::cast)
                .orElse(null);
    }

    private WeaponItem activeWeapon(ResolvedWeaponHandling handling) {
        if (handling.wieldingState() == WieldingState.UNARMED) return null;
        return handling.rightHand().weapon()
                .or(() -> handling.leftHand().weapon())
                .orElse(null);
    }

    private void switchWeaponOrDualWield() {
        var inventory = savedGame.currentInventory();
        WeaponItem right = equippedWeaponAt(EquipmentSlot.RIGHT_HAND);
        WeaponItem left = equippedWeaponAt(EquipmentSlot.LEFT_HAND);

        // Rotor desplegado: antes de ceder las manos debe retraerse. Sin dorsal, esto es imposible.
        if (RotorBackHandService.active(inventory)) {
            try {
                savedGame.replaceCurrentInventory(RotorBackHandService.retract(inventory));
                dualWielding = false;
                dualWieldComboState.reset();
                restorePrimaryHandAfterRotor(right,left);
            } catch (IllegalArgumentException | IllegalStateException exception) {
                output.println("Acción bloqueada: " + exception.getMessage());
            }
            return;
        }

        // Un Rotor empuñado convencionalmente sin BACK_HAND no dispone de vaina. Si posteriormente se equipa
        // el dorsal puede trasladarse a BACK_HAND; sin él no puede ceder las manos a otra arma.
        WeaponItem handRotor = activeHandRotor(right,left);
        if (handRotor != null) {
            if (!inventory.logistics().compartment(domain.inventory.logistics.InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).available()) {
                output.println("Acción bloqueada: el Espadón de Rotor no puede envainarse sin el Sistema de Transporte Dorsal del Rotor V881.");
                return;
            }
            try {
                savedGame.replaceCurrentInventory(RotorBackHandService.moveActiveHandRotorToBackHand(inventory,handRotor));
                dualWielding=false; dualWieldComboState.reset();
                output.println("Espadón de Rotor retraído en BACK_HAND mediante el sistema dorsal.");
            } catch (IllegalArgumentException | IllegalStateException exception) {
                output.println("Acción bloqueada: " + exception.getMessage());
            }
            return;
        }

        boolean rotorRetracted = RotorBackHandService.equippedRotor(inventory).map(WeaponItem::isSheathed).orElse(false);
        if (right == null && left == null) {
            if (rotorRetracted) deployDorsalRotor();
            else output.println("Ambas manos están libres: DESARMADO permanece activo.");
            return;
        }

        if (dualWielding) {
            if (rotorRetracted) deployDorsalRotor();
            else exitDualWield(right,left);
            return;
        }

        if (right != null && left != null) {
            try {
                if (right.isSheathed()) right.drawForHandlingTransition();
                if (left.isSheathed()) left.drawForHandlingTransition();
                DualWieldConfigurationPolicy.activate(right,left);
                dualWielding=true; dualWieldComboState.reset();
                output.println("Dual wielding activado: mano derecha en modo principal y mano izquierda en modo alternativo.");
            } catch (IllegalArgumentException | IllegalStateException exception) {
                if (rotorRetracted) deployDorsalRotor();
                else {
                    WeaponItem active = !right.isSheathed()?right:(!left.isSheathed()?left:null);
                    if(active==right){
                        right.stowForHandlingTransition(); left.drawForHandlingTransition();
                        output.println("Arma activa trasladada a la mano izquierda: "+left.name()+".");
                    } else {
                        if(!left.isSheathed()) left.stowForHandlingTransition();
                        right.drawForHandlingTransition();
                        output.println("Arma activa trasladada a la mano derecha: "+right.name()+".");
                    }
                }
            }
            return;
        }

        WeaponItem only=right!=null?right:left;
        if(rotorRetracted && !only.isSheathed()) { deployDorsalRotor(); return; }
        if(only.isSheathed()) only.drawForHandlingTransition();
        output.println("Arma activa: "+only.name()+".");
    }

    private void deployDorsalRotor() {
        dualWielding=false; dualWieldComboState.reset();
        try {
            savedGame.replaceCurrentInventory(RotorBackHandService.deploy(savedGame.currentInventory()));
            output.println("Espadón de Rotor desplegado desde BACK_HAND: monopoliza ambas manos físicas; LEFT_HAND y RIGHT_HAND permanecen equipadas y envainadas.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            output.println("Acción bloqueada: "+exception.getMessage());
        }
    }

    private void restorePrimaryHandAfterRotor(WeaponItem right, WeaponItem left) {
        if(right!=null){
            if(right.isSheathed()) right.drawForHandlingTransition();
            output.println("Espadón de Rotor retraído en BACK_HAND; vuelve a estar activa la mano derecha: "+right.name()+".");
        } else if(left!=null){
            if(left.isSheathed()) left.drawForHandlingTransition();
            output.println("Espadón de Rotor retraído en BACK_HAND; vuelve a estar activa la mano izquierda: "+left.name()+".");
        } else output.println("Espadón de Rotor retraído en BACK_HAND; DESARMADO queda activo.");
    }

    private WeaponItem activeHandRotor(WeaponItem right, WeaponItem left) {
        if(right!=null && right.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE) && !right.isSheathed()) return right;
        if(left!=null && left.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE) && !left.isSheathed()) return left;
        return null;
    }

    private void exitDualWield(WeaponItem right, WeaponItem left) {
        if (right == null || left == null) {
            dualWielding = false;
            output.println("Dual wielding cancelado por pérdida de una de las manos.");
            return;
        }
        DualWieldConfigurationPolicy.exitToSingleRight(right, left);
        left.stowForHandlingTransition();
        dualWielding = false;
        dualWieldComboState.reset();
        output.println(left.hasTrait(WeaponTrait.SHIELD)
                ? "Dual wielding desactivado: el escudo izquierdo queda sujeto a la espalda por el tahalí y la mano derecha pasa a modo alternativo."
                : "Dual wielding desactivado: el arma izquierda queda envainada y la mano derecha pasa a modo alternativo.");
    }

    private void cycleActiveWeaponConfiguration() {
        if (dualWielding) {
            WeaponItem left = equippedWeaponAt(EquipmentSlot.LEFT_HAND);
            WeaponItem right = equippedWeaponAt(EquipmentSlot.RIGHT_HAND);
            WeaponItem shield = left != null && left.hasTrait(WeaponTrait.SHIELD) ? left
                    : right != null && right.hasTrait(WeaponTrait.SHIELD) ? right : null;
            if (shield != null) {
                var position = shield.toggleShieldGuardPosition();
                output.println("Guardia de " + shield.name() + ": " + position.name() + ".");
                return;
            }
            exitDualWield(right, left);
            return;
        }
        ResolvedWeaponHandling handling = currentHandling();
        WeaponItem weapon = activeWeapon(handling);
        if (weapon == null) {
            var mode = weaponInputPolicy.toggleUnarmedMode();
            output.println("DESARMADO: " + (mode == WeaponActionMode.PRIMARY ? "guardia diestra adelantada" : "guardia zurda adelantada") + ". La resolución lógica permanece RIGHT_HAND + 2H.");
            return;
        }
        if (weapon.hasTrait(WeaponTrait.SHIELD)) {
            var position = weapon.toggleShieldGuardPosition();
            output.println("Guardia de " + weapon.name() + ": " + position.name() + ".");
            return;
        }
        boolean lightComboActive = dualWieldComboState.hasActiveLightCombo();
        int preservedOrdinal = dualWieldComboState.nextLightAttackOrdinal();
        var configuration = weapon.cycleConfiguration();
        output.println("Configuración de " + weapon.name() + ": "
                + configuration.gripMode().label() + " + " + configuration.actionMode().label() + ".");
        if (lightComboActive) {
            output.println("Combo LIGHT preservado al cambiar de modo: el siguiente golpe intenta el ordinal "
                    + preservedOrdinal + " en " + configuration.actionMode().label() + ".");
        }
    }

    private void attemptOffensiveAction(String action) {
        WeaponInput input = combatInputOf(action);
        if (input == null) {
            output.println("Acción ofensiva desconocida: " + action + ".");
            return;
        }
        resolveWeaponInput(input, false);
    }

    private WeaponInput combatInputOf(String action) {
        String normalized = action.toLowerCase(Locale.ROOT);
        if (normalized.contains("salto")) return WeaponInput.JUMP_PRESS;
        if (normalized.contains("ligero")) return WeaponInput.RIGHT_PRESS;
        if (normalized.contains("cargado")) return WeaponInput.CHARGED_HOLD;
        if (normalized.contains("fuerte")) return WeaponInput.HEAVY_PRESS;
        if (normalized.contains("desestabil")) return WeaponInput.DESTABILIZE_PRESS;
        return null;
    }

    private void useQuickAccess(int slot) {
        var inventory = savedGame.currentInventory();
        if (!QuickAccessPolicy.isSlotAvailable(slot, inventory.equipment(), inventory.logistics())) {
            output.println("Acceso rápido " + slot + ": NO DISPONIBLE.");
            return;
        }
        var assigned = inventory.quickAccessBar().slots().get(slot - 1);
        if (assigned.isEmpty()) {
            output.println("Acceso rápido " + slot + ": Vacío.");
            return;
        }
        InventoryEntry item = assigned.get();
        if (item instanceof AstrolabeItem astrolabe) {
            var result = astrolabeUsePolicy.orient(astrolabe, inventory, savedGame.worldMemory(), currentPosition,
                    MovementState.standingOnFoot(), savedGame.animationState());
            output.println(result.message());
            if (result.successful()) {
                output.println("ORIENTARSE | " + result.solution().targetTitle() + " | "
                        + result.solution().direction() + " | "
                        + String.format(Locale.ROOT, "%.1f°", result.solution().headingDegrees()) + ".");
            }
            return;
        }
        output.println("Acceso rápido " + slot + ": " + item.name() + ".");
    }

    private void cycleHudState() {
        hudMode = hudModeCyclePolicy.next(hudMode);
        if (hudMode.gameplayPaused()) gameplayTimeState.pause(PauseOrigin.HUD_CYCLE);
        else gameplayTimeState.resume(PauseOrigin.HUD_CYCLE);
        // La pausa contemplativa no proyecta rótulos, overlays ni mensajes de estado.
    }

    private void toggleTargetLock() {
        targetLocked = !targetLocked;
        output.println(targetLocked
                ? "Objetivo más cercano fijado a la altura del estómago. El puntero permanece invisible."
                : "Fijación de objetivo desactivada.");
    }

    private boolean openPauseMenu() {
        gameplayTimeState.pause(PauseOrigin.PAUSE_MENU);
        output.println();
        output.println("PAUSA");
        output.println("1. CARGAR ÚLTIMO PUNTO DE GUARDADO");
        output.println("2. CONFIGURAR");
        output.println("3. DESENCARNAR");
        output.println(settings.controlMode() == ControlMode.PS4_CONTROLLER ? "OPTIONS. Cerrar" : "ESC. Cerrar");
        String command = normalize(input.readText("PAUSA> "));
        boolean keepPlaying = switch (command) {
            case "3" -> false;
            case "1" -> { loadLatestSavePoint(); yield true; }
            case "2" -> { configurationScreen.open(ConfigurationContext.IN_GAME); yield true; }
            default -> { output.println("Menú de pausa cerrado."); yield true; }
        };
        gameplayTimeState.resume(PauseOrigin.PAUSE_MENU);
        return keepPlaying;
    }

    private ControllerCommandResult executePs4Command(String command, boolean playing) {
        switch (command) {
            case "STICK IZQUIERDO", "LEFT STICK" -> output.println("Kenan se mueve mediante el stick izquierdo.");
            case "STICK DERECHO", "RIGHT STICK" -> output.println("La cámara sigue el stick derecho.");
            case "L3" -> cycleStance();
            case "L3 HOLD", "L3 MANTENER" -> output.println("Kenan corre mientras L3 permanezca pulsado.");
            case "R3" -> toggleTargetLock();
            case "CÍRCULO", "CIRCLE" -> feint("según la dirección del stick izquierdo");
            case "X" -> jump();
            case "R1" -> rightClick();
            case "R2" -> resolveWeaponInput(WeaponInput.HEAVY_PRESS, false);
            case "R2 HOLD", "R2 MANTENER" -> resolveWeaponInput(WeaponInput.CHARGED_HOLD, false);
            case "L1", "L1 HOLD", "L1 MANTENER" -> resolveWeaponInput(WeaponInput.LEFT_HOLD, false);
            case "L2" -> resolveWeaponInput(WeaponInput.LEFT_PRESS, false);
            case "TRIÁNGULO", "TRIANGLE" -> toggleInteraction();
            case "TRIÁNGULO HOLD", "TRIANGLE HOLD" -> output.println("Siguiente acción contextual o línea de diálogo.");
            case "CUADRADO", "SQUARE" -> toggleActiveWeapon();
            case "D-PAD DERECHA", "DPAD RIGHT" -> switchWeaponOrDualWield();
            case "D-PAD IZQUIERDA", "DPAD LEFT" -> cycleActiveWeaponConfiguration();
            case "D-PAD ARRIBA", "DPAD UP" -> executeActiveMastery();
            case "D-PAD ARRIBA HOLD", "DPAD UP HOLD" -> openMasteryWheel(MasteryType.ACTIVE);
                case "D-PAD ABAJO HOLD", "DPAD DOWN HOLD" -> openMasteryWheel(MasteryType.SUSTAINED);
            case "D-PAD ABAJO", "DPAD DOWN" -> toggleSustainedMastery();
            
            case "DESLIZAR TOUCHPAD IZQUIERDA", "TOUCHPAD IZQUIERDA" -> useQuickAccess(1);
            case "DESLIZAR TOUCHPAD ARRIBA", "TOUCHPAD ARRIBA" -> useQuickAccess(2);
            case "DESLIZAR TOUCHPAD DERECHA", "TOUCHPAD DERECHA" -> useQuickAccess(3);
            case "DESLIZAR TOUCHPAD ABAJO", "TOUCHPAD ABAJO" -> useQuickAccess(4);
            case "TOUCHPAD" -> openInventory();
            case "TOUCHPAD HOLD", "TOUCHPAD MANTENER" -> characterSheetScreen.open();
            case "SHARE" -> toggleWorldMemory();
            case "SHARE HOLD", "SHARE MANTENER" -> cycleHudState();
            case "L1 + TRIÁNGULO", "L1 + TRIANGLE" -> callSelectedPersonalTransport();
            case "L1 + TRIÁNGULO HOLD", "L1 + TRIANGLE HOLD", "L1 + TRIÁNGULO MANTENER" -> openPersonalTransportWheel();
            case "OPTIONS" -> { return new ControllerCommandResult(true, openPauseMenu()); }
            default -> { return new ControllerCommandResult(false, playing); }
        }
        return new ControllerCommandResult(true, playing);
    }

    private boolean isUniversalCommand(String command) {
        return command.equals("AYUDA") || command.equals("SALIR");
    }

    private boolean isPs4SpecificCommand(String command) {
        return command.startsWith("STICK") || command.equals("L3") || command.equals("R3") ||
                command.startsWith("D-PAD") || command.startsWith("DPAD") || command.startsWith("TOUCHPAD") ||
                command.startsWith("OPTIONS") || command.startsWith("SHARE") || command.equals("R1") ||
                command.startsWith("R2") || command.startsWith("L1") || command.startsWith("L2") ||
                command.equals("CÍRCULO") || command.equals("CIRCLE") || command.equals("TRIÁNGULO") ||
                command.startsWith("TRIANGLE") || command.equals("CUADRADO") || command.equals("SQUARE");
    }

    private record ControllerCommandResult(boolean recognized, boolean playing) {}

    private boolean executeRemapped(ControlAction action, boolean playing) {
        switch (action) {
            case OPEN_INVENTORY -> openInventory();
            case OPEN_CHARACTER_SHEET -> characterSheetScreen.open();
            case OPEN_ACTIVE_MASTERY_WHEEL -> openMasteryWheel(MasteryType.ACTIVE);
            case OPEN_SUSTAINED_MASTERY_WHEEL -> openMasteryWheel(MasteryType.SUSTAINED);
            case TOGGLE_WORLD_MEMORY -> toggleWorldMemory();
            case CYCLE_HUD_STATE -> cycleHudState();
            case LOCK_OR_UNLOCK_TARGET -> toggleTargetLock();
            case SHEATHE_OR_UNSHEATHE -> toggleActiveWeapon();
            case LIGHT_ATTACK -> rightClick();
            case HEAVY_ATTACK -> resolveWeaponInput(WeaponInput.HEAVY_PRESS, false);
            case CHARGED_ATTACK -> attemptOffensiveAction("Ataque cargado");
            case PARRY -> resolveWeaponInput(WeaponInput.LEFT_PRESS, false);
            case DESTABILIZE -> resolveWeaponInput(WeaponInput.DESTABILIZE_PRESS, false);
            case BLOCK -> resolveWeaponInput(WeaponInput.LEFT_HOLD, false);
            case EXECUTE_ACTIVE_MASTERY -> executeActiveMastery();
            case TOGGLE_SUSTAINED_MASTERY -> toggleSustainedMastery();
            
            
            case QUICK_ACCESS_1 -> useQuickAccess(1); case QUICK_ACCESS_2 -> useQuickAccess(2);
            case QUICK_ACCESS_3 -> useQuickAccess(3); case QUICK_ACCESS_4 -> useQuickAccess(4);
            case OPEN_PAUSE_MENU -> { return openPauseMenu(); }
            case SWITCH_WEAPON -> switchWeaponOrDualWield();
            case CALL_PERSONAL_TRANSPORT -> callSelectedPersonalTransport();
            case OPEN_PERSONAL_TRANSPORT_WHEEL -> openPersonalTransportWheel();
            case CYCLE_WEAPON_CONFIGURATION -> cycleActiveWeaponConfiguration();
            case CYCLE_STANCE -> cycleStance();
            case RUN -> output.println("Kenan corre mientras se mantenga la entrada.");
            case MOVE_CAMERA -> output.println("Cámara desplazada.");
            default -> output.println("Orden reasignada ejecutada: " + action + ".");
        }
        return playing;
    }

    private void callSelectedPersonalTransport() {
        var logistics = savedGame.currentInventory().logistics();
        if (!logistics.personalTransport().ownsAny()) {
            output.println("No hay ningún transporte personal adquirido.");
            return;
        }
        if (logistics.selectedPersonalTransportType().isEmpty()) {
            output.println("No hay un transporte personal seleccionado. Mantenga B para elegirlo.");
            return;
        }
        var result = logistics.callSelectedPersonalTransport(currentPosition, 7.5);
        savedGame.replaceCurrentInventory(savedGame.currentInventory().withLogistics(result.logistics()));
        var call = result.call();
        if (call.status() == domain.inventory.logistics.PersonalTransportCallStatus.UNAVAILABLE) {
            output.println("El transporte seleccionado no puede responder ahora.");
        } else {
            output.println((call.responseSignal()==null||call.responseSignal().isBlank()?"":call.responseSignal()+" · ")
                    + call.type().label() + " responde a la llamada.");
        }
    }

    private void openPersonalTransportWheel() {
        var logistics = savedGame.currentInventory().logistics();
        var owned = logistics.personalTransport().ownedTypes();
        if (owned.isEmpty()) { output.println("No hay transportes personales adquiridos."); return; }
        output.println("RUEDA DE TRANSPORTE PERSONAL");
        output.println("Mantenga B y desplace el ratón alrededor del círculo; en consola seleccione el sector equivalente.");
        for (int i=0;i<owned.size();i++) {
            var candidate = owned.get(i);
            boolean selected = logistics.selectedPersonalTransportType().filter(t -> t == candidate).isPresent();
            output.printf("%d. %s%s%n",i+1,candidate.label(),selected?" [SELECCIONADO]":"");
        }
        int choice=input.readIntegerBetween("Sector: ",1,owned.size());
        var next=logistics.selectPersonalTransport(owned.get(choice-1));
        savedGame.replaceCurrentInventory(savedGame.currentInventory().withLogistics(next));
        output.println("Transporte seleccionado: "+owned.get(choice-1).label()+".");
    }

    private void loadLatestSavePoint() {
        var latest = gameSaveService.latestForPersona(persona.id());
        if (latest.isEmpty()) {
            output.println("No existe todavía ningún punto de guardado para esta PERSONA.");
            return;
        }
        var loaded = gameSaveService.load(latest.get());
        savedGame.replaceFrom(loaded.game());
        this.inventoryAccess = new InventoryAccessService(savedGame.hostileEncounterState());
        this.worldMemory = new WorldMemoryService(savedGame.hostileEncounterState(), savedGame.worldMemory());
        this.worldMemoryScreen = new WorldMemoryScreen(savedGame.worldMemory(), input, output);
        output.println("Último punto de guardado cargado: "+latest.get().metadata().title()+".");
    }

    private void showHelp() {
        output.println("TECLADO");
        output.println("W/A/S/D mover · SHIFT HOLD correr · C postura · C HOLD caminar");
        output.println("SPACE finta atrás · SPACE+W/A/S/D finta direccional · SPACE SPACE saltar");
        output.println("E interactuar · Q acción contextual/cambiar arma · TAB envainar/desenvainar");
        output.println("Z ejecutar maestría activa · Z HOLD rueda de maestrías activas · X activar/desactivar sostenida · X HOLD rueda de maestrías sostenidas");
        output.println("1-4 accesos rápidos · 5 guardado rápido · I inventario · H Hoja del Personaje · M Memoria del Mundo · B llamar transporte · B HOLD rueda de transporte");
        output.println("P pausa contemplativa/reanudar · TRAJE consultar columna del Ingeniero · F fijar objetivo · ESC menú de pausa");
        output.println("RATÓN");
        output.println("MOVER RATÓN controlar cámara");
        output.println("RIGHT CLICK ataque ligero; en el aire, ataque con salto");
        output.println("ALT IZQ ataque fuerte · ALT IZQ HOLD ataque cargado; el Fusil de Repetición contextualiza HOLD como carga con bayoneta");
        output.println("LEFT CLICK parry o inicio de bloqueo · LEFT CLICK HOLD bloquear");
        output.println("MOUSE WHEEL CLICK cambiar configuración · MOUSE WHEEL FORWARD maestría siguiente · MOUSE WHEEL BACKWARD maestría anterior");
        output.println("MANDO PS4 (seleccionable en CONFIGURAR)");
        output.println("STICK IZQUIERDO mover · STICK DERECHO cámara · L3 postura · L3 HOLD correr · R3 fijar");
        output.println("R1 ligero/salto · R2 ataque fuerte · R2 HOLD ataque cargado; Fusil de Repetición = carga con bayoneta · L1 bloquear · L2 parry o inicio de bloqueo");
        output.println("CÍRCULO finta · X salto · TRIÁNGULO interactuar · CUADRADO envainar/desenvainar");
        output.println("D-PAD DERECHA cambiar arma/dual wielding · D-PAD IZQUIERDA cambiar agarre");
        output.println("D-PAD ARRIBA ejecutar activa · mantener ARRIBA rueda de activas · D-PAD ABAJO activar/desactivar sostenida · mantener ABAJO rueda de sostenidas · deslizar TOUCHPAD para accesos rápidos");
        output.println("TOUCHPAD inventario · TOUCHPAD HOLD hoja · SHARE memoria · SHARE HOLD pausa contemplativa · OPTIONS menú de pausa");
        output.println("SALIR: desencarnar y volver al menú");
    }
    public boolean hasPendingDisplayChanges() {
        return settings.hasPendingDisplayChanges();
    }

    public void applyPendingDisplayChangesAtMainMenu() {
        settings.applyPendingDisplayChanges();
        output.println("Los cambios pendientes de pantalla se han aplicado al regresar al menú principal.");
    }

}
