package the_four_primitives_and_weapons.client;

import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.item.TicexWeaponCatalog;
import the_four_primitives_and_weapons.network.WeaponUpgradeMessage;
import the_four_primitives_and_weapons.skill.*;
import the_four_primitives_and_weapons.skill.WeaponGrowthRules.Perk;

public final class WeaponUpgradeScreen extends Screen {
    private final List<Integer> inventorySlots=new ArrayList<>();
    private int selected, choosing=-1;
    private Button power,reset;
    private final List<Button> perks=new ArrayList<>();
    private WeaponUpgradeScreen(){super(text("title"));}
    public static void open(){Minecraft.getInstance().setScreen(new WeaponUpgradeScreen());}
    private static Component text(String suffix,Object... args){return Component.translatable("gui.the_four_primitives_and_weapons.upgrade."+suffix,args);}
    public static Component perkName(Perk perk){return text("perk."+perk.name().toLowerCase(Locale.ROOT));}
    public static Component perkDescription(Perk perk){return text("perk."+perk.name().toLowerCase(Locale.ROOT)+".desc");}
    private ItemStack weapon(){if(minecraft==null||minecraft.player==null||inventorySlots.isEmpty())return ItemStack.EMPTY;return minecraft.player.getInventory().getItem(inventorySlots.get(Math.min(selected,inventorySlots.size()-1)));}
    @Override protected void init(){
        inventorySlots.clear();perks.clear();
        for(int i=0;i<36;i++)if(WeaponGrowth.supported(minecraft.player.getInventory().getItem(i)))inventorySlots.add(i);
        if(WeaponGrowth.supported(minecraft.player.getOffhandItem()))inventorySlots.add(40);
        selected=Math.max(0,Math.min(selected,inventorySlots.size()-1));
        int x=width/2-150;
        if(choosing>=0) {
            for(Perk perk:Perk.values()) {
                Button button=addRenderableWidget(Button.builder(perkName(perk),b->{send(1,choosing,perk.ordinal());choosing=-1;rebuildWidgets();})
                        .bounds(x,58+perk.ordinal()*24,300,20).build());
                button.setTooltip(Tooltip.create(perkDescription(perk)));perks.add(button);
            }
            addRenderableWidget(Button.builder(text("back"),b->{choosing=-1;rebuildWidgets();}).bounds(x,208,300,20).build());
        } else {
            addRenderableWidget(Button.builder(Component.literal("◀"),b->{if(!inventorySlots.isEmpty())selected=Math.floorMod(selected-1,inventorySlots.size());update();}).bounds(x,29,25,20).build());
            addRenderableWidget(Button.builder(Component.literal("▶"),b->{if(!inventorySlots.isEmpty())selected=(selected+1)%inventorySlots.size();update();}).bounds(x+275,29,25,20).build());
            power=addRenderableWidget(Button.builder(text("power"),b->send(0,0,0)).bounds(x,86,300,20).build());
            for(int i=0;i<3;i++) {
                final int slot=i;
                Button button=addRenderableWidget(Button.builder(Component.empty(),b->{var perk=WeaponGrowth.slotPerk(weapon(),slot);if(perk==null){choosing=slot;rebuildWidgets();}else send(1,slot,perk.ordinal());}).bounds(x,112+i*24,300,20).build());
                perks.add(button);
            }
            reset=addRenderableWidget(Button.builder(text("reset"),b->send(2,0,0)).bounds(x,188,300,20).build());
            reset.setTooltip(Tooltip.create(text("reset_cost")));
        }
        update();
    }
    private void send(int action,int slot,int perk) {
        ItemStack weapon=weapon();UUID id=WeaponGrowth.id(weapon);if(id==null||!WeaponGrowth.supported(weapon))return;
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.sendToServer(new WeaponUpgradeMessage(inventorySlots.get(selected),id,action,slot,perk));
    }
    private void update() {
        var weapon=weapon();boolean valid=WeaponGrowth.supported(weapon)&&WeaponGrowth.id(weapon)!=null;
        if(choosing>=0){
            for(int i=0;i<perks.size();i++)perks.get(i).active=valid&&WeaponGrowth.canChoose(weapon,Perk.values()[i])&&WeaponGrowth.rank(weapon,Perk.values()[i])==0&&WeaponGrowth.freePoints(weapon)>=1;
            return;
        }
        int p=WeaponGrowth.power(weapon);
        int emeralds=count(Items.EMERALD),shards=count(the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems.TEMPERING_SHARD.get());
        power.setMessage(text(p>=20?"power_max":"power_next",p+1,WeaponGrowthRules.powerLevelRequired(p)));
        power.active=valid&&p<20&&WeaponGrowth.level(weapon)>=WeaponGrowthRules.powerLevelRequired(p)
                &&emeralds>=WeaponGrowthRules.emeraldCost(p)&&shards>=WeaponGrowthRules.shardCost(p);
        power.setTooltip(Tooltip.create(text("owned",emeralds,shards)));
        boolean any=false;
        for(int i=0;i<3;i++) {
            var perk=WeaponGrowth.slotPerk(weapon,i);int rank=WeaponGrowth.slotRank(weapon,i);any|=rank>0;
            boolean unlocked=WeaponGrowth.level(weapon)>=WeaponGrowthRules.unlock(i);
            Button button=perks.get(i);
            button.setMessage(unlocked?text("slot",i+1,perk==null?text("choose"):perkName(perk),rank):text("slot_locked",i+1,WeaponGrowthRules.unlock(i)));
            button.active=valid&&unlocked&&rank<3&&WeaponGrowth.freePoints(weapon)>=rank+1;
            button.setTooltip(Tooltip.create(perk==null?text("point_cost",rank+1):Component.empty().append(perkDescription(perk)).append("\n").append(text("point_cost",rank+1))));
        }
        reset.active=valid&&any&&emeralds>=5&&shards>=3;
        reset.setTooltip(Tooltip.create(Component.empty().append(text("reset_cost")).append("\n").append(text("owned",emeralds,shards))));
    }
    private int count(Item item){int n=0;for(var stack:minecraft.player.getInventory().items)if(stack.is(item))n+=stack.getCount();return n;}
    @Override public void tick(){super.tick();update();}
    @Override public void render(GuiGraphics graphics,int mouseX,int mouseY,float partialTick){
        renderBackground(graphics);super.render(graphics,mouseX,mouseY,partialTick);
        graphics.drawCenteredString(font,title,width/2,12,0xFFFFFF);
        var weapon=weapon();
        graphics.drawCenteredString(font,weapon.isEmpty()?text("no_weapon"):TicexWeaponCatalog.label(weapon.getItem()),width/2,34,0xFFFFFF);
        if(choosing>=0)return;
        int level=WeaponGrowth.level(weapon),xp=WeaponGrowth.xp(weapon);
        graphics.drawCenteredString(font,text("status",level,40,WeaponGrowth.power(weapon),WeaponGrowth.freePoints(weapon)),width/2,55,0x55FFFF);
        int power=WeaponGrowth.power(weapon);
        graphics.drawCenteredString(font,power>=20?text("power_max"):text("cost",WeaponGrowthRules.emeraldCost(power),WeaponGrowthRules.shardCost(power)),width/2,72,0xBBBBBB);
        graphics.drawCenteredString(font,level>=40?text("level_max"):text("xp",xp-WeaponGrowthRules.xpForLevel(level),WeaponGrowthRules.xpForLevel(level+1)-WeaponGrowthRules.xpForLevel(level)),width/2,217,0xBBBBBB);
    }
    @Override public boolean isPauseScreen(){return false;}
}
