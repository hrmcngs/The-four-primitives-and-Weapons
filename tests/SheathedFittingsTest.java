import java.util.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import the_four_primitives_and_weapons.client.KatanaTsukaModel;

public class SheathedFittingsTest {
    record Model(List<BakedQuad> faces) implements BakedModel {
        public List<BakedQuad> getQuads(BlockState state,Direction side,RandomSource random){return side==null?faces:List.of();}
        public boolean useAmbientOcclusion(){return true;}
        public boolean isGui3d(){return true;}
        public boolean usesBlockLight(){return true;}
        public boolean isCustomRenderer(){return false;}
        public TextureAtlasSprite getParticleIcon(){return null;}
        public ItemTransforms getTransforms(){return ItemTransforms.NO_TRANSFORMS;}
        public ItemOverrides getOverrides(){return ItemOverrides.EMPTY;}
    }
    static BakedQuad face(int tint,float x,float y,float z,int color) {
        int[] vertices=new int[32];
        float[][] points={{x,y,z},{x,y,z+1},{x+1,y,z+1},{x+1,y,z}};
        for(int i=0;i<4;i++) {
            for(int a=0;a<3;a++)vertices[i*8+a]=Float.floatToRawIntBits(points[i][a]);
            vertices[i*8+3]=color;
            vertices[i*8+4]=Float.floatToRawIntBits(.2f+i*.01f);
            vertices[i*8+5]=Float.floatToRawIntBits(.6f-i*.02f);
            vertices[i*8+7]=0x007F00;
        }
        return new BakedQuad(vertices,tint,Direction.UP,null,true);
    }
    static List<BakedQuad> quads(BakedModel model){return model.getQuads(null,null,RandomSource.create(42));}
    static void near(float a,float b){if(Math.abs(a-b)>1e-6)throw new AssertionError(a+" != "+b);}
    public static void main(String[] args) {
        var body=face(0,8,0,0,0xFFABCDEF);
        var source=List.of(face(2,0,7,16,-1),face(2,1,7,16,-1),
            face(1,0,5,16,-1),face(8,0,4,16,-1),face(3,0,3,16,0xFFF6F6F6));
        var base=new Model(List.of(body,face(2,8,7,9,-1),face(1,8,5,9,-1),face(3,8,3,9,-1)));
        var weapon=new Model(source);
        var result=KatanaTsukaModel.withWeaponFittings(base,weapon);
        if(quads(result).get(0)!=body)throw new AssertionError("Sheath artwork changed");
        if(quads(result).size()!=source.size()+1)throw new AssertionError("Shape or underlay lost");
        for(int i=0;i<source.size();i++) {
            var before=source.get(i);var after=quads(result).get(i+1);
            if(before.getTintIndex()!=after.getTintIndex())throw new AssertionError("Material tint lost");
            int[] a=before.getVertices(),b=after.getVertices();
            for(int v=0;v<4;v++) {
                near(Float.intBitsToFloat(b[v*8]),Float.intBitsToFloat(a[v*8])+7.5f);
                near(Float.intBitsToFloat(b[v*8+1]),Float.intBitsToFloat(a[v*8+1]));
                near(Float.intBitsToFloat(b[v*8+2]),Float.intBitsToFloat(a[v*8+2])-7);
                for(int j=3;j<8;j++)if(a[v*8+j]!=b[v*8+j])throw new AssertionError("UV, shading or normal changed");
            }
        }
        if(KatanaTsukaModel.withWeaponFittings(base,weapon)!=result)throw new AssertionError("Model cache missed");
        var plain=new Model(List.of(face(0,0,0,0,-1)));
        if(KatanaTsukaModel.withWeaponFittings(base,plain)!=base)throw new AssertionError("Non-fitting sword changed");
        // A missing guard (e.g. shirasaya) must be removed, with the head used as anchor.
        var bare=new Model(List.of(face(1,0,5,16,-1),face(3,0,3,16,-1)));
        if(quads(KatanaTsukaModel.withWeaponFittings(base,bare)).stream().anyMatch(q->q.getTintIndex()==2))
            throw new AssertionError("Hidden guard reappeared");
        if(base.faces().size()!=4 || weapon.faces().size()!=5)throw new AssertionError("Source model mutated");
        KatanaTsukaModel.clearCache();
        if(KatanaTsukaModel.withWeaponFittings(base,weapon)==result)throw new AssertionError("Reload retained old model");
        System.out.println("Sheathed shape, wrap, UV, tint and shading checks passed");
    }
}
