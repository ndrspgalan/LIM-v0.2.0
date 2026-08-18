package qa.domain.gold;

import application.mdpar.boundary.v1.*;
import application.mdpar.representation.v1.*;
import application.mdpar.serialization.v1.*;
import application.mdpar.transport.v1.FixtureMdparTransportV1;
import application.mdpar.integration.v1.MdparBoundaryClientV1;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;

final class MdparJsonProtocolV1Test {
    private final LimCombatRepresentationJsonV1 representationCodec = new LimCombatRepresentationJsonV1();
    private final MdparBoundaryJsonCodecV1 boundaryCodec = new MdparBoundaryJsonCodecV1();

    @Test void combatRepresentationRoundTripsWithoutBecomingTheGlobalBoundary() {
        var representation=sampleRepresentation();
        String json=representationCodec.write(representation);
        assertEquals(representation,representationCodec.read(json));
        assertEquals(json,representationCodec.write(representationCodec.read(json)));
        assertTrue(json.contains("\"value\":75"));
        assertTrue(json.contains("\"value\":null"));
        assertEquals(LimCombatRepresentationV1.VERSION, representation.schemaVersion());
        assertNotEquals(MdparRequestEnvelopeV1.VERSION, representation.schemaVersion());
    }

    @Test void globalRequestEnvelopeRoundTripsAndContainsNoSccOrPluginSelection() {
        var request=sampleRequest();
        String json=boundaryCodec.writeRequest(request);
        assertEquals(request,boundaryCodec.readRequest(json));
        assertEquals(json,boundaryCodec.writeRequest(boundaryCodec.readRequest(json)));
        assertTrue(json.contains("\"boundaryVersion\":\"mdpar-boundary/v1\""));
        assertTrue(json.contains("\"representationVersion\":\"lim-combat-state/v1\""));
        assertFalse(json.contains("\"who\""));
        assertFalse(json.contains("\"which\""));
        assertFalse(json.contains("\"what\""));
        assertFalse(json.contains("\"howMuch\""));
        assertFalse(json.contains("lim-combat:variables"));
        assertFalse(json.contains("plugin"));
    }

    @Test void operationalResponseIsActionWhyHowConclusionPlusAuditSupport() {
        var response=sampleResponse("request-fixture-1");
        String json=boundaryCodec.writeResponse(response);
        assertEquals(response,boundaryCodec.readResponse(json));
        assertEquals("CONTAIN [S+] | Severity: P2",response.operational().ACTION());
        assertEquals("Contain and verify through reversible steps.",response.operational().HOW());
        assertEquals("positive",response.operational().SUPPORT().fields().get("grace"));
        assertFalse(json.contains("\"decisionId\""));
        assertFalse(json.contains("\"horizonTicks\""));
        assertFalse(json.contains("\"parameters\""));
    }

    @Test void requestAndPublicationHashesAreStable() {
        var request=sampleRequest();
        var response=sampleResponse(request.requestId());
        assertEquals(boundaryCodec.requestSha256(request),boundaryCodec.requestSha256(boundaryCodec.readRequest(boundaryCodec.writeRequest(request))));
        assertEquals(boundaryCodec.responseSha256(response),boundaryCodec.responseSha256(boundaryCodec.readResponse(boundaryCodec.writeResponse(response))));
        assertEquals(64,boundaryCodec.requestSha256(request).length());
        assertEquals(64,boundaryCodec.responseSha256(response).length());
    }

    @Test void strictReadersRejectUnknownBoundaryFieldsVersionsAndEpistemicLeaks() {
        String request=boundaryCodec.writeRequest(sampleRequest());
        assertThrows(WireValidationExceptionV1.class,()->boundaryCodec.readRequest(request.replaceFirst("\\{","{\"unexpected\":1,")));
        assertThrows(WireValidationExceptionV1.class,()->boundaryCodec.readRequest(request.replace(MdparRequestEnvelopeV1.VERSION,"mdpar-boundary/v2")));

        String representation=representationCodec.write(sampleRepresentation());
        String leak=representation.replace("\"valueType\":\"EMPTY\",\"value\":null,\"epistemicState\":\"UNKNOWN\"",
                "\"valueType\":\"INTEGER\",\"value\":75,\"epistemicState\":\"UNKNOWN\"");
        assertThrows(IllegalArgumentException.class,()->representationCodec.read(leak));
    }

    @Test void fixtureTransportEnforcesRequestCorrelation() {
        var request=sampleRequest();
        var transport=new FixtureMdparTransportV1(r->sampleResponse(r.requestId()));
        assertEquals(request.requestId(),transport.request(request).toCompletableFuture().join().requestId());

        var broken=new FixtureMdparTransportV1(r->sampleResponse("another-request"));
        assertThrows(CompletionException.class,()->broken.request(request).toCompletableFuture().join());
    }


    @Test void boundaryClientTracksCanonicalSendReceiveAcceptAndCompletion() {
        var request=sampleRequest();
        var tracker=new MdparRequestTrackerV1();
        var transport=new FixtureMdparTransportV1(r->sampleResponse(r.requestId()));
        var client=new MdparBoundaryClientV1(transport,boundaryCodec,tracker);
        var response=client.request(request).toCompletableFuture().join();
        assertEquals(request.requestId(),response.requestId());
        assertEquals(MdparRequestLifecycleStateV1.ACCEPTED,tracker.state(request.requestId()).orElseThrow());
        client.markActive(request.requestId());
        client.markCompleted(request.requestId());
        assertEquals(MdparRequestLifecycleStateV1.COMPLETED,tracker.state(request.requestId()).orElseThrow());
    }

