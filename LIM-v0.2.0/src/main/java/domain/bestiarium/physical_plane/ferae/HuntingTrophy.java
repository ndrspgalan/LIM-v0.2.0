package domain.bestiarium.physical_plane.ferae;

/** Trofeos Ferae: conserva intacta la bonificación canónica de CARISMA. */
public enum HuntingTrophy {
    COLA_DE_RATA("Cola de rata", 1,
            "Cola larga y flexible, separada en una sola pieza y desecada con sal para evitar putrefacción. Se conserva enrollada o extendida entre tablillas ligeras, de modo que la piel no se cuartee durante el transporte."),
    PLUMA_DE_CUERVO("Pluma de cuervo", 2,
            "Pluma primaria de gran tamaño, seleccionada por conservar barbas completas y raquis recto. Se limpia en seco, se ahúma ligeramente y se guarda entre láminas para impedir que la humedad deforme el vexilo."),
    PEZUNA_DE_CERDO("Pezuña de cerdo", 3,
            "Pezuña íntegra recortada por encima de la cápsula córnea, hervida y raspada hasta retirar tejido blando. Una vez seca se engrasa mínimamente para evitar fisuras y se transporta envuelta en tela basta."),
    CERDA_DE_CABALLO("Cerda de caballo", 4,
            "Mechón de crin tomado de la línea dorsal del cuello y atado antes del corte para conservar la orientación de las fibras. Se lava con agua tibia, se seca bajo tensión y se enrolla alrededor de una pequeña varilla."),
    CAPARAZON_DE_ARMADILLO("Caparazón de armadillo", 5,
            "Sección dorsal completa de placas córneas, retirada sin fracturar las bandas móviles. Tras limpiar la cara interna se cura con sal y humo, conservando la articulación natural que permite plegarlo para su transporte."),
    CORNAMENTA_DE_CIERVO("Cornamenta de ciervo", 6,
            "Porción simétrica de cornamenta elegida por grosor y ausencia de grietas. La base se sierra limpia, se raspa el periostio residual y se pule sólo lo necesario para estabilizar la pieza sin borrar sus marcas de crecimiento."),
    OREJA_DE_TORO("Oreja de toro", 7,
            "Oreja completa cortada con parte de su cartílago basal para que conserve la forma. Se prensa entre paños absorbentes, se sala y se seca lentamente hasta quedar rígida sin perder la geometría del pabellón."),
    PIEL_DE_SERPIENTE("Piel de serpiente", 8,
            "Tira continua de piel obtenida desde el cuello hasta la cola, desprendida sin cortar las escamas ventrales. Se estira sobre una tabla, se cura con sal fina y se enrolla únicamente cuando ha perdido toda humedad libre."),
    COLMILLO_DE_JABALI("Colmillo de jabalí", 9,
            "Colmillo superior curvado extraído con la raíz completa para evitar que la punta se fracture. Se limpia la cavidad, se deja secar y se pule el esmalte exterior hasta eliminar aristas producidas durante la extracción."),
    OJO_DE_LINCE("Ojo de lince", 10,
            "Globo ocular conservado entero inmediatamente después de la extracción. Se sumerge en resina clara dentro de un pequeño recipiente sellado, manteniendo visibles iris, córnea y proporción original del órgano."),
    GARRAS_DE_AGUILA("Garras de águila", 11,
            "Conjunto de garras delanteras completas, retiradas con una pequeña porción del tarso para no dañar la raíz córnea. Se curan en aceite y humo y se fijan sobre una férula ligera que evita que se abran o astillen."),
    CRANEO_DE_LOBO("Cráneo de lobo", 12,
            "Cráneo completo con mandíbula, hervido y raspado hasta retirar tejido residual sin aflojar los dientes. Se blanquea de forma moderada y se inmovilizan los colmillos para que el transporte no altere la dentición."),
    CRIN_DE_LEON("Crin de león", 13,
            "Sección espesa de crin tomada con una franja estrecha de piel que mantiene unidos los folículos. La piel se cura por el reverso y el pelo se peina y ahúma, conservando el volumen sin recurrir a tintes ni rellenos."),
    ZARPA_DE_OSO("Zarpa de oso", 14,
            "Zarpa frontal completa conservada con piel, almohadillas y garras. Se retira la mayor parte del tejido profundo, se curte la envoltura y se endurecen las bases ungueales para que cada garra permanezca en su posición natural."),
    CUERNO_DE_RINOCERONTE("Cuerno de rinoceronte", 15,
            "Cuerno íntegro separado desde la base queratinosa y estabilizado antes de que las capas internas se abran. Se limpia en seco, se sella la superficie basal y se transporta en soporte rígido por su masa y su tendencia a fisurarse." );

    private final String label;
    private final int charismaBonus;
    private final String narrativeDescription;

    HuntingTrophy(String label, int charismaBonus, String narrativeDescription) {
        this.label = label;
        this.charismaBonus = charismaBonus;
        this.narrativeDescription = narrativeDescription;
    }

    public String label() { return label; }
    public int charismaBonus() { return charismaBonus; }
    public String narrativeDescription() { return narrativeDescription; }
}
