package com.yamamuto.android_sample_mvvm.network

import android.content.res.AssetManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * モックフレーバー用の JSON レスポンスデータ。
 *
 * JSON テンプレートは `assets/mock/` に配置し、ktlint の影響を受けない。
 * テンプレート内の `{{key}}` をポケモンデータで置換して返す。
 */
@Singleton
class MockData
    @Inject
    constructor(
        private val assetManager: AssetManager,
    ) {
        private val templates = mutableMapOf<String, String>()

        private fun template(name: String): String =
            templates.getOrPut(name) {
                assetManager.open("mock/$name").bufferedReader().readText()
            }

        private data class Pokemon(
            val id: Int,
            val name: String,
            val jaName: String,
            val height: Int,
            val weight: Int,
            val baseExperience: Int,
            val types: List<String>,
            val abilities: List<String>,
            val hiddenAbility: String?,
            val stats: Stats,
            val genus: String,
            val flavorText: String,
            val habitat: String?,
            val generation: String,
            val eggGroups: List<String>,
            val genderRate: Int,
            val captureRate: Int,
        )

        private data class Stats(
            val hp: Int,
            val attack: Int,
            val defense: Int,
            val spAttack: Int,
            val spDefense: Int,
            val speed: Int,
        )

        // region Pokemon data

        private val pokemons =
            listOf(
                Pokemon(
                    1,
                    "bulbasaur",
                    "フシギダネ",
                    7,
                    69,
                    64,
                    listOf("grass", "poison"),
                    listOf("overgrow"),
                    "chlorophyll",
                    Stats(45, 49, 49, 65, 65, 45),
                    "たねポケモン",
                    "うまれたときから　せなかに\nふしぎな　タネが　うえてあり\nからだと　ともに　そだつという。",
                    "grassland",
                    "generation-i",
                    listOf("monster", "grass"),
                    1,
                    45,
                ),
                Pokemon(
                    2,
                    "ivysaur",
                    "フシギソウ",
                    10,
                    130,
                    142,
                    listOf("grass", "poison"),
                    listOf("overgrow"),
                    "chlorophyll",
                    Stats(60, 62, 63, 80, 80, 60),
                    "たねポケモン",
                    "つぼみが　せなかに　ついていて\nようぶんを　きゅうしゅうしていくと\nおおきな　はなが　さくという。",
                    "grassland",
                    "generation-i",
                    listOf("monster", "grass"),
                    1,
                    45,
                ),
                Pokemon(
                    3,
                    "venusaur",
                    "フシギバナ",
                    20,
                    1000,
                    263,
                    listOf("grass", "poison"),
                    listOf("overgrow"),
                    "chlorophyll",
                    Stats(80, 82, 83, 100, 100, 80),
                    "たねポケモン",
                    "はなから　うっとりする　かおりが\nただよい　たたかう　ものの\nきもちを　なだめてしまう。",
                    "grassland",
                    "generation-i",
                    listOf("monster", "grass"),
                    1,
                    45,
                ),
                Pokemon(
                    4,
                    "charmander",
                    "ヒトカゲ",
                    6,
                    85,
                    62,
                    listOf("fire"),
                    listOf("blaze"),
                    "solar-power",
                    Stats(39, 52, 43, 60, 50, 65),
                    "とかげポケモン",
                    "うまれたときから　しっぽに\nほのおが　ともっている。ほのおが\nきえたとき　その　いのちは　おわる。",
                    "mountain",
                    "generation-i",
                    listOf("monster", "dragon"),
                    1,
                    45,
                ),
                Pokemon(
                    5,
                    "charmeleon",
                    "リザード",
                    11,
                    190,
                    142,
                    listOf("fire"),
                    listOf("blaze"),
                    "solar-power",
                    Stats(58, 64, 58, 80, 65, 80),
                    "かえんポケモン",
                    "たたかいで　きぶんが　たかまると\nしっぽの　さきから　あかるい\nほのおを　ふきだす　らんぼうもの。",
                    "mountain",
                    "generation-i",
                    listOf("monster", "dragon"),
                    1,
                    45,
                ),
                Pokemon(
                    6,
                    "charizard",
                    "リザードン",
                    17,
                    905,
                    267,
                    listOf("fire", "flying"),
                    listOf("blaze"),
                    "solar-power",
                    Stats(78, 84, 78, 109, 85, 100),
                    "かえんポケモン",
                    "ちきゅうじょうの　あらゆるものを\nやきつくす　ほのおを　はける。\nもりかじの　げんいんに　なる。",
                    "mountain",
                    "generation-i",
                    listOf("monster", "dragon"),
                    1,
                    45,
                ),
                Pokemon(
                    7,
                    "squirtle",
                    "ゼニガメ",
                    5,
                    90,
                    63,
                    listOf("water"),
                    listOf("torrent"),
                    "rain-dish",
                    Stats(44, 48, 65, 50, 64, 43),
                    "かめのこポケモン",
                    "うまれると　せなかの　コウラが\nふくらんで　かたくなる。くちから\nいきおいよく　あわを　ふく。",
                    "waters-edge",
                    "generation-i",
                    listOf("monster", "water1"),
                    1,
                    45,
                ),
                Pokemon(
                    8,
                    "wartortle",
                    "カメール",
                    10,
                    225,
                    142,
                    listOf("water"),
                    listOf("torrent"),
                    "rain-dish",
                    Stats(59, 63, 80, 65, 80, 58),
                    "かめポケモン",
                    "ながいきの　シンボルとして\nにんきが　ある。ふさふさの　みみと\nしっぽの　けが　ながいきの　しょうこ。",
                    "waters-edge",
                    "generation-i",
                    listOf("monster", "water1"),
                    1,
                    45,
                ),
                Pokemon(
                    9,
                    "blastoise",
                    "カメックス",
                    16,
                    855,
                    265,
                    listOf("water"),
                    listOf("torrent"),
                    "rain-dish",
                    Stats(79, 83, 100, 85, 105, 78),
                    "こうらポケモン",
                    "こうらの　ロケットほうから\nふきだす　みずは　はがねの いたも\nぶちぬく　はかいりょくだ。",
                    "waters-edge",
                    "generation-i",
                    listOf("monster", "water1"),
                    1,
                    45,
                ),
                Pokemon(
                    25,
                    "pikachu",
                    "ピカチュウ",
                    4,
                    60,
                    112,
                    listOf("electric"),
                    listOf("static"),
                    "lightning-rod",
                    Stats(35, 55, 40, 50, 50, 90),
                    "ねずみポケモン",
                    "ほっぺたの　でんきぶくろに\nでんきを　ためている。ピンチのとき\nに　ほうでんする。",
                    "forest",
                    "generation-i",
                    listOf("field", "fairy"),
                    4,
                    190,
                ),
                Pokemon(
                    26,
                    "raichu",
                    "ライチュウ",
                    8,
                    300,
                    243,
                    listOf("electric"),
                    listOf("static"),
                    "lightning-rod",
                    Stats(60, 90, 55, 90, 80, 110),
                    "ねずみポケモン",
                    "でんきを　ためすぎて　こうふんすると\nかがやく。くらやみで　ひかる。",
                    "forest",
                    "generation-i",
                    listOf("field", "fairy"),
                    4,
                    75,
                ),
                Pokemon(
                    133,
                    "eevee",
                    "イーブイ",
                    3,
                    65,
                    65,
                    listOf("normal"),
                    listOf("run-away", "adaptability"),
                    "anticipation",
                    Stats(55, 55, 50, 45, 65, 55),
                    "しんかポケモン",
                    "ふきそくな　いでんしを\nもっている。いしの　ほうしゃせんで\nからだが　とつぜんへんいする。",
                    "urban",
                    "generation-i",
                    listOf("field"),
                    1,
                    45,
                ),
                Pokemon(
                    143,
                    "snorlax",
                    "カビゴン",
                    21,
                    4600,
                    189,
                    listOf("normal"),
                    listOf("immunity", "thick-fat"),
                    "gluttony",
                    Stats(160, 110, 65, 65, 110, 30),
                    "いねむりポケモン",
                    "いちにちに　たべものを\n400キロ　たべないと\nきが　すまない。たべおわると　ねむる。",
                    "mountain",
                    "generation-i",
                    listOf("monster"),
                    1,
                    25,
                ),
                Pokemon(
                    150,
                    "mewtwo",
                    "ミュウツー",
                    20,
                    1220,
                    340,
                    listOf("psychic"),
                    listOf("pressure"),
                    "unnerve",
                    Stats(106, 110, 90, 154, 90, 130),
                    "いでんしポケモン",
                    "いでんし　そうさによって\nつくられた　ポケモン。にんげんの\nかがくりょくで　からだは　つくれても\nやさしい　こころを　つくることは　できなかった。",
                    "rare",
                    "generation-i",
                    listOf("undiscovered"),
                    -1,
                    3,
                ),
                Pokemon(
                    151,
                    "mew",
                    "ミュウ",
                    4,
                    40,
                    300,
                    listOf("psychic"),
                    listOf("synchronize"),
                    null,
                    Stats(100, 100, 100, 100, 100, 100),
                    "しんしゅポケモン",
                    "すべての　ポケモンの　いでんしを\nもつと　いわれている。あらゆる\nわざを　つかうことが　できる。",
                    "rare",
                    "generation-i",
                    listOf("undiscovered"),
                    -1,
                    45,
                ),
                Pokemon(
                    249,
                    "lugia",
                    "ルギア",
                    52,
                    2160,
                    340,
                    listOf("psychic", "flying"),
                    listOf("pressure"),
                    "multiscale",
                    Stats(106, 90, 130, 90, 154, 110),
                    "せんすいポケモン",
                    "あまりにも　つよすぎる　ちからを\nもつため　ふかい　うみのそこで\nしずかに　ときを　すごしている。",
                    "sea",
                    "generation-ii",
                    listOf("undiscovered"),
                    -1,
                    3,
                ),
                Pokemon(
                    384,
                    "rayquaza",
                    "レックウザ",
                    70,
                    2065,
                    340,
                    listOf("dragon", "flying"),
                    listOf("air-lock"),
                    null,
                    Stats(105, 150, 90, 150, 90, 95),
                    "てんくうポケモン",
                    "オゾンそうの　なかを　なんおくねんも\nとびつづけていた　ポケモン。\nグラードンと　カイオーガの　あらそいを\nしずめたと　いう　でんせつがある。",
                    null,
                    "generation-iii",
                    listOf("undiscovered"),
                    -1,
                    45,
                ),
                Pokemon(
                    448,
                    "lucario",
                    "ルカリオ",
                    12,
                    540,
                    184,
                    listOf("fighting", "steel"),
                    listOf("steadfast", "inner-focus"),
                    "justified",
                    Stats(70, 110, 70, 115, 70, 90),
                    "はどうポケモン",
                    "あらゆる　ものが　はっする\nはどうを　キャッチする　のうりょくを\nもつ。にんげんの　ことばも　わかる。",
                    null,
                    "generation-iv",
                    listOf("field", "human-like"),
                    1,
                    45,
                ),
                Pokemon(
                    658,
                    "greninja",
                    "ゲッコウガ",
                    15,
                    400,
                    239,
                    listOf("water", "dark"),
                    listOf("torrent"),
                    "protean",
                    Stats(72, 95, 67, 103, 71, 122),
                    "しのびポケモン",
                    "にんじゃのように　しんしゅつきぼつ。\nこうそくいどうで　ほんろうしつつ\nみずの　しゅりけんで　きりさく。",
                    null,
                    "generation-vi",
                    listOf("water1"),
                    1,
                    45,
                ),
                Pokemon(
                    778,
                    "mimikyu",
                    "ミミッキュ",
                    2,
                    7,
                    167,
                    listOf("ghost", "fairy"),
                    listOf("disguise"),
                    null,
                    Stats(55, 90, 80, 50, 105, 96),
                    "ばけのかわポケモン",
                    "さびしがりやの　ポケモン。\nピカチュウの　すがたに　ばけると\nなかよく　してもらえると　きいたのだ。",
                    null,
                    "generation-vii",
                    listOf("amorphous"),
                    4,
                    45,
                ),
            )

        private val abilityJaNames =
            mapOf(
                "overgrow" to "しんりょく",
                "chlorophyll" to "ようりょくそ",
                "blaze" to "もうか",
                "solar-power" to "サンパワー",
                "torrent" to "げきりゅう",
                "rain-dish" to "あめうけざら",
                "static" to "せいでんき",
                "lightning-rod" to "ひらいしん",
                "run-away" to "にげあし",
                "adaptability" to "てきおうりょく",
                "anticipation" to "きけんよち",
                "immunity" to "めんえき",
                "thick-fat" to "あついしぼう",
                "gluttony" to "くいしんぼう",
                "pressure" to "プレッシャー",
                "unnerve" to "きんちょうかん",
                "synchronize" to "シンクロ",
                "multiscale" to "マルチスケイル",
                "air-lock" to "エアロック",
                "steadfast" to "ふくつのこころ",
                "inner-focus" to "せいしんりょく",
                "justified" to "せいぎのこころ",
                "protean" to "へんげんじざい",
                "disguise" to "ばけのかわ",
            )

        // endregion

        private val pokemonByName = pokemons.associateBy { it.name }

        fun pokemonList(): String {
            val results =
                pokemons.joinToString(",") { p ->
                    """{"name":"${p.name}","url":"https://pokeapi.co/api/v2/pokemon/${p.id}/"}"""
                }
            return """{"count":${pokemons.size},"results":[$results]}"""
        }

        fun pokemonDetail(name: String): String {
            val p = pokemonByName[name] ?: pokemons.first()
            val types =
                p.types
                    .mapIndexed { i, t ->
                        """{"slot":${i + 1},"type":{"name":"$t","url":"https://pokeapi.co/api/v2/type/$t/"}}"""
                    }.joinToString(",")
            val allAbilities =
                p.abilities.map { it to false } +
                    listOfNotNull(p.hiddenAbility?.let { it to true })
            val abilities =
                allAbilities
                    .mapIndexed { i, (a, hidden) ->
                        """{"ability":{"name":"$a","url":"https://pokeapi.co/api/v2/ability/$a/"},"is_hidden":$hidden,"slot":${i + 1}}"""
                    }.joinToString(",")
            val statNames = listOf("hp", "attack", "defense", "special-attack", "special-defense", "speed")
            val statValues = listOf(p.stats.hp, p.stats.attack, p.stats.defense, p.stats.spAttack, p.stats.spDefense, p.stats.speed)
            val stats =
                statNames
                    .zip(statValues) { n, v ->
                        """{"base_stat":$v,"effort":0,"stat":{"name":"$n","url":"https://pokeapi.co/api/v2/stat/$n/"}}"""
                    }.joinToString(",")

            return template("pokemon_detail.json")
                .replace("{{id}}", p.id.toString())
                .replace("{{name}}", p.name)
                .replace("{{height}}", p.height.toString())
                .replace("{{weight}}", p.weight.toString())
                .replace("{{baseExperience}}", p.baseExperience.toString())
                .replace("{{types}}", types)
                .replace("{{abilities}}", abilities)
                .replace("{{stats}}", stats)
        }

        fun pokemonSpecies(name: String): String {
            val p = pokemonByName[name] ?: pokemons.first()
            val habitatJson =
                if (p.habitat != null) {
                    """{"name":"${p.habitat}","url":"https://pokeapi.co/api/v2/pokemon-habitat/${p.habitat}/"}"""
                } else {
                    "null"
                }
            val eggGroupsJson =
                p.eggGroups.joinToString(",") { eg ->
                    """{"name":"$eg","url":"https://pokeapi.co/api/v2/egg-group/$eg/"}"""
                }

            return template("pokemon_species.json")
                .replace("{{jaName}}", p.jaName)
                .replace("{{enName}}", p.name.replaceFirstChar { it.uppercase() })
                .replace("{{name}}", p.name)
                .replace("{{flavorText}}", p.flavorText)
                .replace("{{genus}}", p.genus)
                .replace("{{eggGroups}}", eggGroupsJson)
                .replace("{{genderRate}}", p.genderRate.toString())
                .replace("{{captureRate}}", p.captureRate.toString())
                .replace("{{habitat}}", habitatJson)
                .replace("{{generation}}", p.generation)
        }

        fun evolutionChain(): String = template("evolution_chain.json")

        fun ability(name: String): String {
            val jaName = abilityJaNames[name] ?: name
            return template("ability.json")
                .replace("{{jaName}}", jaName)
                .replace("{{enName}}", name.replaceFirstChar { it.uppercase() })
        }
    }
