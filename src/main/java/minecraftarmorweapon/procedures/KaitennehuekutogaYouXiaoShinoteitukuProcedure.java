package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class KaitennehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		double rrr = 0;
		double Radius = 0;
		double loop = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		double zRadius3 = 0;
		double zRadius2 = 0;
		double yknockback = 0;
		double zRadius = 0;
		double xRadius2 = 0;
		double xRadius3 = 0;
		double xRadius4 = 0;
		double zRadius4 = 0;
		double dis = 0;
		double Z2 = 0;
		double Z3 = 0;
		double X2 = 0;
		double Z4 = 0;
		double X3 = 0;
		double X4 = 0;
		double xknockback = 0;
		double loop2 = 0;
		double loop3 = 0;
		double zknockback = 0;
		double xRadius = 0;
		double loop4 = 0;
		double Y2 = 0;
		double Y3 = 0;
		double Y4 = 0;
		double dis1 = 0;
		if (entity instanceof LivingEntity _entity)
			_entity.swing(InteractionHand.MAIN_HAND, true);
		loop = Math.toRadians(entity.getYRot());
		xRadius = 4;
		zRadius = 4;
		for (int index0 = 0; index0 < 72; index0++) {
			X = x + Math.cos(loop) * xRadius;
			Y = y + 1;
			Z = z + Math.sin(loop) * zRadius;
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.SWEEP_ATTACK, X, Y, Z, 1, 0.1, 0.1, 0.1, 1);
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X, Y, Z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 10");
			{
				final Vec3 _center = new Vec3(X, Y, Z);
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (!(entityiterator == entity)) {
						if (entityiterator instanceof Mob) {
							if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/kill @s");
									}
								}
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/deta merge entity @s (Health:0)");
									}
								}
							} else {
								entityiterator.hurt(DamageSource.GENERIC, 16);
							}
							xknockback = entityiterator.getX() - entity.getX();
							yknockback = entityiterator.getY() - entity.getY();
							zknockback = entityiterator.getZ() - entity.getZ();
							dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
							if (dis != 0) {
								xknockback = (xknockback / dis) * 5;
								yknockback = Math.min((yknockback / dis) * 13, 9.8);
								zknockback = (zknockback / dis) * 5;
							} else {
								xknockback = 0;
								yknockback = 0;
								zknockback = 0;
							}
							entityiterator.setDeltaMovement(new Vec3(xknockback, yknockback, zknockback));
						}
					}
				}
			}
			loop = loop + Math.toRadians(5);
		}
	}
}
