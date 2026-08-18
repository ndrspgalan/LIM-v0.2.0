package domain.combat.ai.declarative;
import domain.movement.*;
import java.util.*;
/**  — enumera locomoción legal con metros/segundo y PA; nunca elige una alternativa. */
public final class LocomotionActionCandidateResolver {
 private final LocomotionStaminaPolicy stamina=new LocomotionStaminaPolicy(); private final LocomotionDistancePolicy distance=new LocomotionDistancePolicy(); private final VerticalJumpPolicy verticalJump=new VerticalJumpPolicy();
 public List<LocomotionActionCandidate> resolve(CombatActorDecisionState actor,LocomotionProfile profile,OptionalDouble horizontalJumpDistanceMeters){
  Objects.requireNonNull(actor);Objects.requireNonNull(profile);Objects.requireNonNull(horizontalJumpDistanceMeters); List<LocomotionActionCandidate> out=new ArrayList<>();
  for(var mode:profile.allowedModes()) switch(mode){
   case RUNNING->out.add(c(actor,LocomotionActionCandidate.Kind.RUN,mode,stamina.runningCostPerSecond(actor.gender(),actor.sheet(),actor.maximumStamina()),"Correr: velocidad antropométrica; coste por AGUANTE."));
   case TROTTING->out.add(c(actor,LocomotionActionCandidate.Kind.TROT,mode,0,"Trote antropométrico sin coste locomotor."));
   case WALKING->out.add(c(actor,LocomotionActionCandidate.Kind.WALK,mode,0,"Marcha antropométrica sin coste locomotor."));
   case CROUCH_WALKING->out.add(c(actor,LocomotionActionCandidate.Kind.CROUCH_WALK,mode,0,"Desplazamiento agachado antropométrico."));
   case CRAWLING->out.add(c(actor,LocomotionActionCandidate.Kind.CRAWL,mode,0,"Gateo antropométrico."));
   case CLIMBING->out.add(c(actor,LocomotionActionCandidate.Kind.CLIMB,mode,1.0,"Escalar recorre lo mismo que agachado y consume 1 PA/s fijo."));
   case SWIMMING->out.add(c(actor,LocomotionActionCandidate.Kind.SWIM,mode,0,"Nado normal abriendo el agua: velocidad de marcha."));
   case FAST_SWIMMING->out.add(c(actor,LocomotionActionCandidate.Kind.FAST_SWIM,mode,stamina.fastSwimmingCostPerSecond(actor.gender(),actor.sheet(),actor.maximumStamina()),"Brazadas: velocidad y coste de correr."));
   case DIVING->out.add(c(actor,LocomotionActionCandidate.Kind.DIVE,mode,1.0,"Buceo: velocidad de trote, 1 PA/s; PA=0 implica ahogamiento."));
  }
  out.add(new LocomotionActionCandidate(LocomotionActionCandidate.Kind.JUMP_VERTICAL,0,0,OptionalDouble.of(stamina.verticalJumpCost()),OptionalDouble.of(verticalJump.heightMeters(actor.gender(),actor.sheet(),actor.heightMeters())),OptionalDouble.empty(),"Salto vertical: 1 PA fijo."));
  if(horizontalJumpDistanceMeters.isPresent()) out.add(new LocomotionActionCandidate(LocomotionActionCandidate.Kind.JUMP_HORIZONTAL,0,0,OptionalDouble.of(stamina.horizontalJumpCost(actor.gender(),actor.sheet(),actor.maximumStamina())),OptionalDouble.empty(),horizontalJumpDistanceMeters,"Salto horizontal canónico."));
  return List.copyOf(out);
 }
 private LocomotionActionCandidate c(CombatActorDecisionState a,LocomotionActionCandidate.Kind k,LocomotionMode m,double cost,String relation){return new LocomotionActionCandidate(k,cost,distance.metersPerSecond(m,a.gender(),a.heightMeters()),OptionalDouble.empty(),OptionalDouble.empty(),OptionalDouble.empty(),relation);}
}
