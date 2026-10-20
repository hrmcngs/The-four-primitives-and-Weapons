# [![Typing SVG](https://readme-typing-svg.demolab.com?font=Jacquard+12&size=60&pause=1000&color=513C90&background=C1FCFF00&center=true&vCenter=true&random=true&width=1000&height=115&lines=The-four-primitives-and-Weapons)](https://git.io/typing-svg)
***
## リポジトリ管理者
この作品は、今のところは、  
数人の学生によって作られています  
そのため全体的にクオリティが低いです  
改善点あったら教えていただけるとありがたいです  
後嫌なことがあると更新頻度が少し遅くなります  
***
## Apache-2.0 license

Apache-2.0 licenseですが僕に許可くれれば、発酵させてもいいし天日干しにしてもいいよでも使う時は  
userID乗っけてくれると嬉しいよ、程度です  
***
## my mod link

<a href="https://www.curseforge.com/minecraft/mc-mods/the-four-primitives-and-weapons/files" target="_blank">
  <img src="https://github.com/hrmcngs/The-four-primitives-and-Weapons/blob/0afbf5c653e9fd2ea29179ea99e93ef791a0462c/src/main/resources/logo.png" alt="The four primitives and Weapons mod logo" style="width:100px; height:auto;">
</a>

https://curseforge.com/minecraft/mc-mods/the-four-primitives-and-weapons

## Wiki

使い方・仕様・設定方法は[GitHub Wiki](https://github.com/Drowse-Lab/The-four-primitives-and-Weapons/wiki)に、未実装事項・確認待ちの作業は[Issues](https://github.com/Drowse-Lab/The-four-primitives-and-Weapons/issues)にまとめています。

- [レアリティシステム・ツールチップ枠・giveコマンド](https://github.com/Drowse-Lab/The-four-primitives-and-Weapons/wiki/Rarity-System)

***
## other
<img src="https://github-readme-stats-git-master-airopis-projects.vercel.app/api?username=hrmcngs&count_private=true&show_icons=true&locale=en&theme=dark&count_private=true&role=OWNER,ORGANIZATION_MEMBER,COLLABORATOR" />  
<img src="https://github-readme-stats-git-master-airopis-projects.vercel.app/api?username=aya526dev&count_private=true&show_icons=true&locale=en&theme=dark&count_private=true&role=OWNER,ORGANIZATION_MEMBER,COLLABORATOR" />  
<img src="https://github-readme-stats-git-master-airopis-projects.vercel.app/api?username=Renkon65&count_private=true&show_icons=true&locale=en&theme=dark&count_private=true&role=OWNER,ORGANIZATION_MEMBER,COLLABORATOR" />  

***
## 参考リンク
doragon Armorのtextureはここからダウンロードしました  

<a href="https://github.com/GitPois1x/DragonLoot/tree/1.20/src/main/resources/assets/dragonloot/textures" target="_blank">
  <img src="https://github.com/janosh/awesome-normalizing-flows/actions/workflows/link-check.yml/badge.svg" alt="Link Check status badge" style="width:150px; height:auto;">
</a>


dagger鞘をslot backとbeltに入れても3Dmodelが出ない
簡単にできるようにして自動で外部addonでも適応できるように

### 任意MODを選んでビルド・導入

`bash sh/menu.sh` のビルド一覧から「選択MOD＋必須の軽量化MODでビルド・導入」を選びます。
「ビルド」「導入のみ」「クライアント起動」を選択でき、JARの番号を空白区切りで複数指定します。
候補は `libs/external`・`libs/local`・`libs/runtime_selected` と入力したフォルダです。
TiCEXなどの **Forge 1.20.1版JARと必須依存MODも一緒に選択**してください。
自動ダウンロードや、必須依存の自動追加は行いません。

```bash
bash sh/build/build-with-mods.sh
# JARを直接指定（パスに空白がある場合も対応）
bash sh/build/build-with-mods.sh --jar "/path/TiCEX.jar" --jar "/path/TaCZ.jar" --offline
# 導入だけ行い、後で実行する起動コマンドを表示
bash sh/build/build-with-mods.sh --jar "/path/mod.jar" --prepare
```

選択ごとに `libs/interactive_<hash>/` を作り、元のJAR・既存の起動プロファイルは変更しません。
追加MODは開発環境の実行依存として読み込み、本体の配布JARには同梱しません。
ビルドでは選択MODの依存解決・リマップも確認しますが、起動互換性は `--run` で別途確認が必要です。
同じMOD IDの複数バージョン、本体・既存のCurios/JEIとの二重導入は拒否します。

TiCEX一式は `bash sh/build/build-with-mods.sh --ticex --offline` で選択できます。
現在の構成は TiCEX 0.7.0-hotfix（beta）、Tinkers’ Construct 3.11.2.166、
Mantle 1.11.117、AddonAPI-DynLoad 2.1.0 です。
JARと配布元・SHA-512の記録は `libs/external/ticex/` に保存しています（Git対象外）。
Tinkers’ Constructは、既存のJEI 15.20.0.129を満たす3.11系を選択しています。

選択式ビルドでは **Embeddium・Oculus・extra_video_settingsを必ず導入**します。
追加MODは選択したものだけです（本体のCurios・JEI依存は引き続き有効）。
軽量化JARが不足している場合はビルド前に停止します。
追加MODなしの場合は `bash sh/build/build-with-mods.sh --performance-only --offline` を使えます。
この操作ではシェーダーパックや既存のゲーム設定を書き換えません。
