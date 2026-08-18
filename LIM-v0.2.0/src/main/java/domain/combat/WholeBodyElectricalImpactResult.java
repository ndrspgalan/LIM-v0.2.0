package domain.combat;

/** una descarga puede entrar por una pieza localizada y distribuirse por todo el organismo. */
public record WholeBodyElectricalImpactResult(
        NonConventionalImpactResult head,
        NonConventionalImpactResult body,
        boolean fullBodyGroundingPath
) {}
