package the_four_primitives_and_weapons.client;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;

/** Replaces only tint-2 guard geometry, retaining the weapon's texture, tint and transforms. */
public final class TsubaShapeModel implements BakedModel {
    private static final Map<BakedModel, Map<String, BakedModel>> CACHE = new IdentityHashMap<>();
    private final BakedModel base;
    final String shape;
    private final List<BakedQuad> quads;
    private TsubaShapeModel(BakedModel base, String shape) {
        this.base = base; this.shape = shape;
        List<BakedQuad> all = new ArrayList<>(base.getQuads(null, null, RandomSource.create(42)));
        for (Direction d : Direction.values()) all.addAll(base.getQuads(null, d, RandomSource.create(42)));
        List<BakedQuad> guard = all.stream().filter(q -> q.getTintIndex() == 2).toList();
        quads = new ArrayList<>(all.stream().filter(q -> q.getTintIndex() != 2).toList());
        if (guard.isEmpty()) return;
        float[] min = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
        float[] max = {-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
        for (BakedQuad q : guard) {
            int[] data = q.getVertices(); int stride = data.length / 4;
            for (int i = 0; i < 4; i++) for (int a = 0; a < 3; a++) {
                float p = Float.intBitsToFloat(data[i * stride + a]);
                min[a] = Math.min(min[a], p); max[a] = Math.max(max[a], p);
            }
        }
        int thin = 0;
        for (int a = 1; a < 3; a++) if (max[a]-min[a] < max[thin]-min[thin]) thin = a;
        int u = (thin+1)%3, v = (thin+2)%3;
        // The blade's broad cross-section identifies its edge-to-spine direction.
        float[] bladeMin = {Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE};
        float[] bladeMax = {-Float.MAX_VALUE,-Float.MAX_VALUE,-Float.MAX_VALUE};
        boolean blade = false;
        for (BakedQuad q : all) if (q.getTintIndex() == 6 || q.getTintIndex() == 7) {
            blade = true; int[] data = q.getVertices(); int stride = data.length/4;
            for (int i=0;i<4;i++) for (int a=0;a<3;a++) {
                float p=Float.intBitsToFloat(data[i*stride+a]);
                bladeMin[a]=Math.min(bladeMin[a],p); bladeMax[a]=Math.max(bladeMax[a],p);
            }
        }
        int longAxis = blade ? (bladeMax[u]-bladeMin[u] > bladeMax[v]-bladeMin[v] ? u : v)
                            : (max[u]-min[u] > max[v]-min[v] ? u : v);
        float radius = Math.max(max[u]-min[u],max[v]-min[v])/2;
        double[][] outline = the_four_primitives_and_weapons.util.TsubaOutline.points(shape,radius,longAxis==u);
        int segments = outline.length;
        TextureAtlasSprite sprite = guard.get(0).getSprite();
        float[] center = {(min[0]+max[0])/2,(min[1]+max[1])/2,(min[2]+max[2])/2};
        for (int i=0;i<segments;i++) {
            double[] a=outline[i], b=outline[(i+1)%segments];
            float[] p=center.clone(), q=center.clone();
            p[u]+=(float)a[0]; p[v]+=(float)a[1];
            q[u]+=(float)b[0]; q[v]+=(float)b[1];
            for (int side=0;side<2;side++) {
                float h=side==0?min[thin]:max[thin];
                float[] c=center.clone(), x=p.clone(), y=q.clone(); c[thin]=x[thin]=y[thin]=h;
                addFace(new float[][]{c,x,y,y}, sprite, thin, side==0?-1:1, u,v,min,max);
            }
            float[] p0=p.clone(),p1=p.clone(),q0=q.clone(),q1=q.clone();
            p0[thin]=q0[thin]=min[thin]; p1[thin]=q1[thin]=max[thin];
            float[] normal=new float[3]; normal[u]=(p[u]+q[u])/2-center[u]; normal[v]=(p[v]+q[v])/2-center[v];
            addFace(new float[][]{p0,q0,q1,p1},sprite,normal,u,v,min,max);
        }
    }
    private void addFace(float[][] p, TextureAtlasSprite sprite,int axis,int sign,int u,int v,float[] min,float[] max) {
        float[] n=new float[3]; n[axis]=sign; addFace(p,sprite,n,u,v,min,max);
    }
    private void addFace(float[][] p, TextureAtlasSprite sprite,float[] outward,int u,int v,float[] min,float[] max) {
        float[] a=new float[3],b=new float[3];
        for(int j=0;j<3;j++){a[j]=p[1][j]-p[0][j];b[j]=p[2][j]-p[0][j];}
        float[] n={a[1]*b[2]-a[2]*b[1],a[2]*b[0]-a[0]*b[2],a[0]*b[1]-a[1]*b[0]};
        if(n[0]*outward[0]+n[1]*outward[1]+n[2]*outward[2]<0) {
            float[] t=p[1];p[1]=p[3];p[3]=t;
            for(int j=0;j<3;j++)n[j]=-n[j];
        }
        float len=(float)Math.sqrt(n[0]*n[0]+n[1]*n[1]+n[2]*n[2]);
        if(len==0)return;
        int packed=0;for(int j=0;j<3;j++)packed|=((byte)(n[j]/len*127)&255)<<(j*8);
        int[] data=new int[32];
        for(int i=0;i<4;i++) {
            int o=i*8;for(int j=0;j<3;j++)data[o+j]=Float.floatToRawIntBits(p[i][j]);
            data[o+3]=-1;
            data[o+4]=Float.floatToRawIntBits(sprite.getU(16*(p[i][u]-min[u])/(max[u]-min[u])));
            data[o+5]=Float.floatToRawIntBits(sprite.getV(16*(p[i][v]-min[v])/(max[v]-min[v])));
            data[o+7]=packed;
        }
        quads.add(new BakedQuad(data,2,Direction.getNearest(n[0],n[1],n[2]),sprite,true));
    }
    public static BakedModel apply(BakedModel base,String shape) {
        if(!the_four_primitives_and_weapons.util.TsubaOutline.supported(shape))return base;
        return CACHE.computeIfAbsent(base,k->new HashMap<>()).computeIfAbsent(shape,k->new TsubaShapeModel(base,k));
    }
    public static void clearCache() { CACHE.clear(); }
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
