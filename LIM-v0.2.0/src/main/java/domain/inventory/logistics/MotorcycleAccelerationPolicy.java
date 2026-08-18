package domain.inventory.logistics;
public final class MotorcycleAccelerationPolicy {
 public static final double MAX_KMH=110.0;
 /** Curva continua de carburador único: respuesta inicial deliberadamente contenida y aceleración decreciente al acercarse al máximo. */
 public double accelerate(double currentKmh,double heldSeconds){if(currentKmh<0||heldSeconds<0)throw new IllegalArgumentException();double gain=(MAX_KMH-currentKmh)*(1-Math.exp(-0.085*heldSeconds));return Math.min(MAX_KMH,currentKmh+gain);}
 public double brake(double currentKmh,double heldSeconds){return Math.max(0,currentKmh-22.0*heldSeconds);}
 public double pushedReverseKmh(boolean stopped){return stopped?-3.0:0.0;}
}
