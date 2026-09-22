import net.minecraft.nbt.CompoundTag;
import the_four_primitives_and_weapons.item.KeitoFeyn;

public final class KeitoFeynTest {
    public static void main(String[] args) {
        var tag = new CompoundTag();
        KeitoFeyn.applyDefault(tag);
        check("cursed".equals(tag.getString("Feyn")), "New items default to cursed");
        tag.putInt("Damage", 37);
        tag.putString("CustomData", "preserved");
        KeitoFeyn.applyDefault(tag);
        check(tag.getInt("Damage") == 37 && tag.getString("CustomData").equals("preserved"), "Other NBT survives");
        for (String existing : new String[]{"cursed", "sigiled", "sancted", ""}) {
            tag.putString("Feyn", existing);
            KeitoFeyn.applyDefault(tag);
            check(existing.equals(tag.getString("Feyn")), "Explicit Feyn is preserved");
        }
        tag.remove("Feyn");
        KeitoFeyn.applyDefault(tag);
        check("cursed".equals(tag.getString("Feyn")), "Legacy items receive default");
        System.out.println("Keito default curse and existing NBT preservation passed.");
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
