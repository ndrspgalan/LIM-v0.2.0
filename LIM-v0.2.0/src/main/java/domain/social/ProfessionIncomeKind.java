package domain.social;

/** Naturaleza del ingreso mensual de referencia. No implementa transacciones ni sustituye SEV. */
public enum ProfessionIncomeKind {
    NONE("Sin ingreso profesional de referencia"),
    SALARY("Salario mensual de referencia"),
    VARIABLE_INCOME("Ingreso mensual de referencia"),
    PATRIMONIAL_RENT("Renta mensual de referencia");

    private final String label;
    ProfessionIncomeKind(String label){ this.label=label; }
    public String label(){ return label; }
}
