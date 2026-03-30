# Android-Sample-MVVM

PokeAPI を使ったポケモン図鑑アプリ。チーム開発のベースコードとして、Clean Architecture・Jetpack Compose・Hilt を組み合わせた Android サンプルプロジェクト。

---

## 画面

| 一覧画面 | 詳細画面 | 検索画面 | お気に入り画面 |
|---------|---------|---------|------------|
| ポケモンを2列グリッドで無限スクロール表示（自前ページネーション） | タイプ・ステータス・進化チェーン・詳細情報をスクロール表示。ハートアイコンでお気に入りトグル | ポケモン名でリアルタイム検索（500ms デバウンス） | お気に入り登録ポケモンを2列グリッドで表示 |

---

## 技術スタック

| カテゴリ | ライブラリ | バージョン |
|---------|-----------|---------|
| **Language** | Kotlin | 2.3.20 |
| **Build** | Android Gradle Plugin | 9.0.1 |
| | KSP | 2.0.21-1.0.28 |
| **UI** | Jetpack Compose BOM | 2026.03.01 |
| | Material3 | BOM 経由 |
| | Navigation Compose | 2.9.7 |
| | Coil | 3.4.0 |
| **DI** | Hilt | 2.59.2 |
| **Network** | Retrofit | 3.0.0 |
| | OkHttp | 5.3.2 |
| | kotlinx.serialization | 1.10.0 |
| **DB** | Room | 2.8.4 |
| **非同期** | Coroutines | 1.10.2 |
| **Logging** | Timber | 5.0.1 |
| **テスト** | JUnit4 / MockK / Turbine | 4.13.2 / 1.14.9 / 1.2.1 |
| **品質** | ktlint / Detekt | 14.2.0 / 1.23.8 |

---

## アーキテクチャ

Clean Architecture + MVVM。3モジュール構成でシンプルに保ちつつ、パッケージで責務を分離。

```
┌──────────────────────────────────────────────┐
│                     app                       │  ナビゲーション・DI エントリポイント
├──────────────────────────────────────────────┤
│                   feature                     │  画面単位の UI・ViewModel
│   list / detail / search / favorites          │  （パッケージで分離）
├──────────────────────────────────────────────┤
│                    core                       │  ドメイン・データ・共通 UI・テスト補助
│   domain / data / ui / testing                │  （パッケージで分離）
└──────────────────────────────────────────────┘
```

### パッケージ構成

#### `core` — `domain`（ビジネスロジック）
- **Model**: `PokemonDetailModel`, `PokemonSpeciesModel`, `EvolutionStageModel`, `FavoriteModel`, `PokemonSummaryModel`, `PokemonFullDetailModel`, `AppException`
- **Repository Interface**: `PokemonRepository`, `FavoriteRepository`
- **UseCase**: `GetPokemonListUseCase`, `GetPokemonDetailUseCase`, `GetPokemonFullDetailUseCase`, `GetPokemonSpeciesUseCase`, `GetEvolutionChainUseCase`, `GetAbilityJapaneseNameUseCase`, `SearchPokemonUseCase`, `GetFavoritesUseCase`, `GetIsFavoriteUseCase`, `ToggleFavoriteUseCase`

> ViewModel → UseCase → Repository の一方向データフローを統一。パススルーであっても必ず UseCase を経由する。

#### `core` — `data`（データ層）
- **API**: Retrofit + kotlinx.serialization で PokeAPI を呼び出し
- **Cache**: Room で詳細データをキャッシュ（debug: 1分 / release: 5分）。TypeConverters に kotlinx.serialization を使用
- **Mapper**: `data/mapper/` パッケージに集約。命名規則は `.toModel()`（→ ドメイン）/ `.toEntity()`（→ Entity）
- **Repository Handler**: `handleWithCache` / `handleRemote` / `handleLocal` で例外を `AppException` に変換し `Result` で返す
- **DI**: `DataModule`（Hilt `@Binds` + `@Provides`）

