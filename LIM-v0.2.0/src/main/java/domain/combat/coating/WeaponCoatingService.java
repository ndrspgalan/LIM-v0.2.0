package domain.combat.coating;

import domain.character.sheet.CharacterSheet;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.misc.MucusTearItem;

import java.util.Objects;

/** Aplica una única Lágrima de Mucus Blanco a un arma cortante. */
public final class WeaponCoatingService {
    public WeaponCoatingResult applyCurse(CharacterSheet sheet, MucusTearItem tear, WeaponItem weapon) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        Objects.requireNonNull(tear, "La Lágrima de Mucus no puede ser nula.");
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        if (tear.currentUses() < 1
                || !weapon.canBeCoatedWithMucusTear()) {
            return WeaponCoatingResult.rejected();
        }
        tear.removeUnits(1);
        if (!domain.knowledge.PropertyKnowledgePolicy.requirementMet(sheet, domain.character.sheet.Attribute.CLARIVIDENCIA, domain.inventory.item.misc.MucusCrystalItem.TRANSPOSITION_CLARIVOYANCE_THRESHOLD)) {
            return new WeaponCoatingResult(true, 1, null); // Aplicación visible, efecto meramente cosmético.
        }
        WeaponCoating coating = WeaponCoating.curse();
        weapon.applyCoating(coating);
        return new WeaponCoatingResult(true, 1, coating);
    }

}
