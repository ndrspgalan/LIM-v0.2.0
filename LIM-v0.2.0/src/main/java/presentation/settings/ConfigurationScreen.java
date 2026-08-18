package presentation.settings;

import domain.control.ControlAction;
import domain.control.ControlMode;
import domain.control.PcControlScheme;
import domain.control.Ps4ControlScheme;
import domain.settings.ConfigurationContext;
import domain.settings.DisplayMode;
import domain.settings.FrameRateLimit;
import domain.settings.GameSettings;
import domain.settings.GameSettingsStore;
import domain.settings.ScreenResolution;
import domain.settings.TextSize;
import presentation.console.ConsoleInput;

import java.io.PrintStream;

public final class ConfigurationScreen {
    private static final String DIVIDER = "=================================";

    private final GameSettings settings;
    private final GameSettingsStore store;
    private final ConsoleInput input;
    private final PrintStream out;

    public ConfigurationScreen(
            GameSettings settings,
            GameSettingsStore store,
            ConsoleInput input,
            PrintStream out
    ) {
        this.settings = settings;
        this.store = store;
        this.input = input;
        this.out = out;
    }

    public void open() {
        open(ConfigurationContext.MAIN_MENU);
    }

    public void open(ConfigurationContext context) {
        boolean open = true;
        while (open) {
            display(context);
            int choice = input.readIntegerBetween("Seleccione una opción: ", 0, 6);
            switch (choice) {
                case 0 -> open = false;
                case 1 -> controlsMenu();
                case 2 -> displayMenu(context);
                case 3 -> audioMenu();
                case 4 -> interfaceMenu();
                case 5 -> showSummary();
                case 6 -> restoreDefaults();
                default -> throw new IllegalStateException("Opción de configuración no contemplada.");
            }
            saveSafely();
        }

        if (context == ConfigurationContext.MAIN_MENU && settings.hasPendingDisplayChanges()) {
            settings.applyPendingDisplayChanges();
            saveSafely();
            out.println("Los cambios de pantalla se han aplicado al volver al menú principal.");
        } else if (context == ConfigurationContext.IN_GAME && settings.hasPendingDisplayChanges()) {
            out.println("Los cambios de pantalla quedan guardados como CAMBIOS PENDIENTES.");
            out.println("Se aplicarán al regresar al menú principal o al reiniciar la demo.");
        }
    }

    private void display(ConfigurationContext context) {
        out.println();
        out.println(DIVIDER);
        out.println(center("CONFIGURAR", DIVIDER.length()));
        out.println(DIVIDER);
        out.println();
        out.println("1. CONTROLES");
        out.println("2. PANTALLA" + pendingMarker());
        out.println("3. SONIDO");
        out.println("4. INTERFAZ");
        out.println("5. RESUMEN DE AJUSTES");
        out.println("6. RESTAURAR VALORES PREDETERMINADOS");
        out.println("0. VOLVER");
        if (context == ConfigurationContext.IN_GAME) {
            out.println();
            out.println("Los cambios estructurales de pantalla se aplicarán al volver al menú principal.");
        }
        out.println();
    }

    private void controlsMenu() {
        boolean open = true;
        while (open) {
            out.println();
            out.println("CONTROLES");
            out.println("1. Modo de control: " + settings.controlMode().label());
            out.println("2. Reasignar una orden");
            out.println("3. Ver controles");
            out.printf("4. Sensibilidad de cámara: horizontal %d · vertical %d%n",
                    settings.horizontalCameraSensitivity(), settings.verticalCameraSensitivity());
            out.printf("5. Invertir ejes: horizontal %s · vertical %s%n",
                    enabled(settings.invertHorizontalAxis()), enabled(settings.invertVerticalAxis()));
            out.printf("6. Vibración del mando: %s%n", enabled(settings.controllerVibration()));
            out.printf("7. Zonas muertas: stick izquierdo %d%% · stick derecho %d%%%n",
                    settings.leftStickDeadZone(), settings.rightStickDeadZone());
            out.println("8. Restaurar controles del modo actual");
            out.println("0. Volver");

            int choice = input.readIntegerBetween("Seleccione: ", 0, 8);
            switch (choice) {
                case 0 -> open = false;
                case 1 -> chooseControls();
                case 2 -> remap();
                case 3 -> showBindings();
                case 4 -> setSensitivity();
                case 5 -> invertAxes();
                case 6 -> settings.setControllerVibration(!settings.controllerVibration());
                case 7 -> setDeadZones();
                case 8 -> {
                    settings.clearCustomBindings(settings.controlMode());
                    out.println("Controles personalizados eliminados para " + settings.controlMode().label() + ".");
                }
                default -> throw new IllegalStateException("Opción de controles no contemplada.");
            }
            saveSafely();
        }
    }

