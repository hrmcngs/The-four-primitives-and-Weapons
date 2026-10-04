package the_four_primitives_and_weapons.skill;

/** Progression arithmetic, shared by the server and the upgrade screen. */
public final class WeaponGrowthRules {
    public static final int MAX_LEVEL=40, MAX_POWER=20, MAX_RANK=3;
    public enum Perk { POWER, PRECISION, LEECH, HASTE, BREAKER, GUARD }
    private WeaponGrowthRules() {}
    public static int xpForLevel(int level) {
        int n=Math.max(1,Math.min(MAX_LEVEL,level))-1;
        return 20*n+5*n*(n+1);
    }
    public static int level(int xp) {
        int level=1;while(level<MAX_LEVEL && xp>=xpForLevel(level+1))level++;return level;
    }
    public static int points(int xp){return level(xp)/2;}
    public static int unlock(int slot){return switch(slot){case 0->1;case 1->10;case 2->20;default->Integer.MAX_VALUE;};}
    public static int spent(int rank){rank=Math.max(0,Math.min(MAX_RANK,rank));return rank*(rank+1)/2;}
    public static int emeraldCost(int power){return 4+2*Math.max(0,Math.min(MAX_POWER,power));}
    public static int shardCost(int power){return 1+Math.max(0,Math.min(MAX_POWER,power))/5;}
    public static int powerLevelRequired(int power){return 2*(Math.max(0,Math.min(MAX_POWER-1,power))+1);}
    public static double damageScale(int power,int rank){return (1+.04*Math.max(0,Math.min(MAX_POWER,power)))*(1+.05*Math.max(0,Math.min(MAX_RANK,rank)));}
    public static int killXp(float maxHealth) {
        if(!Float.isFinite(maxHealth)||maxHealth<=0)return 0;
        return 8+(int)Math.min(40,maxHealth/5)+(maxHealth>=100?20:0);
    }
}
