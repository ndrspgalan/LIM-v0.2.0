package domain.inventory.item.armor;

import domain.economy.EconomicGoodType;
import domain.economy.EconomicTenderPolicy;
import domain.inventory.item.misc.CurrencyType;
import domain.social.Profession;
import java.util.*;

public record MaterialMarketProfile(
        int referencePriceValeritasPerUnit,
        EconomicGoodType economicGoodType,
        String marketNarrative,
        Set<Profession> interestedProfessions
) {
    public Set<CurrencyType> acceptedCurrencies(){ return EconomicTenderPolicy.acceptedCurrencies(economicGoodType); }

    public MaterialMarketProfile {
        if (referencePriceValeritasPerUnit < 0) throw new IllegalArgumentException("Precio material negativo.");
        economicGoodType = Objects.requireNonNull(economicGoodType);
        marketNarrative = Objects.requireNonNull(marketNarrative).trim();
        if (marketNarrative.isEmpty()) throw new IllegalArgumentException("Justificación de mercado vacía.");
        interestedProfessions = Collections.unmodifiableSet(EnumSet.copyOf(interestedProfessions));
    }
}
