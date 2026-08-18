package qa.domain;
import domain.social.*;
/** las ramas provisionales de cobertura de armadura fueron descartadas antes. */
public final class ArmorDrivenSubprofessionExpansionVerification {
 private ArmorDrivenSubprofessionExpansionVerification(){}
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  for(Subprofession s:Subprofession.values()){
   org.junit.jupiter.api.Assertions.assertTrue(!s.name().equals("ROAD_RIDER")&&!s.name().equals("BULL_BREEDER")&&!s.name().equals("SALON_HOSTESS"),"No deben sobrevivir ramas provisionales : "+s);
  }
 }
 
}
