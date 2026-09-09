# プレイヤー型AIの実装

現行の戦闘アクションは `ALifeAIBridge.java` と `PlayerLikeAIGoal.java` が担当します。
外部プロセスを使っていなかった旧Pythonプロトタイプと未使用のPythonAIProcessは削除しました。

Lispゲノムを使う経路は `ai/lisp/MobAIBrain.java`、読み込みは `AIEvolutionManager.java`、
評価器は `LispInterpreter.java` にあります。ゲノムの編集元は
`src/main/resources/data/the_four_primitives_and_weapons/ai_genomes/` です。

このLispはゲーム内評価器の方言です。ツール用Common Lisp（SBCL）とは別の実行環境で、
Minecraftの起動に外部のSBCLを追加要求するものではありません。
現行のJava/Lisp戦闘処理は、この移行では変更していません。
