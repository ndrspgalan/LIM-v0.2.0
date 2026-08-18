package domain.combat.ai.declarative;

import domain.inventory.*;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.ArtifactAccessory;
import domain.inventory.item.misc.*;
import domain.status.TherapeuticEffectProfile;
import java.util.*;

/**
 * : enumera operaciones materiales de inventario. No puntúa ni selecciona.
 */
public final class InventoryActionCandidateResolver {

    public List<InventoryActionCandidate> resolve(InventoryDecisionState state) { return resolve(null,state); }

    public List<InventoryActionCandidate> resolve(CombatActorDecisionState actor, InventoryDecisionState state) {
        Objects.requireNonNull(state);
        IdentityHashMap<InventoryEntry,InventoryLocationFact> owned = collectOwned(state.inventory());
        ArrayList<InventoryActionCandidate> out = new ArrayList<>();
        for (var e : owned.entrySet()) appendOwned(out,e.getKey(),e.getValue(),state.inventory(),actor);
        for (InventoryEntry ground : state.reachableGroundItems()) {
            InventoryItemFact fact=fact(ground,InventoryLocationFact.ground());
            out.add(new InventoryActionCandidate(InventoryActionType.PICK_UP,fact,Optional.empty(),OptionalInt.empty(),
                    List.of("El objeto está al alcance físico inmediato."),
                    List.of("La instancia entra por la política universal de admisión/colocación de inventario.")));
        }
        appendRotorHandling(out,state.inventory(),owned);
        return List.copyOf(out);
    }

    public List<ActiveEffectFact> activeEffects(InventoryDecisionState state) {
        return state.therapeuticEffects().snapshots().stream()
                .map(s -> new ActiveEffectFact(s.name(),s.remaining(),s.timeScale().name()))
                .toList();
    }

    private void appendOwned(List<InventoryActionCandidate> out, InventoryEntry item, InventoryLocationFact location, InventoryState inventory, CombatActorDecisionState actor) {
        InventoryItemFact fact=fact(item,location);
        var a=InventoryObjectActionPolicy.evaluate(item,inventory);
        if(a.allows(InventoryObjectAction.DROP)) add(out,InventoryActionType.DROP,fact,null,null,List.of(),List.of("La instancia deja de pertenecer al inventario y persiste en el mundo cuando corresponda."));
        if(a.allows(InventoryObjectAction.INSPECT)) add(out,InventoryActionType.INSPECT,fact,null,null,List.of(),List.of("Expone el estado/propiedades de la misma instancia; no la consume."));
        if(a.allows(InventoryObjectAction.ROTATE_90)) add(out,InventoryActionType.ROTATE_90,fact,null,null,List.of("El objeto está almacenado en un grid y posee dimensiones de grid."),List.of("Cambia su orientación 90° si continúa cabiendo en el compartimento."));
        if(a.allows(InventoryObjectAction.UNEQUIP)) add(out,InventoryActionType.UNEQUIP,fact,null,null,List.of(),List.of("Retira la asignación activa/quick y reingresa por la política de almacenamiento cuando corresponda."));
        for(EquipmentSlot slot:a.eligibleEquipmentSlots()) add(out,InventoryActionType.EQUIP_ACTIVE,fact,slot,null,List.of("La ranura "+slot.label()+" está libre y admite físicamente esta instancia."),List.of("La instancia abandona su compartimento y pasa a "+slot.label()+"."));
        for(int slot:a.eligibleQuickSlots()) add(out,InventoryActionType.EQUIP_QUICK_ACCESS,fact,null,slot,List.of("Quick "+slot+" está disponible y corresponde al compartimento físico de origen."),List.of("Quick "+slot+" referencia esta misma instancia almacenada."));
        if(item instanceof ArtifactAccessory artifact) {
            boolean equipped=QuickAccessUsePolicy.isActiveEquipment(item,inventory.equipment());
            boolean attributeOk=actor!=null && actor.sheet().valueOf(artifact.activationAttribute())>=artifact.activationMinimum();
            if(equipped && attributeOk) add(out,InventoryActionType.USE,fact,null,null,artifactPreconditions(artifact),artifactConsequences(artifact));
        } else if(a.allows(InventoryObjectAction.USE)) add(out,InventoryActionType.USE,fact,null,null,usePreconditions(item),useConsequences(item));
    }

