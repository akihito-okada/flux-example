# このアプリについて

## **アーキテクチャ**

### **Flux**

本プロジェクトでは、[DroidKaigi 2019](https://github.com/DroidKaigi/conference-app-2019) の Flux アーキテクチャを参考にしています。

- **Action** は `Sealed Class` で管理。
- **ActionCreator** から **Store** へのデータの受け渡しは `Dispatcher` を用いて `Coroutines Flow` で処理。
- **Coroutines Flow** から **LiveData** への変換は `拡張関数` を使用。

### **Activity / Fragment**

基本構成は **1 Activity: n Fragment** としています。

- 各 **Fragment** 毎に `Action Creator` および `Store` を管理。
- **Activity** でも `Action Creator` および `Store` を持ち、Snackbar や Dialog の表示など画面共通処理を管理。

### **マルチモジュール構成**

本プロジェクトは **マルチモジュール構成** で管理しており、各モジュールの役割は以下の通りです。

- `:shared` - 画像や文字リソースの管理
- `:model` - `data class` や `enum` の定義
- `:data:preferences` - `SharedPreferences` の管理
- `:data:remote` - API や Firebase との通信
- `:data:repository` - Repository 層
- `:feature:common` - 共通のロジックや UI コンポーネント
- `:feature:store` - ストア機能
- `:feature:toys` - おもちゃ機能
- `:app` - Application, Activity, DI の管理

### **画面遷移 (Screen Transition)**

ログイン後の画面では、`BottomNavigationView` の各アイテムごとに **スタックを管理** し、 アイテムを切り替えても状態が保持されるように実装しています。

管理は [FragNav](https://github.com/ncapdevi/FragNav) をベースにした `TabStackManager` で行っています。

各 `Fragment` への遷移は `Navigator` インターフェースを `Dagger` で `Inject` して使用しています。

### **画面間のイベント同期**

Store を経由して View で受け取るイベントには、以下の要件を満たす必要があります。

1. 他の画面のイベントも受け取りたい
2. 画面ごとに閉じて受け取りたい
3. 画面再生成後にも受け取りたい
4. 一度だけ受け取りたい

これを実現するために、以下の方法を採用しています。

| 要件                      | 方法                                                      |
| ----------------------- | ------------------------------------------------------- |
| **1. 他の画面のイベントも受け取りたい** | グローバル `Action` を作成し、各 `Store` で `subscribe`             |
| **2. 画面ごとに閉じて受け取りたい**   | `TaggedDispatcherHandler` で `tagId` を設定し、画面ごとに `filter` |
| **3. 画面再生成後にも受け取りたい**   | `ViewModel` を継承した `Store` で `LiveData.observe()`        |
| **4. 一度だけ受け取りたい**       | `Event` クラスを利用し `getContentIfNotHandled()` で処理          |

### **RecyclerView の管理**

- `Groupie` を利用。
- 共通のアイテムは `feature/common` に配置。
- `RecyclerViewPoolStore` を `ViewModel` で管理し、レイアウトの `inflate` 負荷を軽減。

### **Delegate パターンの活用**

クラス間で共通処理を `Class Delegate` を用いて共通化。

| Delegate クラス                | 役割             |
| --------------------------- | -------------- |
| `ErrorHandlerDelegate`      | API のエラーハンドリング |
| `ValidationHandlerDelegate` | 入力バリデーションの統一   |

## **テスト (Unit Test)**

- 重要な処理にはテストを実装。
- `Spek` を活用し `IntegrationTest` も実施。

## **CI/CD**

本プロジェクトでは `Bitrise` を利用し、以下のジョブを定義しています。

- **Push** (`develop` / `master` マージ時)
    - `Play Console` (βテスト) へ `aab` を `gradle-play-publisher` で `publish`
    - `App Distribution` に `apk` を `publish`
- **Pull Request**
    - `Github Actions` で `Unit-Test` と `Danger` を実行

## **リリース (Release)**

Bitrise の `Push` ジョブで生成した `aab` を `Play Console` で時限指定公開。

- `release/hotfix` ブランチを `master/develop` にマージ
- `GitHub Release` を作成
- 関連する `GitHub Issues` をクローズ

## **ブランチ運用 (Branch Work)**

ブランチ作成は `git-flow` に準拠。

- `develop` へのマージは `Pull Request` を作成し、承認後に実施。
- `release` ブランチの `master/develop` へのマージも `Pull Request` で管理。

## **ツール & コマンド**

### **ユニットテスト**

```sh
./gradlew testDevelopmentDebugUnitTest
```

### **Lint チェック**

```sh
./gradlew lintDevelopmentDebug
```

### **Ktlint チェック**

```sh
./gradlew --continue ktlintCheck
```

### **Ktlint 自動フォーマット**

```sh
./gradlew --continue ktlintFormat
```

### **未使用リソースの削除**

```sh
./gradlew removeUnusedResources
```

