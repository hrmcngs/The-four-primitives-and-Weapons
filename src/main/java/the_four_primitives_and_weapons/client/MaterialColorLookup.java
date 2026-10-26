package the_four_primitives_and_weapons.client;

import java.util.Map;

/** Tinkers stores variant resources as material/variant, but NBT as material#variant. */
public final class MaterialColorLookup {
    private MaterialColorLookup() {}

    public static String resourceKey(String namespace, String path) {
        int variant = path.lastIndexOf('/');
        return namespace + ":" + (variant < 0 ? path
            : path.substring(0, variant) + "#" + path.substring(variant + 1));
    }

    public static int color(Map<String, Integer> colors, String material) {
        Integer exact = colors.get(material);
        if (exact != null) return exact;
        int variant = material.indexOf('#');
        return colors.getOrDefault(variant < 0 ? material : material.substring(0, variant), 0xFFFFFFFF);
    }
}
