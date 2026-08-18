package domain.combat.ai.declarative;

import domain.combat.ai.remote.*;
import domain.inventory.item.ammunition.AmmunitionDescriptor;
import domain.inventory.item.firearms.FirearmHandlingState;
import domain.inventory.item.firearms.FirearmItem;
import domain.inventory.item.firearms.ArcInductionFirearmItem;
import domain.inventory.item.firearms.LimeSprayerItem;
import domain.inventory.item.firearms.ClusterCannonFirearmItem;
import domain.inventory.item.throwingWeapons.ThrowingWeaponEffect;
import domain.inventory.item.throwingWeapons.ThrowingWeaponItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**  — traduce arsenal remoto observado a alternativas declarativas, nunca las ordena ni puntúa. */
public final class RemoteActionCandidateResolver {
    public List<RemoteActionCandidate> resolve(RemoteArsenalSnapshot arsenal, double currentDistanceMeters) {
        Objects.requireNonNull(arsenal);
        if (!Double.isFinite(currentDistanceMeters)||currentDistanceMeters<0) throw new IllegalArgumentException("Distancia inválida.");
        List<RemoteActionCandidate> out=new ArrayList<>();
        for (RemoteCombatOption option: arsenal.options()) {
            out.add(candidateForCurrentState(option,currentDistanceMeters));
            if (option.supportsAiming() && option.source() instanceof FirearmItem firearm
                    && option.readiness()!=RemoteReadiness.UNAVAILABLE
                    && option.readiness()!=RemoteReadiness.RECOVERING) {
                out.add(aimCandidate(option,currentDistanceMeters, firearm.handlingState()==FirearmHandlingState.AIMING));
            }
        }
        return List.copyOf(out);
    }

    private RemoteActionCandidate candidateForCurrentState(RemoteCombatOption o,double distance) {
        RemoteActionType action=switch(o.readiness()) {
            case READY -> o.family()==RemoteOffenseFamily.THROWN ? RemoteActionType.THROW : RemoteActionType.FIRE;
            case NEEDS_RELOAD -> RemoteActionType.RELOAD;
            case NEEDS_AMMUNITION -> RemoteActionType.ACQUIRE_AMMUNITION;
            case NEEDS_CHARGE -> RemoteActionType.CHARGE_WEAPON;
            case RECOVERING -> RemoteActionType.WAIT_RECOVERY;
            case UNAVAILABLE -> o.family()==RemoteOffenseFamily.RANGED_WEAPON ? RemoteActionType.ACQUIRE_AMMUNITION : RemoteActionType.WAIT_RECOVERY;
        };
        boolean executable = o.readiness()==RemoteReadiness.READY
                || o.readiness()==RemoteReadiness.NEEDS_RELOAD
                || o.readiness()==RemoteReadiness.NEEDS_CHARGE
                || o.readiness()==RemoteReadiness.RECOVERING;
        return build(o,distance,action,executable,false,relations(o));
    }

    private RemoteActionCandidate aimCandidate(RemoteCombatOption o,double distance,boolean aiming) {
        List<RemoteRelationFact> relations=new ArrayList<>(relations(o));
        relations.add(new RemoteRelationFact("AIMING_STATE_CHANGE", aiming ? "La acción abandona AIMING." : "La acción entra en AIMING."));
        return build(o,distance,RemoteActionType.TOGGLE_AIM,true,aiming,relations);
    }

    private RemoteActionCandidate build(RemoteCombatOption o,double distance,RemoteActionType action,boolean executable,
                                        boolean aiming,List<RemoteRelationFact> relations) {
        Optional<AmmunitionFact> ammo=o.ammunitionDescriptor().map(AmmunitionFact::from);
        boolean currentAim=o.source() instanceof FirearmItem f && f.handlingState()==FirearmHandlingState.AIMING;
        if (action==RemoteActionType.TOGGLE_AIM) currentAim=aiming;
        return new RemoteActionCandidate(o.name(),o.family(),action,o.readiness(),executable,distance,
                o.minimumAdequateDistanceMeters(),o.maximumEffectiveDistanceMeters(),o.distanceState(distance),
                o.preparationSeconds(),o.reloadDurationSeconds(),o.shotIntervalSeconds(),o.recoverySeconds(),o.chargeDurationSeconds(),o.availableUses(),o.supportsAiming(),currentAim,o.improvised(),o.recoverable(),
                o.lethality(),ammo,relations);
    }

