package presentation.menu;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.communication.*;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.misc.MucusCrystalItem;
import domain.inventory.item.misc.MucusTearItem;
import domain.runic.RunicMarkItem;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.LogisticsState;
import domain.save.GameSessionState;
import presentation.console.ConsoleInput;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class InventoryScreen {
    private static final String TITLE = "INVENTARIO";
    private static final String DIVIDER = "============================================================";

    private final GameSessionState savedGame;
    private final ConsoleInput input;
    private final PrintStream output;
    private final CommunicationPairingService communicationPairingService = new CommunicationPairingService();

    public InventoryScreen(
            GameSessionState savedGame,
            ConsoleInput input,
            PrintStream output
    ) {
        this.savedGame = Objects.requireNonNull(savedGame, "La partida guardada no puede ser nula.");
        this.input = Objects.requireNonNull(input, "La entrada no puede ser nula.");
        this.output = Objects.requireNonNull(output, "La salida no puede ser nula.");
    }

    /** Controlador único de la pantalla; evita que gameplay y menú principal diverjan. */
    public InventoryMenuOption open() {
        boolean open = true;
        InventoryMenuOption exit = InventoryMenuOption.RETURN;
        while (open) {
            display();
            InventoryMenuOption option = InventoryMenuOption.fromCode(input.readIntegerBetween("Seleccione una opción: ", 1, 3));
            switch (option) {
                case ACCEPT -> { exit = option; open = false; }
                case INSPECT_ITEMS -> inspectItems();
                case RETURN -> { exit = option; open = false; }
            }
        }
        return exit;
    }

    public void display() {
        InventoryState inventory = savedGame.currentInventory();

        output.println();
        output.println(DIVIDER);
        output.println(center(TITLE, DIVIDER.length()));
        output.println(DIVIDER);
        output.println();

        displayEquipment(inventory);
        displayQuickAccess(inventory);
        displayLogistics(inventory.logistics());
        displayCompartments(inventory.logistics());

        output.printf("%d. Aceptar%n", InventoryMenuOption.ACCEPT.code());
        output.printf("%d. Inspeccionar objetos%n", InventoryMenuOption.INSPECT_ITEMS.code());
        output.printf("%d. Volver%n", InventoryMenuOption.RETURN.code());
        output.println();
    }

    public void inspectItems() {
        List<InspectableInventoryElement> elements = inspectableElements();
        int selected = 0;
        boolean inspecting = true;
        while (inspecting) {
            InspectableInventoryElement element = elements.get(selected);
            refreshCommunicationLinks();
            renderInspection(elements, selected, element);
            CommunicationDeviceType communicationDevice = CommunicationDevicePolicy.deviceTypeOf(element.item());
            boolean sheathing = element.item() instanceof WeaponItem weapon && weapon.supportsSheathing();
            int maximum = (communicationDevice != null || sheathing) ? 3 : 2;
            int option = input.readIntegerBetween("Seleccione una opción: ", 0, maximum);
            switch (option) {
                case 0 -> inspecting = false;
                case 1 -> selected = (selected - 1 + elements.size()) % elements.size();
                case 2 -> selected = (selected + 1) % elements.size();
                case 3 -> {
                    if (communicationDevice != null) {
                        pairCommunicationUser(element.item(), communicationDevice);
                    } else {
                        WeaponItem weapon = (WeaponItem) element.item();
                        if (!weapon.toggleSheathing(savedGame.characterSheet())) {
                            output.println("La propiedad de envainado todavía no está activa.");
                            input.waitForEnter("Pulse Intro para continuar...");
                        }
                    }
                }
                default -> throw new IllegalStateException("Opción de inspección no contemplada.");
            }
        }
    }

    private void renderInspection(List<InspectableInventoryElement> elements, int selected,
                                  InspectableInventoryElement element) {
        output.println();
        output.println(DIVIDER);
        output.println("INSPECCIÓN DEL INVENTARIO");
        output.println(DIVIDER);
        for (int index = 0; index < elements.size(); index++) {
            output.printf("%s %s%n", index == selected ? "->" : "  ", elements.get(index).label());
        }
        output.println();
        output.println(element.section());
        output.println(element.description());
        output.println();

        InventoryEntry item = element.item();
        if (item != null) {
            renderItemDetails(item);
            renderObjectActions(item);
        }

        output.println("1. Elemento anterior");
        output.println("2. Elemento siguiente");
        CommunicationDeviceType communicationDevice = CommunicationDevicePolicy.deviceTypeOf(item);
        if (communicationDevice != null) {
            output.println("3. Enlazar usuario");
        } else if (item instanceof WeaponItem weapon && weapon.supportsSheathing()) {
            output.printf("3. %s%n", weapon.isSheathed() ? "Desenvainar" : "Envainar");
        }
        output.println("0. Dejar de inspeccionar");
        output.println();
    }

    private void renderObjectActions(InventoryEntry item) {
        var a = domain.inventory.InventoryObjectActionPolicy.evaluate(item, savedGame.currentInventory());
        output.println("ACCIONES DE INVENTARIO");
        output.printf("Tirar objeto | %s%n", a.allows(domain.inventory.InventoryObjectAction.DROP) ? "Disponible" : "No disponible");
        output.printf("Equipar en equipamiento activo | %s", a.allows(domain.inventory.InventoryObjectAction.EQUIP_ACTIVE) ? "Disponible" : "No disponible");
        if (!a.eligibleArmorDestinations().isEmpty()) output.printf(" · %s", a.eligibleArmorDestinations().stream().map(domain.inventory.equipment.ArmorEquipDestination::label).toList());
        else if (!a.eligibleEquipmentSlots().isEmpty()) output.printf(" · %s", a.eligibleEquipmentSlots().stream().map(domain.inventory.equipment.EquipmentSlot::label).toList());
        output.println();
        output.printf("Equipar en acceso rápido | %s", a.allows(domain.inventory.InventoryObjectAction.EQUIP_QUICK_ACCESS) ? "Disponible" : "No disponible");
        if (!a.eligibleQuickSlots().isEmpty()) output.printf(" · %s", a.eligibleQuickSlots());
        output.println();
        output.printf("Desequipar objeto | %s%n", a.allows(domain.inventory.InventoryObjectAction.UNEQUIP) ? "Disponible" : "No disponible");
        output.printf("Usar objeto | %s%n", a.allows(domain.inventory.InventoryObjectAction.USE) ? "Disponible" : "No disponible");
        output.printf("Inspeccionar objeto | %s%n", a.allows(domain.inventory.InventoryObjectAction.INSPECT) ? "Disponible" : "No disponible");
        output.printf("Girar 90° | %s%n", a.allows(domain.inventory.InventoryObjectAction.ROTATE_90) ? "Disponible" : "No disponible");
        output.println();
    }

    private void renderItemDetails(InventoryEntry item) {
        CharacterSheet sheet = savedGame.characterSheet();
        if (!isVisibleTo(item, sheet)) {
            output.println("Este objeto posee una propiedad oculta aún inactiva.");
            output.println();
            return;
        }
        output.println("OBJETO EQUIPADO O ALMACENADO");
        output.println(item.name());
        output.println();
        output.println("DESCRIPCIÓN");
        output.println(item.narrativeDescription());
        output.println();

        List<ItemProperty> visibleProperties = item.properties().stream()
                .filter(property -> property.isVisibleTo(sheet))
                .toList();
        if (!visibleProperties.isEmpty()) {
            output.println("PROPIEDADES");
            for (ItemProperty property : visibleProperties) {
                String requirement = property.activationRequirementHidden() ? "" : property.activationAttributeOptional()
                        .map(attribute -> " (" + attribute.label().toUpperCase() + " " + property.activationMinimum() + ")")
                        .orElse("");
                output.printf("%s%s%n", property.name().toUpperCase(), requirement);
                if (!property.narrativeDescription().isBlank()) output.println(property.narrativeDescription());
                if (!property.isActiveFor(sheet)) output.println("[Propiedad conocida, pero todavía inactiva]");
                output.println();
            }
        }
        output.println("ESTADÍSTICAS DEL OBJETO");
        item.statistics().forEach(output::println);
        for (ItemProperty property : visibleProperties) {
            if (property.isActiveFor(sheet)) output.println(property.effectiveStatistic());
        }
        CommunicationDeviceType communicationDevice = CommunicationDevicePolicy.deviceTypeOf(item);
        if (communicationDevice != null) {
            double range = CommunicationRangePolicy.rangeMeters(communicationDevice, savedGame.environmentalCycle().weatherProfile());
            var memory = savedGame.communicationPairingState().memory(communicationDevice);
            String linked = memory.currentUserId() == null ? "Sin enlace" : displayNameFor(memory.currentUserId());
            output.printf("ALCANCE ACTUAL SEGÚN CLIMA | %s m%n", formatNumber(range));
            output.printf("ENLACE | %s%n", linked);
            if (memory.lastUserId() != null) output.printf("MEMORIA DE REENLACE | %s%n", displayNameFor(memory.lastUserId()));
        }
        if (item instanceof WeaponItem weapon && weapon.supportsSheathing()) {
            output.printf("ESTADO | %s%n", weapon.isSheathed() ? "Envainada" : "Desenvainada");
            output.printf("PESO COMPUTABLE ACTUAL (kg) | %s%n", formatNumber(weapon.effectiveWeightKg()));
            output.printf("CONFIGURACIÓN DE ARMA | %s%n", weapon.currentConfiguration().label());
        }
        output.println();
    }

    private List<InspectableInventoryElement> inspectableElements() {
        InventoryState inventory = savedGame.currentInventory();
        List<InspectableInventoryElement> result = new ArrayList<>();

        // Armadura/ropa: el layout estratificado es la autoridad visual.
        for (var layer : inventory.armorLayout().layers()) {
            String sub = layer.piece().innerChestLayer().map(v -> " / " + v.name())
                    .orElse(layer.piece().innerLeggingsLayer().map(v -> " / " + v.name())
                    .orElse(layer.piece().feetLayer().map(v -> " / " + v.name())
                    .orElse(layer.piece().headLayer().map(v -> " / " + v.name()).orElse(""))));
            result.add(new InspectableInventoryElement(
                    "Capa — " + layer.slot().label() + " / " + layer.position().name() + sub,
                    "RANURA ESTRATIFICADA DE ARMADURA",
                    "Pieza vestida en su posición anatómica canónica.",
                    layer.piece()));
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.BRACERS
                    || slot == EquipmentSlot.LEGGINGS || slot == EquipmentSlot.FEET) continue;
            InventoryEntry equipped = inventory.equipment().itemAt(slot).orElse(null);
            String state = equipped == null ? "Vacía." : "Contiene: " + visibleName(equipped) + ".";
            result.add(new InspectableInventoryElement(
                    "Ranura — " + slot.label(), "RANURA DE EQUIPAMIENTO",
                    equipmentSlotPolicy(slot) + " " + state, equipped));
        }

        for (int index = 0; index < inventory.quickAccessBar().slots().size(); index++) {
            int number = index + 1;
            boolean available = domain.inventory.QuickAccessPolicy.isSlotAvailable(number, inventory.equipment(), inventory.logistics());
            InventoryEntry assigned = inventory.quickAccessBar().slots().get(index).orElse(null);
            String source = switch (number) {
                case 1 -> "inventario LEGGINGS habilitado por la prenda de piernas";
                case 2 -> "inventario CHEST habilitado por la prenda de torso";
                case 3 -> "pernera";
                case 4 -> "bandolera";
                default -> throw new IllegalStateException();
            };
            result.add(new InspectableInventoryElement(
                    "Acceso rápido F" + number,
                    "RANURA DE ACCESO RÁPIDO",
                    "Solo admite objetos procedentes de " + source + ". Estado: " +
                            (available ? (assigned == null ? "vacía." : "asignado " + visibleName(assigned) + ".") : "no disponible."),
                    assigned
            ));
        }

        for (InventoryCompartmentType type : InventoryCompartmentType.values()) {
            InventoryCompartment compartment = inventory.logistics().compartment(type);
            result.add(new InspectableInventoryElement(
                    "Compartimento — " + type.label(),
                    "EXPANSIÓN IMPERMEABLE",
                    type.narrativeDescription() + " Capacidad: " + compartment.grid().verticalSlots() + " x " +
                            compartment.grid().horizontalSlots() + " = " + compartment.grid().capacity() + " slots. Estado: " +
                            (compartment.available() ? "disponible." : "no disponible."),
                    null
            ));
            if (compartment.available()) {
                for (InventoryEntry entry : compartment.entries()) {
                    result.add(new InspectableInventoryElement(
                            type.label() + " — " + visibleName(entry),
                            "OBJETO ALMACENADO",
                            "Objeto contenido exclusivamente en " + type.label() + ".",
                            entry
                    ));
                }
            }
        }
        return List.copyOf(result);
    }

    private void refreshCommunicationLinks() {
        var profile = savedGame.environmentalCycle().weatherProfile();
        var candidates = savedGame.nearbyCommunicationRegistry().snapshot();
        InventoryEntry head = savedGame.currentInventory().equipment().itemAt(EquipmentSlot.HEAD).orElse(null);
        CommunicationDeviceType equipped = CommunicationDevicePolicy.deviceTypeOf(head);
        for (CommunicationDeviceType device : CommunicationDeviceType.values()) {
            communicationPairingService.refresh(savedGame.communicationPairingState(), device, profile, candidates, device == equipped);
        }
    }

    private void pairCommunicationUser(InventoryEntry item, CommunicationDeviceType device) {
        InventoryEntry equippedHead = savedGame.currentInventory().equipment().itemAt(EquipmentSlot.HEAD).orElse(null);
        if (equippedHead != item) {
            output.println("Enlazar usuario no disponible: el dispositivo debe estar equipado en HEAD.");
            input.waitForEnter("Pulse Intro para continuar...");
            return;
        }
        var profile = savedGame.environmentalCycle().weatherProfile();
        var available = communicationPairingService.available(
                device, profile, savedGame.nearbyCommunicationRegistry().snapshot());
        if (available.isEmpty()) {
            output.println("No hay usuarios enlazables dentro del alcance actual.");
            input.waitForEnter("Pulse Intro para continuar...");
            return;
        }
        output.println("USUARIOS DISPONIBLES");
        for (int i=0;i<available.size();i++) {
            PairingCandidate c=available.get(i);
            output.printf("%d. %s · %s m · relación %s%n",
                    i+1,c.displayName(),formatNumber(c.distanceMeters()),c.relationship().label());
        }
        int option=input.readIntegerBetween("Seleccione usuario (0 cancelar): ",0,available.size());
        if(option==0) return;
        PairingCandidate selected=available.get(option-1);
        communicationPairingService.pair(savedGame.communicationPairingState(),device,profile,selected);
        output.println("Usuario enlazado: "+selected.displayName()+".");
        input.waitForEnter("Pulse Intro para continuar...");
    }

    private String displayNameFor(String userId) {
        return savedGame.nearbyCommunicationRegistry().snapshot().stream()
                .filter(c->c.userId().equals(userId))
                .map(PairingCandidate::displayName)
                .findFirst().orElse(userId);
    }

    private String visibleName(InventoryEntry item) {
        return isVisibleTo(item, savedGame.characterSheet()) ? item.name() : "[Objeto velado]";
    }

    private static boolean isVisibleTo(InventoryEntry item, CharacterSheet sheet) {
        if (item instanceof RunicMarkItem mark) return mark.isAwakenedFor(sheet);
        if (item instanceof MucusTearItem || item instanceof MucusCrystalItem) {
            return domain.knowledge.PropertyKnowledgePolicy.requirementMet(sheet, domain.character.sheet.Attribute.CLARIVIDENCIA, domain.inventory.item.misc.MucusCrystalItem.TRANSPOSITION_CLARIVOYANCE_THRESHOLD);
        }
        return true;
    }

    private String equipmentSlotPolicy(EquipmentSlot slot) {
        return switch (slot) {
            case RIGHT_HAND -> "Admite un arma compatible en la mano derecha.";
            case LEFT_HAND -> "Admite un arma compatible con los límites canónicos de peso, longitud y empuñadura de la mano izquierda.";
            case BACK_HAND -> "Ranura dorsal exclusiva del Espadón de Rotor retraído. Hereda RIGHT_HAND como mano dominante efectiva y sólo existe con el Sistema de Transporte Dorsal del Rotor V881.";
            case HEAD -> "Admite exclusivamente armadura de cabeza.";
            case CHEST -> "Admite exclusivamente armadura de torso.";
            case BRACERS -> "Admite exclusivamente brazales.";
            case LEGGINGS -> "Admite prendas de piernas compatibles con el layout corporal.";
            case FEET -> "Admite una pieza de calzado independiente; no participa en ergonomía de PA.";
            case ACCESSORY -> "Admite exclusivamente un abalorio.";
            case RUNIC_MARK -> "Admite exclusivamente una Marca Rúnica. La ranura y la runa equipada son inspeccionables.";
        };
    }

    private record InspectableInventoryElement(
            String label,
            String section,
            String description,
            InventoryEntry item
    ) { }

    private void displayEquipment(InventoryState inventory) {
        output.println("EQUIPAMIENTO ACTIVO");
        output.println("ARMADURA Y ROPA POR CAPAS");
        output.println("HEAD     | TACTICAL / LOWER_ACCESSORY / UPPER_ACCESSORY");
        output.println("CHEST    | INNER(BASE / STRUCTURAL / COVER) / MIDDLE / OUTER");
        output.println("BRACERS  | ranura independiente");
        output.println("LEGGINGS | INNER(BASE / COVER) / MIDDLE / OUTER");
        output.println("FEET     | INNER / OUTER");
        if (inventory.armorLayout().layers().isEmpty()) output.println("[Todas las ranuras de ropa/armadura están vacías]");
        for (var layer : inventory.armorLayout().layers()) {
            String sub = layer.piece().innerChestLayer().map(v -> " / " + v.name())
                    .orElse(layer.piece().innerLeggingsLayer().map(v -> " / " + v.name())
                    .orElse(layer.piece().feetLayer().map(v -> " / " + v.name())
                    .orElse(layer.piece().headLayer().map(v -> " / " + v.name()).orElse(""))));
            output.printf("%-20s %-12s %-14s %s%n", layer.slot().label()+":", layer.position().name(),
                    sub.isBlank()?"":sub.substring(3), visibleName(layer.piece()));
        }
        output.println("OTRO EQUIPAMIENTO");
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.BRACERS
                    || slot == EquipmentSlot.LEGGINGS || slot == EquipmentSlot.FEET) continue;
            String itemName = inventory.equipment().itemAt(slot).map(this::visibleName).orElse("[Vacío]");
            output.printf("%-20s %s%n", slot.label() + ":", itemName);
        }
        output.println();
    }

    private void displayQuickAccess(InventoryState inventory) {
        output.println("ACCESOS RÁPIDOS");
        for (int index = 0; index < inventory.quickAccessBar().slots().size(); index++) {
            int slotNumber = index + 1;
            boolean available = domain.inventory.QuickAccessPolicy.isSlotAvailable(
                    slotNumber, inventory.equipment(), inventory.logistics());
            String itemName = available
                    ? inventory.quickAccessBar().slots().get(index).map(this::visibleName).orElse("[Vacío]")
                    : "[No disponible]";
            output.printf("%d: %-24s", slotNumber, itemName);
            if (slotNumber % 2 == 0) output.println();
        }
        output.println();
    }

    private void displayLogistics(LogisticsState logistics) {
        output.println("LOGÍSTICA ACTIVA");
        displayCompartmentSlot(logistics.compartment(InventoryCompartmentType.LEG_POUCH));
        displayCompartmentSlot(logistics.compartment(InventoryCompartmentType.BANDOLIER));
        displayCompartmentSlot(logistics.compartment(InventoryCompartmentType.BACKPACK));
        displayCompartmentSlot(logistics.compartment(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM));
        displayTransportAndSaddlebags(logistics);
        displayCompartmentSlot(logistics.compartment(InventoryCompartmentType.ARROW_QUIVER));
        output.println();
    }


    private void displayTransportAndSaddlebags(LogisticsState logistics) {
        for (domain.inventory.logistics.PersonalTransportType type : domain.inventory.logistics.PersonalTransportType.values()) {
            var unit = logistics.personalTransport().unit(type);
            String transportState = unit.physicallyPresent() ? "presente" : unit.owned() ? "adquirido" : "no disponible";
            var saddleType = domain.inventory.logistics.PersonalTransportSaddlebagPolicy.compartmentType(type);
            if (saddleType.isPresent()) {
                InventoryCompartment saddle = logistics.compartment(saddleType.get());
                String saddleState = saddle.available()
                        ? saddle.type().label() + " equipadas · " + saddle.capacitySlots() + " slots · máx. " + formatNumber(saddle.type().maximumWeightKg().orElse(0)) + " kg"
                        : saddle.type().label() + " no equipadas";
                output.printf("%-20s [%s · %s]%n", type.label()+":", transportState, saddleState);
            } else {
                output.printf("%-20s [%s · sin sistema lateral de carga]%n", type.label()+":", transportState);
            }
            output.printf("  %s%n", type.technicalDescription());
        }
    }

    private void displayCompartmentSlot(InventoryCompartment compartment) {
        String state = compartment.available()
                ? "[Disponible · peso estructural " + formatNumber(compartment.type().structuralWeightKg()) + " kg]"
                : "[No disponible]";
        output.printf("%-20s %s%n", compartment.type().label() + ":", state);
    }

    private String transportStatus(LogisticsState logistics) {
        return logistics.selectedPersonalTransportType()
                .map(type -> {
                    var state = logistics.personalTransport().unit(type);
                    if (state.physicallyPresent()) return "[" + type.label() + " · presente]";
                    if (state.owned()) return "[" + type.label() + " · referencia en Memoria del Mundo]";
                    return "[" + type.label() + " · no disponible en la demo]";
                })
                .orElse("[No disponible]");
    }

    private void displayCompartments(LogisticsState logistics) {
        for (InventoryCompartmentType type : List.of(
                InventoryCompartmentType.LEGGINGS_STORAGE,
                InventoryCompartmentType.CHEST_STORAGE,
                InventoryCompartmentType.LEG_POUCH,
                InventoryCompartmentType.BANDOLIER,
                InventoryCompartmentType.BACKPACK,
                InventoryCompartmentType.DORSAL_ROTOR_SYSTEM,
                InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE,
                InventoryCompartmentType.SADDLEBAGS_HORSE_RACING,
                InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT,
                InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY,
                InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN,
                InventoryCompartmentType.ARROW_QUIVER)) {
            InventoryCompartment compartment = logistics.compartment(type);
            if (compartment.available()) displayCompartmentContents(compartment);
        }
    }

    private void displayCompartmentContents(InventoryCompartment compartment) {
        InventoryCompartmentType type = compartment.type();
        output.printf("%s (%d verticales x %d horizontales = %d slots)%n",
                type.label().toUpperCase(), compartment.grid().verticalSlots(), compartment.grid().horizontalSlots(), compartment.grid().capacity());
        output.printf("«%s»%n", type.narrativeDescription());
        if (type.structuralWeightKg() > 0) {
            output.printf("PESO ESTRUCTURAL | %s kg%n", formatNumber(type.structuralWeightKg()));
        }
        compartment.type().maximumWeightKg().ifPresent(maximum ->
                output.printf("PESO DEL CONTENIDO | %s / %s kg%n", formatNumber(compartment.contentsWeightKg()), formatNumber(maximum)));
        if (compartment.entries().isEmpty()) {
            output.printf("[Vacío: %d slots disponibles]%n", compartment.freeSlots());
        } else if (type == InventoryCompartmentType.ARROW_QUIVER) {
            var quiver=compartment.arrowQuiverContents().orElseThrow();
            output.printf("CAPACIDAD | %d / %d flechas%n",quiver.totalArrows(),domain.inventory.item.ammunition.ArrowQuiverContents.MAX_ARROWS);
            for(String line:quiver.displayLines()) output.printf("- %s%n",line);
        } else {
            for (InventoryEntry entry : compartment.entries()) {
                String size = entry.footprint().hasGridDimensions()
                        ? entry.footprint().verticalSlots() + " x " + entry.footprint().horizontalSlots()
                        : "tamaño de inventario pendiente";
                output.printf("- %s [%s]%n", visibleName(entry), size);
            }
        }
        output.println();
    }

    private String formatNumber(double value) {
        if (value == Math.rint(value)) return Long.toString(Math.round(value));
        return String.format(java.util.Locale.ROOT, "%.2f", value).replace('.', ',');
    }

    private String center(String text, int width) {
        if (text.length() >= width) return text;
        return " ".repeat((width - text.length()) / 2) + text;
    }
}