    private static List<String> artifactPreconditions(ArtifactAccessory a) {
        ArrayList<String> x=new ArrayList<>();
        x.add("El abalorio-artefacto está equipado y el actor cumple "+a.activationAttribute()+" >= "+a.activationMinimum()+".");
        switch(a.artifactId()){
            case "TOKKOSHO_V881" -> { x.add("Requiere target lock y clima electroatmosférico compatible; la inmunidad a ELECTRICITY impide la descarga sin consumir la carga."); }
            case "HELIOGRAPH_V881" -> x.add("Requiere cielo/luz compatibles; contra un blanco exige target lock y rostro no protegido.");
            case "RESONANT_TUNING_FORK_V881" -> x.add("Sólo produce información útil ante invisibilidad activa o un cambiaformas detectable.");
            case "SEISMOSCOPE_V881" -> x.add("Requiere acoplamiento mecánico y una fuente en movimiento; alcance depende de asfalto/lluvia.");
            case "NOCTURLABE_V881" -> x.add("Requiere NIGHT y evidencia temporal local presente.");
            case "ASTROLABE" -> x.add("Requiere un destino seleccionado; orienta, no resuelve la ruta.");
            default -> x.add("Requiere satisfacer el contexto físico específico del artefacto.");
        }
        return List.copyOf(x);
    }

    private static List<String> artifactConsequences(ArtifactAccessory a) {
        return switch(a.artifactId()){
            case "TOKKOSHO_V881" -> List.of("Si todas las condiciones se cumplen: descarga ELECTRICITY 100; sólo una descarga realmente emitida consume carga.");
            case "HELIOGRAPH_V881" -> List.of("Emite señal o interrumpe la acción actual del blanco según target lock/protección facial.");
            case "RESONANT_TUNING_FORK_V881" -> List.of("Habilita target lock de invisibilidad o identificación privada de cambiaformas.");
            case "SEISMOSCOPE_V881" -> List.of("Revela una fuente vibratoria dentro del radio ambiental efectivo.");
            case "NOCTURLABE_V881" -> List.of("Reproduce 3 s de evidencia temporal local.");
            case "ASTROLABE" -> List.of("Señala la dirección del destino seleccionado; no consume cargas.");
            default -> List.of("Ejecuta la función física declarada por el abalorio-artefacto.");
        };
    }

    private static List<String> usePreconditions(InventoryEntry item) {
        ArrayList<String> x=new ArrayList<>();
        if(QuickAccessUsePolicy.requiresQuickAccess(item)) x.add("La misma instancia debe estar asignada a un acceso rápido actualmente disponible.");
        if(item instanceof StackableMiscellaneousItem s) x.add("Debe conservar al menos una carga/uso; actual="+s.currentUses()+".");
        return List.copyOf(x);
    }

    private static List<String> useConsequences(InventoryEntry item) {
        ArrayList<String> x=new ArrayList<>();
        if(item instanceof StackableMiscellaneousItem s) {
            x.add("Consume una unidad/carga según "+s.resourceKind()+" cuando la acción se completa.");
            if(s.useAnimation()!=null) x.add("Tiempo de uso conocido: "+s.useAnimation().durationRealSeconds()+" s reales.");
        }
        for(ConsumableEffectFact f:effects(item)) x.add(f.key()+" = "+f.value());
        return List.copyOf(x);
    }

    static InventoryItemFact fact(InventoryEntry item, InventoryLocationFact location) {
        int current=0,max=0; OptionalDouble duration=OptionalDouble.empty();
        if(item instanceof StackableMiscellaneousItem s){ current=s.currentUses(); max=s.maximumUses(); if(s.useAnimation()!=null) duration=OptionalDouble.of(s.useAnimation().durationRealSeconds()); }
        return new InventoryItemFact(item,item.canonicalTypeId(),item.name(),item.weightKg(),location,current,max,duration,effects(item));
    }

