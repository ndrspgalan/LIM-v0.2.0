package application.mdpar.representation.v1;

import java.util.List;
import java.util.Objects;

public record TacticalSquadRepresentationV1(String squadId, String forceId, String mission, String compositionKind,
                                      List<String> memberActorIds, boolean ownSquad, EpistemicStateV1 knowledgeState) {
    public TacticalSquadRepresentationV1 {
        squadId=req(squadId);forceId=req(forceId);mission=req(mission);compositionKind=req(compositionKind);
        memberActorIds=List.copyOf(Objects.requireNonNull(memberActorIds)); Objects.requireNonNull(knowledgeState);
        if(memberActorIds.isEmpty()||memberActorIds.size()>10)throw new IllegalArgumentException("Escuadrón fuera de 1..10.");
    }
    private static String req(String s){Objects.requireNonNull(s);if(s.isBlank())throw new IllegalArgumentException("Texto obligatorio.");return s;}
}
