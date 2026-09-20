package the_four_primitives_and_weapons.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import the_four_primitives_and_weapons.events.MagicalKatanaCrystalHandler;

/**
 * /crystal destroy_mine
 *   サーバ上の全プレイヤーのインベントリを走査し、 自分の UUID が刻まれている具現化
 *   Magical Katana を全部破壊する。 ( = 「他人のスロットの自分の武器も破壊」 仕様 )
 */
@Mod.EventBusSubscriber
public class CrystalDestroyCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("crystal")
            .requires(s -> s.hasPermission(0)) // 全プレイヤー可
            .then(Commands.literal("destroy_mine")
                .executes(ctx -> destroyMine(ctx.getSource())))
            .then(Commands.literal("unlock")
                .executes(ctx -> unlockHand(ctx.getSource())))
            .then(Commands.literal("give_materialized")
                .executes(ctx -> giveMaterialized(ctx.getSource())))
        );
    }

    /**
     * test 用: 自分の UUID が刻まれた具現化 Magical Katana を 1 本生成。
     *   /crystal destroy_mine で破壊できる ( 自分の UUID 一致 )。
     */
    private static int giveMaterialized(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.1"));
            return 0;
        }
        net.minecraft.world.item.ItemStack stack =
                MagicalKatanaCrystalHandler.createMaterialized(player.getUUID());
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
        source.sendSuccess(() -> Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.2"), false);
        return 1;
    }

    private static int destroyMine(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.3"));
            return 0;
        }
        int n = MagicalKatanaCrystalHandler.destroyAllOwnedMaterialized(
                source.getServer(), player);
        final int total = n;
        source.sendSuccess(() -> Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.4", total), false);
        return n;
    }

    /**
     * test 用: 手に持っている Magical Katana を解放状態にする ( 特殊技を出せるように )。
     * メイン → オフの順に探す。 既に具現化版なら何もしない ( 既に unlocked )。
     */
    private static int unlockHand(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.5"));
            return 0;
        }
        net.minecraft.world.item.ItemStack main = player.getMainHandItem();
        net.minecraft.world.item.ItemStack off  = player.getOffhandItem();
        net.minecraft.world.item.ItemStack target;
        if (MagicalKatanaCrystalHandler.isMagicalKatana(main)) {
            target = main;
        } else if (MagicalKatanaCrystalHandler.isMagicalKatana(off)) {
            target = off;
        } else {
            source.sendFailure(Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.6"));
            return 0;
        }
        if (MagicalKatanaCrystalHandler.isUnlocked(target)) {
            source.sendSuccess(() -> Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.7"), false);
            return 1;
        }
        MagicalKatanaCrystalHandler.setUnlocked(target);
        // NBT のみ書き換えだと client 側 inventory に同期されない場合があるので
        // 明示的に container sync を発火させる
        player.inventoryMenu.broadcastChanges();
        source.sendSuccess(() -> Component.translatable("command.the_four_primitives_and_weapons.crystaldestroycommand.8"), false);
        return 1;
    }
}
