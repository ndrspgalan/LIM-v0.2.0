package domain.inventory.item;

import domain.character.sheet.Attribute;
import domain.character.Gender;
import domain.combat.UnarmedMassPolicy;
import domain.combat.StaggerPolicy;
import domain.character.sheet.CharacterSheet;
import domain.inventory.InventoryFootprint;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

/** Construye la ficha efectiva de DESARMADO a partir de la antropometría y FUERZA del actor. */
public final class UnarmedWeaponFactory {
    private UnarmedWeaponFactory() {}

    /** Fallback técnico para consumidores que solo necesitan el repertorio de acciones. */
    public static WeaponItem create() {
        return create(1.50, 0, 0, Gender.HOMBRE);
    }

    public static WeaponItem create(CharacterSheet sheet, double heightMeters) {
        return create(sheet, heightMeters, Gender.HOMBRE);
    }

    public static WeaponItem create(CharacterSheet sheet, double heightMeters, Gender gender) {
        if (sheet == null) throw new IllegalArgumentException("La hoja no puede ser nula.");
        return create(heightMeters, sheet.valueOf(Attribute.FUERZA), sheet.valueOf(Attribute.DESTREZA), gender);
    }

    public static WeaponItem create(double heightMeters, int strength) { return create(heightMeters, strength, 0, Gender.HOMBRE); }
    public static WeaponItem create(double heightMeters, int strength, Gender gender) { return create(heightMeters,strength,0,gender); }

    public static WeaponItem create(double heightMeters, int strength, int dexterity, Gender gender) {
        if (!Double.isFinite(heightMeters) || heightMeters <= 0) {
            throw new IllegalArgumentException("La altura debe ser positiva y finita.");
        }
        if (strength < 0 || dexterity < 0) throw new IllegalArgumentException("FUERZA/DESTREZA no pueden ser negativas.");
        double reach = heightMeters * 0.5;
        double offensiveMass = UnarmedMassPolicy.equivalentMassKg(gender);
        double blunt = StaggerPolicy.meleeForceEquivalent(strength, offensiveMass);
        return new WeaponItem(
                "DESARMADO",
                "El cuerpo entero se convierte en arma: la guardia adelantada puede invertirse entre diestra y zurda, los puños encadenan directo, swing y gancho, y las piernas resuelven el tornado 360 y el back kick.",
                offensiveMass, new InventoryFootprint(1, 1), reach,
                List.of(new WeaponMode("Cuerpo", new LethalityProfile(0, 0, blunt))),
                List.of(), List.of(),
                List.of(
                        "TIPO | Arma natural",
                        "MASA OFENSIVA EQUIVALENTE | " + String.format(java.util.Locale.ROOT, "%.1f kg", offensiveMass).replace('.', ','),
                        "LETALIDAD | 0 perforante - 0 cortante - FUERZA + masa ofensiva contundente (" + String.format(java.util.Locale.ROOT, "%.1f", blunt).replace('.', ',') + ")",
                        "ALCANCE (m) | ALTURA ×0,5 (" + String.format(java.util.Locale.ROOT, "%.2f", reach).replace('.', ',') + ")",
                        "CONFIGURACIÓN | Dos manos lógicas · PRIMARY diestra adelantada / ALTERNATIVE zurda adelantada · resolución RIGHT_HAND",
                        "ATAQUE LIGERO | Directo → swing → gancho",
                        "ATAQUE FUERTE | " + (dexterity >= 50 ? "Tornado kick 360° (DES ≥50)" : "Patada baja al muslo; tornado kick 360° desde DES 50"),
                        "BLOQUEO | Guardia facial mantenida · HEAD +50% cobertura · protección contundente = AGUANTE",
                        "ATAQUE CON SALTO | Ataque aéreo desarmado",
                        "GOLPE DESESTABILIZADOR | " + (dexterity >= 35 ? "Back kick (DES ≥35)" : "Patada frontal; back kick desde DES 35")
                ), OptionalDouble.empty(), 0, false,
                WeaponConfigurationPolicy.unarmed(), Set.of(WeaponTrait.UNARMED)
        ).withCombatPolicy(WeaponCombatPolicy.unarmed())
          .withOffensiveMovesetFor(WeaponActionMode.PRIMARY, domain.combat.moveset.UnarmedMovesetCatalog.rightLead(dexterity))
          .withOffensiveMovesetFor(WeaponActionMode.ALTERNATIVE, domain.combat.moveset.UnarmedMovesetCatalog.leftLead(dexterity));
    }
}
