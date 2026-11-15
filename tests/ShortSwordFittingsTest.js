const assert = require('node:assert/strict');
const fs = require('node:fs');
const {execFileSync} = require('node:child_process');
const root = 'src/main/resources/assets/the_four_primitives_and_weapons/models/';
function stripTints(value) {
  if (Array.isArray(value)) return value.map(stripTints);
  if (value && typeof value === 'object') return Object.fromEntries(Object.entries(value)
    .filter(([key]) => key !== 'tintindex').map(([key,v]) => [key,stripTints(v)]));
  return value;
}
for (const [item, model, handleCount] of [['small_sword','kodachiiron3d',3], ['warabitetou','warabite3d',4]]) {
  const path = root+'custom/'+model+'.json';
  const before = JSON.parse(execFileSync('git',['show','HEAD:'+path],{encoding:'utf8'}));
  const after = JSON.parse(fs.readFileSync(path));
  assert.deepEqual(stripTints(after), before, 'Default shape, texture UVs and display transforms must remain unchanged');
  const parts = after.elements.map(e => Object.values(e.faces)[0].tintindex);
  assert.equal(parts.filter(t=>t===1).length,handleCount);
  assert.equal(parts.filter(t=>t===2).length,1);
  assert.ok(parts.includes(6),'Blade must be independently colored');
  for (const e of after.elements) assert.equal(new Set(Object.values(e.faces).map(f=>f.tintindex)).size,1);
  const itemModel = JSON.parse(fs.readFileSync(root+'item/'+item+'.json'));
  for (const key of ['0','1','3','4']) assert.ok(itemModel.textures[key],'Fitting renderer must discover short swords');
}
console.log('Short sword fitting masks preserve default geometry, UVs and transforms');
