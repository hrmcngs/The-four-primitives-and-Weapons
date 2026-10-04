import the_four_primitives_and_weapons.skill.WeaponGrowthRules;
public final class WeaponGrowthRulesTest {
    static void check(boolean b,String message){if(!b)throw new AssertionError(message);}
    public static void main(String[] args){
        check(WeaponGrowthRules.level(-1)==1,"Negative XP must not underflow");
        check(WeaponGrowthRules.xpForLevel(Integer.MIN_VALUE)==0,"Negative level must not overflow");
        check(WeaponGrowthRules.powerLevelRequired(Integer.MAX_VALUE)==40,"Required level must stay bounded");
        check(WeaponGrowthRules.level(Integer.MAX_VALUE)==40,"XP cap");
        int previous=0;
        for(int level=2;level<=40;level++) {
            int xp=WeaponGrowthRules.xpForLevel(level);
            check(xp>previous,"Progression must increase");
            check(WeaponGrowthRules.level(xp-1)==level-1,"Premature level at "+level);
            check(WeaponGrowthRules.level(xp)==level,"Exact threshold at "+level);
            check(WeaponGrowthRules.points(xp)==level/2,"Points at "+level);
            previous=xp;
        }
        check(WeaponGrowthRules.spent(1)==1 && WeaponGrowthRules.spent(2)==3 && WeaponGrowthRules.spent(3)==6,"Rank costs must accumulate");
        check(3*WeaponGrowthRules.spent(3)<=WeaponGrowthRules.points(WeaponGrowthRules.xpForLevel(40)),"All three slots must be maxable");
        check(WeaponGrowthRules.unlock(0)==1 && WeaponGrowthRules.unlock(1)==10 && WeaponGrowthRules.unlock(2)==20,"Slot unlocks");
        check(WeaponGrowthRules.powerLevelRequired(19)==40,"Final power must require final level");
        check(WeaponGrowthRules.emeraldCost(0)==4 && WeaponGrowthRules.emeraldCost(19)==42,"Emerald progression");
        check(WeaponGrowthRules.shardCost(0)==1 && WeaponGrowthRules.shardCost(19)==4,"Shard progression");
        check(WeaponGrowthRules.killXp(Float.NaN)==0 && WeaponGrowthRules.killXp(Float.POSITIVE_INFINITY)==0,"Invalid health");
        check(WeaponGrowthRules.killXp(10000)==68,"Boss XP must be bounded");
        check(Math.abs(WeaponGrowthRules.damageScale(20,3)-2.07)<1e-8,"Final damage budget");
        check(WeaponGrowthRules.damageScale(Integer.MAX_VALUE,Integer.MAX_VALUE)==WeaponGrowthRules.damageScale(20,3),"Corrupt power/rank cannot exceed cap");
        System.out.println("Weapon growth XP boundaries, point economy, boss rewards and power caps passed");
    }
}
