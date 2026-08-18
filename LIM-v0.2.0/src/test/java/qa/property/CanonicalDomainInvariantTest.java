package qa.property;

import domain.bestiarium.physical_plane.aspirant.AspirantReferenceCatalog;
import domain.bestiarium.physical_plane.aspirant.AspirantReferenceId;
import domain.bestiarium.physical_plane.aspirant.AspirantSubprofessionAffinityPolicy;
import domain.bestiarium.physical_plane.ferae.FeraeBranch;
import domain.bestiarium.physical_plane.ferae.FeraeCatalog;
import domain.bestiarium.physical_plane.ferae.FeraeMorphologyCatalog;
import domain.bestiarium.physical_plane.ferae.FeraeSex;
import domain.bestiarium.physical_plane.ferae.charisma.CharismaFeraeProfiles;
import domain.bestiarium.physical_plane.ferae.intelligence.IntelligenceFeraeProfiles;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.social.Subprofession;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("property")
@Tag("domain")
@Tag("gold-smoke")
final class CanonicalDomainInvariantTest {
    @Test
    void everyCanonicalSubprofessionHasFiniteHeightForBothSexesAndAspirantCoverage() {
        for (Subprofession subprofession : Subprofession.values()) {
            for (Gender gender : Gender.values()) {
                double height = subprofession.canonicalHeightMeters(gender);
                assertTrue(Double.isFinite(height) && height > 0.0,
                        () -> subprofession + " carece de altura canónica válida para " + gender);
            }
            assertFalse(AspirantSubprofessionAffinityPolicy.compatibleReferences(subprofession).isEmpty(),
                    () -> subprofession + " carece de referente ASPIRANT compatible.");
        }
    }

    @Test
    void everyFeraeHasTwoProfilesValidAttributesAndMorphology() {
        for (FeraeBranch branch : FeraeBranch.values()) {
            for (var species : FeraeCatalog.branch(branch)) {
                for (FeraeSex sex : FeraeSex.values()) {
                    var profile = branch == FeraeBranch.CARISMA
                            ? CharismaFeraeProfiles.of(species, sex)
                            : IntelligenceFeraeProfiles.of(species, sex);
                    assertEquals(species, profile.species());
                    assertEquals(sex, profile.sex());
                    assertEquals(profile.attributes().totalAttributeLevel(), profile.canonicalLevel());
                    assertTrue(Double.isFinite(FeraeMorphologyCatalog.canonicalHeightMeters(species, sex)));
                    assertTrue(profile.canonicalHeightMeters() > 0.0);
                    for (Attribute attribute : Attribute.values()) {
                        int value = profile.attributes().valueOf(attribute);
                        assertTrue(value >= CharacterSheet.MINIMUM_ATTRIBUTE_VALUE);
                        assertTrue(value <= CharacterSheet.ABSOLUTE_MAXIMUM_ATTRIBUTE_VALUE);
                    }
                }
            }
        }
        assertEquals(FeraeCatalog.all().size(), FeraeMorphologyCatalog.all().size());
    }

    @Test
    void everyAspirantReferenceIsCataloguedAndEvolutionarilyConstrained() {
        assertEquals(AspirantReferenceId.values().length, AspirantReferenceCatalog.all().size());
        for (AspirantReferenceId id : AspirantReferenceId.values()) {
            var profile = AspirantReferenceCatalog.profile(id);
            assertEquals(id, profile.id());
            assertFalse(profile.ecology().mobilityDomains().isEmpty());
            assertFalse(profile.ecology().humanDrives().isEmpty());
            assertFalse(profile.ecology().preferredShelters().isEmpty());
            assertTrue(profile.evolutionaryAffinity().minimumCambiaformasHumanDeviation() >= 0);
        }
    }
}
