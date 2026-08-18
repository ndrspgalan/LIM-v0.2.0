package domain.maintenance;

import domain.combat.HostileEncounterState;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.item.armor.*;
import domain.inventory.item.misc.*;
import domain.inventory.item.rangedWeapons.RangedWeaponItem;
import domain.inventory.item.WeaponItem;
import java.util.List;

public final class MaintenanceService {
    private final MaintenanceAvailabilityPolicy availability = new MaintenanceAvailabilityPolicy();
    private final MaterialRepairPolicy materialRepair = new MaterialRepairPolicy();
    private final ArmorRepairMaterialPolicy armorMaterials = new ArmorRepairMaterialPolicy();

    public MaintenanceResult repairRanged(RangedWeaponItem weapon, ResinJarItem resin, InventoryState inventory,
                                          HostileEncounterState encounter) {
        if (!availability.canBegin(weapon, resin, inventory, encounter)) return MaintenanceResult.rejected("La reparación exige arma equipada, resina en acceso rápido.", MaintenanceAction.REPAIR_WITH_RESIN);
        if (resin.isDepleted() || !weapon.isDegraded()) return MaintenanceResult.rejected("No hay resina o el arma no está degradada.", MaintenanceAction.REPAIR_WITH_RESIN);
        if (!weapon.repairWithResin()) return MaintenanceResult.rejected("El arma no admite resina.", MaintenanceAction.REPAIR_WITH_RESIN);
        resin.consumeOne();
        return MaintenanceResult.completed(MaintenanceAction.REPAIR_WITH_RESIN, resin.useAnimation());
    }

    public MaintenanceResult repairArmor(ArmorPiece armor, ReusableRepairToolItem tool, InventoryState inventory,
                                         HostileEncounterState encounter) {
        return repairArmor(armor, (InventoryEntry) tool, inventory, encounter);
    }

    /**
     * : herramienta correcta + una unidad de cada material degradable constituyente.
     * La pieza puede repararse desde el primer punto de desgaste; no necesita quedar agotada.
     */
    public MaintenanceResult repairArmor(ArmorPiece armor, InventoryEntry tool, InventoryState inventory,
                                         HostileEncounterState encounter) {
        if (!availability.canBegin(armor, tool, inventory, encounter))
            return MaintenanceResult.rejected("La reparación exige armadura equipada y herramienta en acceso rápido.", MaintenanceAction.REPAIR_WITH_TOOLBOX);
        if (!armor.needsMaintenance())
            return MaintenanceResult.rejected("La armadura no presenta desgaste ni una condición reparable.", MaintenanceAction.REPAIR_WITH_TOOLBOX);
        if (armor.containsMaterial(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE))
            return MaintenanceResult.rejected("El compuesto electromecánico exige la Maletín profesional de Alicia e Iván y Líquido Refrigerante.", MaintenanceAction.REPAIR_WITH_TOOLBOX);

        var required = armorMaterials.requiredMaterials(armor);
        boolean compatible = !required.isEmpty() && required.stream()
                .allMatch(m -> materialRepair.accepts(m, MaterialRepairPolicy.ItemCategory.ARMOR, tool));
        if (!compatible)
            return MaintenanceResult.rejected("La herramienta no es compatible con todos los materiales reparables.", MaintenanceAction.REPAIR_WITH_TOOLBOX);
        if (!armorMaterials.hasRequiredMaterials(armor, inventory))
            return MaintenanceResult.rejected("Faltan materiales constituyentes para efectuar la reparación.", MaintenanceAction.REPAIR_WITH_TOOLBOX);
        if (!armorMaterials.consumeRequiredMaterials(armor, inventory))
            return MaintenanceResult.rejected("No se pudieron consumir los materiales de reparación.", MaintenanceAction.REPAIR_WITH_TOOLBOX);

        armor.restoreProtectionFully();
        armor.clearWetCondition();
        return MaintenanceResult.completed(MaintenanceAction.REPAIR_WITH_TOOLBOX,
                new UseAnimation(12, List.of("Retirar la armadura equipada", "Desplegar la herramienta", "Reponer el material deteriorado", "Reparar costuras, placas y uniones", "Volver a equiparla")));
    }

    public MaintenanceResult repairElectromechanical(ArmorPiece armor, PortableLaboratoryItem toolbox,
                                                      CoolantBottleItem coolant, InventoryState inventory,
                                                      HostileEncounterState encounter) {
        if (!availability.canBegin(armor, toolbox, inventory, encounter)) return MaintenanceResult.rejected("La reparación exige el conjunto equipado y la Maletín profesional de Alicia e Iván en acceso rápido.", MaintenanceAction.REPAIR_WITH_COOLANT);
        if (!armor.containsMaterial(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE) || coolant.isDepleted() || !armor.isDegraded()) return MaintenanceResult.rejected("El objetivo o el Líquido Refrigerante no son válidos.", MaintenanceAction.REPAIR_WITH_COOLANT);
        if (!armorMaterials.hasRequiredMaterials(armor, inventory)) return MaintenanceResult.rejected("Falta el material electromecánico constituyente necesario.", MaintenanceAction.REPAIR_WITH_COOLANT);
        if (!armorMaterials.consumeRequiredMaterials(armor, inventory)) return MaintenanceResult.rejected("No se pudo consumir el material electromecánico.", MaintenanceAction.REPAIR_WITH_COOLANT);
        armor.restoreProtectionFully(); coolant.consumeOne();
        return MaintenanceResult.completed(MaintenanceAction.REPAIR_WITH_COOLANT,
                new UseAnimation(18, List.of("Abrir la Maletín profesional de Alicia e Iván", "Aislar los circuitos dañados", "Reponer el módulo deteriorado", "Aplicar una unidad de Líquido Refrigerante", "Calibrar y cerrar el conjunto")));
    }

    public MaintenanceResult sharpen(WeaponItem weapon, UtilityObjectItem whetstone, InventoryState inventory,
                                     HostileEncounterState encounter) {
        if (!availability.canBegin(weapon, whetstone, inventory, encounter)) return MaintenanceResult.rejected("El afilado exige arma equipada, piedra en acceso rápido.", MaintenanceAction.SHARPEN);
        WhetstoneResult result = new WhetstonePolicy().sharpenDetailed(whetstone, weapon);
        return result.successful() ? MaintenanceResult.completed(MaintenanceAction.SHARPEN, whetstone.useAnimation()) : MaintenanceResult.rejected("El arma no admite afilado.", MaintenanceAction.SHARPEN);
    }

    public boolean refillCoolant(CoolantBottleItem bottle, StackableMiscellaneousItem waterskin,
                                 StackableMiscellaneousItem mead, PortableLaboratoryItem toolbox) {
        if (bottle.isFull() || toolbox == null || !toolbox.name().equals("Maletín profesional de Alicia e Iván")) return false;
        if (!CoolantRecipePolicy.consumeInputs(waterskin,mead)) return false;
        return bottle.addUnits(1);
    }
}
