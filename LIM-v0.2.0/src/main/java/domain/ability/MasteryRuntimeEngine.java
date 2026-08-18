package domain.ability;

import domain.combat.ai.perception.InvisibilityInteractionPolicy;

import java.util.*;

/** Ejecuta pulsos sostenidos, duración e interrupciones sobre efectos ya iniciados. */
public final class MasteryRuntimeEngine {
    public List<MasteryExecutionResult> tick(MasteryExecutionContext context,double realSeconds) {
        if(!Double.isFinite(realSeconds)||realSeconds<0)throw new IllegalArgumentException("Tick inválido.");
        List<MasteryExecutionResult> results=new ArrayList<>();
        for(MasteryEffect effect:context.effects().active()) {
            double paPerSecond=effect.magnitudes().getOrDefault("PA_PER_SECOND",0.0);
            if(paPerSecond>0) {
                double cost=SpiritInfatigablePolicy.globalStaminaCost(paPerSecond*realSeconds, context.world().spiritInfatigableActive(), context.hostileEncounter().isActive());
                boolean overdrive=context.effects().contains("TRANSMUTACION:OVERDRIVE:"+context.actorId());
                OverdrivePolicy.ImmediateActionPayment payment=OverdrivePolicy.paySustainedPulse(
                        context.resources().currentStamina(),context.resources().currentHealth(),cost,overdrive,true);
                if(!payment.completed()) {
                    context.effects().remove(effect.id());
                    results.add(MasteryExecutionResult.of(MasteryExecutionStatus.INTERRUPTED,effect.sourceManifestationId(),
                            "La maestría sostenida se interrumpe por falta de recursos.",Map.of(),List.of()));
                    continue;
                }
                context.resources().setCurrentStamina(payment.staminaAfter());
                context.resources().setCurrentHealth(payment.healthAfter());
                results.add(MasteryExecutionResult.of(MasteryExecutionStatus.RESOURCE_CONSUMED,effect.sourceManifestationId(),
                        "Pulso sostenido resuelto.",Map.of("PA",-payment.staminaSpent(),"PV",-payment.healthSpent()),List.of()));
            }
            if(effect.id().startsWith("THETA:")) {
                ThetaRegenerationPolicy.ThetaTick tick=ThetaRegenerationPolicy.tick(
                        context.resources().currentHealth(),context.resources().maximumHealth(),
                        context.resources().currentStamina(),context.resources().maximumStamina(),60.0,realSeconds,
                        1.0,context.effects().contains("TRANSMUTACION:OVERCLOCK:"+context.actorId())?4.0:1.0);
                context.resources().setCurrentHealth(tick.currentHealth());context.resources().setCurrentStamina(tick.currentStamina());
                if(tick.complete()) {
                    context.effects().remove(effect.id());
                    results.add(MasteryExecutionResult.of(MasteryExecutionStatus.FINISHED,effect.sourceManifestationId(),
                            "REGENERACIÓN THETA finaliza con PV restaurados y PA a cero.",Map.of(),List.of()));
                }
            }
        }
        context.effects().tick(realSeconds);
        return List.copyOf(results);
    }

    /** resuelve exclusivamente si una interacción rompe INVISIBILIDAD. */
    public boolean applyInvisibilityInteraction(MasteryExecutionContext context, InvisibilityInteractionPolicy.Interaction interaction) {
        Objects.requireNonNull(context); Objects.requireNonNull(interaction);
        String id="INVISIBILIDAD:INVISIBILIDAD:"+context.actorId();
        if(!context.effects().contains(id)) return false;
        if(new InvisibilityInteractionPolicy().preserves(interaction)) return false;
        context.effects().remove(id);
        return true;
    }

    public List<MasteryExecutionResult> interruptByAction(MasteryExecutionContext context,String reason) {
        List<MasteryExecutionResult> results=new ArrayList<>();
        for(MasteryEffect effect:context.effects().active()) {
            if(effect.magnitudes().getOrDefault("INTERRUPT_ON_ACTION",0.0)>0) {
                context.effects().remove(effect.id());
                results.add(MasteryExecutionResult.of(MasteryExecutionStatus.INTERRUPTED,effect.sourceManifestationId(),
                        reason==null?"La acción interrumpe la maestría.":reason,Map.of(),List.of()));
            }
        }
        return List.copyOf(results);
    }
}
