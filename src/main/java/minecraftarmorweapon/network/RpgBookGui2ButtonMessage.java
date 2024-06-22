
package minecraftarmorweapon.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import minecraftarmorweapon.world.inventory.RpgBookGui2Menu;

import minecraftarmorweapon.procedures.RpgBookGuibackProcedure;
import minecraftarmorweapon.procedures.RpgBookGuiToziruProcedure;
import minecraftarmorweapon.procedures.RpgBookGuiNiguTapProcedure;
import minecraftarmorweapon.procedures.RpgBookGuiChuzumeProcedure;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RpgBookGui2ButtonMessage {
	private final int buttonID, x, y, z;

	public RpgBookGui2ButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public RpgBookGui2ButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(RpgBookGui2ButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(RpgBookGui2ButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
			int buttonID = message.buttonID;
			int x = message.x;
			int y = message.y;
			int z = message.z;
			handleButtonAction(entity, buttonID, x, y, z);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level;
		HashMap guistate = RpgBookGui2Menu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			RpgBookGuiNiguTapProcedure.execute(entity);
		}
		if (buttonID == 1) {

			RpgBookGuiChuzumeProcedure.execute(entity);
		}
		if (buttonID == 2) {

			RpgBookGuiToziruProcedure.execute(entity);
		}
		if (buttonID == 4) {

			RpgBookGuibackProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinecraftArmorWeaponMod.addNetworkMessage(RpgBookGui2ButtonMessage.class, RpgBookGui2ButtonMessage::buffer, RpgBookGui2ButtonMessage::new, RpgBookGui2ButtonMessage::handler);
	}
}
