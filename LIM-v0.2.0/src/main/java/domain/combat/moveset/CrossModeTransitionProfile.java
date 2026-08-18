package domain.combat.moveset;

import java.util.*;
public record CrossModeTransitionProfile(List<CrossModeAttackTransition> transitions) {
    public CrossModeTransitionProfile { transitions=List.copyOf(Objects.requireNonNull(transitions)); }
    public Optional<CrossModeAttackTransition> transition(ModeAttackRef from, ModeAttackRef to){ return transitions.stream().filter(t->t.from().equals(from)&&t.to().equals(to)).findFirst(); }
}
