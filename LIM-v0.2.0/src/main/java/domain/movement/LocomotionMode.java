package domain.movement;

/**
 * Modos discretos de desplazamiento. La velocidad concreta pertenece al motor de
 * movimiento; el dominio decide únicamente qué modos permite la pendiente.
 */
public enum LocomotionMode {
    RUNNING("Correr"),
    TROTTING("Trotar"),
    WALKING("Caminar"),
    CROUCH_WALKING("Caminar agachado"),
    CRAWLING("Gatear"),
    CLIMBING("Escalar"),
    SWIMMING("Nadar"),
    FAST_SWIMMING("Nadar con brazadas"),
    DIVING("Bucear");

    private final String label;

    LocomotionMode(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
