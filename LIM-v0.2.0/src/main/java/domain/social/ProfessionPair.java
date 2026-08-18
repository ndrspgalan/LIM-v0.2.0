package domain.social;

import java.util.Objects;

/** Par no ordenado: (A,B) y (B,A) representan exactamente la misma afinidad. */
public record ProfessionPair(Profession first, Profession second) {
    public ProfessionPair {
        first = Profession.canonicalOrBeggar(first);
        second = Profession.canonicalOrBeggar(second);
        if (first.ordinal() > second.ordinal()) {
            Profession swap = first; first = second; second = swap;
        }
    }
    public static ProfessionPair of(Profession first, Profession second) {
        return new ProfessionPair(first, second);
    }
}
