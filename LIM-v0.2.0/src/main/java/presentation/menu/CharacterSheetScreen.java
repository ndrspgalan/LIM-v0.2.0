package presentation.menu;

import domain.character.CharacterIdentity;
import domain.character.progression.AttributeCapPolicy;
import domain.character.progression.CharacterProgressionState;
import domain.character.progression.LevelUpResult;
import domain.character.progression.LevelUpSession;
import domain.character.progression.MucusRequirementPolicy;
import domain.character.progression.MucusType;
import domain.character.progression.MucusDoctrine;
import domain.runic.RunicMarkId;
import domain.runic.transposition.InventoryTranspositionResult;
import domain.runic.transposition.TranspositionInventoryService;
import domain.character.sheet.Attribute;
import domain.character.sheet.CurrentCharacterStats;
import domain.character.sheet.DamageResistanceProfile;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.save.GameSessionState;
import domain.persona.PersonaProfile;
import presentation.console.ConsoleInput;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

public final class CharacterSheetScreen {
    private static final String DIVIDER = "================================================================";

    private final GameSessionState savedGame;
    private final AttributeCapPolicy capPolicy;
    private final MucusRequirementPolicy mucusPolicy;
    private final DerivedStatisticsCalculator statisticsCalculator;
    private final ConsoleInput input;
    private final PrintStream output;
    private final List<CharacterSheetInspectionEntry> inspectionEntries;
    private final PersonaProfile personaProfile;
    private final TranspositionInventoryService transpositionService = new TranspositionInventoryService();

    public CharacterSheetScreen(
            GameSessionState savedGame,
            AttributeCapPolicy capPolicy,
            MucusRequirementPolicy mucusPolicy,
            DerivedStatisticsCalculator statisticsCalculator,
            PersonaProfile personaProfile,
            ConsoleInput input,
            PrintStream output
    ) {
        this.savedGame = Objects.requireNonNull(savedGame, "La partida no puede ser nula.");
        this.capPolicy = Objects.requireNonNull(capPolicy, "La política de límites no puede ser nula.");
        this.mucusPolicy = Objects.requireNonNull(mucusPolicy, "La política de mucus no puede ser nula.");
        this.statisticsCalculator = Objects.requireNonNull(statisticsCalculator, "La calculadora no puede ser nula.");
        this.personaProfile = Objects.requireNonNull(personaProfile, "La PERSONA no puede ser nula.");
        this.input = Objects.requireNonNull(input, "La entrada no puede ser nula.");
        this.output = Objects.requireNonNull(output, "La salida no puede ser nula.");
        this.inspectionEntries = CharacterSheetInspectionEntry.canonicalEntries();
    }

    /** Único flujo canónico de la Hoja del Personaje. */
    public void open() {
        output.println();
        output.println("HOJA DEL PERSONAJE");

        boolean open = true;
        while (open) {
            displayCurrentSheet();
            savedGame.currentInventory().equipment().synchronizeRunicProgress(savedGame.character().identity().characterClass(), savedGame.masteries(), savedGame.characterSheet());
            RunicMarkCollectionScreen runicMarkCollectionScreen = new RunicMarkCollectionScreen(personaProfile, savedGame.currentInventory().equipment(), input, output);
            boolean canExploreRunicMarks = runicMarkCollectionScreen.available();
            output.println();
            int option = 1;
            int inspectOption = option++;
            int masteriesOption = option++;
            int runicMarksOption = canExploreRunicMarks ? option++ : -1;
            output.println(inspectOption + ". Explorar hoja");
            output.println(masteriesOption + ". Explorar colección de maestrías");
            if (canExploreRunicMarks) output.println(runicMarksOption + ". Explorar Marcas Rúnicas");
            output.println("0. Cerrar Hoja del Personaje");
            output.println();
            int selected = input.readIntegerBetween("Seleccione una opción: ", 0, option - 1);
            if (selected == 0) open = false;
            else if (selected == inspectOption) inspectSheet();
            else if (selected == masteriesOption) new MasteryCollectionScreen(savedGame.characterSheet(), savedGame.masteries(), savedGame.character().identity().gender(), input, output).open();
            else if (selected == runicMarksOption) runicMarkCollectionScreen.open();
            else throw new IllegalStateException("Opción no contemplada: " + selected);
        }

        output.println("Hoja del Personaje cerrada.");
    }

