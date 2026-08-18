package domain.economy;

import domain.inventory.item.misc.CurrencyType;
import java.util.*;

/**
 * Denominación comercial legítima por naturaleza del bien.
 * La equivalencia matemática entre monedas no autoriza a denominar cualquier compraventa en cualquiera de ellas.
 */
public final class EconomicTenderPolicy {
    public static final long VALERITAS_PER_SUELDO = 1_000L;
    public static final long SUELDOS_PER_BERYLARE = 210L;
    public static final long VALERITAS_PER_BERYLARE = VALERITAS_PER_SUELDO * SUELDOS_PER_BERYLARE;
    public static final long BERYLARES_PER_REAL_A5 = 2L;
    public static final long VALERITAS_PER_REAL_A5 = VALERITAS_PER_BERYLARE * BERYLARES_PER_REAL_A5;

    private EconomicTenderPolicy(){}

    public static Set<CurrencyType> acceptedCurrencies(EconomicGoodType type) {
        Objects.requireNonNull(type);
        return switch (type) {
            case FIRST_NECESSITY -> Set.of(CurrencyType.VALERITA, CurrencyType.SUELDO);
            case SOCIAL_INTEREST -> Set.of(CurrencyType.VALERITA, CurrencyType.SUELDO, CurrencyType.BERYLARE);
            case PRIVATE_USE -> Set.of(CurrencyType.VALERITA, CurrencyType.SUELDO, CurrencyType.BERYLARE, CurrencyType.REAL_A5);
        };
    }

    public static boolean denominationAllowed(EconomicGoodType type, CurrencyType currency) {
        return acceptedCurrencies(type).contains(Objects.requireNonNull(currency));
    }
}
