package domain.inventory.logistics;
public record MotorcycleFuelState(double normalLiters,double reserveLiters,MotorcycleFuelTap tap,MotorcycleFuelType fuelType){
 public static final double NORMAL_CAPACITY_L=10.5,RESERVE_CAPACITY_L=3.5,TOTAL_RANGE_KM=255.0,CONSUMPTION_L_PER_100_KM=14.0/255.0*100.0;
 public MotorcycleFuelState{if(normalLiters<0||normalLiters>NORMAL_CAPACITY_L||reserveLiters<0||reserveLiters>RESERVE_CAPACITY_L)throw new IllegalArgumentException("Nivel de combustible inválido.");}
 public static MotorcycleFuelState full(MotorcycleFuelType type){return new MotorcycleFuelState(NORMAL_CAPACITY_L,RESERVE_CAPACITY_L,MotorcycleFuelTap.ON,type);}
 public double availableLiters(){return tap==MotorcycleFuelTap.ON?normalLiters:reserveLiters;}
 public MotorcycleFuelState toggleTap(){return new MotorcycleFuelState(normalLiters,reserveLiters,tap==MotorcycleFuelTap.ON?MotorcycleFuelTap.RESERVE:MotorcycleFuelTap.ON,fuelType);}
 public MotorcycleFuelState consumeDistance(double km){if(km<0)throw new IllegalArgumentException();double l=km*CONSUMPTION_L_PER_100_KM/100.0;if(tap==MotorcycleFuelTap.ON)return new MotorcycleFuelState(Math.max(0,normalLiters-l),reserveLiters,tap,fuelType);return new MotorcycleFuelState(normalLiters,Math.max(0,reserveLiters-l),tap,fuelType);}
}
