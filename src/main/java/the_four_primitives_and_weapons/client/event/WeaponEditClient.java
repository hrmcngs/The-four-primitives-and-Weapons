package the_four_primitives_and_weapons.client.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import the_four_primitives_and_weapons.client.screens.RackEditScreen;
import the_four_primitives_and_weapons.client.screens.StabEditScreen;
import the_four_primitives_and_weapons.entity.StabbedWeaponEntity;
import the_four_primitives_and_weapons.entity.WeaponRackEntity;

/** 共通の編集コマンドから、対象に対応した微調整画面を開く。 */
@OnlyIn(Dist.CLIENT)
public final class WeaponEditClient {
    private WeaponEditClient() {}

    public static void open(int entityId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        var target = mc.level.getEntity(entityId);
        if (target instanceof WeaponRackEntity) {
            mc.setScreen(new RackEditScreen(entityId));
        } else if (target instanceof StabbedWeaponEntity) {
            StabEditScreen.open(entityId);
        }
    }
}