    @Test void lifecycleTracksWithoutCreatingLocalDecisions() {
        var tracker=new MdparRequestTrackerV1();
        var request=sampleRequest();
        tracker.created(request);
        tracker.transition(request.requestId(),MdparRequestLifecycleStateV1.CREATED,MdparRequestLifecycleStateV1.SERIALIZED);
        tracker.transition(request.requestId(),MdparRequestLifecycleStateV1.SERIALIZED,MdparRequestLifecycleStateV1.SENT);
        tracker.transition(request.requestId(),MdparRequestLifecycleStateV1.SENT,MdparRequestLifecycleStateV1.RECEIVED);
        tracker.transition(request.requestId(),MdparRequestLifecycleStateV1.RECEIVED,MdparRequestLifecycleStateV1.ACCEPTED);
        tracker.transition(request.requestId(),MdparRequestLifecycleStateV1.ACCEPTED,MdparRequestLifecycleStateV1.ACTIVE);
        tracker.terminal(request.requestId(),MdparRequestLifecycleStateV1.COMPLETED);
        assertEquals(MdparRequestLifecycleStateV1.COMPLETED,tracker.state(request.requestId()).orElseThrow());
    }

    static MdparRequestEnvelopeV1 sampleRequest(){
        var representation=sampleRepresentation();
        return MdparRequestEnvelopeV1.lim("request-fixture-1",representation.schemaVersion(),40,new LimCombatRepresentationJsonV1().toPayload(representation));
    }

    static MdparResponseEnvelopeV1 sampleResponse(String requestId){
        var routing=new MdparRoutingMetadataV1("OPERATIONAL","RESOLVED",
                "Full MDPAR shuttle completed.",1522,new JsonObjectPayloadV1(Map.of(
                        "rawInputSegments",1,
                        "resolvedSegments",1,
                        "activationSequence",List.of("troubleshooting:english"))));
        var publication=new MdparOperationalPublicationV1(
                "CONTAIN [S+] | Severity: P2",
                "Domain pressure and the structural map jointly authorize containment.",
                "Contain and verify through reversible steps.",
                "The operational posture is CONTAIN.",
                new JsonObjectPayloadV1(Map.of("grace","positive","structuralMark","S_PLUS","severityMark","P2")));
        return new MdparResponseEnvelopeV1(MdparResponseEnvelopeV1.VERSION,requestId,routing,publication);
    }

    static LimCombatRepresentationV1 sampleRepresentation(){
        var pa=KnowledgeFactV1.exact("self.actor.pa",75,"GOLD_FIXTURE");
        var unknown=KnowledgeFactV1.unknown("enemy.currentPa","LIM_PERCEPTION");
        var self=new ActorKnowledgeV1("actor-1",ActorOriginV1.SUBPROFESSION,"FUSILERO_V881",EpistemicStateV1.EXACT,List.of(pa));
        var squad=new TacticalSquadRepresentationV1("squad-1","force-1","ASSAULT","COMPOSITE",List.of("actor-1"),true,EpistemicStateV1.EXACT);
        var force=new ForceRepresentationV1("force-1",List.of("squad-1"),true,EpistemicStateV1.EXACT);
        var battle=new BattlespaceRepresentationV1("fixture-composite-squad",0x505L,17,40,"ARMED_PATROL_OR_CONTRACT","FIELD",List.of(force),List.of(squad),List.of());
        var action=new ActionRepresentationV1("remote-0","REMOTE",ActionAvailabilityV1.LEGAL_NOW,ActionTargetKindV1.AREA,List.of(),List.of(KnowledgeFactV1.exact("action.remote.mode","BLIND_FIRE","LIM_ACTION_RESOLVER")));
        return new LimCombatRepresentationV1(LimCombatRepresentationV1.VERSION,self,battle,List.of(),List.of(action),List.of(),List.of(pa,unknown),TargetingDoctrineV1.canonical());
    }

    @org.junit.jupiter.api.Test
    void requestLifecycleRejectsIllegalTerminalTransitions() {
        var tracker = new MdparRequestTrackerV1();
        var request = new MdparRequestEnvelopeV1(MdparRequestEnvelopeV1.VERSION, "lifecycle-test", "LIM",
                "lim-state/v1", 1, new JsonObjectPayloadV1(Map.of("state", "ready")));
        tracker.created(request);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> tracker.terminal(request.requestId(), MdparRequestLifecycleStateV1.COMPLETED));
        tracker.transition(request.requestId(), MdparRequestLifecycleStateV1.CREATED, MdparRequestLifecycleStateV1.SERIALIZED);
        tracker.transition(request.requestId(), MdparRequestLifecycleStateV1.SERIALIZED, MdparRequestLifecycleStateV1.SENT);
        tracker.terminal(request.requestId(), MdparRequestLifecycleStateV1.TIMED_OUT);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> tracker.transition(request.requestId(), MdparRequestLifecycleStateV1.TIMED_OUT, MdparRequestLifecycleStateV1.ACTIVE));
    }
}
