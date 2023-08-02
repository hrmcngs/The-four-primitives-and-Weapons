package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.network.MinecraftArmorWeaponModVariables;

import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class NetheriteKatanaYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double degree = 0;
		double xRadius = 0;
		double zRadius = 0;
		double x_pos = 0;
		double y_pos = 0;
		double z_pos = 0;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 2) {
			if (entity instanceof LivingEntity _entity)
				_entity.swing(InteractionHand.MAIN_HAND, true);
			r = 1;
			alpha = entity.getYRot();
			beta = entity.getXRot();
			for (int index0 = 0; index0 < 180; index0++) {
				if (entity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.MAIN_HAND, true);
				entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((x + 2), y, (z + 2)));
			}
			for (int index1 = 0; index1 < 5; index1++) {
				{
					final Vec3 _center = new Vec3((x - r * Math.cos(Math.toRadians(beta)) * Math.sin(Math.toRadians(alpha))), (y + 1), (z + r * Math.cos(Math.toRadians(beta)) * Math.cos(Math.toRadians(alpha))));
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator == entity)) {
							if (entityiterator instanceof Mob) {
								if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
									{
										Entity _ent = entityiterator;
										if (!_ent.level.isClientSide() && _ent.getServer() != null) {
											_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(),
													_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "kill @s");
										}
									}
									{
										Entity _ent = entityiterator;
										if (!_ent.level.isClientSide() && _ent.getServer() != null) {
											_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(),
													_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "deta merge entity @s (Health:0)");
										}
									}
								} else {
									if (entityiterator instanceof LivingEntity _entity)
										_entity.hurt(new DamageSource("damege").bypassArmor(), 10);
								}
							}
						}
					}
				}
				world.addParticle(ParticleTypes.SWEEP_ATTACK, (x - r * Math.cos(Math.toRadians(beta)) * Math.sin(Math.toRadians(alpha))), (y + 1), (z + r * Math.cos(Math.toRadians(beta)) * Math.cos(Math.toRadians(alpha))), 0, 0, 0);
				r = r + 0.2;
			}
		}
	}
}
