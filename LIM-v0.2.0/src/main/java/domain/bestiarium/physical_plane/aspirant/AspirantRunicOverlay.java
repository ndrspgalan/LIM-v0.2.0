package domain.bestiarium.physical_plane.aspirant;

import domain.bestiarium.physical_plane.shapeshift.CambiaformasRunicOverlay;
import domain.runic.RunicMarkId;

public final class AspirantRunicOverlay {
    private final CambiaformasRunicOverlay overlay;
    public AspirantRunicOverlay(RunicMarkId original){overlay=new CambiaformasRunicOverlay(original);}
    public RunicMarkId originalMark(){return overlay.originalMark();}
    public RunicMarkId effectiveMark(){return overlay.effectiveMark();}
    public boolean originalEffectsSuppressed(){return overlay.originalEffectsSuppressed();}
    public AspirantForm form(){return overlay.changed()?AspirantForm.CAMBIAFORMAS:AspirantForm.HUMANA;}
    public AspirantForm toggleByMouseWheelClick(){overlay.toggleByMouseWheelClick(); return form();}
}
