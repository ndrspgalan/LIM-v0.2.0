package domain.metaprogression;
/** OST visibles en MEMORAR sólo después de obtener su hito. */
public enum MemorarSoundtrack {
 PORTADOR_DE_SUENOS("portador-de-suenos","OST — El Portador de Sueños"),
 FLAMMARION("flammarion","OST — Flammarion"),
 CONFIGURATIO_ORIGINALIS("configuratio-originalis","OST — Configuratio Originalis"),
 ANCORA_ENCARNADA("ancora-encarnada","OST — Áncora Encarnada"),
 ADVENIMIENTO("advenimiento","OST — Advenimiento"),
 ENVEJECEMOS_JUNTOS("envejecemos-juntos","OST — ¿Envejecemos juntos?"),
 LA_JUGADA_FINAL("la-jugada-final","OST — La Jugada Final"),
 VOLUNTAD_MAYOR("greater-will","OST — La Voluntad Mayor"),
 FAMILIA("familia","OST — Familia"),
 PRODUCTO_DE_UN_RECUERDO("producto-de-un-recuerdo","OST — El Producto de un Recuerdo");
 private final String milestoneId,label;
 MemorarSoundtrack(String milestoneId,String label){this.milestoneId=milestoneId;this.label=label;}
 public String milestoneId(){return milestoneId;} public String label(){return label;}
 public static java.util.Optional<MemorarSoundtrack> forMilestone(domain.milestone.PersonaMilestone m){if(m==null)return java.util.Optional.empty();return java.util.Arrays.stream(values()).filter(s->s.milestoneId.equals(m.id())).findFirst();}
}
