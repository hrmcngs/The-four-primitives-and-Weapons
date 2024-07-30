# say [start]
#########################################################
# ①の処理
#########################################################
summon minecraft:armor_stand ~ ~ ~ {Tags:[AS2,AS2_INI],Small:1b}
execute anchored eyes as @e[tag=AS2_INI,limit=1,sort=nearest] run tp @s ^ ^ ^ ~ ~
#########################################################
scoreboard players set @e[tag=AS2_INI,limit=1,sort=nearest] AS2_D 80
tag @e[tag=AS2_INI,limit=1,sort=nearest] remove AS2_INI
#########################################################
