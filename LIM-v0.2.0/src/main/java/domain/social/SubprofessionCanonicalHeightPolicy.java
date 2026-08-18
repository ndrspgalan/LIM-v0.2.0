package domain.social;

import domain.character.Gender;
import java.util.Objects;

/**  — altura adulta de referencia para instanciar NPC procedurales de cualquier subprofesión.
 * La profesión no causa la talla: los grupos sólo proporcionan una referencia demográfica reproducible
 * cuando Level Design no suministra una antropometría individual.
 */
public final class SubprofessionCanonicalHeightPolicy {
    private SubprofessionCanonicalHeightPolicy() {}
    public static double heightMeters(Subprofession s, Gender g){
        Objects.requireNonNull(s); Objects.requireNonNull(g);
        double male=1.72, female=1.60;
        switch(s.profession()){
            case SOLDIER, MERCENARY, STONEMASON, DAY_LABORER, SAILOR -> { male=1.74; female=1.62; }
            case COURTESAN, DRESSMAKER, HAIRDRESSER -> { male=1.71; female=1.59; }
            default -> { }
        }
        switch(s){
            case COMPETITION_RIDER -> { male=1.68; female=1.57; }
            case COMPETITION_CYCLIST, TRIATHLETE, V881_MOTORCYCLE_RACER -> { male=1.73; female=1.61; }
            case STEVEDORE, HAULAGE_LABORER, EXTRACTION_MINER, STONE_SETTER, STONEWORK_MASTER,
                 V881_HEAVY_WEAPONS_SPECIALIST, INSTITUTIONAL_SHOCK_COMBATANT, CONTRACTUAL_SHOCK_COMBATANT -> { male=1.77; female=1.65; }
            case FOREST_LUMBERJACK, LIVESTOCK_KEEPER, STABLE_HAND, V881_CAMPAIGN_SAPPER -> { male=1.76; female=1.64; }
            default -> { }
        }
        return g==Gender.HOMBRE?male:female;
    }
}
