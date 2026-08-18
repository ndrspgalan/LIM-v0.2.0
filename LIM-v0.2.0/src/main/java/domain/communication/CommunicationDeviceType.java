package domain.communication;

public enum CommunicationDeviceType {
    AERONAUT_INTERCOM("Casco Replegable del Aeronauta"),
    PANOPTICON("Panóptico del Ilustrado");

    private final String label;
    CommunicationDeviceType(String label){ this.label=label; }
    public String label(){ return label; }
}
