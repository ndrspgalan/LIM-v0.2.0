package domain.combat.moveset;

import domain.inventory.item.WeaponCombatAction;
import java.util.List;

/**  — movesets internos del Hacha de Leñador, Martillo de bola y Maza Electro-mecánica V881. */
public final class BluntToolMovesetCatalog {
    private BluntToolMovesetCatalog(){}

    public static MeleeMovesetProfile hachaDeLenador(){
        var l1=m("L1",1,"Hacha alta junto al hombro derecho",
                "Corte diagonal descendente derecha→izquierda con el único filo funcional",
                "Filo","Cabeza baja a la izquierda",BodyAdvance.SLIGHT);
        var l2=m("L2",2,"Cabeza baja a la izquierda",
                "Recuperación circular inferior/exterior que vuelve a presentar el mismo filo y desemboca en corte ascendente izquierda→derecha",
                "Mismo filo","Cabeza alta a la derecha",BodyAdvance.SLIGHT);
        var l3=m("L3",3,"Cabeza alta a la derecha",
                "Corte aproximadamente horizontal derecha→izquierda manteniendo el mismo filo presentado",
                "Mismo filo","Cabeza lateral izquierda",BodyAdvance.SLIGHT);
        var l4=m("L4",4,"Cabeza lateral izquierda",
                "Recuperación circular que reorienta el único filo y descarga una caída diagonal/vertical",
                "Mismo filo","Recuperación próxima al hombro derecho",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La recuperación obligatoria por disponer de un solo filo se convierte en el ataque ascendente."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"La salida alta derecha deja el único filo preparado para el transversal."),
                t("L3","L4",TransitionContinuity.NATURAL,"La recuperación circular vuelve a presentar el filo sin neutralizar el arma."),
                t("L4","L1",TransitionContinuity.NATURAL,"La caída termina reconstruyendo la posición alta de inicio.")));
    }

    public static MeleeMovesetProfile martilloDeBola(){
        var l1=m("L1",1,"Martillo junto al hombro derecho",
                "Martillazo diagonal corto derecha→izquierda",
                "Cara plana","Cabeza baja/centrada",BodyAdvance.NONE);
        var l2=m("L2",2,"Cabeza baja/centrada",
                "Revés corto izquierda→derecha mediante rotación de antebrazo",
                "Bola","Cabeza alta a la derecha",BodyAdvance.NONE);
        var l3=m("L3",3,"Cabeza alta a la derecha",
                "Golpe horizontal compacto derecha→izquierda tras volver a presentar la cara plana",
                "Cara plana","Cabeza lateral izquierda",BodyAdvance.SLIGHT);
        var l4=m("L4",4,"Cabeza lateral izquierda",
                "Pequeño círculo exterior y martillazo vertical corto",
                "Bola","Recuperación junto al hombro derecho",BodyAdvance.SLIGHT);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La corta palanca permite invertir cara plana→bola durante el propio retorno."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"La salida alta derecha permite presentar de nuevo la cara plana sin pausa."),
                t("L3","L4",TransitionContinuity.NATURAL,"El lateral izquierdo alimenta el círculo corto que coloca la bola arriba."),
                t("L4","L1",TransitionContinuity.NATURAL,"El descenso recupera el martillo a su posición inicial compacta.")));
    }

    public static MeleeMovesetProfile mazaElectroMecanica(){
        var l1=m("L1",1,"Maza junto al hombro derecho",
                "Golpe diagonal compacto derecha→izquierda",
                "Cabeza de la maza","Cabeza baja a la izquierda",BodyAdvance.SLIGHT);
        var l2=m("L2",2,"Cabeza baja a la izquierda",
                "Retorno ascendente izquierda→derecha convertido en impacto",
                "Cabeza de la maza","Cabeza alta a la derecha",BodyAdvance.NONE);
        var l3=m("L3",3,"Cabeza alta a la derecha",
                "Golpe descendente corto hacia la línea central",
                "Cabeza de la maza","Brazo recogido hacia el interior/izquierda",BodyAdvance.SLIGHT);
        var l4=m("L4",4,"Brazo recogido hacia el interior/izquierda",
                "Bofetada de revés amplia izquierda→derecha con extensión del brazo",
                "Cabeza de la maza","Brazo completamente extendido; maza orientada hacia el suelo ≈45°",BodyAdvance.COMMITTED);
        var h=heavy("H1","Brazo recogido hacia el interior/izquierda",
                "La misma bofetada de revés amplia de L4, resuelta como ataque fuerte monomanual",
                "Cabeza electro-mecánica","Brazo completamente extendido; maza orientada hacia el suelo ≈45°",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,h),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La salida baja alimenta directamente el retorno ascendente."),
                t("L2","L3",TransitionContinuity.NATURAL,"La cabeza alta permite una caída corta sin gran recolocación."),
                t("L3","L4",TransitionContinuity.EXCELLENT,"La posición interior carga de forma natural la bofetada de revés ligera."),
                t("L3","H1",TransitionContinuity.EXCELLENT,"HEAVY 1 sustituye cinéticamente a L4 con exactamente la misma trayectoria; si ocupa legítimamente ese ordinal, se resuelve como finisher sin sumar multiplicadores."),
                t("L4","L1",TransitionContinuity.NATURAL,"La recuperación inferior devuelve la maza al hombro derecho."),
                t("H1","L1",TransitionContinuity.NATURAL,"El fuerte comparte la salida de L4 y por ello comparte también su recuperación.")));
    }

    private static MeleeAttackMotion m(String id,int ordinal,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.LIGHT_ATTACK,ordinal,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackMotion heavy(String id,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.HEAVY_ATTACK,0,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackTransition t(String from,String to,TransitionContinuity c,String why){return new MeleeAttackTransition(from,to,c,why);}
}
