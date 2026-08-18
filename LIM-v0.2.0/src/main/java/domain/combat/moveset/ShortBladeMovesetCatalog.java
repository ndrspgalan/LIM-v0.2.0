package domain.combat.moveset;

import domain.inventory.item.WeaponCombatAction;
import java.util.List;

/**  — movesets internos del Cuchillo de Carnicero y los dos agarres de Daga. */
public final class ShortBladeMovesetCatalog {
    private ShortBladeMovesetCatalog(){}

    public static MeleeMovesetProfile cuchilloDeCarnicero(){
        var l1=m("L1",1,"Hoja alta junto al hombro derecho",
                "Corte diagonal descendente corto derecha→izquierda acompañado por hombro y tronco",
                "Filo pesado","Hoja baja a la izquierda",BodyAdvance.SLIGHT);
        var l2=m("L2",2,"Hoja baja a la izquierda",
                "Retorno oblicuo ascendente izquierda→derecha convertido en corte",
                "Filo","Hoja alta a la derecha",BodyAdvance.NONE);
        var l3=m("L3",3,"Hoja alta a la derecha",
                "Corte horizontal compacto derecha→izquierda con rotación corta del torso",
                "Filo","Hoja lateral a la izquierda",BodyAdvance.SLIGHT);
        var l4=m("L4",4,"Hoja lateral a la izquierda",
                "Pequeña recuperación circular exterior y corte descendente casi vertical",
                "Filo y masa adelantada de la hoja","Recuperación próxima al hombro derecho",BodyAdvance.SLIGHT);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"El retorno natural del primer corte se convierte directamente en el segundo."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"La salida alta derecha carga el transversal sin regresar a neutral."),
                t("L3","L4",TransitionContinuity.NATURAL,"El lateral izquierdo permite elevar la cuchilla mediante un círculo corto."),
                t("L4","L1",TransitionContinuity.NATURAL,"La recuperación del descendente devuelve la hoja al hombro derecho.")));
    }

    public static MeleeMovesetProfile dagaOscilatorio(){
        var l1=m("L1",1,"Guardia compacta derecha con punta adelantada",
                "Estocada directa mediante extensión corta y paso mínimo",
                "Punta","Punta penetrada/extendida sobre la línea central",BodyAdvance.SLIGHT);
        var l2=m("L2",2,"Brazo extendido sobre la línea central",
                "Extracción diagonal exterior convertida en cuchillada corta",
                "Filo","Hoja desplazada al lado izquierdo",BodyAdvance.NONE);
        var l3=m("L3",3,"Hoja desplazada al lado izquierdo",
                "Pronación corta y estocada oblicua de retorno hacia el centro",
                "Punta","Punta adelantada desde la línea contraria",BodyAdvance.SLIGHT);
        var l4=m("L4",4,"Punta adelantada desde la línea contraria",
                "Extracción con rotación de muñeca convertida en corte horizontal compacto",
                "Filo","Mano recogida a la derecha con punta recuperable",BodyAdvance.NONE);
        var l5=m("L5",5,"Mano recogida a la derecha",
                "Estocada profunda de cierre con transferencia corporal corta",
                "Punta","Brazo extendido, preparado para retirada a guardia inicial",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,l5),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La extracción de la estocada constituye ya la trayectoria cortante."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"El corte deja la punta en la línea contraria para reentrar sin pausa."),
                t("L3","L4",TransitionContinuity.EXCELLENT,"La segunda extracción vuelve a convertirse en corte."),
                t("L4","L5",TransitionContinuity.EXCELLENT,"La salida compacta derecha recoloca inmediatamente la punta para la estocada final."),
                t("L5","L1",TransitionContinuity.NATURAL,"La retirada del cierre reconstruye la guardia oscilatoria sin recorrido amplio.")));
    }

    public static MeleeMovesetProfile dagaInvertido(){
        var l1=m("L1",1,"Agarre invertido alto, codo derecho elevado",
                "Estocada descendente diagonal corta hacia la línea central",
                "Punta","Punta baja/central con codo descendiendo",BodyAdvance.SLIGHT);
        var l2=m("L2",2,"Punta baja/central",
                "Extracción ascendente convertida en corte con el agarre invertido",
                "Filo","Codo compacto y hoja elevada",BodyAdvance.NONE);
        var l3=m("L3",3,"Codo compacto y hoja elevada",
                "Revés diagonal mediante rotación corta del antebrazo",
                "Filo","Brazo cruzado y codo nuevamente cargable arriba",BodyAdvance.NONE);
        var l4=m("L4",4,"Brazo cruzado con codo cargado",
                "Segunda caída de punta, más profunda, acompañada por hombro",
                "Punta","Agarre invertido bajo, recuperable hacia posición alta",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4),List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La propia extracción de la punta genera el corte ascendente."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"La elevación compacta permite cambiar inmediatamente la diagonal."),
                t("L3","L4",TransitionContinuity.EXCELLENT,"El revés vuelve a cargar el codo para una caída de punta."),
                t("L4","L1",TransitionContinuity.NATURAL,"La retirada vertical reconstruye el ataque alto invertido.")));
    }

    private static MeleeAttackMotion m(String id,int ordinal,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.LIGHT_ATTACK,ordinal,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackTransition t(String from,String to,TransitionContinuity c,String why){
        return new MeleeAttackTransition(from,to,c,why);
    }
}
