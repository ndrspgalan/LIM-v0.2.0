package domain.ability;

import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.social.RelationshipType;

import java.util.*;

/** Registro por identificador estable. Jugador e IA ejecutan exactamente las mismas mecánicas. */
public final class MasteryMechanicRegistry {
    private final Map<MasteryManifestationKey, MasteryMechanic> mechanics = new HashMap<>();

    public MasteryMechanicRegistry() { registerCanonical(); }

    public MasteryExecutionResult execute(MasteryManifestation manifestation, MasteryExecutionContext context) {
        MasteryManifestationKey key=MasteryManifestationKey.of(manifestation);
        MasteryMechanic mechanic=mechanics.get(key);
        if(mechanic==null) return MasteryExecutionResult.rejected(key.qualified(),"No existe una mecánica registrada para "+manifestation.name()+".");
        MasteryExecutionResult result=mechanic.execute(manifestation,context);
        if(result.successful()) context.effects().applyAll(result.effects());
        return result;
    }

    public boolean contains(MasteryManifestation manifestation){return mechanics.containsKey(MasteryManifestationKey.of(manifestation));}
    public Set<String> registeredKeys(){return mechanics.keySet().stream().map(MasteryManifestationKey::qualified).collect(java.util.stream.Collectors.toUnmodifiableSet());}

    private void add(MasteryId family,String id,MasteryMechanic mechanic){mechanics.put(new MasteryManifestationKey(family,id),mechanic);}
    private static MasteryEffect effect(String id,String source,String target,double seconds,boolean sustained,Map<String,Double> values){return new MasteryEffect(id,source,target,seconds,sustained,values);}

    private void registerCanonical(){
        add(MasteryId.PULSION,"RECICLAJE_DE_PULSION",(m,c)->started(m,c,Map.of("PASSIVE",1.0)));
        add(MasteryId.PULSION,"AURA_DE_PULSION",(m,c)->started(m,c,Map.of("CHARGED_BLUNT_MULTIPLIER",1.35)));
        add(MasteryId.EXPLOSION_CINETICA,"EXPLOSION_CINETICA",this::kineticExplosion);
        add(MasteryId.EXPLOSION_CINETICA,"ENDURECIMIENTO_POTENCIAL",this::toroidalHardening);
        add(MasteryId.ANULACION,"ANULACION_INCIDENTAL",(m,c)->nullification(m,c,false));
        add(MasteryId.ANULACION,"ANULACION_FUNDACIONAL",(m,c)->nullification(m,c,true));
        add(MasteryId.INCITAR,"PROVOCAR",this::provoke);
        add(MasteryId.INCITAR,"GRITO_DE_GUERRA",this::warCry);
        // CAPITALIZAR y RENTABILIZAR son pasivas transaccionales: las resuelve IncitementCommercePolicy fuera del combate.
        add(MasteryId.SANAR,"RESTAURAR",this::heal);
        add(MasteryId.SANAR,"CUSTODIA",this::custody);
        add(MasteryId.REGENERACION_THETA,"REGENERACION_THETA",this::theta);
        add(MasteryId.HOMEOSTASIS_TERMICA,"ADAPTACION_TERMICA",this::thermalAdaptation);
        add(MasteryId.INVISIBILIDAD,"INVISIBILIDAD",this::invisibility);
        add(MasteryId.TRANSMUTACION,"OVERCLOCK",(m,c)->simpleSustained(m,c,Map.of("PV_REGEN_MULTIPLIER",4.0,"HUNGER_RATE_MULTIPLIER",2.0,"THIRST_RATE_MULTIPLIER",2.0)));
        add(MasteryId.TRANSMUTACION,"OVERDRIVE",(m,c)->simpleSustained(m,c,Map.of("PV_PER_MISSING_PA",1.0)));
        add(MasteryId.TRANSMUTACION,"METAMORPHOSIS",(m,c)->simpleSustained(m,c,Map.of("SWAP_CURSE_POISON_DAMAGE",1.0)));
        add(MasteryId.TRANSMUTACION,"MIRAGE",(m,c)->simpleSustained(m,c,Map.of("IFRAMES",1.0,"TARGET_LOCK_BREAK",1.0)));
        // Las dos variantes helicoidales son pasivas y se ejecutan mediante el bus, no Z/X.
    }

    /** Compatibilidad interna: PULSIÓN ya no crea multiplicadores globales sostenidos. */
    private MasteryExecutionResult sustainedMultiplier(MasteryManifestation m,MasteryExecutionContext c,boolean aura){
        return started(m,c,aura?Map.of("CHARGED_BLUNT_MULTIPLIER",1.35):Map.of("PASSIVE",1.0));
    }

