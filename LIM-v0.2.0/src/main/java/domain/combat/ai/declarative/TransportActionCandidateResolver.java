package domain.combat.ai.declarative;

import domain.bestiarium.physical_plane.ferae.FeraeProfile;
import domain.bestiarium.physical_plane.ferae.equine.*;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.combat.ai.transport.EncounterTransportSnapshot;
import domain.combat.stamina.StaminaRegenerationDelayPolicy;
import domain.character.sheet.DerivedStatisticsCalculator;
import domain.inventory.item.PersonalTransportItemUsePolicy;
import domain.inventory.logistics.*;
import domain.movement.LocomotionStaminaPolicy;
import java.util.*;
import java.util.OptionalDouble;

/**  — enumera transporte/movimiento montado sin decidir entre alternativas. */
public final class TransportActionCandidateResolver {
 private final PersonalTransportItemUsePolicy itemUse=new PersonalTransportItemUsePolicy();
 private final LocomotionStaminaPolicy stamina=new LocomotionStaminaPolicy();
 private final StaminaRegenerationDelayPolicy regenDelay=new StaminaRegenerationDelayPolicy();
 private final DerivedStatisticsCalculator stats=new DerivedStatisticsCalculator();

 public List<TransportFact> facts(TransportDecisionState state){
  List<TransportFact> out=new ArrayList<>();
  for(var t:state.transports()){
   Optional<FeraeProfile> mount=mountProfile(state,t);
   OptionalDouble current=OptionalDouble.empty(),max=OptionalDouble.empty(),delay=OptionalDouble.empty(),full=OptionalDouble.empty(),fuel=OptionalDouble.empty();
   if(mount.isPresent()){
    double m=mount.get().attributes().valueOf(Attribute.AGUANTE); double cur=state.mountCurrentStamina().getOrDefault(t.transportId(),m); double load=state.mountLoadKg().getOrDefault(t.transportId(),0d); double maxLoad=Math.max(1,m);
    current=OptionalDouble.of(Math.min(m,Math.max(0,cur))); max=OptionalDouble.of(m); delay=OptionalDouble.of(regenDelay.naturalDelaySeconds(mount.get().attributes())); full=OptionalDouble.of(stats.staminaRecovery(m,load,maxLoad).fullRecoverySeconds());
   }
   if(t.type().family()==PersonalTransportFamily.MOTORCYCLE && state.motorcycleFuel().isPresent()) fuel=OptionalDouble.of(state.motorcycleFuel().get().normalLiters()+state.motorcycleFuel().get().reserveLiters());
   out.add(new TransportFact(t.transportId(),t.type(),t.distanceMeters(),t.operational(),t.available(),t.currentDriverId(),t.ownerActorId(),mount.map(FeraeProfile::species),current,max,delay,full,fuel));
  }
  return List.copyOf(out);
 }

 public List<TransportActionCandidate> resolve(CombatActorDecisionState actor, TransportDecisionState state){
  List<TransportActionCandidate> out=new ArrayList<>();
  for(var t:state.transports()){
   boolean compatible=state.activeItem().isEmpty()||itemUse.canUseAsDriver(state.activeItem().get(),t.type());
   boolean mounted=state.mountedTransportId().filter(t.transportId()::equals).isPresent();
   if(!mounted && t.available()){
    out.add(c(TransportActionType.APPROACH,t,TransportResourceOwner.ACTOR,OptionalDouble.of(stamina.runningCostPerSecond(actor.gender(),actor.sheet(),actor.maximumStamina())),OptionalDouble.empty(),0,compatible,"Acercarse conserva la locomoción del actor; propiedad ajena no crea una prohibición física."));
    out.add(c(TransportActionType.MOUNT_DRIVER,t,owner(t.type()),OptionalDouble.empty(),OptionalDouble.empty(),0,compatible,"Montar requiere transporte operativo, libre y físicamente accesible; la compatibilidad del objeto activo se declara aparte."));
    if(t.type().supportsCopilot()) out.add(c(TransportActionType.MOUNT_PASSENGER,t,owner(t.type()),OptionalDouble.empty(),OptionalDouble.empty(),0,true,"El transporte admite asiento de copiloto."));
   }
   if(mounted){
    out.add(c(TransportActionType.DISMOUNT,t,owner(t.type()),OptionalDouble.empty(),OptionalDouble.empty(),0,true,"Desmontar devuelve la locomoción al actor."));
    addMove(out,actor,state,t,TransportActionType.MOVE_WALK,t.type().walkKmh(),compatible);
    addMove(out,actor,state,t,TransportActionType.MOVE_TROT,t.type().trotKmh(),compatible);
    addMove(out,actor,state,t,TransportActionType.MOVE_MAXIMUM,t.type().maximumKmh(),compatible);
   }
  }
  return List.copyOf(out);
 }
 private void addMove(List<TransportActionCandidate> out,CombatActorDecisionState actor,TransportDecisionState state,EncounterTransportSnapshot t,TransportActionType a,double speed,boolean compatible){
  OptionalDouble pa=OptionalDouble.empty(),fuel=OptionalDouble.empty(); TransportResourceOwner o=owner(t.type());
  if(o==TransportResourceOwner.ACTOR) pa=OptionalDouble.of(stamina.runningCostPerSecond(actor.gender(),actor.sheet(),actor.maximumStamina()));
  else if(o==TransportResourceOwner.MOUNT){var p=mountProfile(state,t).orElseThrow(); double max=p.attributes().valueOf(Attribute.AGUANTE); Gender g=p.sex()==domain.bestiarium.physical_plane.ferae.FeraeSex.MACHO?Gender.HOMBRE:Gender.MUJER; pa=OptionalDouble.of(stamina.exertionCostPerSecond(g,p.attributes(),max));}
  else fuel=OptionalDouble.of(MotorcycleFuelState.CONSUMPTION_L_PER_100_KM/100.0);
  out.add(c(a,t,o,pa,fuel,speed,compatible,o==TransportResourceOwner.MOUNT?"La montura consume sus propios PA; el jinete no gasta PA locomotores.":o==TransportResourceOwner.ACTOR?"La bicicleta reutiliza la curva porcentual de PA de correr.":"La motocicleta consume combustible, no PA locomotores del conductor."));
 }
 private Optional<FeraeProfile> mountProfile(TransportDecisionState state,EncounterTransportSnapshot t){
  if(t.type().family()!=PersonalTransportFamily.HORSE)return Optional.empty();
  var declared=state.mountSpeciesByTransportId().get(t.transportId());
  if(declared!=null)return Optional.of(EquineFeraeCatalog.profile(declared));
  var v=EquineMountVariant.of(t.type()); return Optional.of(EquineFeraeCatalog.stallion(v));
 }
 private static TransportResourceOwner owner(PersonalTransportType t){return switch(t.family()){case BICYCLE->TransportResourceOwner.ACTOR;case HORSE->TransportResourceOwner.MOUNT;case MOTORCYCLE->TransportResourceOwner.FUEL_SYSTEM;};}
 private static TransportActionCandidate c(TransportActionType a,EncounterTransportSnapshot t,TransportResourceOwner o,OptionalDouble pa,OptionalDouble fuel,double speed,boolean compat,String r){return new TransportActionCandidate(a,t.transportId(),t.type(),o,pa,fuel,speed,compat,r);}
}
