package domain.orientation;

/** Estado mínimo necesario para saber si una consulta deliberada del astrolabio es posible. */
public record MovementState(boolean mounted, boolean moving, boolean actionBlocked) {
    public static MovementState standingOnFoot() { return new MovementState(false, false, false); }
    public static MovementState standingMounted() { return new MovementState(true, false, false); }
    public static MovementState movingOnFoot() { return new MovementState(false, true, false); }
    public static MovementState movingMounted() { return new MovementState(true, true, false); }
    public boolean canOrient() { return !moving && !actionBlocked; }
}
