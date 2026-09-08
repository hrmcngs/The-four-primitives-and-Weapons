package the_four_primitives_and_weapons.util;

/** 上向きから下向きへの振り下ろし。サーバーの使用tickで判定する。 */
public final class NinjatoVaultGesture {
    private int raisedAt = -1;

    public boolean update(int tick, float pitch) {
        if (!Float.isFinite(pitch)) return false;
        if (pitch <= -35.0F) raisedAt = tick;
        if (raisedAt < 0) return false;
        if (tick - raisedAt > 10 || tick < raisedAt) {
            raisedAt = -1;
            return false;
        }
        if (pitch >= 55.0F) {
            raisedAt = -1;
            return true;
        }
        return false;
    }
}
