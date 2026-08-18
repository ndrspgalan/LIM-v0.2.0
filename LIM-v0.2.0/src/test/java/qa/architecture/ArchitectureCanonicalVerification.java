package qa.architecture;

import domain.character.Gender;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.sheet.Attribute;
import domain.control.ControlAction;
import domain.control.PcControlScheme;
import domain.control.Ps4ControlScheme;
import domain.inventory.logistics.PersonalTransportCallPolicy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public final class ArchitectureCanonicalVerification {
    private ArchitectureCanonicalVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(Path.of("src/main/java/domain/bestiarium/BestiaryFamily.java")), "La fachada BestiaryFamily obsoleto debe estar eliminada.");
        org.junit.jupiter.api.Assertions.assertTrue(GenderSoftcapProfile.canonical().softcaps(Gender.HOMBRE, Attribute.FE).equals(java.util.List.of(3,13,32,40,60)), "FE hombre.");
        org.junit.jupiter.api.Assertions.assertTrue(GenderSoftcapProfile.canonical().softcaps(Gender.MUJER, Attribute.FE).equals(java.util.List.of(3,13,32,40,60)), "FE mujer.");
        Set<ControlAction> pc = PcControlScheme.canonicalBindings().stream().map(b -> b.action()).collect(Collectors.toSet());
        Set<ControlAction> ps4 = Ps4ControlScheme.canonicalBindings().stream().map(b -> b.action()).collect(Collectors.toSet());
        org.junit.jupiter.api.Assertions.assertTrue(pc.equals(ps4), "PC y PS4 deben exponer el mismo vocabulario semántico: PC-PS4=" + difference(pc, ps4) + ", PS4-PC=" + difference(ps4, pc));
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportCallPolicy.RENDER_DISTANCE_METERS == 1500.0, "Render transporte 1500 m.");
        org.junit.jupiter.api.Assertions.assertTrue(!Files.exists(Path.of("src/main/java/domain/inventory/ClassStartingLoadout.java")), "Sin ClassStartingLoadout muerto.");
    }

    private static Set<ControlAction> difference(Set<ControlAction> a, Set<ControlAction> b) {
        var copy = new java.util.HashSet<>(a); copy.removeAll(b); return copy;
    }
    
}
