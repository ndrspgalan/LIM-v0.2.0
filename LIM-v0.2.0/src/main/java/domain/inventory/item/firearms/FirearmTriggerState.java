package domain.inventory.item.firearms;

/** Estado de una única pulsación sostenida de RIGHT CLICK. */
public final class FirearmTriggerState {
    private boolean pressed;
    private int shotsOnCurrentPress;

    public boolean pressed() { return pressed; }
    public int shotsOnCurrentPress() { return shotsOnCurrentPress; }

    public void press() {
        if (!pressed) {
            pressed = true;
            shotsOnCurrentPress = 0;
        }
    }

    public void registerShot() {
        if (!pressed) throw new IllegalStateException("No puede registrarse un disparo sin pulsación activa.");
        shotsOnCurrentPress++;
    }

    public void release() {
        pressed = false;
        shotsOnCurrentPress = 0;
    }
}
