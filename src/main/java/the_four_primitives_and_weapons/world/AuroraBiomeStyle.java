package the_four_primitives_and_weapons.world;

/** Local sky appearance; broad climate fallback also supports modded biomes. */
public enum AuroraBiomeStyle {
    POLAR(0.12F, 0.95F, 0.5F, 0.25F, 0.6F, 1F, 1F, 3, 20F),
    FOREST(0.2F, 0.8F, 0.4F, 0.7F, 0.3F, 0.95F, 0.75F, 2, 16F),
    OCEAN(0.08F, 0.65F, 0.95F, 0.35F, 0.85F, 1F, 0.8F, 2, 12F),
    MOUNTAIN(0.35F, 0.45F, 0.95F, 0.8F, 0.3F, 1F, 0.9F, 2, 24F),
    PLAINS(0.25F, 0.8F, 0.55F, 0.4F, 0.65F, 0.85F, 0.45F, 1, 13F),
    HIDDEN(0.25F, 0.8F, 0.55F, 0.4F, 0.65F, 0.85F, 0F, 1, 13F);

    public final float red, green, blue, topRed, topGreen, topBlue, strength, height;
    public final int ribbons;
    AuroraBiomeStyle(float r, float g, float b, float tr, float tg, float tb, float strength, int ribbons, float height) {
        this.red = r; this.green = g; this.blue = b;
        this.topRed = tr; this.topGreen = tg; this.topBlue = tb;
        this.strength = strength; this.ribbons = ribbons; this.height = height;
    }
    public static AuroraBiomeStyle select(float temperature, boolean jungle, boolean badlands,
                                          boolean ocean, boolean mountain, boolean forest) {
        if (jungle || badlands || temperature >= 1F) return HIDDEN;
        if (temperature < 0.2F) return POLAR;
        if (ocean) return OCEAN;
        if (mountain) return MOUNTAIN;
        if (forest) return FOREST;
        return PLAINS;
    }
    public AuroraBiomeStyle forForcedDisplay(boolean forced) {
        return this == HIDDEN && forced ? PLAINS : this;
    }
}
