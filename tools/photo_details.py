"""Deterministic vegetation and set dressing, placed on the finished terrain."""
import math
import random


def scene_details(name, cx, elevation):
    lines = []
    rng = random.Random('gallery-detail-' + name)

    def ground(x, z):
        return 64 + elevation(x, z)

    def block(x, y, z, material):
        lines.append(f'setblock {cx+x} {y} {z} {material} keep')

    def bush(x, z, leaf='oak_leaves', radius=2):
        for dx in range(-radius, radius+1):
            for dz in range(-radius, radius+1):
                if dx*dx+dz*dz > radius*radius+1:
                    continue
                y=ground(x+dx,z+dz)
                # Keep planted weapons, foundations and paths clear.
                lines.append(f'execute if block {cx+x+dx} {y} {z+dz} minecraft:grass_block run setblock {cx+x+dx} {y+1} {z+dz} {leaf}[persistent=true]')
                if dx*dx+dz*dz<=1:
                    lines.append(f'execute if block {cx+x+dx} {y+1} {z+dz} {leaf} run setblock {cx+x+dx} {y+2} {z+dz} {leaf}[persistent=true] keep')

    def cherry(x,z,height):
        y=ground(x,z)+1
        # Clear only the trunk's grass/flowers; leave neighboring planting intact.
        lines.append(f'fill {cx+x} {y} {z} {cx+x} {y+height} {z} cherry_log')
        # Shallow umbrella crown over spreading limbs, rather than a round foliage ball.
        for ax,az in ((1,0),(-1,0),(0,1),(0,-1)):
            for reach in range(1,4):
                block(x+ax*reach,y+height-1-(reach==3),z+az*reach,
                      'cherry_log[axis='+('x' if ax else 'z')+']')
        for dy,r in ((-1,4),(0,5),(1,3),(2,1)):
            for dx in range(-r,r+1):
                width=round(math.sqrt(max(0,r*r-dx*dx)))
                lines.append(f'fill {cx+x+dx} {y+height+dy} {z-width} {cx+x+dx} {y+height+dy} {z+width} cherry_leaves[persistent=true] replace air')
        # Uneven, separate blossom strands hang from the crown rim down toward the grass.
        # Their ends follow the hillside and leave at least two blocks of clearance.
        for dx in range(-5,6):
            for dz in range(-5,6):
                if not 16<=dx*dx+dz*dz<=25 or (dx+2*dz)%3==0:
                    continue
                depth=3+((dx*7+dz*11+x+z)%3)
                top=y+height
                bottom=max(ground(x+dx,z+dz)+3,top-depth)
                if bottom<=top:
                    lines.append(f'fill {cx+x+dx} {bottom} {z+dz} {cx+x+dx} {top} {z+dz} cherry_leaves[persistent=true] replace air')
        for _ in range(20):
            dx,dz=rng.randint(-5,5),rng.randint(-5,5)
            yy=ground(x+dx,z+dz)
            lines.append(f'execute if block {cx+x+dx} {yy} {z+dz} grass_block run setblock {cx+x+dx} {yy+1} {z+dz} pink_petals[flower_amount={rng.randint(1,4)}] keep')

    if name=='flowers':
        for x,z,h in [(10,-16,6),(-23,-22,5),(-10,-24,6),(23,-21,5),(-24,-5,5),
                      (23,-2,6),(-20,13,5),(21,21,5),(8,25,4)]:
            cherry(x,z,h)
        for x,z in [(-16,-11),(-7,-18),(18,-10),(17,9),(-17,4),(-24,22),(4,21),(25,12),(-4,-27)]:
            bush(x,z,'flowering_azalea_leaves')
        # Small weathered stones and a bench on the outer path.
        for x,z in [(-14,8),(-19,-15),(15,17),(23,-12),(-5,23)]:
            y=ground(x,z)
            block(x,y+1,z,'mossy_cobblestone')
            block(x+1,ground(x+1,z)+1,z,'mossy_cobblestone_slab')
        for x in range(-18,-14):
            block(x,ground(x,20)+1,20,'oak_slab')
        for x,z in [(-13,22),(14,11),(-17,-7),(17,-22)]:
            y=ground(x,z)
            lines.append(f'setblock {cx+x} {y+1} {z} cobblestone_wall')
            block(x,y+2,z,'lantern')
    elif name=='rubble':
        # Bent rebar, broken drainage pipes and smaller rubble around the drowned blocks.
        for x,z in [(-42,27),(-28,30),(22,28),(46,20),(-53,5),(53,-37),(-36,-83),(24,-88)]:
            y=ground(x,z)
            for dx in range(4):
                block(x+dx,ground(x+dx,z)+1,z,'cracked_stone_bricks')
            block(x,y+2,z,'iron_bars')
            block(x,y+3,z,'iron_bars')
            block(x+1,y+3,z,'iron_bars')
            block(x+3,ground(x+3,z+1)+1,z+1,'polished_andesite_slab')
        for x,z in [(-39,-16),(-29,-35),(31,-46),(39,-6),(-10,-44),(14,-34)]:
            lines.append(f'execute if block {cx+x} 64 {z} water run setblock {cx+x} 65 {z} lily_pad keep')
    elif name in ('burned','battlefield'):
        for x,z,h in [(-27,-25,6),(26,-22,5),(-25,21,4),(25,24,6),(-27,1,4)]:
            y=ground(x,z)+1
            for dy in range(h):block(x,y+dy,z,'basalt' if name=='burned' else 'stripped_dark_oak_log')
            block(x+1,y+h-2,z,'dark_oak_fence')
            block(x+2,y+h-2,z,'dark_oak_fence')
            block(x-1,y+h-3,z,'dark_oak_fence')
        for x,z in [(-19,24),(17,23),(-24,-12),(23,5)]:
            y=ground(x,z)
            block(x,y+1,z,'barrel[facing=up]')
            block(x+1,ground(x+1,z)+1,z,'spruce_trapdoor[facing=north,half=bottom]')
            block(x+2,ground(x+2,z)+1,z,'dark_oak_slab')
        if name=='battlefield':
            for z in (-18,-9,0,12,18):
                x=-8+round(3*math.sin(z/7))
                block(x,ground(x,z)+1,z,'dark_oak_slab')
                block(x-2,ground(x-2,z)+1,z,'mud_bricks')
                block(x+2,ground(x+2,z)+1,z,'mud_brick_slab')
    else:
        for x,z in [(-40,12),(-39,-16),(39,26),(20,30),(-12,33),(-30,-36)]:
            bush(x,z,'azalea_leaves')
        for x,z in [(-38,-30),(33,-35)]:cherry(x,z,5)
        for x,z in [(-9,10),(-16,3),(13,12),(-7,-9)]:
            lines.append(f'execute if block {cx+x} 64 {z} water run setblock {cx+x} 65 {z} lily_pad keep')
        for x,z in [(-36,4),(23,24),(-12,29)]:
            block(x,65,z,'mossy_cobblestone')
            block(x,66,z,'moss_carpet')

    # Small plants in gaps, never on water, rubble floors, roads or existing decoration.
    limit=28 if name!='main' else 43
    for _ in range(300):
        x,z=rng.randint(-limit,limit),rng.randint(-limit,limit)
        if name=='flowers' and (abs(x-int(4*math.sin(z/8)))<=2 or (3<=x<=9 and -8<=z<=-2)):
            continue
        if name=='main' and -34<=x<=34 and -30<=z<=29:continue
        y=ground(x,z)
        plant=rng.choice(['grass','fern','azalea']) if name in ('flowers','main') else 'dead_bush'
        soil='grass_block' if name in ('flowers','main') else 'coarse_dirt'
        lines.append(f'execute if block {cx+x} {y} {z} {soil} run setblock {cx+x} {y+1} {z} {plant} keep')
    return lines
