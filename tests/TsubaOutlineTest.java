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
        if(TsubaOutline.supported("guard_b"))throw new AssertionError("Rapier guard changed");
        for(double radius:new double[]{0,-1,Double.NaN,Double.POSITIVE_INFINITY}) {
            try{TsubaOutline.points("circle",radius,true);throw new AssertionError("Invalid radius accepted");}
            catch(IllegalArgumentException expected){}
        }
        System.out.println("Tsuba outline tests passed");
    }
}
