import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.model.geom.ModelPart;
import the_four_primitives_and_weapons.entity.ButterflyAppearanceSettings;
import the_four_primitives_and_weapons.client.renderer.ButterflyRenderer;
public class ButterflyAppearanceTest {
    public static void main(String[] args) throws Exception {
        var tag = new CompoundTag();
        tag.putString("WingColor", "#12ABEF"); tag.putString("EdgeColor", "0xABCDEF");
        tag.putInt("Pattern", 10); tag.putBoolean("Tails", false);
        tag.putFloat("AntennaLength", 2); tag.putFloat("AntennaSpread", 60);
        tag.putFloat("BodyLength", 1.7F); tag.putFloat("FlapRestAngle", 45);
        var parsed = ButterflyAppearanceSettings.readAppearance(tag);
        check(parsed.getInt("WingColor")==0x12ABEF && parsed.getInt("EdgeColor")==0xABCDEF, "hex colors");
        check(parsed.getInt("Pattern")==10 && parsed.contains("Tails") && !parsed.getBoolean("Tails"), "pattern and explicit false");
        check(parsed.getFloat("AntennaLength")==2 && parsed.getFloat("BodyLength")==1.7F, "custom anatomy");
        tag.putFloat("AntennaLength", Float.NaN); tag.putFloat("BodyWidth", Float.POSITIVE_INFINITY);
        tag.putFloat("AntennaSpread", 999); tag.putInt("Pattern", 999);
        parsed=ButterflyAppearanceSettings.readAppearance(tag);
        check(!parsed.contains("AntennaLength") && !parsed.contains("BodyWidth"), "nonfinite fallback");
        check(parsed.getFloat("AntennaSpread")==80 && !parsed.contains("Pattern"), "bounds");
        check(ButterflyAppearanceSettings.readAppearance(new CompoundTag()).isEmpty(), "remove resets defaults");
        var model=new ButterflyRenderer.Model();
        var field=ButterflyRenderer.Model.class.getDeclaredField("body");field.setAccessible(true);
        ModelPart body=(ModelPart)field.get(model),left=body.getChild("leftAntenna"),right=body.getChild("rightAntenna");
        model.configureAntennae(3,80,70);
        check(left.x==-right.x && left.z==right.z && left.z< -2.5F, "symmetric head roots");
        check(left.yRot==-right.yRot && left.xRot<0 && left.zScale==3, "length and orientation");
        model.configureAntennae(1,25,20);
        check(left.zScale==1 && left.x==-0.32F && right.x==0.32F, "next entity resets without moving roots");
        System.out.println("Butterfly NBT validation, removal defaults, antenna transforms and shared model reset passed.");
    }
    static void check(boolean b,String name){if(!b)throw new AssertionError(name);}
}
