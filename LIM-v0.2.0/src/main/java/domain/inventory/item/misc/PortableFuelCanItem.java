package domain.inventory.item.misc;
import domain.inventory.*;import domain.inventory.logistics.MotorcycleFuelType;import java.util.List;
public final class PortableFuelCanItem extends InventoryEntry {
 public static final double CAPACITY_LITERS=1.0; private final MotorcycleFuelType fuelType;
 public PortableFuelCanItem(MotorcycleFuelType type){super(name(type),description(type),weight(type),new InventoryFootprint(3,2),List.of("CAPACIDAD | 1 litro","TAMAÑO | 3×2 slots","COMBUSTIBLE | "+type));this.fuelType=type;}
 public MotorcycleFuelType fuelType(){return fuelType;}
 private static String name(MotorcycleFuelType t){return t==MotorcycleFuelType.ETHANOL?"Bidón de Etanol":"Bidón de Queroseno Ligero";}
 private static double weight(MotorcycleFuelType t){return t==MotorcycleFuelType.ETHANOL?1.15:1.30;}
 private static String description(MotorcycleFuelType t){return t==MotorcycleFuelType.ETHANOL?"Recipiente portátil reforzado con capacidad para un litro de etanol obtenido mediante fermentación y destilación de patata. Permite reabastecer la Motocicleta Cardán V881 allí donde no existe una instalación de suministro.":"Recipiente portátil reforzado con capacidad para un litro de queroseno ligero. Su cierre y cuello estrecho reducen derrames durante el transporte y permiten reabastecer la Motocicleta Cardán V881 fuera de una instalación de suministro.";}
}
