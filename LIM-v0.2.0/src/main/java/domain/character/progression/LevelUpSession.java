package domain.character.progression;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Objects;

public final class LevelUpSession {
    private final Gender gender;
    private final CharacterClass characterClass;
    private final AttributeCapPolicy capPolicy;
    private final MucusRequirementPolicy mucusPolicy;
    private final CharacterProgressionState original;
    private CharacterProgressionState preview;

    public LevelUpSession(
            Gender gender,
            CharacterClass characterClass,
            CharacterProgressionState current,
            AttributeCapPolicy capPolicy,
            MucusRequirementPolicy mucusPolicy
    ) {
        this.gender = Objects.requireNonNull(gender, "El género no puede ser nulo.");
        this.characterClass = Objects.requireNonNull(characterClass, "La clase no puede ser nula.");
        this.original = Objects.requireNonNull(current, "La progresión actual no puede ser nula.");
        this.preview = current;
        this.capPolicy = Objects.requireNonNull(capPolicy, "La política de límites no puede ser nula.");
        this.mucusPolicy = Objects.requireNonNull(mucusPolicy, "La política de mucus no puede ser nula.");
    }

    public CharacterProgressionState original() {
        return original;
    }

    public CharacterProgressionState preview() {
        return preview;
    }

    public int maximumFor(Attribute attribute) {
        return capPolicy.maximumFor(gender, characterClass, preview.sheet(), attribute);
    }

    public MucusType requirementForNext(Attribute attribute) {
        int destination = preview.sheet().valueOf(attribute) + 1;
        return mucusPolicy.requiredForDestination(gender, attribute, destination);
    }

    public LevelUpResult increase(Attribute attribute) {
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        CharacterSheet sheet = preview.sheet();
        int current = sheet.valueOf(attribute);
        int maximum = maximumFor(attribute);
        if (current >= maximum) {
            return LevelUpResult.failure(attribute.label() + " ha alcanzado su límite actual de " + maximum + ".");
        }
        if (preview.level() >= CharacterProgressionState.MAXIMUM_LEVEL) {
            return LevelUpResult.failure("El personaje ha alcanzado el nivel máximo 999.");
        }

        MucusType required = mucusPolicy.requiredForDestination(gender, attribute, current + 1);
        if (!preview.mucusWallet().contains(required)) {
            return LevelUpResult.failure("Se requiere 1 " + required.label() + ".");
        }

        preview = new CharacterProgressionState(
                preview.level() + 1,
                sheet.increase(attribute),
                preview.mucusWallet().consumeOne(required)
        );
        return LevelUpResult.success(
                attribute.label() + " pasa provisionalmente de " + current + " a " + (current + 1)
                        + " refinando 1 " + required.label() + "."
        );
    }

    public void discard() {
        preview = original;
    }

    public boolean hasChanges() {
        return !preview.equals(original);
    }
}
