package application.mdpar.serialization.v1;

public final class WireValidationExceptionV1 extends IllegalArgumentException {
    public WireValidationExceptionV1(String message) { super(message); }
    public WireValidationExceptionV1(String message, Throwable cause) { super(message, cause); }
}
