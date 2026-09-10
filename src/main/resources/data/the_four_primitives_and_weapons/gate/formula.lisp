;; ======================================================================
;; Gate / ConvergentGate / GateProjectile の数値パラメータを Lisp で定義。
;; 起動時に一度だけ評価され、結果はキャッシュされる。
;; データパックで上書きすれば Java 再コンパイル無しに調整可能。
;; ======================================================================

;; --- GateItem (空中展開 → 連続射出) -----------------------------------------------
;; 本数 (≧1 の整数)
(define gate-projectile-count 18)
;; 1段あたりの本数・段の高さ間隔
(define gate-columns 6)
(define gate-row-spacing 1.7)
;; 空中で構える時間と各剣の射出間隔 (tick)
(define gate-warmup-ticks 15)
(define gate-launch-interval 1)
;; 横展開: 外側の剣がプレイヤーから左右へ離れる距離 (ブロック)
(define gate-side-spread 5.0)
;; 前後オフセット (負=後方スポーン、ブロック)
(define gate-forward-offset -2)
;; 垂直オフセット (目の高さ基準、ブロック)
(define gate-vertical-offset 1.0)
;; 初速 (blocks/tick 相当; projectile の deltaMovement 倍率)
(define gate-shoot-velocity 2.0)
;; クールダウン (tick)
(define gate-cooldown 50)
;; 効果音の回数 (Gate: 展開音 / ConvergentGate: wither.shoot)
(define gate-sound-reps 4)
;; 耐性バフ: 振幅 (0=I, 4=V) と持続 (tick; Gateは展開待ち時間も加算)
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

;; --- GateProjectileEntity (共通の飛翔体) ------------------------------
;; 自動消滅 tick (タイムアウト時に爆発エフェクトあり)
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
