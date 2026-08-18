package domain.bestiarium.physical_plane.ancient;

import domain.ability.MasteryId;
import domain.ability.TransmutationNodeId;
import domain.character.CharacterClass;
import java.util.*;

/**
 * Un ANCIENT conserva las maestrías especializadas de su clase. Además posee sólo progresiones
 * universales cuyo desbloqueo depende del atributo por sí mismo. Los requisitos biográficos
 * (exposición, trofeo, hazaña, descubrimiento, sufrimiento previo, etc.) no se fabrican.
 */
public final class AncientMasteryPolicy {
    private AncientMasteryPolicy() {}

    public static AncientMasteryProfile resolve(AncientArchetype a) {
        EnumSet<MasteryId> ids=EnumSet.of(MasteryId.ELECTROGENESIS, MasteryId.TRIBOGENESIS,
                MasteryId.INVISIBILIDAD, MasteryId.SANAR, MasteryId.INCITAR);
        Set<String> stages=new LinkedHashSet<>(List.of("INVISIBILIDAD","DRENAR","RESTAURAR","CUSTODIA"));
        if(a.sex()==AncientSex.HOMBRE || a.sex()==AncientSex.HERMAFRODITA) {
            stages.add("PROVOCAR"); stages.add("GRITO DE GUERRA");
        }
        if(a.sex()==AncientSex.MUJER || a.sex()==AncientSex.HERMAFRODITA) {
            stages.add("CAPITALIZAR"); stages.add("RENTABILIZAR");
        }
        EnumSet<TransmutationNodeId> nodes=EnumSet.noneOf(TransmutationNodeId.class);
        switch(a.characterClass()) {
            case LUCHADOR -> ids.add(MasteryId.INCITAR);
            case INTELECTUAL -> ids.add(MasteryId.EMPATIA_ANIMAL);
            case INDOMITO -> ids.addAll(List.of(MasteryId.PULSION,MasteryId.EXPLOSION_CINETICA,MasteryId.LIBERACION_HELICOIDAL,MasteryId.ANULACION));
            case ESPECIALISTA -> ids.addAll(List.of(MasteryId.HOMEOSTASIS_TERMICA,MasteryId.TRAYECTORIA_CONVERGENTE,MasteryId.INVISIBILIDAD));
            case APODERADO -> ids.addAll(List.of(MasteryId.SANAR,MasteryId.REGENERACION_THETA,MasteryId.ESPIRITU_INFATIGABLE));
            case HERALDO -> ids.add(MasteryId.INCITAR);
            case MAESTRO -> {
                ids.add(MasteryId.TRANSMUTACION);
                nodes.addAll(EnumSet.allOf(TransmutationNodeId.class));
            }
        }
        return new AncientMasteryProfile(ids,stages,nodes,List.of(
                "Las maestrías especializadas de la clase se conservan como conocimiento canónico del individuo.",
                "ELECTROGÉNESIS y TRIBOGÉNESIS aparecen por VITALIDAD 120 y ADAPTABILIDAD canónica del arquetipo.",
                "INVISIBILIDAD y las manifestaciones atributivas de SANAR/INCITAR se reconocen por sus umbrales puros.",
                "No se conceden por nivel requisitos históricos no cumplidos: exposiciones, trofeos, repeticiones, descubrimientos o sufrimiento previo."
        ));
    }
}
