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
    LAVENDER(0xAD98D7, 0x574D80, 0xEDE4FF, 4, false);

    public final int wingColor, edgeColor, accentColor, pattern;
    public final boolean tails;

    ButterflyVariant(int wingColor, int edgeColor, int accentColor, int pattern, boolean tails) {
        this.wingColor=wingColor; this.edgeColor=edgeColor; this.accentColor=accentColor;
        this.pattern=pattern; this.tails=tails;
    }

    private static final ButterflyVariant[] ALL = values();
    public static int count() { return ALL.length; }
    public static ButterflyVariant byId(int id) { return ALL[id >= 0 && id < ALL.length ? id : 0]; }
}
