package application.mdpar.transport.v1;

import application.mdpar.boundary.v1.MdparRequestEnvelopeV1;
import application.mdpar.boundary.v1.MdparResponseEnvelopeV1;

import java.util.concurrent.CompletionStage;

/** Transporte agnóstico de dominio y tecnología. HTTP será una implementación, no la dependencia de LIM. */
public interface MdparTransportV1 {
    CompletionStage<MdparResponseEnvelopeV1> request(MdparRequestEnvelopeV1 request);
}
