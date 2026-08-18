package qa.domain;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import org.junit.jupiter.api.Tag; import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
@Tag("domain") @Tag("gold-smoke")
final class PropertyKnowledgePolicyTest {
 @Test void hiddenPropertiesUseTheirOwnAttributeRequirementWithoutUniversalFaithOrClairvoyancePolicy(){
   var property=ItemProperty.hiddenWithHiddenRequirement(ItemPropertyId.GENERIC,"FUNCIÓN","",Attribute.CLARIVIDENCIA,22,"ACTIVA");
   var below=CharacterSheet.of(1,1,1,1,1,1,75,1,21); var at=CharacterSheet.of(1,1,1,1,1,1,1,1,22);
   assertFalse(property.isVisibleTo(below)); assertFalse(property.isActiveFor(below));
   assertTrue(property.isVisibleTo(at)); assertTrue(property.isActiveFor(at));
 }
}
