import the_four_primitives_and_weapons.integration.TetraCombatRules;
public class TetraCombatRulesTest {
 public static void main(String[] args) {
  check("tetra:modular_sword", "sword/basic_blade", "", "sword");
  check("tetra:modular_sword", "sword/basic_blade", "katana", "katana");
  check("tetra:modular_sword", "sword/short_blade", "katana", "dagger");
  check("tetra:modular_sword", "sword/heavy_blade", "katana", "greatsword");
  check("tetra:modular_shield", "", "", "shield");
  check("tetra:modular_bow", "", "", "bow");
  check("tetra:modular_crossbow", "", "", "crossbow");
  if (TetraCombatRules.weaponType("tetra:modular_double", "", "") != null) throw new AssertionError("Tools must not become swords");
  if (TetraCombatRules.isModularWeapon("minecraft:iron_sword")) throw new AssertionError("Unrelated item");
  System.out.println("Tetra combat classification and modular stat ownership passed");
 }
 static void check(String item, String blade, String origin, String expected) {
  if (!expected.equals(TetraCombatRules.weaponType(item, blade, origin))) throw new AssertionError(item+blade);
  if (!TetraCombatRules.isModularWeapon(item)) throw new AssertionError("Stat preservation: "+item);
 }
}
