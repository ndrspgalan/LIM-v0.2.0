package domain.combat;

import domain.audio.ImpactSoundCue;
import domain.audio.ImpactSoundResolver;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;

import java.util.Objects;

/**
 * Punto único de resolución para impactos armados: aplica desgaste y produce
 * la señal sonora correspondiente al retroceso del mismo impacto.
 */
public final class WeaponImpactResolver {
    private final WeaponDurabilityResolver durabilityResolver;
    private final ImpactSoundResolver soundResolver;

    public WeaponImpactResolver() {
        this(new WeaponDurabilityResolver(), new ImpactSoundResolver());
    }

    public WeaponImpactResolver(
            WeaponDurabilityResolver durabilityResolver,
            ImpactSoundResolver soundResolver
    ) {
        this.durabilityResolver = Objects.requireNonNull(durabilityResolver);
        this.soundResolver = Objects.requireNonNull(soundResolver);
    }

    public WeaponImpactResult resolve(
            WeaponItem weapon,
            WeaponMode mode,
            double finalInflictedDamage,
            RecoilType recoilType,
            double relativeRecoilIntensity
    ) {
        if (finalInflictedDamage < 0) throw new IllegalArgumentException("El daño final no puede ser negativo.");
        boolean durabilityReduced = false; // +: el desgaste armado se resuelve al colisionar con capa HEAVY.
        ImpactSoundCue soundCue = soundResolver.resolve(recoilType, relativeRecoilIntensity);
        return new WeaponImpactResult(durabilityReduced, soundCue);
    }
}
