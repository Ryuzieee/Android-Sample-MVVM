package com.yamamuto.android_sample_mvvm.ui

/** アプリ全体で使用する UI 文字列定数。画面ごとにグルーピングして管理する。 */
object Strings {
    // ── 共通 ──────────────────────────────────────────────

    object Common {
        const val RETRY = "リトライ"
        const val CLOSE = "閉じる"
        const val ERROR_TITLE = "エラー"
    }

    // ── エラー ────────────────────────────────────────────

    object Error {
        const val NETWORK_MESSAGE = "ネットワークに接続できません"
        const val NETWORK_SUB_MESSAGE = "接続を確認してリトライしてください"
        const val SESSION_EXPIRED = "セッションの有効期限が切れました。再度ログインしてください"
        const val FORCE_UPDATE = "新しいバージョンが利用可能です。アップデートしてください"
        const val SERVER_ERROR_FORMAT = "サーバーエラー (%d)"
        const val UNKNOWN_ERROR = "不明なエラーが発生しました"

        fun notFound(query: String): String = "「$query」に一致するポケモンは見つかりませんでした"
    }

    // ── ダイアログ ────────────────────────────────────────

    object Dialog {
        const val SESSION_EXPIRED_TITLE = "セッション切れ"
        const val SESSION_EXPIRED_MESSAGE = "セッションの有効期限が切れました。\n再度ログインしてください。"
        const val SESSION_EXPIRED_BUTTON = "ログイン画面へ"

        const val FORCE_UPDATE_TITLE = "アップデートが必要です"
        const val FORCE_UPDATE_MESSAGE = "新しいバージョンが利用可能です。\nアプリをアップデートしてください。"
        const val FORCE_UPDATE_BUTTON = "ストアを開く"
    }

    // ── 一覧画面 ──────────────────────────────────────────

    object List {
        const val SCREEN_TITLE = "Pokédex"
        const val SEARCH_DESCRIPTION = "検索"
        const val FAVORITES_DESCRIPTION = "お気に入り"
    }

    // ── 詳細画面 ──────────────────────────────────────────

    object Detail {
        const val INFO_BUTTON_DESCRIPTION = "詳細情報を表示"
        const val REMOVE_FAVORITE_DESCRIPTION = "お気に入りから削除"
        const val ADD_FAVORITE_DESCRIPTION = "お気に入りに追加"
        const val BOTTOM_SHEET_TITLE = "くわしい情報"
        const val LABEL_CATEGORY = "分類"
        const val LABEL_GENERATION = "世代"
        const val LABEL_HABITAT = "生息地"
        const val LABEL_CAPTURE_RATE = "捕獲率"
        const val LABEL_EGG_GROUP = "タマゴグループ"
        const val LABEL_GENDER_RATIO = "性別比率"
        const val LABEL_NO_GENDER = "性別なし"
        const val LABEL_ABILITIES = "とくせい"
        const val LABEL_HIDDEN_ABILITY = "かくれとくせい"
        const val LABEL_BASE_EXPERIENCE = "きそけいけんち"
        const val SECTION_EVOLUTION = "しんか"
        const val SECTION_BASE_STATS = "しゅぞくち"
        const val EGG_GROUP_SEPARATOR = "、"
        const val LABEL_HEIGHT = "たかさ"
        const val LABEL_WEIGHT = "おもさ"
        const val UNIT_CM = "cm"
        const val UNIT_KG = "kg"
        const val EVOLUTION_ARROW = "→"
        const val EVOLUTION_LEVEL_PREFIX = "Lv."
        const val GENDER_FEMALE = "♀"
        const val GENDER_MALE = "♂"

        fun heightWeight(
            heightCm: Int,
            weightKg: Double,
        ): String = "$LABEL_HEIGHT: $heightCm $UNIT_CM ・ $LABEL_WEIGHT: $weightKg $UNIT_KG"

        fun genderRatio(
            femalePercent: Double,
            malePercent: Double,
        ): String = "$GENDER_FEMALE $femalePercent% / $GENDER_MALE $malePercent%"
    }

    // ── 検索画面 ──────────────────────────────────────────

    object Search {
        const val SEARCH_PLACEHOLDER = "ポケモン名を入力..."
        const val SEARCH_IDLE_MESSAGE = "ポケモン名を入力してください"
    }

    // ── お気に入り画面 ────────────────────────────────────

    object Favorites {
        const val SCREEN_TITLE = "お気に入り"
        const val EMPTY_MESSAGE = "お気に入りがありません"
        const val EMPTY_SUB_MESSAGE = "詳細画面のハートアイコンから追加できます"
    }

    // ── PokeAPI 英語→日本語変換 ──────────────────────────

    object Translation {
        private val types =
            mapOf(
                "normal" to "ノーマル",
                "fire" to "ほのお",
                "water" to "みず",
                "electric" to "でんき",
                "grass" to "くさ",
                "ice" to "こおり",
                "fighting" to "かくとう",
                "poison" to "どく",
                "ground" to "じめん",
                "flying" to "ひこう",
                "psychic" to "エスパー",
                "bug" to "むし",
                "rock" to "いわ",
                "ghost" to "ゴースト",
                "dragon" to "ドラゴン",
                "dark" to "あく",
                "steel" to "はがね",
                "fairy" to "フェアリー",
            )

        private val stats =
            mapOf(
                "hp" to "HP",
                "attack" to "こうげき",
                "defense" to "ぼうぎょ",
                "special-attack" to "とくこう",
                "special-defense" to "とくぼう",
                "speed" to "すばやさ",
            )

        private val eggGroups =
            mapOf(
                "monster" to "かいじゅう",
                "water1" to "すいちゅう1",
                "water2" to "すいちゅう2",
                "water3" to "すいちゅう3",
                "bug" to "むし",
                "flying" to "ひこう",
                "ground" to "りくじょう",
                "fairy" to "ようせい",
                "plant" to "しょくぶつ",
                "humanshape" to "ひとがた",
                "mineral" to "こうぶつ",
                "indeterminate" to "ふていけい",
                "ditto" to "メタモン",
                "dragon" to "ドラゴン",
                "no-eggs" to "タマゴ未発見",
            )

        private val habitats =
            mapOf(
                "cave" to "どうくつ",
                "forest" to "もり",
                "grassland" to "そうげん",
                "mountain" to "やま",
                "rare" to "きちょう",
                "rough-terrain" to "あれち",
                "sea" to "うみ",
                "urban" to "まち",
                "waters-edge" to "みずべ",
            )

        private val generations =
            mapOf(
                "generation-i" to "第1世代",
                "generation-ii" to "第2世代",
                "generation-iii" to "第3世代",
                "generation-iv" to "第4世代",
                "generation-v" to "第5世代",
                "generation-vi" to "第6世代",
                "generation-vii" to "第7世代",
                "generation-viii" to "第8世代",
                "generation-ix" to "第9世代",
            )

        fun type(key: String): String = types[key] ?: key

        fun stat(key: String): String = stats[key] ?: key

        fun eggGroup(key: String): String = eggGroups[key] ?: key

        fun habitat(key: String): String = habitats[key] ?: key

        fun generation(key: String): String = generations[key] ?: key
    }
}
