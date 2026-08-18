package domain.combat;

import domain.inventory.item.LethalityProfile;

/** caída radial lineal desde el perfil máximo en el epicentro hasta 1 punto en el límite. */
public final class RadialLethalityPolicy {
    private RadialLethalityPolicy(){}
    public static double channel(double maximum,double distanceMeters,double radiusMeters){
        if(!Double.isFinite(maximum)||maximum<0||!Double.isFinite(distanceMeters)||distanceMeters<0||!Double.isFinite(radiusMeters)||radiusMeters<=0)
            throw new IllegalArgumentException("Parámetros radiales inválidos.");
        if(distanceMeters>radiusMeters) return 0;
        if(maximum<=0) return 0;
        double t=distanceMeters/radiusMeters;
        return maximum-(maximum-1.0)*t;
    }
    public static LethalityProfile physical(LethalityProfile maximum,double distanceMeters,double radiusMeters){
        return new LethalityProfile(channel(maximum.piercing(),distanceMeters,radiusMeters), channel(maximum.slashing(),distanceMeters,radiusMeters), channel(maximum.blunt(),distanceMeters,radiusMeters));
    }
}
