package qa.domain;

import domain.ability.FoundationalNullificationRelationshipPolicy;
import domain.ability.FullStaminaMasteryPolicy;
import domain.ability.HalfStaminaMasteryPolicy;
import domain.ability.MasteryCatalog;
import domain.ability.MasteryId;
import domain.ability.MasteryType;
import domain.ability.PairMastery;
import domain.ability.TransmutationMastery;
import domain.ability.TransmutationNodeId;
import domain.social.RelationshipType;
import domain.targeting.MirageTargetLockPolicy;
import domain.targeting.MirageTargetLockResult;

public final class MasteryPolicyVerification {
    private MasteryPolicyVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyHalfStaminaRequirement();
        verifyFoundationalNullificationRelationships();
        verifyMirageTargetLockDirection();
        verifyCanonicalDescriptions();
    }

    private static void verifyHalfStaminaRequirement() {
        if (HalfStaminaMasteryPolicy.canUse("EXPLOSIÓN CINÉTICA", 20, 40)) fail("REFINAMIENTO ya no se activa al 50% de PA.");
        if (HalfStaminaMasteryPolicy.staminaAfterUse("EXPLOSIÓN CINÉTICA", 40, 40) != 40) fail("EXPLOSIÓN no consume 50% al activarse.");
        PairMastery pair=(PairMastery)MasteryCatalog.require(MasteryId.EXPLOSION_CINETICA);
        if(pair.original().type()!=MasteryType.SUSTAINED||pair.refined().type()!=MasteryType.SUSTAINED)fail("Ambas ramas deben ser sostenidas.");
    }

    private static void verifyFoundationalNullificationRelationships() {
        for(RelationshipType r:RelationshipType.values()) if(FoundationalNullificationRelationshipPolicy.relationshipAfterEnteringField(r)!=r) fail("ANULACIÓN FUNDACIONAL no altera relaciones.");
    }

    private static void verifyMirageTargetLockDirection() {
        MirageTargetLockResult result = MirageTargetLockPolicy.whenTargetActivatesMirage(true);
        if (!result.targetLockTemporarilyLost() || !result.targetLockAutomaticallyRestored()) fail("El observador debe perder y recuperar la fijación sobre el usuario de MIRAGE.");
        MirageTargetLockResult noLock = MirageTargetLockPolicy.whenTargetActivatesMirage(false);
        if (noLock.targetLockTemporarilyLost() || noLock.targetLockAutomaticallyRestored()) fail("Sin fijación previa no debe producirse cambio.");
    }

    private static void verifyCanonicalDescriptions() {
        PairMastery explosion=(PairMastery)MasteryCatalog.require(MasteryId.EXPLOSION_CINETICA);
        if(!explosion.name().equals("REFINAMIENTO DE ENERGÍA MALDITA")||!explosion.refined().name().equals("ENDURECIMIENTO POTENCIAL"))fail("Canon de Refinamiento incorrecto.");
        PairMastery nullification=(PairMastery)MasteryCatalog.require(MasteryId.ANULACION);
        if(!nullification.refined().mechanicalDescription().contains("abalorio"))fail("ANULACIÓN debe documentar supresión de abalorio.");
        TransmutationMastery transmutation=(TransmutationMastery)MasteryCatalog.require(MasteryId.TRANSMUTACION);
        String mirage=transmutation.node(TransmutationNodeId.MIRAGE).mechanicalDescription();
        if(!mirage.contains("fijado al usuario")||!mirage.contains("PV REGEN del usuario"))fail("MIRAGE desactualizado.");
    }

    private static void fail(String message) { throw new AssertionError(message); }
}
