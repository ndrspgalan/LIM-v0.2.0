package domain.animation;

/** Estado semántico y ejecutable de la animación canónica de DESCANSAR. */
public final class CharacterAnimationState {
    private CharacterPosture posture = CharacterPosture.STANDING;
    private CanonicalAnimation currentAnimation = CanonicalAnimation.IDLE;

    public CharacterPosture posture() { return posture; }
    public CanonicalAnimation currentAnimation() { return currentAnimation; }
    public boolean isResting() { return posture == CharacterPosture.RESTING; }

    public void enterRest() {
        currentAnimation = CanonicalAnimation.ENTER_REST;
        posture = CharacterPosture.RESTING;
        currentAnimation = CanonicalAnimation.REST_LOOP;
    }

    public void resumeRestLoop() {
        posture = CharacterPosture.RESTING;
        currentAnimation = CanonicalAnimation.REST_LOOP;
    }

    /** Animación automática de subir un cubrecuellos: ENMASCARAR. */
    public void mask() {
        currentAnimation = CanonicalAnimation.MASK;
    }

    /** Animación deliberada de consulta del astrolabio: ORIENTARSE. */
    public void orient() {
        if (isResting()) throw new IllegalStateException("No puede orientarse durante DESCANSAR.");
        currentAnimation = CanonicalAnimation.ORIENT;
    }

    public void completeOrientation() {
        if (currentAnimation == CanonicalAnimation.ORIENT) currentAnimation = CanonicalAnimation.IDLE;
    }

    public void completeMasking() {
        if (currentAnimation == CanonicalAnimation.MASK) currentAnimation = CanonicalAnimation.IDLE;
    }

    public void exitRest() {
        if (!isResting()) return;
        currentAnimation = CanonicalAnimation.EXIT_REST;
        posture = CharacterPosture.STANDING;
        currentAnimation = CanonicalAnimation.IDLE;
    }
}