    private MasteryExecutionResult kineticExplosion(MasteryManifestation m,MasteryExecutionContext c){
        int e=c.sheet().valueOf(Attribute.AGUANTE); int required=c.gender()==Gender.HOMBRE?20:15;
        if(e<required)return reject(m,"EXPLOSIÓN CINÉTICA requiere AGUANTE "+required+" para este sexo.");
        return started(m,c,Map.of("TRIGGER_ON_STAMINA_EMPTY",1.0,"KINETIC_EXPLOSION",1.0));
    }

    private MasteryExecutionResult toroidalHardening(MasteryManifestation m,MasteryExecutionContext c){
        int e=c.sheet().valueOf(Attribute.AGUANTE); int required=c.gender()==Gender.HOMBRE?40:30;
        if(e<required)return reject(m,"ENDURECIMIENTO POTENCIAL requiere AGUANTE "+required+" para este sexo.");
        return started(m,c,Map.of("TRIGGER_ON_STAMINA_EMPTY",1.0,"POTENTIAL_HARDENING",1.0));
    }

    private MasteryExecutionResult nullification(MasteryManifestation m,MasteryExecutionContext c,boolean foundational){
        MasteryTargetContext t=c.targetOptional().orElse(null);
        if(t==null)return reject(m,"ANULACIÓN exige un adversario para resolver su efecto.");
        int userEndurance=c.sheet().valueOf(Attribute.AGUANTE);
        if(!NullificationPolicy.eligible(t.relationship(),userEndurance,t.endurance()))return reject(m,"ANULACIÓN sólo afecta a un adversario HOSTIL con menos AGUANTE.");
        double radius=foundational?MasteryMath.foundationalRadiusMeters(c.world().actorHeightMeters()):0.0;
        if(foundational && t.distanceMeters()>radius)return reject(m,"ANULACIÓN FUNDACIONAL sólo alcanza adversarios dentro de su campo radial.");
        if(!foundational && !NullificationDeliveryPolicy.incidentalCanApply(t.nullificationDelivery()))return reject(m,"ANULACIÓN INCIDENTAL sólo se aplica mediante un impacto de arma válido.");
        NullificationPolicy.SuppressionState suppression=NullificationPolicy.apply(t.relationship(),userEndurance,t.endurance(),c.hostileEncounter().isActive(),t.equippedAccessoryName(),foundational);
        if(!suppression.suppressed())return reject(m,"ANULACIÓN requiere que el adversario tenga un abalorio equipado.");
        t.accessorySuppression(suppression);
        MasteryEffect e=effect("NULLIFIED_ACCESSORY:"+t.id(),key(m),t.id(),0,true,Map.of(
                "SUPPRESS_ACCESSORY_PROPERTIES",1.0,"PERSIST_UNTIL_HOSTILE_ENCOUNTER_END",1.0,
                "RADIUS_METERS",radius,"INCIDENTAL_WEAPON_IMPACT",foundational?0.0:1.0));
        return applied(m,"ANULACIÓN inhibe los efectos del abalorio equipado hasta el fin del encuentro hostil.",e);
    }

    private MasteryExecutionResult provoke(MasteryManifestation m,MasteryExecutionContext c){
        MasteryTargetContext t=c.targetOptional().orElse(null); if(t==null)return reject(m,"PROVOCAR exige un adversario.");
        ProvokeEncounterPolicy.Result r=new ProvokeEncounterPolicy().resolve(c.gender(),c.sheet().valueOf(Attribute.FUERZA),
                t.gender(),t.strength(),c.hostileEncounter().isActive(),t.attackingActorMelee(),t.helicalReleaseActive());
        if(!r.applied()) return reject(m,r.reason());
        MasteryEffect e=effect("PROVOKED:"+t.id(),key(m),t.id(),0,true,Map.of(
                "CANNOT_LOCK_PROVOKER_WHEN_ATTACKING",1.0,"PA_REGEN_DELAY_SECONDS",r.regenDelaySeconds(),
                "PA_FULL_RECOVERY_SECONDS",r.fullRecoverySeconds(),"PERSIST_UNTIL_HOSTILE_ENCOUNTER_END",1.0));
        return applied(m,r.reason(),e);
    }

    private MasteryExecutionResult warCry(MasteryManifestation m,MasteryExecutionContext c){
        WarCryStaminaPolicy.Prepared prepared=IncitementPolicy.warCry(c.gender(),c.resources().currentStamina(),c.resources().maximumStamina());
        if(!prepared.accepted()) return reject(m,prepared.reason());
        MasteryEffect e=effect("WAR_CRY_NEXT_MELEE:"+c.actorId(),key(m),c.actorId(),0,true,
                Map.of("NEXT_CONNECTED_MELEE_OPPORTUNITY",1.0,"FREE_ONLY_AGAINST_MALE",1.0,"CONSUME_ON_FEMALE_HIT",1.0));
        return applied(m,prepared.reason(),e);
    }

