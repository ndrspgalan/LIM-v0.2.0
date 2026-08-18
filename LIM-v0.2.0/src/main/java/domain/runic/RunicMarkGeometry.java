package domain.runic;

/** geometría canónica visible de cada Marca Rúnica. */
public enum RunicMarkGeometry {
    PARHELIO_TRIPLE_SOLAR("triple disco solar enlazado"),
    ROSA_OCHO_RUMBOS("estrella direccional de ocho puntas"),
    RESONANCIA_CONCENTRICA("dos circunferencias concéntricas atravesadas por un eje de impacto"),
    SILENCIO_ANILLO_FONEMICO_ROTO("anillo fonémico interrumpido en segmentos"),
    ESPEJO_ROMBO_BILATERAL("rombo de simetría bilateral sobre eje especular"),
    VOTO_HEXAGONO_LIGADO("hexágono cerrado por una diagonal vinculante"),
    TRANSPOSICION_CRISOL_CONCENTRICO("círculo de transposición con crisol central y tres anillos concéntricos");

    private final String label;
    RunicMarkGeometry(String label){this.label=label;}
    public String label(){return label;}
}
