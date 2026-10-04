package the_four_primitives_and_weapons.network;

import java.util.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.item.WeaponUpgradeJournalItem;
import the_four_primitives_and_weapons.skill.*;

@Mod.EventBusSubscriber(modid=TheFourPrimitivesAndWeaponsMod.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public record WeaponUpgradeMessage(int inventorySlot,UUID weaponId,int action,int slot,int perk) {
    public WeaponUpgradeMessage(FriendlyByteBuf b){this(b.readVarInt(),b.readUUID(),b.readVarInt(),b.readVarInt(),b.readVarInt());}
    public static void encode(WeaponUpgradeMessage m,FriendlyByteBuf b){b.writeVarInt(m.inventorySlot);b.writeUUID(m.weaponId);b.writeVarInt(m.action);b.writeVarInt(m.slot);b.writeVarInt(m.perk);}
    private static int count(net.minecraft.world.entity.player.Player player,Item item){int count=0;for(var stack:player.getInventory().items)if(stack.is(item))count+=stack.getCount();return count;}
    private static void consume(net.minecraft.world.entity.player.Player player,Item item,int count){for(var stack:player.getInventory().items)if(stack.is(item)){int n=Math.min(count,stack.getCount());stack.shrink(n);count-=n;if(count==0)return;}}
    public static void handle(WeaponUpgradeMessage m,java.util.function.Supplier<NetworkEvent.Context> supplier){
        var ctx=supplier.get();ctx.enqueueWork(()->{
            var player=ctx.getSender();if(player==null||player.isSpectator()||player.containerMenu!=player.inventoryMenu)return;
            boolean journal=false;for(int i=0;i<player.getInventory().getContainerSize();i++)if(player.getInventory().getItem(i).getItem() instanceof WeaponUpgradeJournalItem){journal=true;break;}
            if(!journal||!((m.inventorySlot>=0&&m.inventorySlot<36)||m.inventorySlot==40))return;
            var weapon=player.getInventory().getItem(m.inventorySlot);
            if(!WeaponGrowth.supported(weapon)||!m.weaponId.equals(WeaponGrowth.id(weapon)))return;
            var tag=WeaponGrowth.data(weapon).copy();int emeralds=0,shards=0;
            boolean valid=false;
            if(m.action==0) {
                int power=WeaponGrowth.power(weapon);
                valid=power<20&&WeaponGrowth.level(weapon)>=WeaponGrowthRules.powerLevelRequired(power);
                emeralds=WeaponGrowthRules.emeraldCost(power);shards=WeaponGrowthRules.shardCost(power);
                if(valid)tag.putInt("Power",power+1);
            } else if(m.action==1 && m.slot>=0&&m.slot<3 && m.perk>=0&&m.perk<WeaponGrowthRules.Perk.values().length) {
                var perk=WeaponGrowthRules.Perk.values()[m.perk];
                int rank=WeaponGrowth.slotRank(weapon,m.slot);
                valid=WeaponGrowth.canChoose(weapon,perk)&&WeaponGrowth.level(weapon)>=WeaponGrowthRules.unlock(m.slot)&&rank<3&&WeaponGrowth.freePoints(weapon)>=rank+1
                    &&(rank==0?WeaponGrowth.rank(weapon,perk)==0:WeaponGrowth.slotPerk(weapon,m.slot)==perk);
                if(valid){tag.putString("Perk"+m.slot,perk.name());tag.putInt("Rank"+m.slot,rank+1);}
            } else if(m.action==2) {
                for(int i=0;i<3;i++)valid|=WeaponGrowth.slotRank(weapon,i)>0;
                emeralds=5;shards=3;
                if(valid)for(int i=0;i<3;i++){tag.remove("Perk"+i);tag.remove("Rank"+i);}
            }
            if(!valid){player.displayClientMessage(Component.translatable("message.the_four_primitives_and_weapons.upgrade_requirements"),true);return;}
            Item shard=TheFourPrimitivesAndWeaponsModItems.TEMPERING_SHARD.get();
            if(count(player,Items.EMERALD)<emeralds||count(player,shard)<shards){player.displayClientMessage(Component.translatable("message.the_four_primitives_and_weapons.upgrade_materials"),true);return;}
            consume(player,Items.EMERALD,emeralds);consume(player,shard,shards);
            WeaponGrowth.write(weapon,tag);player.getInventory().setChanged();player.inventoryMenu.broadcastChanges();
            player.level().playSound(null,player.blockPosition(),net.minecraft.sounds.SoundEvents.ANVIL_USE,net.minecraft.sounds.SoundSource.PLAYERS,.6f,1.3f);
        });ctx.setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event){TheFourPrimitivesAndWeaponsMod.addNetworkMessage(WeaponUpgradeMessage.class,WeaponUpgradeMessage::encode,WeaponUpgradeMessage::new,WeaponUpgradeMessage::handle);}
}
