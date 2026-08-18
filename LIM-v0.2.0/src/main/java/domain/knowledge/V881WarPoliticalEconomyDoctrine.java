package domain.knowledge;

/** Internal reason why war persists despite extremely cheap decisive V881 force. */
public final class V881WarPoliticalEconomyDoctrine {
    private V881WarPoliticalEconomyDoctrine(){}

    public static CanonVisibility visibility(){ return CanonVisibility.INTERNAL_CANON; }

    public static String truth(){
        return """
                V881 permite resolver numerosos enfrentamientos de forma rápida, barata y con pocas bajas
                directas: zepelines de gran altura, impacto cinético, cañones de riel, negación atmosférica
                y armamento individual reducen la necesidad de guerras industriales de desgaste.

                La guerra persiste porque destruir un ejército no es su única utilidad. Las capas capaces de
                promoverla la emplean para encarecer o abaratar recursos, modificar rutas, inducir migraciones,
                probar lealtades, justificar concesiones, liquidar o crear mercados, ensayar doctrinas y diseños
                V881, desplazar poblaciones y reordenar jerarquías políticas.

                La Primera Marcha Exaltada produjo el nuevo equilibrio tecnológico sin resolver quién lo
                monopolizaría. La Segunda Marcha Exaltada ocurre dentro de esa disputa. No es la vuelta de
                una guerra antigua, sino la continuación política de un estándar que volvió demasiado barata
                la violencia decisiva y demasiado valiosas sus consecuencias indirectas.
                """.strip();
    }
}
