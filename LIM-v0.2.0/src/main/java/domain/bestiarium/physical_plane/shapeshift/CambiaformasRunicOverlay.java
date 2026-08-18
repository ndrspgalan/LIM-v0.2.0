package domain.bestiarium.physical_plane.shapeshift;

import domain.runic.RunicMarkId;
import java.util.Objects;

/**
 * Política común  para ASPIRANT/ANCIENT: CAMBIAFORMAS crece superpuesta a la
 * marca rúnica original. La geometría precedente permanece como procedencia, pero sus
 * efectos quedan sobrescritos en ambas formas. MOUSE WHEEL alterna la expresión corporal.
 */
public final class CambiaformasRunicOverlay {
    private final RunicMarkId originalMark;
    private boolean changed;

    public CambiaformasRunicOverlay(RunicMarkId originalMark) {
        this.originalMark=Objects.requireNonNull(originalMark,"La marca original no puede ser nula.");
        if(originalMark==RunicMarkId.CAMBIAFORMAS) throw new IllegalArgumentException("CAMBIAFORMAS no puede ser su propia procedencia.");
    }
    public RunicMarkId originalMark(){return originalMark;}
    public RunicMarkId effectiveMark(){return RunicMarkId.CAMBIAFORMAS;}
    public boolean originalEffectsSuppressed(){return true;}
    public boolean changed(){return changed;}
    public boolean toggleByMouseWheelClick(){changed=!changed; return changed;}
}
