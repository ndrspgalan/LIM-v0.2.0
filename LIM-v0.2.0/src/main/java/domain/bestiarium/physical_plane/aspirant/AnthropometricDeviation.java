package domain.bestiarium.physical_plane.aspirant;

/** Distancia conceptual discreta 0..10 respecto de un referente antropométrico. */
public record AnthropometricDeviation(int steps) {
    public AnthropometricDeviation {
        if(steps<0||steps>10) throw new IllegalArgumentException("La desviación antropométrica debe estar entre 0 y 10.");
    }
    public double normalized(){return steps/10.0;}
    public boolean converged(){return steps==0;}
}
