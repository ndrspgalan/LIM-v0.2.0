package domain.social;

/** Las diecinueve profesiones canónicas del Reino de Valerian. */
public enum Profession {
    EBONY_WARRIOR("Guerrero de Ébano"), MERCHANT("Comerciante"), COURTESAN("Cortesana"),
    MERCENARY("Mercenario"), BEGGAR("Mendigo"), NOBLE("Noble"), SOLDIER("Soldado"),
    BLACKSMITH("Herrero"), CARPENTER("Carpintero"), FAIRGROUND_WORKER("Feriante"),
    TEACHER("Maestro"), JURIST("Jurista"), HUNTER("Cazador"), SAILOR("Marinero"),
    TANNER("Curtidor"), DRESSMAKER("Modista"), HAIRDRESSER("Peluquero"),
    STONEMASON("Cantero"), DAY_LABORER("Jornalero");

    private final String label;
    Profession(String label) { this.label = label; }
    public String label() { return label; }

    public ProfessionProfile profile() { return ProfessionProfileCatalog.profile(this); }
    public String narrativeDescription() { return profile().narrativeDescription(); }
    public ProfessionIncomeKind incomeKind() { return profile().incomeKind(); }
    public int monthlyReferenceValeritas() { return profile().monthlyReferenceValeritas(); }
    public String monthlyReferenceLabel() { return profile().monthlyReferenceLabel(); }
    public java.util.List<Subprofession> authoredSubprofessions() { return Subprofession.forProfession(this); }

    /** La ausencia de profesión nunca se propaga al dominio: se normaliza a Mendigo. */
    public static Profession canonicalOrBeggar(Profession profession) {
        return profession == null ? BEGGAR : profession;
    }
}
