package com.yamamuto.android_sample_mvvm.domain.translation

/**
 * PokeAPI が返す英語キー (タイプ / 種族値 / タマゴグループ / 生息地 / 世代) を
 * 日本語の表示文字列に変換するドメイン辞書。
 *
 * UI 層に置かず Domain 層に置くのは、辞書が表示文言ではなく
 * 「ドメイン用語の対訳」だから (将来 PokeAPI 多言語エンドポイントに切り替えた場合の
 * 差し替え単位がここになる)。
 */
object PokemonTermLocalizer {
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
