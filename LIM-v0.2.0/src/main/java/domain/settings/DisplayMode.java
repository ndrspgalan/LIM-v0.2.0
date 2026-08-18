package domain.settings;
public enum DisplayMode { WINDOWED("VENTANA"), BORDERLESS("VENTANA SIN BORDES"), FULLSCREEN("PANTALLA COMPLETA");
    private final String label; DisplayMode(String label){this.label=label;} public String label(){return label;}}
