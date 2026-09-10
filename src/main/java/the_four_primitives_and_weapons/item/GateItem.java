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
 * ドロップ操作で3本を召喚し、待機中に視線の50ブロック先を狙って射出。
 * MODの既存操作として右クリックでも同じ召喚を行う。
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
            Vec3 origin = player.position();

            // プレイヤーの前方・右方向ベクトル（水平面）
            double yawRad = Math.toRadians(player.getYRot());
            double forwardX = -Math.sin(yawRad);
            double forwardZ = Math.cos(yawRad);
            double rightX = -Math.cos(yawRad);
            double rightZ = -Math.sin(yawRad);

            // poof.mcfunction:54-56 の ^3.5 ^0.3 ^-2 / ^0 ^0.6 ^-2 / ^-3.5 ^0.7 ^-2。
            // MODではドロップアイテムの代わりに使用者の位置と水平向きを基準にする。
            int count = GateFormula.gateProjectileCount();
            double side = GateFormula.gateSideSpread();
            double depth = GateFormula.gateForwardOffset();
            double speed = GateFormula.gateShootVelocity();
            double[] heights = {0.3, 0.6, 0.7};
            for (int i = 0; i < count; i++) {
                double lateral = count <= 1 ? 0 : side - 2.0 * side * i / (count - 1);
                double height = heights[i % heights.length] + GateFormula.gateVerticalOffset();

                the_four_primitives_and_weapons.entity.GateProjectileEntity projectile =
                        new the_four_primitives_and_weapons.entity.GateProjectileEntity(level, player);

                double spawnX = origin.x + -rightX * lateral + forwardX * depth;
                // 不可視防具立ての原点から、表示される剣の高さへ補正。
                double spawnY = origin.y + height + 1.0;
                double spawnZ = origin.z + -rightZ * lateral + forwardZ * depth;
                projectile.setPos(spawnX, spawnY, spawnZ);

                projectile.prepareGateLaunch(lookVec.scale(speed), GateFormula.gateWarmupTicks());

                level.addFreshEntity(projectile);
            }

            // 展開音 (各剣の射出音は飛翔体側で鳴らす)
            int soundReps = 8; // poof.mcfunction:59-66
            for (int i = 0; i < soundReps; i++) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ILLUSIONER_PREPARE_MIRROR, SoundSource.PLAYERS, 10.0f, 1.5f);
            }

            // 耐性付与 (反動防止)
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE,
                    40, 100, true, false)); // poof.mcfunction:57


            player.getCooldowns().addCooldown(this, GateFormula.gateCooldown());
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§6右クリック: " + GateFormula.gateProjectileCount() + "本の剣を召喚"));
        tooltip.add(Component.literal("§6ドロップキー: 同じ召喚 / Shift+ドロップ: 手放す"));
        tooltip.add(Component.literal("§7Knockback X / Unbreakable"));
    }

    public static boolean isGateSword(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof GateItem;
    }
}
