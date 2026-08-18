package domain.combat;

import domain.inventory.item.armor.ArmorProtectionProfile;

/** la pavesina metálica es reparable con acero; la reparación completa restaura el gradiente cementado. */
public final class ShieldRepairPolicy {
    private ShieldRepairPolicy(){}
    public static ArmorProtectionProfile repairPavesina(ArmorProtectionProfile current, int steelUnits){
        if(current==null) throw new IllegalArgumentException("El perfil no puede ser nulo.");
        if(steelUnits<=0) return current;
        return ShieldCombatPolicy.PAVESINA_V881.protection();
    }
    public static boolean repairableWithSteel(){return ShieldCombatPolicy.PAVESINA_V881.repairableWithSteel();}
}
