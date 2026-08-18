package domain.combat.coating;

import domain.bestiarium.BestiaryDescriptor;
import domain.bestiarium.ExistencePlane;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.combat.DamageType;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.ammunition.AmmunitionSource;
import domain.inventory.item.firearms.PneumaticFirearmItem;
import domain.inventory.item.misc.UtilityAction;
import domain.inventory.item.misc.UtilityObjectItem;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/** Recubrimiento de mercurio: admite armas compatibles y la munición .46 de plomo canónica, incluida su utilización como bala de Honda. */
public final class MercuryCoatingService {
    public static final String NARRATIVE_DESCRIPTION =
            "El dicho «acero para los humanos, plata para los monstruos» procede de una interpretación incompleta. " +
            "Las armas que originaron aquella reputación no debían su eficacia a la plata, sino a una película de mercurio " +
            "fijada sobre el acero mediante fricción. El brillo pálido de la superficie y la pérdida posterior del procedimiento " +
            "terminaron sustituyendo el material real por otro más reconocible dentro de la tradición popular. " +
            "Contra organismos del plano físico, esa película actúa por su propia toxicidad. Frente a entidades del Intersticio, " +
            "el mercurio ofrece continuidad a la energía maldita, aunque reconocer y aprovechar ese comportamiento exige Clarividencia suficiente. " +
            "Nunca fue plata para los monstruos. Era mercurio sobre acero.";

    private final Map<Object, MercuryCoatingState> coated = new IdentityHashMap<>();

    public boolean rub(UtilityObjectItem stone, Object target, int availableUnitsOrShots) {
        Objects.requireNonNull(stone); Objects.requireNonNull(target);
        if (!stone.actions().contains(UtilityAction.RUB) || stone.isDepleted() || !validTarget(target)) return false;
        boolean melee = target instanceof WeaponItem;
        coated.put(target, new MercuryCoatingState(melee ? 0 : Math.max(1, availableUnitsOrShots), melee));
        stone.consumeOne();
        return true;
    }

    public MercuryImpactEffect resolveImpact(Object source, BestiaryDescriptor target, CharacterSheet sheet) {
        Objects.requireNonNull(target); Objects.requireNonNull(sheet);
        MercuryCoatingState state = coated.get(source);
        if (state == null || !state.active()) return MercuryImpactEffect.none(false);
        if (target.plane() == ExistencePlane.PHYSICAL_PLANE) return new MercuryImpactEffect(DamageType.POISON, 100, false);
        if (sheet.valueOf(Attribute.CLARIVIDENCIA) >= 11) return new MercuryImpactEffect(DamageType.CURSE, 100, false);
        return MercuryImpactEffect.none(true);
    }

    public void consumeApplication(Object source) {
        MercuryCoatingState state = coated.get(source);
        if (state == null || state.untilBluntZero()) return;
        MercuryCoatingState next = state.consumeOne();
        if (next.active()) coated.put(source, next); else coated.remove(source);
    }
    public boolean isCoated(Object source) { return coated.containsKey(source); }
    public void clear(Object source) { coated.remove(source); }

    private boolean validTarget(Object target) {
        if (target instanceof WeaponItem weapon) return weapon.canBeCoatedWithMucusTear();
        String pkg = target.getClass().getPackageName();
        if (pkg.contains("throwingWeapons")) return true;
        if (target instanceof PneumaticFirearmItem) return true;
        if (target instanceof AmmunitionSource source) {
            var descriptor = source.ammunitionDescriptor();
            return descriptor.family() == domain.inventory.item.ammunition.AmmunitionFamily.CARTRIDGE
                    && ".46".equals(descriptor.caliber())
                    && "Plomo".equals(descriptor.material());
        }
        return false;
    }
}
