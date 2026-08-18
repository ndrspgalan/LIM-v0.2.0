package domain.economy;

import domain.inventory.item.misc.CurrencyType;
import java.util.*;

public record EconomicValuation(
        String objectName,
        EconomicGoodType goodType,
        EconomicValuationStatus status,
        OptionalLong priceValeritas,
        String priceRationale
) {
    public EconomicValuation {
        if (objectName == null || objectName.isBlank()) throw new IllegalArgumentException("Objeto económico sin nombre.");
        objectName = objectName.trim();
        Objects.requireNonNull(goodType);
        Objects.requireNonNull(status);
        Objects.requireNonNull(priceValeritas);
        if (status == EconomicValuationStatus.PRICED && (priceValeritas.isEmpty() || priceValeritas.getAsLong() < 1))
            throw new IllegalArgumentException("Un bien tasado necesita precio positivo.");
        if (status != EconomicValuationStatus.PRICED && priceValeritas.isPresent())
            throw new IllegalArgumentException("Una tasación pendiente no puede fingir un precio.");
        if (priceRationale == null || priceRationale.isBlank())
            throw new IllegalArgumentException("Toda situación económica necesita justificación.");
        priceRationale = priceRationale.trim();
    }

    public static EconomicValuation priced(String name, EconomicGoodType type, long valeritas, String rationale) {
        return new EconomicValuation(name,type,EconomicValuationStatus.PRICED,OptionalLong.of(valeritas),rationale);
    }

    public static EconomicValuation ogcPending(String name, EconomicGoodType type, String rationale) {
        return new EconomicValuation(name,type,EconomicValuationStatus.OGC_APPRAISAL_PENDING,OptionalLong.empty(),rationale);
    }

    public static EconomicValuation personalProvenance(String name, EconomicGoodType type, String rationale) {
        return new EconomicValuation(name,type,EconomicValuationStatus.PERSONAL_PROVENANCE_NOT_FOR_ORDINARY_SALE,
                OptionalLong.empty(),rationale);
    }

    public boolean ordinarilySellable() { return status == EconomicValuationStatus.PRICED; }
    public Set<CurrencyType> acceptedCurrencies() { return EconomicTenderPolicy.acceptedCurrencies(goodType); }
}
