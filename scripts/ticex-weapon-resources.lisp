;;; SBCL: generates solid weapon fallback models and the reusable pattern recipe.
(defun write-resource (path text)
  (ensure-directories-exist path)
  (with-open-file (out path :direction :output :if-exists :supersede) (write-string text out)))
(defun cube-json (box texture)
  (destructuring-bind (x y z a b c) box
    (format nil "{\"from\":[~a,~a,~a],\"to\":[~a,~a,~a],\"faces\":{~{~a~^,~}}}" x y z a b c
      (loop for face in '("north" "south" "east" "west" "up" "down")
        collect (format nil "~s:{\"uv\":[0,0,16,16],\"texture\":~s}" face texture)))))
(defun model (name pieces)
  (write-resource (format nil "src/main/resources/assets/the_four_primitives_and_weapons/models/ticex/~a.json" name)
    (format nil "{\"parent\":\"minecraft:block/block\",\"textures\":{\"metal\":\"minecraft:block/iron_block\",\"wood\":\"minecraft:block/oak_planks\",\"string\":\"minecraft:block/white_wool\",\"particle\":\"minecraft:block/iron_block\"},\"display\":{\"thirdperson_righthand\":{\"rotation\":[0,-90,55],\"translation\":[0,4,0],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[0,90,-55],\"translation\":[0,4,0],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[0,-90,25],\"translation\":[1.13,3.2,1.13],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[0,90,-25],\"translation\":[1.13,3.2,1.13],\"scale\":[0.68,0.68,0.68]},\"gui\":{\"rotation\":[0,0,-45],\"scale\":[0.8,0.8,0.8]},\"ground\":{\"translation\":[0,2,0],\"scale\":[0.5,0.5,0.5]},\"fixed\":{\"rotation\":[0,180,0],\"scale\":[0.8,0.8,0.8]}},\"elements\":[~{~a~^,~}]}~%"
      (loop for (box texture) in pieces collect (cube-json box texture)))))
(model "sword" '(((7 1 7 9 5 9) "#wood") ((4 5 7 12 6 9) "#metal") ((7 6 7 9 16 9) "#metal")))
(model "knife" '(((7 2 7 9 7 9) "#wood") ((6 7 7 10 8 9) "#metal") ((7 8 7 9 14 9) "#metal")))
(model "shield" '(((2 1 6 14 15 8) "#metal") ((6 5 8 10 11 10) "#wood")))
(model "trident" '(((7.5 0 7.5 8.5 12 8.5) "#wood") ((4 11 7 12 12 9) "#metal")
                   ((4 12 7 5 15 9) "#metal") ((7 12 7 9 16 9) "#metal") ((11 12 7 12 15 9) "#metal")))
(loop for stage from 0 to 3 do
  (let ((pull (* stage .6)))
    (model (format nil "bow~a" stage)
      `(((3 6 7 5 10 9) "#wood") ((4 3 7 6 6 9) "#metal") ((4 10 7 6 13 9) "#metal")
        ((6 1 7 9 3 9) "#metal") ((6 13 7 9 15 9) "#metal")
        ((,(+ 8 pull) 3 7.8 ,(+ 8.3 pull) 13 8.2) "#string")))))
(model "crossbow" '(((7 2 6 9 14 9) "#wood") ((1 10 7 15 12 9) "#metal")
                    ((1 8 7 3 10 9) "#metal") ((13 8 7 15 10 9) "#metal") ((2 8 7.8 14 8.3 8.2) "#string")))
(write-resource "src/main/resources/assets/the_four_primitives_and_weapons/models/item/ticex_weapon_pattern.json"
  "{\"parent\":\"minecraft:item/generated\",\"textures\":{\"layer0\":\"minecraft:item/paper\"}}")
(write-resource "src/main/resources/data/the_four_primitives_and_weapons/recipes/ticex_weapon_pattern.json"
  "{\"conditions\":[{\"type\":\"forge:mod_loaded\",\"modid\":\"ticex\"}],\"type\":\"minecraft:crafting_shapeless\",\"ingredients\":[{\"item\":\"minecraft:paper\"},{\"item\":\"minecraft:stick\"}],\"result\":{\"item\":\"the_four_primitives_and_weapons:ticex_weapon_pattern\"}}")
