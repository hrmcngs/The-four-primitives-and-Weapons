package the_four_primitives_and_weapons.util;

/** Guard outline in the plane normal to the blade, with its edge-to-spine direction supplied. */
public final class TsubaOutline {
    private TsubaOutline() {}
    public static boolean supported(String shape) {
        return "oval".equals(shape)||"diamond".equals(shape)||"circle".equals(shape)
            ||"square".equals(shape)||"octagon".equals(shape)||"mokko".equals(shape)||"hexagon".equals(shape);
    }
    /** Four distinct corners per cap cell, avoiding degenerate triangles in shader tangent calculations. */
    public static double[][] capCell(double[][] outline, int index) {
        double[] previous=outline[(index+outline.length-1)%outline.length];
        double[] current=outline[index], next=outline[(index+1)%outline.length];
        return new double[][]{{0,0},{(previous[0]+current[0])/2,(previous[1]+current[1])/2},
            current.clone(),{(current[0]+next[0])/2,(current[1]+next[1])/2}};
    }
    /** Map the generated geometry's bounds into the source face's UV rectangle. */
    public static double uvFraction(double value, double min, double max) {
        return max<=min ? .5 : Math.max(0,Math.min(1,(value-min)/(max-min)));
    }
    public static double[][] points(String shape,double radius,boolean edgeAlongU) {
        if(!supported(shape)||!Double.isFinite(radius)||radius<=0)throw new IllegalArgumentException("Invalid guard shape or radius");
        int count=switch(shape){case "diamond", "square" -> 4; case "octagon" -> 8; case "hexagon" -> 6; default -> 32;};
        double ru=radius,rv=radius;
        if("oval".equals(shape)){if(edgeAlongU)rv*=.76;else ru*=.76;}
        double[][] points=new double[count][2];
        for(int i=0;i<count;i++) {
            double angle=2*Math.PI*i/count + ("square".equals(shape)?Math.PI/4:0);
            double scale="mokko".equals(shape)? .86+.14*Math.cos(4*angle):1;
            points[i][0]=scale*ru*Math.cos(angle);points[i][1]=scale*rv*Math.sin(angle);
        }
        return points;
    }
}
