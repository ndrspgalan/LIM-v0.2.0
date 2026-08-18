package domain.metaprogression;
/** Siete Design Works de los manuscritos de completitud. No existe entrada visible hasta desbloqueo. */
public enum MemorarDesignWorks {
 PANOPLIA_DEL_MAESTRE("Design Works — Panoplia del Maestre","Ayudar a Rhoy a completar la panoplia con todas las armas y armaduras del juego o su memoria gráfica."),
 LIBRO_CONTABLE_DEL_INTELECTUAL("Design Works — Libro Contable del Intelectual","Ayudar a Iván a tasar todos los misceláneos canónicos tasables."),
 CANONICUM_DEL_ASPIRANTE("Design Works — Canonicum del Aspirante","Mapear todas las lagunas de la Memoria del Mundo."),
 BESTIARIUM_DE_LA_CAZADORA("Design Works — Bestiarium de la Cazadora","Vencer a cada Ferae de INTELIGENCIA obteniendo su trofeo y acariciar al menos una vez cada Ferae de CARISMA."),
 BITACORA_DE_LA_ESTRAPERLISTA("Design Works — Bitácora de la Estraperlista","Conseguir todos los abalorios y mostrárselos a la Estraperlista."),
 CUADERNO_DE_LA_DIBUJANTE("Design Works — Cuaderno de la Dibujante","Ayudar a Kiara a presenciar cada fenómeno atmosférico inusual para que pueda dibujarlo."),
 GRIMORIO_DEL_MAESTRO("Design Works — Grimorio del Maestro","Reunir todas las páginas del Grimorio del Maestro.");
 private final String label,requirement;
 MemorarDesignWorks(String label,String requirement){this.label=label;this.requirement=requirement;}
 public String label(){return label;} public String requirement(){return requirement;}
}
