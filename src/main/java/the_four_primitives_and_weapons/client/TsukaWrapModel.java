package the_four_primitives_and_weapons.client;

import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import the_four_primitives_and_weapons.util.TsukaPattern;

/** Subdivides the existing handle surface; no extra overlapping layer or texture generation. */
public final class TsukaWrapModel implements BakedModel {
    private static final Map<BakedModel, Map<String, TsukaWrapModel>> CACHE = new IdentityHashMap<>();
    final BakedModel base;
    final String style;
    private final List<BakedQuad> quads = new ArrayList<>();
    private TsukaWrapModel(BakedModel base, String style) {
        this.base = base;
        this.style = style;
        List<BakedQuad> all = new ArrayList<>(base.getQuads(null, null, RandomSource.create(42)));
        for (Direction side : Direction.values()) all.addAll(base.getQuads(null, side, RandomSource.create(42)));
        float[] min = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
        float[] max = {-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
        for (BakedQuad q : all) if (q.getTintIndex() == 1) {
            int[] data = q.getVertices(); int stride = data.length / 4;
            for (int i=0;i<4;i++) for(int a=0;a<3;a++) {
                float value = Float.intBitsToFloat(data[i*stride+a]);
                min[a]=Math.min(min[a],value); max[a]=Math.max(max[a],value);
            }
        }
        int longAxis=0;
        for(int a=1;a<3;a++) if(max[a]-min[a]>max[longAxis]-min[longAxis]) longAxis=a;
        TextureAtlasSprite white = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
            .getSprite(new ResourceLocation("minecraft", "block/white_concrete"));
        for(BakedQuad q:all) {
            if(q.getTintIndex()!=1){quads.add(q);continue;}
            int[] original=q.getVertices(); int stride=original.length/4;
            // Keep vertex ordering and interpolate positions on the original face.
            double edge01=Math.abs(Float.intBitsToFloat(original[stride+longAxis])-Float.intBitsToFloat(original[longAxis]));
            double edge03=Math.abs(Float.intBitsToFloat(original[3*stride+longAxis])-Float.intBitsToFloat(original[longAxis]));
            boolean lengthAlongV=edge01>edge03;
            int rows=lengthAlongV?36:8, columns=lengthAlongV?8:36;
            for(int row=0;row<rows;row++) for(int col=0;col<columns;col++) {
                int[] data=original.clone();
                double[][] corners={{(double)col/columns,(double)row/rows},{(double)col/columns,(double)(row+1)/rows},
                    {(double)(col+1)/columns,(double)(row+1)/rows},{(double)(col+1)/columns,(double)row/rows}};
                double along=0;
                for(int i=0;i<4;i++) {
                    double u=corners[i][0],v=corners[i][1];
                    for(int a=0;a<3;a++) {
                        double value=(1-u)*(1-v)*Float.intBitsToFloat(original[a])
                            +(1-u)*v*Float.intBitsToFloat(original[stride+a])
                            +u*v*Float.intBitsToFloat(original[2*stride+a])
                            +u*(1-v)*Float.intBitsToFloat(original[3*stride+a]);
                        data[i*stride+a]=Float.floatToRawIntBits((float)value);
                        if(a==longAxis) along+=(value-min[a])/Math.max(1e-6,max[a]-min[a])/4;
                    }
                    data[i*stride+3]=-1;
                    data[i*stride+4]=Float.floatToRawIntBits(white.getU(corners[i][0]*16));
                    data[i*stride+5]=Float.floatToRawIntBits(white.getV(corners[i][1]*16));
                }
                // Across is measured on the face's shorter edge, independent of UV rotation.
                double across=lengthAlongV?(col+.5)/columns:(row+.5)/rows;
                boolean cap=Math.max(edge01,edge03)<1e-6;
                quads.add(new BakedQuad(data,!cap && TsukaPattern.underlay(style,across,along)?8:1,q.getDirection(),white,q.isShade()));
            }
        }
    }
    public static BakedModel apply(BakedModel base,String style) {
        return CACHE.computeIfAbsent(base,k->new HashMap<>()).computeIfAbsent(style,k->new TsukaWrapModel(base,k));
    }
    public static void clearCache(){CACHE.clear();}
    public List<BakedQuad> getQuads(BlockState state,Direction side,RandomSource random){return side==null?quads:List.of();}
    public boolean useAmbientOcclusion(){return base.useAmbientOcclusion();}
    public boolean isGui3d(){return base.isGui3d();}
    public boolean usesBlockLight(){return base.usesBlockLight();}
    public boolean isCustomRenderer(){return base.isCustomRenderer();}
    public TextureAtlasSprite getParticleIcon(){return base.getParticleIcon();}
    public ItemTransforms getTransforms(){return base.getTransforms();}
    public ItemOverrides getOverrides(){return ItemOverrides.EMPTY;}
    public BakedModel applyTransform(net.minecraft.world.item.ItemDisplayContext context,com.mojang.blaze3d.vertex.PoseStack pose,boolean left){base.applyTransform(context,pose,left);return this;}
}
