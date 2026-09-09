#!/usr/bin/env python3
"""Generate a standalone 1.20.1 photo world and its editable datapack. No dependencies."""
import argparse
import gzip
import json
from pathlib import Path
import random
import shutil
import struct
import time
import zipfile
from photo_scenes import generate_scenes
from photo_details import scene_details

ROOT = Path(__file__).resolve().parents[1]
PACK = ROOT / 'photography' / 'blade_gallery'
MOD = 'the_four_primitives_and_weapons'


def function(name, lines):
    target = PACK / 'data/blade_gallery/functions' / (name + '.mcfunction')
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text('\n'.join(lines) + '\n', encoding='utf-8')


def write_json(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')


def generate_pack():
    write_json(PACK / 'pack.mcmeta', {'pack': {'pack_format': 15, 'description': '刀と侵食結晶 — 夕暮れの廃神殿・撮影庭園'}})
    for event in ('load', 'tick'):
        write_json(PACK / f'data/minecraft/tags/functions/{event}.json', {'values': [f'blade_gallery:{event}']})
    function('load', ['execute unless data storage blade_gallery:state {main_version:3} run function blade_gallery:setup',
                      'function blade_gallery:scenes/load'])
    function('setup', [
        '# Overworld x/z -48..47, y 60..100 is rebuilt. Use the supplied dedicated world.',
        'execute in minecraft:overworld run forceload add -48 -48 47 47',
        'schedule function blade_gallery:wait_for_chunks 1s replace'])
    chunks = ' '.join(f'if loaded {x} 64 {z}' for x in range(-48, 48, 16) for z in range(-48, 48, 16))
    function('wait_for_chunks', [
        'schedule function blade_gallery:wait_for_chunks 1s replace',
        f'execute in minecraft:overworld {chunks} run function blade_gallery:build'])
    lines = ['schedule clear blade_gallery:wait_for_chunks',
             'kill @e[type='+MOD+':stabbed_weapon,tag=blade_gallery.display]',
             'gamerule doDaylightCycle false', 'gamerule doWeatherCycle false',
             'gamerule doMobSpawning false', 'gamerule doPatrolSpawning false',
             'gamerule doTraderSpawning false', 'gamerule doFireTick false',
             'gamerule mobGriefing false', 'gamerule keepInventory true',
             'gamerule spawnRadius 0', 'difficulty peaceful', 'weather clear', 'time set 12500']

    def fill(x1, y1, z1, x2, y2, z2, block):
        volume = (abs(x2-x1)+1)*(abs(y2-y1)+1)*(abs(z2-z1)+1)
        assert volume <= 32768, volume
        lines.append(f'fill {x1} {y1} {z1} {x2} {y2} {z2} {block}')

    def block(x, y, z, material):
        lines.append(f'setblock {x} {y} {z} {material}')

    for x in range(-48, 48, 16):
        for z in range(-48, 48, 16):
            fill(x, 60, z, x+15, 63, z+15, 'dirt')
            fill(x, 64, z, x+15, 64, z+15, 'grass_block')
            fill(x, 65, z, x+15, 100, z+15, 'air')
    # Long shallow reflecting pool, stone border and a walkway across the water.
    fill(-21, 62, -13, 19, 64, 17, 'stone_bricks')
    fill(-20, 63, -12, 18, 64, 16, 'water')
    fill(-20, 62, -12, 18, 62, 16, 'polished_deepslate')
    fill(6, 64, -12, 8, 64, 26, 'polished_andesite')
    # Raised main display island; visible from the southwest bank.
    fill(2, 63, -7, 12, 64, 3, 'stone_bricks')
    fill(3, 65, -6, 11, 65, 2, 'polished_andesite')
    fill(4, 66, -5, 10, 66, 1, 'smooth_stone')
    fill(6, 67, -3, 8, 67, -1, 'chiseled_stone_bricks')
    for z, y in ((6, 64), (5, 64), (4, 64), (3, 64), (2, 65)):
        fill(6, y, z, 8, y, z, 'stone_brick_stairs[facing=north]')
    # Ruined cloister behind the pool: broken columns and asymmetric arch silhouette.
    fill(-26, 64, -29, 26, 65, -19, 'stone_bricks')
    fill(-25, 66, -28, 25, 66, -20, 'cracked_stone_bricks')
    for x, height in ((-23, 7), (-15, 12), (-7, 11), (7, 12), (15, 10), (23, 5)):
        fill(x-1, 67, -26, x+1, 67, -24, 'chiseled_stone_bricks')
        fill(x, 68, -25, x, 67+height, -25, 'stone_bricks')
        fill(x-1, 68+height, -26, x+1, 68+height, -24, 'stone_brick_slab')
    fill(-15, 78, -25, -7, 79, -25, 'stone_bricks')
    fill(7, 77, -25, 15, 78, -25, 'mossy_stone_bricks')
    fill(-7, 67, -25, -5, 75, -24, 'stone_bricks')
    fill(5, 67, -25, 7, 75, -24, 'stone_bricks')
    fill(-5, 75, -25, -3, 77, -24, 'stone_bricks')
    fill(3, 75, -25, 5, 77, -24, 'stone_bricks')
    fill(-3, 78, -25, 3, 79, -24, 'chiseled_stone_bricks')
    # Stepped grassy sword hill to the right, and stone photography terrace to the left.
    for inset, y in ((0, 65), (3, 66), (6, 67)):
        fill(25+inset, y, -11+inset, 45-inset, y, 13-inset, 'grass_block')
    fill(-33, 64, 18, -15, 64, 29, 'stone_bricks')
    fill(-32, 65, 19, -16, 65, 28, 'smooth_stone_slab')
    rng = random.Random(20260909)
    for _ in range(90):
        x, z = rng.randrange(-44, 45), rng.randrange(-43, -30)
        block(x, 65, z, rng.choice(['mossy_cobblestone', 'cobblestone_slab', 'grass', 'fern']))
    # Crystals on rocky islands: restrained cyan/violet light around the focal area.
    for x, z, color in ((-15, -6, 'cyan'), (-12, -9, 'violet'), (16, 10, 'cyan'), (22, -18, 'violet')):
        fill(x-2, 63, z-2, x+2, 64, z+2, 'mossy_cobblestone')
        block(x, 64, z, 'sea_lantern')
        for dx, dz, h in ((0, 0, 5), (1, 0, 3), (-1, 1, 2), (0, -1, 3)):
            fill(x+dx, 65, z+dz, x+dx, 64+h, z+dz, f'{MOD}:{color}_corrosion_crystal')
    for x, z in ((-25, 18), (-17, 18), (5, 20), (9, 20), (-20, -20), (20, -20)):
        block(x, 65, z, 'stone_brick_wall')
        block(x, 66, z, 'lantern')
    lines += scene_details('main',0,lambda x,z: 0)
    lines += ['function blade_gallery:weapons', 'setworldspawn -24 66 23',
              'data modify storage blade_gallery:state initialized set value 1b',
              'data modify storage blade_gallery:state main_version set value 3',
              'forceload remove -48 -48 47 47',
              'tellraw @a {"text":"撮影庭園が完成しました。 /function blade_gallery:camera/main で撮影位置へ","color":"gold"}']
    function('build', lines)
    weapons = [f'kill @e[type={MOD}:stabbed_weapon,tag=blade_gallery.display]']
    for x, y, z, yaw, tilt in [(7.5, 68.5, -1.5, 35, 8), (30.5, 67.5, 0.5, 15, 17),
                               (35.5, 68.5, 2.5, 65, 6), (38.5, 68.5, -2.5, 120, 22),
                               (32.5, 68.5, -3.5, 170, 10), (-18.5, 67.5, -22.5, 55, 12),
                               (19.5, 67.5, -22.5, 90, 25)]:
        weapons.append(f'summon {MOD}:stabbed_weapon {x} {y} {z} '+
                       '{Tags:["blade_gallery.display"],StabItem:{id:"'+MOD+':iron_katana",Count:1b},'+
                       f'StabYaw:{yaw}f,StabTilt:{tilt}f,StabScale:1f,DisappearOnPickup:1b'+'}')
    function('weapons', weapons)
    function('reset_weapons', ['execute in minecraft:overworld run function blade_gallery:weapons'])
    function('tick', ['execute if data storage blade_gallery:state initialized as @a[tag=!blade_gallery.visited] run function blade_gallery:welcome'])
    function('welcome', ['tag @s add blade_gallery.visited', 'gamemode creative @s', 'function blade_gallery:camera/main',
                         'tellraw @s {"text":"F1でHUD非表示。撮影位置: /function blade_gallery:camera/main・water・hill・detail","color":"aqua"}'])
    for name, pos in {'main':'-24 67 23', 'water':'-9 65 12', 'hill':'23 69 16', 'detail':'5 69 3'}.items():
        target = '35 68 0' if name == 'hill' else '7.5 69 -1.5'
        function('camera/'+name, [f'execute in minecraft:overworld run tp @s {pos} facing {target}'])
    for name, ticks in [('sunset',12500), ('day',6000), ('night',18000)]:
        function('light/'+name, [f'time set {ticks}', 'weather clear'])
    generate_scenes(function, MOD)


# Small typed NBT writer: creates a fresh save, without copying player data or old chunks.
def payload(kind, value):
    if kind in (1, 3, 4):
        return struct.pack({1:'>b', 3:'>i', 4:'>q'}[kind], value)
    if kind == 8:
        data = value.encode('utf-8')
        return struct.pack('>H', len(data)) + data
    if kind == 9:
        subtype, entries = value
        return bytes([subtype]) + struct.pack('>i', len(entries)) + b''.join(payload(subtype, entry) for entry in entries)
    if kind == 10:
        return b''.join(bytes([t])+payload(8, name)+payload(t, v) for name, (t, v) in value.items()) + b'\0'
    raise ValueError(kind)


def make_world(world):
    if world.exists():
        raise SystemExit(f'既存ワールドを保護するため中止: {world}')
    flat = {'type':(8,'minecraft:flat'), 'settings':(10,{
        'biome':(8,'minecraft:plains'), 'lakes':(1,0), 'features':(1,0),
        'structure_overrides':(9,(8,[])),
        'layers':(9,(10,[{'block':(8,b), 'height':(3,h)} for b,h in
                         [('minecraft:bedrock',1),('minecraft:dirt',127),('minecraft:grass_block',1)]]))})}
    dimensions = {'minecraft:overworld':(10, {'type':(8,'minecraft:overworld'), 'generator':(10,flat)})}
    for dim in ('the_nether','the_end'):
        source = {'type':(8,'minecraft:the_end')} if dim == 'the_end' else {'type':(8,'minecraft:multi_noise'),'preset':(8,'minecraft:nether')}
        dimensions['minecraft:'+dim] = (10,{'type':(8,'minecraft:'+dim), 'generator':(10,{
            'type':(8,'minecraft:noise'), 'settings':(8,'minecraft:'+('end' if dim == 'the_end' else 'nether')), 'biome_source':(10,source)})})
    data = {'DataVersion':(3,3465), 'version':(3,19133),
            'Version':(10,{'Id':(3,3465),'Name':(8,'1.20.1'),'Series':(8,'main'),'Snapshot':(1,0)}),
            'LevelName':(8,world.name), 'GameType':(3,1), 'allowCommands':(1,1),
            'hardcore':(1,0), 'Difficulty':(1,0), 'initialized':(1,1),
            'SpawnX':(3,-24), 'SpawnY':(3,66), 'SpawnZ':(3,23),
            'Time':(4,0), 'DayTime':(4,12500), 'LastPlayed':(4,int(time.time()*1000)),
            'clearWeatherTime':(3,1000000), 'raining':(1,0), 'thundering':(1,0),
            'GameRules':(10,{k:(8,'false') for k in ['doDaylightCycle','doWeatherCycle','doMobSpawning']}),
            'DataPacks':(10,{'Enabled':(9,(8,['vanilla','mod:forge','mod:'+MOD,'file/blade_gallery'])), 'Disabled':(9,(8,[]))}),
            'WorldGenSettings':(10,{'seed':(4,20260909),'generate_features':(1,0),'bonus_chest':(1,0),'dimensions':(10,dimensions)})}
    world.mkdir(parents=True)
    (world/'level.dat').write_bytes(gzip.compress(bytes([10,0,0])+payload(10,{'Data':(10,data)})))
    shutil.copytree(PACK, world/'datapacks/blade_gallery')
    shutil.copy2(ROOT/'photography/README.md', world/'撮影ガイド.md')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--world', type=Path, default=ROOT/'run/saves/刀の庭園 — 紹介画像用')
    args = parser.parse_args()
    generate_pack()
    make_world(args.world)
    archive = ROOT/'photography/blade-gallery-world.zip'
    with zipfile.ZipFile(archive, 'w', zipfile.ZIP_DEFLATED) as out:
        for path in args.world.rglob('*'):
            if path.is_file():
                out.write(path, Path(args.world.name)/path.relative_to(args.world))
    print(f'World: {args.world}\nDatapack: {PACK}\nZIP: {archive}')
