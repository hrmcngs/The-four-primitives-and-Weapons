#!/usr/bin/env bash
# 金木犀・銀木犀・枝垂れ梅・椿の素材を再生成。SBCL・Java 17・unzipが必要。
# bash sh/generate/generate-flowering-woods.sh
# bash sh/generate/generate-flowering-woods.sh --vanilla-jar /path/to/1.20.1/client.jar
# 既定はローカルのForgeGradleキャッシュからMinecraft 1.20.1のclient.jarを参照します。通信は行いません。
# バニラのoak原木・年輪・板材・苗木、azalea葉・花付き葉を元に、Lispで配色を変更します。
# 花・葉・木を分けて染色し、元の解像度・透過・木目・葉の形を維持します。
# src/main/resources内の花木テクスチャ・モデル・レシピ・タグ・翻訳を更新します。
# 木の入手: /give @s the_four_primitives_and_weapons:kinmokusei_sapling
# 木を配置: /place feature the_four_primitives_and_weapons:kinmokusei_tree （地面の1ブロック上を指定）
# ginmokusei / shidare_ume / tsubaki も同様。苗木は骨粉でも育ちます。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
exec sbcl --script lisp/assets/flowering-woods.lisp "$@"
