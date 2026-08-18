package domain.ability.progression;

import domain.ability.*;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Set;

/** Evalúa descubrimientos irreversibles sin anticipar maestrías desconocidas. */
public final class MasteryProgressionPolicy {
    public static final Set<String> REQUIRED_FEARE_TROPHIES = Set.of(
            "cola de rata", "pluma de cuervo", "cráneo de lobo", "ojo de lince", "garras de águila",
            "colmillo de jabalí", "zarpa de oso", "cornamenta de ciervo", "oreja de toro", "cerda de caballo",
            "pezuña de cerdo", "caparazón de armadillo", "crin de león", "cuerno de rinoceronte", "piel de serpiente");


    public void evaluate(CharacterMasteryCollection collection, CharacterSheet sheet, MasteryProgressState progress) {
        evaluate(collection, sheet, progress, Gender.HOMBRE);
    }

    public void evaluate(CharacterMasteryCollection collection, CharacterSheet sheet, MasteryProgressState progress, Gender gender) {
        revealIndomitablePairs(collection, progress);
        progressIncitement(collection, sheet, gender);
        progressAnimalEmpathy(collection, sheet, progress);
        progressThermalHomeostasis(collection, sheet, progress);
        progressHealingTriad(collection, sheet);
        progressMalignantEnergyRefinement(collection, sheet, gender);
        progressTransmutation(collection, sheet, progress);
        progressTheta(collection, sheet, progress);
        progressTirelessSpirit(collection, sheet, progress);
        progressConvergentTrajectory(collection, sheet, progress);
        unlockAtOnce(collection, sheet, MasteryId.INVISIBILIDAD, Attribute.DESTREZA, 70);
        progressEvolutives(collection, sheet);
        unlockRevealedFamiliesByAttributes(collection, sheet);
        collection.synchronizePassiveManifestations(sheet);
    }

    private void revealIndomitablePairs(CharacterMasteryCollection c, MasteryProgressState p) {
        if (!p.sleptAtMaximumHungerAndThirstPenalty()) return;
        c.reveal(MasteryId.PULSION); c.reveal(MasteryId.EXPLOSION_CINETICA);
        c.reveal(MasteryId.LIBERACION_HELICOIDAL); c.reveal(MasteryId.ANULACION);
    }

    private void progressIncitement(CharacterMasteryCollection c, CharacterSheet s, Gender gender) {
        if (gender == Gender.HOMBRE) {
            if (s.valueOf(Attribute.FUERZA) >= 25) c.unlockStage(MasteryId.INCITAR, "PROVOCAR");
            if (s.valueOf(Attribute.FUERZA) >= 50) c.unlockStage(MasteryId.INCITAR, "GRITO DE GUERRA");
        } else {
            if (s.valueOf(Attribute.CARISMA) >= 18) c.unlockStage(MasteryId.INCITAR, "CAPITALIZAR");
            if (s.valueOf(Attribute.CARISMA) >= 21) c.unlockStage(MasteryId.INCITAR, "RENTABILIZAR");
        }
    }

    private void progressMalignantEnergyRefinement(CharacterMasteryCollection c, CharacterSheet s, Gender gender) {
        if (!c.knowledgeState(MasteryId.EXPLOSION_CINETICA).isVisible()) return;
        int endurance=s.valueOf(Attribute.AGUANTE);
        int first=gender==Gender.HOMBRE?20:15;
        if(endurance>=first)c.unlock(MasteryId.EXPLOSION_CINETICA);
    }

    private void progressAnimalEmpathy(CharacterMasteryCollection c, CharacterSheet s, MasteryProgressState p) {
        if (c.knowledgeState(MasteryId.EMPATIA_ANIMAL) == MasteryKnowledgeState.UNLOCKED) return;
        if (p.heldFeareTrophies().containsAll(REQUIRED_FEARE_TROPHIES) && !p.pettedCharismaFerae().isEmpty()) {
            c.reveal(MasteryId.EMPATIA_ANIMAL);
            if (s.valueOf(Attribute.CARISMA) >= 12 || s.valueOf(Attribute.INTELIGENCIA) >= 12) c.unlock(MasteryId.EMPATIA_ANIMAL);
        }
    }

    private void progressThermalHomeostasis(CharacterMasteryCollection c, CharacterSheet s, MasteryProgressState p) {
        if (p.validBitingFrostExposures() >= 14) c.reveal(MasteryId.HOMEOSTASIS_TERMICA);
        if (c.knowledgeState(MasteryId.HOMEOSTASIS_TERMICA).isVisible() && s.valueOf(Attribute.DESTREZA) >= 20) c.unlock(MasteryId.HOMEOSTASIS_TERMICA);
    }

