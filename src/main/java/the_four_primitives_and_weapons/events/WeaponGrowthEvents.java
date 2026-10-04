package the_four_primitives_and_weapons.events;

import java.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.entity.ThrowingKnifeEntity;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.mixin.ThrownTridentGrowthAccessor;
import the_four_primitives_and_weapons.skill.*;
import the_four_primitives_and_weapons.skill.WeaponGrowthRules.Perk;

@Mod.EventBusSubscriber(modid=TheFourPrimitivesAndWeaponsMod.MODID)
public final class WeaponGrowthEvents {
    private static final String PROJECTILE="TFPWGrowthWeapon";
    private static final UUID HASTE=UUID.fromString("b6e8fc45-5466-4679-b0d5-44761b2e204f");
    private static final Map<LivingEntity,Hit> HITS=new WeakHashMap<>();
    private static final Map<Player,Long> HEALED=new WeakHashMap<>();
    private record Hit(UUID owner,ItemStack weapon,Entity projectile,long tick) {}
    @SubscribeEvent public static void prepare(PlayerInteractEvent.RightClickItem event) {
        if(!event.getLevel().isClientSide && WeaponGrowth.supported(event.getItemStack()))WeaponGrowth.ensureId(event.getItemStack());
    }
    @SubscribeEvent public static void projectile(EntityJoinLevelEvent event) {
        if(event.getLevel().isClientSide||!(event.getEntity() instanceof Projectile projectile)||projectile.getPersistentData().contains(PROJECTILE))return;
        ItemStack weapon=ItemStack.EMPTY;
        if(projectile instanceof ThrownTrident trident)weapon=((ThrownTridentGrowthAccessor)trident).tfpw$growthWeapon();
        else if(projectile instanceof ThrowingKnifeEntity knife)weapon=knife.getItem();
        else if(projectile instanceof AbstractArrow arrow&&arrow.getOwner() instanceof Player player) {
            for(var stack:new ItemStack[]{player.getUseItem(),player.getMainHandItem(),player.getOffhandItem()}) {
                if(arrow.shotFromCrossbow()?stack.getItem() instanceof CrossbowItem:stack.getItem() instanceof BowItem){weapon=stack;break;}
            }
        }
        if(!WeaponGrowth.supported(weapon))return;
        WeaponGrowth.ensureId(weapon);projectile.getPersistentData().put(PROJECTILE,weapon.save(new CompoundTag()));
    }
    public static ItemStack usedWeapon(DamageSource source) {
        if(!(source.getEntity() instanceof Player player))return ItemStack.EMPTY;
        if(source.getDirectEntity() instanceof Projectile projectile) {
            if(projectile.getPersistentData().contains(PROJECTILE,10))return ItemStack.of(projectile.getPersistentData().getCompound(PROJECTILE));
            return ItemStack.EMPTY;
        }
        return source.getDirectEntity()==player?player.getMainHandItem():ItemStack.EMPTY;
    }
    @SubscribeEvent(priority=EventPriority.LOW) public static void damage(LivingHurtEvent event) {
        if(event.getEntity().level().isClientSide||event.getAmount()<=0)return;
        if(event.getEntity() instanceof Player defender) {
            var weapon=defender.isUsingItem()?defender.getUseItem():defender.getMainHandItem();
            int rank=WeaponGrowth.rank(weapon,Perk.GUARD);
            if(defender.isBlocking()||defender.getPersistentData().getInt("SwordGuardTicks")>0)
                event.setAmount(event.getAmount()*(1-.06f*rank));
        }
        var weapon=usedWeapon(event.getSource());if(!WeaponGrowth.supported(weapon))return;
        float damage=(float)(event.getAmount()*WeaponGrowthRules.damageScale(WeaponGrowth.power(weapon),WeaponGrowth.rank(weapon,Perk.POWER)));
        if(event.getEntity().getRandom().nextFloat()<.08f*WeaponGrowth.rank(weapon,Perk.PRECISION))damage*=1.5f;
        event.setAmount(damage);
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void landed(LivingDamageEvent event) {
        if(event.getEntity().level().isClientSide||event.getAmount()<=0||!(event.getSource().getEntity() instanceof Player player))return;
        var weapon=usedWeapon(event.getSource());if(!WeaponGrowth.supported(weapon))return;
        WeaponGrowth.ensureId(weapon);
        long now=event.getEntity().level().getGameTime();
        HITS.put(event.getEntity(),new Hit(player.getUUID(),weapon,event.getSource().getDirectEntity() instanceof Projectile p?p:null,now));
        int leech=WeaponGrowth.rank(weapon,Perk.LEECH);
        boolean hostile=event.getEntity() instanceof Enemy || event.getEntity() instanceof Player
                || event.getEntity() instanceof Mob mob && mob.getTarget()==player;
        if(hostile&&leech>0&&now-HEALED.getOrDefault(player,Long.MIN_VALUE/2)>=20) {
            player.heal(Math.min(.25f*leech,Math.min(event.getAmount(),event.getEntity().getHealth())*.15f));HEALED.put(player,now);
        }
    }
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void killed(LivingDeathEvent event) {
        if(event.getEntity().level().isClientSide||!(event.getEntity() instanceof Enemy
                ||event.getEntity() instanceof Mob mob&&mob.getTarget() instanceof Player))return;
        Hit hit=HITS.remove(event.getEntity());
        LivingEntity credit=event.getSource().getEntity() instanceof LivingEntity attacker?attacker:event.getEntity().getKillCredit();
        if(hit==null||!(credit instanceof Player player)||!hit.owner.equals(player.getUUID())
                ||event.getEntity().level().getGameTime()-hit.tick>100||player.isCreative()||player.isSpectator())return;
        UUID id=WeaponGrowth.id(hit.weapon);ItemStack original=ItemStack.EMPTY;
        for(int i=0;i<player.getInventory().getContainerSize();i++) {
            var stack=player.getInventory().getItem(i);if(id!=null&&id.equals(WeaponGrowth.id(stack))&&WeaponGrowth.supported(stack)){original=stack;break;}
        }
        if(original.isEmpty()&&hit.projectile instanceof ThrowingKnifeEntity knife)original=knife.getItem();
        if(original.isEmpty()&&hit.projectile instanceof ThrownTrident trident)original=((ThrownTridentGrowthAccessor)trident).tfpw$growthWeapon();
        if(!WeaponGrowth.supported(original)||!Objects.equals(id,WeaponGrowth.id(original)))return;
        int oldLevel=WeaponGrowth.level(original);WeaponGrowth.addXp(original,WeaponGrowthRules.killXp(event.getEntity().getMaxHealth()));
        if(hit.projectile instanceof ThrowingKnifeEntity knife)knife.setItem(original);
        if(WeaponGrowth.level(original)>oldLevel)player.displayClientMessage(Component.translatable("message.the_four_primitives_and_weapons.weapon_level_up",original.getHoverName(),WeaponGrowth.level(original)),true);
        player.getInventory().setChanged();player.inventoryMenu.broadcastChanges();
        if(event.getEntity().getMaxHealth()>=100||event.getEntity().getRandom().nextFloat()<.3f)
            event.getEntity().spawnAtLocation(new ItemStack(TheFourPrimitivesAndWeaponsModItems.TEMPERING_SHARD.get(),event.getEntity().getMaxHealth()>=100?4:1));
    }
    @SubscribeEvent public static void attributes(ItemAttributeModifierEvent event) {
        if(event.getSlotType()!=EquipmentSlot.MAINHAND||!WeaponGrowth.supported(event.getItemStack()))return;
        int rank=WeaponGrowth.rank(event.getItemStack(),Perk.HASTE);
        if(rank>0)event.addModifier(Attributes.ATTACK_SPEED,new AttributeModifier(HASTE,"Weapon growth haste",.06*rank,AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
    @SubscribeEvent public static void bow(net.minecraftforge.event.entity.player.ArrowLooseEvent event) {
        int rank=WeaponGrowth.rank(event.getBow(),Perk.HASTE);
        if(rank>0&&event.getCharge()>0)event.setCharge((int)Math.ceil(event.getCharge()*(1+.06*rank)));
    }
}
