package domain.combat;

/**
 * Autoridad para impactos donde toda la FUERZA del personaje y toda la masa del objeto
 * se transfieren al canal contundente: +1 Ct por nivel de FUERZA y +1 Ct por kilogramo.
 */
public final class StrengthMassBluntPolicy {
    private StrengthMassBluntPolicy(){}

    public static double blunt(int strength, double objectMassKg){
        if(strength < 0) throw new IllegalArgumentException("FUERZA no puede ser negativa.");
        if(!Double.isFinite(objectMassKg) || objectMassKg < 0)
            throw new IllegalArgumentException("La masa del objeto debe ser finita y no negativa.");
        return strength + objectMassKg;
    }
}
