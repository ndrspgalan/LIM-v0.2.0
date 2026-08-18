package domain.ability;

import domain.ability.event.UnarmedImpactEvent;
import domain.character.sheet.CharacterSheet;
import domain.inventory.item.armor.ArmorMaterial;
import java.util.Objects;

/** DESARMADO : PULSIÓN no amplifica B; las pasivas de contacto siguen resolviéndose por el bus. */
public final class UnarmedMasteryImpactResolver {
    public UnarmedImpactEvent resolve(CharacterSheet sheet, MasteryEventBus bus, MasteryEffectRegistry effects,
                                      ArmorMaterial contactedMaterial, boolean intersticeTarget,
                                      int piercing, int slashing, int blunt) {
        Objects.requireNonNull(sheet); Objects.requireNonNull(bus); Objects.requireNonNull(effects);
        UnarmedImpactEvent event = new UnarmedImpactEvent(
                sheet.valueOf(domain.character.sheet.Attribute.VITALIDAD),
                sheet.valueOf(domain.character.sheet.Attribute.ADAPTABILIDAD),
                contactedMaterial, intersticeTarget, piercing, slashing, blunt);
        bus.publish(event);
        return event;
    }
}
