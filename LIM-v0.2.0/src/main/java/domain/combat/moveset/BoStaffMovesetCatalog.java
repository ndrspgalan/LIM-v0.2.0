package domain.combat.moveset;

import domain.inventory.item.*;
import java.util.*;

/**  — Bō: acciones unitarias por agarre y grafo cinético completo entre 2H/1H. */
public final class BoStaffMovesetCatalog {
    private BoStaffMovesetCatalog() {}

    public static MeleeMovesetProfile twoHanded() {
        var l = m("2L", WeaponCombatAction.LIGHT_ATTACK, 1,
                "Bō centrado a dos manos, extremo derecho ligeramente adelantado",
                "Barrido horizontal/diagonal rápido que desliza las manos y deja presentado el extremo opuesto",
                "Extremo distal", "Extremo contrario adelantado al lado izquierdo", BodyAdvance.NONE);
        var h = m("2H", WeaponCombatAction.HEAVY_ATTACK, 0,
                "Guardia alta bimanual",
                "Golpe descendente con toda la palanca del asta y caída corta del centro corporal",
                "Extremo distal", "Bō bajo y centrado delante del cuerpo", BodyAdvance.SLIGHT);
        var c = m("2C", WeaponCombatAction.CHARGED_ATTACK, 0,
                "Bō transversal frente al torso",
                "Florecimiento bilateral corto por ambos costados que amenaza líneas alternas y descarga en un barrido amplio",
                "Extremo distal", "Bō extendido lateralmente tras el barrido", BodyAdvance.NONE);
        var j = m("2J", WeaponCombatAction.JUMP_ATTACK, 0,
                "Impulso bimanual con el asta recogida",
                "Salto corto y golpe descendente con uno de los extremos, usando la longitud sin arrastre frontal",
                "Extremo distal", "Bō bajo frente al cuerpo al aterrizar", BodyAdvance.SLIGHT);
        var ms = List.of(l,h,c,j);
        return new MeleeMovesetProfile(ms, internal(ms, true));
    }

    public static MeleeMovesetProfile oneHanded() {
        var l = m("1L", WeaponCombatAction.LIGHT_ATTACK, 1,
                "Bō lateral a una mano con el extremo distal cargado",
                "Revés rápido de muñeca, codo y hombro que hace cruzar el extremo distal sin comprometer el tronco",
                "Extremo distal", "Brazo extendido al lado contrario", BodyAdvance.NONE);
        var h = m("1H", WeaponCombatAction.HEAVY_ATTACK, 0,
                "Bō abierto a una mano",
                "Barrido monomanual de máxima extensión que usa toda la longitud como palanca y sacrifica recuperación",
                "Extremo distal", "Brazo completamente extendido y asta fuera de línea", BodyAdvance.SLIGHT);
        var c = m("1C", WeaponCombatAction.CHARGED_ATTACK, 0,
                "Bō colgado lateralmente de la mano dominante",
                "Moulinet compacto de muñeca/antebrazo con amenaza visual cambiante y descarga en golpe cruzado",
                "Extremo distal", "Asta cruzada delante del torso", BodyAdvance.NONE);
        var j = m("1J", WeaponCombatAction.JUMP_ATTACK, 0,
                "Bō recogido a un costado",
                "Salto corto con giro de cadera y golpe lateral-descendente rápido, sin gran pirueta corporal",
                "Extremo distal", "Asta baja al costado al aterrizar", BodyAdvance.SLIGHT);
        var ms = List.of(l,h,c,j);
        return new MeleeMovesetProfile(ms, internal(ms, false));
    }

    private static List<MeleeAttackTransition> internal(List<MeleeAttackMotion> ms, boolean twoHanded) {
        List<MeleeAttackTransition> out = new ArrayList<>();
        for (var a : ms) for (var b : ms) if (a != b) {
            var q = twoHanded ? q2(a.id(), b.id()) : q1(a.id(), b.id());
            out.add(new MeleeAttackTransition(a.id(), b.id(), q,
                    "Calidad derivada de la orientación final del asta, mano disponible, altura del extremo activo y recolocación de pies."));
        }
        return out;
    }

    private static TransitionContinuity q2(String a, String b) {
        if ((a.equals("2L") && b.equals("2C")) || (a.equals("2C") && b.equals("2L"))) return TransitionContinuity.EXCELLENT;
        if ((a.equals("2L") && b.equals("2H")) || (a.equals("2J") && b.equals("2L")) || (a.equals("2H") && b.equals("2C"))) return TransitionContinuity.NATURAL;
        if ((a.equals("2H") && b.equals("2J")) || (a.equals("2J") && b.equals("2H"))) return TransitionContinuity.FORCED;
        return TransitionContinuity.NEUTRAL;
    }

    private static TransitionContinuity q1(String a, String b) {
        if ((a.equals("1L") && b.equals("1C")) || (a.equals("1C") && b.equals("1L"))) return TransitionContinuity.EXCELLENT;
        if ((a.equals("1C") && b.equals("1J")) || (a.equals("1J") && b.equals("1L"))) return TransitionContinuity.NATURAL;
        if ((a.equals("1H") && b.equals("1L")) || (a.equals("1H") && b.equals("1C"))) return TransitionContinuity.FORCED;
        return TransitionContinuity.NEUTRAL;
    }

    public static CrossModeTransitionProfile crossMode() {
        var P = WeaponActionMode.PRIMARY; var A = WeaponActionMode.ALTERNATIVE;
        List<CrossModeAttackTransition> out = new ArrayList<>();
        String[] p={"2L","2H","2C","2J"}, a={"1L","1H","1C","1J"};
        for(String x:p) for(String y:a) out.add(t(P,x,A,y,cross(x,y)));
        for(String x:a) for(String y:p) out.add(t(A,x,P,y,cross(y,x)));
        return new CrossModeTransitionProfile(out);
    }

    private static TransitionContinuity cross(String two, String one) {
        // Ruta de MOUSE WHEEL especialmente fluida del Bō: usa entrega de mano y simetría del asta, no inercia de cabeza.
        if ((two.equals("2J")&&one.equals("1L")) ||
            (two.equals("2H")&&one.equals("1C")) ||
            (two.equals("2L")&&one.equals("1J")) ||
            (two.equals("2C")&&one.equals("1H"))) return TransitionContinuity.EXCELLENT;
        if ((two.equals("2L")&&one.equals("1L")) ||
            (two.equals("2H")&&one.equals("1L")) ||
            (two.equals("2C")&&one.equals("1J")) ||
            (two.equals("2J")&&one.equals("1C"))) return TransitionContinuity.NATURAL;
        if ((two.equals("2H")&&one.equals("1H")) ||
            (two.equals("2J")&&one.equals("1H"))) return TransitionContinuity.FORCED;
        return TransitionContinuity.NEUTRAL;
    }

    private static CrossModeAttackTransition t(WeaponActionMode fm,String f,WeaponActionMode tm,String to,TransitionContinuity q){
        return new CrossModeAttackTransition(new ModeAttackRef(fm,f),new ModeAttackRef(tm,to),q,
                "MOUSE WHEEL permite cambiar de agarre durante la recuperación; la calidad depende de entrega de mano, simetría del asta y apoyo corporal.");
    }

    private static MeleeAttackMotion m(String id,WeaponCombatAction a,int o,String s,String tr,String surf,String e,BodyAdvance ba){
        return new MeleeAttackMotion(id,a,o,s,tr,surf,e,ba);
    }
}
