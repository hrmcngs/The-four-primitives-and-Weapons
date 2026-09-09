package the_four_primitives_and_weapons.util;

import com.google.gson.*;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/** Packaged model geometry, usable on both the dedicated server and client. */
public final class StabbedWeaponGeometry {
    public static final float RENDER_OFFSET = -0.15F;
    private static final Map<ResourceLocation, Optional<Vec3[]>> CACHE = new ConcurrentHashMap<>();
    private StabbedWeaponGeometry() {}

    public static Vec3[] localAxis(ItemStack stack) {
        if (stack.isEmpty()) return null;
        ResourceLocation model = modelFor(stack);
        if (model == null) return null;
        return CACHE.computeIfAbsent(model, id -> Optional.ofNullable(readAxis(id))).orElse(null);
    }

    public static java.util.List<net.minecraft.world.phys.shapes.VoxelShape> collisionPieces(Vec3[] ends, double radius) {
        int count = Math.max(1, Math.min(128, (int)Math.ceil(ends[0].distanceTo(ends[1]) / 0.08)));
        java.util.List<net.minecraft.world.phys.shapes.VoxelShape> shapes = new java.util.ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Vec3 a = ends[0].lerp(ends[1], i / (double) count);
            Vec3 b = ends[0].lerp(ends[1], (i + 1.0) / count);
            shapes.add(net.minecraft.world.phys.shapes.Shapes.create(new net.minecraft.world.phys.AABB(a,b).inflate(radius)));
        }
        return shapes;
    }

    private static ResourceLocation modelFor(ItemStack stack) {
        if (CuriosScabbardHelper.isScabbard(stack)) {
            String key = CuriosScabbardHelper.storageKeyFor(stack);
            ItemStack stored = stack.hasTag() ? ItemStack.of(stack.getTag().getCompound(key)) : ItemStack.EMPTY;
            SayaRegistry.SayaType type = switch (key) {
                case "StoredRapier" -> SayaRegistry.SayaType.RAPIER;
                case "StoredDagger" -> SayaRegistry.SayaType.DAGGER;
                case "StoredSword" -> stack.getItem() instanceof the_four_primitives_and_weapons.item.TyokutoSayaItem
                    ? SayaRegistry.SayaType.TYOKUTO : SayaRegistry.SayaType.SWORD;
                default -> SayaRegistry.SayaType.KATANA;
            };
            SayaRegistry.Entry entry = SayaRegistry.getEntry(type, stored);
            if (entry != null && entry.hasCustomModel()) return entry.modelLocation();
        }
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return id == null ? null : new ResourceLocation(id.getNamespace(), "item/" + id.getPath());
    }

    private static JsonObject load(ResourceLocation id, int depth) throws Exception {
        if (depth > 20) return new JsonObject();
        String path = "/assets/" + id.getNamespace() + "/models/" + id.getPath() + ".json";
        try (var stream = StabbedWeaponGeometry.class.getResourceAsStream(path)) {
            if (stream == null) return new JsonObject();
            JsonObject own = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject merged = own.has("parent") ? load(new ResourceLocation(own.get("parent").getAsString()), depth + 1) : new JsonObject();
            for (var field : own.entrySet()) {
                if (field.getKey().equals("display") && merged.has("display")) {
                    for (var display : field.getValue().getAsJsonObject().entrySet())
                        merged.getAsJsonObject("display").add(display.getKey(), display.getValue());
                } else merged.add(field.getKey(), field.getValue());
            }
            return merged;
        }
    }

    private static Vector3f vector(JsonObject object, String key, float fallback) {
        if (!object.has(key)) return new Vector3f(fallback);
        JsonArray a = object.getAsJsonArray(key);
        return new Vector3f(a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat());
    }

    private static Vec3[] readAxis(ResourceLocation id) {
        try {
            JsonObject model = load(id, 0);
            if (!model.has("elements") || model.getAsJsonArray("elements").isEmpty()) return null;
            Vector3f min = new Vector3f(Float.POSITIVE_INFINITY), max = new Vector3f(Float.NEGATIVE_INFINITY);
            for (JsonElement value : model.getAsJsonArray("elements")) {
                JsonObject element = value.getAsJsonObject();
                Vector3f from = vector(element, "from", 0), to = vector(element, "to", 0);
                for (int i = 0; i < 8; i++) {
                    Vector3f corner = new Vector3f((i & 1) == 0 ? from.x : to.x,
                        (i & 2) == 0 ? from.y : to.y, (i & 4) == 0 ? from.z : to.z);
                    if (element.has("rotation")) {
                        JsonObject rotation = element.getAsJsonObject("rotation");
                        Vector3f origin = vector(rotation, "origin", 0);
                        float angle = (float)Math.toRadians(rotation.get("angle").getAsFloat());
                        String axis = rotation.get("axis").getAsString();
                        corner.sub(origin);
                        if (axis.equals("x")) corner.rotateX(angle);
                        else if (axis.equals("y")) corner.rotateY(angle);
                        else corner.rotateZ(angle);
                        if (rotation.has("rescale") && rotation.get("rescale").getAsBoolean()) {
                            float scale = 1.0F / (float)Math.cos(angle);
                            corner.mul(axis.equals("x") ? 1 : scale, axis.equals("y") ? 1 : scale, axis.equals("z") ? 1 : scale);
                        }
                        corner.add(origin);
                    }
                    min.min(corner); max.max(corner);
                }
            }
            Vector3f a = new Vector3f(min).add(max).mul(0.5F), b = new Vector3f(a);
            Vector3f size = new Vector3f(max).sub(min);
            int axis = size.y >= size.x && size.y >= size.z ? 1 : size.x >= size.z ? 0 : 2;
            a.setComponent(axis, min.get(axis)); b.setComponent(axis, max.get(axis));
            JsonObject display = model.has("display") && model.getAsJsonObject("display").has("thirdperson_righthand")
                ? model.getAsJsonObject("display").getAsJsonObject("thirdperson_righthand") : new JsonObject();
            Vector3f scale = vector(display, "scale", 1), rotation = vector(display, "rotation", 0), translation = vector(display, "translation", 0).div(16);
            Quaternionf q = new Quaternionf().rotationXYZ((float)Math.toRadians(rotation.x),
                (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
            a.div(16).sub(0.5F,0.5F,0.5F).mul(scale).rotate(q).add(translation);
            b.div(16).sub(0.5F,0.5F,0.5F).mul(scale).rotate(q).add(translation);
            return new Vec3[]{new Vec3(a.x,a.y,a.z), new Vec3(b.x,b.y,b.z)};
        } catch (Exception exception) {
            org.apache.logging.log4j.LogManager.getLogger(StabbedWeaponGeometry.class).warn("Cannot read planted weapon model {}", id, exception);
            return null;
        }
    }
}
