
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import minecraftarmorweapon.potion.ZokuseizanngekiMobEffect;
import minecraftarmorweapon.potion.ZanngekitokubetuMobEffect;
import minecraftarmorweapon.potion.ZanngekikaiMobEffect;
import minecraftarmorweapon.potion.YouwakaranMobEffect;
import minecraftarmorweapon.potion.WindStepEffectMobEffect;
import minecraftarmorweapon.potion.WazaMobEffect;
import minecraftarmorweapon.potion.TunderbolteffrctMobEffect;
import minecraftarmorweapon.potion.TpAttackMobEffect;
import minecraftarmorweapon.potion.TokubetusounazangekiMobEffect;
import minecraftarmorweapon.potion.TobeMobEffect;
import minecraftarmorweapon.potion.TissokuMobEffect;
import minecraftarmorweapon.potion.ThunderHitMobEffect;
import minecraftarmorweapon.potion.SyugekinanozeeMobEffect;
import minecraftarmorweapon.potion.SyugekiMobEffect;
import minecraftarmorweapon.potion.SwordOfNightEffectMobEffect;
import minecraftarmorweapon.potion.SuityuuMobEffect;
import minecraftarmorweapon.potion.StormEffectMobEffect;
import minecraftarmorweapon.potion.OtitaMobEffect;
import minecraftarmorweapon.potion.OtiruyooMobEffect;
import minecraftarmorweapon.potion.OtiroMobEffect;
import minecraftarmorweapon.potion.NgsMobEffect;
import minecraftarmorweapon.potion.NagiharaiMobEffect;
import minecraftarmorweapon.potion.Nagiharai2MobEffect;
import minecraftarmorweapon.potion.LongRangeWeaponCutMobEffect;
import minecraftarmorweapon.potion.KurutimenasiMobEffect;
import minecraftarmorweapon.potion.KougekiMobEffect;
import minecraftarmorweapon.potion.KitterukitteruMobEffect;
import minecraftarmorweapon.potion.KarikarisiterunemaidomaidoMobEffect;
import minecraftarmorweapon.potion.KaminariyadeMobEffect;
import minecraftarmorweapon.potion.KaitenMobEffect;
import minecraftarmorweapon.potion.GyetonzangekiMobEffect;
import minecraftarmorweapon.potion.FireballeffectMobEffect;
import minecraftarmorweapon.potion.EffectMagicMobEffect;
import minecraftarmorweapon.potion.EffectBloodTpMobEffect;
import minecraftarmorweapon.potion.DevourBloodMobEffect;
import minecraftarmorweapon.potion.BubbleshotEffectMobEffect;
import minecraftarmorweapon.potion.BowAttackMobEffect;
import minecraftarmorweapon.potion.BloodTpMobEffect;
import minecraftarmorweapon.potion.BarrierEffectMobEffect;
import minecraftarmorweapon.potion.AttackBowMobEffect;
import minecraftarmorweapon.potion.Arrow1MobEffect;
import minecraftarmorweapon.potion.AaaaMobEffect;
import minecraftarmorweapon.potion.AaMobEffect;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<MobEffect> FIREBALLEFFECT = REGISTRY.register("fireballeffect", () -> new FireballeffectMobEffect());
	public static final RegistryObject<MobEffect> ARROW_1 = REGISTRY.register("arrow_1", () -> new Arrow1MobEffect());
	public static final RegistryObject<MobEffect> TUNDERBOLTEFFRCT = REGISTRY.register("tunderbolteffrct", () -> new TunderbolteffrctMobEffect());
	public static final RegistryObject<MobEffect> BOW_ATTACK = REGISTRY.register("bow_attack", () -> new BowAttackMobEffect());
	public static final RegistryObject<MobEffect> ATTACK_BOW = REGISTRY.register("attack_bow", () -> new AttackBowMobEffect());
	public static final RegistryObject<MobEffect> OTIRUYOO = REGISTRY.register("otiruyoo", () -> new OtiruyooMobEffect());
	public static final RegistryObject<MobEffect> AAAA = REGISTRY.register("aaaa", () -> new AaaaMobEffect());
	public static final RegistryObject<MobEffect> SYUGEKI = REGISTRY.register("syugeki", () -> new SyugekiMobEffect());
	public static final RegistryObject<MobEffect> SUITYUU = REGISTRY.register("suityuu", () -> new SuityuuMobEffect());
	public static final RegistryObject<MobEffect> TISSOKU = REGISTRY.register("tissoku", () -> new TissokuMobEffect());
	public static final RegistryObject<MobEffect> BUBBLESHOT_EFFECT = REGISTRY.register("bubbleshot_effect", () -> new BubbleshotEffectMobEffect());
	public static final RegistryObject<MobEffect> THUNDER_HIT = REGISTRY.register("thunder_hit", () -> new ThunderHitMobEffect());
	public static final RegistryObject<MobEffect> NAGIHARAI = REGISTRY.register("nagiharai", () -> new NagiharaiMobEffect());
	public static final RegistryObject<MobEffect> KURUTIMENASI = REGISTRY.register("kurutimenasi", () -> new KurutimenasiMobEffect());
	public static final RegistryObject<MobEffect> WAZA = REGISTRY.register("waza", () -> new WazaMobEffect());
	public static final RegistryObject<MobEffect> EFFECT_MAGIC = REGISTRY.register("effect_magic", () -> new EffectMagicMobEffect());
	public static final RegistryObject<MobEffect> BARRIER_EFFECT = REGISTRY.register("barrier_effect", () -> new BarrierEffectMobEffect());
	public static final RegistryObject<MobEffect> AA = REGISTRY.register("aa", () -> new AaMobEffect());
	public static final RegistryObject<MobEffect> OTIRO = REGISTRY.register("otiro", () -> new OtiroMobEffect());
	public static final RegistryObject<MobEffect> WIND_STEP_EFFECT = REGISTRY.register("wind_step_effect", () -> new WindStepEffectMobEffect());
	public static final RegistryObject<MobEffect> KAITEN = REGISTRY.register("kaiten", () -> new KaitenMobEffect());
	public static final RegistryObject<MobEffect> OTITA = REGISTRY.register("otita", () -> new OtitaMobEffect());
	public static final RegistryObject<MobEffect> ZANNGEKITOKUBETU = REGISTRY.register("zanngekitokubetu", () -> new ZanngekitokubetuMobEffect());
	public static final RegistryObject<MobEffect> YOUWAKARAN = REGISTRY.register("youwakaran", () -> new YouwakaranMobEffect());
	public static final RegistryObject<MobEffect> GYETONZANGEKI = REGISTRY.register("gyetonzangeki", () -> new GyetonzangekiMobEffect());
	public static final RegistryObject<MobEffect> TOBE = REGISTRY.register("tobe", () -> new TobeMobEffect());
	public static final RegistryObject<MobEffect> KOUGEKI = REGISTRY.register("kougeki", () -> new KougekiMobEffect());
	public static final RegistryObject<MobEffect> ZANNGEKIKAI = REGISTRY.register("zanngekikai", () -> new ZanngekikaiMobEffect());
	public static final RegistryObject<MobEffect> SYUGEKINANOZEE = REGISTRY.register("syugekinanozee", () -> new SyugekinanozeeMobEffect());
	public static final RegistryObject<MobEffect> NAGIHARAI_2 = REGISTRY.register("nagiharai_2", () -> new Nagiharai2MobEffect());
	public static final RegistryObject<MobEffect> KAMINARIYADE = REGISTRY.register("kaminariyade", () -> new KaminariyadeMobEffect());
	public static final RegistryObject<MobEffect> TOKUBETUSOUNAZANGEKI = REGISTRY.register("tokubetusounazangeki", () -> new TokubetusounazangekiMobEffect());
	public static final RegistryObject<MobEffect> KARIKARISITERUNEMAIDOMAIDO = REGISTRY.register("karikarisiterunemaidomaido", () -> new KarikarisiterunemaidomaidoMobEffect());
	public static final RegistryObject<MobEffect> DEVOUR_BLOOD = REGISTRY.register("devour_blood", () -> new DevourBloodMobEffect());
	public static final RegistryObject<MobEffect> EFFECT_BLOOD_TP = REGISTRY.register("effect_blood_tp", () -> new EffectBloodTpMobEffect());
	public static final RegistryObject<MobEffect> BLOOD_TP = REGISTRY.register("blood_tp", () -> new BloodTpMobEffect());
	public static final RegistryObject<MobEffect> NGS = REGISTRY.register("ngs", () -> new NgsMobEffect());
	public static final RegistryObject<MobEffect> SWORD_OF_NIGHT_EFFECT = REGISTRY.register("sword_of_night_effect", () -> new SwordOfNightEffectMobEffect());
	public static final RegistryObject<MobEffect> LONG_RANGE_WEAPON_CUT = REGISTRY.register("long_range_weapon_cut", () -> new LongRangeWeaponCutMobEffect());
	public static final RegistryObject<MobEffect> STORM_EFFECT = REGISTRY.register("storm_effect", () -> new StormEffectMobEffect());
	public static final RegistryObject<MobEffect> KITTERUKITTERU = REGISTRY.register("kitterukitteru", () -> new KitterukitteruMobEffect());
	public static final RegistryObject<MobEffect> TP_ATTACK = REGISTRY.register("tp_attack", () -> new TpAttackMobEffect());
	public static final RegistryObject<MobEffect> ZOKUSEIZANNGEKI = REGISTRY.register("zokuseizanngeki", () -> new ZokuseizanngekiMobEffect());
}