    public void openSleepProgression() {
        if (!savedGame.sleepProgressionActive()) throw new IllegalStateException("Refinar mucus sólo está disponible mientras Kenan duerme.");
        openLevelUpDraft();
        if (savedGame.currentInventory().equipment().hasActiveRunicMark(RunicMarkId.TRANSPOSICION, savedGame.characterSheet(), personaProfile)) openTransposition();
    }

    private void openLevelUpDraft() {
        CharacterIdentity identity = savedGame.character().identity();
        LevelUpSession session = new LevelUpSession(
                identity.gender(),
                identity.characterClass(),
                savedGame.progression(),
                capPolicy,
                mucusPolicy
        );

        boolean drafting = true;
        while (drafting) {
            displayLevelUpDraft(session);
            int option = input.readIntegerBetween("Seleccione un atributo o una acción: ", 1, 11);

            if (option <= Attribute.values().length) {
                Attribute attribute = Attribute.values()[option - 1];
                displaySelectedAttributeGuidance(attribute, session);
                LevelUpResult result = session.increase(attribute);
                output.println(result.message());
                continue;
            }

            switch (option) {
                case 10 -> {
                    if (!session.hasChanges()) {
                        output.println("No hay cambios provisionales que confirmar.");
                    } else {
                        CharacterProgressionState committed = session.preview();
                        CurrentCharacterStats stats = calculateStats(committed);
                        savedGame.replaceProgression(committed, stats);
                        output.println("El refinado de mucus se confirma. Todos los cambios del borrador quedan aplicados.");
                        drafting = false;
                    }
                }
                case 11 -> {
                    session.discard();
                    output.println("El borrador se descarta. Todo el mucus provisional vuelve a su reserva.");
                    drafting = false;
                }
                default -> throw new IllegalStateException("Opción no contemplada: " + option);
            }
        }
    }

    private void inspectSheet() {
        int selectedIndex = 0;
        boolean inspecting = true;

        while (inspecting) {
            displayInspection(selectedIndex);
            int option = input.readIntegerBetween("Seleccione una opción: ", 0, 2);
            switch (option) {
                case 0 -> inspecting = false;
                case 1 -> selectedIndex = (selectedIndex + 1) % inspectionEntries.size();
                case 2 -> selectedIndex = (selectedIndex - 1 + inspectionEntries.size()) % inspectionEntries.size();
                default -> throw new IllegalStateException("Opción no contemplada: " + option);
            }
        }
    }

    private void displayInspection(int selectedIndex) {
        output.println();
        output.println(DIVIDER);
        output.println("INSPECCIONAR HOJA");
        output.println(DIVIDER);

        for (int index = 0; index < inspectionEntries.size(); index++) {
            CharacterSheetInspectionEntry entry = inspectionEntries.get(index);
            String cursor = index == selectedIndex ? "->" : "  ";
            output.printf("%s %s%n", cursor, entry.label());
        }

        CharacterSheetInspectionEntry selected = inspectionEntries.get(selectedIndex);
        output.println();
        if (selected.label().equals("Vitalidad")) {
            int vitality = savedGame.progression().sheet().valueOf(Attribute.VITALIDAD);
            output.println(VitalityNarrative.descriptionFor(vitality));
        } else if (selected.label().equals("Aguante")) {
            int endurance = savedGame.progression().sheet().valueOf(Attribute.AGUANTE);
            output.println(EnduranceNarrative.descriptionFor(endurance, savedGame.character().identity().gender()));
        } else if (selected.label().equals("Adaptabilidad")) {
            int adaptability = savedGame.progression().sheet().valueOf(Attribute.ADAPTABILIDAD);
            output.println(AdaptabilityNarrative.descriptionFor(adaptability, savedGame.character().identity().gender()));
        } else if (selected.label().equals("Fuerza")) {
            int strength = savedGame.progression().sheet().valueOf(Attribute.FUERZA);
            output.println(StrengthNarrative.descriptionFor(strength, savedGame.character().identity().gender()));
        } else if (selected.label().equals("Destreza")) {
            int dexterity = savedGame.progression().sheet().valueOf(Attribute.DESTREZA);
            output.println(DexterityNarrative.descriptionFor(dexterity, savedGame.character().identity().gender()));
        } else if (selected.label().equals("Inteligencia")) {
            int intelligence = savedGame.progression().sheet().valueOf(Attribute.INTELIGENCIA);
            output.println(IntelligenceNarrative.descriptionFor(intelligence, savedGame.character().identity().gender()));
        } else if (selected.label().equals("Fe")) {
            int faith = savedGame.progression().sheet().valueOf(Attribute.FE);
            output.println(FaithNarrative.descriptionFor(faith, savedGame.character().identity().gender()));
        } else if (selected.label().equals("Carisma")) {
            int charisma = savedGame.progression().sheet().valueOf(Attribute.CARISMA);
            output.println(CharismaNarrative.descriptionFor(charisma, savedGame.character().identity().gender()));
        } else if (selected.label().equals("Clarividencia")) {
            int clairvoyance = savedGame.progression().sheet().valueOf(Attribute.CLARIVIDENCIA);
            output.println(ClairvoyanceNarrative.descriptionFor(clairvoyance));
        } else if (selected.hasStaticDescription()) {
            output.println(selected.description());
        } else {
            throw new IllegalStateException("La entrada no tiene una narrativa resoluble: " + selected.label());
        }
        output.println();
        output.println("1. Siguiente");
        output.println("2. Anterior");
        output.println("0. Volver");
        output.println();
    }

