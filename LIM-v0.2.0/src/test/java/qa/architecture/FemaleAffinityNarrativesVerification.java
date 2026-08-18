package qa.architecture;

import domain.character.Gender;
import presentation.menu.CharismaNarrative;
import presentation.menu.DexterityNarrative;
import presentation.menu.FaithNarrative;

import java.nio.file.Files;
import java.nio.file.Path;

/** Contratos narrativos : DESTREZA, FE y CARISMA. */
public final class FemaleAffinityNarrativesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        dexteritySoftcaps(); faithSoftcaps(); charismaSexualSplit(); approvedVoices(); noDirectMasteryNames(); sheetUsesGenderAwareNarratives();
    }
    private static void dexteritySoftcaps(){
        same(d(1),d(19)); different(d(19),d(20)); same(d(20),d(69)); different(d(69),d(70)); same(d(70),d(74)); different(d(74),d(75));
        failure(() -> DexterityNarrative.descriptionFor(71, Gender.HOMBRE));
    }
    private static String d(int v){return DexterityNarrative.descriptionFor(v,Gender.MUJER);}
    private static void faithSoftcaps(){
        same(f(1),f(2)); different(f(2),f(3)); same(f(3),f(12)); different(f(12),f(13)); same(f(13),f(31)); different(f(31),f(32)); same(f(32),f(39)); different(f(39),f(40)); same(f(40),f(59)); different(f(59),f(60)); same(f(60),f(74)); different(f(74),f(75));
        failure(() -> FaithNarrative.descriptionFor(61,Gender.HOMBRE));
    }
    private static String f(int v){return FaithNarrative.descriptionFor(v,Gender.MUJER);}
    private static void charismaSexualSplit(){
        same(cw(1),cw(17)); different(cw(17),cw(18)); same(cw(18),cw(20)); different(cw(20),cw(21)); same(cw(21),cw(39)); different(cw(39),cw(40)); same(cw(40),cw(74)); different(cw(74),cw(75));
        same(cm(1),cm(24)); different(cm(24),cm(25)); same(cm(25),cm(49)); different(cm(49),cm(50)); failure(() -> CharismaNarrative.descriptionFor(51,Gender.HOMBRE));
    }
    private static String cw(int v){return CharismaNarrative.descriptionFor(v,Gender.MUJER);} private static String cm(int v){return CharismaNarrative.descriptionFor(v,Gender.HOMBRE);}
    private static void approvedVoices(){
        org.junit.jupiter.api.Assertions.assertTrue(d(20).contains("accidente estadístico"),"DESTREZA debe conservar Accidente estadístico.");
        org.junit.jupiter.api.Assertions.assertTrue(d(70).contains("Repetid la prueba"),"DESTREZA 70 debe ser Error de medición.");
        org.junit.jupiter.api.Assertions.assertTrue(f(1).contains("Qué descanso"),"FE inicial debe mostrar satisfacción de la falsa conciencia.");
        org.junit.jupiter.api.Assertions.assertTrue(f(13).contains("¿Tienes más PV?") && f(13).contains("¿Más PA?"),"FE 13 debe interrogar agresivamente la utilidad aparente.");
        org.junit.jupiter.api.Assertions.assertTrue(f(40).trim().equals("..."),"FE 40-59 debe contener sólo puntos suspensivos.");
        org.junit.jupiter.api.Assertions.assertTrue(f(60).contains("El tesoro está justo ahí"),"FE 60 debe adoptar la provocación final.");
        org.junit.jupiter.api.Assertions.assertTrue(f(75).contains("Tu conciencia, claro"),"FE 75 debe revelar la identidad declarada de la voz.");
        org.junit.jupiter.api.Assertions.assertTrue(cw(18).toLowerCase().contains("capital erótico"),"CARISMA femenino 18 debe introducir capital erótico.");
        org.junit.jupiter.api.Assertions.assertTrue(cw(21).toLowerCase().contains("capital erótico") && cw(21).toLowerCase().contains("ahorro social"),"CARISMA femenino 21 debe acumular ambos conceptos.");
        org.junit.jupiter.api.Assertions.assertTrue(cw(75).contains("Mamasita consumada"),"CARISMA femenino 75 debe culminar como Mamasita consumada.");
        org.junit.jupiter.api.Assertions.assertTrue(cm(1).contains("te sacan los dineros"),"CARISMA masculino inicial debe remarcar el desastre económico.");
        org.junit.jupiter.api.Assertions.assertTrue(cm(25).contains("Los animales") && cm(25).contains("manada"),"CARISMA masculino medio debe incorporar lectura animal/social.");
        org.junit.jupiter.api.Assertions.assertTrue(cm(50).contains("Papuchón consumado"),"CARISMA masculino 50 debe culminar como Papuchón consumado.");
    }
    private static void noDirectMasteryNames(){
        String all=d(75)+f(75)+cw(75)+cm(50);
        String[] forbidden={"PULSIÓN","AURA DE PULSIÓN","INVISIBILIDAD","SANAR","RESTAURAR","CUSTODIA","DRENAR","INCITAR","CAPITALIZAR","RENTABILIZAR","EMPATÍA ANIMAL"};
        for(String name:forbidden) org.junit.jupiter.api.Assertions.assertTrue(!all.contains(name),"Las narrativas no deben nombrar maestrías: "+name);
    }
    private static void sheetUsesGenderAwareNarratives() throws Exception {
        String source=Files.readString(Path.of("src/main/java/presentation/menu/CharacterSheetScreen.java"));
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("DexterityNarrative.descriptionFor(dexterity, savedGame.character().identity().gender())"),"DESTREZA debe resolver por sexo.");
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("FaithNarrative.descriptionFor(faith, savedGame.character().identity().gender())"),"FE debe resolver por sexo.");
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("CharismaNarrative.descriptionFor(charisma, savedGame.character().identity().gender())"),"CARISMA debe resolver por sexo.");
    }
    private static void same(String a,String b){org.junit.jupiter.api.Assertions.assertTrue(a.equals(b),"Se esperaba el mismo tramo narrativo.");} private static void different(String a,String b){org.junit.jupiter.api.Assertions.assertTrue(!a.equals(b),"Se esperaba transición narrativa.");} private static void failure(Runnable r){try{r.run();throw new IllegalStateException("Se esperaba rechazo.");}catch(IllegalArgumentException expected){}} 
}
