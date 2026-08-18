package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import java.util.List;

public final class RepairToolCatalog {
    private RepairToolCatalog() {}

    public static ReusableRepairToolItem artisanBox() {
        return new ReusableRepairToolItem(ReusableRepairToolItem.Kind.ARTISAN_BOX,
                "Caja del Artesano",
                "Caja portátil de mantenimiento blando. Organiza agujas rectas y curvas, hilo de varios calibres, lezna, punzones, sacabocados, pequeñas tenazas, remaches ligeros, dedales de presión, raspadores, cuchillas, bruñidores y útiles de sujeción. Su función es recomponer costuras, parches, pliegues, juntas flexibles y capas que pueden trabajarse sin una operación metalúrgica pesada. Por ello interviene sobre tela, cuero endurecido, papel técnico, tejido mineral multicapa, caucho, caucho vulcanizado y tela dieléctrica. También conserva los útiles mecánicos sencillos empleados en bicicletas. No sustituye material consumido: la reparación sigue exigiendo una unidad del material constituyente cuando éste es degradable.",
                3.0, new InventoryFootprint(3, 2),
                List.of(
                        "USOS | Ilimitados",
                        "INSTRUMENTAL | Agujas · hilo · leznas · punzones · sacabocados · tenazas · remaches · bruñidores",
                        "REPARACIÓN | Tela · Cuero endurecido · Papel · Tejido mineral multicapa · Caucho · Caucho vulcanizado · Tela dieléctrica",
                        "MECÁNICA SIMPLE | Bicicletas",
                        "LÍMITE | No trabaja metales estructurales, vidrio laminado ni sistemas electromecánicos"
                ));
    }

    public static ReusableRepairToolItem toolbox() {
        return new ReusableRepairToolItem(ReusableRepairToolItem.Kind.TOOLBOX,
                "Caja de Herramientas",
                "Caja reforzada para mantenimiento mecánico general. Contiene martillos de bola, botadores, punzones, remachadoras, limas, sierras pequeñas, mordazas, llaves, alicates, extractores, terrajas, machos, calibres simples y útiles de ajuste capaces de enderezar, desmontar y recomponer uniones metálicas ordinarias. Puede realizar todo el trabajo de la Caja del Artesano y añade operaciones sobre bronce, acero y soportes de vidrio laminado. Sigue sin disponer de la instrumentación de aislamiento, medida, purga, laboratorio y calibración requerida por wolframio técnico o Compuesto Electromecánico.",
                6.0, new InventoryFootprint(4, 3),
                List.of(
                        "USOS | Ilimitados",
                        "INSTRUMENTAL | Martillos · botadores · remachadoras · limas · mordazas · llaves · extractores · terrajas · machos · calibres",
                        "REPARACIÓN | Todo el alcance de Caja del Artesano + Bronce · Acero · Vidrio laminado",
                        "LÍMITE | No calibra Compuesto Electromecánico ni interviene wolframio V881"
                ));
    }
}
