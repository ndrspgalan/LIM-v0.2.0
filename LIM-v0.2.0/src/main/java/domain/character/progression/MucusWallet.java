package domain.character.progression;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Reserva de mucus medida en mL.  admite fracciones de 0,5 mL para que
 * Transposición pueda consumir 2,5 mL de mucus ensangrentado sin redondeos.
 */
public final class MucusWallet {
    private static final double EPS=1e-9;
    private final EnumMap<MucusType, Double> quantities;

    public MucusWallet(Map<MucusType, ? extends Number> quantities) {
        Objects.requireNonNull(quantities, "Las existencias de mucus no pueden ser nulas.");
        this.quantities = new EnumMap<>(MucusType.class);
        for (MucusType type : MucusType.values()) {
            double quantity = quantities.containsKey(type) ? quantities.get(type).doubleValue() : 0.0;
            validateHalfMl(quantity,type);
            if (quantity > type.maximumReserveMl()+EPS) {
                throw new IllegalArgumentException("La cantidad de " + type.label()
                        + " no puede superar " + type.maximumReserveMl() + " mL.");
            }
            this.quantities.put(type, quantity);
        }
    }

    public double quantityMlOf(MucusType type) {
        return quantities.get(Objects.requireNonNull(type));
    }

    /** Compatibilidad nominal: la unidad sigue siendo mL. */
    public double quantityOf(MucusType type) { return quantityMlOf(type); }

    public boolean contains(MucusType type) { return quantityMlOf(type) > EPS; }

    public double totalQuantity() { return quantities.values().stream().mapToDouble(Double::doubleValue).sum(); }
    public boolean isEmpty() { return totalQuantity() <= EPS; }

    public MucusWallet addOne(MucusType type) { return add(type, 1.0); }

    public MucusWallet add(MucusType type, double amount) {
        Objects.requireNonNull(type); validateHalfMl(amount,type);
        if (amount < -EPS) throw new IllegalArgumentException("La cantidad añadida no puede ser negativa.");
        if (amount <= EPS) return this;
        EnumMap<MucusType,Double> updated=new EnumMap<>(quantities);
        updated.put(type, Math.min(type.maximumReserveMl(), quantityMlOf(type)+amount));
        return new MucusWallet(updated);
    }

    public MucusWallet consumeOne(MucusType type) { return consume(type,1.0); }

    public MucusWallet consume(MucusType type, double amount) {
        Objects.requireNonNull(type);
        validateHalfMl(amount,type);
        if (amount < -EPS) throw new IllegalArgumentException("La cantidad consumida no puede ser negativa.");
        if (amount > quantityMlOf(type)+EPS)
            throw new IllegalStateException("No hay suficiente "+type.label()+" disponible.");
        if (amount <= EPS) return this;
        EnumMap<MucusType,Double> updated=new EnumMap<>(quantities);
        double next=quantityMlOf(type)-amount;
        updated.put(type,Math.abs(next)<EPS?0.0:next);
        return new MucusWallet(updated);
    }

    public Map<MucusType, Double> quantities() { return Map.copyOf(quantities); }

    public static MucusWallet empty() { return of(0,0,0,0,0,0); }

    public static MucusWallet of(int white,int yellow,int green,int brown,int bloody,int blackish) {
        EnumMap<MucusType,Number> values=new EnumMap<>(MucusType.class);
        values.put(MucusType.BLANCO,white); values.put(MucusType.AMARILLENTO,yellow);
        values.put(MucusType.VERDOSO,green); values.put(MucusType.MARRON,brown);
        values.put(MucusType.ENSANGRENTADO,bloody); values.put(MucusType.NEGRUZCO,blackish);
        return new MucusWallet(values);
    }

    private static void validateHalfMl(double value,MucusType type){
        if(!Double.isFinite(value) || value < -EPS)
            throw new IllegalArgumentException("La cantidad de "+type.label()+" debe ser finita y no negativa.");
        double doubled=value*2.0;
        if(Math.abs(doubled-Math.rint(doubled))>EPS)
            throw new IllegalArgumentException(" representa mucus en incrementos mínimos de 0,5 mL: "+value);
    }
}