#### `core` — `ui`（共通 UI）
- **コンポーネント**: `AppScaffold`, `AppText`, `AppBottomSheet`, `AppLazyVerticalGrid`（末尾到達検知付き）, `EmptyContent`, `ErrorContent`, `LoadingIndicator`, `PokemonCard`, `PokemonImage`, `SearchTextField`, `UiStateContent`
- **文字列定数**: `Strings.kt` にアプリ全体の UI 文字列を画面ごとにグルーピング
- **ユーティリティ**: `UiState`（Idle / Loading / Success / Error）、`Result<T>.toUiState()` 拡張関数
- **テーマ**: Material3 テーマ定義（Color, Type, Theme）

#### `core` — `testing`（テスト補助）
- `MainDispatcherRule`、`TestFixtures`

#### `feature`（各画面）
- 各 ViewModel は `ViewModel()` を直接継承し `MutableStateFlow` で状態管理（基底クラスなし）
- UI 状態は `UiStateContent` で Loading / Error / Success / Idle を統一描画
- UiState のプロパティ名は `content` に統一
- `search`: `Job` + `delay(500ms)` でデバウンス検索
- `favorites`: suspend で取得し `toUiState()` で変換
- `detail`: 進化チェーン表示、BottomSheet による詳細情報、お気に入りトグル

### データフロー

```
UI (Compose)
  └─ ViewModel (StateFlow)
       └─ UseCase
            └─ Repository Interface
                 └─ RepositoryImpl
                      ├─ RemoteDataSource → PokeApiService (Retrofit)
                      └─ PokemonDao (Room Cache)
```

---

## データモデル

3種類のデータモデルを層ごとに分離し、各レイヤーの関心事を閉じ込める。

| 種類 | パッケージ | 用途 | 命名規則 | 依存 |
|------|----------|------|---------|------|
| **Response (DTO)** | `data/api/dto/` | API のレスポンスを忠実に表現する | `~Response`（ネストされた子要素は `~Response.Item` 等） | `kotlinx.serialization` |
| **Entity** | `data/local/entity/` | Room テーブルの1行に対応するキャッシュ用データ | `~Entity` | `androidx.room` |
| **Model** | `domain/model/` | ドメインロジック・UI が扱う純粋なデータ | `~Model` | フレームワーク非依存 |

### 変換の流れ

```
API Response (DTO)  ──toEntity()──▶  Entity  ──toModel()──▶  Model
                                       ▲                       │
                                       │                       ▼
                                    Room DB               ViewModel / UI
```

- **`.toEntity()`**: DTO → Entity。API レスポンスをキャッシュ可能な形に変換
- **`.toModel()`**: Entity → Model。DB データをドメインモデルに変換
- `handleWithCache` はこの変換パイプラインを `fetch` → `toEntity` → `save` + `toModel` として一元管理

### 具体例

| Response (DTO) | Entity | Model |
|----------------|--------|-------|
| `PokemonDetailResponse` | `PokemonDetailEntity` | `PokemonDetailModel` |
| `PokemonListResponse` / `.Item` | `PokemonNameEntity` | `PokemonSummaryModel` |
| `PokemonSpeciesResponse` | — (キャッシュなし) | `PokemonSpeciesModel` |
| `EvolutionChainResponse` | — (キャッシュなし) | `EvolutionStageModel` |
| `AbilityResponse` | — (キャッシュなし) | `Map<String, String>` |
| — | `FavoriteEntity` | `FavoriteModel` |

> キャッシュ対象でない API レスポンスは Entity を経由せず、`handleRemote` で直接 Model に変換する。

---

## モジュール構成

```
Android-Sample-MVVM/
├── app/                           # アプリエントリポイント
│   ├── di/                        # ネットワーク層 DI（Interceptor 等）
│   ├── ui/navigation/             # ナビゲーショングラフ
│   └── ui/component/              # App レベルの Composable（AppEventDialogs）
├── core/
│   └── src/main/java/
│       ├── domain/                # ドメイン層（model / repository / usecase）
│       ├── data/                  # データ層（api / local / repository / mapper / di）
│       ├── ui/                    # 共通 UI（component / theme / util / Strings.kt）
│       └── testing/               # テスト補助（MainDispatcherRule / TestFixtures）
├── feature/
│   └── src/main/java/
│       ├── list/                  # ポケモン一覧画面
│       ├── detail/                # ポケモン詳細画面
│       ├── search/                # ポケモン検索画面
│       └── favorites/             # お気に入り一覧画面
└── build-logic/                  # Convention Plugins
```

