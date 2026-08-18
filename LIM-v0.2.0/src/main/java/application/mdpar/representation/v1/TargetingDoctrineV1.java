package application.mdpar.representation.v1;

/** Garantías de inmersión para la IA hostil ordinaria de LIM. */
public record TargetingDoctrineV1(
        boolean actorTargetingSupported,
        boolean positionTargetingSupported,
        boolean areaTargetingSupported,
        boolean blindFireSchemaSupported,
        boolean blindThrowSchemaSupported,
        boolean predictiveInterceptAimingAllowed,
        boolean postLaunchTrajectoryCorrectionAllowed,
        String aimingBasis
) {
    public TargetingDoctrineV1 { if(aimingBasis==null||aimingBasis.isBlank())throw new IllegalArgumentException("Base de puntería obligatoria."); }
    public static TargetingDoctrineV1 canonical(){return new TargetingDoctrineV1(true,true,true,true,true,false,false,
            "OBSERVED_OR_LAST_KNOWN_POSITION_AT_COMMIT_TIME");}
}
