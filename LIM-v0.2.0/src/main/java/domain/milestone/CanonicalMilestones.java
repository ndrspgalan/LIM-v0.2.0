package domain.milestone;
public final class CanonicalMilestones {
 private CanonicalMilestones(){}
 public static PersonaMilestone configuratioOriginalis(){return new PersonaMilestone("configuratio-originalis","[CONFIGURATIO ORIGINALIS]","Desbloquea ELECTROGÉNESIS y TRIBOGÉNESIS.",true);}
 public static PersonaMilestone portadorDeSuenos(){return new PersonaMilestone("portador-de-suenos","[EL PORTADOR DE SUEÑOS]","Completa el arco de La Última Luz.",true);}
 public static PersonaMilestone flammarion(){return new PersonaMilestone("flammarion","[FLAMMARION]","Completa La Edad de Hierro.",true);}
 public static PersonaMilestone ancoraEncarnada(){return new PersonaMilestone("ancora-encarnada","[ÁNCORA ENCARNADA]","Toma consciencia de ti mismo.",true);}
 public static PersonaMilestone advenimiento(){return new PersonaMilestone("advenimiento","[ADVENIMIENTO]","Completa La Segunda Marcha Exaltada.",true);}
 public static PersonaMilestone envejecemosJuntos(){return new PersonaMilestone("envejecemos-juntos","[¿ENVEJECEMOS JUNTOS?]","Empareja a Kenan con Kiara.",true);}
 public static PersonaMilestone laJugadaFinal(){return new PersonaMilestone("la-jugada-final","[LA JUGADA FINAL]","Alcanza La Espiral de Locura que Envuelve este Mundo.",true);}
 public static PersonaMilestone voluntadMayor(String text){return new PersonaMilestone("greater-will","[LA VOLUNTAD MAYOR]",text,true);}
 public static PersonaMilestone familia(){return new PersonaMilestone("familia","[FAMILIA]","Al terminar el juego, Kenan, Kiara, Jacob, Iván, Alicia, Rhoy y Sofía siguen vivos y unidos.",true);}
 public static PersonaMilestone productoDeUnRecuerdo(){return new PersonaMilestone("producto-de-un-recuerdo","[EL PRODUCTO DE UN RECUERDO]","En el postgame, Kenan y Kiara forman una familia.",true);}
}
