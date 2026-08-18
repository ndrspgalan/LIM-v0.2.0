package presentation.menu;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.KnowledgeStatus;
import domain.worldmemory.filter.SpatialMemoryRequirement;
import domain.worldmemory.history.WorldMemoryEntryRevision;
import domain.worldmemory.filter.WorldMemoryFilter;
import domain.worldmemory.filter.WorldMemoryFilterEngine;
import domain.worldmemory.navigation.WorldMemoryCategoryNavigator;
import domain.worldmemory.navigation.WorldMemoryCategorySummary;
import domain.worldmemory.query.WorldMemoryQuery;
import domain.worldmemory.search.WorldMemorySearch;
import domain.worldmemory.search.WorldMemorySearchResult;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.WorldCoordinate;
import domain.worldmemory.ui.WorldMemoryEntryView;
import domain.worldmemory.ui.WorldMemoryEntryViewAssembler;
import presentation.console.ConsoleInput;

import java.io.PrintStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

/** Navegación categorial, fichas, relaciones, búsqueda, filtros e historial. */
public final class WorldMemoryScreen {
    private static final DateTimeFormatter ACQUIRED_AT = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    private final WorldMemory memory;
    private final WorldMemoryCategoryNavigator navigator;
    private final WorldMemoryEntryViewAssembler viewAssembler;
    private final WorldMemorySearch search;
    private final WorldMemoryFilterEngine filters;
    private final ConsoleInput input;
    private final PrintStream output;

    public WorldMemoryScreen(WorldMemory memory, ConsoleInput input, PrintStream output) {
        this.memory = Objects.requireNonNull(memory);
        this.navigator = new WorldMemoryCategoryNavigator(memory.knowledge());
        this.viewAssembler = new WorldMemoryEntryViewAssembler(memory.knowledge());
        this.search = new WorldMemorySearch(memory.knowledge());
        this.filters = new WorldMemoryFilterEngine(memory.knowledge());
        this.input = Objects.requireNonNull(input);
        this.output = Objects.requireNonNull(output);
    }

    public void display() {
        boolean browsing = true;
        while (browsing) {
            List<WorldMemoryCategorySummary> categories = navigator.categories();
            renderRoot(categories);
            int selected = input.readIntegerBetween("Seleccione una familia o acción: ", 0, categories.size() + 3);
            if (selected == 0) browsing = false;
            else if (selected == categories.size() + 1) browseSearch();
            else if (selected == categories.size() + 2) browseFilters();
            else if (selected == categories.size() + 3) togglePersonalObservationMark();
            else browseCategory(categories.get(selected - 1).category());
        }
        memory.viewState().close();
    }

    private void renderRoot(List<WorldMemoryCategorySummary> categories) {
        output.println();
        output.println("MEMORIA DEL MUNDO");
        output.println("Conocimiento adquirido por el personaje");
        output.println();
        for (int i = 0; i < categories.size(); i++) {
            WorldMemoryCategorySummary summary = categories.get(i);
            output.printf("%d. %s (%d)%n", i + 1, summary.category().label(), summary.knownEntries());
        }
        output.printf("%d. Buscar por nombre%n", categories.size() + 1);
        output.printf("%d. Filtrar conocimiento%n", categories.size() + 2);
        String markState = memory.knowledge().observationMark().isEmpty()
                ? "sin marca"
                : (memory.knowledge().observationMarkSelected() ? "seleccionada · volver a seleccionar la retira" : "disponible");
        output.printf("%d. Marca de observación (%s)%n", categories.size() + 3, markState);
        output.println("0. Replegar la Memoria del Mundo");
        output.println();
    }


    private void togglePersonalObservationMark() {
        if (memory.knowledge().observationMark().isEmpty()) {
            output.println("No hay ninguna marca de observación personal activa.");
            input.waitForEnter("Pulse Intro para continuar...");
            return;
        }
        memory.knowledge().toggleObservationMarkSelection();
        output.println(memory.knowledge().observationMark().isEmpty()
                ? "La marca de observación ha sido retirada."
                : "La marca de observación ha quedado seleccionada como referencia espacial.");
        input.waitForEnter("Pulse Intro para continuar...");
    }

    private void browseSearch() {
        boolean searching = true;
        while (searching) {
            output.println();
            output.println("BUSCAR EN LA MEMORIA DEL MUNDO");
            output.println("La búsqueda ignora mayúsculas y tildes. Deje el texto vacío para volver.");
            output.println();

            WorldMemoryQuery query = new WorldMemoryQuery(input.readText("Buscar: "));
            if (query.isEmpty()) return;

            List<WorldMemorySearchResult> results = search.search(query);
            if (results.isEmpty()) {
                output.println();
                output.println("No se ha encontrado ninguna entrada adquirida con ese nombre.");
                input.waitForEnter("Pulse Intro para realizar otra búsqueda...");
                continue;
            }

            output.println();
            output.printf("RESULTADOS PARA «%s»%n%n", query.text());
            for (int i = 0; i < results.size(); i++) {
                WorldMemorySearchResult result = results.get(i);
                output.printf("%d. %s [%s]%n", i + 1, result.title(), result.category().label());
            }
            output.println("0. Nueva búsqueda");
            output.println();

            int selected = input.readIntegerBetween("Seleccione una entrada: ", 0, results.size());
            if (selected != 0) {
                WorldMemorySearchResult result = results.get(selected - 1);
                WorldMemoryEntry entry = memory.knowledge().entry(result.entryId())
                        .orElseThrow(() -> new IllegalStateException(
                                "Un resultado de búsqueda no puede apuntar a conocimiento ausente."));
                renderEntry(viewAssembler.assemble(entry));
            }
        }
    }

