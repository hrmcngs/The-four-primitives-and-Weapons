const assert = require('node:assert/strict');
const fs = require('node:fs');
const root='src/main/resources/';
const read=p=>JSON.parse(fs.readFileSync(root+p));
const types=read('data/the_four_primitives_and_weapons/weapon_types/weapon_types.json').types;
const stats=read('data/the_four_primitives_and_weapons/weapon_stats/weapon_stats.json').weapons;
const saya=read('data/the_four_primitives_and_weapons/maw_saya/main.json');
function model(name) {
 const p='assets/the_four_primitives_and_weapons/models/'+name+'.json';
 const d=read(p);
 const parent=d.parent&&d.parent.startsWith('the_four_primitives_and_weapons:')?model(d.parent.split(':')[1]):{};
 return {...parent,...d,textures:{...parent.textures,...d.textures}};
}
for(const kind of ['katana','sword','rapier','dagger','tyokuto']) {
 const id='the_four_primitives_and_weapons:copper_'+kind;
 assert.ok(types[kind==='tyokuto'?'straight_sword':kind].items.includes(id));
 assert.ok(saya[kind==='tyokuto'?'straight_sword' in saya?'straight_sword':'tyokuto':kind][id]);
 assert.equal(stats[id].durability,180);
 const recipe=read('data/the_four_primitives_and_weapons/recipes/copper_'+kind+'.json');
 assert.equal(recipe.result.item,id);
 assert.equal(recipe.key.C.item,'minecraft:copper_ingot');
 const d=model('item/copper_'+kind);
 assert.ok(d.elements.length>0);
 assert.ok(d.elements.some(e=>Object.values(e.faces).some(f=>f.tintindex===6)),kind+' must show copper blade tint');
 for(const e of d.elements) for(const f of Object.values(e.faces)) assert.ok(d.textures[f.texture.slice(1)],kind+' missing texture');
 for(const lang of ['ja_jp','en_us']) assert.ok(read('assets/the_four_primitives_and_weapons/lang/'+lang+'.json')['item.the_four_primitives_and_weapons.copper_'+kind]);
}
console.log('Five copper weapons: recipes, stats, types, sheathing, models, color and names passed');
