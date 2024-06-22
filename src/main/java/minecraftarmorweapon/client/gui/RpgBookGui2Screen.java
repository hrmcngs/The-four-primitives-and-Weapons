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

import minecraftarmorweapon.world.inventory.RpgBookGui2Menu;

import minecraftarmorweapon.network.RpgBookGui2ButtonMessage;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class RpgBookGui2Screen extends AbstractContainerScreen<RpgBookGui2Menu> {
	private final static HashMap<String, Object> guistate = RpgBookGui2Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_ninja;
	Button button_vampire;
	ImageButton imagebutton_texture1;
	ImageButton imagebutton_next;
	ImageButton imagebutton_back;

	public RpgBookGui2Screen(RpgBookGui2Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 170;
		this.imageHeight = 195;
	}

	private static final ResourceLocation texture = new ResourceLocation("minecraft_armor_weapon:textures/screens/rpg_book_gui_2.png");

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
		button_ninja = new Button(this.leftPos + 102, this.topPos + 60, 51, 20, Component.translatable("gui.minecraft_armor_weapon.rpg_book_gui_2.button_ninja"), e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGui2ButtonMessage(0, x, y, z));
				RpgBookGui2ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_ninja", button_ninja);
		this.addRenderableWidget(button_ninja);
		button_vampire = new Button(this.leftPos + 92, this.topPos + 90, 61, 20, Component.translatable("gui.minecraft_armor_weapon.rpg_book_gui_2.button_vampire"), e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGui2ButtonMessage(1, x, y, z));
				RpgBookGui2ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_vampire", button_vampire);
		this.addRenderableWidget(button_vampire);
		imagebutton_texture1 = new ImageButton(this.leftPos + 7, this.topPos + 8, 16, 16, 0, 0, 16, new ResourceLocation("minecraft_armor_weapon:textures/screens/atlas/imagebutton_texture1.png"), 16, 32, e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGui2ButtonMessage(2, x, y, z));
				RpgBookGui2ButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:imagebutton_texture1", imagebutton_texture1);
		this.addRenderableWidget(imagebutton_texture1);
		imagebutton_next = new ImageButton(this.leftPos + 137, this.topPos + 8, 16, 16, 0, 0, 16, new ResourceLocation("minecraft_armor_weapon:textures/screens/atlas/imagebutton_next.png"), 16, 32, e -> {
		});
		guistate.put("button:imagebutton_next", imagebutton_next);
		this.addRenderableWidget(imagebutton_next);
		imagebutton_back = new ImageButton(this.leftPos + 37, this.topPos + 8, 16, 16, 0, 0, 16, new ResourceLocation("minecraft_armor_weapon:textures/screens/atlas/imagebutton_back.png"), 16, 32, e -> {
			if (true) {
				MinecraftArmorWeaponMod.PACKET_HANDLER.sendToServer(new RpgBookGui2ButtonMessage(4, x, y, z));
				RpgBookGui2ButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		});
		guistate.put("button:imagebutton_back", imagebutton_back);
		this.addRenderableWidget(imagebutton_back);
	}
}
