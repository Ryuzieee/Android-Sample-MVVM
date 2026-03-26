# Android-Sample-MVVM

PokeAPI を使ったポケモン図鑑アプリ。マルチモジュール構成・Clean Architecture・最新の Jetpack ライブラリを組み合わせた Android サンプルプロジェクト。

---

## 画面

| 一覧画面 | 詳細画面 | 検索画面 | お気に入り画面 |
|---------|---------|---------|------------|
| ポケモンを2列グリッドで無限スクロール表示 | タイプ・ステータス・高さ・重さをスクロール表示。♡ アイコンでお気に入りトグル | ポケモン名でリアルタイム検索（500ms デバウンス） | お気に入り登録ポケモンを2列グリッドで表示 |

---

## 技術スタック

| カテゴリ | ライブラリ | バージョン |
|---------|-----------|---------|
| **Language** | Kotlin | 2.3.20 |
| **Build** | Android Gradle Plugin | 9.0.1 |
| | KSP | 2.0.21-1.0.28 |
| **UI** | Jetpack Compose BOM | 2026.03.00 |
| | Material3 | BOM 経由 |
| | Navigation Compose | 2.9.7 |
| | Coil | 3.4.0 |
| **DI** | Hilt | 2.59.2 |
| **Network** | Retrofit | 3.0.0 |
| | OkHttp | 5.3.2 |
| | kotlinx.serialization | 1.10.0 |
| **DB** | Room | 2.8.4 |
| **非同期** | Coroutines | 1.10.2 |
| **ページング** | Paging 3 | 3.4.2 |
| **Logging** | Timber | 5.0.1 |
| **テスト** | JUnit4 / MockK / Turbine | 4.13.2 / 1.14.9 / 1.2.1 |
| | Robolectric / Roborazzi | 4.16.1 / 1.59.0 |
| **品質** | ktlint / Detekt | 14.2.0 / 1.23.8 |

---

## アーキテクチャ

Clean Architecture + MVVM をベースにマルチモジュール構成を採用。

```
┌──────────────────────────────────────────────────────┐
│                        app                            │  ナビゲーション・DI エントリポイント
├────────────┬────────────┬─────────────┬──────────────┤
│ feature:   │ feature:   │ feature:    │ feature:     │  画面単位の機能モジュール
│   list     │   detail   │   search    │   favorites  │
├────────────┴────────────┴─────────────┴──────────────┤
│  core:ui                                              │  共通 Compose コンポーネント・テーマ
├──────────────────────────────────────────────────────┤
│  core:data                                            │  Repository 実装・API・Room
├──────────────────────────────────────────────────────┤
│  core:domain                                          │  ビジネスロジック（Android 非依存）
└──────────────────────────────────────────────────────┘
```

### レイヤー詳細

#### `core:domain`（Android 非依存の純粋 Kotlin）
- **Model**: `Pokemon`, `PokemonDetail`, `Favorite`, `UiState`, `AppException`
- **Repository Interface**: `PokemonRepository`, `FavoriteRepository`
- **UseCase**: `GetPokemonListUseCase`, `GetPokemonDetailUseCase`, `SearchPokemonUseCase`, `GetFavoritesUseCase`, `GetIsFavoriteUseCase`, `ToggleFavoriteUseCase`

#### `core:data`
- **API**: Retrofit + kotlinx.serialization で PokeAPI を呼び出し
- **Paging**: `PokemonPagingSource`（offset/limit ベース、pageSize=20）
- **Cache**: Room で詳細データを 5 分間キャッシュ（Local-First パターン）
- **DI**: `DataModule`（Hilt）

#### `core:ui`
- **共通コンポーネント**: `LoadingIndicator`, `ErrorContent`, `PokemonIdText`, `PokemonNameText`
- **BaseViewModel**: `StateFlow<S>` + `Channel<UiEvent>` を共通化した抽象 ViewModel
- **UiEvent / ObserveAsEvents**: 一回限りイベント (SnackBar 等) のライフサイクル安全な配信
- Material3 テーマ定義 (Color, Type, Theme)

#### `feature:list` / `feature:detail` / `feature:search` / `feature:favorites`
- `ViewModel` → `UseCase` → `Repository` の一方向データフロー
- UI 状態は `StateFlow<UiState<T>>` で管理
- 一度きりのイベント（Snackbar 等）は `Channel` + `ObserveAsEvents` で管理
- `feature:search`: クエリの `debounce(500ms)` + `flatMapLatest` でリアルタイム検索
- `feature:favorites`: `StateFlow<List<Favorite>>` を Room の Flow から `stateIn` で公開
- `feature:detail`: `isFavorite` を `flatMapLatest` で詳細ロード後に DB 監視開始

### データフロー

