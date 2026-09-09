package the_four_primitives_and_weapons.util;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/** 接続している導体だけを、距離・探索数の上限内でたどる。 */
public final class BoundedConduction {
    private BoundedConduction() {}
    private record Visit<T>(T node, int distance) {}

    public static <T> Set<T> collect(Collection<T> seeds, Function<T, Iterable<T>> neighbors,
            Predicate<T> conductive, int maxDistance, int maxNodes) {
        Set<T> reached = new LinkedHashSet<>();
        ArrayDeque<Visit<T>> queue = new ArrayDeque<>();
        if (maxDistance < 0 || maxNodes <= 0) return reached;
        for (T seed : seeds) {
            if (reached.size() >= maxNodes) break;
            if (conductive.test(seed) && reached.add(seed)) queue.add(new Visit<>(seed, 0));
        }
        while (!queue.isEmpty() && reached.size() < maxNodes) {
            Visit<T> current = queue.remove();
            if (current.distance() >= maxDistance) continue;
            for (T next : neighbors.apply(current.node())) {
                if (reached.size() >= maxNodes) break;
                if (!reached.contains(next) && conductive.test(next) && reached.add(next)) {
                    queue.add(new Visit<>(next, current.distance() + 1));
                }
            }
        }
        return reached;
    }
}
