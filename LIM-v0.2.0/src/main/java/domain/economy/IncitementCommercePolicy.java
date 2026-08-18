package domain.economy;

import domain.character.Gender;
import domain.social.RelationshipType;

import java.util.Objects;

/** Contrato  de CAPITALIZAR y RENTABILIZAR. */
public final class IncitementCommercePolicy {
    private IncitementCommercePolicy() {}

    public static double capitalizeDiscountPercent(
            Gender buyerGender, Gender sellerGender, int buyerCharisma, int sellerCharisma, EconomicGoodType type) {
        Objects.requireNonNull(buyerGender); Objects.requireNonNull(sellerGender); Objects.requireNonNull(type);
        if (buyerGender != Gender.MUJER || sellerGender != Gender.HOMBRE || buyerCharisma <= sellerCharisma) return 0.0;
        int d = Math.min(10, buyerCharisma - sellerCharisma);
        return switch (type) {
            case FIRST_NECESSITY -> interpolate(1.0, 4.0, d);
            case SOCIAL_INTEREST -> interpolate(1.0, 10.0, d);
            case PRIVATE_USE -> interpolate(15.0, 30.0, d);
        };
    }

    public static double purchasePrice(double basePrice, Gender buyerGender, Gender sellerGender,
                                       int buyerCharisma, int sellerCharisma, EconomicGoodType type) {
        if (!Double.isFinite(basePrice) || basePrice < 0) throw new IllegalArgumentException("Precio base inválido.");
        double discount = capitalizeDiscountPercent(buyerGender, sellerGender, buyerCharisma, sellerCharisma, type);
        return basePrice * (1.0 - discount / 100.0);
    }

    public static double profitableSaleBonusPercent(
            Gender sellerGender, Gender buyerGender, RelationshipType relationship, EconomicGoodType type) {
        Objects.requireNonNull(sellerGender); Objects.requireNonNull(buyerGender); Objects.requireNonNull(relationship); Objects.requireNonNull(type);
        if (sellerGender != Gender.MUJER || buyerGender != Gender.HOMBRE) return 0.0;
        return switch (relationship) {
            case FRIENDLY -> switch (type) { case FIRST_NECESSITY -> 100.0; case SOCIAL_INTEREST -> 66.2; case PRIVATE_USE -> 100.0; };
            case RELIABLE -> switch (type) { case FIRST_NECESSITY -> 100.0; case SOCIAL_INTEREST -> 100.0; case PRIVATE_USE -> 49.9; };
            case INDIFFERENT -> switch (type) { case FIRST_NECESSITY -> 8.2; case SOCIAL_INTEREST -> 52.4; case PRIVATE_USE -> 0.4; };
            case DISTRUSTFUL -> switch (type) { case FIRST_NECESSITY -> 0.2; case SOCIAL_INTEREST -> 12.3; case PRIVATE_USE -> 0.0; };
            case ANTIPATHETIC -> switch (type) { case FIRST_NECESSITY -> 0.0; case SOCIAL_INTEREST -> 1.3; case PRIVATE_USE -> 0.0; };
            case HOSTILE, ROMANTIC -> 0.0;
        };
    }

    /** Incrementa el beneficio, no el valor intrínseco ni necesariamente el precio bruto. */
    public static double saleProfit(double baseProfit, Gender sellerGender, Gender buyerGender,
                                    RelationshipType relationship, EconomicGoodType type) {
        if (!Double.isFinite(baseProfit) || baseProfit < 0) throw new IllegalArgumentException("Beneficio base inválido.");
        return baseProfit * (1.0 + profitableSaleBonusPercent(sellerGender, buyerGender, relationship, type) / 100.0);
    }

    private static double interpolate(double min, double max, int charismaDifference) {
        if (charismaDifference <= 1) return min;
        if (charismaDifference >= 10) return max;
        return min + (max - min) * (charismaDifference - 1) / 9.0;
    }
}