```
UI (Compose)
  └─ ViewModel (StateFlow)
       └─ UseCase
            └─ Repository Interface
                 └─ RepositoryImpl
                      ├─ PagingSource → RemoteDataSource → PokeApiService (Retrofit)
                      └─ PokemonDao (Room Cache)
```

---

## モジュール構成

```
Android-Sample-MVVM/
├── app/                        # アプリエントリポイント
│   ├── di/AppModule.kt         # ネットワーク層 DI
│   └── ui/navigation/          # ナビゲーショングラフ
├── core/
│   ├── domain/                 # ドメイン層（Android 非依存）
│   ├── data/                   # データ層
│   └── ui/                     # 共通 UI
├── feature/
│   ├── list/                   # ポケモン一覧画面
│   ├── detail/                 # ポケモン詳細画面（お気に入りトグル含む）
│   ├── search/                 # ポケモン検索画面
│   └── favorites/              # お気に入り一覧画面
└── build-logic/
    └── convention/             # Convention Plugins
```

### Convention Plugins（`build-logic`）

各モジュールのビルド設定を一元管理するカスタム Gradle プラグイン。

| プラグイン ID | 適用モジュール | 内容 |
|-------------|-------------|------|
| `convention.android.application` | `app` | compileSdk=36, minSdk=29, targetSdk=36, JVM 11 |
| `convention.android.library` | `core:*`, `feature:*` | ライブラリ向け共通 Android 設定 |
| `convention.android.compose` | Compose 使用モジュール | `buildFeatures.compose=true`、Stability 設定 |
| `convention.kotlin.library` | `core:domain` | 純粋 Kotlin（Android 非依存）モジュール向け |

---

## ナビゲーション

型安全ナビゲーション（Navigation Compose 2.8+）を採用。

```kotlin
// ルート定義
@Serializable data object PokemonListRoute
@Serializable data class PokemonDetailRoute(val name: String)

// 遷移
navController.navigate(PokemonDetailRoute(pokemonName))
```

画面遷移には `slideInHorizontally` / `slideOutHorizontally`（350ms）アニメーションを適用。

---

## キャッシュ戦略

詳細画面は **Local-First** パターンで実装。

```
getPokemonDetail(name) の呼び出し時:
  1. Room から該当データを取得
  2. キャッシュが存在 かつ 取得から 5 分以内 → キャッシュを返す
  3. それ以外 → API から取得 → Room に保存 → 返す
```

---

## パフォーマンス最適化

### Compose Stability（`compose-stability.conf`）

```
com.yamamuto.android_sample_mvvm.domain.model.Pokemon
com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail.Stat
```

ドメインモデルを Stable としてマークし、不要な recomposition を防止。

### ProGuard / Resource Shrinking

Release ビルドでコード圧縮・リソース圧縮を有効化。

---

## Product Flavors

| Flavor | ApplicationId | 用途 |
|--------|-------------|------|
| `dev` | `com.yamamuto.android_sample_mvvm.dev` | 開発・検証 |
| `prod` | `com.yamamuto.android_sample_mvvm` | 本番リリース |

---

## テスト

### ユニットテスト

```bash
./gradlew test
```

| テストクラス | 対象 | ツール |
|------------|------|--------|
| `PokemonListViewModelTest` | Paging データの出力検証 | MockK + Paging Testing |
| `PokemonDetailViewModelTest` | StateFlow の状態遷移・お気に入りトグル検証 | Turbine + MockK |
| `GetPokemonListUseCaseTest` | PagingData の出力検証 | MockK + Paging Testing |
| `GetPokemonDetailUseCaseTest` | 詳細取得のロジック検証 | MockK |
| `PokemonRepositoryImplTest` | DTO → Domain マッピング検証 | MockK |

### スクリーンショットテスト（Roborazzi）

```bash
# スクリーンショット記録（初回・更新時）
./gradlew :feature:detail:recordRoborazziDebug

# スクリーンショット比較（CI 等）
./gradlew :feature:detail:verifyRoborazziDebug
```

対象：詳細画面の Loading / Error 状態

---

## セットアップ

### 必要環境

- JDK 11 以上
- Android Studio Meerkat 以上（推奨）
- Android SDK API 36

### ビルド

```bash
# 全ビルド（ktlintFormat が自動実行される）
./gradlew build

# 開発用 APK
./gradlew assembleDevDebug

# 本番 APK
./gradlew assembleProdRelease
```

### コード品質

```bash
# フォーマット自動修正
./gradlew ktlintFormat

# 静的解析
./gradlew detekt
```

> **Note**: `preBuild` に `ktlintFormat` を紐付けているため、ビルド前に自動でコードフォーマットが実行されます。

---

## API

[PokeAPI v2](https://pokeapi.co/) を使用。認証不要・無料。

| エンドポイント | 用途 |
|-------------|------|
| `GET /pokemon?limit={n}&offset={m}` | ポケモン一覧取得（ページング） |
| `GET /pokemon/{name}` | ポケモン詳細取得 |
