import the_four_primitives_and_weapons.util.TsubaOutline;

public final class TsubaOutlineTest {
    private static void near(double a,double b){if(Math.abs(a-b)>1e-8)throw new AssertionError(a+" != "+b);}
    private static double area(double[][] p){double a=0;for(int i=0;i<p.length;i++){var q=p[(i+1)%p.length];a+=p[i][0]*q[1]-q[0]*p[i][1];}return a/2;}
    public static void main(String[] args){
        var circle=TsubaOutline.points("circle",1,false);
        for(var p:circle)near(p[0]*p[0]+p[1]*p[1],1);
        var oval=TsubaOutline.points("oval",1,true);
        near(oval[0][0],1);near(oval[8][1],.76);
        oval=TsubaOutline.points("oval",1,false);
        near(oval[0][0],.76);near(oval[8][1],1);
        var diamond=TsubaOutline.points("diamond",1,true);
        if(diamond.length!=4)throw new AssertionError("Diamond must have four corners");
        near(diamond[0][0],1);near(diamond[1][1],1);near(area(diamond),2);
        if(area(circle)<=3||area(TsubaOutline.points("oval",1,true))<=2)throw new AssertionError("Bad winding or contour");
        for(String style:new String[]{"square","octagon","mokko","hexagon"}) {
            var points=TsubaOutline.points(style,1,true);
            if(area(points)<=1.8)throw new AssertionError("Degenerate or reversed "+style);
            for(var point:points)if(!Double.isFinite(point[0])||Math.hypot(point[0],point[1])>1.000001)
                throw new AssertionError("Contour exceeds guard bounds");
        }
        if(TsubaOutline.points("octagon",1,true).length!=8)throw new AssertionError("Octagon corners missing");
        if(TsubaOutline.points("hexagon",1,true).length!=6)throw new AssertionError("Hexagon corners missing");
        var square=TsubaOutline.points("square",1,true);
        near(square[0][0],square[0][1]);
        var mokko=TsubaOutline.points("mokko",1,true);
        if(Math.hypot(mokko[4][0],mokko[4][1])>.8)throw new AssertionError("Mokko indent missing");
        // Every shader-facing quad needs a nonzero first triangle and complete cap coverage.
        for(String style:new String[]{"circle","oval","diamond","square","octagon","mokko","hexagon"}) {
            double[][] outline=TsubaOutline.points(style,1.05,true);
            double cellsArea=0;
            double minU=Double.POSITIVE_INFINITY,maxU=Double.NEGATIVE_INFINITY;
            double minV=minU,maxV=maxU;
            for(double[] point:outline) {
                minU=Math.min(minU,point[0]);maxU=Math.max(maxU,point[0]);
                minV=Math.min(minV,point[1]);maxV=Math.max(maxV,point[1]);
            }
            for(int i=0;i<outline.length;i++) {
                double[][] cell=TsubaOutline.capCell(outline,i);
                cellsArea+=area(cell);
                double cross=cell[1][0]*cell[2][1]-cell[1][1]*cell[2][0];
                if(cross<=1e-8)throw new AssertionError("Degenerate shader triangle: "+style);
                for(double[] point:cell) {
                    double u=TsubaOutline.uvFraction(point[0],minU,maxU);
                    double v=TsubaOutline.uvFraction(point[1],minV,maxV);
                    if(!Double.isFinite(u)||!Double.isFinite(v)||u<0||u>1||v<0||v>1)
                        throw new AssertionError("UV escapes the source face: "+style);
                }
            }
            near(cellsArea,area(outline));
        }
        // Iron guard is 1.6 x 2.1: its new contour exceeds the original narrow UV projection.
        if((1.05+.8)/1.6<=1)throw new AssertionError("Regression fixture must exceed the old bounds");
        near(TsubaOutline.uvFraction(1.05,-1.05,1.05),1);
        near(TsubaOutline.uvFraction(-1.05,-1.05,1.05),0);
        if(TsubaOutline.supported("guard_b"))throw new AssertionError("Rapier guard changed");
        for(double radius:new double[]{0,-1,Double.NaN,Double.POSITIVE_INFINITY}) {
            try{TsubaOutline.points("circle",radius,true);throw new AssertionError("Invalid radius accepted");}
            catch(IllegalArgumentException expected){}
        }
        System.out.println("Tsuba outline tests passed");
    }
}
