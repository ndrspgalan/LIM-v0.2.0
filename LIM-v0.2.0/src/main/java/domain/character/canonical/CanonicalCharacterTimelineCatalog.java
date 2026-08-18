package domain.character.canonical;
import domain.ability.MasteryId; import domain.character.*; import domain.social.*; import java.util.*;
/** canon de los tres timelapses. Kenan sólo está fijado aquí en CHILD; a 15/18 su progresión pertenece al jugador. */
public final class CanonicalCharacterTimelineCatalog {
 private CanonicalCharacterTimelineCatalog(){}
 private static final List<MasteryId> CHILD_MASTERIES=List.of(MasteryId.REGENERACION_THETA,MasteryId.ESPIRITU_INFATIGABLE);
 private static CanonicalAppearanceProfile a(String p,String f,String h,String e,String b,String ph,String pr){return new CanonicalAppearanceProfile(p,f,h,e,b,ph,pr);}
 private static CanonicalBodyProfile b(double h,double w,double wrist,String build){return new CanonicalBodyProfile(h,w,wrist,build);}
 private static CanonicalCharacterStageProfile p(String n,Gender g,CharacterClass c,CanonicalLifeStage s,CanonicalBodyProfile b,CanonicalAppearanceProfile a,List<Subprofession> jobs,String equipment){
  boolean child=s==CanonicalLifeStage.CHILD; return new CanonicalCharacterStageProfile(n,g,c,s,b,a,jobs,child?OptionalInt.of(9):OptionalInt.empty(),child?CHILD_MASTERIES:List.of(),child?RelationshipType.RELIABLE:RelationshipType.FRIENDLY,s!=CanonicalLifeStage.ADULT,equipment);
 }
 private static CanonicalAppearanceProfile kenan(){return a("Sereno, protector y moralmente firme; enorme autoconfianza, humor e irreverencia controlada.","Rostro joven relativamente estrecho, mandíbula definida sin ser ancha.","Castaño, medio, liso a ligeramente ondulado, mechones sueltos.","Marrones.","En adulto, barba y bigote ligeros y recortados; niño sin vello facial.","Delgado y definido, musculatura funcional sin gran volumen.","Calma vigilante que puede convertirse en desenfado seguro.");}
 private static CanonicalAppearanceProfile kiara(){return a("Alegre, afectuosa, luminosa y emocionalmente perceptiva.","Rostro fino, amable y muy expresivo.","Castaño claro a rubio oscuro, corto a media melena y desenfadado.","Claros.","Ninguno.","Pequeña, fina y proporcionada.","Expresión abierta y vivaz.");}
 private static CanonicalAppearanceProfile jacob(){return a("Enigmático, reservado, analítico y difícil de leer; parece saber más de lo que verbaliza.","Rostro fino y sobrio, facciones regulares.","Oscuro, corto y ordenado sin rigidez.","Gris parduzco.","Barba corta y discreta en adulto.","Longilíneo moderado y seco.","Elegancia contenida y mirada inescrutable.");}
 private static CanonicalAppearanceProfile elena(){return a("Vivaz, orgullosa, decidida, curiosa y elegante; fuerte voluntad bajo una presencia cálida.","Rostro ovalado de facciones vivas.","Castaño rojizo, largo, abundante y ligeramente ondulado.","Azul grisáceo.","Ninguno.","Fina, ligeramente más sólida que Kiara.","Expresiva, segura y con porte natural.");}
 private static CanonicalAppearanceProfile ivan(){return a("Curioso, brillante, idealista, algo torpe socialmente y absorbido por comprender cómo funcionan las cosas.","Rostro alargado y amable, expresión intelectual.","Castaño oscuro, corto, algo indisciplinado.","Avellana.","Barba escasa o afeitado en adulto.","Estructura media y funcional.","Atención inquisitiva; gestualidad rápida cuando explica algo.");}
 private static CanonicalAppearanceProfile alicia(){return a("Avispada, independiente, pragmática, rápida y mordaz; fuerte instinto de iniciativa.","Rostro anguloso y expresivo.","Castaño oscuro, largo o recogido funcionalmente.","Verde avellana.","Ninguno.","Ligera-media, atlética por movilidad más que por volumen.","Mirada alerta y postura preparada para moverse.");}
 private static CanonicalAppearanceProfile rhoy(){return a("Lacónico, introspectivo, observador y contenido; emoción profunda bajo exterior frío.","Rostro recto, sobrio y de mandíbula marcada.","Oscuro, corto y austero.","Gris oscuro.","Barba corta en adulto, bien contenida.","Robusto y musculoso de forma funcional.","Quietud intensa y economía gestual.");}
 private static CanonicalAppearanceProfile sofia(){return a("Ingeniosa, independiente, escéptica y verbalmente afilada; provocadora con humor seco.","Rostro expresivo de líneas suaves con mirada incisiva.","Castaño oscuro, largo, cuidado y estilizado.","Verdes.","Ninguno.","Estructura media-robusta, proporcionada.","Elegancia consciente con sonrisa irónica frecuente.");}
 public static List<CanonicalCharacterStageProfile> all(){List<CanonicalCharacterStageProfile>x=new ArrayList<>();
  x.add(p("Kenan",Gender.HOMBRE,CharacterClass.INDOMITO,CanonicalLifeStage.CHILD,b(1.16,20.5,12.0,"fina"),kenan(),List.of(),"Ropa civil infantil; sin profesión, armas ni abalorio; inventario mínimo de primera necesidad."));
  add(x,"Kiara",Gender.MUJER,CharacterClass.HERALDO,kiara(),new double[][]{{1.13,19,11.5},{1.56,46,13.8},{1.58,49,14}},Subprofession.SHOPKEEPER,Subprofession.KINGDOM_MESSENGER);
  add(x,"Jacob",Gender.HOMBRE,CharacterClass.MAESTRO,jacob(),new double[][]{{1.18,21.5,12.1},{1.68,53,14.4},{1.75,64.5,15}},Subprofession.CABINETMAKER,Subprofession.ENLIGHTENED_PATRON);
  add(x,"Iván",Gender.HOMBRE,CharacterClass.INTELECTUAL,ivan(),new double[][]{{1.19,22,12.3},{1.71,57,15.2},{1.78,69,16}},Subprofession.V881_ELECTROMECHANIC,Subprofession.MATRIX_ARCHITECT);
  add(x,"Alicia",Gender.MUJER,CharacterClass.ESPECIALISTA,alicia(),new double[][]{{1.17,21,11.8},{1.62,52,14.2},{1.64,55,15}},Subprofession.WILDLIFE_TRACKER,Subprofession.MOBILE_ESCORT);
  add(x,"Rhoy",Gender.HOMBRE,CharacterClass.LUCHADOR,rhoy(),new double[][]{{1.21,23.5,12.6},{1.74,63,16.1},{1.81,77,17}},Subprofession.STONE_SETTER,Subprofession.KINGDOM_AGENT);
  add(x,"Sofía",Gender.MUJER,CharacterClass.APODERADO,sofia(),new double[][]{{1.15,20,11.6},{1.65,57,15.3},{1.67,61,16}},Subprofession.WORK_TAILOR,Subprofession.SALON_DRESSMAKER,Subprofession.SALON_HAIRDRESSER);
  add(x,"Elena",Gender.MUJER,CharacterClass.APODERADO,elena(),new double[][]{{1.16,20.5,11.9},{1.59,49,13.8},{1.61,51.5,14}},Subprofession.LIVESTOCK_KEEPER,Subprofession.FORENSIC_INVESTIGATOR);
  return List.copyOf(x);}
 private static void add(List<CanonicalCharacterStageProfile>x,String n,Gender g,CharacterClass c,CanonicalAppearanceProfile a,double[][]v,Subprofession adolescent,Subprofession...adult){
  x.add(p(n,g,c,CanonicalLifeStage.CHILD,b(v[0][0],v[0][1],v[0][2],v[2][2]>=16?"robusta":v[2][2]>=15?"media":"fina"),a,List.of(),"Ropa civil infantil; sin profesión, armas ni abalorio; inventario mínimo de primera necesidad."));
  x.add(p(n,g,c,CanonicalLifeStage.ADOLESCENT,b(v[1][0],v[1][1],v[1][2],v[2][2]>=16?"robusta":v[2][2]>=15?"media":"fina"),a,List.of(adolescent),"Hereda el estándar completo de "+adolescent.label()+" para su clase; abalorio deliberadamente en blanco."));
  x.add(p(n,g,c,CanonicalLifeStage.ADULT,b(v[2][0],v[2][1],v[2][2],v[2][2]>=16?"robusta":v[2][2]>=15?"media":"fina"),a,List.of(adult),"Hereda el estándar completo de "+Arrays.stream(adult).map(Subprofession::label).toList()+"; en multiprofesión se fusionan sólo elementos no redundantes y se respeta capacidad."));
 }
 public static List<CanonicalCharacterStageProfile> forName(String name){return all().stream().filter(p->p.name().equalsIgnoreCase(name)).toList();}
}
