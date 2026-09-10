package the_four_primitives_and_weapons.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.item.GateItem;
import the_four_primitives_and_weapons.item.LunaItem;
import the_four_primitives_and_weapons.util.GateDropContext;
import the_four_primitives_and_weapons.util.GateDropContext.Reason;

import javax.annotation.Nullable;
import java.util.UUID;

/** Shared Gate/Luna provenance. Item ownership/thrower UUID alone never authorizes a summon. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class GateDropHandler {
    public static final String DROP_DATA = "the_four_primitives_and_weapons:gate_drop";
    public static final String LAST_DROP = "the_four_primitives_and_weapons:gate_last_drop";

    public static final String LUNA_DROP_DATA = "the_four_primitives_and_weapons:luna_drop";
    public static final String LUNA_LAST_DROP = "the_four_primitives_and_weapons:luna_last_drop";

    private GateDropHandler() {}

    private static boolean isTracked(ItemStack stack) {
        return GateItem.isGateSword(stack) || stack.getItem() instanceof LunaItem;
    }

    private static String dataKey(ItemEntity dropped) {
        return dropped.getItem().getItem() instanceof LunaItem
                || dropped.getPersistentData().contains(LUNA_DROP_DATA) ? LUNA_DROP_DATA : DROP_DATA;
    }

    private static CompoundTag dropData(ItemEntity dropped) {
        return dropped.getPersistentData().getCompound(dataKey(dropped));
    }

    /** Called only from ServerPlayer.drop(boolean), after vanilla removes the selected stack. */
    @Nullable
    public static ItemEntity dropFromKey(Player player, ItemStack stack, boolean includeName) {
        if (!isTracked(stack)) return ForgeHooks.onPlayerTossEvent(player, stack, includeName);
        int selected = player.getInventory().selected;
        Reason reason = player.isShiftKeyDown() ? Reason.SNEAK_DROP_KEY : Reason.DROP_KEY;
        ItemEntity dropped = GateDropContext.during(player.getUUID(), reason,
                () -> ForgeHooks.onPlayerTossEvent(player, stack, includeName));
        // Forge cancellation can transfer the item to a grave/other system. Never recreate it.
        if (dropped == null || dropped.isRemoved() || !dropped.isAddedToWorld()
                || !isTracked(dropped.getItem())) return dropped;
        CompoundTag provenance = dropData(dropped);
        if (!provenance.hasUUID("ActorUUID") || !player.getUUID().equals(provenance.getUUID("ActorUUID"))
                || !provenance.hasUUID("ItemEntityUUID")
                || !dropped.getUUID().equals(provenance.getUUID("ItemEntityUUID"))
                || !Reason.DROP_KEY.name().equals(provenance.getString("Reason"))) return dropped;
        boolean connected = player instanceof ServerPlayer serverPlayer && !serverPlayer.hasDisconnected();
        if (!GateDropContext.canSummon(reason, player.isAlive(), connected, player.isSpectator())) return dropped;
        // Do not overwrite a slot changed by another event handler or summon from the wrong hand.
        if (player.getInventory().selected != selected || !player.getInventory().getItem(selected).isEmpty()) {
            finish(player, dropped, "DROPPED_SLOT_CHANGED");
            return dropped;
        }
        if (dropped.getItem().getItem() instanceof LunaItem) {
            UUID companionId = LunaCompanionHandler.trySummon(player, dropped);
            if (companionId != null) dropData(dropped).putUUID("SummonedEntityUUID", companionId);
            finish(player, dropped, companionId == null ? "DROPPED_SUMMON_FAILED" : "SUMMONED");
            return dropped;
        }
        ItemStack returned = dropped.getItem();
        GateItem gate = (GateItem) returned.getItem();
        player.getInventory().setItem(selected, returned);
        dropped.setItem(ItemStack.EMPTY);
        dropped.discard();
        boolean cooldown = player.getCooldowns().isOnCooldown(gate);
        finish(player, dropped, cooldown ? "RETURNED_COOLDOWN" : "SUMMONED");
        if (!cooldown) gate.use(player.level(), player, InteractionHand.MAIN_HAND);
        player.containerMenu.broadcastChanges();
        return dropped;
    }

    @Nullable
    public static ItemEntity dropFromMenu(Player player, ItemStack stack, boolean includeName, Reason reason) {
        if (!isTracked(stack) || player.level().isClientSide) return player.drop(stack, includeName);
        Reason actual = !player.isAlive() ? Reason.DEATH
                : player instanceof ServerPlayer sp && sp.hasDisconnected() ? Reason.DISCONNECT : reason;
        return GateDropContext.during(player.getUUID(), actual, () -> player.drop(stack, includeName));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onToss(ItemTossEvent event) {
        Player player = event.getPlayer();
        ItemEntity dropped = event.getEntity();
        if (player.level().isClientSide || !isTracked(dropped.getItem())) return;
        Reason reason = GateDropContext.current(player.getUUID());
        if (!player.isAlive()) reason = Reason.DEATH;
        else if (player instanceof ServerPlayer sp && sp.hasDisconnected()) reason = Reason.DISCONNECT;
        else if (reason == Reason.UNKNOWN) reason = Reason.PLAYER_TOSS_OTHER;
        record(dropped, player.getUUID(), reason);
        finish(player, dropped, "DROPPED");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeathDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;
        for (ItemEntity dropped : event.getDrops()) {
            if (!isTracked(dropped.getItem())) continue;
            record(dropped, event.getEntity().getUUID(), Reason.DEATH);
            CompoundTag data = dropData(dropped);
            data.putString("DamageSource", event.getSource().getMsgId());
            if (event.getSource().getEntity() != null) {
                data.putUUID("AttackerUUID", event.getSource().getEntity().getUUID());
            }
            if (event.getEntity() instanceof Player player) finish(player, dropped, "DROPPED");
        }
        // No cancellation, restitution or summoning. keepInventory, vanishing and grave mods own death rules.
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide || !(event.getEntity() instanceof ItemEntity dropped)
                || !isTracked(dropped.getItem())) return;
        // A reloaded item keeps its provenance; old saves, blocks, dispensers and commands are ordinary drops.
        CompoundTag data = dropData(dropped);
        if (!data.hasUUID("ItemEntityUUID") || !dropped.getUUID().equals(data.getUUID("ItemEntityUUID"))) {
            record(dropped, null, Reason.UNKNOWN);
        }
    }

    /** Lookup by the dropped entity UUID, including the player's most recent reclaimed Gate. */
    @Nullable
    public static CompoundTag findByUUID(ServerLevel level, UUID itemEntityId) {
        Entity entity = level.getEntity(itemEntityId);
        if (entity instanceof ItemEntity item
                && (item.getPersistentData().contains(DROP_DATA) || item.getPersistentData().contains(LUNA_DROP_DATA))) {
            return dropData(item).copy();
        }
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            for (String key : new String[] {LAST_DROP, LUNA_LAST_DROP}) {
                CompoundTag last = player.getPersistentData().getCompound(key);
                if (last.hasUUID("ItemEntityUUID") && itemEntityId.equals(last.getUUID("ItemEntityUUID"))) {
                    return last.copy();
                }
            }
        }
        return null;
    }

    public static boolean addWorldDrop(Level level, Entity entity, Reason reason) {
        if (!level.isClientSide && entity instanceof ItemEntity dropped && isTracked(dropped.getItem())) {
            record(dropped, null, reason);
        }
        return level.addFreshEntity(entity);
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        for (String key : new String[] {LAST_DROP, LUNA_LAST_DROP}) {
            if (event.getOriginal().getPersistentData().contains(key)) {
                event.getEntity().getPersistentData().put(key,
                        event.getOriginal().getPersistentData().getCompound(key).copy());
            }
        }
    }

    private static void record(ItemEntity dropped, @Nullable UUID actorId, Reason reason) {
        CompoundTag data = new CompoundTag();
        data.putUUID("ItemEntityUUID", dropped.getUUID());
        if (actorId != null) data.putUUID("ActorUUID", actorId);
        data.putString("Reason", reason.name());
        data.putLong("GameTime", dropped.level().getGameTime());
        data.putString("Dimension", dropped.level().dimension().location().toString());
        data.putString("Outcome", "DROPPED");
        dropped.getPersistentData().put(dataKey(dropped), data);
    }

    private static void finish(Player player, ItemEntity dropped, String outcome) {
        CompoundTag data = dropData(dropped);
        data.putString("Outcome", outcome);
        // The latest action remains inspectable even when its temporary item entity was reclaimed.
        player.getPersistentData().put(dataKey(dropped).equals(LUNA_DROP_DATA) ? LUNA_LAST_DROP : LAST_DROP, data.copy());
    }
}
