package domain.combat.natural;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.combat.StaggerPolicy;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;
import java.util.*;

/** Adapta un cuerpo natural al mismo contrato WeaponItem que ya consume la IA melee. */
public final class NaturalCombatWeaponFactory {
    private NaturalCombatWeaponFactory(){}

    public static WeaponItem create(NaturalCombatProfile profile, CharacterSheet sheet, double heightMeters){
        Objects.requireNonNull(profile,"El perfil natural no puede ser nulo.");
        Objects.requireNonNull(sheet,"La hoja no puede ser nula.");
        if(!Double.isFinite(heightMeters)||heightMeters<=0) throw new IllegalArgumentException("La altura debe ser positiva y finita.");
        double mass=profile.effectiveUnarmedMassKg();
        double reach=heightMeters*.5;
        double blunt=StaggerPolicy.meleeForceEquivalent(sheet.valueOf(Attribute.FUERZA),mass);
        List<String> stats=new ArrayList<>();
        stats.add("TIPO | Arma natural");
        stats.add("MASA OFENSIVA EQUIVALENTE | "+String.format(Locale.ROOT,"%.2f kg",mass).replace('.',','));
        stats.add("LETALIDAD / STAGGER BASE | FUERZA + masa ofensiva equivalente ("+String.format(Locale.ROOT,"%.2f",blunt).replace('.',',')+")");
        stats.add("ALCANCE (m) | ALTURA ×0,5 ("+String.format(Locale.ROOT,"%.2f",reach).replace('.',',')+")");
        profile.attackPresentation().forEach((a,p)->stats.add(a.name()+" | "+p));
        WeaponCombatPolicy policy=new WeaponCombatPolicy(profile.offensiveActions());
        return new WeaponItem(
                "DESARMADO — "+profile.label(),
                "Arma natural efectiva. Conserva la gramática táctica universal; la anatomía sólo sustituye la representación del ataque.",
                mass,new InventoryFootprint(1,1),reach,
                List.of(new WeaponMode("Cuerpo natural",new LethalityProfile(0,0,blunt))),
                List.of(),List.of(),stats,OptionalDouble.empty(),0,false,
                WeaponConfigurationPolicy.unarmed(),Set.of(WeaponTrait.UNARMED)
        ).withCombatPolicy(policy).withCombatActionsFor(WeaponActionMode.PRIMARY,profile.offensiveActions());
    }
}