    private List<RemoteRelationFact> relations(RemoteCombatOption o) {
        List<RemoteRelationFact> r=new ArrayList<>();
        if (o.ammunitionDescriptor().isPresent()) {
            AmmunitionDescriptor d=o.ammunitionDescriptor().orElseThrow();
            r.add(new RemoteRelationFact("AMMUNITION_COMPATIBILITY",
                    d.family()+" / "+d.caliber()+" / "+d.material()+" / "+d.variant()));
            if (d.recoverable()) r.add(new RemoteRelationFact("PROJECTILE_RECOVERABLE","La munición conserva recuperabilidad material."));
            if (d.family()==domain.inventory.item.ammunition.AmmunitionFamily.ARROW
                    || d.family()==domain.inventory.item.ammunition.AmmunitionFamily.PEBBLE) {
                r.add(new RemoteRelationFact("HELICOIDAL_INTERACTION","Flechas y guijarros son desviables por una Espada Helicoidal durante hitbox ofensiva activa."));
            }
            if (".46".equalsIgnoreCase(d.caliber()) && "Plomo".equalsIgnoreCase(d.material())) {
                r.add(new RemoteRelationFact("HELICOIDAL_INTERACTION","El .46 de plomo neumático es desviable por intercepción oblicua de la Espada Helicoidal."));
            }
        }
        if (o.source() instanceof ThrowingWeaponItem thrown) {
            ThrowingWeaponEffect e=thrown.effect();
            switch(e) {
                case THROWING_KNIFE -> r.add(new RemoteRelationFact("HELICOIDAL_INTERACTION","El cuchillo arrojadizo puede ser desviado por la hoja helicoidal activa."));
                case AMMONIA_CAPSULE, INCENDIARY_TERRACOTTA, PHOSPHORUS_SULFUR_EGG ->
                        r.add(new RemoteRelationFact("HELICOIDAL_INTERACTION","El objeto detona/rompe con normalidad al impactar contra la hoja helicoidal; no queda neutralizado."));
            }
            r.add(new RemoteRelationFact("THROWN_EFFECT", e.name()));
        }
        if (o.recoverable()) r.add(new RemoteRelationFact("SOURCE_RECOVERABLE","La unidad/proyectil puede recuperarse según su política física."));
        if (o.family()==RemoteOffenseFamily.FIREARM && o.source() instanceof FirearmItem f) {
            r.add(new RemoteRelationFact("RECOIL_VELOCITY_MPS",Double.toString(f.effectiveRecoilVelocityPerShotMps())));
            r.add(new RemoteRelationFact("RELOAD_DURATION_SECONDS",Double.toString(f.reloadDurationSeconds())));
            r.add(new RemoteRelationFact("SHOT_INTERVAL_SECONDS",Double.toString(f.shotIntervalSeconds())));
            if(f instanceof domain.inventory.item.firearms.PneumaticFirearmItem p) r.add(new RemoteRelationFact("PRESSURE_STEP_SECONDS",Double.toString(p.pressureStepDurationSeconds())));
            r.add(new RemoteRelationFact("TRAJECTORY_BEYOND_EFFECTIVE_RANGE",Boolean.toString(f.trajectoryMayContinueBeyondEffectiveDirectRange())));
            if (f instanceof ArcInductionFirearmItem)
                r.add(new RemoteRelationFact("ELECTRIC_CONTROL","La descarga de inducción aplica su efecto eléctrico físico; LIM no lo convierte en utilidad táctica."));
            if (f instanceof LimeSprayerItem)
                r.add(new RemoteRelationFact("CORROSIVE_EFFECT","El rociador conserva su efecto corrosivo material sin puntuación táctica."));
            if (f instanceof ClusterCannonFirearmItem)
                r.add(new RemoteRelationFact("AREA_EFFECT","El cañón de racimo conserva su consecuencia de área material sin puntuación táctica."));
        }
        return List.copyOf(r);
    }
}
