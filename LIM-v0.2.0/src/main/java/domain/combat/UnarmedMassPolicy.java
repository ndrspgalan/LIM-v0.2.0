package domain.combat;

import domain.character.Gender;

/** Masa ofensiva convencional de DESARMADO: no es masa corporal total. */
public final class UnarmedMassPolicy {
    private UnarmedMassPolicy(){}
    public static double equivalentMassKg(Gender gender){
        if(gender==null) throw new IllegalArgumentException("El sexo no puede ser nulo.");
        return gender==Gender.HOMBRE?1.0:0.5;
    }
}