    private void openTransposition() {
        boolean open = true;
        while (open) {
            var wallet = savedGame.progression().mucusWallet();
            output.println();
            output.println(DIVIDER);
            output.println("TRANSPOSICIÓN — GRIMORIO DEL MAESTRO");
            output.println(DIVIDER);
            output.println(MucusDoctrine.CRYSTALLIZATION);
            output.println();
            output.println(domain.runic.transposition.TranspositionYieldPolicy.doctrineSummary());
            output.println();
            MucusType[] types = MucusType.values();
            for (int i = 0; i < types.length; i++) {
                MucusType type = types[i];
                output.printf("%d. %s — %s/%d mL%n", i + 1, type.label(), formatMucusMl(wallet.quantityMlOf(type)), type.maximumReserveMl());
            }
            output.println("0. Volver");
            int selected = input.readIntegerBetween("Seleccione el mucus que desea estudiar/transponer: ", 0, types.length);
            if (selected == 0) { open = false; continue; }
            MucusType type = types[selected - 1];
            output.println();
            output.println(MucusDoctrine.transpositionKnowledge(type));
            output.println();
            if (!wallet.contains(type)) {
                output.println("No dispone de este mucus para materializar la transposición.");
                input.waitForEnter("Pulse Intro para continuar...");
                continue;
            }
            int confirm = input.readIntegerBetween("1. Transponer  0. Cancelar: ", 0, 1);
            if (confirm == 0) continue;
            InventoryTranspositionResult result = transpositionService.transpose(type, wallet, savedGame.currentInventory(),
                    savedGame.characterSheet(), savedGame.currentInventory().equipment());
            output.println(result.message());
            if (result.allowed()) {
                CharacterProgressionState progression = savedGame.progression();
                CharacterProgressionState updated = new CharacterProgressionState(progression.level(), progression.sheet(), result.wallet());
                savedGame.replaceCurrentInventory(result.inventory());
                savedGame.replaceProgression(updated, calculateStats(updated));
            }
            input.waitForEnter("Pulse Intro para continuar...");
        }
    }

    private void displayCurrentSheet() {
        CharacterProgressionState current = savedGame.progression();
        CurrentCharacterStats currentStats = calculateStats(current);
        CharacterIdentity identity = savedGame.character().identity();

        output.println();
        output.println(DIVIDER);
        output.println("HOJA DEL PERSONAJE");
        output.println(DIVIDER);
        output.printf("%s · %s · %.2f m · %.1f kg%n", identity.name(), identity.gender().label(), identity.heightMeters(), identity.weightKilograms());
        output.printf("OFICIO | %s%n", identity.profession().label());
        output.printf("ALTURA | %.2f m%n", identity.heightMeters());
        output.printf("NIVEL | %d%n%n", current.level());

        output.println("MUCUS DISPONIBLE");
        for (MucusType type : MucusType.values()) {
            output.printf("%-22s %s / %d mL%n", type.label() + ":", formatMucusMl(current.mucusWallet().quantityMlOf(type)), type.maximumReserveMl());
        }
        output.println();

        output.println("ATRIBUTOS");
        for (Attribute attribute : Attribute.values()) {
            output.printf("%-22s %d%n", attribute.label() + ":", current.sheet().valueOf(attribute));
        }
        output.println();

        displayStatistics(currentStats);
    }

