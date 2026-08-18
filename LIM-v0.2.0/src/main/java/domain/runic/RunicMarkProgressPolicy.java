package domain.runic;

import domain.ability.CharacterMasteryCollection;
import domain.ability.MasteryCatalog;
import domain.ability.MasteryId;
import domain.ability.MasteryCategory;
import domain.ability.MasteryResonancePolicy;
import domain.character.Gender;
import domain.character.CharacterClass;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Arrays;

/** La rama afín completa manifiesta la geometría; todas las maestrías completadas le conceden función. */
public final class RunicMarkProgressPolicy {
    public RunicMarkProgressState resolve(CharacterClass characterClass, CharacterMasteryCollection collection, CharacterSheet sheet) {
        Gender gender = collection.ownerGender().orElseGet(() -> switch(characterClass) {
            case LUCHADOR, INTELECTUAL, INDOMITO -> Gender.HOMBRE;
            case ESPECIALISTA, APODERADO, HERALDO -> Gender.MUJER;
            case MAESTRO -> Gender.HOMBRE; // compatibilidad de colecciones antiguas sin sexo; los perfiles canónicos deben suministrarlo.
        });
        // 75 en el atributo profesional revela la Marca. El límite humano no depende de los HITOS evolutivos.
        if (sheet.valueOf(affinityAttribute(characterClass)) < 75) return RunicMarkProgressState.ABSENT;
        boolean allHumanMasteriesUnlocked = Arrays.stream(MasteryId.values())
                .filter(id -> MasteryCatalog.require(id).category() != MasteryCategory.EVOLUTIVE)
                .allMatch(id -> collection.knowledgeState(id).isUsable());
        return allHumanMasteriesUnlocked ? RunicMarkProgressState.AWAKENED : RunicMarkProgressState.COSMETIC;
    }

    public Attribute affinityAttribute(CharacterClass c) {
        return switch (c) {
            case LUCHADOR -> Attribute.FUERZA;
            case INTELECTUAL -> Attribute.INTELIGENCIA;
            case INDOMITO -> Attribute.AGUANTE;
            case ESPECIALISTA -> Attribute.DESTREZA;
            case APODERADO -> Attribute.FE;
            case HERALDO -> Attribute.CARISMA;
            case MAESTRO -> Attribute.CLARIVIDENCIA;
        };
    }
}
