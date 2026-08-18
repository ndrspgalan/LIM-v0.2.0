package domain.character.progression;

import domain.character.Gender;
import domain.character.sheet.Attribute;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class GenderSoftcapProfile {
    private final Map<Gender, Map<Attribute, List<Integer>>> profiles;

    public GenderSoftcapProfile(Map<Gender, Map<Attribute, List<Integer>>> profiles) {
        Objects.requireNonNull(profiles, "Los perfiles de softcaps no pueden ser nulos.");
        EnumMap<Gender, Map<Attribute, List<Integer>>> copy = new EnumMap<>(Gender.class);
        for (Gender gender : Gender.values()) {
            Map<Attribute, List<Integer>> source = Objects.requireNonNull(
                    profiles.get(gender), "Falta el perfil de " + gender.label() + "."
            );
            EnumMap<Attribute, List<Integer>> byAttribute = new EnumMap<>(Attribute.class);
            for (Attribute attribute : Attribute.values()) {
                List<Integer> softcaps = List.copyOf(source.getOrDefault(attribute, List.of()));
                int previous = 0;
                for (int softcap : softcaps) {
                    if (softcap <= previous || softcap > 75) {
                        throw new IllegalArgumentException("Softcaps inválidos para " + attribute.label() + ".");
                    }
                    previous = softcap;
                }
                byAttribute.put(attribute, softcaps);
            }
            copy.put(gender, Map.copyOf(byAttribute));
        }
        this.profiles = Map.copyOf(copy);
    }

    public List<Integer> softcaps(Gender gender, Attribute attribute) {
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        return profiles.get(gender).get(attribute);
    }

    public int ordinaryCap(Gender gender, Attribute attribute) {
        List<Integer> softcaps = softcaps(gender, attribute);
        return softcaps.isEmpty() ? 75 : softcaps.get(softcaps.size() - 1);
    }

    public static GenderSoftcapProfile canonical() {
        EnumMap<Gender, Map<Attribute, List<Integer>>> profiles = new EnumMap<>(Gender.class);
        profiles.put(Gender.HOMBRE, profile(
                List.of(15, 18, 25, 40, 75),
                List.of(20, 40),
                List.of(12, 75),
                List.of(25, 50),
                List.of(20, 70),
                List.of(30, 70),
                List.of(3, 13, 32, 40, 60),
                List.of(25, 50),
                List.of(11, 22, 33, 66, 75)
        ));
        profiles.put(Gender.MUJER, profile(
                List.of(13, 16, 25, 30, 75),
                List.of(15, 30),
                List.of(12, 75),
                List.of(21, 30),
                List.of(20, 70),
                List.of(30, 70),
                List.of(3, 13, 32, 40, 60),
                List.of(18, 21, 40),
                List.of(11, 22, 33, 66, 75)
        ));
        return new GenderSoftcapProfile(profiles);
    }

    private static Map<Attribute, List<Integer>> profile(
            List<Integer> vitality,
            List<Integer> endurance,
            List<Integer> adaptability,
            List<Integer> strength,
            List<Integer> dexterity,
            List<Integer> intelligence,
            List<Integer> faith,
            List<Integer> charisma,
            List<Integer> clairvoyance
    ) {
        EnumMap<Attribute, List<Integer>> profile = new EnumMap<>(Attribute.class);
        profile.put(Attribute.VITALIDAD, vitality);
        profile.put(Attribute.AGUANTE, endurance);
        profile.put(Attribute.ADAPTABILIDAD, adaptability);
        profile.put(Attribute.FUERZA, strength);
        profile.put(Attribute.DESTREZA, dexterity);
        profile.put(Attribute.INTELIGENCIA, intelligence);
        profile.put(Attribute.FE, faith);
        profile.put(Attribute.CARISMA, charisma);
        profile.put(Attribute.CLARIVIDENCIA, clairvoyance);
        return profile;
    }
}
