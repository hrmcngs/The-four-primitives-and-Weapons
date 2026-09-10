;; ======================================================================
;; Gate / ConvergentGate / GateProjectile の数値パラメータを Lisp で定義。
;; 起動時に一度だけ評価され、結果はキャッシュされる。
;; データパックで上書きすれば Java 再コンパイル無しに調整可能。
;; ======================================================================

;; --- GateItem: gate1-16/data/superdog/functions/poof.mcfunction ---
;; 54-56行: 1回に3本。18本/3段の展開ではない。
(define gate-projectile-count 3)
;; 31行: 通常の剣は15tickで射出。29行のlimit=1による例外は再現しない。
(define gate-warmup-ticks 15)
(define gate-side-spread 3.5)
(define gate-forward-offset -2)
;; 元の高さ0.3/0.6/0.7に加える補正 (使用者の足元基準)
(define gate-vertical-offset 0.0)
;; 元の tp ^ ^ ^4 に合わせる。
(define gate-shoot-velocity 4.0)
;; 元に召喚クールダウンはない。
(define gate-cooldown 0)
;; 以下は収束型の既存設定。通常Gateの召喚音/耐性は元の値を使用。
(define gate-sound-reps 4)
(define gate-resist-amp 4)
(define gate-resist-dur 20)

;; --- ConvergentGateItem (収束型) --------------------------------------
;; 収束地点までの距離 (ブロック)
(define converge-distance 20.0) ;; 収束しなくていい
;; 左右の展開幅 (±SPREAD、ブロック)
(define converge-spread 5.0)
;; 本数 (≧2; 等間隔配置に 2 以上必要)
(define converge-projectile-count 5)
;; 初速
(define converge-shoot-velocity 2.0)
;; クールダウン (tick)
(define converge-cooldown 20)

;; --- GateProjectileEntity (共通の命中演出・収束型の寿命) ------------------------------
;; 収束型の自動消滅 tick (通常Gateは元に合わせて召喚から50tickで無音消滅)
(define gate-proj-lifetime 100)
;; タイムアウト時の爆発半径 (1.5 ≒ 3x3x3)
(define gate-proj-end-radius 1.5)
;; エンティティ直撃時のダメージ
(define gate-proj-hit-damage 15.0)
;; エンティティ/ブロック直撃時の爆発半径
(define gate-proj-hit-radius 2.0)
;; 毎 tick 生成するパーティクル数
(define gate-proj-particles-per-tick 3)
;; 重力 (0 = 完全直進)
(define gate-proj-gravity 0.0)
