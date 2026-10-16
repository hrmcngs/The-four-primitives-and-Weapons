// Native pixel asset and software mesh preview. No image generation or external packages.
// Run from the repository root: node tools/assets/fantasy-mantle.cjs
const fs = require('fs');
const zlib = require('zlib');
const path = require('path');
const asset = 'src/main/resources/assets/the_four_primitives_and_weapons';
const output = 'output/mantle';
fs.mkdirSync(output, {recursive:true});
function crc32(buf) { let c=0xffffffff; for(const x of buf){ c^=x; for(let k=0;k<8;k++) c=(c>>>1)^((c&1)?0xedb88320:0); } return (c^0xffffffff)>>>0; }
function png(filename,w,h,pixels) {
 const chunk=(tag,bytes)=>{const t=Buffer.from(tag),l=Buffer.alloc(4),c=Buffer.alloc(4);l.writeUInt32BE(bytes.length);c.writeUInt32BE(crc32(Buffer.concat([t,bytes])));return Buffer.concat([l,t,bytes,c]);};
 const ih=Buffer.alloc(13);ih.writeUInt32BE(w,0);ih.writeUInt32BE(h,4);ih[8]=8;ih[9]=6;
 const raw=Buffer.alloc((w*4+1)*h);for(let y=0;y<h;y++) Buffer.from(pixels.slice(y*w*4,(y+1)*w*4)).copy(raw,y*(w*4+1)+1);
 fs.writeFileSync(filename,Buffer.concat([Buffer.from([137,80,78,71,13,10,26,10]),chunk('IHDR',ih),chunk('IDAT',zlib.deflateSync(raw)),chunk('IEND',Buffer.alloc(0))]));
}
const shades=[[12,14,18],[18,21,26],[25,29,35],[34,39,46]];
const tex=new Uint8Array(64*64*4);
for(let y=0;y<64;y++)for(let x=0;x<64;x++) {
 // Broad stepped folds, quiet lower-edge stitching; no random speckle or symbols.
 const wave=(x+Math.floor(y/16))%16;
 let tone=wave<3?0:wave<8?1:wave<13?2:1;
 if(y<2 && tone===2)tone=3;
 if(y===62 && x%4<2)tone=Math.min(2,tone+1);
 const p=(y*64+x)*4;tex.set([...shades[tone],255],p);
}
png(`${asset}/textures/models/armor/fantasy_mantle.png`,64,64,tex);
fs.writeFileSync(`${asset}/textures/models/armor/fantasy_mantle.png.mcmeta`,JSON.stringify({texture:{blur:false,clamp:true}},null,2)+'\n');
const elements=[];
const boxItem=(from,to,uv=[0,0,16,16])=>elements.push({from,to,faces:Object.fromEntries(['north','south','east','west','up','down'].map(f=>[f,{uv,texture:'#cloth'}]))});
boxItem([3,13,7],[13,15,9]);
for(let i=0;i<7;i++)boxItem([1+i*2,1,8],[3.2+i*2,13,9]);
const item={parent:'minecraft:block/block',textures:{cloth:'the_four_primitives_and_weapons:models/armor/fantasy_mantle',particle:'the_four_primitives_and_weapons:models/armor/fantasy_mantle'},elements,display:{gui:{rotation:[15,150,0],scale:[.85,.85,.85]},ground:{translation:[0,2,0],scale:[.4,.4,.4]},thirdperson_righthand:{rotation:[0,90,0],scale:[.5,.5,.5]},firstperson_righthand:{rotation:[0,150,0],scale:[.55,.55,.55]}}};
fs.writeFileSync(`${asset}/models/item/fantasy_mantle.json`,JSON.stringify(item,null,2)+'\n');
// Mesh preview uses the same dimensions, pivots and rest rotations as FantasyMantleRenderer.
const tris=[];
const rotate=(v,r)=>{let[x,y,z]=v;let c=Math.cos(r[2]),s=Math.sin(r[2]);[x,y]=[c*x-s*y,s*x+c*y];c=Math.cos(r[1]);s=Math.sin(r[1]);[x,z]=[c*x+s*z,-s*x+c*z];c=Math.cos(r[0]);s=Math.sin(r[0]);return[x,c*y-s*z,s*y+c*z];};
const transform=(v,p,r)=>rotate(v,r).map((n,k)=>n+p[k]);
function cube(a,b,transforms,color,uvRect=null) {
 const [x,y,z]=a,[X,Y,Z]=b;
 const faces=[[[x,y,Z],[X,y,Z],[X,Y,Z],[x,Y,Z]],[[X,y,z],[x,y,z],[x,Y,z],[X,Y,z]],[[x,y,z],[x,y,Z],[x,Y,Z],[x,Y,z]],[[X,y,Z],[X,y,z],[X,Y,z],[X,Y,Z]],[[x,y,z],[X,y,z],[X,y,Z],[x,y,Z]],[[x,Y,Z],[X,Y,Z],[X,Y,z],[x,Y,z]]];
 const rect=uvRect||[0,0,1,1]; const uv=[[rect[0],rect[1]],[rect[2],rect[1]],[rect[2],rect[3]],[rect[0],rect[3]]];
 for(let f=0;f<6;f++) {const verts=faces[f].map(v=>transforms.reduce((p,t)=>transform(p,t.p,t.r),v));for(const ids of [[0,1,2],[0,2,3]])tris.push({v:ids.map(i=>verts[i]),uv:ids.map(i=>uv[i]),color,shade:[1,.73,.79,.92,1.08,.65][f]});}
}
const mannequin=[99,109,118];
cube([-4,-8,-4],[4,0,4],[],mannequin);
cube([-4,0,-2],[4,12,2],[],mannequin);
cube([-8,0,-2],[-4,12,2],[],mannequin);cube([4,0,-2],[8,12,2],[],mannequin);
cube([-3.9,12,-2],[-.1,24,2],[],mannequin);cube([.1,12,-2],[3.9,24,2],[],mannequin);
cube([-5,-1.2,2.4],[5,.6,3.6],[],null);
for(const sign of [-1,1]) {
 cube([-2,-.6,-2.4],[2,.6,3.4],[{p:[sign*4.6,-.4,0],r:[.06,0,sign*.16]}],null);
 cube([-.65,-1.3,-.45],[.65,1.9,.45],[{p:[sign*3.7,.2,-2.3],r:[-.15,0,-sign*.18]}],null);
}
for(let i=-3;i<=3;i++) {
 const parent={p:[i*2.2,0,3.2+Math.abs(i)*.05],r:[.07,-i*.025,-i*.012]};
 cube([-1.3,0,0],[1.3,9,.7],[parent],null,[(i+3)/7,0,(i+4)/7,9/23]);
 cube([-1.6,0,0],[1.6,14-Math.abs(i)*.4,.7],[{p:[0,9,0],r:[.035,0,-i*.012]},parent],null,[(i+3)/7,9/23,(i+4)/7,1]);
}
function render(filename,angle) {
 const W=640,H=800,S=21,pixels=new Uint8Array(W*H*4),depth=new Float64Array(W*H).fill(-Infinity);
 for(let y=0;y<H;y++)for(let x=0;x<W;x++){const p=(y*W+x)*4;const c=235-Math.round(y/H*14);pixels.set([c,c+2,c+5,255],p);}
 const project=v=>{const q=rotate(v,[0,angle,0]);return[W/2+q[0]*S,192+q[1]*S,q[2]];};
 for(const tri of tris) {
  const v=tri.v.map(project),[a,b,c]=v;
  const area=(b[1]-c[1])*(a[0]-c[0])+(c[0]-b[0])*(a[1]-c[1]);if(Math.abs(area)<.001)continue;
  const x0=Math.max(0,Math.floor(Math.min(...v.map(p=>p[0])))),x1=Math.min(W-1,Math.ceil(Math.max(...v.map(p=>p[0]))));
  const y0=Math.max(0,Math.floor(Math.min(...v.map(p=>p[1])))),y1=Math.min(H-1,Math.ceil(Math.max(...v.map(p=>p[1]))));
  for(let y=y0;y<=y1;y++)for(let x=x0;x<=x1;x++) {
   const A=((b[1]-c[1])*(x+.5-c[0])+(c[0]-b[0])*(y+.5-c[1]))/area;
   const B=((c[1]-a[1])*(x+.5-c[0])+(a[0]-c[0])*(y+.5-c[1]))/area,C=1-A-B;if(A<0||B<0||C<0)continue;
   const z=A*a[2]+B*b[2]+C*c[2],idx=y*W+x;if(z<=depth[idx])continue;depth[idx]=z;
   let col=tri.color;
   if(!col){const u=A*tri.uv[0][0]+B*tri.uv[1][0]+C*tri.uv[2][0],vv=A*tri.uv[0][1]+B*tri.uv[1][1]+C*tri.uv[2][1];const ix=Math.min(63,Math.max(0,Math.floor(u*64))),iy=Math.min(63,Math.max(0,Math.floor(vv*64)));col=tex.slice((iy*64+ix)*4,(iy*64+ix)*4+3);}
   pixels.set([...col.map(n=>Math.max(0,Math.min(255,Math.round(n*tri.shade)))),255],idx*4);
  }
 }
 png(filename,W,H,pixels);
}
render(`${output}/back.png`,.48);render(`${output}/front.png`,Math.PI-.48);
console.log('Created 64x64 four-tone native mantle texture, matching item model and mesh previews.');
