package qa.domain;
import domain.bestiarium.physical_plane.ancient.*;
/**  historical contract narrowed in : detailed ANCIENT combat remains explicitly pending. */
public final class AncientVerification{
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  org.junit.jupiter.api.Assertions.assertTrue(AncientCatalog.all().size()==7,"Siete ANCIENT permanecen definidos");
  for(var p:AncientCatalog.all()){org.junit.jupiter.api.Assertions.assertTrue(p!=null,"Perfil Ancient no nulo"); org.junit.jupiter.api.Assertions.assertTrue(p.archetype()!=null,"Arquetipo Ancient definido"); org.junit.jupiter.api.Assertions.assertTrue(p.sheet()!=null,"Hoja Ancient estructural definida");}
 }
 
}
