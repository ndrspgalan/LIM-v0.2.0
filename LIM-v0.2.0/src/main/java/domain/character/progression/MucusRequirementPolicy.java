package domain.character.progression;

import domain.character.Gender;
import domain.character.sheet.Attribute;

import java.util.List;
import java.util.Objects;

public final class MucusRequirementPolicy {
    private final GenderSoftcapProfile softcaps;

    public MucusRequirementPolicy(GenderSoftcapProfile softcaps) {
        this.softcaps = Objects.requireNonNull(softcaps, "Los softcaps no pueden ser nulos.");
    }

    public MucusType requiredForDestination(Gender gender, Attribute attribute, int destinationValue) {
        if (destinationValue < 2 || destinationValue > 120) {
            throw new IllegalArgumentException("El nivel de destino debe estar entre 2 y 120.");
        }
        List<Integer> attributeSoftcaps = softcaps.softcaps(gender, attribute);
        int surpassedSoftcaps = 0;
        for (int softcap : attributeSoftcaps) {
            if (destinationValue > softcap) surpassedSoftcaps++;
        }
        return MucusType.forRarity(surpassedSoftcaps);
    }

    public int requiredQuantity() {
        return 1;
    }
}