### Convention Plugins（`build-logic`）

各モジュールのビルド設定を一元管理するカスタム Gradle プラグイン。

| プラグイン ID | 適用モジュール | 内容 |
|-------------|-------------|------|
| `convention.android.application` | `app` | compileSdk=36, minSdk=29, targetSdk=36, JVM 11 |
| `convention.android.library` | `core`, `feature` | ライブラリ向け共通 Android 設定 |
| `convention.android.compose` | `core`, `feature` | `buildFeatures.compose=true`、Stability 設定 |
| `convention.feature` | `feature` | Hilt / KSP / ktlint / 共通依存を一括適用。namespace 自動導出 |

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

遷移アニメーションは `pushComposable` / `modalComposable` で宣言し、`AppNavGraph` は遷移の種類を知らない。

| 関数 | アニメーション | 用途 |
|------|-------------|------|
| `pushComposable` | 水平スライド（350ms） | 一覧 → 詳細など通常遷移 |
| `modalComposable` | 下から上スライド（350ms） | 検索・お気に入りなどモーダル的遷移 |

---

## キャッシュ戦略

詳細画面は **Local-First** パターンで実装。

```
getPokemonDetail(name) の呼び出し時:
  1. Room から該当データを取得
  2. キャッシュが存在 かつ 有効期間内 → キャッシュを返す
     （debug: 1分 / release: 5分）
  3. それ以外 → API から取得 → toEntity() で変換 → Room に保存 → toModel() で返す
```

`handleWithCache` は `fetch` → `toEntity` → `save` + `toModel` のパイプラインを一元管理し、例外を `AppException` に変換して `Result` で返す。

---

## パフォーマンス最適化

### Compose Stability（`compose-stability.conf`）

```
com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel.Stat
```

ドメインモデルを Stable としてマークし、不要な recomposition を防止。

### ProGuard / Resource Shrinking

Release ビルドでコード圧縮・リソース圧縮を有効化。

---

## Product Flavors

| Flavor | ApplicationId | 用途 |
|--------|-------------|------|
| `dev` | `com.yamamuto.android_sample_mvvm.dev` | 開発・検証 |
| `mock` | `com.yamamuto.android_sample_mvvm.mock` | モックデータで動作確認 |
| `prod` | `com.yamamuto.android_sample_mvvm` | 本番リリース |

---

## テスト

```bash
./gradlew :core:testDebugUnitTest :feature:testDebugUnitTest
```

| レイヤー | テストクラス | ツール |
|---------|------------|--------|
| **Mapper** | `PokemonDetailMapperTest`, `PokemonSpeciesMapperTest`, `AbilityMapperTest`, `FavoriteMapperTest`, `PokemonNameMapperTest` | JUnit |
| **Repository Handler** | `RepositoryHandlerTest` | JUnit |
| **Repository** | `PokemonRepositoryImplTest`, `FavoriteRepositoryImplTest` | MockK |
| **UseCase** | `GetPokemonFullDetailUseCaseTest`, `GetPokemonSpeciesUseCaseTest`, `GetEvolutionChainUseCaseTest`, `GetAbilityJapaneseNameUseCaseTest`, `SearchPokemonUseCaseTest`, `ToggleFavoriteUseCaseTest`, `GetFavoritesUseCaseTest`, `GetIsFavoriteUseCaseTest` | MockK |
| **ViewModel** | `PokemonListViewModelTest`, `PokemonDetailViewModelTest`, `SearchViewModelTest`, `FavoritesViewModelTest` | Turbine + MockK |

ViewModel テストは Turbine の `test {}` ブロックで StateFlow を検証し、`awaitItem()` / `expectMostRecentItem()` で状態遷移を追跡する。

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

# モック APK
./gradlew assembleMockDebug

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
| `GET /pokemon-species/{name}` | 種族情報（日本語名・分類・世代等） |
| `GET /evolution-chain/{id}` | 進化チェーン取得 |
| `GET /ability/{id}` | とくせい（日本語名）取得 |
