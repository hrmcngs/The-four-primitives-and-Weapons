# say [start]
#########################################################
# ①の処理
########################################################
summon minecraft:armor_stand ~ ~1 ~ {Tags:[AS2,AS2_INI],Small:1b,Invisible:1b,Invulnerable:1b,NoBasePlate:1b}
#summon minecraft:arrow ~ ~1 ~ {Tags:[AS2,AS2_INI],life:1200s}
execute anchored eyes as @e[tag=AS2_INI,limit=1,sort=nearest] run tp @s ^ ^ ^ ~ ~
execute anchored eyes as @e[tag=AS2_INI,limit=1,sort=nearest] run effect give @s minecraft_armor_weapon:test_bow_effect 2 1 true
#########################################################
scoreboard players set @e[tag=AS2_INI,limit=1,sort=nearest] AS2_D 80
tag @e[tag=AS2_INI,limit=1,sort=nearest] remove AS2_INI
#########################################################
