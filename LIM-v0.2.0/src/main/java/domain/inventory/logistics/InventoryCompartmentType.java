package domain.inventory.logistics;

import domain.inventory.InventoryFootprint;
import domain.inventory.InventoryGridDefinition;
import java.util.Optional;
import java.util.OptionalDouble;

public enum InventoryCompartmentType {
    /** Obsoleto accesible sólo en estados antiguos;  ya no lo entrega al personaje inicial. */
    BODY("Inventario corporal legado", "Compatibilidad con estados anteriores a ; no constituye capacidad inicial del personaje.",
            null, new InventoryGridDefinition(2, 7), 0.0, OptionalDouble.empty(), InventoryFootprint.equipmentOnly()),

    /** Proveedores que  poblará desde prendas inventariables. */
    LEGGINGS_STORAGE("Inventario de polainas", "Espacio impermeable compuesto por módulos aportados por prendas de LEGGINGS inventariables.",
            null, InventoryGridDefinition.empty(), 0.0, OptionalDouble.empty(), InventoryFootprint.equipmentOnly()),
    CHEST_STORAGE("Inventario de coraza", "Espacio impermeable compuesto por módulos aportados por prendas de CHEST inventariables.",
            null, InventoryGridDefinition.empty(), 0.0, OptionalDouble.empty(), InventoryFootprint.equipmentOnly()),

    LEG_POUCH("Pernera Modular de Camino V881",
            "Contenedor modular ceñido al muslo mediante correas independientes; conserva acceso inmediato sin ocupar las manos y admite una carga completa de cuatro inyecciones estimulantes de servicio.",
            new InventoryPhysicalDimensions(4, 2, 1), null, 0.45, OptionalDouble.empty(), new InventoryFootprint(4, 2)),
    BANDOLIER("Bandolera de Servicio V881",
            "Bandolera cruzada de cuero reforzado con cuerpo profundo y apertura de servicio accesible desde el frente del torso.",
            new InventoryPhysicalDimensions(4, 2, 2), null, 0.80, OptionalDouble.empty(), new InventoryFootprint(3, 2)),
    BACKPACK("Mochila Dorsal de Expedición V881",
            "Mochila dorsal reforzada de geometría 5×3×3; la profundidad se proyecta sobre la rejilla bidimensional y su arnés exterior admite un casco compatible.",
            new InventoryPhysicalDimensions(5, 3, 3), null, 1.50, OptionalDouble.empty(), new InventoryFootprint(5, 3)),
    DORSAL_ROTOR_SYSTEM("Sistema de Transporte Dorsal del Rotor V881",
            "Armazón dorsal exclusivo que habilita BACK_HAND para un Espadón de Rotor completamente retraído. El arma y el sistema se adquieren por separado; el armazón sustituye a la mochila dorsal y no constituye una vaina convencional.",
            new InventoryPhysicalDimensions(9, 2, 1), new InventoryGridDefinition(2, 9), 3.20, OptionalDouble.of(3.80), new InventoryFootprint(9, 2)),
    SADDLEBAGS_HORSE_LEISURE("Alforjas de Monta", "Par de alforjas ecuestres de cuero flexible, unidas por puente de silla y equilibradas a ambos flancos; volumen suficiente para viaje sin convertir una montura de paseo en animal de carga.",
            new InventoryPhysicalDimensions(4, 3, 2), null, 2.60, OptionalDouble.of(15.0), new InventoryFootprint(4, 3)),
    SADDLEBAGS_HORSE_RACING("Alforjas de Carrera", "Par de alforjas ecuestres mínimas, ceñidas detrás de la silla y sin bolsas profundas; reducen balanceo, resistencia y masa para preservar la mecánica de una montura rápida.",
            new InventoryPhysicalDimensions(3, 2, 2), null, 1.40, OptionalDouble.of(6.0), new InventoryFootprint(3, 2)),
    SADDLEBAGS_HORSE_DRAFT("Alforjas de Carga", "Alforjas ecuestres reforzadas de gran formato, con puente ancho y fondos estructurados para el dorso de un caballo pesado; privilegian estabilidad y reparto bilateral sobre acceso rápido.",
            new InventoryPhysicalDimensions(6, 3, 3), null, 4.80, OptionalDouble.of(30.0), new InventoryFootprint(6, 3)),
    SADDLEBAGS_BICYCLE_MILITARY("Bolsas de Portaequipajes Militar V881", "Pareja de bolsas de lona reforzada inspiradas en equipamiento ciclista militar: cuelgan a ambos lados del portaequipajes, con respaldo semirrígido y fijaciones que impiden contacto con radios, rueda y talón.",
            new InventoryPhysicalDimensions(4, 2, 2), null, 2.20, OptionalDouble.of(10.0), new InventoryFootprint(4, 2)),
    SADDLEBAGS_MOTORCYCLE_CARDAN("Maletas Laterales Cardán V881", "Pareja de maletas rígidas de servicio, inspiradas en el equipaje de motocicletas militares pesadas: anclaje al subchasis, tapas superiores y reparto simétrico para mantener la masa baja y próxima al eje longitudinal.",
            new InventoryPhysicalDimensions(6, 3, 2), null, 6.80, OptionalDouble.of(20.0), new InventoryFootprint(6, 3)),
    ARROW_QUIVER("Carcaj para flechas", "Carcaj rígido ligero para hasta 12 flechas; su contenido se agrega por variante y no consume una rejilla interna por flecha.",
            new InventoryPhysicalDimensions(8, 1, 1), new InventoryGridDefinition(8, 12), 0.650, OptionalDouble.empty(), new InventoryFootprint(8, 1));

    private final String label;
    private final String narrativeDescription;
    private final InventoryPhysicalDimensions physicalDimensions;
    private final InventoryGridDefinition grid;
    private final double structuralWeightKg;
    private final OptionalDouble maximumWeightKg;
    private final InventoryFootprint storedFootprint;

    InventoryCompartmentType(String label, String narrativeDescription,
                             InventoryPhysicalDimensions physicalDimensions, InventoryGridDefinition explicitGrid,
                             double structuralWeightKg, OptionalDouble maximumWeightKg, InventoryFootprint storedFootprint) {
        if (structuralWeightKg < 0) throw new IllegalArgumentException("El peso estructural no puede ser negativo.");
        this.label = label;
        this.narrativeDescription = narrativeDescription;
        this.physicalDimensions = physicalDimensions;
        this.grid = explicitGrid != null ? explicitGrid : InventoryVolumeProjectionPolicy.project(physicalDimensions);
        this.structuralWeightKg = structuralWeightKg;
        this.maximumWeightKg = maximumWeightKg;
        this.storedFootprint = storedFootprint;
    }

    public String label() { return label; }
    public String narrativeDescription() { return narrativeDescription; }
    public InventoryGridDefinition grid() { return grid; }
    public Optional<InventoryPhysicalDimensions> physicalDimensions() { return Optional.ofNullable(physicalDimensions); }
    public double structuralWeightKg() { return structuralWeightKg; }
    public OptionalDouble maximumWeightKg() { return maximumWeightKg; }
    public InventoryFootprint storedFootprint() {
        return physicalDimensions == null ? storedFootprint : InventoryVolumeProjectionPolicy.footprint(physicalDimensions);
    }
    public boolean supportsExternalHelmetCarrier() { return this == BACKPACK; }
}
