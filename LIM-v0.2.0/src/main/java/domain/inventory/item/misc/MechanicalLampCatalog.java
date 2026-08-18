package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import java.util.List;

/** Catálogo  de iluminación mecánica sin baterías. */
public final class MechanicalLampCatalog {
    private MechanicalLampCatalog(){}
    public static MechanicalLampItem magnetlampe(){
        return new MechanicalLampItem(
                "MAGNETLAMPE",
                "Linterna mecánica compacta preparada para colgarse de la coraza. Un anillo exterior tira de un cordón unido a un resorte y a un volante de inercia que hace girar un pequeño generador magnético. Un tirón firme produce una luz amarilla durante cinco segundos sin depender de pilas.",
                0.320, new InventoryFootprint(2,2), MechanicalLampItem.Mechanism.PULL_CORD, 2.5, 5.0,
                new UseAnimation(0.7, List.of("Aferrar el anillo exterior.","Tirar con fuerza hacia abajo.","Soltar el cordón mientras el volante mantiene el generador girando.")));
    }
    public static MechanicalLampItem knijpkat(){
        return new MechanicalLampItem(
                "KNIJPKAT",
                "Linterna mecánica de cuerpo metálico concebida para fijarse a la coraza. Una palanca superior mueve un tren de engranajes que acelera una dinamo interna; al comprimirla repetidamente con la mano mantiene una luz amarilla continua mientras se bombea.",
                0.142, new InventoryFootprint(1,1), MechanicalLampItem.Mechanism.SQUEEZE_DYNAMO, 4.5, 0.0,
                new UseAnimation(0.35, List.of("Cerrar la mano sobre la palanca.","Comprimirla contra el cuerpo de la linterna.","Repetir el bombeo para mantener la dinamo girando.")));
    }
}
