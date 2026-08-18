package domain.combat;

import domain.inventory.item.armor.ArmorProtectionProfile;

/** resolución de la capa exterior de un escudo dedicado. No usa barra de vida ni irreparabilidad. */
public final class ShieldImpactResolver {
    public ShieldProfileImpactResult resolve(PhysicalDamage incoming,ArmorProtectionProfile current){
        if(incoming==null||current==null) throw new IllegalArgumentException("Impacto y perfil son obligatorios.");
        PhysicalDamage residual=new PhysicalDamage(
                ArmorMitigationPolicy.transmitted(incoming.piercing(),current.piercing()),
                ArmorMitigationPolicy.transmitted(incoming.slashing(),current.slashing()),
                ArmorMitigationPolicy.transmitted(incoming.blunt(),current.blunt()));
        double m=ShieldCombatPolicy.PAVESINA_V881.wearMultiplier();
        double pLoss=incoming.piercing()>current.piercing()?Math.min(current.piercing(),m):0;
        double cLoss=incoming.slashing()>current.slashing()?Math.min(current.slashing(),m):0;
        double bLoss=incoming.blunt()>current.blunt()?Math.min(current.blunt(),m):0;
        ArmorProtectionProfile remaining=new ArmorProtectionProfile(
                Math.max(0,current.piercing()-pLoss),Math.max(0,current.slashing()-cLoss),Math.max(0,current.blunt()-bLoss));
        return new ShieldProfileImpactResult(residual,remaining,new ArmorProfileWearResult(pLoss,cLoss,bLoss),StaggerPolicy.resolve(incoming.blunt()));
    }
}
