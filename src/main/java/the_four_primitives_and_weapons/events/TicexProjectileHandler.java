package the_four_primitives_and_weapons.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.item.TicexWeaponRecipe;

@Mod.EventBusSubscriber(modid=TheFourPrimitivesAndWeaponsMod.MODID)
public final class TicexProjectileHandler {
    @SubscribeEvent public static void spawned(EntityJoinLevelEvent event) {
        if(event.getLevel().isClientSide || !(event.getEntity() instanceof AbstractArrow arrow)
                ||arrow.getPersistentData().getBoolean("TFPWMaterialApplied"))return;
        ItemStack source=ItemStack.EMPTY;
        if(arrow instanceof ThrownTrident) {
            source=ItemStack.of(arrow.saveWithoutId(new CompoundTag()).getCompound("Trident"));
        } else if(arrow.getOwner() instanceof LivingEntity owner) {
            for(ItemStack stack:new ItemStack[]{owner.getUseItem(),owner.getMainHandItem(),owner.getOffhandItem()}) {
                if(arrow.shotFromCrossbow()?stack.getItem() instanceof CrossbowItem:stack.getItem() instanceof BowItem) {source=stack;break;}
            }
        }
        if(!source.hasTag()||!source.getTag().contains(TicexWeaponRecipe.MATERIAL_TAG,10))return;
        float bonus=source.getTag().getCompound(TicexWeaponRecipe.MATERIAL_TAG).getFloat("Attack")-2;
        if(!Float.isFinite(bonus))return;
        arrow.getPersistentData().putBoolean("TFPWMaterialApplied",true);
        if(arrow instanceof ThrownTrident)arrow.getPersistentData().putFloat("TFPWMaterialBonus",bonus);
        else arrow.setBaseDamage(Math.max(.1,arrow.getBaseDamage()+bonus));
    }
    @SubscribeEvent public static void tridentHit(LivingHurtEvent event) {
        if(event.getSource().getDirectEntity() instanceof ThrownTrident trident
                &&trident.getPersistentData().getBoolean("TFPWMaterialApplied"))
            event.setAmount(Math.max(.1f,event.getAmount()+trident.getPersistentData().getFloat("TFPWMaterialBonus")));
    }
}
