package application.mdpar.serialization.v1;

/** Convenciones de la frontera global. Las unidades de LIM pertenecen a la representación, no al boundary MDPAR. */
public final class WireConventionsV1 {
    private WireConventionsV1() {}
    public static final String BOUNDARY = "mdpar-boundary/v1";
    public static final String ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "application/json";
}
