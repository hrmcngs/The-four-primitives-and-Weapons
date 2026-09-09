package the_four_primitives_and_weapons.util;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import top.theillusivec4.curios.api.CuriosApi;

/** ringスロットの実装備をサーバーで検証して武器を呼び出す。 */
public final class PromiseRing {
    private PromiseRing() {}

    public static ItemStack findEquipped(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(entity).map(handler ->
            handler.getStacksHandler("ring").map(slots -> {
                for (int i = 0; i < slots.getStacks().getSlots(); i++) {
                    ItemStack item = slots.getStacks().getStackInSlot(i);
                    if (item.is(TheFourPrimitivesAndWeaponsModItems.RING.get())) return item;
                }
                return ItemStack.EMPTY;
            }).orElse(ItemStack.EMPTY)).orElse(ItemStack.EMPTY);
    }

    public static Item weapon(int form) {
        return switch (form) {
            case 0 -> TheFourPrimitivesAndWeaponsModItems.PROMISE_KATANA.get();
            case 1 -> TheFourPrimitivesAndWeaponsModItems.PROMISE_SWORD.get();
            case 2 -> TheFourPrimitivesAndWeaponsModItems.PROMISE_STRAIGHT_SWORD.get();
            default -> null;
        };
    }

    public static int form(ItemStack stack) {
        for (int i = 0; i < 3; i++) if (stack.is(weapon(i))) return i;
        return -1;
    }

    private static String storage(int form) { return "PromiseStored" + form; }

    public static void addDrawOptions(net.minecraft.world.entity.player.Player player,
            java.util.List<CuriosScabbardHelper.DrawableWeaponInfo> options) {
        ItemStack ring = findEquipped(player);
        if (ring.isEmpty()) return;
        String[] forms = {"katana", "sword", "straight_sword"};
        for (int i = 0; i < 3; i++) {
            Item item = weapon(i);
            // 鞘に納めてある同じ形は、既存の鞘から抜く候補を使う。
            if (options.stream().anyMatch(option -> option.weaponStack.is(item))) continue;
            ItemStack preview = ring.hasTag() ? ItemStack.of(ring.getTag().getCompound(storage(i))) : ItemStack.EMPTY;
            if (preview.isEmpty()) preview = new ItemStack(item);
            options.add(new CuriosScabbardHelper.DrawableWeaponInfo(ring, preview,
                    CuriosScabbardHelper.ScabbardLocation.RING, "ring", i,
                    "ring · " + Component.translatable("gui.the_four_primitives_and_weapons.ring.form." + forms[i]).getString()));
        }
    }

    public static void addStoreOptions(net.minecraft.world.entity.player.Player player,
            java.util.List<CuriosScabbardHelper.DrawableWeaponInfo> results) {
        ItemStack ring = PromiseRing.findEquipped(player);
        for (net.minecraft.world.InteractionHand hand : net.minecraft.world.InteractionHand.values()) {
            ItemStack held = player.getItemInHand(hand);
            if (PromiseRing.canStore(ring, held)) {
                results.add(new CuriosScabbardHelper.DrawableWeaponInfo(ring, held, CuriosScabbardHelper.ScabbardLocation.RING, "ring", PromiseRing.form(held), "ring"));
                break;
            }
        }
    }

    public static boolean canStore(ItemStack ring, ItemStack weapon) {
        int form = form(weapon);
        return ring.is(TheFourPrimitivesAndWeaponsModItems.RING.get()) && form >= 0
                && (!ring.hasTag() || !ring.getTag().contains(storage(form)));
    }

    public static boolean store(net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand) {
        if (!(player instanceof ServerPlayer server) || !player.isAlive() || player.isSpectator()) return false;
        ItemStack ring = findEquipped(player);
        ItemStack held = player.getItemInHand(hand);
        if (!canStore(ring, held)) return false;
        int form = form(held);
        ring.getOrCreateTag().put(storage(form), held.save(new net.minecraft.nbt.CompoundTag()));
        ring.getOrCreateTag().putInt("PromiseSelectedForm", form);
        player.setItemInHand(hand, ItemStack.EMPTY);
        changed(server);
        return true;
    }

    public static boolean draw(net.minecraft.world.entity.player.Player player, int form) {
        if (!(player instanceof ServerPlayer server) || !player.isAlive() || player.isSpectator()
                || !player.getMainHandItem().isEmpty()) return false;
        Item item = weapon(form);
        ItemStack ring = findEquipped(player);
        if (item == null || ring.isEmpty()) return false;
        ItemStack drawn = ring.hasTag() ? ItemStack.of(ring.getTag().getCompound(storage(form))) : ItemStack.EMPTY;
        if (!drawn.isEmpty()) {
            if (!drawn.is(item)) return false;
            ring.getTag().remove(storage(form));
        } else {
            // インベントリに呼び出し済みなら、その実物を手元へ移動する。
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack existing = player.getInventory().getItem(i);
                if (existing.is(item)) {
                    drawn = existing;
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                    break;
                }
            }
            if (drawn.isEmpty()) {
                for (var option : CuriosScabbardHelper.findAllLoadedScabbards(player)) {
                    if (option.location != CuriosScabbardHelper.ScabbardLocation.RING && option.weaponStack.is(item)) return false;
                }
                if (player.getCooldowns().isOnCooldown(TheFourPrimitivesAndWeaponsModItems.RING.get())) return false;
                if (!ring.getOrCreateTag().hasUUID("PromiseRingId")) ring.getOrCreateTag().putUUID("PromiseRingId", UUID.randomUUID());
                drawn = new ItemStack(item);
                drawn.getOrCreateTag().putString("StoryWeaponId", "promise_end");
                drawn.getOrCreateTag().putUUID("PromiseRingId", ring.getTag().getUUID("PromiseRingId"));
                drawn.getOrCreateTag().putUUID("PromiseOwner", player.getUUID());
                player.getCooldowns().addCooldown(TheFourPrimitivesAndWeaponsModItems.RING.get(), 20);
            }
        }
        ring.getOrCreateTag().putInt("PromiseSelectedForm", form);
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, drawn);
        changed(server);
        return true;
    }

    public static boolean drawSelected(net.minecraft.world.entity.player.Player player) {
        ItemStack ring = findEquipped(player);
        if (ring.isEmpty()) return false;
        int selected = ring.hasTag() ? ring.getTag().getInt("PromiseSelectedForm") : 0;
        return draw(player, selected >= 0 && selected < 3 ? selected : 0);
    }

    private static void changed(ServerPlayer player) {
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        player.level().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_IRON,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
