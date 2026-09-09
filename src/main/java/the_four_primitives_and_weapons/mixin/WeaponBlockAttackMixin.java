package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_four_primitives_and_weapons.events.ChargedAttackHandler;

/** 武器の長押しはチャージ入力に任せ、採掘の繰り返し攻撃イベントを発生させない。 */
@Mixin(Minecraft.class)
public abstract class WeaponBlockAttackMixin {
    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void maw_stopWeaponMining(boolean held, CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (!held || mc.player == null || mc.gameMode == null || mc.hitResult == null) return;
        if (mc.hitResult.getType() != HitResult.Type.BLOCK
                || !ChargedAttackHandler.isWeapon(mc.player.getMainHandItem())) return;
        // ツールで採掘中に武器へ持ち替えた場合も、途中の採掘状態を解消する。
        mc.gameMode.stopDestroyBlock();
        ci.cancel();
    }
}
