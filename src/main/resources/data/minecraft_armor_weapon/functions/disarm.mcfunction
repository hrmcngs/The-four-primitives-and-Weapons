#武装解除
summon item ~ ~ ~ {PickupDelay:10s,Tags:["DisarmedItem"],Item:{id:"minecraft:structure_void",Count:1b}}
execute if entity @s[type=!player] run data modify entity @e[tag=DisarmedItem,limit=1,sort=nearest] Item set from entity @s HandItems[0]
execute if entity @s[type=player] run data modify entity @e[tag=DisarmedItem,limit=1,sort=nearest] Item set from entity @s SelectedItem
replaceitem entity @s weapon.mainhand air
kill @e[limit=1,sort=nearest,tag=DisarmedItem,nbt={Item:{id:"minecraft:structure_void",Count:1b}}]

