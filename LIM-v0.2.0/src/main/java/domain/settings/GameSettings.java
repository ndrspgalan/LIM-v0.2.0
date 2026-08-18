package domain.settings;

import domain.control.ControlAction;
import domain.control.ControlBinding;
import domain.control.ControlMode;
import domain.control.PcControlScheme;
import domain.control.Ps4ControlScheme;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class GameSettings {
    public static final int FIXED_RENDER_DISTANCE_METERS = 1500;
    private ControlMode controlMode = ControlMode.KEYBOARD_MOUSE;

    private ScreenResolution activeResolution = ScreenResolution.R1920X1080;
    private ScreenResolution requestedResolution = ScreenResolution.R1920X1080;
    private DisplayMode activeDisplayMode = DisplayMode.FULLSCREEN;
    private DisplayMode requestedDisplayMode = DisplayMode.FULLSCREEN;
    private FrameRateLimit activeFrameRateLimit = FrameRateLimit.FPS_60;
    private FrameRateLimit requestedFrameRateLimit = FrameRateLimit.FPS_60;
    private boolean activeVSync = true;
    private boolean requestedVSync = true;

    private int gammaPercent = 100;

    private int masterVolume = 100;
    private int musicVolume = 80;
    private int effectsVolume = 100;
    private int voicesVolume = 100;

    private int horizontalCameraSensitivity = 50;
    private int verticalCameraSensitivity = 50;
    private boolean invertHorizontalAxis;
    private boolean invertVerticalAxis;
    private boolean controllerVibration = true;
    private int leftStickDeadZone = 10;
    private int rightStickDeadZone = 10;

    private int hudScale = 100;
    private int hudOpacity = 100;
    private boolean subtitles = true;
    private TextSize textSize = TextSize.MEDIUM;
    private boolean contextualHints = true;

    private final Map<ControlMode, EnumMap<ControlAction, String>> customBindings =
            new EnumMap<>(ControlMode.class);

    public GameSettings() {
        for (ControlMode mode : ControlMode.values()) {
            customBindings.put(mode, new EnumMap<>(ControlAction.class));
        }
    }

    public ControlMode controlMode() { return controlMode; }
    public void setControlMode(ControlMode value) { controlMode = require(value, "modo de control"); }

    public ScreenResolution resolution() { return requestedResolution; }
    public ScreenResolution activeResolution() { return activeResolution; }
    public void setResolution(ScreenResolution value) { requestedResolution = require(value, "resolución"); }

    public DisplayMode displayMode() { return requestedDisplayMode; }
    public DisplayMode activeDisplayMode() { return activeDisplayMode; }
    public void setDisplayMode(DisplayMode value) { requestedDisplayMode = require(value, "modo de pantalla"); }

    public FrameRateLimit frameRateLimit() { return requestedFrameRateLimit; }
    public FrameRateLimit activeFrameRateLimit() { return activeFrameRateLimit; }
    public void setFrameRateLimit(FrameRateLimit value) { requestedFrameRateLimit = require(value, "límite de FPS"); }

    public boolean vSync() { return requestedVSync; }
    public boolean activeVSync() { return activeVSync; }
    public void setVSync(boolean value) { requestedVSync = value; }

    public boolean hasPendingDisplayChanges() {
        return activeResolution != requestedResolution
                || activeDisplayMode != requestedDisplayMode
                || activeFrameRateLimit != requestedFrameRateLimit
                || activeVSync != requestedVSync;
    }

    public void applyPendingDisplayChanges() {
        activeResolution = requestedResolution;
        activeDisplayMode = requestedDisplayMode;
        activeFrameRateLimit = requestedFrameRateLimit;
        activeVSync = requestedVSync;
    }

    /** Distancia de renderizado canónica y no configurable de LIM. */
    public int renderDistanceMeters() { return FIXED_RENDER_DISTANCE_METERS; }
    public boolean renderDistanceLocked() { return true; }

    public int gammaPercent() { return gammaPercent; }
    public void setGammaPercent(int value) { gammaPercent = validateRange(value, 50, 150, "gamma"); }

    public int masterVolume() { return masterVolume; }
    public int musicVolume() { return musicVolume; }
    public int effectsVolume() { return effectsVolume; }
    public int voicesVolume() { return voicesVolume; }
    public void setMasterVolume(int value) { masterVolume = validatePercent(value, "volumen maestro"); }
    public void setMusicVolume(int value) { musicVolume = validatePercent(value, "volumen de música"); }
    public void setEffectsVolume(int value) { effectsVolume = validatePercent(value, "volumen de efectos"); }
    public void setVoicesVolume(int value) { voicesVolume = validatePercent(value, "volumen de voces"); }

    public int horizontalCameraSensitivity() { return horizontalCameraSensitivity; }
    public int verticalCameraSensitivity() { return verticalCameraSensitivity; }
    public void setHorizontalCameraSensitivity(int value) { horizontalCameraSensitivity = validatePercent(value, "sensibilidad horizontal"); }
    public void setVerticalCameraSensitivity(int value) { verticalCameraSensitivity = validatePercent(value, "sensibilidad vertical"); }
    public boolean invertHorizontalAxis() { return invertHorizontalAxis; }
    public boolean invertVerticalAxis() { return invertVerticalAxis; }
    public void setInvertHorizontalAxis(boolean value) { invertHorizontalAxis = value; }
    public void setInvertVerticalAxis(boolean value) { invertVerticalAxis = value; }
    public boolean controllerVibration() { return controllerVibration; }
    public void setControllerVibration(boolean value) { controllerVibration = value; }
    public int leftStickDeadZone() { return leftStickDeadZone; }
    public int rightStickDeadZone() { return rightStickDeadZone; }
    public void setLeftStickDeadZone(int value) { leftStickDeadZone = validateRange(value, 0, 40, "zona muerta izquierda"); }
    public void setRightStickDeadZone(int value) { rightStickDeadZone = validateRange(value, 0, 40, "zona muerta derecha"); }

    public int hudScale() { return hudScale; }
    public void setHudScale(int value) { hudScale = validateRange(value, 50, 150, "tamaño del HUD"); }
    public int hudOpacity() { return hudOpacity; }
    public void setHudOpacity(int value) { hudOpacity = validatePercent(value, "opacidad del HUD"); }
    public boolean subtitles() { return subtitles; }
    public void setSubtitles(boolean value) { subtitles = value; }
    public TextSize textSize() { return textSize; }
    public void setTextSize(TextSize value) { textSize = require(value, "tamaño del texto"); }
    public boolean contextualHints() { return contextualHints; }
    public void setContextualHints(boolean value) { contextualHints = value; }

    public BindingUpdateResult remap(ControlAction action, String input) {
        return remap(controlMode, action, input);
    }

    public BindingUpdateResult remap(ControlMode mode, ControlAction action, String input) {
        require(mode, "modo de control");
        require(action, "orden");
        String normalized = normalizeBinding(input);
        if (normalized.isBlank()) {
            return BindingUpdateResult.rejected("La entrada no puede estar vacía.");
        }

        Optional<ControlAction> conflict = actionForInput(mode, normalized)
                .filter(existing -> existing != action);
        if (conflict.isPresent()) {
            return BindingUpdateResult.rejected(
                    "La entrada ya está asignada a " + conflict.get() + " en " + mode.label() + "."
            );
        }

        customBindings.get(mode).put(action, normalized);
        return BindingUpdateResult.accepted("Orden reasignada para " + mode.label() + ".");
    }

    public Optional<ControlAction> actionForCustomInput(String input) {
        String normalized = normalizeBinding(input);
        return customBindings.get(controlMode).entrySet().stream()
                .filter(entry -> entry.getValue().equals(normalized))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public Optional<ControlAction> actionForInput(ControlMode mode, String input) {
        String normalized = normalizeBinding(input);
        Optional<ControlAction> custom = customBindings.get(mode).entrySet().stream()
                .filter(entry -> entry.getValue().equals(normalized))
                .map(Map.Entry::getKey)
                .findFirst();
        if (custom.isPresent()) {
            return custom;
        }
        return canonicalBindings(mode).stream()
                .filter(binding -> normalizeBinding(binding.input()).equals(normalized))
                .map(ControlBinding::action)
                .findFirst();
    }

    public Map<ControlAction, String> customBindings() {
        return customBindings(controlMode);
    }

    public Map<ControlAction, String> customBindings(ControlMode mode) {
        return Map.copyOf(customBindings.get(mode));
    }

    public void clearCustomBindings(ControlMode mode) {
        customBindings.get(require(mode, "modo de control")).clear();
    }

    public void clearAllCustomBindings() {
        customBindings.values().forEach(Map::clear);
    }

    public void restoreDefaults() {
        controlMode = ControlMode.KEYBOARD_MOUSE;
        activeResolution = requestedResolution = ScreenResolution.R1920X1080;
        activeDisplayMode = requestedDisplayMode = DisplayMode.FULLSCREEN;
        activeFrameRateLimit = requestedFrameRateLimit = FrameRateLimit.FPS_60;
        activeVSync = requestedVSync = true;
        gammaPercent = 100;
        masterVolume = 100;
        musicVolume = 80;
        effectsVolume = 100;
        voicesVolume = 100;
        horizontalCameraSensitivity = 50;
        verticalCameraSensitivity = 50;
        invertHorizontalAxis = false;
        invertVerticalAxis = false;
        controllerVibration = true;
        leftStickDeadZone = 10;
        rightStickDeadZone = 10;
        hudScale = 100;
        hudOpacity = 100;
        subtitles = true;
        textSize = TextSize.MEDIUM;
        contextualHints = true;
        clearAllCustomBindings();
    }

    public void loadActiveDisplayStateFromRequested() {
        applyPendingDisplayChanges();
    }

    private List<ControlBinding> canonicalBindings(ControlMode mode) {
        return mode == ControlMode.KEYBOARD_MOUSE
                ? PcControlScheme.canonicalBindings()
                : Ps4ControlScheme.canonicalBindings();
    }

    private String normalizeBinding(String input) {
        return input == null ? "" : input.trim().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    private int validatePercent(int value, String name) {
        return validateRange(value, 0, 100, name);
    }

    private int validateRange(int value, int minimum, int maximum, String name) {
        if (value < minimum || value > maximum) {
            throw new IllegalArgumentException(
                    "El valor de " + name + " debe estar entre " + minimum + " y " + maximum + "."
            );
        }
        return value;
    }

    private <T> T require(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException("El " + name + " no puede ser nulo.");
        }
        return value;
    }
}
