package the_four_primitives_and_weapons.entity;

import java.util.Collections;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModCustomEntities;
import the_four_primitives_and_weapons.util.NinjatoTetherCutRule;

/** 接続部分だけの不可視の当たり判定。通常攻撃と既存スキルの範囲判定で共通に狙える。 */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", bus = Mod.EventBusSubscriber.Bus.MOD)
public class NinjatoTetherSegmentEntity extends LivingEntity {
    private StabbedWeaponEntity tether;

    public NinjatoTetherSegmentEntity(EntityType<? extends NinjatoTetherSegmentEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
    }

    public void attach(StabbedWeaponEntity parent) { tether = parent; }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TheFourPrimitivesAndWeaponsModCustomEntities.NINJATO_TETHER_SEGMENT.get(),
            LivingEntity.createLivingAttributes().build());
    }

    @Override
    public void tick() {
        if (!level().isClientSide && (tether == null || tether.isRemoved() || tether.getTetherOwner().isEmpty())) {
            discard();
            return;
        }
        // 武器・鎧・ポーション等の生物向けtick処理は不要。位置は接続元が更新する。
        baseTick();
    }

    public boolean hurtFromSkill(Player player, float damage) {
        return !level().isClientSide && tether != null && !isRemoved() && tether.cutTether(player, true, damage);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide || isRemoved() || tether == null) return false;
        if (!(source.getEntity() instanceof Player player) || source.getDirectEntity() != player) return false;
        return tether.cutTether(player, NinjatoTetherCutRule.inSkill(), amount);
    }

    @Override public boolean canBeSeenAsEnemy() { return false; }
    @Override public boolean isPickable() { return !isRemoved(); }
    @Override public boolean isPushable() { return false; }
    @Override public boolean isPushedByFluid() { return false; }
    @Override public boolean canBeAffected(net.minecraft.world.effect.MobEffectInstance effect) { return false; }
    @Override public Iterable<ItemStack> getArmorSlots() { return Collections.emptyList(); }
    @Override public ItemStack getItemBySlot(EquipmentSlot slot) { return ItemStack.EMPTY; }
    @Override public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}
    @Override public HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }
    @Override public Packet<ClientGamePacketListener> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}
