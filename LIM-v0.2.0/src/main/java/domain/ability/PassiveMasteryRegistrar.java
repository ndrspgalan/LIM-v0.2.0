package domain.ability;

import domain.ability.event.*;
import domain.character.sheet.Attribute;
import domain.inventory.item.armor.ArmorMaterial;

import java.util.*;

/** Registra una sola vez las pasivas desbloqueadas y reconstruye el registro tras cargar. */
public final class PassiveMasteryRegistrar {
    private PassiveMasteryRegistrar() {}

    public static Set<String> synchronize(CharacterMasteryCollection collection,
                                          domain.character.sheet.CharacterSheet sheet,
                                          MasteryEventBus bus) {
        Set<String> active=collection.activePassiveManifestations(sheet);
        bus.clear();
        active.stream().sorted(Comparator.comparingInt(PassiveMasteryRegistrar::phase).thenComparing(String::compareTo))
                .forEach(passive -> register(passive,sheet,bus));
        return bus.registeredPassiveIds();
    }

    public static void restore(CharacterMasteryCollection collection,
                               domain.character.sheet.CharacterSheet sheet,
                               MasteryEventBus bus,
                               Collection<String> persisted) {
        bus.clear();
        Set<String> actuallyActive=collection.activePassiveManifestations(sheet);
        actuallyActive.stream().sorted(Comparator.comparingInt(PassiveMasteryRegistrar::phase).thenComparing(String::compareTo))
                .forEach(passive -> register(passive,sheet,bus));
    }

    /** Fases canónicas: amplificación física antes de conversiones de canal. */
    private static int phase(String passive) {
        return switch (passive) {
            case "TRAYECTORIA CONVERGENTE" -> 10;
            case "ELECTROGÉNESIS", "TRIBOGÉNESIS" -> 20;
            case "MIRROR'S EDGE" -> 30;
            default -> 20;
        };
    }

    private static void register(String passive, domain.character.sheet.CharacterSheet sheet, MasteryEventBus bus) {
        switch(passive) {
            case "DRENAR" -> bus.register(passive,MasteryEvent.class,e->{
                if(e instanceof CharacterDiedEvent d){
                    DrainPolicy.onCharacterDeath(d.beneficiary(),d.vitality(),d.totalHealth(),d.healthRegenerationInhibited(),true,d.killedByBeneficiaryMelee());
                } else if(e instanceof EnemyDiedEvent d){
                    // Acepta publicadores con el contrato mínimo vigente.
                    DrainPolicy.onCharacterDeath(d.beneficiary(),d.enemyVitality(),d.enemyTotalHealth(),d.enemyHealthRegenerationInhibited(),true,d.killedByBeneficiaryMelee());
                }
            });
            case "TRAYECTORIA CONVERGENTE" -> bus.register(passive,UnarmedImpactEvent.class,e->{
                /* Se resuelve por ordinal de combo en ConvergentTrajectoryPolicy, no por impacto. */
            });
            case "ELECTROGÉNESIS" -> bus.register(passive,UnarmedImpactEvent.class,e->{
                UnarmedImpactEvent u=(UnarmedImpactEvent)e;
                ElectrogenesisResult r=new ElectrogenesisPolicy().resolveUnarmedContact(sheet,true);
                u.addElectricity(r.electricityDamage());
            });
            case "TRIBOGÉNESIS" -> bus.register(passive,UnarmedImpactEvent.class,e->{
                UnarmedImpactEvent u=(UnarmedImpactEvent)e;
                int burn=new TribogenesisPolicy().burnDamage(sheet,true,u.contactedMaterial());
                u.addBurning(burn);
            });
            case "ESPÍRITU INFATIGABLE" -> bus.register(passive,ExplorationActionEvent.class,e->{
                ExplorationActionEvent x=(ExplorationActionEvent)e;
                double cost=SpiritInfatigablePolicy.staminaCost(x.staminaCost(),true,x.hostileEncounter(),x.mode());
                x.setStaminaCost(cost);
            });
            case "LIBERACIÓN HELICOIDAL" -> bus.register(passive,ExplorationActionEvent.class,e->{ /* la recuperación de PA bajo carga consulta la presencia de esta pasiva */ });
            case "OPTIMIZACIÓN HELICOIDAL" -> bus.register(passive,ExplorationActionEvent.class,e->{ /* inmunidad consultada por la política de PA REGEN */ });
            case "RECICLAJE DE PULSIÓN", "AURA DE PULSIÓN", "ANULACIÓN INCIDENTAL", "ANULACIÓN FUNDACIONAL", "CUSTODIA" ->
                    bus.register(passive, MasteryEvent.class, e -> { /* pasivas consultadas por políticas de dominio específicas */ });
            case "REGULACIÓN CALÓRICA SUPERIOR" -> bus.register(passive,EnvironmentalTickEvent.class,e->((EnvironmentalTickEvent)e).doubleSatisfiedDurations());
                        case "MIRROR'S EDGE" -> bus.register(passive,UnarmedImpactEvent.class,e->{ /* : reciprocidad normal con Intersticio; no transmuta daño. */ });
            default -> {
                boolean ferae = java.util.Arrays.stream(domain.bestiarium.physical_plane.ferae.FeraeSpecies.values())
                        .anyMatch(species -> species.label().equalsIgnoreCase(passive));
                if (ferae) {
                    bus.register(passive,FeraeEncounterEvent.class,e->{
                        FeraeEncounterEvent f=(FeraeEncounterEvent)e;
                        if(!passive.equalsIgnoreCase(f.species().label())) return;
                        f.setRelationship(AnimalEmpathyPolicy.relationship(f.species(),f.context()));
                        f.setCompanionEligible(AnimalEmpathyPolicy.companionEligibility(f.species(),f.context()).eligible());
                    });
                }
            }
        }
    }
}
