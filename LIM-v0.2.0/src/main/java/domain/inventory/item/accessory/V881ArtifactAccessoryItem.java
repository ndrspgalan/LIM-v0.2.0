package domain.inventory.item.accessory;

import domain.character.sheet.Attribute;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;
import java.util.List;

/**  — artefacto V881 cuyo segundo uso no se revela en UI hasta comprenderlo. */
public final class V881ArtifactAccessoryItem extends AccessoryItem implements ArtifactAccessory {
    private final String artifactId;
    public V881ArtifactAccessoryItem(String id,String name,String narrative,double weight,int vertical,int horizontal){
        super(name,narrative,weight,new InventoryFootprint(vertical,horizontal),List.of(),
                List.of(ItemProperty.hiddenWithHiddenRequirement(ItemPropertyId.GENERIC,"FUNCIÓN V881",narrative,
                        Attribute.CLARIVIDENCIA,22,"ACTIVACIÓN | E")),
                List.of(AccessoryEffect.hidden("FUNCIÓN V881",AccessoryEffectType.ARTIFACT_ACTIVATION,Attribute.CLARIVIDENCIA,22,1)));
        this.artifactId=id;
    }
    @Override public String artifactId(){return artifactId;}
    @Override public Attribute activationAttribute(){return Attribute.CLARIVIDENCIA;}
    @Override public int activationMinimum(){return 22;}
}
