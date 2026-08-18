package domain.ability;

@FunctionalInterface
public interface MasteryMechanic {
    MasteryExecutionResult execute(MasteryManifestation manifestation, MasteryExecutionContext context);
}
