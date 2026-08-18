package qa.property;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.character.progression.AttributeActorCapPolicy;
import domain.character.progression.AttributeActorScope;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.property.support.SeededPropertySupport;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("property")
@Tag("gold-smoke")
final class CharacterSheetPropertyTest {
    @Test
    void generatedSheetsPreserveAttributeAndLevelInvariants() {
        long seed = SeededPropertySupport.propertySeed();
        var random = SeededPropertySupport.random(seed);
        int cases = SeededPropertySupport.propertyCases();

        for (int i = 0; i < cases; i++) {
            AttributeActorScope scope = AttributeActorScope.values()[random.nextInt(AttributeActorScope.values().length)];
            EnumMap<Attribute, Integer> values = new EnumMap<>(Attribute.class);
            int expectedTotal = 0;
            for (Attribute attribute : Attribute.values()) {
                int maximum = AttributeActorCapPolicy.absoluteMaximum(scope, attribute);
                int value = random.nextInt(CharacterSheet.MINIMUM_ATTRIBUTE_VALUE, maximum + 1);
                values.put(attribute, value);
                expectedTotal += value;
            }
            int caseIndex = i;
            int total = expectedTotal;
            SeededPropertySupport.check("CharacterSheet/" + scope, seed, i, values, () -> {
                var sheet = new CharacterSheet(values);
                AttributeActorCapPolicy.requireValid(scope, sheet);
                assertEquals(total, sheet.totalAttributeLevel());
                assertTrue(sheet.totalAttributeLevel() >= Attribute.values().length);
                int actorMaximum = java.util.Arrays.stream(Attribute.values())
                        .mapToInt(a -> AttributeActorCapPolicy.absoluteMaximum(scope, a)).sum();
                assertTrue(sheet.totalAttributeLevel() <= actorMaximum);
                for (Attribute attribute : Attribute.values()) assertEquals(values.get(attribute).intValue(), sheet.valueOf(attribute));

                Attribute chosen = Attribute.values()[caseIndex % Attribute.values().length];
                int maximum = AttributeActorCapPolicy.absoluteMaximum(scope, chosen);
                if (sheet.valueOf(chosen) < maximum) {
                    var increased = sheet.increase(chosen);
                    AttributeActorCapPolicy.requireValid(scope, increased);
                    assertEquals(sheet.totalAttributeLevel() + 1, increased.totalAttributeLevel());
                    assertEquals(sheet.valueOf(chosen) + 1, increased.valueOf(chosen));
                } else {
                    // CharacterSheet sólo conoce el techo estructural; el techo contextual pertenece a AttributeActorCapPolicy.
                    if (maximum == CharacterSheet.structuralMaximum(chosen)) assertThrows(IllegalStateException.class, () -> sheet.increase(chosen));
                    else assertThrows(IllegalArgumentException.class, () -> AttributeActorCapPolicy.requireValid(scope, sheet.increase(chosen)));
                }
            });
        }
    }

    @Test
    void onlyCanonicalSpecialActorsMayUseVitalityOrAdaptabilityAbove75() {
        var extended=CharacterSheet.of(120,75,120,75,75,75,75,75,75);
        for(var allowed: java.util.List.of(AttributeActorScope.KENAN,AttributeActorScope.CANONICAL_NPC,AttributeActorScope.ASPIRANT,AttributeActorScope.ANCIENT)) AttributeActorCapPolicy.requireValid(allowed,extended);
        for(var denied: java.util.List.of(AttributeActorScope.PROCEDURAL_SUBPROFESSION_NPC,AttributeActorScope.FERAE,AttributeActorScope.OTHER)) assertThrows(IllegalArgumentException.class,()->AttributeActorCapPolicy.requireValid(denied,extended));
        assertEquals(75,AttributeActorCapPolicy.absoluteMaximum(AttributeActorScope.FERAE,Attribute.VITALIDAD));
    }
}
