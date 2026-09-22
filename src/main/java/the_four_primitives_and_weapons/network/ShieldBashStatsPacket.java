package the_four_primitives_and_weapons.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.event.ShieldBashHandler;

/** サーバー側JSONの盾設定をログイン・/reload時にHUDと説明欄へ同期する。 */
public record ShieldBashStatsPacket(Map<ResourceLocation, ShieldBashHandler.Settings> settings) {
    public ShieldBashStatsPacket(FriendlyByteBuf buffer) {
        this(buffer.readMap(FriendlyByteBuf::readResourceLocation,
                buf -> new ShieldBashHandler.Settings(buf.readFloat(), buf.readVarInt())));
    }

    public static void encode(ShieldBashStatsPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.settings, FriendlyByteBuf::writeResourceLocation, (buf, value) -> {
            buf.writeFloat(value.damage());
            buf.writeVarInt(value.cooldown());
        });
    }

    public static void handle(ShieldBashStatsPacket packet, Supplier<NetworkEvent.Context> context) {
        var ctx = context.get();
        if (ctx.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.enqueueWork(() -> {
                ShieldBashHandler.CLIENT_SETTINGS.clear();
                ShieldBashHandler.CLIENT_SETTINGS.putAll(packet.settings);
            });
        }
        ctx.setPacketHandled(true);
    }

    @Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent
        public static void register(FMLCommonSetupEvent event) {
            TheFourPrimitivesAndWeaponsMod.addNetworkMessage(ShieldBashStatsPacket.class,
                    ShieldBashStatsPacket::encode, ShieldBashStatsPacket::new, ShieldBashStatsPacket::handle);
        }
    }

    @Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID)
    public static final class Sync {
        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
            Map<ResourceLocation, ShieldBashHandler.Settings> settings = new HashMap<>();
            ForgeRegistries.ITEMS.forEach(item -> {
                ItemStack stack = new ItemStack(item);
                if (ShieldBashHandler.isShield(stack)) settings.put(ForgeRegistries.ITEMS.getKey(item),
                        new ShieldBashHandler.Settings(ShieldBashHandler.getAttackDamage(stack), ShieldBashHandler.getCooldownTicks(stack)));
            });
            var packet = new ShieldBashStatsPacket(settings);
            if (event.getPlayer() != null) {
                TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(event::getPlayer), packet);
            } else {
                TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), packet);
            }
        }
    }
}
