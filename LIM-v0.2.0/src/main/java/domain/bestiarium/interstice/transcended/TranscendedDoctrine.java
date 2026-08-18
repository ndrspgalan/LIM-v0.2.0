package domain.bestiarium.interstice.transcended;

import domain.bestiarium.BestiaryTaxon;
import domain.bestiarium.ExistencePlane;

/** Ontología canónica de TRANSCENDED, independiente del futuro diseño de niveles. */
public final class TranscendedDoctrine {
    private TranscendedDoctrine(){}
    public static BestiaryTaxon taxon(){return BestiaryTaxon.TRANSCENDED;}
    public static ExistencePlane plane(){return ExistencePlane.INTERSTICE;}
    public static boolean hasPhysicalForm(){return false;}
    public static String canonicalNarrative(){
        return "Los TRANSCENDED no son organismos ni combatientes convencionales. Son fuerzas incorpóreas del Intersticio cuya manifestación estable sobre el plano físico puede ser confundida por sus habitantes con una ley universal. No imponen la voluntad de Kenan ni deciden por él: inclinan el abanico de oportunidades que la causalidad pone a su alcance. Cada ley parte de neutralidad 0.5; por debajo o por encima existe una tendencia hacia uno de sus polos, mientras que exactamente 0.5 significa ausencia de influencia de esa ley. No trabajan con probabilidades: para un mismo estado causal, la oportunidad materializada es determinista y reproducible.";
    }
}