    private MasteryExecutionResult heal(MasteryManifestation m,MasteryExecutionContext c){
        MasteryTargetContext t=c.targetOptional().orElse(null);if(t==null)return reject(m,"RESTAURAR exige un organismo objetivo.");
        if(t.id().equals(c.actorId()))return reject(m,"RESTAURAR no puede aplicarse sobre uno mismo.");
        if(t.resources().currentHealth()<=0)return reject(m,"RESTAURAR no puede aplicarse a un organismo con 0 PV.");
        double ceiling=Math.min(t.resources().maximumHealth(),c.resources().maximumStamina());
        if(t.resources().currentHealth()>=ceiling)return reject(m,"El receptor ya ha alcanzado o superado el techo restaurable del usuario.");
        MasteryEffect e=effect("RESTORE:"+t.id(),key(m),t.id(),0,true,Map.of("HEALTH_CEILING",ceiling,"PV_PER_SECOND",t.staminaRegenPerSecond()));
        return applied(m,"RESTAURAR queda limitado a PV "+ceiling+" y progresa al PA REGEN del receptor.",e);
    }

    private MasteryExecutionResult custody(MasteryManifestation m,MasteryExecutionContext c){
        double radius=MasteryMath.foundationalRadiusMeters(c.world().actorHeightMeters());
        return started(m,c,Map.of("RADIUS_METERS",radius,"SHARED_HEALTH_REGEN",1.0,"HEALTH_REGEN_INHIBITION_IMMUNITY",1.0,"NO_REPULSION",1.0));
    }

    private MasteryExecutionResult theta(MasteryManifestation m,MasteryExecutionContext c){
        if(!ThetaRegenerationPolicy.canActivate(c.resources().currentStamina(),c.resources().maximumStamina(),c.stationary(),c.hostileEncounter().isActive()))
            return reject(m,"REGENERACIÓN THETA exige PA completos, inmovilidad y ausencia de encuentro hostil.");
        MasteryEffect e=effect("THETA:"+c.actorId(),key(m),c.actorId(),0,true,Map.of("INTERRUPT_ON_ACTION",1.0,"CONVERT_PA_TO_PV",1.0));
        return MasteryExecutionResult.of(MasteryExecutionStatus.STARTED,key(m),"REGENERACIÓN THETA inicia la conversión continua de PA en PV.",Map.of(),List.of(e));
    }

    private MasteryExecutionResult thermalAdaptation(MasteryManifestation m,MasteryExecutionContext c){
        if(!c.world().coldBuildUpActive())return reject(m,"ADAPTACIÓN TÉRMICA solo puede activarse con build-up de FRÍO ESCARCHANTE activo.");
        return started(m,c,Map.of("PA_PER_SECOND",1.0,"FREEZE_BUILDUP",1.0));
    }

    private MasteryExecutionResult invisibility(MasteryManifestation m,MasteryExecutionContext c){
        if(!c.world().naked())return reject(m,"INVISIBILIDAD exige el cuerpo desnudo.");
        return started(m,c,Map.of("PA_PER_SECOND",1.0,"UNTARGETABLE_VISUAL",1.0));
    }

    private MasteryExecutionResult simpleSustained(MasteryManifestation m,MasteryExecutionContext c,Map<String,Double> values){return started(m,c,values);}
    private MasteryExecutionResult started(MasteryManifestation m,MasteryExecutionContext c,Map<String,Double> values){
        MasteryEffect e=effect(key(m)+":"+c.actorId(),key(m),c.actorId(),0,true,values);
        return MasteryExecutionResult.of(MasteryExecutionStatus.STARTED,key(m),m.name()+" queda activada.",Map.of(),List.of(e));
    }
    private MasteryExecutionResult applied(MasteryManifestation m,String message,MasteryEffect e){return MasteryExecutionResult.of(MasteryExecutionStatus.EFFECT_APPLIED,key(m),message,Map.of(),List.of(e));}
    private MasteryExecutionResult reject(MasteryManifestation m,String message){return MasteryExecutionResult.rejected(key(m),message);}
    private boolean fullStamina(MasteryExecutionContext c){return Math.abs(c.resources().currentStamina()-c.resources().maximumStamina())<1e-9;}
    private String key(MasteryManifestation m){return MasteryManifestationKey.of(m).qualified();}
}
