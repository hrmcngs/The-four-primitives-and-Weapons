
# say [start]
#########################################################
# ①の処理
#########################################################
#summon firework_rocket ~ ~1 ~ {Tags:[AS2,AS2_INI],Invisible:1b,Invulnerable:1b,NoBasePlate:1b,Marker:1b,LifeTime:20,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Flicker:0b,Trail:0b,Colors:[I;15790320],FadeColors:[I;15790320]}],Flight:1}}}}
#summon minecraft:armor_stand ~ ~1 ~ {Tags:[AS2,AS2_INI],Invisible:1b,Invulnerable:1b,NoBasePlate:1b,Marker:1b}
summon minecraft:armor_stand ~ ~1 ~ {Tags:[AS2,AS2_INI],Invisible:1b,Invulnerable:1b,NoBasePlate:1b,Marker:1b,life:1200}
execute anchored eyes as @e[tag=AS2_INI,limit=1,sort=nearest] run tp @s ^ ^ ^ ~ ~
execute anchored eyes as @e[tag=AS2_INI,limit=1,sort=nearest] run effect give @s minecraft_armor_weapon:tyokutou_arrow_effect 10 1 true
#########################################################
scoreboard players set @e[tag=AS2_INI,limit=1,sort=nearest] AS2_D 80
tag @e[tag=AS2_INI,limit=1,sort=nearest] remove AS2_INI
#########################################################
