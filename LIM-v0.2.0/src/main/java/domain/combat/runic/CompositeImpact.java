package domain.combat.runic;

import domain.combat.PhysicalDamage;
import domain.combat.StaggerResult;
import java.util.Objects;

public record CompositeImpact(
        PhysicalDamage physicalNet,
        double curseRaw,
        double curseNet,
        double physicalRecoilUnits,
        double mentalRecoilUnits,
        StaggerResult accumulatedStagger
) {
    public CompositeImpact {
        Objects.requireNonNull(physicalNet); Objects.requireNonNull(accumulatedStagger);
        if (curseRaw < 0 || curseNet < 0 || physicalRecoilUnits < 0 || mentalRecoilUnits < 0) {
            throw new IllegalArgumentException("Los canales del impacto no pueden ser negativos.");
        }
    }
    public double totalNetDamage() {
        return physicalNet.piercing() + physicalNet.slashing() + physicalNet.blunt() + curseNet;
    }
}
