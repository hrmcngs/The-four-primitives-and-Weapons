package the_four_primitives_and_weapons.entity;

/** Stylized palettes inspired by butterflies, not exact species identification. IDs are save-compatible. */
public enum ButterflyVariant {
    MORPHO(0x278EDE, 0x172D45, 0xA7E7FF, 0, false),
    MONARCH(0xE88B29, 0x282021, 0xFFF2D7, 1, false),
    SWALLOWTAIL(0xEADC83, 0x292C27, 0xE99566, 2, true),
    CABBAGE_WHITE(0xF0EFD9, 0x555D50, 0xFFFFFF, 3, false),
    SULPHUR(0xE7D94E, 0x657139, 0xFFF5AE, 3, false),
    OWL(0x997650, 0x342C27, 0xDFCB8C, 4, false),
    POSTMAN(0x302C35, 0x14171D, 0xDF5146, 5, false),
    BLUE_SWALLOWTAIL(0x345F72, 0x17262F, 0x71D3C5, 2, true),
    SAKURA(0xE5A8BE, 0x83576D, 0xFFF1DC, 5, false),
    LAVENDER(0xAD98D7, 0x574D80, 0xEDE4FF, 4, false),
    PEARL(0xEDE6DC, 0x59687A, 0xFFFFFF, 6, false),
    SUNSET(0xEE805E, 0x753859, 0xFFD889, 7, false),
    EMERALD(0x39AF86, 0x193C36, 0xC7F7C7, 8, true),
    OBSIDIAN(0x32313F, 0x12131B, 0xB7A6DD, 6, true),
    SNOW(0xFFFFFF, 0x9DAEC0, 0xD5E9F4, 0, false),
    COPPER(0xBF7242, 0x51392C, 0xF3D59B, 1, false),
    RUBY(0xBF4166, 0x402736, 0xFFC5CA, 4, false),
    SAPPHIRE(0x4559CE, 0x222746, 0x9FC9FF, 8, true),
    AMBER(0xDDAF38, 0x695129, 0xFFF0A3, 9, false),
    MINT(0x9ED9B5, 0x3A796E, 0xF1FFD8, 3, false),
    CORAL(0xEC9192, 0x924755, 0xFFE7BF, 6, false),
    INDIGO(0x595293, 0x2E294A, 0xB7A8EA, 5, false),
    LIME(0xB7CC52, 0x435238, 0xEFFFA6, 2, true),
    TEAL(0x329FA7, 0x254C5A, 0xABE7D8, 7, false),
    ROSE(0xD181A7, 0x703A64, 0xFFDEEA, 8, false),
    GOLD(0xEBC55D, 0x756036, 0xFFF4C8, 6, true),
    SILVER(0xAFBFCC, 0x4D6477, 0xEEF5FF, 9, false),
    CHOCOLATE(0x78533E, 0x372B2B, 0xDBBC87, 4, false),
    CREAM(0xE9D8AB, 0x8C7656, 0xFFFAE7, 3, false),
    VIOLET(0x9565BD, 0x453053, 0xEFC1FF, 2, true),
    AQUA(0x6BDDE0, 0x356674, 0xEAFFFC, 5, false),
    SCARLET(0xD5523E, 0x472426, 0xFFD084, 1, false),
    FOREST(0x526E46, 0x29382A, 0xBFD68C, 9, false),
    DUSK(0x9B83A5, 0x514158, 0xE8CBDD, 7, false),
    DAWN(0xF4C0A2, 0x9D7180, 0xFFF4CD, 0, false),
    ICE(0xAED9EE, 0x62879B, 0xF3FFFF, 6, true),
    PLUM(0x854A74, 0x422942, 0xE4A4CF, 8, false),
    OLIVE(0x959357, 0x4D5033, 0xE8E5AE, 2, false),
    PEACH(0xF1B79B, 0xBA7769, 0xFFF2D9, 3, false),
    OCEAN(0x326BAC, 0x233E5C, 0x70DCCF, 4, false),
    MAGENTA(0xCA6BA9, 0x613756, 0xFDD4F0, 7, true),
    SAND(0xCBBB95, 0x746345, 0xF3E8CD, 9, false),
    CHARCOAL(0x56585D, 0x252B32, 0xD7CAB1, 5, false),
    LILAC(0xD2B7E4, 0x877391, 0xF8EAFB, 6, false),
    CYAN(0x38BEE0, 0x21536E, 0xBBF6FF, 8, false),
    CRIMSON(0x8E3043, 0x2F202C, 0xF18B80, 2, true),
    FIREFLY_GOLD(0xDBC057, 0x313D36, 0xFFF5A7, 5, false),
    MOONLIGHT(0xD6DCEB, 0x575B83, 0xFFFFFF, 4, true),
    DREAMWING(0xF06428, 0xA51D30, 0xFFD65A, 10, false);

    public final int wingColor, edgeColor, accentColor, pattern;
    public final boolean tails;

    ButterflyVariant(int wingColor, int edgeColor, int accentColor, int pattern, boolean tails) {
        this.wingColor=wingColor; this.edgeColor=edgeColor; this.accentColor=accentColor;
        this.pattern=pattern; this.tails=tails;
    }

    private static final ButterflyVariant[] ALL = values();
    public static final int PATTERN_COUNT = 11;
    public static ButterflyVariant byName(String name) {
        try { return valueOf(name.toUpperCase(java.util.Locale.ROOT)); }
        catch (IllegalArgumentException exception) { return MORPHO; }
    }
    public static int count() { return ALL.length; }
    public static ButterflyVariant byId(int id) { return ALL[id >= 0 && id < ALL.length ? id : 0]; }
}
