package domain.combat.coating;
import domain.combat.DamageType;
public record MercuryImpactEffect(DamageType type, int buildup, boolean cosmeticOnly) {
    public static MercuryImpactEffect none(boolean cosmetic) { return new MercuryImpactEffect(null, 0, cosmetic); }
}
