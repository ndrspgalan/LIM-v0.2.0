package domain.economy;

/** Estado económico explícito: nunca se representa una ausencia de precio mediante null. */
public enum EconomicValuationStatus {
    PRICED,
    OGC_APPRAISAL_PENDING,
    PERSONAL_PROVENANCE_NOT_FOR_ORDINARY_SALE
}
