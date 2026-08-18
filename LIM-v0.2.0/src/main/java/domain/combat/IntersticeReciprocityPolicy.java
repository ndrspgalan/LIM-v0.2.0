package domain.combat;
/** MIRROR'S EDGE: reciprocidad irreversible con el Intersticio. */
public final class IntersticeReciprocityPolicy {
 public record Reciprocity(boolean actorCanDamageIntersticeNormally,boolean intersticeCanDamageActorNormally){}
 public Reciprocity resolve(boolean mirrorsEdgeUnlocked){return new Reciprocity(mirrorsEdgeUnlocked,mirrorsEdgeUnlocked);}
}
