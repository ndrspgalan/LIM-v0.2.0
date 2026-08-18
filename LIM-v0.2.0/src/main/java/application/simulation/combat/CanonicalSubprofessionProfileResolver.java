package application.simulation.combat;

import domain.character.CharacterClass;
import domain.social.*;
import java.util.*;

/** Adaptador GOLD sobre las autoridades ; no fabrica hojas ni niveles. */
final class CanonicalSubprofessionProfileResolver {
    private CanonicalSubprofessionProfileResolver(){}
    static List<CanonicalSubprofessionProfile> active(Subprofession s){
        Map<CharacterClass,CanonicalSubprofessionProfile> m = switch(s.profession()){
            case BEGGAR -> BeggarCanonicalProfiles.activeProfiles(s);
            case BLACKSMITH -> BlacksmithCanonicalProfiles.activeProfiles(s);
            case CARPENTER -> CarpenterCanonicalProfiles.activeProfiles(s);
            case COURTESAN -> CourtesanCanonicalProfiles.activeProfiles(s);
            case DAY_LABORER -> DayLaborerCanonicalProfiles.activeProfiles(s);
            case DRESSMAKER -> DressmakerCanonicalProfiles.activeProfiles(s);
            case FAIRGROUND_WORKER -> FairgroundWorkerCanonicalProfiles.activeProfiles(s);
            case HAIRDRESSER -> HairdresserCanonicalProfiles.activeProfiles(s);
            case HUNTER -> HunterCanonicalProfiles.activeProfiles(s);
            case JURIST -> JuristCanonicalProfiles.profiles(s);
            case MERCENARY -> MercenaryCanonicalProfiles.activeProfiles(s);
            case MERCHANT -> MerchantCanonicalProfiles.profiles(s);
            case NOBLE -> NobleCanonicalProfiles.profiles(s);
            case SAILOR -> SailorCanonicalProfiles.activeProfiles(s);
            case SOLDIER -> SoldierCanonicalProfiles.activeProfiles(s);
            case STONEMASON -> StonemasonCanonicalProfiles.activeProfiles(s);
            case TANNER -> TannerCanonicalProfiles.activeProfiles(s);
            case TEACHER -> TeacherCanonicalProfiles.activeProfiles(s);
            case EBONY_WARRIOR -> Map.of(); // titular canónico único: fuera del endpoint MDPAR.
        };
        return m.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparingInt(Enum::ordinal))).map(Map.Entry::getValue).toList();
    }
}
