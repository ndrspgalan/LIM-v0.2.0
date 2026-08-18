package domain.inventory.logistics;

public enum PersonalTransportType {
    HORSE_LEISURE(PersonalTransportFamily.HORSE,"Caballo de Paseo",480,5,14,38,true,true,false,false,"Relincho",
            "Caballo de monta equilibrado, de dorso medio y temperamento estable. Su conformación prioriza jornadas largas, control predecible y reparto simétrico de carga; admite copiloto y alforjas moderadas sin especializarse ni en velocidad ni en tiro."),
    HORSE_RACING(PersonalTransportFamily.HORSE,"Caballo de Carreras",430,6,18,60,true,true,false,false,"Relincho",
            "Montura ligera, longilínea y de respuesta rápida, seleccionada para aceleración y velocidad. El menor margen dorsal obliga a reducir equipaje y a mantenerlo alto, compacto y perfectamente equilibrado para no penalizar la zancada."),
    HORSE_DRAFT(PersonalTransportFamily.HORSE,"Caballo de Tiro",700,4,11,28,true,true,false,false,"Relincho",
            "Caballo pesado de gran sección ósea, dorso ancho y elevada fuerza sostenida. Sacrifica velocidad y agilidad a cambio de estabilidad y margen de carga, por lo que acepta las alforjas ecuestres de mayor volumen sin convertir la monta en transporte de arrastre."),
    BICYCLE_FOLDING_V881(PersonalTransportFamily.BICYCLE,"Bicicleta Plegable V881",15,5,17,30,false,false,true,true,"Ring ring",
            "Bicicleta militar plegable inspirada en las Bianchi de los Bersaglieri: bastidor articulado, ruedas compactas y suspensión destinada a tolerar firme irregular. Puede plegarse y cargarse a la espalda; esa prioridad de portabilidad excluye un sistema lateral de equipaje permanente."),
    BICYCLE_MILITARY_V881(PersonalTransportFamily.BICYCLE,"Bicicleta Militar V881",22,5,16,28,true,true,true,false,"Ring ring",
            "Bicicleta de servicio inspirada en la Truppenfahrrad : cuadro de acero robusto, geometría estable, portaequipajes posterior y puntos de fijación utilitarios. Está pensada para enlace y marcha sostenida con equipo, no para velocidad deportiva; admite bolsas específicas sin invadir el pedaleo."),
    MOTORCYCLE_CARDAN_V881(PersonalTransportFamily.MOTORCYCLE,"Motocicleta Cardán V881",198,0,0,110,true,true,false,false,"Rugido de motor",
            "Motocicleta militar de transmisión por cardán inspirada en la Zündapp KS 750: arquitectura de gran par, baja relación para terreno difícil y bastidor concebido para servicio pesado. Sus anclajes laterales reciben maletas rígidas independientes y mantienen la carga baja y próxima al centro longitudinal." );

    private final PersonalTransportFamily family; private final String label; private final double massKg;
    private final double walkKmh,trotKmh,maxKmh; private final boolean saddlebags,copilot,maintenance,foldable; private final String response; private final String technicalDescription;
    PersonalTransportType(PersonalTransportFamily family,String label,double massKg,double walkKmh,double trotKmh,double maxKmh,boolean saddlebags,boolean copilot,boolean maintenance,boolean foldable,String response,String technicalDescription){
        this.family=family;this.label=label;this.massKg=massKg;this.walkKmh=walkKmh;this.trotKmh=trotKmh;this.maxKmh=maxKmh;this.saddlebags=saddlebags;this.copilot=copilot;this.maintenance=maintenance;this.foldable=foldable;this.response=response;this.technicalDescription=technicalDescription;
    }
    public PersonalTransportFamily family(){return family;} public String label(){return label;} public double massKg(){return massKg;}
    public double walkKmh(){return walkKmh;} public double trotKmh(){return trotKmh;} public double maximumKmh(){return maxKmh;}
    public boolean supportsSaddlebags(){return saddlebags;} public boolean supportsCopilot(){return copilot;} public boolean requiresMaintenance(){return maintenance;}
    public boolean foldable(){return foldable;} public String responseSignal(){return response;} public String technicalDescription(){return technicalDescription;}
}
