import java.util.List;
import java.util.Set;
import the_four_primitives_and_weapons.util.BoundedConduction;

public final class BoundedConductionTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        var connected = BoundedConduction.collect(List.of(0), n -> List.of(n - 1, n + 1),
                n -> n != 2, 4, 512);
        check(connected.equals(Set.of(-4, -3, -2, -1, 0, 1)), "Dry gap must stop conduction");
        var joined = BoundedConduction.collect(List.of(0, 6), n -> List.of(n - 1, n + 1),
                n -> n >= 0 && n <= 6, 3, 512);
        check(joined.size() == 7, "All wet contacts must seed conduction");
        var capped = BoundedConduction.collect(List.of(0), n -> List.of(n - 1, n + 1, n),
                n -> true, 100, 5);
        check(capped.size() == 5, "Cycles and large water bodies must obey the work limit");
        check(BoundedConduction.collect(List.of(0), n -> List.of(n + 1), n -> false, 4, 512).isEmpty(),
                "Dry chain must not energize nearby water");
        check(BoundedConduction.collect(List.of(0, 0), n -> List.of(n + 1), n -> true, 0, 512).equals(Set.of(0)),
                "Zero distance only includes distinct wet contact blocks");
        check(BoundedConduction.collect(List.of(0), n -> List.of(n + 1), n -> true, 4, 0).isEmpty(),
                "Zero work budget must not visit seeds");
        System.out.println("Bounded conduction checks passed");
    }
}
