package domain.metaprogression;
import domain.milestone.PersonaMilestone;
import java.util.*;
/** Live wallpapers visibles en MEMORAR sólo después de obtener su hito. */
public enum MemorarPoster {
 PORTADOR_DE_SUENOS("portador-de-suenos","Live wallpaper — El Portador de Sueños"),
 FLAMMARION("flammarion","Live wallpaper — Flammarion"),
 CONFIGURATIO_ORIGINALIS("configuratio-originalis","Live wallpaper — Configuratio Originalis"),
 ANCORA_ENCARNADA("ancora-encarnada","Live wallpaper — Áncora Encarnada"),
 ADVENIMIENTO("advenimiento","Live wallpaper — Advenimiento"),
 ENVEJECEMOS_JUNTOS("envejecemos-juntos","Live wallpaper — ¿Envejecemos juntos?"),
 LA_JUGADA_FINAL("la-jugada-final","Live wallpaper — La Jugada Final"),
 VOLUNTAD_MAYOR("greater-will","Live wallpaper — La Voluntad Mayor"),
 FAMILIA("familia","Live wallpaper — Familia"),
 PRODUCTO_DE_UN_RECUERDO("producto-de-un-recuerdo","Live wallpaper — El Producto de un Recuerdo");
 private final String milestoneId,label;
 MemorarPoster(String milestoneId,String label){this.milestoneId=milestoneId;this.label=label;}
 public String milestoneId(){return milestoneId;} public String label(){return label;}
 public static Optional<MemorarPoster> forMilestone(PersonaMilestone m){if(m==null)return Optional.empty();return Arrays.stream(values()).filter(p->p.milestoneId.equals(m.id())).findFirst();}
}
