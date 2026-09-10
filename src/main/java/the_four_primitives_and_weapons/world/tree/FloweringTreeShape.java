package the_four_primitives_and_weapons.world.tree;

import java.util.*;

/** Pure geometry: rooted branches, clustered crowns and supported hanging leaves. */
public final class FloweringTreeShape {
    public record Pos(int x,int y,int z) {
        Pos add(int x,int y,int z){return new Pos(this.x+x,this.y+y,this.z+z);}
    }
    public record Shape(Set<Pos> logs,Map<Pos,Integer> leaves) {}
    private static final int[][] SIDES={{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
    public static Shape create(String species,long seed) {
        Random random=new Random(seed);
        boolean weeping=species.equals("shidare_ume"), compact=species.equals("tsubaki");
        int height=(compact?4:6)+random.nextInt(3);
        Set<Pos> logs=new LinkedHashSet<>(), foliage=new LinkedHashSet<>();
        Pos trunk=new Pos(0,0,0);
        for(int y=0;y<height;y++)logs.add(new Pos(0,y,0));
        crown(foliage,new Pos(0,height-1,0),compact?2:3,2,random);
        int branches=5+random.nextInt(3);
        for(int i=0;i<branches;i++) {
            double angle=2*Math.PI*(i+random.nextDouble()*0.45)/branches;
            int radius=(weeping?4:2)+random.nextInt(2),start=height-3-random.nextInt(2);
            Pos previous=new Pos(0,start,0);
            for(int step=1;step<=radius;step++) {
                int rise=weeping?(step<3?1:0):step/2;
                Pos next=new Pos((int)Math.round(Math.cos(angle)*step),start+rise,(int)Math.round(Math.sin(angle)*step));
                connect(logs,previous,next);previous=next;
            }
            crown(foliage,previous.add(0,1,0),2,compact?1:2,random);
            if(weeping) for(int d=0;d<3;d++) {
                Pos tip=previous.add((int)Math.signum(previous.x),-d,(int)Math.signum(previous.z));
                foliage.add(tip);foliage.add(tip.add(0,0,1));
            }
        }
        // Distances follow actual log support; no persistent floating leaf curtains.
        Map<Pos,Integer> distance=new LinkedHashMap<>();ArrayDeque<Pos> queue=new ArrayDeque<>(logs);
        Map<Pos,Integer> all=new HashMap<>();logs.forEach(p->all.put(p,0));
        while(!queue.isEmpty()) {
            Pos p=queue.remove();int next=all.get(p)+1;if(next>6)continue;
            for(int[] side:SIDES){Pos q=p.add(side[0],side[1],side[2]);
                if(foliage.contains(q)&&!all.containsKey(q)){all.put(q,next);distance.put(q,next);queue.add(q);}}
        }
        return new Shape(Collections.unmodifiableSet(logs),Collections.unmodifiableMap(distance));
    }
    private static void connect(Set<Pos> logs,Pos from,Pos to){
        int x=from.x,y=from.y,z=from.z;logs.add(from);
        while(x!=to.x||y!=to.y||z!=to.z){
            if(x!=to.x){x+=Integer.signum(to.x-x);logs.add(new Pos(x,y,z));}
            if(z!=to.z){z+=Integer.signum(to.z-z);logs.add(new Pos(x,y,z));}
            if(y!=to.y){y+=Integer.signum(to.y-y);logs.add(new Pos(x,y,z));}
        }
    }
    private static void crown(Set<Pos> leaves,Pos center,int radius,int height,Random random){
        for(int x=-radius;x<=radius;x++)for(int z=-radius;z<=radius;z++)for(int y=-height;y<=height;y++){
            double edge=(x*x+z*z)/(double)(radius*radius)+y*y/(double)(height*height+1);
            if(edge<=1.15 && (edge<0.8||random.nextDouble()>0.15))leaves.add(center.add(x,y,z));
        }
    }
    private FloweringTreeShape(){}
}
