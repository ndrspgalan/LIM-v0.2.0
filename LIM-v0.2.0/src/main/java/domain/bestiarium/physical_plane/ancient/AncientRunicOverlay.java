package domain.bestiarium.physical_plane.ancient;

import domain.bestiarium.physical_plane.shapeshift.CambiaformasRunicOverlay;
import domain.runic.RunicMarkId;

/** Adaptador ANCIENT sobre la política común CAMBIAFORMAS de ASPIRANT/ANCIENT. */
public final class AncientRunicOverlay {
    private final CambiaformasRunicOverlay overlay;
    public AncientRunicOverlay(RunicMarkId originalMark){overlay=new CambiaformasRunicOverlay(originalMark);}
    public RunicMarkId originalMark(){return overlay.originalMark();}
    public RunicMarkId effectiveMark(){return overlay.effectiveMark();}
    public AncientForm form(){return overlay.changed()?AncientForm.CAMBIAFORMAS:AncientForm.HUMANA;}
    public boolean originalEffectsSuppressed(){return overlay.originalEffectsSuppressed();}
    public boolean shapeshiftActive(){return overlay.changed();}
    public AncientForm toggleByMouseWheelClick(){overlay.toggleByMouseWheelClick(); return form();}
    public String narrativeDescription(){
        return "CAMBIAFORMAS no sustituyó la geometría antigua borrándola: creció sobre ella. La marca original sigue " +
                "siendo reconocible bajo la nueva trama, pero ha dejado de gobernar el cuerpo. La superposición la " +
                "sobrescribe funcionalmente y convierte MOUSE WHEEL en el gesto de transición entre forma humana y " +
                "forma cambiada; ninguna de las dos reactiva los efectos de la marca precedente.";
    }
}
