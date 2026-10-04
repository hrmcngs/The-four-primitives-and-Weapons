package the_four_primitives_and_weapons.skill;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import the_four_primitives_and_weapons.item.TicexWeaponCatalog;
import the_four_primitives_and_weapons.skill.WeaponGrowthRules.Perk;

public final class WeaponGrowth {
    public static final String KEY="TFPWWeaponGrowth";
    private WeaponGrowth() {}
    public static boolean supported(ItemStack stack){return !stack.isEmpty()&&stack.getCount()==1&&TicexWeaponCatalog.supported(stack.getItem());}
    public static boolean canChoose(ItemStack stack,Perk perk){
        if(!supported(stack))return false;
        if(perk==Perk.GUARD)return stack.getItem() instanceof net.minecraft.world.item.SwordItem || stack.getItem() instanceof net.minecraft.world.item.ShieldItem;
        if(perk==Perk.BREAKER)return stack.getItem() instanceof net.minecraft.world.item.SwordItem || stack.getItem() instanceof net.minecraft.world.item.ShieldItem || stack.getItem() instanceof net.minecraft.world.item.TridentItem;
        if(perk==Perk.HASTE)return !(stack.getItem() instanceof net.minecraft.world.item.ShieldItem);
        return true;
    }
    public static CompoundTag data(ItemStack stack){return stack.hasTag()?stack.getTag().getCompound(KEY):new CompoundTag();}
    public static void write(ItemStack stack,CompoundTag tag){stack.getOrCreateTag().put(KEY,tag);}
    public static UUID id(ItemStack stack) {
        var tag=data(stack);return tag.hasUUID("Id")?tag.getUUID("Id"):null;
    }
    public static UUID ensureId(ItemStack stack){
        var tag=data(stack);if(!tag.hasUUID("Id")){tag.putUUID("Id",UUID.randomUUID());write(stack,tag);}return tag.getUUID("Id");
    }
    public static int xp(ItemStack stack){return Math.max(0,Math.min(WeaponGrowthRules.xpForLevel(40),data(stack).getInt("Xp")));}
    public static int level(ItemStack stack){return WeaponGrowthRules.level(xp(stack));}
    public static int power(ItemStack stack){return Math.max(0,Math.min(20,data(stack).getInt("Power")));}
    public static int slotRank(ItemStack stack,int slot){return slot>=0&&slot<3?Math.max(0,Math.min(3,data(stack).getInt("Rank"+slot))):0;}
    public static Perk slotPerk(ItemStack stack,int slot){
        if(slot<0||slot>2||slotRank(stack,slot)==0)return null;
        try{return Perk.valueOf(data(stack).getString("Perk"+slot));}catch(IllegalArgumentException e){return null;}
    }
    public static int rank(ItemStack stack,Perk perk){for(int i=0;i<3;i++)if(slotPerk(stack,i)==perk)return slotRank(stack,i);return 0;}
    public static int freePoints(ItemStack stack){int spent=0;for(int i=0;i<3;i++)spent+=WeaponGrowthRules.spent(slotRank(stack,i));return Math.max(0,WeaponGrowthRules.points(xp(stack))-spent);}
    public static void addXp(ItemStack stack,int amount){if(!supported(stack)||amount<=0)return;ensureId(stack);var tag=data(stack);tag.putInt("Xp",(int)Math.min(WeaponGrowthRules.xpForLevel(40),(long)xp(stack)+amount));write(stack,tag);}
}