    private void browseFilters() {
        WorldMemoryFilter filter = readFilter();
        List<WorldMemoryEntry> results = filters.filter(filter);

        output.println();
        output.println("CONOCIMIENTO FILTRADO");
        output.println(filterSummary(filter));
        output.println();

        if (results.isEmpty()) {
            output.println("Ninguna entrada adquirida satisface todos los criterios seleccionados.");
            input.waitForEnter("Pulse Intro para volver a las familias...");
            return;
        }

        boolean browsing = true;
        while (browsing) {
            for (int i = 0; i < results.size(); i++) {
                WorldMemoryEntry entry = results.get(i);
                output.printf("%d. %s [%s]%n", i + 1, entry.title(), entry.category().label());
            }
            output.println("0. Volver a las familias");
            output.println();
            int selected = input.readIntegerBetween("Seleccione una entrada: ", 0, results.size());
            if (selected == 0) browsing = false;
            else renderEntry(viewAssembler.assemble(results.get(selected - 1)));
        }
    }

    private WorldMemoryFilter readFilter() {
        output.println();
        output.println("FILTRAR LA MEMORIA DEL MUNDO");
        output.println("Los criterios se combinan: una entrada debe satisfacerlos todos.");
        output.println();

        List<WorldMemoryCategory> categories = Arrays.stream(WorldMemoryCategory.values())
                .filter(category -> category != WorldMemoryCategory.EXPLORED_TERRITORY)
                .toList();
        output.println("Categoría:");
        output.println("0. Cualquiera");
        for (int i = 0; i < categories.size(); i++) output.printf("%d. %s%n", i + 1, categories.get(i).label());
        int categoryIndex = input.readIntegerBetween("Seleccione una categoría: ", 0, categories.size());
        Optional<WorldMemoryCategory> category = categoryIndex == 0
                ? Optional.empty() : Optional.of(categories.get(categoryIndex - 1));

        Optional<KnowledgeStatus> status = selectOptionalEnum("Estado epistémico", KnowledgeStatus.values());
        Optional<KnowledgeReliability> reliability = selectOptionalEnum("Fiabilidad", KnowledgeReliability.values());
        Optional<KnowledgeSourceType> sourceType = selectOptionalEnum("Tipo de fuente", KnowledgeSourceType.values());

        output.println();
        output.println("Memoria espacial:");
        SpatialMemoryRequirement[] spatialValues = SpatialMemoryRequirement.values();
        for (int i = 0; i < spatialValues.length; i++) output.printf("%d. %s%n", i, spatialValues[i].label());
        int spatialIndex = input.readIntegerBetween("Seleccione una condición espacial: ", 0, spatialValues.length - 1);

        return new WorldMemoryFilter(category, status, reliability, sourceType, spatialValues[spatialIndex]);
    }

    private <E extends Enum<E>> Optional<E> selectOptionalEnum(String title, E[] values) {
        output.println();
        output.println(title + ":");
        output.println("0. Cualquiera");
        for (int i = 0; i < values.length; i++) output.printf("%d. %s%n", i + 1, label(values[i].name()));
        int selected = input.readIntegerBetween("Seleccione una opción: ", 0, values.length);
        return selected == 0 ? Optional.empty() : Optional.of(values[selected - 1]);
    }

    private String filterSummary(WorldMemoryFilter filter) {
        String category = filter.category().map(WorldMemoryCategory::label).orElse("Cualquiera");
        String status = filter.status().map(value -> label(value.name())).orElse("Cualquiera");
        String reliability = filter.reliability().map(value -> label(value.name())).orElse("Cualquiera");
        String source = filter.sourceType().map(value -> label(value.name())).orElse("Cualquiera");
        return "Categoría: " + category + " · Estado: " + status + " · Fiabilidad: " + reliability
                + " · Fuente: " + source + " · Espacio: " + filter.spatialRequirement().label();
    }

    private void browseCategory(WorldMemoryCategory category) {
        if (category == WorldMemoryCategory.EXPLORED_TERRITORY) {
            renderExploredTerritory(category);
            return;
        }

        List<WorldMemoryEntry> entries = navigator.entries(category);
        if (entries.isEmpty()) {
            renderCategoryHeader(category);
            output.println("El personaje no ha adquirido conocimiento perteneciente a esta familia.");
            input.waitForEnter("Pulse Intro para volver a las familias...");
            return;
        }

        boolean browsingCategory = true;
        while (browsingCategory) {
            renderCategoryHeader(category);
            for (int i = 0; i < entries.size(); i++) {
                output.printf("%d. %s%n", i + 1, entries.get(i).title());
            }
            output.println("0. Volver a las familias");
            output.println();
            int selected = input.readIntegerBetween("Seleccione una entrada: ", 0, entries.size());
            if (selected == 0) browsingCategory = false;
            else renderEntry(viewAssembler.assemble(entries.get(selected - 1)));
        }
    }

