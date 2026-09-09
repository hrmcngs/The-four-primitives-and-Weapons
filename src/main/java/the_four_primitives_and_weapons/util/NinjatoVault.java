package the_four_primitives_and_weapons.util;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import the_four_primitives_and_weapons.entity.StabbedWeaponEntity;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.item.SayaItem;

public final class NinjatoVault {
    public static final String TETHERED = "NinjatoTethered";
    public static final String MATERIAL = "NinjatoTetherMaterial";
    public static final String CORD_COLOR = "NinjatoCordColor";

    public static int cordColor(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(CORD_COLOR, 99)
            ? stack.getTag().getInt(CORD_COLOR) & 0xFFFFFF : 0xBEA370;
    }

    public static boolean hasDyeableCord(ItemStack stack) {
        return stack.is(TheFourPrimitivesAndWeaponsModItems.NINJATO_RECALL_CORD.get())
            || (isNinjatoSaya(stack) && stack.hasTag() && stack.getTag().getBoolean(TETHERED) && !isChain(stack));
    }

    public static boolean isChain(ItemStack stack) {
        return stack.hasTag() && "chain".equals(stack.getTag().getString(MATERIAL));
    }

    public static boolean isRecallItem(ItemStack stack) {
        return stack.is(TheFourPrimitivesAndWeaponsModItems.NINJATO_RECALL_CORD.get())
            || stack.is(TheFourPrimitivesAndWeaponsModItems.NINJATO_RECALL_CHAIN.get());
    }
    private static final Map<Player, NinjatoVaultGesture> GESTURES = new WeakHashMap<>();
    private NinjatoVault() {}

    public static boolean isLoaded(ItemStack stack) {
        return stack.getItem() instanceof SayaItem && stack.hasTag()
            && ItemStack.of(stack.getTag().getCompound("StoredKatana"))
                .is(TheFourPrimitivesAndWeaponsModItems.NINJATOU.get());
    }

    /** 専用鞘と、既存の汎用鞘へ忍者刀を納めたものの両方を識別する。 */
    public static boolean isNinjatoSaya(ItemStack stack) {
        return stack.is(TheFourPrimitivesAndWeaponsModItems.NINJATO_SAYA.get()) || isLoaded(stack)
            || (stack.getItem() instanceof SayaItem && stack.hasTag()
                && ItemStack.of(stack.getTag().getCompound("StoredKatana")).isEmpty()
                && "the_four_primitives_and_weapons:ninjatou".equals(stack.getTag().getString("LastSheathedWeapon")));
    }

    public static void begin(Player player) {
        if (!player.level().isClientSide) {
            NinjatoVaultGesture gesture = new NinjatoVaultGesture();
            gesture.update(player.tickCount, player.getXRot());
            GESTURES.put(player, gesture);
        }
    }

    public static void end(Player player) { GESTURES.remove(player); }

    public static void tick(Player player, ItemStack stack) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (player.getUsedItemHand() != InteractionHand.MAIN_HAND || !isLoaded(stack)) {
            end(player);
            return;
        }
        NinjatoVaultGesture gesture = GESTURES.get(player);
        if (gesture == null || !gesture.update(player.tickCount, player.getXRot())) return;
        if (!player.onGround() || player.isPassenger() || player.isSpectator()) return;
        Vec3 eye = player.getEyePosition();
        BlockHitResult hit = player.level().clip(new ClipContext(eye,
            eye.add(player.getLookAngle().scale(4.5)), ClipContext.Block.COLLIDER,
            ClipContext.Fluid.NONE, player));
        if (hit.getType() != HitResult.Type.BLOCK || hit.getDirection() != Direction.UP
                || !player.mayInteract(player.level(), hit.getBlockPos())) return;

        StabbedWeaponEntity planted = new StabbedWeaponEntity(player.level());
        planted.setItem(stack);
        planted.setVaultOwner(player.getUUID(), stack.getOrCreateTag().getBoolean(TETHERED));
        planted.setStabYaw(player.getYRot());
        planted.setTilt(0);
        // 鞘の手持ちモデルには既に180度の反転があるため、設置用の反転を相殺する。
        planted.setRoll(180);
        planted.setRadius(0.7F);
        // モデル変換後の鞘先を狙った点へ合わせる。先端だけ0.04ブロック埋める。
        Vec3[] axis = planted.weaponSegment();
        Vec3 tip = axis[0].y < axis[1].y ? axis[0] : axis[1];
        Vec3 origin = planted.position().add(hit.getLocation().subtract(tip)).add(0, -0.04, 0);
        planted.setPos(origin.x, origin.y, origin.z);
        if (!player.level().addFreshEntity(planted)) return;

        ItemStack inHand = ItemStack.EMPTY;
        if (stack.getOrCreateTag().getBoolean(TETHERED)) {
            inHand = new ItemStack(isChain(stack) ? TheFourPrimitivesAndWeaponsModItems.NINJATO_RECALL_CHAIN.get()
                : TheFourPrimitivesAndWeaponsModItems.NINJATO_RECALL_CORD.get());
            inHand.getOrCreateTag().putUUID("PlantedWeapon", planted.getUUID());
            inHand.getOrCreateTag().putUUID("CordOwner", player.getUUID());
            if (stack.getTag().contains(CORD_COLOR, 99))
                inHand.getOrCreateTag().putInt(CORD_COLOR, cordColor(stack));
        }
        player.stopUsingItem();
        player.setItemInHand(InteractionHand.MAIN_HAND, inHand);
        if (!inHand.isEmpty()) player.startUsingItem(InteractionHand.MAIN_HAND);
        end(player);
        // 同じ振り下ろしでも、Shift中は設置だけにする。
        if (!player.isShiftKeyDown()) {
            Vec3 velocity = player.getDeltaMovement();
            // 通常ジャンプと同じ効果量を、忍者刀の跳躍速度へ加算する。
            player.setDeltaMovement(velocity.x, 1.25 + player.getJumpBoostPower(), velocity.z);
            player.fallDistance = 0;
            player.hurtMarked = true;
            serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(player));
        }
        player.level().playSound(null, hit.getBlockPos(), net.minecraft.sounds.SoundEvents.TRIDENT_HIT_GROUND,
            net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 0.9F);
    }
}
