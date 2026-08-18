package domain.combat.moveset;

import domain.inventory.item.WeaponCombatAction;
import java.util.List;

/**  — movesets canónicos internos para Hoz, Guadaña y Boathook. */
public final class HookedPolearmMovesetCatalog {
    private HookedPolearmMovesetCatalog() {}

    public static MeleeMovesetProfile hoz() {
        var l1 = m("L1", WeaponCombatAction.LIGHT_ATTACK, 1,
                "Hoz alta junto al lado derecho", "Corte diagonal entrante derecha→izquierda hacia cuello/torso",
                "Filo interior curvo", "Hoz baja a la izquierda", BodyAdvance.SLIGHT);
        var l2 = m("L2", WeaponCombatAction.LIGHT_ATTACK, 2,
                "Hoz baja a la izquierda", "Retirada convertida en draw cut ascendente izquierda→derecha sin volver a neutral",
                "Filo interior curvo", "Hoz alta/centrada a la derecha", BodyAdvance.NONE);
        var l3 = m("L3", WeaponCombatAction.LIGHT_ATTACK, 3,
                "Hoz alta/centrada", "Gancho ofensivo corto ascendente y tirón inmediato sin activar todavía la mecánica HEAVY de enganche",
                "Punta y curva interior", "Hoz recogida cerca del torso", BodyAdvance.SLIGHT);
        var h = m("H1", WeaponCombatAction.HEAVY_ATTACK, 0,
                "Hoz recogida con curva preparada", "Entrada deliberada de la curva sobre extremidad o línea corporal y tracción hacia el usuario",
                "Gancho interior", "Brazo retraído con adversario atraído si hubo daño perforante real", BodyAdvance.COMMITTED);
        var j = m("J1", WeaponCombatAction.JUMP_ATTACK, 0,
                "Impulso con hoz elevada", "Corte descendente oblicuo durante la caída",
                "Filo interior", "Hoz baja al aterrizar", BodyAdvance.COMMITTED);
        var d = m("D1", WeaponCombatAction.DESTABILIZE, 0,
                "Guardia corta", "Golpe de hombro general manteniendo la hoz fuera de la trayectoria corporal",
                "Hombro", "Guardia corta recuperada", BodyAdvance.SLIGHT);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,h,j,d), List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"La salida baja convierte la propia recuperación en draw cut."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"El ascendente deja la curva preparada para un gancho corto."),
                t("L3","L1",TransitionContinuity.NATURAL,"El tirón recoge el arma y permite reconstruir la línea alta."),
                t("L3","H1",TransitionContinuity.EXCELLENT,"La curva ya está orientada para profundizar el gancho en vez de recuperarlo."),
                t("H1","L1",TransitionContinuity.NATURAL,"La tracción termina con la hoz recogida y lista para el diagonal de entrada.")));
    }

    public static MeleeMovesetProfile guadana() {
        var l1 = m("L1", WeaponCombatAction.LIGHT_ATTACK, 1,
                "Guadaña cargada al lado derecho", "Barrido de siega medio derecha→izquierda con paso y rotación de cadera",
                "Filo de la hoja transversal", "Hoja extendida al lado izquierdo", BodyAdvance.SLIGHT);
        var l2 = m("L2", WeaponCombatAction.LIGHT_ATTACK, 2,
                "Hoja extendida a la izquierda", "Recuperación continua alrededor/detrás de la cabeza y corte alto desde el plano contrario",
                "Filo de la hoja transversal", "Hoja alta al lado derecho", BodyAdvance.SLIGHT);
        var l3 = m("L3", WeaponCombatAction.LIGHT_ATTACK, 3,
                "Hoja alta derecha", "Cambio de nivel y corte bajo ascendente que hace viajar el asta con cadera y paso",
                "Filo de la hoja transversal", "Hoja elevada al lado izquierdo", BodyAdvance.SLIGHT);
        var l4 = m("L4", WeaponCombatAction.LIGHT_ATTACK, 4,
                "Hoja elevada a la izquierda", "Corte descendente hacia cuello/hombro con recuperación abierta",
                "Filo de la hoja transversal", "Hoja baja y exterior, recuperable hacia la siega media", BodyAdvance.COMMITTED);
        var h = m("H1", WeaponCombatAction.HEAVY_ATTACK, 0,
                "Asta lateral con hoja preparada", "La hoja entra por el lateral de cuello/torso y se retrae mediante una tracción de cuerpo completo",
                "Gancho formado por hoja y asta", "Guadaña recogida tras la tracción", BodyAdvance.COMMITTED);
        var c = m("C1", WeaponCombatAction.CHARGED_ATTACK, 0,
                "Preparación amplia", "Gran moulinet de recolocación alrededor del cuerpo y detrás de la cabeza que desemboca en un barrido de siega de radio máximo",
                "Filo de la hoja transversal", "Hoja extendida tras el barrido", BodyAdvance.COMMITTED);
        var j = m("J1", WeaponCombatAction.JUMP_ATTACK, 0,
                "Impulso con asta alta", "Corte descendente oblicuo durante el salto, guiando la hoja transversal hacia la línea superior",
                "Filo de la hoja transversal", "Hoja baja al aterrizar", BodyAdvance.COMMITTED);
        var d = m("D1", WeaponCombatAction.DESTABILIZE, 0,
                "Guardia con ambas manos en el asta", "Patada frontal para abrir distancia sin abandonar el control bimanual de la guadaña",
                "Planta/talón del pie", "Guardia bimanual recuperada", BodyAdvance.SLIGHT);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,l4,h,c,j,d), List.of(
                t("L1","L2",TransitionContinuity.EXCELLENT,"El gran barrido puede continuar detrás de la cabeza sin frenar la hoja."),
                t("L2","L3",TransitionContinuity.NATURAL,"El cambio desde plano alto a línea baja exige descenso corporal pero conserva circulación."),
                t("L3","L4",TransitionContinuity.EXCELLENT,"El ascendente deja la hoja elevada para el descendente."),
                t("L4","L1",TransitionContinuity.NATURAL,"La salida baja/exterior necesita recolocar el asta antes de recuperar la siega media."),
                t("L1","H1",TransitionContinuity.NATURAL,"El barrido lateral puede cerrarse sobre el cuerpo para convertir la hoja en gancho."),
                t("H1","L3",TransitionContinuity.NATURAL,"La tracción recoge el asta y permite reabrirla desde una línea baja."),
                t("L2","C1",TransitionContinuity.EXCELLENT,"La recuperación alrededor de la cabeza coincide con el inicio del moulinet cargado."),
                t("C1","L1",TransitionContinuity.NATURAL,"El gran barrido cargado termina en una salida equivalente a la siega media.")));
    }

    public static MeleeMovesetProfile boathook() {
        var l1 = m("L1", WeaponCombatAction.LIGHT_ATTACK, 1,
                "Boathook retraído frente al torso", "Empuje longitudinal de ambas manos con la cabeza del boathook",
                "Cabeza/gancho contundente", "Cabeza extendida frente al usuario", BodyAdvance.SLIGHT);
        var l2 = m("L2", WeaponCombatAction.LIGHT_ATTACK, 2,
                "Cabeza extendida", "Recuperación hacia el costado y barrido transversal con giro de cadera",
                "Cabeza/gancho contundente", "Cabeza desplazada al lateral", BodyAdvance.SLIGHT);
        var l3 = m("L3", WeaponCombatAction.LIGHT_ATTACK, 3,
                "Cabeza alejada al lateral", "El extremo posterior del asta entra en corto en vez de devolver gratuitamente la cabeza",
                "Culata del asta", "Asta centrada y preparada para volver a proyectar la cabeza", BodyAdvance.NONE);
        var h = m("H1", WeaponCombatAction.HEAVY_ATTACK, 0,
                "Cabeza próxima a la línea rival", "Entrada del gancho seguida de tracción bimanual hacia el usuario",
                "Gancho contundente", "Cabeza recogida tras arrastre", BodyAdvance.COMMITTED);
        var j = m("J1", WeaponCombatAction.JUMP_ATTACK, 0,
                "Impulso con cabeza elevada", "Entrada descendente contundente con la cabeza del boathook",
                "Cabeza/gancho contundente", "Cabeza baja al aterrizar", BodyAdvance.COMMITTED);
        var d = m("D1", WeaponCombatAction.DESTABILIZE, 0,
                "Guardia bimanual", "Patada frontal mientras ambas manos mantienen el asta fuera de la trayectoria de la pierna",
                "Planta/talón del pie", "Guardia bimanual recuperada", BodyAdvance.SLIGHT);
        return new MeleeMovesetProfile(List.of(l1,l2,l3,h,j,d), List.of(
                t("L1","L2",TransitionContinuity.NATURAL,"El empuje debe retirarse parcialmente antes de abrir el barrido lateral."),
                t("L2","L3",TransitionContinuity.EXCELLENT,"La cabeza queda lejos y convierte la culata próxima en el golpe de recuperación más económico."),
                t("L3","L1",TransitionContinuity.EXCELLENT,"El golpe de culata recentra el asta y carga el siguiente empuje."),
                t("L1","H1",TransitionContinuity.EXCELLENT,"Una entrada longitudinal que pasa junto al cuerpo puede deslizarse directamente al enganche."),
                t("L2","H1",TransitionContinuity.NATURAL,"El barrido necesita reorientar la boca del gancho antes de tirar."),
                t("H1","L3",TransitionContinuity.EXCELLENT,"La tracción acerca la culata al adversario y deja la cabeza ocupada en el arrastre.")));
    }

    private static MeleeAttackMotion m(String id, WeaponCombatAction action, int ordinal, String start, String trajectory,
                                       String surface, String end, BodyAdvance advance) {
        return new MeleeAttackMotion(id, action, ordinal, start, trajectory, surface, end, advance);
    }
    private static MeleeAttackTransition t(String from, String to, TransitionContinuity continuity, String why) {
        return new MeleeAttackTransition(from, to, continuity, why);
    }
}
