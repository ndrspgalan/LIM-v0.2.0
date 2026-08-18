package domain.combat.natural;

import domain.inventory.item.WeaponCombatAction;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Perfil corporal ofensivo independiente de especie. La IA decide acciones canónicas;
 * este perfil sólo declara cómo puede materializarlas físicamente un actor sin arma equipada.
 */
public record NaturalCombatProfile(
        String label,
        double effectiveUnarmedMassKg,
        Map<WeaponCombatAction,String> attackPresentation,
        boolean canBlock,
        boolean canDestabilize
) {
    public NaturalCombatProfile {
        if(label==null || label.isBlank()) throw new IllegalArgumentException("El perfil natural necesita etiqueta.");
        if(!Double.isFinite(effectiveUnarmedMassKg) || effectiveUnarmedMassKg<=0)
            throw new IllegalArgumentException("La masa ofensiva natural debe ser positiva y finita.");
        attackPresentation=Map.copyOf(Objects.requireNonNull(attackPresentation,"Las presentaciones no pueden ser nulas."));
        if(!attackPresentation.containsKey(WeaponCombatAction.LIGHT_ATTACK))
            throw new IllegalArgumentException("Todo perfil natural ofensivo necesita LIGHT_ATTACK.");
    }

    public Set<WeaponCombatAction> offensiveActions(){ return attackPresentation.keySet(); }
    public String presentationFor(WeaponCombatAction action){
        return attackPresentation.getOrDefault(Objects.requireNonNull(action), action.name());
    }
}
