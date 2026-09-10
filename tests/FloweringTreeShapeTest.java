import java.util.*;
import the_four_primitives_and_weapons.world.tree.FloweringTreeShape;
public class FloweringTreeShapeTest {
    public static void main(String[] args){
        int[][] sides={{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
        for(String species:List.of("kinmokusei","ginmokusei","shidare_ume","tsubaki"))for(int seed=0;seed<100;seed++){
            var tree=FloweringTreeShape.create(species,seed);
            if(!tree.equals(FloweringTreeShape.create(species,seed)))throw new AssertionError("seed");
            var seen=new HashSet<FloweringTreeShape.Pos>();var queue=new ArrayDeque<FloweringTreeShape.Pos>();
            var root=new FloweringTreeShape.Pos(0,0,0);seen.add(root);queue.add(root);
            while(!queue.isEmpty()){var p=queue.remove();for(var d:sides){var q=new FloweringTreeShape.Pos(p.x()+d[0],p.y()+d[1],p.z()+d[2]);
                if(tree.logs().contains(q)&&seen.add(q))queue.add(q);}}
            if(seen.size()!=tree.logs().size())throw new AssertionError("floating branch");
            if(tree.leaves().size()<30||tree.logs().size()+tree.leaves().size()>1200)throw new AssertionError("size");
            for(var entry:tree.leaves().entrySet()){
                var p=entry.getKey();int distance=entry.getValue();boolean supported=false;
                for(var d:sides){var q=new FloweringTreeShape.Pos(p.x()+d[0],p.y()+d[1],p.z()+d[2]);
                    if(distance==1?tree.logs().contains(q):tree.leaves().getOrDefault(q,99)==distance-1)supported=true;}
                if(!supported||distance>6)throw new AssertionError("unsupported leaves");
            }
        }
        System.out.println("400 tree shapes: deterministic, rooted connected branches, supported leaves and bounded size passed.");
    }
}
