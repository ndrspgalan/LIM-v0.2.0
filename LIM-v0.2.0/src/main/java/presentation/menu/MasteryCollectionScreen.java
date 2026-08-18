package presentation.menu;

import domain.ability.*;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import presentation.console.ConsoleInput;

import java.io.PrintStream;
import java.util.*;

/** Colección universal de familias de maestrías, navegable por afinidad de clase. */
public final class MasteryCollectionScreen {
    public static final String GENERAL_NARRATIVE = "Una maestría no añade al individuo una facultad que antes no existía. Designa un estado elevado en el que una capacidad potencial, sometida a suficiente atributo, adversidad, repetición y reconocimiento, deja de manifestarse como accidente y se estabiliza como actuación reproducible. El Grimorio no concede estas posibilidades: registra el momento en que el individuo aprende a reconocerse en ellas. Por eso las vías de revelación difieren. Cada maestría exige haber vivido el tipo de problema que vuelve inteligible su solución.";
    private final CharacterSheet sheet;
    private final CharacterMasteryCollection characterMasteries;
    private final Gender gender;
    private final ConsoleInput input;
    private final PrintStream output;

    public MasteryCollectionScreen(CharacterSheet sheet, CharacterMasteryCollection characterMasteries, Gender gender, ConsoleInput input, PrintStream output) {
        this.sheet = Objects.requireNonNull(sheet);
        this.characterMasteries = Objects.requireNonNull(characterMasteries);
        this.gender = Objects.requireNonNull(gender);
        this.input = Objects.requireNonNull(input);
        this.output = Objects.requireNonNull(output);
    }

    public void open() {
                boolean open=true;
        while(open){
            output.println();
            output.println(GENERAL_NARRATIVE);
            List<Mastery> masteries=characterMasteries.visibleIds().stream()
                    .map(MasteryCatalog::require)
                    .filter(m->m.category()!=MasteryCategory.EVOLUTIVE)
                    .sorted(Comparator.comparing(Mastery::name))
                    .toList();
            output.println();
            output.println("================================================================");
            output.println("COLECCIÓN DE MAESTRÍAS DE KENAN");
            output.println("================================================================");
            for(int i=0;i<masteries.size();i++){
                Mastery m=masteries.get(i);
                output.printf("%d. %s [%s]%n",i+1,m.name(),characterMasteries.knowledgeState(m.id()).name());
            }
            output.println("0. Volver");
            String command=input.readText("MAESTRÍAS> ").trim();
            if(command.equals("0")){open=false;continue;}
            try{int selected=Integer.parseInt(command)-1; if(selected>=0&&selected<masteries.size()){Mastery m=masteries.get(selected);inspect(m,characterMasteries.knowledgeState(m.id()));}else output.println("Selección no válida.");}
            catch(NumberFormatException ex){output.println("Orden no reconocida.");}
        }
    }

    private void inspect(Mastery mastery, MasteryKnowledgeState state) {
        output.println();
        output.println(mastery.name() + " [" + mastery.structure() + "]");
        output.println("Afinidad de clase: " + affinityLabel(mastery));
        output.println(mastery.narrativeDescription());
        if (state == MasteryKnowledgeState.REVEALED) {
            input.waitForEnter("Pulse Intro para volver a la colección...");
            return;
        }
        if (mastery instanceof PairMastery pair) {
            for (MasteryVariant variant : characterMasteries.visibleVariants(pair, sheet)) printVariant(variant);
        } else if (mastery instanceof StructuredMastery structured) {
            for (MasteryStage stage : characterMasteries.visibleStages(structured, sheet)) {
                if (structured.id() == MasteryId.INCITAR && !stageMatchesGender(stage)) continue;
                String types = stage.natures().stream().map(MasteryType::label).sorted().reduce((a,b)->a+"/"+b).orElse("");
                output.printf("- %s [%s · %s %d]%n", stage.name(), types,
                        stage.progressionAttributeOptional().map(a -> a.label()).orElse("SIN REQUISITO"), stage.threshold());
                stage.narrativeDescriptionOptional().ifPresent(output::println);
                output.println(stage.mechanicalDescription());
            }
        } else if (mastery instanceof TransmutationMastery transmutation) {
            for (TransmutationNode node : characterMasteries.visibleTransmutationNodes(sheet)) {
                output.printf("- %s [%s · %s %d]%n%s%n%s%n", node.name(), node.type().label(),
                        node.requirementAttributeOptional().map(a -> a.label()).orElse("SIN REQUISITO"), node.requirementMinimum(), node.narrativeDescription(), node.mechanicalDescription());
            }
        } else if (mastery instanceof EvolutiveMastery evolutive) {
            output.printf("[%s 76-120]%n%s%n", evolutive.progressionAttribute().label(), evolutive.mechanicalDescription());
        }
        input.waitForEnter("Pulse Intro para volver a la colección...");
    }

    private String affinityLabel(Mastery mastery) {
        if (mastery.resonanceClass() == null) return "UNIVERSAL";
        if (mastery.id() == MasteryId.INCITAR) return gender == Gender.MUJER ? CharacterClass.HERALDO.label() : CharacterClass.LUCHADOR.label();
        return mastery.resonanceClass().label();
    }

    private boolean stageMatchesGender(MasteryStage stage) {
        return switch (stage.name()) {
            case "PROVOCAR", "GRITO DE GUERRA" -> gender == Gender.HOMBRE;
            case "CAPITALIZAR", "RENTABILIZAR" -> gender == Gender.MUJER;
            default -> true;
        };
    }

    private void printVariant(MasteryVariant variant) {
        output.printf("- %s [%s · %s %d/%d]%n%s%n%s%n", variant.name(), variant.type().label(),
                variant.scalingAttribute().label(), variant.scalingStart(), variant.accessibilityThreshold(),
                variant.narrativeDescription(), variant.mechanicalDescription());
    }
}
