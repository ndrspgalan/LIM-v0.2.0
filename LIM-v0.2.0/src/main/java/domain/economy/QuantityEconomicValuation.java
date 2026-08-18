package domain.economy;

import java.util.Objects;

/** Desglose económico de una instancia cuyo contenido material puede variar. */
public record QuantityEconomicValuation(
        String objectName,
        EconomicGoodType goodType,
        long structuralValueValeritas,
        long contentValueValeritas,
        long currentValueValeritas,
        String rationale
) {
    public QuantityEconomicValuation {
        if(objectName==null || objectName.isBlank()) throw new IllegalArgumentException("Objeto sin nombre.");
        objectName=objectName.trim();
        Objects.requireNonNull(goodType);
        if(structuralValueValeritas<0 || contentValueValeritas<0 || currentValueValeritas<0)
            throw new IllegalArgumentException("Los valores no pueden ser negativos.");
        if(currentValueValeritas!=Math.addExact(structuralValueValeritas,contentValueValeritas))
            throw new IllegalArgumentException("El valor actual debe ser continente + contenido.");
        if(rationale==null || rationale.isBlank()) throw new IllegalArgumentException("Falta justificación.");
        rationale=rationale.trim();
    }
}
