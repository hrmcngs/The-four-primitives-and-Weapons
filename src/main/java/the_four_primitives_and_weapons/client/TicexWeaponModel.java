package the_four_primitives_and_weapons.client;

import java.util.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.item.*;

/** Retains existing solid geometry and overrides; flat material weapons use solid family models. */
public final class TicexWeaponModel implements BakedModel {
    private static final List<String> FAMILIES=List.of("sword","knife","shield","bow0","bow1","bow2","bow3","crossbow","trident");
    private static final Map<String,BakedModel> SOLID=new HashMap<>();
    private static final Map<BakedModel,Map<Integer,TicexWeaponModel>> COLORS=new IdentityHashMap<>();
    private static final Map<String,Integer> MATERIAL_COLORS=new HashMap<>();
    private final BakedModel base;
    private final Integer color;
    private final Map<Direction,List<BakedQuad>> sides=new EnumMap<>(Direction.class);
    private final List<BakedQuad> general;
    private TicexWeaponModel(BakedModel base,Integer color){
        this.base=base;this.color=color;
        general=color==null?List.of():tint(base.getQuads(null,null,RandomSource.create(42)),color);
        if(color!=null)for(Direction side:Direction.values())sides.put(side,tint(base.getQuads(null,side,RandomSource.create(42)),color));
    }
    private static ResourceLocation id(String family){return new ResourceLocation(TheFourPrimitivesAndWeaponsMod.MODID,"ticex/"+family);}
    @Mod.EventBusSubscriber(modid=TheFourPrimitivesAndWeaponsMod.MODID,bus=Mod.EventBusSubscriber.Bus.MOD,value=Dist.CLIENT)
    public static final class Events {
        @SubscribeEvent public static void additional(ModelEvent.RegisterAdditional event){for(String family:FAMILIES)event.register(id(family));}
        @SubscribeEvent(priority=EventPriority.LOWEST) public static void models(ModelEvent.ModifyBakingResult event){
            SOLID.clear();COLORS.clear();MATERIAL_COLORS.clear();
            for(String family:FAMILIES){BakedModel model=event.getModels().get(id(family));if(model!=null)SOLID.put(family,model);}
            for(Item item:TicexWeaponCatalog.all()) {
                var itemId=ForgeRegistries.ITEMS.getKey(item);
                var location=new ModelResourceLocation(itemId,"inventory");
                BakedModel base=event.getModels().get(location);
                if(base!=null)event.getModels().put(location,new TicexWeaponModel(base,null));
            }
        }
    }
    private static int materialColor(String id) {
        return MATERIAL_COLORS.computeIfAbsent(id,key->{
            try {
                Class<?> materialId=Class.forName("slimeknights.tconstruct.library.materials.definition.MaterialId");
                Class<?> variantId=Class.forName("slimeknights.tconstruct.library.materials.definition.MaterialVariantId");
                Object material=materialId.getConstructor(String.class).newInstance(key);
                Object variant=variantId.getMethod("create",materialId,String.class).invoke(null,material,"");
                Class<?> loader=Class.forName("slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader");
                Optional<?> info=(Optional<?>)loader.getMethod("getRenderInfo",variantId).invoke(loader.getField("INSTANCE").get(null),variant);
                if(info.isPresent())return ((Number)info.get().getClass().getMethod("vertexColor").invoke(info.get())).intValue();
            }catch(ReflectiveOperationException|LinkageError ignored){}
            return -1;
        });
    }
    private final ItemOverrides overrides=new ItemOverrides(){
        @Override public BakedModel resolve(BakedModel model,ItemStack stack,ClientLevel level,LivingEntity entity,int seed){
            BakedModel resolved=base.getOverrides().resolve(base,stack,level,entity,seed);if(resolved==null)resolved=base;
            if(!stack.hasTag()||!stack.getTag().contains(TicexWeaponRecipe.MATERIAL_TAG,10))return resolved;
            if((!resolved.isGui3d()&&!resolved.isCustomRenderer())
                    ||resolved==net.minecraft.client.Minecraft.getInstance().getModelManager().getMissingModel()) {
                String family=stack.getItem() instanceof CrossbowItem?"crossbow":stack.getItem() instanceof BowItem?"bow0"
                        :stack.getItem() instanceof TridentItem?"trident":stack.getItem() instanceof ShieldItem?"shield"
                        :stack.getItem() instanceof ThrowingKnifeItem?"knife":"sword";
                if(stack.getItem() instanceof BowItem && entity!=null && entity.isUsingItem() && entity.getUseItem()==stack) {
                    int charge=stack.getUseDuration()-entity.getUseItemRemainingTicks();
                    charge=(int)Math.ceil(charge*(1+.06*the_four_primitives_and_weapons.skill.WeaponGrowth.rank(stack,
                            the_four_primitives_and_weapons.skill.WeaponGrowthRules.Perk.HASTE)));
                    float draw=BowItem.getPowerForTime(charge);
                    family="bow"+(draw>=1?3:draw>.65?2:draw>.2?1:0);
                }
                resolved=SOLID.getOrDefault(family,resolved);
            }
            int rgb=materialColor(stack.getTag().getCompound(TicexWeaponRecipe.MATERIAL_TAG).getString("Id"));
            BakedModel solid=resolved;
            return COLORS.computeIfAbsent(solid,k->new HashMap<>()).computeIfAbsent(rgb,c->new TicexWeaponModel(solid,c));
        }
    };
    private static List<BakedQuad> tint(List<BakedQuad> quads,int color){
        List<BakedQuad> out=new ArrayList<>();
        for(BakedQuad q:quads){
            String texture=q.getSprite().contents().name().getPath();
            boolean metal=q.getTintIndex()==6||q.getTintIndex()==7||texture.contains("blade/")||texture.equals("block/iron_block");
            if(!metal||color==-1){out.add(q);continue;}
            int[] data=q.getVertices().clone();int stride=data.length/4;
            // Baked vertex colors are packed ABGR.
            int abgr=0xFF000000|((color&255)<<16)|(color&0xFF00)|((color>>>16)&255);
            for(int i=0;i<4;i++)data[i*stride+3]=abgr;
            out.add(new BakedQuad(data,q.getTintIndex(),q.getDirection(),q.getSprite(),q.isShade()));
        }return out;
    }
    public List<BakedQuad> getQuads(BlockState state,Direction side,RandomSource random){return color==null?base.getQuads(state,side,random):side==null?general:sides.getOrDefault(side,List.of());}
    public boolean useAmbientOcclusion(){return base.useAmbientOcclusion();}
    public boolean isGui3d(){return base.isGui3d();}
    public boolean usesBlockLight(){return base.usesBlockLight();}
    public boolean isCustomRenderer(){return base.isCustomRenderer();}
    public TextureAtlasSprite getParticleIcon(){return base.getParticleIcon();}
    public ItemTransforms getTransforms(){return base.getTransforms();}
    public ItemOverrides getOverrides(){return color==null?overrides:ItemOverrides.EMPTY;}
    public BakedModel applyTransform(ItemDisplayContext context,com.mojang.blaze3d.vertex.PoseStack pose,boolean left){base.applyTransform(context,pose,left);return this;}
}
