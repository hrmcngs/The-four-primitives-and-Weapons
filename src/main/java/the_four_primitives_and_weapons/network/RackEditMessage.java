package the_four_primitives_and_weapons.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.entity.WeaponRackEntity;

@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", bus = Mod.EventBusSubscriber.Bus.MOD)
public record RackEditMessage(int entityId, int slot, int mode, int direction, boolean fine) {
    public RackEditMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
    }
    public static void buffer(RackEditMessage m, FriendlyByteBuf buf) {
        buf.writeInt(m.entityId); buf.writeInt(m.slot); buf.writeInt(m.mode);
        buf.writeInt(m.direction); buf.writeBoolean(m.fine);
    }
    public static void handler(RackEditMessage m, Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        ctx.enqueueWork(() -> {
            var player = ctx.getSender();
            if (player == null || player.isSpectator() || m.slot < 0 || m.slot > 1
                || m.mode < 0 || m.mode > 8 || (m.direction != -1 && m.direction != 1)) return;
            if (!(player.level().getEntity(m.entityId) instanceof WeaponRackEntity rack)
                || rack.isRemoved() || rack.distanceToSqr(player) > 36
                || !player.hasLineOfSight(rack) || !player.level().mayInteract(player, rack.blockPosition())) return;
            rack.editSlot(m.slot, m.mode, m.direction, m.fine);
        });
        ctx.setPacketHandled(true);
    }
    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(RackEditMessage.class,
            RackEditMessage::buffer, RackEditMessage::new, RackEditMessage::handler);
    }
}
