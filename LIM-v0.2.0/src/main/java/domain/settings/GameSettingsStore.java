package domain.settings;

import domain.control.ControlAction;
import domain.control.ControlMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public final class GameSettingsStore {
    private static final String DIRECTORY_NAME = ".lim-vertical-slice";
    private static final String FILE_NAME = "settings.properties";

    private final Path file;

    public GameSettingsStore() {
        this(Path.of(System.getProperty("user.home"), DIRECTORY_NAME, FILE_NAME));
    }

    public GameSettingsStore(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("La ruta de configuración no puede ser nula.");
        }
        this.file = file;
    }

    public GameSettings load() {
        GameSettings settings = new GameSettings();
        if (!Files.isRegularFile(file)) {
            return settings;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(file)) {
            properties.load(input);
            loadGeneral(settings, properties);
            loadBindings(settings, properties);
            settings.loadActiveDisplayStateFromRequested();
            return settings;
        } catch (IOException | IllegalArgumentException exception) {
            return new GameSettings();
        }
    }

    public void save(GameSettings settings) {
        if (settings == null) {
            throw new IllegalArgumentException("La configuración no puede ser nula.");
        }

        Properties properties = new Properties();
        properties.setProperty("control.mode", settings.controlMode().name());
        properties.setProperty("display.resolution", settings.resolution().name());
        properties.setProperty("display.mode", settings.displayMode().name());
        properties.setProperty("display.fps", settings.frameRateLimit().name());
        properties.setProperty("display.vsync", Boolean.toString(settings.vSync()));
        properties.setProperty("display.gamma", Integer.toString(settings.gammaPercent()));

        properties.setProperty("audio.master", Integer.toString(settings.masterVolume()));
        properties.setProperty("audio.music", Integer.toString(settings.musicVolume()));
        properties.setProperty("audio.effects", Integer.toString(settings.effectsVolume()));
        properties.setProperty("audio.voices", Integer.toString(settings.voicesVolume()));

        properties.setProperty("controls.sensitivity.horizontal", Integer.toString(settings.horizontalCameraSensitivity()));
        properties.setProperty("controls.sensitivity.vertical", Integer.toString(settings.verticalCameraSensitivity()));
        properties.setProperty("controls.invert.horizontal", Boolean.toString(settings.invertHorizontalAxis()));
        properties.setProperty("controls.invert.vertical", Boolean.toString(settings.invertVerticalAxis()));
        properties.setProperty("controls.vibration", Boolean.toString(settings.controllerVibration()));
        properties.setProperty("controls.deadzone.left", Integer.toString(settings.leftStickDeadZone()));
        properties.setProperty("controls.deadzone.right", Integer.toString(settings.rightStickDeadZone()));

        properties.setProperty("interface.hud.scale", Integer.toString(settings.hudScale()));
        properties.setProperty("interface.hud.opacity", Integer.toString(settings.hudOpacity()));
        properties.setProperty("interface.subtitles", Boolean.toString(settings.subtitles()));
        properties.setProperty("interface.text.size", settings.textSize().name());
        properties.setProperty("interface.contextual.hints", Boolean.toString(settings.contextualHints()));

        for (ControlMode mode : ControlMode.values()) {
            settings.customBindings(mode).forEach((action, binding) ->
                    properties.setProperty("binding." + mode.name() + "." + action.name(), binding)
            );
        }

        try {
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(
                    file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                properties.store(output, "La Idea del Mundo - Vertical Slice");
            }
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo guardar la configuración en " + file + ".", exception);
        }
    }

    public Path file() {
        return file;
    }

    private void loadGeneral(GameSettings settings, Properties properties) {
        settings.setControlMode(enumValue(properties, "control.mode", ControlMode.class, settings.controlMode()));
        settings.setResolution(enumValue(properties, "display.resolution", ScreenResolution.class, settings.resolution()));
        settings.setDisplayMode(enumValue(properties, "display.mode", DisplayMode.class, settings.displayMode()));
        settings.setFrameRateLimit(enumValue(properties, "display.fps", FrameRateLimit.class, settings.frameRateLimit()));
        settings.setVSync(booleanValue(properties, "display.vsync", settings.vSync()));
        settings.setGammaPercent(intValue(properties, "display.gamma", settings.gammaPercent()));

        settings.setMasterVolume(intValue(properties, "audio.master", settings.masterVolume()));
        settings.setMusicVolume(intValue(properties, "audio.music", settings.musicVolume()));
        settings.setEffectsVolume(intValue(properties, "audio.effects", settings.effectsVolume()));
        settings.setVoicesVolume(intValue(properties, "audio.voices", settings.voicesVolume()));

        settings.setHorizontalCameraSensitivity(intValue(properties, "controls.sensitivity.horizontal", settings.horizontalCameraSensitivity()));
        settings.setVerticalCameraSensitivity(intValue(properties, "controls.sensitivity.vertical", settings.verticalCameraSensitivity()));
        settings.setInvertHorizontalAxis(booleanValue(properties, "controls.invert.horizontal", settings.invertHorizontalAxis()));
        settings.setInvertVerticalAxis(booleanValue(properties, "controls.invert.vertical", settings.invertVerticalAxis()));
        settings.setControllerVibration(booleanValue(properties, "controls.vibration", settings.controllerVibration()));
        settings.setLeftStickDeadZone(intValue(properties, "controls.deadzone.left", settings.leftStickDeadZone()));
        settings.setRightStickDeadZone(intValue(properties, "controls.deadzone.right", settings.rightStickDeadZone()));

        settings.setHudScale(intValue(properties, "interface.hud.scale", settings.hudScale()));
        settings.setHudOpacity(intValue(properties, "interface.hud.opacity", settings.hudOpacity()));
        settings.setSubtitles(booleanValue(properties, "interface.subtitles", settings.subtitles()));
        settings.setTextSize(enumValue(properties, "interface.text.size", TextSize.class, settings.textSize()));
        settings.setContextualHints(booleanValue(properties, "interface.contextual.hints", settings.contextualHints()));
    }

    private void loadBindings(GameSettings settings, Properties properties) {
        for (ControlMode mode : ControlMode.values()) {
            for (ControlAction action : ControlAction.values()) {
                String value = properties.getProperty("binding." + mode.name() + "." + action.name());
                if (value != null && !value.isBlank()) {
                    settings.remap(mode, action, value);
                }
            }
        }
    }

    private int intValue(Properties properties, String key, int fallback) {
        String value = properties.getProperty(key);
        return value == null ? fallback : Integer.parseInt(value.trim());
    }

    private boolean booleanValue(Properties properties, String key, boolean fallback) {
        String value = properties.getProperty(key);
        return value == null ? fallback : Boolean.parseBoolean(value.trim());
    }

    private <E extends Enum<E>> E enumValue(Properties properties, String key, Class<E> type, E fallback) {
        String value = properties.getProperty(key);
        return value == null ? fallback : Enum.valueOf(type, value.trim());
    }
}
