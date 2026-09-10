package the_four_primitives_and_weapons.util;

import the_four_primitives_and_weapons.ai.lisp.LispInterpreter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/** Parsed once per reload. Only numbers and booleans cross into gameplay code. */
public final class LunaBehaviorScript {
    public enum Setting {
        ANCHOR_SIDE(1.5, -16, 16), ANCHOR_HEIGHT(1.4, -16, 16),
        IDLE_SPEED(0.22, 0, 4), COMBAT_SPEED(0.32, 0, 4), STOP_DISTANCE(0.08, 0, 2),
        TELEPORT_DISTANCE(32, 1, 256), SCAN_INTERVAL(10, 1, 1200), SCAN_RANGE(12, 0, 64),
        TARGET_RANGE(24, 0.1, 128), ANCHOR_TOLERANCE(2, 0, 16),
        ROTATION_BLEND(0.22, 0.001, 1), AIM_TOLERANCE(7, 0, 180),
        LASER_DAMAGE(8, 0, 1000), MIN_COOLDOWN(10, 1, 1200), COOLDOWN_EXTRA(5, 0, 1200),
        FIRING_TICKS(7, 1, 1200), PARTICLE_INTERVAL(5, 0, 1200), LASER_STEP(0.2, 0.05, 4);
        public final String symbol;
        public final double fallback, min, max;
        Setting(double fallback, double min, double max) {
            this.symbol = name().toLowerCase(Locale.ROOT).replace('_', '-');
            this.fallback = fallback; this.min = min; this.max = max;
        }
    }

    private static final Set<String> RULES = Set.of("damage", "cooldown", "target-priority",
            "attack-enabled", "prefer-luna-element", "combined-element-level");
    private static final Set<String> SENSORS = Set.of("owner-health-ratio", "owner-attack-delay",
            "target-distance", "owner-attacked-target", "target-attacked-owner",
            "luna-element-level", "book-element-level");
    private final LispInterpreter interpreter = new LispInterpreter();
    private final Map<String, Object> constants = new HashMap<>();
    private final Map<String, Object> rules = new HashMap<>();
    private final Set<String> warned = ConcurrentHashMap.newKeySet();
    private final Consumer<String> warning;

    private LunaBehaviorScript(Consumer<String> warning) {
        this.warning = warning;
        for (Setting setting : Setting.values()) constants.put(setting.symbol, setting.fallback);
        interpreter.registerFunction("ceil", args -> Math.ceil(LispInterpreter.toDouble(args.get(0))));
        interpreter.registerFunction("floor", args -> Math.floor(LispInterpreter.toDouble(args.get(0))));
    }

    public static LunaBehaviorScript defaults() { return new LunaBehaviorScript(message -> {}); }

    public static LunaBehaviorScript parse(String source, Consumer<String> warning) {
        checkStructure(source);
        LunaBehaviorScript script = new LunaBehaviorScript(warning);
        Object parsed = script.interpreter.parse("(seq\n" + source + "\n)");
        if (!(parsed instanceof List<?> forms)) throw new IllegalArgumentException("Expected Lisp forms");
        Set<String> defined = new HashSet<>();
        for (int i = 1; i < forms.size(); i++) {
            if (!(forms.get(i) instanceof List<?> form) || form.size() != 3
                    || !(form.get(0) instanceof LispInterpreter.Symbol operation)
                    || !(form.get(1) instanceof LispInterpreter.Symbol name)) {
                throw new IllegalArgumentException("Expected (define name value) or (rule name expression)");
            }
            if (!defined.add(operation.name + ":" + name.name)) throw new IllegalArgumentException("Duplicate " + name.name);
            if (operation.name.equals("define")) {
                Setting setting = Arrays.stream(Setting.values()).filter(s -> s.symbol.equals(name.name))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown setting " + name.name));
                validate(form.get(2), script.constants.keySet());
                Object result = script.interpreter.eval(form.get(2), new HashMap<>(script.constants));
                if (!(result instanceof Number n) || !Double.isFinite(n.doubleValue())
                        || n.doubleValue() < setting.min || n.doubleValue() > setting.max) {
                    throw new IllegalArgumentException("Invalid " + name.name + " (" + setting.min + ".." + setting.max + ")");
                }
                if (Set.of(Setting.SCAN_INTERVAL, Setting.MIN_COOLDOWN, Setting.COOLDOWN_EXTRA,
                        Setting.FIRING_TICKS, Setting.PARTICLE_INTERVAL).contains(setting)
                        && n.doubleValue() != Math.floor(n.doubleValue())) {
                    throw new IllegalArgumentException("Expected whole ticks: " + name.name);
                }
                script.constants.put(name.name, n.doubleValue());
            } else if (operation.name.equals("rule") && RULES.contains(name.name)) {
                Set<String> available = new HashSet<>(script.constants.keySet());
                available.addAll(SENSORS);
                validate(form.get(2), available);
                script.rules.put(name.name, form.get(2));
            } else throw new IllegalArgumentException("Unknown form " + operation.name + " " + name.name);
        }
        return script;
    }

