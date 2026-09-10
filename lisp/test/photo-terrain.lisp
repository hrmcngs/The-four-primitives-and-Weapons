(load "lisp/common/core.lisp")
(load "lisp/generate/photo-terrain.lisp")
(in-package :maw-tools)
(let ((first (terrain-scene "flowers" 512 12345)))
  (assert (equal first (terrain-scene "flowers" 512 12345)))
  (assert (not (equal first (terrain-scene "flowers" 512 54321))))
  (assert (some (lambda (s) (search "cherry_leaves" s)) first))
  (assert (some (lambda (s) (search "magical_katana" s)) first))
  (assert (some (lambda (s) (search " water" s)) first)))
(loop for seed in '(0 1 12345 54321 2147483647) do
  (let ((*terrain-seed* seed))
    (loop for x from 464 below 560 do (loop for z from -48 below 48
      for h = (terrain-height x z 512) do
        (assert (<= 54 h 90))
        (assert (<= (abs (- h (terrain-height (1+ x) z 512))) 4))))))
(format t "Photo terrain reproducibility, variation, vegetation, water and slope checks passed.~%")
(loop for scene in '("rubble" "burned" "battlefield" "flowers") for center from 128 by 128 do
  (multiple-value-bind (lines camera) (terrain-focus scene center 1874615110)
    (assert (= 1 (count-if (lambda (s) (search "summon " s)) lines)))
    (assert (search " facing " camera))
    (assert (search "StabScale:1.5f" (car (last lines))))))
