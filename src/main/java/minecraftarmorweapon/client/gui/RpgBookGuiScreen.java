package minecraftarmorweapon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import minecraftarmorweapon.world.inventory.RpgBookGuiMenu;

import minecraftarmorweapon.network.RpgBookGuiButtonMessage;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class RpgBookGuiScreen extends AbstractContainerScreen<RpgBookGuiMenu> {
	private final static HashMap<String, Object> guistate = RpgBookGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_bogged_outer;
	Button button_magic_swordsman;
	Button button_ninja;
	Button button_vampire;
	ImageButton imagebutton_tapmimit;
	ImageButton imagebutton_texture1;
	ImageButton imagebutton_next;

	public RpgBookGuiScreen(RpgBookGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 170;
		this.imageHeight = 195;
	}

	private static final ResourceLocation texture = new ResourceLocation("minecraft_armor_weapon:textures/screens/rpg_book_gui.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, texture);
		this.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void init() {
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		button_bogged_outer = new Button(this.leftPos + 67, this.topPos + 120, 87, 20, Component.translatable("gui.minecraft_armor_weapon.rpg_book_gui.button_bogged_outer"), e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGuiButtonMessage(0, x, y, z));
				RpgBookGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_bogged_outer", button_bogged_outer);
		this.addRenderableWidget(button_bogged_outer);
		button_magic_swordsman = new Button(this.leftPos + 52, this.topPos + 150, 103, 20, Component.translatable("gui.minecraft_armor_weapon.rpg_book_gui.button_magic_swordsman"), e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGuiButtonMessage(1, x, y, z));
				RpgBookGuiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_magic_swordsman", button_magic_swordsman);
		this.addRenderableWidget(button_magic_swordsman);
		button_ninja = new Button(this.leftPos + 102, this.topPos + 60, 51, 20, Component.translatable("gui.minecraft_armor_weapon.rpg_book_gui.button_ninja"), e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGuiButtonMessage(2, x, y, z));
				RpgBookGuiButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:button_ninja", button_ninja);
		this.addRenderableWidget(button_ninja);
		button_vampire = new Button(this.leftPos + 92, this.topPos + 90, 61, 20, Component.translatable("gui.minecraft_armor_weapon.rpg_book_gui.button_vampire"), e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGuiButtonMessage(3, x, y, z));
				RpgBookGuiButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		});
		guistate.put("button:button_vampire", button_vampire);
		this.addRenderableWidget(button_vampire);
		imagebutton_tapmimit = new ImageButton(this.leftPos + 7, this.topPos + 165, 16, 16, 0, 0, 16, new ResourceLocation("minecraft_armor_weapon:textures/screens/atlas/imagebutton_tapmimit.png"), 16, 32, e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGuiButtonMessage(4, x, y, z));
				RpgBookGuiButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		});
		guistate.put("button:imagebutton_tapmimit", imagebutton_tapmimit);
		this.addRenderableWidget(imagebutton_tapmimit);
		imagebutton_texture1 = new ImageButton(this.leftPos + 7, this.topPos + 8, 16, 16, 0, 0, 16, new ResourceLocation("minecraft_armor_weapon:textures/screens/atlas/imagebutton_texture1.png"), 16, 32, e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGuiButtonMessage(5, x, y, z));
				RpgBookGuiButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		});
		guistate.put("button:imagebutton_texture1", imagebutton_texture1);
		this.addRenderableWidget(imagebutton_texture1);
		imagebutton_next = new ImageButton(this.leftPos + 137, this.topPos + 8, 16, 16, 0, 0, 16, new ResourceLocation("minecraft_armor_weapon:textures/screens/atlas/imagebutton_next.png"), 16, 32, e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGuiButtonMessage(6, x, y, z));
				RpgBookGuiButtonMessage.handleButtonAction(entity, 6, x, y, z);
			}
		});
		guistate.put("button:imagebutton_next", imagebutton_next);
		this.addRenderableWidget(imagebutton_next);
	}
}
