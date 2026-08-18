package domain.status;

public record TimedEffect(String name, double duration, TimeScale timeScale) {
    public TimedEffect {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("El nombre del efecto es obligatorio.");
        if (duration <= 0) throw new IllegalArgumentException("La duración debe ser positiva.");
        if (timeScale == null) throw new NullPointerException("La escala temporal no puede ser nula.");
    }
}
