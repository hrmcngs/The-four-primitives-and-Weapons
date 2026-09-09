package the_four_primitives_and_weapons.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.entity.StabbedWeaponEntity;
import the_four_primitives_and_weapons.entity.WeaponRackEntity;
import the_four_primitives_and_weapons.network.OpenStabEditMessage;

/**
 * /weaponedit (別名 /stabedit・/rackedit): 視線先のラック・刺さった武器/杭を微調整する。
 */
@Mod.EventBusSubscriber
public class StabEditCommand {

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		var edit = dispatcher.register(Commands.literal("weaponedit")
				.requires(s -> s.hasPermission(0))
				.executes(ctx -> open(ctx.getSource())));
		dispatcher.register(Commands.literal("stabedit").executes(ctx -> open(ctx.getSource())).redirect(edit));
		dispatcher.register(Commands.literal("rackedit").executes(ctx -> open(ctx.getSource())).redirect(edit));
	}

	private static int open(CommandSourceStack source) {
		if (!(source.getEntity() instanceof ServerPlayer player)) {
			source.sendFailure(Component.literal("§cプレイヤーから実行してください"));
			return 0;
		}
		double reach = 6.0;
		Vec3 eye = player.getEyePosition(1.0f);
		Vec3 look = player.getViewVector(1.0f);
		// OBB ( カプセル ) 精密判定で、 視線が実際に武器に当たっているものを選ぶ
		AABB search = player.getBoundingBox().expandTowards(look.scale(reach)).inflate(2.0);
		Entity target = null;
		double bestT = Double.MAX_VALUE;
		for (StabbedWeaponEntity cand : player.level().getEntitiesOfClass(StabbedWeaponEntity.class, search)) {
			double t = cand.clipWeapon(eye, look, reach);
			if (t >= 0 && t < bestT) { bestT = t; target = cand; }
		}
		Vec3 end = eye.add(look.scale(reach));
		for (WeaponRackEntity rack : player.level().getEntitiesOfClass(WeaponRackEntity.class, search)) {
			if (rack.distanceToSqr(player) > 36 || !player.hasLineOfSight(rack)) continue;
			var box = rack.getBoundingBox();
			var hit = box.clip(eye, end);
			double t = box.contains(eye) ? 0 : hit.map(eye::distanceTo).orElse(-1.0);
			if (t >= 0 && t < bestT) { bestT = t; target = rack; }
		}
		if (target == null) {
			source.sendFailure(Component.literal("§c編集するラック・刺さった武器/杭にカーソルを合わせてください"));
			return 0;
		}
		TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.send(
				PacketDistributor.PLAYER.with(() -> player), new OpenStabEditMessage(target.getId()));
		return 1;
	}
}
