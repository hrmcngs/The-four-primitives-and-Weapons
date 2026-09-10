package the_four_primitives_and_weapons.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import javax.annotation.Nullable;

/**
 * Gate - 金の直刀ベースの特殊武器。
 * 右クリックでプレイヤーの周囲に剣の陣を展開し、短い待機の後に順番に
 * 視線方向のターゲットに向かって飛んでいく。
 * ヒット時にブロック破壊なしの爆発。
 * データパック「gate1-16」のForge MOD移植版。
 */
public class GateItem extends SwordItem {
    public GateItem() {
        super(new Tier() {
            public int getUses() { return 0; }
            public float getSpeed() { return 4f; }
            public float getAttackDamageBonus() { return 6f; }
            public int getLevel() { return 4; }
            public int getEnchantmentValue() { return 22; }
            public Ingredient getRepairIngredient() { return Ingredient.of(Items.GOLD_INGOT); }
        }, 3, -2.4f, new Properties().rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Shift押下中は発射しない（ガード等を優先）
        if (player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide) {
            Vec3 lookVec = player.getLookAngle();
            Vec3 eyePos = player.getEyePosition();

            // プレイヤーの前方・右方向ベクトル（水平面）
            double yawRad = Math.toRadians(player.getYRot());
            double forwardX = -Math.sin(yawRad);
            double forwardZ = Math.cos(yawRad);
            double rightX = -Math.cos(yawRad);
            double rightZ = -Math.sin(yawRad);

            // 複数段の扇状に展開する。中央上部ほど高くして視界を空ける。
            int count = GateFormula.gateProjectileCount();
            double side = GateFormula.gateSideSpread();
            double fwd  = GateFormula.gateForwardOffset();
            double vy   = GateFormula.gateVerticalOffset();
            double speed = GateFormula.gateShootVelocity();

            int columns = Math.min(count, GateFormula.gateColumns());
            for (int i = 0; i < count; i++) {
                int row = i / columns;
                int rowCount = Math.min(columns, count - row * columns);
                double position = rowCount <= 1 ? 0.0 : -1.0 + 2.0 * (i % columns) / (rowCount - 1);
                double lateral = side * position * (1.0 + row * 0.12);
                double height = vy + row * GateFormula.gateRowSpacing()
                        + (1.0 - position * position) * 1.5;
                double depth = fwd - row * 0.65;

                the_four_primitives_and_weapons.entity.GateProjectileEntity projectile =
                        new the_four_primitives_and_weapons.entity.GateProjectileEntity(level, player);

                double spawnX = eyePos.x + rightX * lateral + forwardX * depth;
                double spawnY = eyePos.y + height;
                double spawnZ = eyePos.z + rightZ * lateral + forwardZ * depth;
                projectile.setPos(spawnX, spawnY, spawnZ);

                projectile.prepareLaunch(lookVec.scale(speed),
                        GateFormula.gateWarmupTicks() + i * GateFormula.gateLaunchInterval());

                level.addFreshEntity(projectile);
            }

            // 展開音 (各剣の射出音は飛翔体側で鳴らす)
            int soundReps = GateFormula.gateSoundReps();
            for (int i = 0; i < soundReps; i++) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ILLUSIONER_PREPARE_MIRROR, SoundSource.PLAYERS, 2.0f, 1.0f);
            }

            // 耐性付与 (反動防止)
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE,
                    GateFormula.gateResistDur() + GateFormula.gateWarmupTicks()
                            + (count - 1) * GateFormula.gateLaunchInterval(),
                    GateFormula.gateResistAmp(), true, false));

            player.getCooldowns().addCooldown(this, GateFormula.gateCooldown());
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§6右クリック: " + GateFormula.gateProjectileCount() + "本の剣を展開して連続射出"));
        tooltip.add(Component.literal("§7Knockback X / Unbreakable"));
    }

    public static boolean isGateSword(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof GateItem;
    }
}
