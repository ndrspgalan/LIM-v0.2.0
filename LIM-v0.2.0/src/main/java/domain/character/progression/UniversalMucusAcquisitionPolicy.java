package domain.character.progression;

import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** reward relativo y universal. El enemigo no posee un drop fijo de mucus. */
public final class UniversalMucusAcquisitionPolicy {
    private final GenderSoftcapProfile softcaps;
    private final MucusRequirementPolicy requirements;
    public UniversalMucusAcquisitionPolicy(GenderSoftcapProfile softcaps) {
        this.softcaps=Objects.requireNonNull(softcaps);
        this.requirements=new MucusRequirementPolicy(softcaps);
    }
    public MucusAcquisitionResult onEnemyDefeated(Gender playerGender, CharacterProgressionState player, CharacterSheet enemy) {
        Objects.requireNonNull(playerGender); Objects.requireNonNull(player); Objects.requireNonNull(enemy);
        MucusWallet wallet=player.mucusWallet(); EnumMap<MucusType,Integer> gained=new EnumMap<>(MucusType.class);
        for(Attribute a:Attribute.values()) {
            int current=player.sheet().valueOf(a);
            if(current>=AttributeActorCapPolicy.absoluteMaximum(AttributeActorScope.KENAN,a)) continue;
            MucusType needed=requirements.requiredForDestination(playerGender,a,current+1);
            int threshold=minimumEnemyValue(playerGender,a,needed);
            if(enemy.valueOf(a)>=threshold && wallet.quantityMlOf(needed)<needed.maximumReserveMl()) {
                wallet=wallet.addOne(needed); gained.merge(needed,1,Integer::sum);
            }
        }
        return new MucusAcquisitionResult(wallet,Map.copyOf(gained));
    }
    /** El cambio de rareza se gana ya derrotando a alguien situado exactamente en el softcap frontera. */
    public int minimumEnemyValue(Gender gender, Attribute attribute, MucusType needed) {
        if(needed.rarity()==0) return CharacterSheet.MINIMUM_ATTRIBUTE_VALUE;
        List<Integer> caps=softcaps.softcaps(gender,attribute);
        int index=needed.rarity()-1;
        if(index>=caps.size()) return caps.isEmpty()?CharacterSheet.MINIMUM_ATTRIBUTE_VALUE:caps.get(caps.size()-1);
        return caps.get(index);
    }
    public record MucusAcquisitionResult(MucusWallet wallet, Map<MucusType,Integer> gained) {}
}
