// Regression checks for solid fallback geometry and unchanged UVs in fitting-enabled models.
const assert = require('node:assert/strict');
const fs = require('node:fs');
const {execFileSync} = require('node:child_process');
const root = 'src/main/resources/assets/the_four_primitives_and_weapons/models/';
const models = ['sword','knife','shield','trident','crossbow','bow0','bow1','bow2','bow3'];
for (const name of models) {
  const model = JSON.parse(fs.readFileSync(root+'ticex/'+name+'.json', 'utf8'));
  // A generated/handheld parent discards solid elements when Minecraft bakes the model.
  assert.equal(model.parent, 'minecraft:block/block', name+' must bake as solid geometry');
  assert.ok(model.elements.length > 0);
  for (const e of model.elements) {
    for (let axis=0;axis<3;axis++) assert.ok(Number.isFinite(e.from[axis]) && e.to[axis] > e.from[axis], name+' has a flat or invalid part');
    assert.equal(Object.keys(e.faces).length, 6);
    for (const face of Object.values(e.faces)) assert.ok(model.textures[face.texture.slice(1)], 'Missing texture');
  }
  assert.ok(model.display.firstperson_righthand && model.display.thirdperson_lefthand, 'Both hands must have transforms');
}
const tagged = ['weapon/katana/katana_b_parent','swordb','swordb2','kurikarakatana3d','katananiguzyouwankotu3d'];
function withoutTints(obj) {
  if (Array.isArray(obj)) return obj.map(withoutTints);
  if (obj && typeof obj==='object') return Object.fromEntries(Object.entries(obj).filter(([k])=>k!=='tintindex').map(([k,v])=>[k,withoutTints(v)]));
  return obj;
}
for (const name of tagged) {
  const path=root+'custom/'+name+'.json';
  const after=JSON.parse(fs.readFileSync(path, 'utf8'));
  const before=JSON.parse(execFileSync('git',['show','HEAD:'+path],{encoding:'utf8'}));
  assert.deepEqual(withoutTints(after),withoutTints(before),'UV/geometry/display changed: '+name);
  assert.ok(after.elements.some(e=>Object.values(e.faces).some(f=>f.tintindex===2)), 'Guard cannot be replaced: '+name);
}
const readRecipe = name => JSON.parse(fs.readFileSync('src/main/resources/data/the_four_primitives_and_weapons/recipes/'+name+'.json'));
// Evaluate Forge mod-loaded conditions for both supported installations and absence.
const recipeDir = 'src/main/resources/data/the_four_primitives_and_weapons/recipes/';
const integrationRecipes = ['ticex_weapon', 'ticex_weapon_pattern'];
function collectRecipes(dir, prefix = '') {
  for (const entry of fs.readdirSync(dir, {withFileTypes: true})) {
    if (entry.isDirectory()) collectRecipes(dir+'/'+entry.name, prefix+entry.name+'/');
    else if (entry.name.endsWith('.json')) integrationRecipes.push('ticex_katana/'+prefix+entry.name.slice(0,-5));
  }
}
collectRecipes(recipeDir+'ticex_katana');
for (const name of integrationRecipes) {
  const recipe = readRecipe(name);
  for (const [mods, expected] of [
    [[], false], [['ticex'], false], [['tconstruct'], true], [['tconstruct','ticex'], true]
  ]) {
    assert.equal(recipe.conditions.every(c => c.type === 'forge:mod_loaded' && mods.includes(c.modid)),
      expected, name+' with '+mods.join(','));
  }
}
for (const name of ['casting', 'tsuba/casting', 'kashira/casting']) {
  const recipe = readRecipe('ticex_katana/'+name);
  assert.equal(recipe.type, 'tconstruct:table_casting_material');
  assert.equal(recipe.fluid, undefined, 'Casting must resolve registered materials, not a fixed metal');
}
assert.deepEqual(readRecipe('ticex_weapon_pattern').ingredients.map(i=>i.item).sort(), ['minecraft:paper','minecraft:stick']);
for(const lang of ['ja_jp','en_us']) {
  const translations=JSON.parse(fs.readFileSync('src/main/resources/assets/the_four_primitives_and_weapons/lang/'+lang+'.json'));
  for(const key of ['item.the_four_primitives_and_weapons.ticex_weapon_pattern','gui.the_four_primitives_and_weapons.koshirae.tsuba.oval','gui.the_four_primitives_and_weapons.koshirae.tsuba.diamond','gui.the_four_primitives_and_weapons.koshirae.tsuba.circle']) assert.ok(translations[key]);
}
console.log('TicEX 3D geometry, guard UV preservation, recipe and language checks passed');