    private void displayMenu(ConfigurationContext context) {
        boolean open = true;
        while (open) {
            out.println();
            out.println("PANTALLA" + pendingMarker());
            out.println("1. Resolución solicitada: " + settings.resolution().label());
            out.println("2. Modo de pantalla solicitado: " + settings.displayMode().label());
            out.println("3. Límite de FPS solicitado: " + settings.frameRateLimit().label());
            out.println("4. Sincronización vertical: " + enabled(settings.vSync()));
            out.println("5. Brillo / gamma: " + settings.gammaPercent() + "%");
            out.println("6. Ver estado activo y solicitado");
            out.println("Distancia de renderizado: " + settings.renderDistanceMeters() + " m [BLOQUEADO]");
            out.println("0. Volver");

            int choice = input.readIntegerBetween("Seleccione: ", 0, 6);
            switch (choice) {
                case 0 -> open = false;
                case 1 -> chooseResolution();
                case 2 -> chooseDisplayMode();
                case 3 -> chooseFrameRate();
                case 4 -> settings.setVSync(!settings.vSync());
                case 5 -> settings.setGammaPercent(input.readIntegerBetween("Gamma (50-150): ", 50, 150));
                case 6 -> showDisplayState(context);
                default -> throw new IllegalStateException("Opción de pantalla no contemplada.");
            }
            saveSafely();
        }
    }

    private void audioMenu() {
        boolean open = true;
        while (open) {
            out.println();
            out.println("SONIDO");
            out.printf("1. Volumen maestro: %d%%%n", settings.masterVolume());
            out.printf("2. Música: %d%%%n", settings.musicVolume());
            out.printf("3. Efectos: %d%%%n", settings.effectsVolume());
            out.printf("4. Voces: %d%%%n", settings.voicesVolume());
            out.println("0. Volver");

            int choice = input.readIntegerBetween("Seleccione: ", 0, 4);
            switch (choice) {
                case 0 -> open = false;
                case 1 -> settings.setMasterVolume(percent("Volumen maestro"));
                case 2 -> settings.setMusicVolume(percent("Música"));
                case 3 -> settings.setEffectsVolume(percent("Efectos"));
                case 4 -> settings.setVoicesVolume(percent("Voces"));
                default -> throw new IllegalStateException("Opción de sonido no contemplada.");
            }
            saveSafely();
        }
    }

    private void interfaceMenu() {
        boolean open = true;
        while (open) {
            out.println();
            out.println("INTERFAZ");
            out.printf("1. Tamaño del HUD: %d%%%n", settings.hudScale());
            out.printf("2. Opacidad del HUD: %d%%%n", settings.hudOpacity());
            out.println("3. Subtítulos: " + enabled(settings.subtitles()));
            out.println("4. Tamaño del texto: " + settings.textSize().label());
            out.println("5. Ayudas contextuales: " + enabled(settings.contextualHints()));
            out.println("0. Volver");

            int choice = input.readIntegerBetween("Seleccione: ", 0, 5);
            switch (choice) {
                case 0 -> open = false;
                case 1 -> settings.setHudScale(input.readIntegerBetween("Tamaño del HUD (50-150): ", 50, 150));
                case 2 -> settings.setHudOpacity(percent("Opacidad del HUD"));
                case 3 -> settings.setSubtitles(!settings.subtitles());
                case 4 -> chooseTextSize();
                case 5 -> settings.setContextualHints(!settings.contextualHints());
                default -> throw new IllegalStateException("Opción de interfaz no contemplada.");
            }
            saveSafely();
        }
    }

    private void chooseControls() {
        out.println("1. TECLADO Y RATÓN");
        out.println("2. MANDO PS4");
        settings.setControlMode(input.readIntegerBetween("Modo: ", 1, 2) == 1
                ? ControlMode.KEYBOARD_MOUSE
                : ControlMode.PS4_CONTROLLER);
    }

    private void remap() {
        ControlAction[] actions = ControlAction.values();
        for (int index = 0; index < actions.length; index++) {
            out.printf("%d. %s%n", index + 1, actions[index]);
        }
        int selected = input.readIntegerBetween("Orden: ", 1, actions.length);
        String binding = input.readText("Nueva tecla, botón o combinación textual: ");
        var result = settings.remap(settings.controlMode(), actions[selected - 1], binding);
        out.println(result.message());
    }

    private void showBindings() {
        var list = settings.controlMode() == ControlMode.KEYBOARD_MOUSE
                ? PcControlScheme.canonicalBindings()
                : Ps4ControlScheme.canonicalBindings();
        out.println();
        out.println(settings.controlMode().label());
        list.forEach(binding -> out.printf("%-28s %-28s %s%n",
                binding.input() + " [" + binding.gesture() + "]",
                binding.action(),
                binding.condition()));
        if (!settings.customBindings().isEmpty()) {
            out.println("PERSONALIZADOS");
            settings.customBindings().forEach((action, binding) -> out.println(binding + " -> " + action));
        }
    }

    private void setSensitivity() {
        settings.setHorizontalCameraSensitivity(percent("Sensibilidad horizontal"));
        settings.setVerticalCameraSensitivity(percent("Sensibilidad vertical"));
    }

