package domain.ability;

import java.util.Objects;
import java.util.Set;

/** Despachador canónico de Z/X respaldado por un registro de mecánicas tipadas. */
public final class MasteryExecutionDispatcher {
    private static final MasteryMechanicRegistry REGISTRY = new MasteryMechanicRegistry();
    private MasteryExecutionDispatcher() {}

    public static MasteryExecutionResult executeActive(MasteryManifestation manifestation, MasteryExecutionContext context) {
        Objects.requireNonNull(manifestation); Objects.requireNonNull(context);
        return REGISTRY.execute(manifestation, context);
    }

    public static MasteryExecutionResult toggleSustained(MasteryManifestation manifestation, MasteryExecutionContext context,
                                                         Set<String> sustainedActive) {
        Objects.requireNonNull(manifestation); Objects.requireNonNull(context); Objects.requireNonNull(sustainedActive);
        String source=MasteryManifestationKey.of(manifestation).qualified();
        if(sustainedActive.remove(source)) {
            context.effects().removeBySource(source);
            return MasteryExecutionResult.of(MasteryExecutionStatus.FINISHED,source,
                    manifestation.name()+" queda desactivada.",java.util.Map.of(),java.util.List.of());
        }
        if (manifestation.familyId() == MasteryId.EXPLOSION_CINETICA) {
            java.util.List<String> conflicting=sustainedActive.stream().filter(x->x.startsWith(MasteryId.EXPLOSION_CINETICA.name()+":" )).toList();
            for(String other:conflicting){sustainedActive.remove(other);context.effects().removeBySource(other);}
        }
        MasteryExecutionResult result=REGISTRY.execute(manifestation,context);
        if(result.successful()) sustainedActive.add(source);
        return result;
    }

    /** Adaptadores temporales para consumidores /. */
    public static MasteryActionResult executeActive(MasteryManifestation m, MasteryRuntimeContext c,
                                                    Set<String> sustained, Set<String> effects) {
        return executeActive(m,MasteryExecutionContext.fromRuntimeContext(c)).toActionResult();
    }
    public static MasteryActionResult toggleSustained(MasteryManifestation m, MasteryRuntimeContext c,
                                                      Set<String> sustained) {
        return toggleSustained(m,MasteryExecutionContext.fromRuntimeContext(c),sustained).toActionResult();
    }
    public static MasteryMechanicRegistry registry(){return REGISTRY;}
}
