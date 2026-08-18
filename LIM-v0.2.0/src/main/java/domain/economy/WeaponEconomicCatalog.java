package domain.economy;

import java.util.*;

/**  — arma cuerpo a cuerpo tasada anticipadamente durante la depuración del bloque armamentístico. */
public final class WeaponEconomicCatalog {
    public static final String PAVESINA = "Pavesina Cementada de Asalto V881";
    private static final Map<String,EconomicValuation> DATA=Map.of(
            PAVESINA, EconomicValuation.priced(PAVESINA,EconomicGoodType.PRIVATE_USE,4800,
                    "La Pavesina parte de 8,8 kg de acero al níquel-cromo conformado en una placa convexa continua y sometido a cementación/endurecimiento superficial sin perder tenacidad de núcleo. A ello se añaden cuero, fieltro prensado, abrazadera, suspensión y trabajo de ajuste para gobernarla a una mano. El coste corresponde a aleación, masa metálica, tratamiento térmico, conformado y ergonomía especializada; su protección del 100 % no se utiliza como multiplicador de precio.")
    );
    private WeaponEconomicCatalog(){}
    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Arma  sin tasación: "+name);
        return v;
    }
    public static Map<String,EconomicValuation> all(){return DATA;}
}
