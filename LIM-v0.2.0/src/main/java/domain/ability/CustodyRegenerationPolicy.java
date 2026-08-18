package domain.ability;
import domain.social.RelationshipType; import java.util.*;
/** CUSTODIA: campo invisible de PV REGEN compartido, sin repulsión. */
public final class CustodyRegenerationPolicy {
 public static boolean eligible(RelationshipType r){return r==RelationshipType.RELIABLE||r==RelationshipType.FRIENDLY||r==RelationshipType.ROMANTIC;}
 public Result resolve(double radiusMeters,List<Member> members){double sum=members.stream().filter(m->m.distanceMeters()<=radiusMeters&&eligible(m.relationship())).mapToDouble(Member::healthRegen).sum();return new Result(sum,true,members.stream().filter(m->m.distanceMeters()<=radiusMeters&&eligible(m.relationship())).map(Member::id).toList());}
 public record Member(String id,RelationshipType relationship,double distanceMeters,double healthRegen){} public record Result(double sharedHealthRegen,boolean inhibitionImmune,List<String> beneficiaries){}
}
