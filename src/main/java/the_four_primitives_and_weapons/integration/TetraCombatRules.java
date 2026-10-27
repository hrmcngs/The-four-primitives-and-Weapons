package the_four_primitives_and_weapons.integration;

/** NBT module classification without linking against Tetra. */
public final class TetraCombatRules {
    private TetraCombatRules() {}

    public static boolean isModularWeapon(String item) {
        return switch (item) {
            case "tetra:modular_sword", "tetra:modular_shield", "tetra:modular_bow", "tetra:modular_crossbow" -> true;
            default -> false;
        };
    }

    public static String weaponType(String item, String blade, String originType) {
        return switch (item) {
            case "tetra:modular_sword" -> switch (blade) {
                case "sword/short_blade", "sword/throwing_knife" -> "dagger";
                case "sword/heavy_blade" -> "greatsword";
                default -> "katana".equals(originType) ? "katana" : "sword";
            };
            case "tetra:modular_shield" -> "shield";
            case "tetra:modular_bow" -> "bow";
            case "tetra:modular_crossbow" -> "crossbow";
            default -> null;
        };
    }
}
