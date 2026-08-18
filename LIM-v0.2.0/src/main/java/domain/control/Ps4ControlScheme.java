package domain.control;

import java.util.List;

/**
 * Esquema PS4 sincronizado con la gramática semántica de PC.
 * Las colisiones físicas son contextuales: el objeto equipado decide si un mismo botón
 * significa ataque cuerpo a cuerpo, puntería/disparo de firearm o manipulación energética.
 */
public final class Ps4ControlScheme {
    private Ps4ControlScheme() {}

    public static List<ControlBinding> canonicalBindings() {
        return List.of(
                new ControlBinding("STICK IZQUIERDO", InputGesture.PRESS_WITH_DIRECTION, ControlAction.MOVE, "Movimiento analógico"),
                new ControlBinding("L3", InputGesture.PRESS, ControlAction.DIVE, "EN AGUA: bucear; prevalece contextualmente sobre postura y consume 1 PA/s"),
                new ControlBinding("L3", InputGesture.PRESS, ControlAction.CYCLE_STANCE, "Trotar → Agacharse → Gatear → Trotar"),
                new ControlBinding("L3", InputGesture.HOLD, ControlAction.RUN, "Correr mientras se mantiene"),
                new ControlBinding("L3", InputGesture.DOUBLE_PRESS, ControlAction.WALK, "Alterna caminar mientras el contexto no resuelve carrera"),
                new ControlBinding("STICK DERECHO", InputGesture.PRESS_WITH_DIRECTION, ControlAction.MOVE_CAMERA, "Control de cámara"),
                new ControlBinding("R3", InputGesture.PRESS, ControlAction.LOCK_OR_UNLOCK_TARGET, "Fijación invisible a la altura del estómago"),

                new ControlBinding("CÍRCULO", InputGesture.PRESS, ControlAction.SLIDE, "MIENTRAS CORRE: deslizamiento frontal de 1,5 × altura; prevalece contextualmente sobre la finta"),
                new ControlBinding("CÍRCULO", InputGesture.PRESS_WITH_DIRECTION, ControlAction.FEINT, "Fuera de carrera: finta direccional"),
                new ControlBinding("X", InputGesture.PRESS, ControlAction.JUMP, "Salto"),
                new ControlBinding("R1", InputGesture.PRESS, ControlAction.LIGHT_ATTACK, "En tierra: ataque ligero cuando no se usa firearm"),
                new ControlBinding("R1", InputGesture.PRESS, ControlAction.JUMP_ATTACK, "En el aire: ataque con salto"),
                new ControlBinding("R2", InputGesture.PRESS, ControlAction.HEAVY_ATTACK, "Ataque fuerte contextual; la Maza Electro-mecánica resuelve aquí su descarga si está cargada"),
                new ControlBinding("R2", InputGesture.HOLD, ControlAction.CHARGED_ATTACK, "Ataque cargado a dos manos; en el Fusil de Repetición se contextualiza como carga con bayoneta"),
                new ControlBinding("L1", InputGesture.HOLD, ControlAction.BLOCK, "Si el arma lo permite"),
                new ControlBinding("L2", InputGesture.PRESS, ControlAction.PARRY, "PARRY o inicio de bloqueo cuando no se usa puntería firearm"),

                /* Firearms: equivalentes semánticos de LEFT/RIGHT CLICK, R y rueda de ratón. */
                new ControlBinding("L2", InputGesture.PRESS, ControlAction.TOGGLE_FIREARM_AIM,
                        "Solo firearms con AIMING; Lanza-Arcos, Rociador y arrojadizos no lo utilizan"),
                new ControlBinding("L2", InputGesture.PRESS, ControlAction.TOGGLE_RANGED_WEAPON_AIM,
                        "HONDA Y ARCOS: activa o desactiva AIMING"),
                new ControlBinding("R1", InputGesture.PRESS, ControlAction.FIRE_RANGED_WEAPON,
                        "HONDA Y ARCOS: disparo 1A; respeta recuperación y munición compatible"),
                new ControlBinding("R1", InputGesture.PRESS, ControlAction.FIRE_FIREARM,
                        "FIREARM: dispara/descarga según la plataforma"),
                new ControlBinding("R1", InputGesture.HOLD, ControlAction.FIRE_FIREARM,
                        "FIREARM: mantiene 3A, AA o rociado cuando la plataforma lo permita"),
                new ControlBinding("CUADRADO", InputGesture.PRESS, ControlAction.RELOAD_FIREARM,
                        "FIREARM: recarga; durante manipulación energética cancela y vuelve al manejo normal"),
                new ControlBinding("CUADRADO", InputGesture.HOLD, ControlAction.ENTER_PNEUMATIC_PRESSURIZATION,
                        "RIFLE NEUMÁTICO: entra en presurización; R1 acciona el mecanismo"),
                new ControlBinding("CUADRADO", InputGesture.HOLD, ControlAction.ENTER_ELECTROMAGNETIC_MANUAL_CHARGE,
                        "FUSIL BIFILAR: sujeta la manivela; R1 la hace girar"),
                new ControlBinding("CUADRADO", InputGesture.HOLD, ControlAction.ENTER_ARC_MANUAL_CHARGE,
                        "LANZA-ARCOS: manivela como acción manual preferente; avanza automáticamente mientras ninguna acción prioritaria la interrumpa"),
                new ControlBinding("CUADRADO", InputGesture.HOLD, ControlAction.ENTER_CLUSTER_TIMER_CONFIGURATION,
                        "CAÑÓN DE RACIMO: abre ajuste temporal; R1 alterna 3/4/5 s y CUADRADO confirma/sale"),
                new ControlBinding("D-PAD IZQUIERDA", InputGesture.PRESS, ControlAction.CYCLE_FIRE_MODE,
                        "FIREARM: cambia entre 1A / 3A / AA disponibles"),

                new ControlBinding("TRIÁNGULO", InputGesture.PRESS, ControlAction.INTERACT, ""),
                new ControlBinding("TRIÁNGULO", InputGesture.PRESS, ControlAction.TOGGLE_OBSERVATION_MARK,
                        "MONOCULAR ACTIVO: coloca, sustituye o retira la única marca de observación"),
                new ControlBinding("D-PAD DERECHA", InputGesture.PRESS, ControlAction.MONOCULAR_ZOOM_IN,
                        "MONOCULAR ACTIVO: ×3 → ×4 → ×5"),
                new ControlBinding("D-PAD IZQUIERDA", InputGesture.PRESS, ControlAction.MONOCULAR_ZOOM_OUT,
                        "MONOCULAR ACTIVO: ×5 → ×4 → ×3"),
                new ControlBinding("TRIÁNGULO", InputGesture.HOLD, ControlAction.CYCLE_CONTEXTUAL_ACTION, "Acción contextual secundaria"),
                new ControlBinding("CUADRADO", InputGesture.PRESS, ControlAction.SHEATHE_OR_UNSHEATHE, "Cuando no se resuelve como recarga firearm"),
                new ControlBinding("D-PAD DERECHA", InputGesture.PRESS, ControlAction.SWITCH_WEAPON, "Cambia arma o activa/desactiva dual wielding según el manejo actual"),
                new ControlBinding("D-PAD IZQUIERDA", InputGesture.PRESS, ControlAction.TOGGLE_SHAPESHIFT, "ASPIRANT/ANCIENT con CAMBIAFORMAS: alterna forma humana y cambiada; equivalencia contextual de MOUSE WHEEL"),
                new ControlBinding("D-PAD IZQUIERDA", InputGesture.PRESS, ControlAction.CYCLE_WEAPON_CONFIGURATION, "Cuando el objeto equipado no resuelve CYCLE_FIRE_MODE"),

                new ControlBinding("D-PAD ARRIBA", InputGesture.PRESS, ControlAction.EXECUTE_ACTIVE_MASTERY, "Ejecuta la maestría activa seleccionada"),
                new ControlBinding("D-PAD ARRIBA", InputGesture.HOLD, ControlAction.OPEN_ACTIVE_MASTERY_WHEEL, "Abre la rueda radial de maestrías activas"),
                new ControlBinding("D-PAD ABAJO", InputGesture.PRESS, ControlAction.TOGGLE_SUSTAINED_MASTERY, "Activa o desactiva la maestría sostenida seleccionada según su política"),
                new ControlBinding("D-PAD ABAJO", InputGesture.HOLD, ControlAction.OPEN_SUSTAINED_MASTERY_WHEEL, "Abre la rueda radial de maestrías sostenidas"),

                new ControlBinding("DESLIZAR TOUCHPAD IZQUIERDA", InputGesture.PRESS, ControlAction.QUICK_ACCESS_1, ""),
                new ControlBinding("DESLIZAR TOUCHPAD ARRIBA", InputGesture.PRESS, ControlAction.QUICK_ACCESS_2, ""),
                new ControlBinding("DESLIZAR TOUCHPAD DERECHA", InputGesture.PRESS, ControlAction.QUICK_ACCESS_3, ""),
                new ControlBinding("DESLIZAR TOUCHPAD ABAJO", InputGesture.PRESS, ControlAction.QUICK_ACCESS_4, ""),
                new ControlBinding("TOUCHPAD", InputGesture.PRESS, ControlAction.OPEN_INVENTORY, "Bloqueado durante un encuentro hostil"),
                new ControlBinding("TOUCHPAD", InputGesture.HOLD, ControlAction.OPEN_CHARACTER_SHEET, "Hoja en tiempo real"),
                new ControlBinding("SHARE", InputGesture.PRESS, ControlAction.TOGGLE_WORLD_MEMORY, "Bloqueado durante encuentro hostil"),
                new ControlBinding("SHARE", InputGesture.HOLD, ControlAction.CYCLE_HUD_STATE, ""),
                new ControlBinding("OPTIONS", InputGesture.PRESS, ControlAction.OPEN_PAUSE_MENU, "Equivalente a ESC"),

                new ControlBinding("L1 + TRIÁNGULO", InputGesture.PRESS, ControlAction.CALL_PERSONAL_TRANSPORT,
                        "Silbido universal: llama al transporte seleccionado a una posición válida entre 5 y 10 m"),
                new ControlBinding("L1 + TRIÁNGULO", InputGesture.HOLD, ControlAction.OPEN_PERSONAL_TRANSPORT_WHEEL,
                        "Abre el menú circular de transportes adquiridos")
        );
    }
}
