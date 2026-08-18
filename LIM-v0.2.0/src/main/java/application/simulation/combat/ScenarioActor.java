package application.simulation.combat;

import domain.bestiarium.physical_plane.ferae.FeraeSex;
import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.social.Profession;
import domain.social.Subprofession;

import java.util.Objects;
import java.util.Optional;

/**
 * Actor individual dentro de un escenario determinista. El escuadrón coordina, pero no sustituye
 * su identidad ni su estado material: no existe mente colmena a nivel de combatiente.
 */
public record ScenarioActor(
        String actorId,
        Optional<Subprofession> subprofession,
        Optional<FeraeSpecies> feraeSpecies,
        Optional<FeraeSex> feraeSex,
        Gender gender,
        CharacterSheet sheet,
        double heightMeters,
        double currentPa,
        double totalPa
) {
    public ScenarioActor {
        if (actorId == null || actorId.isBlank()) throw new IllegalArgumentException("Actor sin identidad.");
        subprofession = Objects.requireNonNull(subprofession);
        feraeSpecies = Objects.requireNonNull(feraeSpecies);
        feraeSex = Objects.requireNonNull(feraeSex);
        Objects.requireNonNull(gender);
        Objects.requireNonNull(sheet);
        if (subprofession.isPresent() == feraeSpecies.isPresent())
            throw new IllegalArgumentException("Un actor debe ser humano o Ferae, nunca ambos/ninguno.");
        if (feraeSpecies.isPresent() != feraeSex.isPresent())
            throw new IllegalArgumentException("Toda Ferae necesita sexo canónico y un humano no usa FeraeSex.");
        if (!Double.isFinite(heightMeters) || heightMeters <= 0) throw new IllegalArgumentException("Altura inválida.");
        if (!Double.isFinite(totalPa) || !Double.isFinite(currentPa) || totalPa <= 0 || currentPa < 0 || currentPa > totalPa)
            throw new IllegalArgumentException("PA inválidos.");
    }

    public boolean armedHuman() {
        return subprofession.filter(s -> s.profession() == Profession.SOLDIER || s.profession() == Profession.MERCENARY).isPresent();
    }
}
