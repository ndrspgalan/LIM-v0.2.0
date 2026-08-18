package domain.combat;

/** una sola exposición extensa se distribuye 9 % HEAD y 91 % BODY, sin duplicar daño. */
public final class AreaBodyDistributionPolicy {
    public static final double HEAD_RATIO=0.09;
    public static final double BODY_RATIO=0.91;
    private AreaBodyDistributionPolicy(){}
    public static Split split(double raw){
        if(!Double.isFinite(raw)||raw<0) throw new IllegalArgumentException("Daño inválido.");
        return new Split(raw*HEAD_RATIO,raw*BODY_RATIO);
    }
    public record Split(double head,double body){}
}