    public double value(Setting setting) { return ((Number) constants.get(setting.symbol)).doubleValue(); }
    public int ticks(Setting setting) { return (int) value(setting); }

    public double damage(Map<String, Object> sensors) {
        return number("damage", sensors, value(Setting.LASER_DAMAGE), 0, 1000);
    }
    public int cooldown(Map<String, Object> sensors) {
        double delay = ((Number) sensors.get("owner-attack-delay")).doubleValue();
        double fallback = Math.max(value(Setting.MIN_COOLDOWN), Math.ceil(delay) + value(Setting.COOLDOWN_EXTRA));
        return (int) Math.ceil(number("cooldown", sensors, fallback, 1, 2400));
    }
    public double targetPriority(Map<String, Object> sensors) {
        double fallback = Boolean.TRUE.equals(sensors.get("owner-attacked-target")) ? 3
                : Boolean.TRUE.equals(sensors.get("target-attacked-owner")) ? 2 : 1;
        return number("target-priority", sensors, fallback, -10000, 10000);
    }
    public boolean attackEnabled(Map<String, Object> sensors) { return bool("attack-enabled", sensors, true); }
    public boolean preferLunaElement(Map<String, Object> sensors) { return bool("prefer-luna-element", sensors, true); }
    public int combinedElementLevel(Map<String, Object> sensors, int luna, int book) {
        double fallback = (double) Math.max(luna, book) + Math.max(Math.min(luna, book) / 2, 1);
        return (int) number("combined-element-level", sensors, fallback, 0, Integer.MAX_VALUE);
    }

    private Object evaluate(String rule, Map<String, Object> sensors, Object fallback) {
        if (!rules.containsKey(rule)) return fallback;
        Map<String, Object> environment = new HashMap<>(constants);
        environment.putAll(sensors);
        try { return interpreter.eval(rules.get(rule), environment); }
        catch (RuntimeException ex) { warn(rule); return fallback; }
    }
    private double number(String rule, Map<String, Object> sensors, double fallback, double min, double max) {
        Object result = evaluate(rule, sensors, fallback);
        if (result instanceof Number n && Double.isFinite(n.doubleValue()) && n.doubleValue() >= min && n.doubleValue() <= max) {
            return n.doubleValue();
        }
        warn(rule); return fallback;
    }
    private boolean bool(String rule, Map<String, Object> sensors, boolean fallback) {
        Object result = evaluate(rule, sensors, fallback);
        if (result instanceof Boolean b) return b;
        warn(rule); return fallback;
    }
    private void warn(String rule) {
        if (warned.add(rule)) warning.accept("Invalid Luna rule result: " + rule + "; using default result");
    }

    private static void validate(Object expression, Set<String> variables) {
        if (expression instanceof Number || expression instanceof Boolean) return;
        if (expression instanceof LispInterpreter.Symbol symbol && variables.contains(symbol.name)) return;
        if (expression instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof LispInterpreter.Symbol op) {
            int arity = switch (op.name) {
                case "if" -> 3;
                case "+", "-", "*", "/", "min", "max", "<", ">", "<=", ">=", "=" -> 2;
                case "not", "abs", "ceil", "floor" -> 1;
                case "and", "or" -> list.size() - 1;
                default -> -1;
            };
            if (arity >= 1 && list.size() == arity + 1) {
                for (int i = 1; i < list.size(); i++) validate(list.get(i), variables);
                return;
            }
        }
        throw new IllegalArgumentException("Invalid Luna expression: " + expression);
    }

    // The shared interpreter tolerates missing parentheses; reject them before parsing this config.
    private static void checkStructure(String source) {
        if (source.length() > 65536) throw new IllegalArgumentException("Luna script exceeds 64 KiB");
        int depth = 0;
        boolean comment = false;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c == '\n') comment = false;
            if (comment) continue;
            if (c == ';') { comment = true; continue; }
            if (c == '"') throw new IllegalArgumentException("Luna formulas use numeric/boolean values, not strings");
            if (c == '(' && ++depth > 64) throw new IllegalArgumentException("Luna script nesting exceeds 64");
            if (c == ')' && --depth < 0) throw new IllegalArgumentException("Unexpected closing parenthesis");
        }
        if (depth != 0) throw new IllegalArgumentException("Unclosed parenthesis");
    }
}
