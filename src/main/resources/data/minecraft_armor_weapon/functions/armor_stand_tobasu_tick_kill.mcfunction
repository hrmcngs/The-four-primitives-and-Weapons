#########################################################
execute as @a[scores={AS2_R=1..}] at @s run function minecraft_armor_weapon:armor_stand_tobasu_start_kill
scoreboard players set @a[scores={AS2_R=1..}] AS2_R 0
#########################################################

#########################################################
scoreboard players remove @e[scores={AS2_D=1..}] AS2_D 1
kill @e[scores={AS2_D=0}]
#########################################################

#########################################################
execute as @e[tag=AS2] at @s run function minecraft_armor_weapon:armor_stand_tobasu_main
#########################################################

