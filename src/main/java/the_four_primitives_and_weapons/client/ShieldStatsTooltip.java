package the_four_primitives_and_weapons.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.event.ShieldBashHandler;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, value = Dist.CLIENT)
public final class ShieldStatsTooltip {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!ShieldBashHandler.isShield(stack)) return;
        Component attackDamage = Component.translatable("tooltip.the_four_primitives_and_weapons.shield_attack_damage",
                ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(ShieldBashHandler.getDisplaySettings(stack).damage()))
                .withStyle(ChatFormatting.DARK_GREEN);
        // バニラの「攻撃力 +7」属性行を共通攻撃力の表示で置き換え、二重に追記しない。
        // 翻訳キーで識別し、名前や説明文に含まれる「攻撃力」は削除しない。
        boolean replaced = false;
        var lines = event.getToolTip().listIterator();
        while (lines.hasNext()) {
            Component line = lines.next();
            if (hasTranslation(line, key -> key.startsWith("attribute.modifier."))
                    && hasTranslation(line, key -> key.equals("attribute.name.generic.attack_damage"))) {
                if (replaced) lines.remove();
                else {
                    lines.set(attackDamage);
                    replaced = true;
                }
            }
        }
        if (!replaced) event.getToolTip().add(attackDamage);
        event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.shield_bash_damage",
                ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(ShieldBashHandler.getDisplaySettings(stack).damage() * 0.25f),
                ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(ShieldBashHandler.getDisplaySettings(stack).damage()))
                .withStyle(ChatFormatting.DARK_GREEN));
        event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.shield_parry_damage",
                ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(ShieldBashHandler.getDisplaySettings(stack).damage()))
                .withStyle(ChatFormatting.GRAY));
        event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.shield_bash_cooldown",
                ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(ShieldBashHandler.getDisplaySettings(stack).cooldown() / 20.0f))
                .withStyle(ChatFormatting.DARK_GREEN));
        if (the_four_primitives_and_weapons.item.GreatshieldItem.isGreatshield(stack)) {
            event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.greatshield_place")
                    .withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.greatshield_weight")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.shield_dash_bash")
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    private static boolean hasTranslation(Component component, java.util.function.Predicate<String> matches) {
        if (component.getContents() instanceof TranslatableContents translated) {
            if (matches.test(translated.getKey())) return true;
            for (Object argument : translated.getArgs()) {
                if (argument instanceof Component child && hasTranslation(child, matches)) return true;
            }
        }
        for (Component sibling : component.getSiblings()) {
            if (hasTranslation(sibling, matches)) return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onLogout(net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) {
        ShieldBashHandler.CLIENT_SETTINGS.clear();
    }
}
