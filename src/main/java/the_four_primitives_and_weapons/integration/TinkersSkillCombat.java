package the_four_primitives_and_weapons.integration;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.DoublePredicate;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.util.DamageCalculator;

/** Optional bridge to the native Tinkers modifier list, including TiCEX addon modifiers. */
public final class TinkersSkillCombat {
    private static Api api;
    private static boolean checked;
    private static boolean warned;
    private TinkersSkillCombat() {}

    private static Api api() {
        if (!checked) {
            checked = true;
            if (ModList.get().isLoaded("tconstruct")) try { api = new Api(); }
            catch (ReflectiveOperationException | LinkageError error) { warn(error); }
        }
        return api;
    }
    private static void warn(Throwable error) {
        if (!warned) {
            warned = true;
            TheFourPrimitivesAndWeaponsMod.LOGGER.warn("Tinkers skill modifier bridge could not invoke the installed API", error);
        }
    }

    public static boolean hurt(LivingEntity attacker, LivingEntity target, ItemStack weapon,
            InteractionHand hand, float base, DoublePredicate hit) {
        if (attacker.level().isClientSide || SkillModifierPipeline.active()) return hit.test(base);
        Api a = api();
        if (a == null || !a.modifiable.isInstance(weapon.getItem())) return hit.test(base);
        boolean[] attempted = {false}, accepted = {false};
        try {
            Object tool = a.from.invoke(null, weapon);
            if ((boolean)a.broken.invoke(tool)) return false;
            Float scale = DamageCalculator.getCooldownScaleContext();
            float attackCooldown = scale != null ? scale
                : attacker instanceof Player player ? player.getAttackStrengthScale(0.5f) : 1;
            Object builder = a.attacker.invoke(null, attacker, attacker instanceof Player p ? p : null);
            a.target.invoke(builder, target);
            a.hand.invoke(builder, hand);
            a.baseDamage.invoke(builder, base);
            a.cooldown.invoke(builder, Math.max(0, Math.min(1, attackCooldown)));
            Object context = a.build.invoke(builder);
            List<?> entries = (List<?>)a.modifiers.invoke(tool);
            float health = target.getHealth();
            float[] knockback = {0};
            Object previousLootingSlot = a.getLootingSlot.invoke(null, attacker);
            try {
                a.setLootingSlot.invoke(null, attacker, hand == InteractionHand.MAIN_HAND
                    ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
                return SkillModifierPipeline.hit(base, new SkillModifierPipeline.Hooks() {
                public float damage(float value) throws ReflectiveOperationException {
                    for (Object entry : entries) value = ((Number)a.damage.invoke(a.hook.invoke(entry, a.damageHook), tool, entry, context, base, value)).floatValue();
                    return value;
                }
                public void before(float value) throws ReflectiveOperationException {
                    for (Object entry : entries) knockback[0] = ((Number)a.before.invoke(a.hook.invoke(entry, a.hitHook), tool, entry, context, value, 0f, knockback[0])).floatValue();
                }
                public void after(float value) throws ReflectiveOperationException {
                    float dealt = Math.max(0, health - target.getHealth());
                    if (Float.isFinite(knockback[0]) && knockback[0] > 0)
                        target.knockback(knockback[0], attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
                    for (Object entry : entries) a.after.invoke(a.hook.invoke(entry, a.hitHook), tool, entry, context, dealt);
                    // Skills bypass Item.hurtEnemy, so apply native tool durability hooks here.
                    a.damageTool.invoke(null, tool, 1, attacker, hand);
                }
                public void failed(float value) throws ReflectiveOperationException {
                    for (Object entry : entries) a.failed.invoke(a.hook.invoke(entry, a.hitHook), tool, entry, context, value);
                }
            }, amount -> {
                attempted[0] = true;
                accepted[0] = hit.test(amount);
                return accepted[0];
                });
            } finally { a.setLootingSlot.invoke(null, attacker, previousLootingSlot); }
        } catch (ReflectiveOperationException | LinkageError error) {
            warn(error);
            // Never deal the base hit again if a modifier's post-hit hook failed.
            return attempted[0] ? accepted[0] : hit.test(base);
        }
    }

    private static final class Api {
        final Class<?> modifiable;
        final Method from, broken, modifiers, hook, attacker, target, hand, baseDamage, cooldown, build;
        final Method damage, before, after, failed, damageTool;
        final Method getLootingSlot, setLootingSlot;
        final Object damageHook, hitHook;
        Api() throws ReflectiveOperationException {
            modifiable = Class.forName("slimeknights.tconstruct.library.tools.item.IModifiable");
            Class<?> view = Class.forName("slimeknights.tconstruct.library.tools.nbt.IToolStackView");
            Class<?> stack = Class.forName("slimeknights.tconstruct.library.tools.nbt.ToolStack");
            Class<?> entry = Class.forName("slimeknights.tconstruct.library.modifiers.ModifierEntry");
            Class<?> module = Class.forName("slimeknights.tconstruct.library.module.ModuleHook");
            Class<?> context = Class.forName("slimeknights.tconstruct.library.tools.context.ToolAttackContext");
            Class<?> builder = Class.forName("slimeknights.tconstruct.library.tools.context.ToolAttackContext$Builder");
            Class<?> hooks = Class.forName("slimeknights.tconstruct.library.modifiers.ModifierHooks");
            Class<?> damageApi = Class.forName("slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook");
            Class<?> hitApi = Class.forName("slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook");
            from = stack.getMethod("from", ItemStack.class);
            broken = view.getMethod("isBroken"); modifiers = view.getMethod("getModifierList");
            hook = entry.getMethod("getHook", module);
            attacker = context.getMethod("attacker", LivingEntity.class, Player.class);
            target = builder.getMethod("target", LivingEntity.class);
            hand = builder.getMethod("hand", InteractionHand.class);
            baseDamage = builder.getMethod("baseDamage", float.class);
            cooldown = builder.getMethod("cooldown", float.class); build = builder.getMethod("build");
            damageHook = hooks.getField("MELEE_DAMAGE").get(null); hitHook = hooks.getField("MELEE_HIT").get(null);
            damage = damageApi.getMethod("getMeleeDamage", view, entry, context, float.class, float.class);
            before = hitApi.getMethod("beforeMeleeHit", view, entry, context, float.class, float.class, float.class);
            after = hitApi.getMethod("afterMeleeHit", view, entry, context, float.class);
            failed = hitApi.getMethod("failedMeleeHit", view, entry, context, float.class);
            damageTool = Class.forName("slimeknights.tconstruct.library.tools.helper.ToolDamageUtil")
                .getMethod("damageAnimated", view, int.class, LivingEntity.class, InteractionHand.class);
            Class<?> looting = Class.forName("slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler");
            getLootingSlot = looting.getMethod("getLootingSlot", LivingEntity.class);
            setLootingSlot = looting.getMethod("setLootingSlot", LivingEntity.class, net.minecraft.world.entity.EquipmentSlot.class);
        }
    }
}
