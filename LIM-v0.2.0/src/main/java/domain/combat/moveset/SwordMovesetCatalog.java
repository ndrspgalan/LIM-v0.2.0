package domain.combat.moveset;

import domain.inventory.item.WeaponCombatAction;
import java.util.List;

/**  — movesets internos de Cimitarra, Espada Helicoidal y Katana Termo-mecánica V881. */
public final class SwordMovesetCatalog {
    private SwordMovesetCatalog() {}

    public static MeleeMovesetProfile cimitarra() {
        var l1 = light("L1",1,"Hoja alta a la derecha",
                "Corte diagonal descendente derecha→izquierda conservando la circulación de la hoja curva",
                "Filo único","Hoja baja a la izquierda",BodyAdvance.SLIGHT);
        var l2 = light("L2",2,"Hoja baja a la izquierda",
                "Retorno ascendente izquierda→derecha convertido en corte continuo",
                "Filo único","Hoja alta a la derecha",BodyAdvance.SLIGHT);
        var l3 = light("L3",3,"Hoja alta a la derecha",
                "Barrido horizontal derecha→izquierda a la línea del torso",
                "Filo único","Hoja lateral izquierda",BodyAdvance.SLIGHT);
        var l4 = light("L4",4,"Hoja lateral izquierda",
                "Recuperación circular inferior y corte ascendente izquierda→derecha de mayor recorrido",
                "Filo único","Hoja alta a la derecha",BodyAdvance.SLIGHT);
        var l5 = light("L5",5,"Hoja alta a la derecha",
                "Corte diagonal descendente profundo derecha→izquierda con avance corporal corto",
                "Filo único","Salida baja que circula de nuevo hacia la guardia inicial",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,l5),List.of(
                transition("L1","L2",TransitionContinuity.EXCELLENT,"La salida baja alimenta directamente el corte ascendente."),
                transition("L2","L3",TransitionContinuity.EXCELLENT,"La hoja termina alta a la derecha y entra sin pausa en el barrido."),
                transition("L3","L4",TransitionContinuity.EXCELLENT,"La continuidad circular evita frenar la hoja curva."),
                transition("L4","L5",TransitionContinuity.EXCELLENT,"La salida alta carga inmediatamente el diagonal profundo."),
                transition("L5","L1",TransitionContinuity.EXCELLENT,"La recuperación conserva el giro y reconstruye la entrada del primer corte.")));
    }

    public static MeleeMovesetProfile espadaHelicoidal() {
        var l1 = light("L1",1,"Guardia alta derecha",
                "Diagonal descendente derecha→izquierda con paso corto",
                "Filo/plano helicoidal","Hoja baja a la izquierda",BodyAdvance.SLIGHT);
        var l2 = light("L2",2,"Hoja baja a la izquierda",
                "Diagonal ascendente izquierda→derecha haciendo viajar una gran sección de hoja por el frente",
                "Filo/plano helicoidal","Hoja alta a la derecha",BodyAdvance.SLIGHT);
        var l3 = light("L3",3,"Hoja alta a la derecha",
                "Corte transversal derecha→izquierda a la altura del torso",
                "Filo/plano helicoidal","Hoja lateral izquierda",BodyAdvance.SLIGHT);
        var l4 = light("L4",4,"Hoja lateral izquierda",
                "Diagonal ascendente izquierda→derecha que eleva manos y espada hacia guardia alta",
                "Filo/plano helicoidal","Guardia alta preparada",BodyAdvance.SLIGHT);
        var h1 = heavy("H1","Guardia alta preparada",
                "Descarga desde guardia alta mediante corte descendente amplio y controlado",
                "Filo/plano helicoidal","Hoja baja frontal",BodyAdvance.COMMITTED);
        var cLr = charged("C-LR","Floritura helicoidal indefinida y vistosa, alternando visualmente la espada entre manos; salida situada al lado izquierdo",
                "Liberación de la floritura en un swing lateral muy amplio izquierda→derecha",
                "Filo/plano helicoidal","Hoja extendida al lado derecho",BodyAdvance.COMMITTED);
        var cRl = charged("C-RL","Floritura helicoidal indefinida y vistosa, alternando visualmente la espada entre manos; salida situada al lado derecho",
                "Liberación de la floritura en un swing lateral muy amplio derecha→izquierda",
                "Filo/plano helicoidal","Hoja extendida al lado izquierdo",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,h1,cLr,cRl),List.of(
                transition("L1","L2",TransitionContinuity.EXCELLENT,"La salida baja alimenta la diagonal ascendente."),
                transition("L2","L3",TransitionContinuity.NATURAL,"La posición alta derecha permite abrir el plano transversal."),
                transition("L3","L4",TransitionContinuity.EXCELLENT,"El lateral izquierdo se convierte en elevación hacia guardia alta."),
                transition("L4","L1",TransitionContinuity.NATURAL,"La guardia alta puede reiniciar el ciclo ligero."),
                transition("L4","H1",TransitionContinuity.EXCELLENT,"L4 termina exactamente en la guardia alta requerida por el fuerte."),
                transition("L2","H1",TransitionContinuity.NATURAL,"La salida alta derecha admite elevar las manos y descargar desde arriba."),
                transition("C-LR","L1",TransitionContinuity.NATURAL,"La salida derecha puede reciclarse hacia la entrada diagonal del combo."),
                transition("C-RL","L3",TransitionContinuity.NATURAL,"La salida izquierda alimenta el transversal desde el lado contrario.")));
    }

    public static MeleeMovesetProfile katanaTermoMecanica() {
        var l1 = light("L1",1,"Guardia alta/derecha compacta",
                "Corte diagonal descendente derecha→izquierda con recorrido limpio",
                "Filo","Hoja baja a la izquierda",BodyAdvance.SLIGHT);
        var l2 = light("L2",2,"Hoja baja a la izquierda",
                "Corte diagonal ascendente izquierda→derecha con recuperación ajustada",
                "Filo","Hoja alta a la derecha",BodyAdvance.SLIGHT);
        var l3 = light("L3",3,"Hoja alta a la derecha",
                "Diagonal descendente izquierda→derecha tras cambiar el plano corporal",
                "Filo","Hoja baja/lateral derecha",BodyAdvance.SLIGHT);
        var l4 = light("L4",4,"Hoja baja/lateral derecha",
                "Corte horizontal derecha→izquierda y recuperación contenida hacia la línea de saya",
                "Filo","Posición recuperable hacia envainado",BodyAdvance.SLIGHT);
        var h1 = heavy("H1","Katana envainada; manos preparadas en empuñadura y saya",
                "Desenvaine agresivo longitudinal: la hoja sale hacia el rival hasta terminar apuntándolo; torso y pies se inclinan hacia delante para maximizar alcance por inercia",
                "Filo y punta durante el desenvaine","Katana completamente extendida apuntando al rival",BodyAdvance.COMMITTED);
        var c1 = charged("C1","Espera indefinida en saya mientras se mantiene la carga",
                "Al liberar, ejecuta exactamente el mismo desenvaine agresivo longitudinal de H1, con máxima extensión hacia el rival",
                "Filo y punta durante el desenvaine","Katana completamente extendida apuntando al rival",BodyAdvance.COMMITTED);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,h1,c1),List.of(
                transition("L1","L2",TransitionContinuity.EXCELLENT,"La salida baja permite convertir la recuperación en ascendente."),
                transition("L2","L3",TransitionContinuity.NATURAL,"El cambio de lado exige reordenar el plano sin volver a neutral."),
                transition("L3","L4",TransitionContinuity.EXCELLENT,"La salida lateral derecha alimenta el horizontal de cierre."),
                transition("L4","L1",TransitionContinuity.NATURAL,"La recuperación compacta puede reconstruir la guardia inicial."),
                transition("H1","L2",TransitionContinuity.NATURAL,"La extensión longitudinal puede recuperarse hacia el ascendente del segundo ligero."),
                transition("C1","L2",TransitionContinuity.NATURAL,"CHARGED comparte exactamente la salida cinética de H1.")));
    }

    private static MeleeAttackMotion light(String id,int ordinal,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.LIGHT_ATTACK,ordinal,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackMotion heavy(String id,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.HEAVY_ATTACK,0,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackMotion charged(String id,String start,String trajectory,String surface,String end,BodyAdvance advance){
        return new MeleeAttackMotion(id,WeaponCombatAction.CHARGED_ATTACK,0,start,trajectory,surface,end,advance);
    }
    private static MeleeAttackTransition transition(String from,String to,TransitionContinuity continuity,String rationale){
        return new MeleeAttackTransition(from,to,continuity,rationale);
    }
}