    private void renderCategoryHeader(WorldMemoryCategory category) {
        output.println();
        output.println(category.label().toUpperCase());
        output.println(category.description());
        output.println();
    }

    private void renderEntry(WorldMemoryEntryView view) {
        boolean inspecting = true;
        while (inspecting) {
            output.println();
            output.println(view.title().toUpperCase());
            output.println();
            field("Categoría", view.category().label());
            field("Estado", label(view.status().name()));
            field("Fuente principal", sourceLabel(view));
            field("Fiabilidad", label(view.reliability().name()));
            field("Adquirido", ACQUIRED_AT.format(view.acquiredAt()));
            if (!view.description().isBlank()) field("Descripción", view.description());
            view.spatialMemory().ifPresent(this::renderSpatialMemory);
            renderRelations(view);
            output.println();
            int historyOption = view.selectableAsReference() ? 2 : 1;
            if (view.selectableAsReference()) output.println("1. Seleccionar como referencia del Astrolabio");
            output.printf("%d. Consultar historial de la entrada%n", historyOption);
            output.println("0. Volver a la categoría");
            output.println();

            int selected = input.readIntegerBetween("Seleccione una acción: ", 0, historyOption);
            if (selected == 0) inspecting = false;
            else if (selected == historyOption) renderHistory(view);
            else {
                memory.knowledge().select(view.id());
                output.println("La entrada ha quedado seleccionada como referencia recordada.");
                input.waitForEnter("Pulse Intro para continuar...");
            }
        }
    }

    private void renderHistory(WorldMemoryEntryView view) {
        List<WorldMemoryEntryRevision> revisions = memory.knowledge().historyOf(view.id());
        output.println();
        output.println("HISTORIAL — " + view.title().toUpperCase());
        output.println("La versión vigente se conserva en la ficha principal.");
        output.println();
        for (WorldMemoryEntryRevision revision : revisions) {
            output.printf("%d. %s · %s%n", revision.sequence(),
                    ACQUIRED_AT.format(revision.revisedAt()), revision.type().label());
            output.println("   Origen: " + revision.origin());
            output.println("   Estado resultante: " + label(revision.resultingStatus().name()));
            output.println("   Fiabilidad resultante: " + label(revision.resultingReliability().name()));
            revision.previousStatus().ifPresent(previous -> output.println(
                    "   Estado anterior: " + label(previous.name())));
            revision.previousReliability().ifPresent(previous -> output.println(
                    "   Fiabilidad anterior: " + label(previous.name())));
            if (!revision.note().isBlank()) output.println("   " + revision.note());
            if (!revision.snapshot().description().isBlank()) {
                output.println("   Descripción conservada: " + revision.snapshot().description());
            }
            output.println();
        }
        input.waitForEnter("Pulse Intro para volver a la ficha...");
    }

    private void renderRelations(WorldMemoryEntryView view) {
        output.println("Relaciones:");
        if (view.relations().isEmpty()) {
            output.println("No se recuerdan relaciones con otras entradas adquiridas.");
            output.println();
            return;
        }
        for (var relation : view.relations()) {
            output.printf("- %s → %s [%s]%n", relation.label(), relation.relatedEntryTitle(),
                    relation.relatedEntryCategory().label());
            if (!relation.note().isBlank()) output.println("  " + relation.note());
        }
        output.println();
    }

    private void renderSpatialMemory(RememberedPosition position) {
        WorldCoordinate coordinate = position.coordinate();
        field("Precisión espacial", label(position.precision().name()));
        field("Ubicación recordada", String.format("x %.2f · y %.2f · elevación %.2f",
                coordinate.x(), coordinate.y(), coordinate.elevation()));
        field("Incertidumbre", String.format("%.2f m", position.uncertaintyRadiusMeters()));
    }

    private String sourceLabel(WorldMemoryEntryView view) {
        return label(view.primarySourceType().name()) + " — " + view.primarySourceReference();
    }

    private void field(String label, String value) {
        output.println(label + ":");
        output.println(value);
        output.println();
    }

    private String label(String enumName) {
        String[] parts = enumName.toLowerCase().split("_");
        String joined = String.join(" ", parts);
        return Character.toUpperCase(joined.charAt(0)) + joined.substring(1);
    }

    private void renderExploredTerritory(WorldMemoryCategory category) {
        renderCategoryHeader(category);
        int observations = memory.knowledge().terrain().observationCount();
        if (observations == 0) output.println("No hay territorio explorado registrado.");
        else {
            output.printf("Observaciones territoriales registradas: %d%n", observations);
            output.println("La inspección territorial granular permanece reservada para una iteración posterior.");
        }
        input.waitForEnter("Pulse Intro para volver a las familias...");
    }
}
