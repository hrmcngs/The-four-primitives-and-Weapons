package the_four_primitives_and_weapons.util;

/** Guard outline in the plane normal to the blade, with its edge-to-spine direction supplied. */
public final class TsubaOutline {
    private TsubaOutline() {}
    public static boolean supported(String shape) {
        return "oval".equals(shape)||"diamond".equals(shape)||"circle".equals(shape);
    }
    public static double[][] points(String shape,double radius,boolean edgeAlongU) {
        if(!supported(shape)||!Double.isFinite(radius)||radius<=0)throw new IllegalArgumentException("Invalid guard shape or radius");
        int count="diamond".equals(shape)?4:32;
        double ru=radius,rv=radius;
        if("oval".equals(shape)){if(edgeAlongU)rv*=.76;else ru*=.76;}
        double[][] points=new double[count][2];
        for(int i=0;i<count;i++) {
            double angle=2*Math.PI*i/count;
            points[i][0]=ru*Math.cos(angle);points[i][1]=rv*Math.sin(angle);
        }
        return points;
    }
}