    private void displayLevelUpDraft(LevelUpSession session) {
        CharacterProgressionState current = session.original();
        CharacterProgressionState preview = session.preview();
        CurrentCharacterStats currentStats = calculateStats(current);
        CurrentCharacterStats previewStats = calculateStats(preview);
        CharacterIdentity identity = savedGame.character().identity();

        output.println();
        output.println(DIVIDER);
        output.println("BORRADOR DE SUBIDA DE NIVEL");
        output.println(DIVIDER);
        output.printf("%s · %s · Nivel %d%n", identity.name(), identity.profession().label(), current.level());
        output.printf("Nivel: %d -> %d%n%n", current.level(), preview.level());

        output.println("MUCUS DISPONIBLE");
        for (MucusType type : MucusType.values()) {
            output.printf("%-22s %s -> %s mL%n", type.label() + ":",
                    formatMucusMl(current.mucusWallet().quantityMlOf(type)),
                    formatMucusMl(preview.mucusWallet().quantityMlOf(type)));
        }
        output.println();

        output.println("ATRIBUTOS");
        Attribute[] attributes = Attribute.values();
        for (int index = 0; index < attributes.length; index++) {
            Attribute attribute = attributes[index];
            int actual = current.sheet().valueOf(attribute);
            int expected = preview.sheet().valueOf(attribute);
            int maximum = session.maximumFor(attribute);
            String requirement = expected < maximum
                    ? session.requirementForNext(attribute).label()
                    : "límite alcanzado";
            output.printf("%d. %-16s %3d -> %-3d  [máx. %3d; siguiente: %s]%n",
                    index + 1, attribute.label(), actual, expected, maximum, requirement);
        }
        output.println();

        displayStatistics(currentStats, previewStats);
        displayInventoryPreview(current, preview);

        output.println("10. Confirmar todos los cambios");
        output.println("11. Descartar borrador");
        output.println();
    }

    private String healthRegenerationLabel() {
        return savedGame.character().identity().gender() == domain.character.Gender.MUJER
                ? "PV REGEN / 5 s" : "PV REGEN / 6 s";
    }

    private void displayStatistics(CurrentCharacterStats current) {
        output.println("ESTADÍSTICAS DERIVADAS");
        displaySingleStat("PV TOTAL", current.totalHealth(), "");
        displaySingleStat(healthRegenerationLabel(), current.healthRegeneration(), "");
        displaySingleStat("ESTABILIDAD FÍSICA", current.physicalStability(), "");
        displaySingleStat("CORDURA", current.sanity(), "");
        output.println();
        displaySingleStat("PA TOTAL", current.totalStamina(), "");
        displaySingleStat("PA REGEN / s", current.staminaRegeneration(), "");
        output.printf("%-24s %s / %s kg%n", "CARGA:", format(current.currentLoadKg()), format(current.maximumLoadKg()));
        output.println();

        output.println("RESISTENCIAS");
        displaySingleResistances(current.resistances());
        output.println();
        displayEnvironmentalAdversities();
    }

    private void displayStatistics(CurrentCharacterStats current, CurrentCharacterStats preview) {
        output.println("ESTADÍSTICAS DERIVADAS");
        displayStat("PV TOTAL", current.totalHealth(), preview.totalHealth(), "");
        displayStat(healthRegenerationLabel(), current.healthRegeneration(), preview.healthRegeneration(), "");
        displayStat("ESTABILIDAD FÍSICA", current.physicalStability(), preview.physicalStability(), "");
        displayStat("CORDURA", current.sanity(), preview.sanity(), "");
        output.println();
        displayStat("PA TOTAL", current.totalStamina(), preview.totalStamina(), "");
        displayStat("PA REGEN / s", current.staminaRegeneration(), preview.staminaRegeneration(), "");
        displayLoad(current, preview);
        output.println();

        output.println("RESISTENCIAS");
        displayResistances(current.resistances(), preview.resistances());
        output.println();
        displayEnvironmentalAdversities();
    }


    private void displayInventoryPreview(CharacterProgressionState current, CharacterProgressionState preview) {
        output.println("CAMBIOS EN EL INVENTARIO");
        List<String> changes = InventoryLevelUpPreview.changes(
                savedGame.currentInventory(), current.sheet(), preview.sheet());
        if (changes.isEmpty()) {
            output.println("Sin cambios de visibilidad, activación o requisitos de uso.");
        } else {
            changes.forEach(change -> output.println("- " + change));
        }
        output.println();
    }

