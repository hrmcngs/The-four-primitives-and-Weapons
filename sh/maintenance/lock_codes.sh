#!/bin/bash
# --all: ベースMod設定・マーカー・カスタムファイルのバックアップも作成します。
# 使い方・運用メモ (help / --help / -h でも表示)
# # MCreator コード自動再生成の無効化方法
#
# このプロジェクトでは、MCreatorによるコードの自動再生成を防ぐための複数の対策を実装しています。
#
# ## 実装済みの対策
#
# ### 1. 既存コードのロック (完了)
# - `the_four_primitives_and_weapons.mcreator`内のすべての`locked_code`フラグを`true`に設定
# - バックアップファイル: `the_four_primitives_and_weapons.mcreator.backup`
#
# ### 2. 自動ロックシステム
#
# #### a) Gradleタスク (`lock_all_codes.gradle`)
# - ビルド実行前に自動的にすべてのコードをロック
# - `build.gradle`に統合済み
#
# #### b) Common Lispスクリプト (`lisp/maintenance/lock-codes.lisp`)
# - MCreatorファイルのすべての要素を自動的にロック
# - JSONフォーマットを保持しながら安全に更新
#
# #### c) 実行用スクリプト
# - **Windows**: `lock_codes.bat`を実行
# - **Linux/Mac**: `./lock_codes.sh`を実行
#
# ## 使用方法
#
# ### MCreatorを開く前に（推奨）
# ```bash
# # Windowsの場合
# lock_codes.bat
#
# # Linux/Macの場合
# ./lock_codes.sh
# ```
#
# ### ビルド時（自動）
# Gradleビルドを実行すると、自動的にコードロックが適用されます：
# ```bash
# ./gradlew build
# ```
#
# ## 新しい要素を追加した後
#
# 1. MCreatorで新しい要素を作成
# 2. MCreatorを閉じる
# 3. `lock_codes.bat`（Windows）または`./lock_codes.sh`（Linux/Mac）を実行
# 4. MCreatorを再度開く
#
# これにより、新しく追加した要素もコード再生成から保護されます。
#
# ## 元に戻す方法
#
# コードロックを解除したい場合：
# ```bash
# # バックアップから復元
# cp the_four_primitives_and_weapons.mcreator.backup the_four_primitives_and_weapons.mcreator
# ```
#
# ## 注意事項
#
# - MCreatorのGUI上では「Regenerate code and build」ボタンが表示されますが、コードがロックされているため実際には再生成されません
# - 新しい要素を追加するたびにロックスクリプトを実行することをお勧めします
# - バックアップファイルは定期的に作成されます（`.backup_auto`拡張子）
#
# # MCreatorでファイルの自動上書きを防ぐ方法
#
# ## 手順
#
# ### 1. MCreatorでベースMod要素ファイルをロック
#
# 1. MCreatorを開く
# 2. メニューから「**Workspace → Workspace settings**」を選択
# 3. 「**Lock base mod element files**」にチェックを入れる
# 4. 「**OK**」をクリック
#
# これにより、以下のファイルが自動再生成から保護されます：
# - `TheFourPrimitivesAndWeaponsMod.java`（メインクラス）
# - `TheFourPrimitivesAndWeaponsModEntities.java`
# - `TheFourPrimitivesAndWeaponsModEntityRenderers.java`
# - その他の基本的なModファイル
#
# ### 2. 個別要素のロック（実装済み）
#
# `lisp/maintenance/lock-codes.lisp`スクリプトが既に実行されており、すべての個別要素が`locked_code: true`に設定されています。
#
# ### 3. カスタムエンティティの安全な実装（実装済み）
#
# MCreatorによって上書きされないカスタムファイル：
# - `/src/main/java/the_four_primitives_and_weapons/init/CustomEntityInit.java`
# - `/src/main/java/the_four_primitives_and_weapons/client/init/CustomEntityRenderers.java`
# - `/src/main/java/the_four_primitives_and_weapons/entity/DarkProjectileEntity.java`
# - `/src/main/java/the_four_primitives_and_weapons/client/renderer/DarkProjectileRenderer.java`
#
# これらのファイルは独立しているため、MCreatorの自動生成システムの影響を受けません。
#
# ## 重要な注意点
#
# ⚠️ **必ず「Lock base mod element files」を有効にしてください**
# これをしないと、ビルドのたびに`TheFourPrimitivesAndWeaponsMod.java`が上書きされ、カスタムエンティティの登録が消えてしまいます。
#
# ## 確認方法
#
# ロックが成功している場合、`TheFourPrimitivesAndWeaponsMod.java`の最初のコメントが以下のように表示されます：
#
# ```
# /*
#  *    MCreator note:
#  *
#  *    If you lock base mod element files, you can edit this file and it won't get overwritten.
#  *    ...
#  */
# ```
#
# ロックされていない場合は以下のように表示されます：
#
# ```
# /*
#  *    MCreator note: This file will be REGENERATED on each build.
#  */
# ```
#
# ## トラブルシューティング
#
# もし再び自動上書きされる場合：
# 1. MCreatorの「Workspace settings」で「Lock base mod element files」が有効になっているか確認
# 2. `lock_codes.bat`または`sh/maintenance/lock_codes.sh`を再実行
# 3. MCreatorを再起動
#
# これで、DarkProjectileEntityやその他のカスタムコードが保護されます！
#
# 実行場所: リポジトリのルートで bash sh/maintenance/lock_codes.sh。Common Lisp 3が必要です。
#
# Linux/Mac用スクリプト - MCreatorのコードを自動ロック


for shell_help_arg in "$@"; do
    case "$shell_help_arg" in
        help|--help|-h)
            awk 'NR == 1 { next } /^#/ { sub(/^# ?/, ""); print; next } /^[[:space:]]*$/ { print; next } { exit }' "$0"
            exit 0
            ;;
    esac
done

cd "$(dirname "$0")/../.."
command -v sbcl >/dev/null || { echo "SBCL (Common Lisp) が必要です。" >&2; exit 1; }
exec sbcl --script lisp/maintenance/lock-codes.lisp "$@"
