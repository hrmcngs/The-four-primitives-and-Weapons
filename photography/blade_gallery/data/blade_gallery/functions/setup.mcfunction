# Overworld x/z -48..47, y 60..100 is rebuilt. Use the supplied dedicated world.
execute in minecraft:overworld run forceload add -48 -48 47 47
schedule function blade_gallery:wait_for_chunks 1s replace
