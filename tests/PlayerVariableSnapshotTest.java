import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import the_four_primitives_and_weapons.network.TheFourPrimitivesAndWeaponsModVariables.PlayerVariables;
import the_four_primitives_and_weapons.network.TheFourPrimitivesAndWeaponsModVariables.PlayerVariablesSyncMessage;
import the_four_primitives_and_weapons.performance.SyncSnapshot;

public class PlayerVariableSnapshotTest {
    public static void main(String[] args) throws Exception {
        PlayerVariables original = new PlayerVariables();
        for (var field : PlayerVariables.class.getFields()) {
            if (field.getType() == double.class) field.setDouble(original, 12.5);
            if (field.getType() == boolean.class) field.setBoolean(original, true);
            if (field.getType() == String.class) field.set(original, "日本語の同期テスト");
        }
        CompoundTag expected = (CompoundTag) original.writeNBT();
        var packet = new PlayerVariablesSyncMessage(original);
        original.ddd = "modified after send";
        original.aaa = 999;
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            PlayerVariablesSyncMessage.buffer(packet, buffer);
            var decoded = new PlayerVariablesSyncMessage(buffer);
            if (!expected.equals(decoded.data.writeNBT())) throw new AssertionError("packet snapshot mismatch");
        } finally { buffer.release(); }
        var gate = new SyncSnapshot<CompoundTag>();
        Object receiver = new Object();
        if (!gate.shouldSend(receiver, expected, false) || gate.shouldSend(receiver, expected.copy(), false))
            throw new AssertionError("NBT structural equality");
        if (!gate.shouldSend(receiver, (CompoundTag) original.writeNBT(), false))
            throw new AssertionError("NBT changes suppressed");
        System.out.println("All player fields survive packet roundtrip; post-send mutation isolated; NBT dedup passed.");
    }
}
