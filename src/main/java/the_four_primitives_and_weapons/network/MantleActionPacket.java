package the_four_primitives_and_weapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.init.KnifeExtrasRegistrar;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MantleActionPacket {
    public enum Action { SWEEP, THRUST, GUARD }
    private final Action action;
    public MantleActionPacket(Action action) { this.action = action; }
    public MantleActionPacket(FriendlyByteBuf buf) { action = buf.readEnum(Action.class); }
    public static void encode(MantleActionPacket msg, FriendlyByteBuf buf) { buf.writeEnum(msg.action); }

    public static ItemStack equipped(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, KnifeExtrasRegistrar.FANTASY_MANTLE.get())
            .map(result -> result.stack()).orElse(ItemStack.EMPTY);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || !player.isAlive() || player.isSpectator() || equipped(player).isEmpty()
                || player.getCooldowns().isOnCooldown(KnifeExtrasRegistrar.FANTASY_MANTLE.get())) return;
            player.getCooldowns().addCooldown(KnifeExtrasRegistrar.FANTASY_MANTLE.get(), action == Action.GUARD ? 120 : 40);
            if (action == Action.GUARD) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 1));
                player.serverLevel().sendParticles(ParticleTypes.ENCHANT, player.getX(), player.getY() + 1,
                    player.getZ(), 36, .7, .8, .7, .1);
                player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, .7f);
                return;
            }
            Vec3 direction = player.getLookAngle().normalize();
            double range = action == Action.SWEEP ? 4 : 7;
            for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(range), target -> target != player && target.isAlive())) {
                Vec3 delta = target.getBoundingBox().getCenter().subtract(player.getEyePosition());
                double along = delta.dot(direction);
                boolean hit = action == Action.SWEEP
                    ? delta.lengthSqr() <= range * range && along > 0
                    : along > 0 && along <= range && delta.subtract(direction.scale(along)).lengthSqr() < 1;
                if (!hit || player.isAlliedTo(target) || target.isAlliedTo(player) || !player.hasLineOfSight(target)) continue;
                if (target.hurt(player.damageSources().playerAttack(player), action == Action.SWEEP ? 7 : 10)) {
                    target.knockback(.6, player.getX() - target.getX(), player.getZ() - target.getZ());
                }
            }
            for (int i = 1; i <= 14; i++) {
                Vec3 point = player.getEyePosition().add(direction.scale(range * i / 14));
                player.serverLevel().sendParticles(action == Action.SWEEP ? ParticleTypes.SWEEP_ATTACK : ParticleTypes.CRIT,
                    point.x, point.y, point.z, 2, .25, .2, .25, .02);
            }
            player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1, .7f);
        });
        ctx.get().setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(MantleActionPacket.class,
            MantleActionPacket::encode, MantleActionPacket::new, (msg, ctx) -> msg.handle(ctx));
    }
}
