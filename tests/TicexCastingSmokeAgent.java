import java.lang.instrument.Instrumentation;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
public class TicexCastingSmokeAgent {
 static Instrumentation inst; static ClassLoader loader;
 static Class<?> cls(String n) throws Exception {Class<?> c=Class.forName(n,true,loader);open(c);return c;}
 static void open(Class<?> c){Module m=c.getModule();if(m.isNamed())inst.redefineModule(m,Set.of(),Map.of(),Map.of(c.getPackageName(),Set.of(TicexCastingSmokeAgent.class.getModule())),Set.of(),Map.of());}
 static Object call(Object o,String n,Class<?>[] types,Object... args)throws Exception{Method m=o.getClass().getMethod(n,types);open(m.getDeclaringClass());m.setAccessible(true);return m.invoke(o,args);}
 static Object field(Class<?> c,String n)throws Exception{Field f=c.getDeclaredField(n);f.setAccessible(true);return f.get(null);}
 static Object id(String s)throws Exception{return cls("net.minecraft.resources.ResourceLocation").getConstructor(String.class).newInstance(s);}
 static Object item(String s)throws Exception{return call(field(cls("net.minecraftforge.registries.ForgeRegistries"),"ITEMS"),"getValue",new Class[]{cls("net.minecraft.resources.ResourceLocation")},id(s));}
 static Object stack(Object item)throws Exception{return cls("net.minecraft.world.item.ItemStack").getConstructor(cls("net.minecraft.world.level.ItemLike")).newInstance(item);}
 static void check(boolean b,String reason){if(!b)throw new AssertionError(reason);}
 static double speed(Object tool)throws Exception {
  Object slot=field(cls("net.minecraft.world.entity.EquipmentSlot"),"MAINHAND");
  Object attributes=call(tool,"getAttributeModifiers",new Class[]{cls("net.minecraft.world.entity.EquipmentSlot")},slot);
  Object speed=field(cls("net.minecraft.world.entity.ai.attributes.Attributes"),"ATTACK_SPEED");
  double amount=0;
  for(Object modifier:(Collection<?>)call(attributes,"get",new Class[]{Object.class},speed))amount+=((Number)call(modifier,"getAmount",new Class[]{})).doubleValue();
  return amount;
 }
 static void test(Object server,Path out){StringBuilder log=new StringBuilder();try{
  Object access=call(server,"registryAccess",new Class[]{}), world=call(server,"overworld",new Class[]{});
  Object manager=call(server,"getRecipeManager",new Class[]{});
  Class<?> loc=cls("net.minecraft.resources.ResourceLocation"), stackType=cls("net.minecraft.world.item.ItemStack"), reg=cls("net.minecraft.core.RegistryAccess");
  Map<String,Object> recipes=new HashMap<>();
  for(String name:new String[]{"part_builder","gold_cast","casting","assembly"}){
   Object recipe=((Optional<?>)call(manager,"byKey",new Class[]{loc},id("the_four_primitives_and_weapons:ticex_katana/"+name))).orElseThrow();
   recipes.put(name,recipe);log.append(name).append('=').append(recipe.getClass().getName()).append('\n');
  }
  Object bladeItem=item("the_four_primitives_and_weapons:ticex_katana_blade");
  check(cls("slimeknights.tconstruct.library.tools.part.ToolPartItem").isInstance(bladeItem),"not a native ToolPartItem");
  Class<?> material=cls("slimeknights.tconstruct.library.materials.definition.MaterialVariantId");
  Object iron=material.getMethod("parse",String.class).invoke(null,"tconstruct:iron");
  Class<?> materialItem=cls("slimeknights.tconstruct.library.tools.part.IMaterialItem");
  Object blade=materialItem.getMethod("withMaterial",stackType,material).invoke(null,stack(bladeItem),iron);
  Object display=cls("the_four_primitives_and_weapons.init.TicexCastingRegistrar").getMethod("displayBlade").invoke(null);
  check(call(display,"getTag",new Class[]{}).toString().contains("tconstruct:iron"),"creative blade has no material");
  Object fake=cls("net.minecraftforge.common.util.FakePlayerFactory").getMethod("getMinecraft",cls("net.minecraft.server.level.ServerLevel")).invoke(null,world);
  Object inventory=call(fake,"getInventory",new Class[]{});
  Object menu=cls("net.minecraft.world.inventory.CraftingMenu").getConstructor(int.class,cls("net.minecraft.world.entity.player.Inventory")).newInstance(0,inventory);
  Field gridField=menu.getClass().getDeclaredField("craftSlots");gridField.setAccessible(true);Object grid=gridField.get(menu);
  call(grid,"setItem",new Class[]{int.class,stackType},0,blade);
  call(grid,"setItem",new Class[]{int.class,stackType},1,stack(item("minecraft:stick")));
  Object assembly=recipes.get("assembly");
  check((Boolean)call(assembly,"matches",new Class[]{cls("net.minecraft.world.inventory.CraftingContainer"),cls("net.minecraft.world.level.Level")},grid,world),"assembly does not match");
  Object result=call(assembly,"assemble",new Class[]{cls("net.minecraft.world.inventory.CraftingContainer"),reg},grid,access);
  Object tag=call(result,"getTag",new Class[]{});check(tag!=null&&tag.toString().contains("tconstruct:iron"),"lost blade material");
  log.append("assembly=").append(result).append(' ').append(tag).append('\n');
  var displays=(List<?>)call(recipes.get("casting"),"getRecipes",new Class[]{reg},access);
  Object ironCasting=null;
  for(Object d:displays){Object output=call(d,"getOutput",new Class[]{});Object t=call(output,"getTag",new Class[]{});if(t!=null&&t.toString().contains("tconstruct:iron")){ironCasting=d;break;}}
  check(ironCasting!=null,"iron casting variant absent");
  Object fluidStack=((List<?>)call(ironCasting,"getFluids",new Class[]{})).get(0);
  check(((Number)call(fluidStack,"getAmount",new Class[]{})).intValue()==270,"wrong casting fluid cost");
  Object fluid=call(fluidStack,"getFluid",new Class[]{}),cast=stack(item("the_four_primitives_and_weapons:ticex_katana_blade_cast"));
  Class<?> casting=cls("slimeknights.tconstruct.library.recipe.casting.ICastingContainer");
  Object goldRecipe=recipes.get("gold_cast");
  Object goldStack=((List<?>)call(goldRecipe,"getFluids",new Class[]{})).get(0);
  check(((Number)call(goldStack,"getAmount",new Class[]{})).intValue()==90,"wrong gold cast cost");
  Object goldFluid=call(goldStack,"getFluid",new Class[]{});
  Object goldContainer=Proxy.newProxyInstance(loader,new Class[]{casting},(proxy,m,a)->{
   if(m.getName().equals("getFluid"))return goldFluid;
   if(m.getName().equals("getStack"))return blade;
   if(m.isDefault())return InvocationHandler.invokeDefault(proxy,m,a);
   throw new UnsupportedOperationException(m.toString());});
  check((Boolean)call(goldRecipe,"matches",new Class[]{casting,cls("net.minecraft.world.level.Level")},goldContainer,world),"gold cast recipe rejects native blade");
  Object producedCast=call(goldRecipe,"getOutput",new Class[]{});
  check(call(producedCast,"getItem",new Class[]{})==item("the_four_primitives_and_weapons:ticex_katana_blade_cast"),"wrong gold cast output");
  check((Boolean)call(goldRecipe,"isConsumed",new Class[]{}),"original blade should be consumed for gold casting");
  log.append("gold cast=90mB, native blade accepted, original consumed\n");
  Object container=Proxy.newProxyInstance(loader,new Class[]{casting},(proxy,m,a)->{
   if(m.getName().equals("getFluid"))return fluid;
   if(m.getName().equals("getStack"))return cast;
   if(m.isDefault())return InvocationHandler.invokeDefault(proxy,m,a);
   throw new UnsupportedOperationException(m.toString());});
  check((Boolean)call(recipes.get("casting"),"matches",new Class[]{casting,cls("net.minecraft.world.level.Level")},container,world),"casting table rejects cast/fluid");
  Object castBlade=call(recipes.get("casting"),"assemble",new Class[]{casting,reg},container,access);
  check(call(castBlade,"getTag",new Class[]{}).toString().contains("tconstruct:iron"),"casting lost material");
  check(!(Boolean)call(ironCasting,"isConsumed",new Class[]{}),"gold cast is consumed");
  Object wrongCast=stack(item("minecraft:stick"));
  Object wrongContainer=Proxy.newProxyInstance(loader,new Class[]{casting},(proxy,m,a)->{
   if(m.getName().equals("getFluid"))return fluid;
   if(m.getName().equals("getStack"))return wrongCast;
   if(m.isDefault())return InvocationHandler.invokeDefault(proxy,m,a);
   throw new UnsupportedOperationException(m.toString());});
  check(!(Boolean)call(recipes.get("casting"),"matches",new Class[]{casting,cls("net.minecraft.world.level.Level")},wrongContainer,world),"invalid cast accepted");
  call(grid,"setItem",new Class[]{int.class,stackType},2,stack(item("minecraft:stick")));
  check(!(Boolean)call(assembly,"matches",new Class[]{cls("net.minecraft.world.inventory.CraftingContainer"),cls("net.minecraft.world.level.Level")},grid,world),"extra ingredient accepted");
  Object building=((Optional<?>)call(manager,"byKey",new Class[]{loc},id("the_four_primitives_and_weapons:ticex_katana/tool_building"))).orElseThrow();
  check(((List<?>)call(building,"getToolParts",new Class[]{})).size()==4,"katana does not have four native parts");
  check(((List<?>)call(building,"getLayoutSlots",new Class[]{})).size()==4,"missing four-part UI layout");
  String[] partIds={"ticex_katana_blade","ticex_tsuka","ticex_tsuba","ticex_kashira"};
  String[] materials={"tconstruct:iron","tconstruct:wood","tconstruct:iron","tconstruct:copper"};
  Object empty=field(stackType,"EMPTY");
  Object[] components=new Object[]{empty,empty,empty,empty,empty};
  for(int index=0;index<4;index++){
   Object materialValue=material.getMethod("parse",String.class).invoke(null,materials[index]);
   components[index]=materialItem.getMethod("withMaterial",stackType,material).invoke(null,stack(item("the_four_primitives_and_weapons:"+partIds[index])),materialValue);
   if(index>1){
    for(String kind:new String[]{"part_builder","gold_cast","casting"}){
     check(((Optional<?>)call(manager,"byKey",new Class[]{loc},id("the_four_primitives_and_weapons:ticex_katana/"+partIds[index].substring(6)+"/"+kind))).isPresent(),"missing fitting recipe "+kind);
    }
   }
  }
  Class<?> stationType=cls("slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer");
  Object station=Proxy.newProxyInstance(loader,new Class[]{stationType},(proxy,m,a)->{
   if(m.getName().equals("getTinkerableStack"))return empty;
   if(m.getName().equals("getInputCount"))return 5;
   if(m.getName().equals("getInput"))return components[(Integer)a[0]];
   if(m.getName().equals("getInputMaterial"))return null;
   if(m.isDefault())return InvocationHandler.invokeDefault(proxy,m,a);
   throw new UnsupportedOperationException(m.toString());});
  check((Boolean)call(building,"matches",new Class[]{stationType,cls("net.minecraft.world.level.Level")},station,world),"native anvil assembly rejected four parts");
  Object katana=call(building,"assemble",new Class[]{stationType,reg},station,access);
  check(call(katana,"getItem",new Class[]{})==item("the_four_primitives_and_weapons:ticex_katana"),"wrong four-part katana output");
  Object toolTag=call(katana,"getTag",new Class[]{});
  Object materialList=call(toolTag,"getList",new Class[]{String.class,int.class},"tic_materials",8);
  check(((Number)call(materialList,"size",new Class[]{})).intValue()==4,"lost component materials");
  for(int index=0;index<4;index++)check(materials[index].equals(call(materialList,"getString",new Class[]{int.class},index)),"wrong material at component "+index);
  check(((Number)call(katana,"getMaxDamage",new Class[]{})).intValue()>0,"native katana durability absent");
  Object traits=call(toolTag,"getList",new Class[]{String.class,int.class},"tic_modifiers",10);
  check(((Number)call(traits,"size",new Class[]{})).intValue()>0,"native material traits absent");
  log.append("material traits=").append(traits).append('\n');
  log.append("four-part native katana=").append(materialList).append(" durability=").append(call(katana,"getMaxDamage",new Class[]{})).append('\n');
  Object woodenRecipe=((Optional<?>)call(manager,"byKey",new Class[]{loc},id("the_four_primitives_and_weapons:ticex_katana/tsuka/crafting"))).orElseThrow();
  for(int slot=0;slot<9;slot++)call(grid,"setItem",new Class[]{int.class,stackType},slot,empty);
  for(String planks:new String[]{"minecraft:oak_planks","minecraft:spruce_planks"}){
   call(grid,"setItem",new Class[]{int.class,stackType},0,stack(item(planks)));
   call(grid,"setItem",new Class[]{int.class,stackType},3,stack(item(planks)));
   check((Boolean)call(woodenRecipe,"matches",new Class[]{cls("net.minecraft.world.inventory.CraftingContainer"),cls("net.minecraft.world.level.Level")},grid,world),"wooden handle crafting rejected "+planks);
   Object handle=call(woodenRecipe,"assemble",new Class[]{cls("net.minecraft.world.inventory.CraftingContainer"),reg},grid,access);
   check(call(handle,"getTag",new Class[]{}).toString().contains("tconstruct:wood"),"crafted handle lost wood material");
   components[1]=handle;
  }
  for(String oldRecipe:new String[]{"casting","gold_cast","part_builder"})check(((Optional<?>)call(manager,"byKey",new Class[]{loc},id("the_four_primitives_and_weapons:ticex_katana/tsuka/"+oldRecipe))).isEmpty(),"obsolete handle recipe remains");
  log.append("wooden handle crafting accepts oak/spruce planks; metal casting and pattern recipes removed\n");
  Object[] savedParts=components.clone();
  for(String metal:new String[]{"iron","cobalt","manyullyn","hepatizon","rose_gold"}){
   String materialId="tconstruct:"+metal;
   Object metalId=material.getMethod("parse",String.class).invoke(null,materialId);
   for(int index=0;index<4;index++){
    if(index==1)continue; // Main handle is crafted from wood only.
    String path=index==0?"casting":partIds[index].substring(6)+"/casting";
    Object recipe=((Optional<?>)call(manager,"byKey",new Class[]{loc},id("the_four_primitives_and_weapons:ticex_katana/"+path))).orElseThrow();
    Object variant=null;
    for(Object candidate:(List<?>)call(recipe,"getRecipes",new Class[]{reg},access)){
     Object output=call(candidate,"getOutput",new Class[]{});
     Object nbt=call(output,"getTag",new Class[]{});
     if(nbt!=null&&nbt.toString().contains("\""+materialId+"\"")){variant=candidate;break;}
    }
    check(variant!=null,"missing metal casting "+materialId+" / "+partIds[index]);
    Object metalFluid=call(((List<?>)call(variant,"getFluids",new Class[]{})).get(0),"getFluid",new Class[]{});
    Object metalCast=stack(item("the_four_primitives_and_weapons:"+partIds[index]+"_cast"));
    Object metalContainer=Proxy.newProxyInstance(loader,new Class[]{casting},(proxy,m,a)->{
     if(m.getName().equals("getFluid"))return metalFluid;
     if(m.getName().equals("getStack"))return metalCast;
     if(m.isDefault())return InvocationHandler.invokeDefault(proxy,m,a);
     throw new UnsupportedOperationException(m.toString());});
    check((Boolean)call(recipe,"matches",new Class[]{casting,cls("net.minecraft.world.level.Level")},metalContainer,world),"metal casting rejected "+materialId);
    components[index]=call(recipe,"assemble",new Class[]{casting,reg},metalContainer,access);
    check(call(components[index],"getTag",new Class[]{}).toString().contains(materialId),"casting lost metal "+materialId);
   }
   check((Boolean)call(building,"matches",new Class[]{stationType,cls("net.minecraft.world.level.Level")},station,world),"metal assembly rejected "+materialId);
   Object metalTool=call(building,"assemble",new Class[]{stationType,reg},station,access);
   check(((Number)call(metalTool,"getMaxDamage",new Class[]{})).intValue()>0,"missing metal durability");
   log.append("metal casting and four-part assembly PASS: ").append(materialId).append('\n');
  }
  System.arraycopy(savedParts,0,components,0,components.length);
  Object pommel=components[3];components[3]=empty;
  check(!(Boolean)call(building,"matches",new Class[]{stationType,cls("net.minecraft.world.level.Level")},station,world),"missing pommel accepted");
  components[3]=pommel;Object guard=components[2];components[2]=stack(item("minecraft:stick"));
  check(!(Boolean)call(building,"matches",new Class[]{stationType,cls("net.minecraft.world.level.Level")},station,world),"wrong guard accepted");
  components[2]=guard;
  log.append("four part recipes and layout loaded; missing/wrong parts rejected\n");
  log.append("casting=iron, 270mB, reusable cast, material retained\ninvalid cast and extra assembly ingredient rejected\nPASS\n");
 }catch(Throwable e){log.append("FAIL\n").append(e);for(Throwable c=e.getCause();c!=null;c=c.getCause())log.append("\ncaused by ").append(c);}
 try{Files.writeString(out,log.toString());}catch(Exception e){throw new RuntimeException(e);}}
 public static void agentmain(String output,Instrumentation instrumentation)throws Exception{
  inst=instrumentation;
  Class<?> hooks=Arrays.stream(inst.getAllLoadedClasses()).filter(c->c.getName().equals("net.minecraftforge.server.ServerLifecycleHooks")).findFirst().orElseThrow();loader=hooks.getClassLoader();open(hooks);
  Object server=hooks.getMethod("getCurrentServer").invoke(null);
  call(server,"execute",new Class[]{Runnable.class},(Runnable)()->test(server,Path.of(output)));
 }
 public static void main(String[] args)throws Exception{var vm=com.sun.tools.attach.VirtualMachine.attach(args[0]);try{vm.loadAgent(args[1],args[2]);}finally{vm.detach();}}
}