    private static List<ConsumableEffectFact> effects(InventoryEntry item) {
        ArrayList<ConsumableEffectFact> e=new ArrayList<>();
        if(item instanceof FoodItem f){
            e.add(new ConsumableEffectFact("HAMBRE","-"+f.foodType().baseHungerReduction()+" nivel(es)"));
            if(f.thirstRestored()>0)e.add(new ConsumableEffectFact("SED","-"+f.thirstRestored()+" nivel(es)"));
            if(f.thirstAdded()>0)e.add(new ConsumableEffectFact("SED","+"+f.thirstAdded()+" nivel(es)"));
            e.add(new ConsumableEffectFact("COMBINACION_ALIMENTARIA",Boolean.toString(f.foodType().canReceiveCombinationBonus())));
        }
        if(item instanceof BeverageItem) e.add(new ConsumableEffectFact("SED","-1 nivel por carga"));
        if(item instanceof TherapeuticItem t){
            TherapeuticEffectProfile p=t.therapeuticEffect();
            if(p.healingKind()!=TherapeuticEffectProfile.HealingKind.NONE)e.add(new ConsumableEffectFact("HEALING_KIND",p.healingKind().name()));
            if(p.instantHealthFraction()>0)e.add(new ConsumableEffectFact("PV_INSTANTANEO_FRACCION",Double.toString(p.instantHealthFraction())));
            if(p.healthRegenerationMultiplier()!=1)e.add(new ConsumableEffectFact("PV_REGEN_MULTIPLICADOR",Double.toString(p.healthRegenerationMultiplier())));
            if(p.minimumHealth()>0)e.add(new ConsumableEffectFact("PV_MINIMO",Double.toString(p.minimumHealth())));
            if(p.staminaRegenerationMultiplier()!=1)e.add(new ConsumableEffectFact("PA_REGEN_MULTIPLICADOR",Double.toString(p.staminaRegenerationMultiplier())));
            if(p.carryingCapacityMultiplier()!=1)e.add(new ConsumableEffectFact("CARGA_MULTIPLICADOR",Double.toString(p.carryingCapacityMultiplier())));
            if(p.physicalStabilityModifier()!=0)e.add(new ConsumableEffectFact("ESTABILIDAD_FISICA_MOD",Integer.toString(p.physicalStabilityModifier())));
            if(p.sanityModifier()!=0)e.add(new ConsumableEffectFact("CORDURA_MOD",Integer.toString(p.sanityModifier())));
            if(p.feintReachMultiplier()!=1)e.add(new ConsumableEffectFact("FINTAR_ALCANCE_MULTIPLICADOR",Double.toString(p.feintReachMultiplier())));
            if(p.mirageInvulnerabilityMultiplier()!=1)e.add(new ConsumableEffectFact("MIRAGE_IFRAME_MULTIPLICADOR",Double.toString(p.mirageInvulnerabilityMultiplier())));
            if(p.frenzyImmunity())e.add(new ConsumableEffectFact("INMUNIDAD_FRENESI","true"));
            if(p.duration()!=null)e.add(new ConsumableEffectFact("DURACION",p.duration().duration()+" "+p.duration().timeScale()));
            SurvivalConsumptionEffect s=t.survivalEffect();
            s.foodType().ifPresent(ft->e.add(new ConsumableEffectFact("HAMBRE","-"+ft.baseHungerReduction()+" nivel(es)")));
            if(s.thirstRestored()>0)e.add(new ConsumableEffectFact("SED","-"+s.thirstRestored()+" nivel(es)"));
            if(s.thirstAdded()>0)e.add(new ConsumableEffectFact("SED","+"+s.thirstAdded()+" nivel(es)"));
        }
        return List.copyOf(e);
    }

    private static IdentityHashMap<InventoryEntry,InventoryLocationFact> collectOwned(InventoryState state) {
        IdentityHashMap<InventoryEntry,InventoryLocationFact> m=new IdentityHashMap<>();
        for(var type:domain.inventory.logistics.InventoryCompartmentType.values()){
            var c=state.logistics().compartment(type); if(!c.available())continue;
            for(InventoryEntry i:c.entries())m.putIfAbsent(i,InventoryLocationFact.stored(type));
        }
        for(EquipmentSlot s:EquipmentSlot.values()) state.equipment().itemAt(s).ifPresent(i->m.put(i,InventoryLocationFact.equipped(s)));
        for(int slot=1;slot<=QuickAccessBar.SLOT_COUNT;slot++){
            final int n=slot; state.quickAccessBar().slots().get(slot-1).ifPresent(i->m.putIfAbsent(i,InventoryLocationFact.quick(n)));
        }
        return m;
    }

    private static void appendRotorHandling(List<InventoryActionCandidate> out, InventoryState inventory, IdentityHashMap<InventoryEntry,InventoryLocationFact> owned){
        inventory.equipment().itemAt(EquipmentSlot.BACK_HAND).filter(WeaponItem.class::isInstance).map(WeaponItem.class::cast)
                .filter(w->w.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)).ifPresent(rotor->{
                    InventoryItemFact f=fact(rotor,owned.getOrDefault(rotor,InventoryLocationFact.equipped(EquipmentSlot.BACK_HAND)));
                    if(rotor.isSheathed()) add(out,InventoryActionType.DEPLOY_DORSAL_ROTOR,f,null,null,
                            List.of("El Espadón de Rotor está retraído en BACK_HAND y existe sistema dorsal funcional."),
                            List.of("El Rotor se despliega; armas LEFT/RIGHT permanecen en sus slots pero se envainan para liberar ambas manos físicas."));
                    else add(out,InventoryActionType.RETRACT_DORSAL_ROTOR,f,null,null,
                            List.of("El sistema dorsal está disponible para recibir el Rotor."),
                            List.of("El Rotor se retrae en BACK_HAND y deja libres las manos físicas."));
                });
    }

    private static void add(List<InventoryActionCandidate> out, InventoryActionType type, InventoryItemFact item,
                            EquipmentSlot equipmentSlot, Integer quickSlot,List<String> pre,List<String> post){
        out.add(new InventoryActionCandidate(type,item,Optional.ofNullable(equipmentSlot),quickSlot==null?OptionalInt.empty():OptionalInt.of(quickSlot),pre,post));
    }
}
