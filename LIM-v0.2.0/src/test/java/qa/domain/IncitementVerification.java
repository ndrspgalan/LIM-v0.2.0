package qa.domain;

import domain.ability.*;
import domain.ability.progression.MasteryProgressState;
import domain.ability.progression.MasteryProgressionPolicy;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.economy.*;
import domain.social.RelationshipType;

public final class IncitementVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  var mastery=(StructuredMastery)MasteryCatalog.require(MasteryId.INCITAR);
  org.junit.jupiter.api.Assertions.assertTrue(mastery.stages().stream().map(MasteryStage::name).toList().equals(java.util.List.of("PROVOCAR","GRITO DE GUERRA","CAPITALIZAR","RENTABILIZAR")),"Cuatro manifestaciones .");
  var policy=new MasteryProgressionPolicy(); var female=CharacterMasteryCollection.forClass(CharacterClass.LUCHADOR);
  policy.evaluate(female,sheet(1,18),new MasteryProgressState(),Gender.MUJER); org.junit.jupiter.api.Assertions.assertTrue(female.isStageUnlocked(MasteryId.INCITAR,"CAPITALIZAR"),"Capitalizar 18");
  policy.evaluate(female,sheet(1,21),new MasteryProgressState(),Gender.MUJER); org.junit.jupiter.api.Assertions.assertTrue(female.isStageUnlocked(MasteryId.INCITAR,"RENTABILIZAR"),"Rentabilizar 21");
  org.junit.jupiter.api.Assertions.assertTrue(IncitementCommercePolicy.capitalizeDiscountPercent(Gender.MUJER,Gender.HOMBRE,30,20,EconomicGoodType.PRIVATE_USE)==30.0,"Capitalizar techo");
  org.junit.jupiter.api.Assertions.assertTrue(IncitementCommercePolicy.profitableSaleBonusPercent(Gender.MUJER,Gender.HOMBRE,RelationshipType.FRIENDLY,EconomicGoodType.FIRST_NECESSITY)==100.0,"Rentabilizar amistosa");
  var cry=new WarCryStaminaPolicy().prepare(Gender.HOMBRE,100,100); org.junit.jupiter.api.Assertions.assertTrue(cry.accepted(),"Grito con PA completos");
 }
 private static CharacterSheet sheet(int strength,int charisma){return CharacterSheet.of(1,1,1,strength,1,1,1,charisma,1);}
 
}
