;; Luna companion: initial values preserve the existing behavior.
;; Server datapack resource. Reload with /reload; no Java rebuild for datapack edits.

;; Follow position, speed (blocks/tick), stopping and teleport distance (blocks).
(define anchor-side 1.5)
(define anchor-height 1.4)
(define idle-speed 0.22)
(define combat-speed 0.32)
(define stop-distance 0.08)
(define teleport-distance 32)

;; Scanning interval (ticks), threat scan box radius and maximum target distance.
(define scan-interval 10)
(define scan-range 12)
(define target-range 24)
(define anchor-tolerance 2)
(define rotation-blend 0.22)
(define aim-tolerance 7)

;; Laser damage, cooldown (ticks), visual firing duration, particles (0 disables ambient particles).
(define laser-damage 8)
(define min-cooldown 10)
(define cooldown-extra 5)
(define firing-ticks 7)
(define particle-interval 5)
(define laser-step 0.2)

;; Larger priority wins; equal priorities prefer the nearest target.
;; Sensors: owner-health-ratio, owner-attack-delay, target-distance,
;; owner-attacked-target, target-attacked-owner, luna-element-level, book-element-level.
(rule target-priority
  (if owner-attacked-target 3 (if target-attacked-owner 2 1)))
(rule attack-enabled true)
(rule damage laser-damage)
(rule cooldown (max min-cooldown (+ (ceil owner-attack-delay) cooldown-extra)))

;; true: Luna's element wins when both Luna and the owner's book have different elements.
(rule prefer-luna-element true)
;; Same element: larger level + at least 1, or half the smaller level rounded down.
(rule combined-element-level
  (+ (max luna-element-level book-element-level)
     (max (floor (/ (min luna-element-level book-element-level) 2)) 1)))
