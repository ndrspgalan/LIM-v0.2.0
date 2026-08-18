package domain.bestiarium.physical_plane.ferae.intelligence;

import domain.bestiarium.physical_plane.ferae.*;
import domain.combat.natural.NaturalCombatProfile;
import domain.inventory.item.WeaponCombatAction;
import java.util.*;

/**
 *  — anatomía ofensiva de INTELIGENCIA. No contiene decisiones tácticas:
 * sólo masa efectiva y representación física de LIGHT/HEAVY/JUMP.
 */
public final class IntelligenceFeraeCombatProfiles {
    private IntelligenceFeraeCombatProfiles(){}
    private record Definition(double maleMass,double femaleMass,String light,String heavy,String jump){}
    private static final Map<FeraeSpecies,Definition> DEFINITIONS=build();

    public static NaturalCombatProfile of(FeraeProfile fera){
        Objects.requireNonNull(fera,"La Ferae no puede ser nula.");
        if(fera.species().branch()!=FeraeBranch.INTELIGENCIA)
            throw new IllegalArgumentException("Sólo INTELIGENCIA tiene perfil de combate .");
        Definition d=DEFINITIONS.get(fera.species());
        if(d==null) throw new IllegalStateException("Falta anatomía ofensiva para "+fera.species().label());
        double mass=fera.sex()==FeraeSex.MACHO?d.maleMass():d.femaleMass();
        return new NaturalCombatProfile(
                fera.species().label()+" "+fera.sex().label(),mass,
                Map.of(
                        WeaponCombatAction.LIGHT_ATTACK,d.light(),
                        WeaponCombatAction.HEAVY_ATTACK,d.heavy(),
                        WeaponCombatAction.JUMP_ATTACK,d.jump()),
                false,false);
    }

    public static int definitionCount(){ return DEFINITIONS.size(); }

    private static Map<FeraeSpecies,Definition> build(){
        EnumMap<FeraeSpecies,Definition> m=new EnumMap<>(FeraeSpecies.class);
        put(m,FeraeSpecies.RATA,.15,.12,"Mordisco rápido","Mordisco profundo","Salto corto con mordisco");
        put(m,FeraeSpecies.CUERVO,.30,.30,"Picotazo","Picotazo violento con impulso corporal","Picado con picotazo");
        put(m,FeraeSpecies.CERDO,4.0,3.5,"Mordisco","Embestida corta de cabeza y hombro","Salto corporal con golpe frontal");
        put(m,FeraeSpecies.ARMADILLO,1.5,1.3,"Mordisco corto","Acometida con impacto corporal","Salto corto con impacto corporal");
        put(m,FeraeSpecies.CABALLO_PASEO,12,12,"Coz rápida","Coz a plena extensión","Patada durante el salto");
        put(m,FeraeSpecies.CABALLO_CARRERAS,10,10,"Coz rápida","Coz a plena extensión","Patada durante el salto");
        put(m,FeraeSpecies.CABALLO_TIRO,16,16,"Coz pesada","Coz a plena extensión","Patada durante el salto");
        put(m,FeraeSpecies.CIERVO,6,4.5,"Golpe corto con cornamenta","Embestida con cornamenta","Embestida aérea con cornamenta");
        put(m,FeraeSpecies.TORO,18,12,"Cornada corta","Embestida pesada","Cornada en salto");
        put(m,FeraeSpecies.AGUILA,1.0,1.2,"Picotazo o garra rápida","Garrazos potentes","Picado con garras");
        put(m,FeraeSpecies.SERPIENTE,.5,.6,"Mordedura rápida","Mordedura profunda","Proyección corporal con mordedura");
        put(m,FeraeSpecies.JABALI,7,5.5,"Colmillazo corto","Embestida con colmillos","Salto frontal con colmillazo");
        put(m,FeraeSpecies.LINCE,2.5,2.2,"Zarpazo","Mordisco potente","Salto depredador con mordisco");
        put(m,FeraeSpecies.LOBO,3.5,3.0,"Mordisco","Mordisco comprometido","Mordisco en salto");
        put(m,FeraeSpecies.LEON,7.5,6.0,"Zarpazo","Mordisco potente","Abalanzamiento con mordisco");
        put(m,FeraeSpecies.OSO,14,10,"Zarpazo","Golpe pesado de miembro anterior","Abalanzamiento con zarpazo o mordisco");
        put(m,FeraeSpecies.RINOCERONTE,25,20,"Golpe corto con cuerno y cabeza","Carga con cuerno","Embestida elevada con impacto de cuerno");
        return Collections.unmodifiableMap(m);
    }
    private static void put(Map<FeraeSpecies,Definition> m,FeraeSpecies s,double mm,double fm,String l,String h,String j){m.put(s,new Definition(mm,fm,l,h,j));}
}
