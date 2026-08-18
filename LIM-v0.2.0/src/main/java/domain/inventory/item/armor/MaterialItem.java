package domain.inventory.item.armor;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import java.util.*;

/** una instancia representa exactamente una unidad física de material en bruto. */
public final class MaterialItem extends InventoryEntry {
    private final ArmorMaterial material;
    private final String unitFormat;
    private final double unitWeightKg;
    private boolean consumed;

    public MaterialItem(ArmorMaterial material, String unitFormat, String description, int quantity, int maximumStack,
                        double unitWeightKg, InventoryFootprint footprint, List<String> statistics,
                        List<ItemProperty> properties) {
        super(material.label(), description, unitWeightKg, footprint, statistics, properties);
        this.material=Objects.requireNonNull(material);
        this.unitFormat=Objects.requireNonNull(unitFormat);
        if(quantity!=1 || maximumStack!=1)
            throw new IllegalArgumentException("cada MaterialItem debe representar una sola unidad física.");
        if(unitWeightKg<0) throw new IllegalArgumentException("Peso material negativo.");
        this.unitWeightKg=unitWeightKg;
    }

    public ArmorMaterial material(){ return material; }
    public String unitFormat(){ return unitFormat; }
    public double unitWeightKg(){ return unitWeightKg; }
    public int currentUses(){ return consumed?0:1; }
    public int maximumStack(){ return 1; }
    public boolean isDepleted(){ return consumed; }
    public boolean consumeOne(){ if(consumed)return false; consumed=true; return true; }
    @Override public double weightKg(){ return consumed?0.0:unitWeightKg; }
    public MaterialMarketProfile marketProfile(){ return MaterialMarketCatalog.profile(material); }
}
