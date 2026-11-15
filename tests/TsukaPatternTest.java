import the_four_primitives_and_weapons.util.TsukaPattern;

public final class TsukaPatternTest {
    public static void main(String[] args) {
        for (String style : new String[]{"hineri", "katate", "hira", "spiral", "basket"}) {
            int exposed = 0, cord = 0;
            // Sample the actual handle mesh resolution: all designs must show both selected colors.
            for (int along=0;along<36;along++) for(int across=0;across<8;across++) {
                if(TsukaPattern.underlay(style,(across+.5)/8,(along+.5)/36)) exposed++; else cord++;
            }
            if(exposed<10 || cord<10) throw new AssertionError(style+" loses one of the colors");
        }
        if(TsukaPattern.underlay("spiral",.2,.03)==TsukaPattern.underlay("spiral",.8,.03))
            throw new AssertionError("Spiral must run diagonally across the handle");
        if(!TsukaPattern.underlay("hineri",.5,1.0/12)) throw new AssertionError("Diamond opening missing");
        if(TsukaPattern.underlay("hineri",0,1.0/12)) throw new AssertionError("Edge must retain cord");
        if(!TsukaPattern.underlay("hineri",.5,5.5/6)) throw new AssertionError("End opening missing");
        if(TsukaPattern.underlay("katate",.5,3.5/6)) throw new AssertionError("Katate center must be bound");
        if(!TsukaPattern.underlay("katate",.5,.5/6)) throw new AssertionError("Katate end must expose underlay");
        if(TsukaPattern.supported("tuka_red")) throw new AssertionError("Legacy texture must remain unchanged");
        System.out.println("Wrap pattern visibility and style checks passed");
    }
}
