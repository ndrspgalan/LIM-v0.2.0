package domain.inventory.item;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.knowledge.PropertyKnowledgePolicy;

import java.util.Objects;
import java.util.Optional;

public record AccessoryEffect(
        String propertyName,
        AccessoryEffectType type,
        Attribute affectedAttribute,
        double amount,
        boolean hidden,
        Attribute activationAttribute,
        int activationMinimum
) {
    public AccessoryEffect {
        Objects.requireNonNull(propertyName, "El nombre de la propiedad no puede ser nulo.");
        if (propertyName.isBlank()) throw new IllegalArgumentException("El nombre de la propiedad no puede estar vacío.");
        Objects.requireNonNull(type, "El tipo de efecto no puede ser nulo.");
        if (!Double.isFinite(amount)) throw new IllegalArgumentException("La magnitud del efecto debe ser finita.");
        if (type == AccessoryEffectType.ATTRIBUTE_BONUS && affectedAttribute == null) {
            throw new IllegalArgumentException("Una bonificación de atributo debe indicar el atributo afectado.");
        }
        if (activationAttribute == null && activationMinimum != 0) {
            throw new IllegalArgumentException("Un efecto sin requisito debe usar umbral cero.");
        }
        if (activationAttribute != null && (activationMinimum < 1 || activationMinimum > 120)) {
            throw new IllegalArgumentException("El umbral de activación debe estar entre 1 y 120.");
        }
    }

    public static AccessoryEffect always(String propertyName, AccessoryEffectType type, double amount) {
        return new AccessoryEffect(propertyName, type, null, amount, false, null, 0);
    }

    public static AccessoryEffect attribute(String propertyName, Attribute attribute, double amount) {
        return new AccessoryEffect(propertyName, AccessoryEffectType.ATTRIBUTE_BONUS, attribute, amount, false, null, 0);
    }

    public static AccessoryEffect hidden(String propertyName, AccessoryEffectType type, Attribute activationAttribute,
                                         int activationMinimum, double amount) {
        return new AccessoryEffect(propertyName, type, null, amount, true, activationAttribute, activationMinimum);
    }

    public boolean isVisibleTo(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return PropertyKnowledgePolicy.visible(sheet,hidden,activationAttribute,activationMinimum);
    }

    public boolean isActiveFor(CharacterSheet sheet) {
        if (!isVisibleTo(sheet)) return false;
        return activationAttribute == null || PropertyKnowledgePolicy.requirementMet(sheet,activationAttribute,activationMinimum);
    }

    public Optional<Attribute> affectedAttributeOptional() { return Optional.ofNullable(affectedAttribute); }
}
