
package minecraftarmorweapon.item;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public abstract class DasHerzEinerFeeArmorItem extends ArmorItem {
	public DasHerzEinerFeeArmorItem(EquipmentSlot slot, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForSlot(EquipmentSlot slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * 0;
			}

			@Override
			public int getDefenseForSlot(EquipmentSlot slot) {
				return new int[]{3, 6, 8, 3}[slot.getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 9;
			}

			@Override
			public SoundEvent getEquipSound() {
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.armor.equip_netherite"));
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}

			@Override
			public String getName() {
				return "das_herz_einer_fee_armor";
			}

			@Override
			public float getToughness() {
				return 3f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0.1f;
			}
		}, slot, properties);
	}

	public static class Helmet extends DasHerzEinerFeeArmorItem {
		public Helmet() {
			super(EquipmentSlot.HEAD, new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_ARMOR));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "minecraft_armor_weapon:textures/models/armor/nannka_layer_1.png";
		}
	}

	public static class Chestplate extends DasHerzEinerFeeArmorItem {
		public Chestplate() {
			super(EquipmentSlot.CHEST, new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_ARMOR));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "minecraft_armor_weapon:textures/models/armor/nannka_layer_1.png";
		}
	}

	public static class Leggings extends DasHerzEinerFeeArmorItem {
		public Leggings() {
			super(EquipmentSlot.LEGS, new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_ARMOR));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "minecraft_armor_weapon:textures/models/armor/nannka_layer_2.png";
		}
	}

	public static class Boots extends DasHerzEinerFeeArmorItem {
		public Boots() {
			super(EquipmentSlot.FEET, new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_ARMOR));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "minecraft_armor_weapon:textures/models/armor/nannka_layer_1.png";
		}
	}
}
