"""Additional, separated screenshot sets for the blade gallery datapack."""
import random
import math
from photo_details import scene_details


def terrain_offset(name, x, z):
    """Continuous hills with level foundations and level water surfaces."""
    def hill(px,pz,height,radius):
        return height*math.exp(-((x-px)**2+(z-pz)**2)/(radius*radius))
    if name == 'rubble':
        if abs(x)<=48 and -65<=z<=18:
            return 0
        # Keep tower foundations level; the surrounding banks bury their lower floors.
        for bx,bz,w,h in [(-58,-100,19,57),(-31,-103,20,46),(-2,-106,22,61),
                          (28,-98,25,51),(-55,-66,18,38),(-23,-72,20,30),
                          (9,-74,21,43),(39,-65,19,34),(-61,-29,13,28),(48,-23,13,32)]:
            if bx-1<=x<=bx+w+1 and bz-1<=z<=bz+17:
                return 3 if bz<-80 else 1
        value=hill(-49,24,11,14)+hill(40,30,8,17)+hill(-59,-45,12,13)+hill(58,-45,13,15)+hill(0,-87,7,22)
        edge=min(x+64,63-x,z+112,47-z)
    else:
        edge=min(x+32,31-x,z+32,31-z)
        if name == 'flowers':
            if 5<=x<=16 and -20<=z<=-11: return 5
            if 4<=x<=8 and -7<=z<=-3: return 7
            value=hill(8,-10,8,15)+hill(-18,-18,6,13)+hill(23,15,5,12)
        elif name == 'burned':
            for bx,bz,height in [(-19,-17,3),(7,-19,6)]:
                if bx-1<=x<=bx+12 and bz-1<=z<=bz+11:return height
            value=hill(14,-13,7,17)+hill(-20,-15,4,14)+hill(-12,18,4,12)
        else:
            for px,pz,r,height in [(-16,-5,5,1),(12,-13,6,3),(0,8,3,0)]:
                if (x-px)**2+(z-pz)**2 <= (r+1)**2:return height
            # A winding trench passes behind the foreground weapons.
            trench=-8+round(3*math.sin(z/7))
            if -22<=z<=20 and abs(x-trench)<=1:return -2
            value=hill(20,-16,9,15)+hill(-21,-22,6,13)+hill(21,15,5,13)+hill(-23,13,4,12)
    return round(value*min(1,max(0,edge)/6))


def contour_scene(lines, start, name, cx, bounds):
    """Warp decoration columns onto terrain; retain uniform elevations on building pads."""
    xmin,xmax,zmin,zmax=bounds
    out=lines[:start]
    for x in range(xmin,xmax+1):
        for z in range(zmin,zmax+1):
            dy=terrain_offset(name,x,z)
            if dy>0:
                out.append(f'fill {cx+x} 64 {z} {cx+x} {63+dy} {z} '+('stone' if name=='rubble' else 'dirt'))
                out.append(f'setblock {cx+x} {64+dy} {z} '+('gravel' if name=='rubble' else 'grass_block'))
            elif dy<0:
                out.append(f'fill {cx+x} {65+dy} {z} {cx+x} 64 {z} air')
                out.append(f'setblock {cx+x} {64+dy} {z} coarse_dirt')
    for line in lines[start:]:
        b=line.split()
        if b[0]=='fill':
            x,y,z,xx,yy,zz=map(int,b[1:7])
            for row in range(z,zz+1):
                left=x
                while left<=xx:
                    dy=terrain_offset(name,left-cx,row)
                    right=left
                    while right<xx and terrain_offset(name,right+1-cx,row)==dy:right+=1
                    out.append(f'fill {left} {y+dy} {row} {right} {yy+dy} {row} '+ ' '.join(b[7:]))
                    left=right+1
        elif b[0] in ('setblock','summon'):
            at=1 if b[0]=='setblock' else 2
            dy=terrain_offset(name,math.floor(float(b[at]))-cx,math.floor(float(b[at+2])))
            value=float(b[at+1])+dy
            b[at+1]=str(int(value)) if value.is_integer() else str(value)
            out.append(' '.join(b))
        else:out.append(line)
    return out


