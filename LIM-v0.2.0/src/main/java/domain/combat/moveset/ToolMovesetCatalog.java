package domain.combat.moveset;

import domain.inventory.item.WeaponCombatAction;
import java.util.List;

/**  — movesets canónicos internos para Pico, Zapapico y Piqueta. */
public final class ToolMovesetCatalog {
    private ToolMovesetCatalog(){}

    public static MeleeMovesetProfile pico(){
        var l1=m("L1",1,"Hombro derecho / cabeza alta","Diagonal descendente derecha→izquierda","Pico estrecho","Cabeza baja a la izquierda",BodyAdvance.SLIGHT);
        var l2=m("L2",2,"Cabeza baja a la izquierda","Arco ascendente izquierda→derecha sin detener la cabeza","Borde ancho","Cabeza alta a la derecha",BodyAdvance.SLIGHT);
        var l3=m("L3",3,"Cabeza alta a la derecha","Revés horizontal derecha→izquierda","Cabeza lateral","Cabeza lateral izquierda",BodyAdvance.SLIGHT);
        var l4=m("L4",4,"Cabeza lateral izquierda","Recuperación circular y caída vertical","Pico estrecho","Bajo frontal, recuperable hacia hombro derecho",BodyAdvance.COMMITTED);
        var h=heavy("H","Hombro derecho / arma descansada","Swing de aproximadamente 180° con brazos extendidos y rotación corporal de izquierda a derecha","Cabeza del pico sobre el lateral derecho del torso rival","Rotación completada con avance corporal inevitable",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,h),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La salida baja izquierda alimenta directamente el arco ascendente."),
                t("L2","L3",TransitionContinuity.NATURAL,"La cabeza alta derecha conserva momento para el revés."),
                t("L3","L4",TransitionContinuity.EXCELLENT,"El lateral izquierdo permite elevar circularmente y descargar vertical."),
                t("L4","L1",TransitionContinuity.NATURAL,"La recuperación del golpe vertical devuelve la herramienta al hombro derecho."),
                t("L3","H",TransitionContinuity.EXCELLENT,"El revés ya inicia la rotación amplia requerida por el fuerte.")));
    }

    public static MeleeMovesetProfile zapapico(){
        var l1=m("L1",1,"Hombro derecho / cabeza alta","Diagonal descendente derecha→izquierda","Hoja de azada","Cabeza baja a la izquierda",BodyAdvance.SLIGHT);
        var l2=m("L2",2,"Cabeza baja a la izquierda","Retorno diagonal ascendente con inversión de cabeza","Pico","Cabeza alta a la derecha",BodyAdvance.SLIGHT);
        var l3=m("L3",3,"Cabeza alta a la derecha","Trayectoria oblicua ascendente/continua mediante nueva inversión","Hoja de azada","Cabeza elevada al lado izquierdo",BodyAdvance.SLIGHT);
        var l4=m("L4",4,"Cabeza elevada a la izquierda","Caída penetrante hacia delante","Pico","Bajo frontal, recuperable hacia hombro derecho",BodyAdvance.COMMITTED);
        var h=heavy("H","Hombro derecho / arma descansada","Swing de aproximadamente 180° con brazos extendidos y rotación corporal de izquierda a derecha","Hoja de azada sobre el lateral derecho del torso rival","Rotación completada con avance corporal inevitable",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,h),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La extracción del golpe de azada invierte naturalmente la cabeza y presenta el pico."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"El retorno con pico carga la cara de azada para continuar sin neutralizar."),
                t("L3","L4",TransitionContinuity.EXCELLENT,"La orientación alta de la cabeza deja el pico preparado para la caída."),
                t("L4","L1",TransitionContinuity.NATURAL,"La recuperación vuelve a presentar la azada desde el hombro derecho."),
                t("L2","H",TransitionContinuity.NATURAL,"El retorno ascendente permite ensanchar inmediatamente la rotación para el swing fuerte.")));
    }

    public static MeleeMovesetProfile piqueta(){
        var l1=m("L1",1,"Junto al hombro derecho","Golpe diagonal corto derecha→izquierda","Cara de martillo","Cabeza baja/centrada",BodyAdvance.NONE);
        var l2=m("L2",2,"Cabeza baja/centrada","Retorno corto con pronación/supinación de antebrazo","Cincel","Cabeza alta a la derecha",BodyAdvance.NONE);
        var l3=m("L3",3,"Cabeza alta a la derecha","Martillazo corto casi vertical","Cara de martillo","Recuperación junto al hombro derecho",BodyAdvance.SLIGHT);
        return new MeleeMovesetProfile(List.of(l1,l2,l3),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La masa compacta permite invertir martillo→cincel durante la propia recuperación."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"El retorno deja la cara de martillo elevada para una caída corta."),
                t("L3","L1",TransitionContinuity.EXCELLENT,"La recuperación vertical vuelve al origen sin recorrido muerto.")));
    }

    private static MeleeAttackMotion m(String id,int ordinal,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.LIGHT_ATTACK,ordinal,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackMotion heavy(String id,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.HEAVY_ATTACK,0,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackTransition t(String from,String to,TransitionContinuity c,String why){return new MeleeAttackTransition(from,to,c,why);}
}
