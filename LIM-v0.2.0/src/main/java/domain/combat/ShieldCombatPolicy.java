package domain.combat;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponItem;

/** protección, letalidad y ergonomía de escudos dedicados. */
public final class ShieldCombatPolicy {
    public static final ShieldSpecification PAVESINA_V881 = new ShieldSpecification(
            "Pavesina Cementada de Asalto V881", 0.55, 0.42, 0.005, 8.8, 3, 3,
            new domain.inventory.item.armor.ArmorProtectionProfile(100,100,100), 0.15, 0.5, true);
    private ShieldCombatPolicy(){}

    public static LethalityProfile lightAttackLethality(CharacterSheet sheet, WeaponItem shield){
        if(sheet==null||shield==null) throw new IllegalArgumentException("Hoja y escudo son obligatorios.");
        return new LethalityProfile(0,0,StrengthMassBluntPolicy.blunt(sheet.valueOf(Attribute.FUERZA), shield.weightKg()));
    }

    /** ERGONOMÍA INTRINCADA: la carga monomanual se reparte entre FUERZA y AGUANTE. */
    public static boolean canWieldIntricateOneHanded(CharacterSheet sheet, WeaponItem shield){
        if(sheet==null||shield==null) return false;
        double demand=shield.weightKg()*10.0;
        return sheet.valueOf(Attribute.FUERZA)+sheet.valueOf(Attribute.AGUANTE)+1e-9>=demand;
    }
}
