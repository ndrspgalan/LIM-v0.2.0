package domain.ability;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.item.armor.ArmorMaterial;

import java.util.Objects;

/** Resolución de la maestría evolutiva pasiva TRIBOGÉNESIS. */
public final class TribogenesisPolicy {
    public enum EnvironmentalUse { IGNITE_FIRE, IGNITE_AMADOU, REPLACE_FLINT, BURST_LOCK }

    public int burnDamage(CharacterSheet sheet, boolean unarmedAttack, ArmorMaterial contactedMaterial) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(contactedMaterial, "El material no puede ser nulo.");
        if (!unarmedAttack || !supportsTribogenesis(contactedMaterial)) return 0;
        return EvolutiveIntensityPolicy.intensity(sheet.valueOf(Attribute.ADAPTABILIDAD));
    }

    public boolean supportsTribogenesis(ArmorMaterial material) {
        Objects.requireNonNull(material, "El material no puede ser nulo.");
        return material == ArmorMaterial.BRONZE
                || material == ArmorMaterial.STEEL
                || material == ArmorMaterial.ELECTROMECHANICAL_COMPOSITE;
    }

    public boolean canPerform(CharacterSheet sheet, EnvironmentalUse use) {
        Objects.requireNonNull(use, "El uso ambiental no puede ser nulo.");
        return sheet.valueOf(Attribute.ADAPTABILIDAD) >= EvolutiveIntensityPolicy.MINIMUM_ATTRIBUTE;
    }
}