    private void progressHealingTriad(CharacterMasteryCollection c, CharacterSheet s) {
        int faith = s.valueOf(Attribute.FE);
        if (faith >= 32) c.unlockStage(MasteryId.SANAR, "DRENAR");
        if (faith >= 40) c.unlockStage(MasteryId.SANAR, "RESTAURAR");
        if (faith >= 60) c.unlockStage(MasteryId.SANAR, "CUSTODIA");
    }

    private void progressTransmutation(CharacterMasteryCollection c, CharacterSheet s, MasteryProgressState p) {
        if (p.sufferedRealFrenzyDamage()) c.reveal(MasteryId.TRANSMUTACION);
        if (c.knowledgeState(MasteryId.TRANSMUTACION).isVisible() && s.valueOf(Attribute.CLARIVIDENCIA) >= 11) {
            c.unlock(MasteryId.TRANSMUTACION);
            c.unlockAvailableTransmutationNodes(s);
        }
    }

    private void progressTheta(CharacterMasteryCollection c, CharacterSheet s, MasteryProgressState p) {
        if (p.sleptAfterFortyEightAwakeHours()) c.reveal(MasteryId.REGENERACION_THETA);
        if (c.knowledgeState(MasteryId.REGENERACION_THETA).isVisible() && s.valueOf(Attribute.FE) >= 13) c.unlock(MasteryId.REGENERACION_THETA);
    }

    private void progressTirelessSpirit(CharacterMasteryCollection c, CharacterSheet s, MasteryProgressState p) {
        if (p.attemptedRunning() && p.attemptedClimbing() && p.attemptedSwimming()) c.reveal(MasteryId.ESPIRITU_INFATIGABLE);
        if (c.knowledgeState(MasteryId.ESPIRITU_INFATIGABLE).isVisible() && s.valueOf(Attribute.FE) >= 3) c.unlock(MasteryId.ESPIRITU_INFATIGABLE);
    }

    private void progressConvergentTrajectory(CharacterMasteryCollection c, CharacterSheet s, MasteryProgressState p) {
        if (p.unarmedLightAttacks() >= 1000) c.reveal(MasteryId.TRAYECTORIA_CONVERGENTE);
        if (c.knowledgeState(MasteryId.TRAYECTORIA_CONVERGENTE).isVisible() && s.valueOf(Attribute.DESTREZA) >= 20) c.unlock(MasteryId.TRAYECTORIA_CONVERGENTE);
    }

    private void progressEvolutives(CharacterMasteryCollection c, CharacterSheet s) {
        if (s.valueOf(Attribute.VITALIDAD) >= 75) c.reveal(MasteryId.ELECTROGENESIS);
        if (s.valueOf(Attribute.VITALIDAD) >= 76) c.unlock(MasteryId.ELECTROGENESIS);
        if (s.valueOf(Attribute.ADAPTABILIDAD) >= 75) c.reveal(MasteryId.TRIBOGENESIS);
        if (s.valueOf(Attribute.ADAPTABILIDAD) >= 76) c.unlock(MasteryId.TRIBOGENESIS);
    }

    private void unlockAtOnce(CharacterMasteryCollection c, CharacterSheet s, MasteryId id, Attribute a, int value) {
        if (s.valueOf(a) >= value) c.unlock(id);
    }

    private void unlockRevealedFamiliesByAttributes(CharacterMasteryCollection c, CharacterSheet s) {
        for (MasteryId id : c.visibleIds()) {
            if (c.knowledgeState(id) != MasteryKnowledgeState.REVEALED) continue;
            Mastery mastery = MasteryCatalog.require(id);
            if (id == MasteryId.SANAR || id == MasteryId.TRANSMUTACION || id == MasteryId.INCITAR ||
                    id == MasteryId.EMPATIA_ANIMAL || id == MasteryId.HOMEOSTASIS_TERMICA ||
                    id == MasteryId.REGENERACION_THETA || id == MasteryId.ESPIRITU_INFATIGABLE || id == MasteryId.EXPLOSION_CINETICA ||
                    id == MasteryId.TRAYECTORIA_CONVERGENTE || id == MasteryId.INVISIBILIDAD ||
                    mastery instanceof EvolutiveMastery) continue;
            if (meetsFirstRequirement(mastery, s)) c.unlock(id);
        }
    }

    private boolean meetsFirstRequirement(Mastery mastery, CharacterSheet sheet) {
        if (mastery instanceof PairMastery p) return p.original().isAccessibleTo(sheet);
        if (mastery instanceof StructuredMastery s) return s.stages().stream().anyMatch(stage -> stage.isAccessibleTo(sheet));
        return false;
    }
}
