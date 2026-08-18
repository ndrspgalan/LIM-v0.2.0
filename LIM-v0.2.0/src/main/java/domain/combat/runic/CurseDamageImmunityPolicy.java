package domain.combat.runic;

import domain.ability.NullificationPolicy;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;
import domain.runic.EffectImmunity;

/** Inmunidad categórica al canal Maldición/Energía Maldita. */
public final class CurseDamageImmunityPolicy {
    public double netDamage(double ordinaryNetDamage, CharacterSheet target, EquipmentState equipment,
                            NullificationPolicy.SuppressionState suppression) {
        if (!Double.isFinite(ordinaryNetDamage) || ordinaryNetDamage < 0) throw new IllegalArgumentException("Daño maldito inválido.");
        return equipment != null && target != null
                && equipment.effectImmunities(target, suppression).contains(EffectImmunity.CURSE_DAMAGE) ? 0.0 : ordinaryNetDamage;
    }
}
