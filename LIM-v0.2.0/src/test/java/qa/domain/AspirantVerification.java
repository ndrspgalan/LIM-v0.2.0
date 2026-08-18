package qa.domain;

import domain.bestiarium.physical_plane.aspirant.*;
import domain.combat.DamageType;
import domain.runic.RunicMarkId;

public final class AspirantVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var ref=new ConvergentAnimalReference("jabalí","Sus scrofa");
        var human=new AspirantMorphology(ref,new AnthropometricDeviation(1),new AnthropometricDeviation(9));
        var animaloid=new AspirantMorphology(ref,new AnthropometricDeviation(9),new AnthropometricDeviation(2));
        var incoherent=new AspirantMorphology(ref,new AnthropometricDeviation(9),new AnthropometricDeviation(9));
        org.junit.jupiter.api.Assertions.assertTrue(human.region()==AspirantMorphologyRegion.HUMAN_DOMINANT,"Debe existir extremo humano-dominante.");
        org.junit.jupiter.api.Assertions.assertTrue(animaloid.region()==AspirantMorphologyRegion.ANIMALOID_SYNTHESIS,"Debe existir síntesis animaloide sin convertirse en animal.");
        org.junit.jupiter.api.Assertions.assertTrue(incoherent.region()==AspirantMorphologyRegion.INCOHERENT_CONFIGURATION,"Las distancias siguen siendo independientes, pero la aberración divergente deja de ser canónica.");
        org.junit.jupiter.api.Assertions.assertTrue(animaloid.canonicalSynthesis()&&!incoherent.canonicalSynthesis(),"La síntesis canónica debe excluir incoherencia y convergencia animal literal.");
        var overlay=new AspirantRunicOverlay(RunicMarkId.PARHELIO);
        org.junit.jupiter.api.Assertions.assertTrue(overlay.effectiveMark()==RunicMarkId.CAMBIAFORMAS&&overlay.originalEffectsSuppressed(),"CAMBIAFORMAS debe sobrescribir la marca original.");
        org.junit.jupiter.api.Assertions.assertTrue(overlay.toggleByMouseWheelClick()==AspirantForm.CAMBIAFORMAS,"MOUSE WHEEL debe activar CAMBIAFORMAS.");
        var damage=new AspirantDamagePolicy();
        org.junit.jupiter.api.Assertions.assertTrue(damage.canReceive(DamageType.SLASHING)&&damage.canReceive(DamageType.CURSE),"ASPIRANT sigue siendo lesionable por canales ordinarios existentes.");
        org.junit.jupiter.api.Assertions.assertTrue(damage.outgoingType(AspirantForm.HUMANA,DamageType.SLASHING)==DamageType.SLASHING,"HUMANA conserva el canal material del ataque.");
        org.junit.jupiter.api.Assertions.assertTrue(damage.outgoingType(AspirantForm.CAMBIAFORMAS,DamageType.SLASHING)==DamageType.SLASHING,"CAMBIAFORMAS ya no convierte la salida a MALDICIÓN.");
        org.junit.jupiter.api.Assertions.assertTrue(AspirantDoctrine.CANON.contains("nunca se diseña primero un monstruo"),"Debe persistir la regla de diseño.");
    }
    
}
