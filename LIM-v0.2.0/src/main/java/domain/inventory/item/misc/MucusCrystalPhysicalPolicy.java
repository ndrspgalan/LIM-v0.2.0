package domain.inventory.item.misc;

import domain.character.progression.MucusType;
import domain.inventory.logistics.InventoryPhysicalDimensions;
import domain.runic.transposition.TranspositionYieldPolicy;

import java.util.Objects;

/**
 * : el volumen cristalino es el volumen de mucus convertido por unidad.
 * La forma regular determina la envolvente XYZ; las referencias visuales dejan de ser autoridad.
 */
public final class MucusCrystalPhysicalPolicy {
    private static final double KG_PER_ML = 0.001; // conservación macroscópica 1 mL -> ~1 g transpuesto
    private MucusCrystalPhysicalPolicy(){}

    public static MucusType mucusType(MucusCrystalGeometry g){
        return switch(Objects.requireNonNull(g)){
            case TETRAEDRO -> MucusType.AMARILLENTO;
            case OCTAEDRO -> MucusType.VERDOSO;
            case CUBO -> MucusType.MARRON;
            case ESFERA -> MucusType.ENSANGRENTADO;
            case DODECAEDRO -> MucusType.NEGRUZCO;
        };
    }

    public static double convertedVolumeMl(MucusCrystalGeometry g){
        return TranspositionYieldPolicy.precursorMlPerCrystal(mucusType(g));
    }

    public static double massKg(MucusCrystalGeometry g){
        return convertedVolumeMl(g)*KG_PER_ML;
    }

    public static double boundingDimensionMeters(MucusCrystalGeometry g){
        double vM3=convertedVolumeMl(g)*1e-6;
        return switch(g){
            // Tetraedro regular orientado en cuatro vértices alternos de su caja cúbica: V=s^3/3.
            case TETRAEDRO -> Math.cbrt(3.0*vM3);
            // Octaedro regular alineado con XYZ: V=s^3/6 para su caja cúbica.
            case OCTAEDRO -> Math.cbrt(6.0*vM3);
            case CUBO -> Math.cbrt(vM3);
            case ESFERA -> Math.cbrt(6.0*vM3/Math.PI);
            case DODECAEDRO -> {
                double a=Math.cbrt(vM3 / ((15.0+7.0*Math.sqrt(5.0))/4.0));
                double diameter=(a/2.0)*Math.sqrt(3.0)*(1.0+Math.sqrt(5.0));
                yield diameter;
            }
        };
    }

    public static InventoryPhysicalDimensions dimensions(MucusCrystalGeometry g){
        double d=boundingDimensionMeters(g);
        return InventoryPhysicalDimensions.fromMetricDimensions(d,d,d);
    }
}
