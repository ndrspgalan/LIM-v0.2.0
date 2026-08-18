package domain.combat;

public final class ShieldPolicy {

    private ShieldPolicy(){}

    public static boolean protectsCompletely(int bluntProtection){
        return bluntProtection > 0;
    }

    public static boolean isBroken(int bluntProtection){
        return bluntProtection <= 0;
    }
}
