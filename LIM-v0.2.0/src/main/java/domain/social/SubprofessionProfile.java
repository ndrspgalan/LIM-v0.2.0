package domain.social;

import java.util.*;

public record SubprofessionProfile(
        Subprofession subprofession,
        ProfessionIncomeKind incomeKind,
        int monthlyReferenceValeritas,
        String monthlyReferenceLabel,
        String narrativeDescription,
        boolean uniqueContemporaryHolder,
        Optional<String> contemporaryHolder
) {
    public SubprofessionProfile {
        Objects.requireNonNull(subprofession);
        Objects.requireNonNull(incomeKind);
        if(monthlyReferenceValeritas<0) throw new IllegalArgumentException("Paga negativa.");
        monthlyReferenceLabel=Objects.requireNonNull(monthlyReferenceLabel).trim();
        narrativeDescription=Objects.requireNonNull(narrativeDescription).trim();
        contemporaryHolder=Objects.requireNonNull(contemporaryHolder);
        if(monthlyReferenceLabel.isEmpty() || narrativeDescription.isEmpty())
            throw new IllegalArgumentException("Perfil de subprofesión incompleto.");
        if(uniqueContemporaryHolder != contemporaryHolder.isPresent())
            throw new IllegalArgumentException("Una subprofesión única debe identificar a su titular y viceversa.");
    }
}
