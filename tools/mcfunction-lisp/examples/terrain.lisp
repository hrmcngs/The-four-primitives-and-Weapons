(in-package :mcfunction-lisp)
;; Vanilla-only example. Run /function demo:build at the intended construction origin.
;; This replaces a 25x25 area around that origin, from relative Y=-3 to Y=6.
(emit-function "build"
  (loop for x from -12 to 12 append
    (loop for z from -12 to 12
      for height = (round (+ 1 (* 2 (sin (/ x 5d0)) (cos (/ z 6d0))))) append
        (list (command "fill ~~~D ~~-3 ~~~D ~~~D ~~6 ~~~D air" x z x z)
              (command "fill ~~~D ~~-3 ~~~D ~~~D ~~~D ~~~D dirt" x z x (1- height) z)
              (command "setblock ~~~D ~~~D ~~~D grass_block" x height z)))))
(emit-function "welcome" '("tellraw @a {\"text\":\"Lisp datapack loaded. Run /function demo:build at your build site.\"}"))
(on-load "welcome")