    private void invertAxes() {
        out.println("1. Alternar eje horizontal");
        out.println("2. Alternar eje vertical");
        out.println("3. Alternar ambos");
        int choice = input.readIntegerBetween("Seleccione: ", 1, 3);
        if (choice == 1 || choice == 3) {
            settings.setInvertHorizontalAxis(!settings.invertHorizontalAxis());
        }
        if (choice == 2 || choice == 3) {
            settings.setInvertVerticalAxis(!settings.invertVerticalAxis());
        }
    }

    private void setDeadZones() {
        settings.setLeftStickDeadZone(input.readIntegerBetween("Zona muerta del stick izquierdo (0-40): ", 0, 40));
        settings.setRightStickDeadZone(input.readIntegerBetween("Zona muerta del stick derecho (0-40): ", 0, 40));
    }

    private void chooseResolution() {
        ScreenResolution[] values = ScreenResolution.values();
        for (int index = 0; index < values.length; index++) {
            out.printf("%d. %s%n", index + 1, values[index].label());
        }
        settings.setResolution(values[input.readIntegerBetween("Resolución: ", 1, values.length) - 1]);
    }

    private void chooseDisplayMode() {
        DisplayMode[] values = DisplayMode.values();
        for (int index = 0; index < values.length; index++) {
            out.printf("%d. %s%n", index + 1, values[index].label());
        }
        settings.setDisplayMode(values[input.readIntegerBetween("Modo: ", 1, values.length) - 1]);
    }

    private void chooseFrameRate() {
        FrameRateLimit[] values = FrameRateLimit.values();
        for (int index = 0; index < values.length; index++) {
            out.printf("%d. %s%n", index + 1, values[index].label());
        }
        settings.setFrameRateLimit(values[input.readIntegerBetween("Límite: ", 1, values.length) - 1]);
    }

    private void chooseTextSize() {
        TextSize[] values = TextSize.values();
        for (int index = 0; index < values.length; index++) {
            out.printf("%d. %s%n", index + 1, values[index].label());
        }
        settings.setTextSize(values[input.readIntegerBetween("Tamaño: ", 1, values.length) - 1]);
    }

    private void showDisplayState(ConfigurationContext context) {
        out.println("ACTIVO");
        out.println("Resolución: " + settings.activeResolution().label());
        out.println("Modo: " + settings.activeDisplayMode().label());
        out.println("FPS: " + settings.activeFrameRateLimit().label());
        out.println("VSync: " + enabled(settings.activeVSync()));
        out.println("SOLICITADO");
        out.println("Resolución: " + settings.resolution().label());
        out.println("Modo: " + settings.displayMode().label());
        out.println("FPS: " + settings.frameRateLimit().label());
        out.println("VSync: " + enabled(settings.vSync()));
        if (context == ConfigurationContext.IN_GAME && settings.hasPendingDisplayChanges()) {
            out.println("Estado: CAMBIOS PENDIENTES");
        }
    }

    private void showSummary() {
        out.println();
        out.println("RESUMEN DE AJUSTES");
        out.println("Control: " + settings.controlMode().label());
        out.printf("Cámara: H %d · V %d · invertir H %s · invertir V %s%n",
                settings.horizontalCameraSensitivity(), settings.verticalCameraSensitivity(),
                enabled(settings.invertHorizontalAxis()), enabled(settings.invertVerticalAxis()));
        out.printf("Mando: vibración %s · zonas muertas %d%%/%d%%%n",
                enabled(settings.controllerVibration()), settings.leftStickDeadZone(), settings.rightStickDeadZone());
        out.printf("Pantalla: %s · %s · %s · VSync %s · gamma %d%%%s%n",
                settings.resolution().label(), settings.displayMode().label(), settings.frameRateLimit().label(),
                enabled(settings.vSync()), settings.gammaPercent(), pendingMarker());
        out.printf("Sonido: maestro %d · música %d · efectos %d · voces %d%n",
                settings.masterVolume(), settings.musicVolume(), settings.effectsVolume(), settings.voicesVolume());
        out.printf("Interfaz: HUD %d%%/%d%% · subtítulos %s · texto %s · ayudas %s%n",
                settings.hudScale(), settings.hudOpacity(), enabled(settings.subtitles()),
                settings.textSize().label(), enabled(settings.contextualHints()));
        out.println("Persistencia: " + store.file());
    }

    private void restoreDefaults() {
        out.println("1. Confirmar restauración completa");
        out.println("0. Cancelar");
        if (input.readIntegerBetween("Seleccione: ", 0, 1) == 1) {
            settings.restoreDefaults();
            out.println("Configuración restaurada a sus valores predeterminados.");
        }
    }


    private String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int leftPadding = (width - text.length()) / 2;
        return " ".repeat(leftPadding) + text;
    }

    private int percent(String label) {
        return input.readIntegerBetween(label + " (0-100): ", 0, 100);
    }

    private String enabled(boolean value) {
        return value ? "ACTIVADO" : "DESACTIVADO";
    }

    private String pendingMarker() {
        return settings.hasPendingDisplayChanges() ? " [CAMBIOS PENDIENTES]" : "";
    }

    private void saveSafely() {
        try {
            store.save(settings);
        } catch (IllegalStateException exception) {
            out.println("Advertencia: " + exception.getMessage());
        }
    }
}
