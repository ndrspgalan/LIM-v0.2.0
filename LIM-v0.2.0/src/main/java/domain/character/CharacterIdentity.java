package domain.character;

import domain.social.Profession;
import java.util.Objects;

public record CharacterIdentity(String name, Gender gender, CharacterClass characterClass,
                                Profession profession, double heightMeters) {
    public CharacterIdentity {
        name = requireName(name);
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        Objects.requireNonNull(characterClass, "La clase no puede ser nula.");
        profession = Profession.canonicalOrBeggar(profession);
        if (heightMeters <= 0.5 || heightMeters > 3.0) throw new IllegalArgumentException("Altura corporal no válida.");
    }
    public static CharacterIdentity kenanCanonical() { return KenanCanonicalProfile.identity(); }

    public static CharacterIdentity jacobCanonical() {
        return new CharacterIdentity("Jacob", Gender.HOMBRE, CharacterClass.MAESTRO,
                Profession.MERCENARY, 1.75);
    }


    public static CharacterIdentity kiaraCanonical() {
        return new CharacterIdentity("Kiara", Gender.MUJER, CharacterClass.HERALDO,
                Profession.TEACHER, 1.58);
    }

    public static CharacterIdentity valerianCanonical() {
        return new CharacterIdentity("Valerian", Gender.HOMBRE, CharacterClass.INTELECTUAL,
                Profession.JURIST, 1.80);
    }

    public double weightKilograms() {
        if (name.equalsIgnoreCase("Kenan")) return KenanCanonicalProfile.WEIGHT_KILOGRAMS;
        return domain.character.canonical.CanonicalCharacterTimelineCatalog.forName(name).stream()
                .filter(p -> Math.abs(p.body().heightMeters()-heightMeters)<1e-9)
                .map(p -> p.body().weightKilograms()).findFirst()
                .orElseThrow(() -> new IllegalStateException("El peso debe proceder de un perfil antropométrico canónico explícito: "+name));
    }

    private static String requireName(String name) {
        Objects.requireNonNull(name, "El nombre no puede ser nulo.");
        String normalizedName = name.trim();
        if (normalizedName.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        return normalizedName;
    }
}
