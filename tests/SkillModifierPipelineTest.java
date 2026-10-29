import java.util.*;
import the_four_primitives_and_weapons.integration.SkillModifierPipeline;
public class SkillModifierPipelineTest {
 public static void main(String[] args) throws Exception {
  List<String> order=new ArrayList<>();
  var hooks=new SkillModifierPipeline.Hooks(){
   public float damage(float base){order.add("damage");return (base+3)*2;}
   public void before(float damage){order.add("before");check(damage==26,"modified damage passed to hit hooks");}
   public void after(float damage){order.add("after");}
   public void failed(float damage){order.add("failed");}
  };
  check(SkillModifierPipeline.hit(10,hooks,damage->{order.add("hit");check(damage==26,"trait damage reaches actual hit");return true;}),"accepted hit");
  check(order.equals(List.of("damage","before","hit","after")),"one successful native lifecycle");
  order.clear();
  check(!SkillModifierPipeline.hit(10,hooks,damage->{order.add("hit");return false;}),"rejected hit");
  check(order.equals(List.of("damage","before","hit","failed")),"blocked hits cannot trigger successful-hit traits");
  order.clear();
  var recursive=new SkillModifierPipeline.Hooks(){
   public float damage(float base){order.add("damage");return base;}
   public void before(float damage){}
   public void after(float damage) throws ReflectiveOperationException {
    order.add("after");
    SkillModifierPipeline.hit(10,this,nested->{order.add("secondary");return true;});
   }
   public void failed(float damage){}
  };
  SkillModifierPipeline.hit(10,recursive,damage->true);
  check(order.equals(List.of("damage","after","secondary")),"trait-generated damage must not recursively duplicate hooks");
  check(!SkillModifierPipeline.active(),"guard cleared after hit");
  var failure=new SkillModifierPipeline.Hooks(){
   public float damage(float base) throws ReflectiveOperationException{throw new ReflectiveOperationException("addon failure");}
   public void before(float damage){}
   public void after(float damage){}
   public void failed(float damage){}
  };
  try{SkillModifierPipeline.hit(10,failure,damage->{throw new AssertionError("hit not yet allowed");});throw new AssertionError("exception swallowed");}
  catch(ReflectiveOperationException expected){}
  check(!SkillModifierPipeline.active(),"guard cleared after failing addon hook");
  int[] actualHits={0};
  var postFailure=new SkillModifierPipeline.Hooks(){
   public float damage(float base){return base;}
   public void before(float damage){}
   public void after(float damage) throws ReflectiveOperationException{throw new ReflectiveOperationException("post-hit addon failure");}
   public void failed(float damage){}
  };
  try{SkillModifierPipeline.hit(10,postFailure,damage->{actualHits[0]++;return true;});}
  catch(ReflectiveOperationException expected){}
  check(actualHits[0]==1,"post-hit errors cannot repeat the actual damage");
  check(!SkillModifierPipeline.active(),"post-hit error clears recursion guard");
  order.clear();SkillModifierPipeline.hit(10,hooks,damage->true);
  check(order.equals(List.of("damage","before","after")),"next target still gets its own traits");
  System.out.println("Skill traits: damage, successful/blocked hit lifecycle, recursion and error cleanup passed");
 }
 static void check(boolean result,String message){if(!result)throw new AssertionError(message);}
}
