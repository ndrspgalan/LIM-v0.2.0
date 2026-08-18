package application.mdpar.representation.v1;

import java.util.List;
import java.util.Objects;

/** Acción conocida por el actor: legal ahora o conocida pero bloqueada, nunca puntuada por LIM. */
public record ActionRepresentationV1(String actionId,String family,ActionAvailabilityV1 availability,
                               ActionTargetKindV1 targetKind,List<String>blockingReasons,List<KnowledgeFactV1>facts){
 public ActionRepresentationV1{actionId=req(actionId);family=req(family);Objects.requireNonNull(availability);Objects.requireNonNull(targetKind);blockingReasons=List.copyOf(Objects.requireNonNull(blockingReasons));facts=List.copyOf(Objects.requireNonNull(facts));}
 private static String req(String s){Objects.requireNonNull(s);if(s.isBlank())throw new IllegalArgumentException("Texto obligatorio.");return s;}
}
