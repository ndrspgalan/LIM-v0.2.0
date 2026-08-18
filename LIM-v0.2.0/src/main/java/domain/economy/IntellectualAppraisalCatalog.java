package domain.economy;
import domain.inventory.catalog.PhysicalObjectCatalog;
import java.util.*;
/**  — universo que Iván debe tasar para el Design Works del Libro Contable. */
public final class IntellectualAppraisalCatalog {
 private IntellectualAppraisalCatalog(){}
 public static Set<String> requiredNames(){LinkedHashSet<String> r=new LinkedHashSet<>();r.addAll(MiscellaneousEconomicCatalog.all().keySet());r.addAll(AmmunitionEconomicCatalog.persistentNames());r.addAll(AmmunitionEconomicCatalog.unitaryNames());r.addAll(FirearmAccessoryEconomicCatalog.all().keySet());InventoryExpanderEconomicCatalog.all().values().forEach(v->r.add(v.objectName()));
  // Munición/proyectiles y expansores pertenecen al encargo aunque vivan en catálogos económicos especializados.
  // Mucus (Lágrimas/Cristales), monedas y abalorios quedan fuera expresamente.
  PhysicalObjectCatalog.all().stream().filter(d->Set.of("ammunition","firearmAccessory").contains(d.family())).map(d->d.displayName()).forEach(r::add);
  r.removeIf(n->n.toLowerCase(Locale.ROOT).contains("mucus")||n.equals("Valeritas")||n.equals("Sueldos")||n.equals("Berylares")||n.equals("Reales A5"));return Set.copyOf(r);}
 public static boolean complete(Set<String> appraisedNames){return appraisedNames!=null&&appraisedNames.containsAll(requiredNames());}
}
