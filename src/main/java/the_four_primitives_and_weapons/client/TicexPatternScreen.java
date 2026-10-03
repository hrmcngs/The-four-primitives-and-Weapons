package the_four_primitives_and_weapons.client;

import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.item.TicexWeaponCatalog;
import the_four_primitives_and_weapons.network.TicexPatternMessage;

public final class TicexPatternScreen extends Screen {
    private final InteractionHand hand;
    private EditBox search;
    private final List<Button> choices=new ArrayList<>();
    private List<Item> filtered=List.of();
    private int page;
    private Button previous,next;
    private TicexPatternScreen(InteractionHand hand){super(Component.translatable("gui.the_four_primitives_and_weapons.ticex_pattern"));this.hand=hand;}
    public static void open(InteractionHand hand){Minecraft.getInstance().setScreen(new TicexPatternScreen(hand));}
    @Override protected void init() {
        choices.clear();
        int x=width/2-150;
        search=addRenderableWidget(new EditBox(font,x,32,300,20,Component.translatable("gui.the_four_primitives_and_weapons.ticex_pattern_search")));
        search.setHint(Component.translatable("gui.the_four_primitives_and_weapons.ticex_pattern_search"));
        search.setResponder(s->{page=0;update();});
        for(int i=0;i<6;i++) {
            final int slot=i;
            choices.add(addRenderableWidget(Button.builder(Component.empty(),b->select(slot))
                    .bounds(x,60+i*24,300,20).build()));
        }
        previous=addRenderableWidget(Button.builder(Component.literal("◀"),b->{page--;update();}).bounds(x,208,50,20).build());
        next=addRenderableWidget(Button.builder(Component.literal("▶"),b->{page++;update();}).bounds(x+250,208,50,20).build());
        update();setInitialFocus(search);
    }
    private void update() {
        String query=search.getValue().toLowerCase(Locale.ROOT).trim();
        filtered=TicexWeaponCatalog.all().stream().filter(i->TicexWeaponCatalog.label(i).getString().toLowerCase(Locale.ROOT).contains(query)
                ||ForgeRegistries.ITEMS.getKey(i).toString().contains(query)).toList();
        page=Math.max(0,Math.min(page,Math.max(0,(filtered.size()-1)/6)));
        for(int i=0;i<choices.size();i++) {
            int index=page*6+i;Button b=choices.get(i);b.visible=index<filtered.size();
            if(b.visible)b.setMessage(TicexWeaponCatalog.label(filtered.get(index)));
        }
        previous.active=page>0;next.active=(page+1)*6<filtered.size();
    }
    private void select(int slot) {
        int index=page*6+slot;if(index>=filtered.size())return;
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.sendToServer(new TicexPatternMessage(hand,ForgeRegistries.ITEMS.getKey(filtered.get(index))));
        onClose();
    }
    @Override public void render(GuiGraphics graphics,int mouseX,int mouseY,float delta) {
        renderBackground(graphics);super.render(graphics,mouseX,mouseY,delta);
        graphics.drawCenteredString(font,title,width/2,12,0xFFFFFF);
        graphics.drawCenteredString(font,(page+1)+" / "+Math.max(1,(filtered.size()+5)/6),width/2,214,0xFFFFFF);
    }
    @Override public boolean isPauseScreen(){return false;}
    @Override public boolean mouseScrolled(double x,double y,double delta) {
        if(delta==0)return false;
        page+=delta>0?-1:1;update();return true;
    }
}
