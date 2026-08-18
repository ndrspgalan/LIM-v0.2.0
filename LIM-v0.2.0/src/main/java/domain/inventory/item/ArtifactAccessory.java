package domain.inventory.item;

import domain.character.sheet.Attribute;

/**  — abalorio tecnológico activable mediante interacción contextual. */
public interface ArtifactAccessory {
    String artifactId();
    Attribute activationAttribute();
    int activationMinimum();
}
