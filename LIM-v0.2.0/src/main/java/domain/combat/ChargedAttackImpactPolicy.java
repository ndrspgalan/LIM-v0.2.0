package domain.combat;

import domain.inventory.item.*;
import java.util.Objects;
import domain.ability.CharacterMasteryCollection;
import domain.character.sheet.CharacterSheet;

/** El cargado ordinario amplifica contundencia;  exceptúa al Espadón dentro de DE_ROTOR. */
public final class ChargedAttackImpactPolicy {
    public static final double STANDARD_BLUNT_MULTIPLIER=1.30;
    public static final double AURA_PULSION_BLUNT_MULTIPLIER=1.35;
    public static final double THERMO_MECHANICAL_DRAW_MULTIPLIER=1.20;

    public PhysicalDamage resolve(WeaponItem w,WeaponMode m){return resolve(w,m,false);}
    public PhysicalDamage resolve(WeaponItem w,WeaponMode m,CharacterMasteryCollection masteries,CharacterSheet sheet){
        Objects.requireNonNull(masteries);Objects.requireNonNull(sheet);
        return resolve(w,m,masteries.isPassiveActive("AURA DE PULSIÓN",sheet));
    }
    public PhysicalDamage resolve(WeaponItem w,WeaponMode m,boolean aura){
        Objects.requireNonNull(w);Objects.requireNonNull(m);
        if(w.hasTrait(WeaponTrait.DE_ROTOR) && !w.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) throw new IllegalArgumentException("DE_ROTOR ordinario no dispone de ataque cargado.");
        PhysicalDamage base=MeleeWeaponImpactPolicy.baseImpact(w,m);
        double x=w.hasTrait(WeaponTrait.THERMO_MECHANICAL)
                ? THERMO_MECHANICAL_DRAW_MULTIPLIER
                : (aura?AURA_PULSION_BLUNT_MULTIPLIER:STANDARD_BLUNT_MULTIPLIER);
        return new PhysicalDamage(base.piercing(),base.slashing(),base.blunt()*x);
    }
}
