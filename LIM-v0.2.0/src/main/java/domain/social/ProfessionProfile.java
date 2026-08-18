package domain.social;

import java.util.Objects;

/**
 * Perfil socioeconómico base .
 * Los valores están expresados en Valeritas como unidad contable interna exclusivamente para
 * comparar referencias; el texto visible conserva Sueldos/Berylare cuando corresponde.
 */
public record ProfessionProfile(
        Profession profession,
        String narrativeDescription,
        ProfessionIncomeKind incomeKind,
        int monthlyReferenceValeritas,
        String monthlyReferenceLabel
) {
    public ProfessionProfile {
        Objects.requireNonNull(profession);
        narrativeDescription=Objects.requireNonNull(narrativeDescription).trim();
        Objects.requireNonNull(incomeKind);
        monthlyReferenceLabel=Objects.requireNonNull(monthlyReferenceLabel).trim();
        if(narrativeDescription.isEmpty()) throw new IllegalArgumentException("Descripción profesional vacía.");
        if(monthlyReferenceValeritas<0) throw new IllegalArgumentException("Ingreso profesional negativo.");
        if(monthlyReferenceLabel.isEmpty()) throw new IllegalArgumentException("Etiqueta salarial vacía.");
        if(incomeKind==ProfessionIncomeKind.NONE && monthlyReferenceValeritas!=0)
            throw new IllegalArgumentException("Una profesión sin renta de referencia debe valer 0.");
    }
}
