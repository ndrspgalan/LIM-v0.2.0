package domain.save;
public final class CorruptSaveException extends RuntimeException { public CorruptSaveException(String message){super(message);} public CorruptSaveException(String message,Throwable cause){super(message,cause);} }
