package the_four_primitives_and_weapons.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TrueCrafterInfoCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("difficultyinfo")
            .requires(source -> source.hasPermission(0))
            .executes(context -> {
                CustomDifficultyCommand.CustomDifficulty diff = CustomDifficultyCommand.getCurrentDifficulty();
                String c = diff.getColorCode();

                // ヘッダー
                context.getSource().sendSuccess(() ->
                    Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.1", c, diff.getName().toUpperCase()), false);

                // 基本情報
                context.getSource().sendSuccess(() ->
                    Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.2", String.format(java.util.Locale.ROOT, "%.1f", diff.getDamageMultiplier()), String.format(java.util.Locale.ROOT, "%.2f", diff.getHealthMultiplier()), String.format(java.util.Locale.ROOT, "%d", diff.getAiLevel())), false);

                // 装備品質
                Component eqLabel;
                switch (diff.getEquipmentTier()) {
                    case 0: eqLabel = Component.translatable("command.the_four_primitives_and_weapons.label.equipment.none"); break;
                    case 1: eqLabel = Component.translatable("command.the_four_primitives_and_weapons.label.equipment.wood"); break;
                    case 2: eqLabel = Component.translatable("command.the_four_primitives_and_weapons.label.equipment.iron"); break;
                    case 3: eqLabel = Component.translatable("command.the_four_primitives_and_weapons.label.equipment.diamond"); break;
                    case 4: eqLabel = Component.translatable("command.the_four_primitives_and_weapons.label.equipment.netherite"); break;
                    default: eqLabel = Component.translatable("command.the_four_primitives_and_weapons.label.equipment.unknown"); break;
                }
                final Component equipLabel = eqLabel;
                context.getSource().sendSuccess(() ->
                    Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.3", equipLabel, String.format("%.0f%%", diff.getShieldChance() * 100)), false);

                // 確率パラメータ
                if (diff.getAiLevel() > 0) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.4", String.format(java.util.Locale.ROOT, "%.0f", diff.getEliteSpawnChance() * 100), String.format(java.util.Locale.ROOT, "%.0f", diff.getReinforceChance() * 100), String.format(java.util.Locale.ROOT, "%.0f", diff.getBuffEffectChance() * 100)), false);
                }

                // 有効機能リスト
                context.getSource().sendSuccess(() ->
                    Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.5"), false);

                if (diff.getAiLevel() >= 1) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.6", getSpeedLabel(diff.getAiLevel())), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.7", getSpeedLabel(diff.getAiLevel())), false);
                }
                if (diff.getShieldChance() > 0) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.8", String.format("%.0f%%", diff.getShieldChance() * 100)), false);
                }
                if (diff.getEliteSpawnChance() > 0) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.9", String.format("%.0f%%", diff.getEliteSpawnChance() * 100)), false);
                }
                if (diff.getReinforceChance() > 0) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.10", String.format("%.0f%%", diff.getReinforceChance() * 100)), false);
                }
                if (diff.getBuffEffectChance() > 0) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.11", String.format("%.0f%%", diff.getBuffEffectChance() * 100)), false);
                }
                if (diff.isBlockPlaceEnabled()) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.12"), false);
                }
                if (diff.isBlockBreakEnabled()) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.13"), false);
                }
                if (diff.isWallSenseEnabled()) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.14"), false);
                }
                if (diff.isFallDmgImmune()) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.15"), false);
                }
                if (!diff.isBedSleepEnabled()) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.16"), false);
                }
                if (diff.getHealthMultiplier() > 1.0f) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.17", String.format("%.0f%%", diff.getHealthMultiplier() * 100)), false);
                }

                // 特性システム
                if (diff.getTraitChance() > 0) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.18"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.19", String.format("%.0f%%", diff.getTraitChance() * 100)), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.20"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.21"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.22"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.23"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.24"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.25"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.26"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.27"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.28"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.29"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.30"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.31"), false);
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.32"), false);
                }

                // True Crafterモード判定
                if (CustomDifficultyCommand.isTrueCrafterEnabled()) {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.33", c, diff.getAiLevel()), false);
                } else {
                    context.getSource().sendSuccess(() ->
                        Component.translatable("command.the_four_primitives_and_weapons.truecrafterinfocommand.34"), false);
                }

                return 1;
            })
        );
    }

    private static Component getSpeedLabel(int aiLevel) {
        switch (aiLevel) {
            case 1: return Component.translatable("command.the_four_primitives_and_weapons.label.speed.1");
            case 2: return Component.translatable("command.the_four_primitives_and_weapons.label.speed.2");
            case 3: return Component.translatable("command.the_four_primitives_and_weapons.label.speed.3");
            case 4: return Component.translatable("command.the_four_primitives_and_weapons.label.speed.4");
            case 5: return Component.translatable("command.the_four_primitives_and_weapons.label.speed.5");
            default: return Component.empty();
        }
    }
}
