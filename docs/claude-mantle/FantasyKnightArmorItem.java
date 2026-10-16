package the_four_primitives_and_weapons.item;

import net.minecraft.world.item.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import the_four_primitives_and_weapons.client.model.FantasyKnightArmorModel;
import java.util.function.Consumer;

/** Diamond-tier plate armor; mantle abilities remain available with mixed equipment. */
public class FantasyKnightArmorItem extends ArmorItem {
    public FantasyKnightArmorItem(Type type) { super(ArmorMaterials.DIAMOND, type, new Item.Properties()); }
    @Override public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "the_four_primitives_and_weapons:textures/models/armor/fantasy_knight.png";
    }
    @Override public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private FantasyKnightArmorModel model;
            @Override @SuppressWarnings("unchecked") public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack,
                    EquipmentSlot slot, HumanoidModel<?> original) {
                if (model == null) model = new FantasyKnightArmorModel();
                ((HumanoidModel<LivingEntity>) original).copyPropertiesTo(model);
                model.head.copyFrom(original.head);
                model.body.copyFrom(original.body);
                model.leftArm.copyFrom(original.leftArm);
                model.rightArm.copyFrom(original.rightArm);
                model.leftLeg.copyFrom(original.leftLeg);
                model.rightLeg.copyFrom(original.rightLeg);
                model.setAllVisible(false);
                switch (slot) {
                    case HEAD -> model.head.visible = true;
                    case CHEST -> { model.body.visible = true; model.leftArm.visible = true; model.rightArm.visible = true; }
                    case LEGS -> { model.leftLeg.visible = true; model.rightLeg.visible = true; }
                    case FEET -> { model.leftLeg.visible = true; model.rightLeg.visible = true; }
                    default -> { }
                }
                model.showLegParts(slot);
                return model;
            }
        });
    }
}