def generate_scenes(function, mod):
    scenes = [('rubble', 128, '瓦礫地帯'), ('burned', 256, '焼け跡の剣'),
              ('battlefield', 384, '戦場の果て'), ('flowers', 512, '花畑')]
    function('scenes/setup', [f'function blade_gallery:scenes/{name}/setup' for name, _, _ in scenes])
    load = []
    for name, _, _ in scenes:
        version = 4
        if version == 1:
            load.append(f'execute if data storage blade_gallery:state {{scenes_version:1}} run data modify storage blade_gallery:state scenes.{name} set value 1')
        load.append(f'execute unless data storage blade_gallery:state {{scenes:{{{name}:{version}}}}} run function blade_gallery:scenes/{name}/setup')
    function('scenes/load', load)
    for index, (name, cx, title) in enumerate(scenes):
        prefix = 'scenes/' + name
        tag = 'blade_gallery.' + name
        xmin, xmax, zmin, zmax, ceiling = (-64, 63, -112, 47, 148) if name == 'rubble' else (-32,31,-32,31,100)
        function(prefix+'/setup', [
            f'execute in minecraft:overworld run forceload add {cx+xmin} {zmin} {cx+xmax} {zmax}',
            f'schedule function blade_gallery:{prefix}/wait 1s replace'])
        loaded = ' '.join(f'if loaded {x} 64 {z}' for x in range(cx+xmin, cx+xmax+1, 16) for z in range(zmin, zmax+1, 16))
        function(prefix+'/wait', [f'schedule function blade_gallery:{prefix}/wait 1s replace',
            f'execute in minecraft:overworld {loaded} run function blade_gallery:{prefix}/build'])
        lines = [f'schedule clear blade_gallery:{prefix}/wait',
                 f'kill @e[type={mod}:stabbed_weapon,tag={tag}]', 'gamerule doFireTick false']

        def fill(x, y, z, xx, yy, zz, material):
            assert (abs(xx-x)+1)*(abs(yy-y)+1)*(abs(zz-z)+1) <= 32768
            lines.append(f'fill {cx+x} {y} {z} {cx+xx} {yy} {zz} {material}')

        def block(x, y, z, material):
            lines.append(f'setblock {cx+x} {y} {z} {material}')

        def sword(x, y, z, tilt=12, yaw=25, item=None, faded=False, habaki=None):
            item = item or mod+':iron_katana'
            colors = ''
            if faded:
                # Actual supported fitting NBT, deliberately low-saturation palettes.
                palette = rng.choice([(0x777267,0x77796F,0x686C68,0x8C8673),
                                      (0x747B73,0x837B70,0x73716B,0x969080),
                                      (0x817675,0x727778,0x747575,0x8A8980)])
                colors = ',tag:{'+','.join(f'{key}:{value}' for key,value in zip(
                    ('TsukaColor','TsubaColor','KashiraColor','HabakiColor'),palette))+'}'
            if habaki is not None:
                assert not faded
                colors = ',tag:{HabakiColor:'+str(habaki)+'}'
            lines.append(f'summon {mod}:stabbed_weapon {cx+x} {y} {z} '+
                '{Tags:["'+tag+'"],StabItem:{id:"'+item+'",Count:1b'+colors+'},'+
                f'StabTilt:{tilt}f,StabYaw:{yaw}f,StabScale:1f,DisappearOnPickup:1b'+'}')

        for x in range(xmin, xmax+1, 16):
            for z in range(zmin, zmax+1, 16):
                fill(x, 60, z, x+15, 63, z+15, 'dirt')
                fill(x, 64, z, x+15, 64, z+15, 'grass_block')
                fill(x, 65, z, x+15, ceiling, z+15, 'air')
        rng = random.Random(734+index)
        terrain_start = len(lines)
        if name == 'rubble':
            # Broad drowned city basin, with irregular shorelines and a dry foreground.
            fill(-63,64,-111,62,64,36,'gravel')
            for z in range(-65,19):
                width = int(48*math.sqrt(max(0,1-((z+23)/44)**2)))
                if width > 0:
                    fill(-width,61,z,width,61,z,'clay')
                    fill(-width,62,z,width,64,z,'water')
            # Staggered skeletal towers hide the far edge; broken floors and open windows.
            for bx,bz,w,h in [(-58,-100,19,57),(-31,-103,20,46),(-2,-106,22,61),
                               (28,-98,25,51),(-55,-66,18,38),(-23,-72,20,30),
                               (9,-74,21,43),(39,-65,19,34),(-61,-29,13,28),(48,-23,13,32)]:
                fill(bx,64,bz,bx+w,65,bz+16,'cracked_stone_bricks')
                for dx in (0,w//2,w):
                    for dz in (0,16):
                        height=h-rng.randrange(0,8)
                        fill(bx+dx,66,bz+dz,bx+dx,65+height,bz+dz,'gray_concrete')
                for floor in range(70,65+h-6,6):
                    fill(bx,floor,bz,bx+w,floor,bz+16,'light_gray_concrete')
                    # Missing corners become increasingly large on upper storeys.
                    gap=rng.randint(3,max(4,w//2))
                    fill(bx,floor,bz+10,bx+gap,floor,bz+16,'air')
                    fill(bx+2,floor+1,bz,bx+w-2,floor+3,bz,'gray_concrete')
                    for window in range(3,w-2,4):
                        fill(bx+window,floor+1,bz,bx+window+1,floor+2,bz,'air')
                    fill(bx+w,floor+1,bz+3,bx+w,floor+2,bz+8,'iron_bars')
                for _ in range(14):
                    dx,dz=rng.randint(0,w),rng.randint(0,16)
                    block(bx+dx,66,bz+dz,rng.choice(['andesite','cobblestone','stone_slab']))
            # Collapsed concrete ribs and debris islands protrude from the floodwater.
            for x,z in [(-38,-38),(-26,-50),(18,-44),(31,-28),(-33,2),(26,5)]:
                fill(x,63,z,x+8,65,z+4,'mossy_cobblestone')
                fill(x+2,66,z+1,x+6,66,z+2,'stone_brick_slab')
                fill(x+4,66,z+2,x+4,69,z+2,'iron_bars')
            fill(-22,64,-23,22,64,6,'cracked_stone_bricks')
            # Ragged foundations, broken walls and fallen columns.
            for x, h in [(-22,8),(-17,5),(-12,3),(10,4),(16,6),(21,10)]:
                fill(x,65,-23,x+1,64+h,-21,'stone_bricks')
                fill(x,65,-20,x+1,66,-17,'mossy_stone_bricks')
            for x,z,length in [(-14,-8,9),(6,-16,11),(-8,2,6)]:
                fill(x,65,z,x+length,66,z+1,'polished_andesite')
                block(x,67,z,'chiseled_stone_bricks')
            for _ in range(160):
                x,z=rng.randint(-25,25),rng.randint(-25,10)
                block(x,65,z,rng.choice(['cobblestone','mossy_cobblestone','stone_brick_slab','gravel']))
            fill(2,65,-3,5,65,0,'smooth_stone')
            sword(3.5,66.5,-1.5)
        elif name == 'burned':
            # Charred house outlines, roof timbers and permanent small flames.
            for x in range(-25,26):
                for z in range(-25,19):
                    if (x/27)**2+(z/27)**2 < 1:
                        block(x,64,z,rng.choice(['coarse_dirt','gray_concrete_powder','blackstone','basalt']))
            for x,z in [(-19,-17),(7,-19)]:
                fill(x,64,z,x+11,64,z+10,'polished_blackstone_bricks')
                for dx,dz,h in [(0,0,7),(11,0,5),(0,10,3),(11,10,6)]:
                    fill(x+dx,65,z+dz,x+dx,64+h,z+dz,'basalt[axis=y]')
                fill(x,65,z,x+8,65,z,'deepslate_bricks')
                fill(x+2,65,z+3,x+9,65,z+3,'dark_oak_log[axis=x]')
                fill(x+10,65,z+5,x+10,69,z+6,'cracked_deepslate_bricks')
            for x,z in [(-17,-13),(12,-15),(-8,-4),(15,3),(-20,8)]:
                block(x,64,z,'netherrack')
                block(x,65,z,'fire')
                block(x+2,64,z,'campfire[lit=true]')
            # Almost horizontal blades look abandoned on the ash.
            sword(1.5,65.15,2.5,86,65)
            sword(-4.5,65.15,-2.5,82,140)
            sword(9.5,65.5,-7.5,25,10)
        elif name == 'battlefield':
            for x in range(-29,30):
                for z in range(-29,20):
                    block(x,64,z,rng.choice(['coarse_dirt','coarse_dirt','gravel','mud','grass_block']))
            # Water-filled shell craters with broken rims.
            for x,z,r in [(-16,-5,5),(12,-13,6),(0,8,3)]:
                for dx in range(-r,r+1):
                    for dz in range(-r,r+1):
                        if dx*dx+dz*dz < r*r:
                            block(x+dx,64,z+dz,'air')
                            block(x+dx,63,z+dz,'water')
                            block(x+dx,62,z+dz,'gravel')
            for x in range(-26,27,7):
                fill(x,65,-24,x,68,-24,'stripped_dark_oak_log')
                fill(x,66,-24,x+3,66,-24,'dark_oak_fence')
                block(x,69,-24,'black_banner')
            weapons = ['minecraft:iron_sword',mod+':iron_tyokuto',mod+':iron_katana']
            positions = [(-24,3),(-7,-14),(4,-21),(23,-8),(-19,-19),(17,8),(-7,9),(7,-3),
                         (-26,-16),(-22,12),(-11,16),(10,14),(25,14),(23,-20),(-4,-24),
                         (-25,-8),(-6,0),(0,-12),(18,-23),(25,0),(-17,18),(5,18),
                         (-10,-22),(16,0),(21,19),(-28,18),(-2,-18),(28,-16),
                         (-22,-24),(10,4),(2,-6),(-12,5),(14,19),(26,-27),(0,20),(8,-25)]
            for i,(x,z) in enumerate(positions):
                fallen = i % 8 == 0
                sword(x+.5,65.15 if fallen else 65.5,z+.5,
                      83 if fallen else rng.randint(6,33),rng.randint(0,359),weapons[i%3],True)
            fill(1,64,0,4,64,3,'gravel')
            sword(2.5,65.5,1.5,5,30,mod+':iron_katana',True)
        else:
            # Color drifts with a winding clear path to a flowering tree.
            for x in range(-29,30):
                for z in range(-28,26):
                    if abs(x-int(4*math.sin(z/8))) <= 1:
                        block(x,64,z,'dirt_path')
                    elif rng.random() < .64:
                        palette = (['oxeye_daisy','azure_bluet','white_tulip'] if x < -9 else
                                   ['poppy','pink_tulip','allium'] if x < 10 else
                                   ['dandelion','cornflower','orange_tulip'])
                        block(x,65,z,rng.choice(palette))
            fill(10,65,-16,11,71,-15,'cherry_log')
            fill(6,70,-20,15,72,-11,'cherry_leaves[persistent=true]')
            fill(8,73,-18,13,74,-13,'cherry_leaves[persistent=true]')
            fill(5,70,-17,16,71,-14,'cherry_leaves[persistent=true]')
            fill(5,65,-6,7,65,-4,'mossy_cobblestone')
            sword(6.5,66.5,-4.5,8,45,mod+':magical_katana',habaki=0xE8B4C8)
        lines = contour_scene(lines, terrain_start, name, cx, (xmin,xmax,zmin,zmax))
        lines += scene_details(name,cx,lambda x,z: terrain_offset(name,x,z))
        lines += [f'forceload remove {cx+xmin} {zmin} {cx+xmax} {zmax}',
                  f'tellraw @a {{"text":"{title}を生成しました。 /function blade_gallery:camera/{name}","color":"gold"}}']
        version = 4
        lines.append(f'data modify storage blade_gallery:state scenes.{name} set value {version}')
        function(prefix+'/build', lines)
        camera_y=67+terrain_offset(name,-10,18)
        target_y=66+terrain_offset(name,3,-5)
        function('camera/'+name,[f'execute in minecraft:overworld run tp @s {cx-10} {camera_y} 18 facing {cx+3} {target_y} -5'])
        if name == 'rubble':
            function('camera/rubble',[f'execute in minecraft:overworld run tp @s {cx-12} {68+terrain_offset(name,-12,30)} 30 facing {cx+3} 75 -55'])
