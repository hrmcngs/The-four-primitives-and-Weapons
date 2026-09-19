import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import the_four_primitives_and_weapons.world.AstronomyData;
import the_four_primitives_and_weapons.world.AstronomySettings;
import the_four_primitives_and_weapons.network.AstronomySyncPacket;

public class AstronomyPersistenceTest {
    public static void main(String[] args) {
        var settings = new AstronomySettings(2F, 5, 4, 1, false, 0.5F, 0.75F);
        var data = new AstronomyData();
        data.update(settings);
        if (!data.isDirty()) throw new AssertionError("Settings must be saved");
        var loaded = AstronomyData.load(data.save(new CompoundTag()));
        if (!settings.equals(loaded.settings())) throw new AssertionError("SavedData round trip");
        if (!AstronomySettings.DEFAULT.equals(AstronomyData.load(new CompoundTag()).settings()))
            throw new AssertionError("Old/empty saves default to natural cycle");
        var oldSave = AstronomyData.encode(settings);
        oldSave.remove("Solar"); oldSave.remove("Lunar");
        var migrated = AstronomyData.load(oldSave).settings();
        if (!migrated.equals(new AstronomySettings(2F, 5, 4, 1, false)))
            throw new AssertionError("Existing settings survive eclipse schema migration");
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            AstronomySyncPacket.encode(new AstronomySyncPacket(settings), buffer);
            if (!settings.equals(new AstronomySyncPacket(buffer).settings()) || buffer.readableBytes() != 0)
                throw new AssertionError("Network round trip");
        } finally { buffer.release(); }
        System.out.println("Astronomy persistence and network round trips passed.");
    }
}