    private void displayEnvironmentalAdversities() {
        output.println("ADVERSIDADES AMBIENTALES");
        output.println("TOXICIDAD VIRULENTA");
        output.println("QUEMADURA ASFIXIANTE");
        output.println("FRÍO ESCARCHANTE");
        output.println("EMPAPADO");
        output.println();
    }

    private void displaySelectedAttributeGuidance(Attribute attribute, LevelUpSession session) {
        int currentValue = session.preview().sheet().valueOf(attribute);
        int maximum = session.maximumFor(attribute);

        output.println();
        output.println("POLÍTICA DE " + attribute.label().toUpperCase(java.util.Locale.ROOT));
        output.println(AttributeLevelUpGuidance.descriptionOf(attribute, session.preview().sheet()));
        output.printf("Estado provisional: %d / %d.%n", currentValue, maximum);
        if (currentValue < maximum) {
            output.printf("Siguiente nivel: requiere 1 %s.%n", session.requirementForNext(attribute).label());
        } else {
            output.println("Siguiente nivel: límite actual alcanzado.");
        }
        output.println();
    }

    private CurrentCharacterStats calculateStats(CharacterProgressionState state) {
        return statisticsCalculator.calculate(
                state.sheet(),
                savedGame.character().identity().gender(),
                savedGame.currentInventory(),
                savedGame.environmentalCycle().phase()
        );
    }

    private void displayLoad(CurrentCharacterStats current, CurrentCharacterStats preview) {
        output.printf("%-24s %s / %s kg -> %s / %s kg%n", "CARGA:",
                format(current.currentLoadKg()), format(current.maximumLoadKg()),
                format(preview.currentLoadKg()), format(preview.maximumLoadKg()));
    }

    private void displaySingleResistances(DamageResistanceProfile current) {
        output.println("  FÍSICAS");
        displaySingleStat("  Perforante", current.piercing(), "%");
        displaySingleStat("  Cortante", current.slashing(), "%");
        displaySingleStat("  Contundente", current.blunt(), "%");
        output.println("  ELEMENTALES");
        displaySingleStat("  Veneno", current.poison(), "%");
        displaySingleStat("  Quemadura", current.burn(), "%");
        displaySingleStat("  Congelación", current.frost(), "%");
        displaySingleStat("  Electricidad", current.electricity(), "%");
        output.println("  ESPIRITUALES");
        displaySingleStat("  Maldición", current.curse(), "%");
        displaySingleStat("  Frenesí", current.frenzy(), "%");
    }

    private void displayResistances(DamageResistanceProfile current, DamageResistanceProfile preview) {
        output.println("  FÍSICAS");
        displayStat("  Perforante", current.piercing(), preview.piercing(), "%");
        displayStat("  Cortante", current.slashing(), preview.slashing(), "%");
        displayStat("  Contundente", current.blunt(), preview.blunt(), "%");
        output.println("  ELEMENTALES");
        displayStat("  Veneno", current.poison(), preview.poison(), "%");
        displayStat("  Quemadura", current.burn(), preview.burn(), "%");
        displayStat("  Congelación", current.frost(), preview.frost(), "%");
        displayStat("  Electricidad", current.electricity(), preview.electricity(), "%");
        output.println("  ESPIRITUALES");
        displayStat("  Maldición", current.curse(), preview.curse(), "%");
        displayStat("  Frenesí", current.frenzy(), preview.frenzy(), "%");
    }

    private void displaySingleStat(String label, OptionalDouble value, String suffix) {
        output.printf("%-24s %s%s%n", label + ":", format(value), suffix);
    }

    private void displayStat(String label, OptionalDouble actual, OptionalDouble expected, String suffix) {
        output.printf("%-24s %s%s -> %s%s%n", label + ":", format(actual), suffix, format(expected), suffix);
    }

    private String format(OptionalDouble value) {
        return value.isPresent() ? format(value.getAsDouble()) : "pendiente";
    }

    private String format(double value) {
        if (value == Math.rint(value)) {
            return Long.toString(Math.round(value));
        }
        return String.format("%.2f", value);
    }

    private static String formatMucusMl(double ml) {
        return Math.abs(ml-Math.rint(ml))<1e-9 ? Long.toString(Math.round(ml)) : String.format(java.util.Locale.ROOT,"%.1f",ml);
    }
}
