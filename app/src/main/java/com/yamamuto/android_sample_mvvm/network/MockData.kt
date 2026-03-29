@file:Suppress("ktlint:standard:max-line-length")

package com.yamamuto.android_sample_mvvm.network

/**
 * モックフレーバー用の JSON レスポンスデータ。
 *
 * PokeAPI のレスポンス形式に準拠した充実したモックデータを提供する。
 */
object MockData {
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

    private val pokemons =
        listOf(
            Pokemon(
                id = 1,
                name = "bulbasaur",
                jaName = "フシギダネ",
                height = 7,
                weight = 69,
                baseExperience = 64,
                types = listOf("grass", "poison"),
                abilities = listOf("overgrow"),
                hiddenAbility = "chlorophyll",
                stats = Stats(hp = 45, attack = 49, defense = 49, spAttack = 65, spDefense = 65, speed = 45),
                genus = "たねポケモン",
                flavorText = "うまれたときから　せなかに\nふしぎな　タネが　うえてあり\nからだと　ともに　そだつという。",
                habitat = "grassland",
                generation = "generation-i",
                eggGroups = listOf("monster", "grass"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 2,
                name = "ivysaur",
                jaName = "フシギソウ",
                height = 10,
                weight = 130,
                baseExperience = 142,
                types = listOf("grass", "poison"),
                abilities = listOf("overgrow"),
                hiddenAbility = "chlorophyll",
                stats = Stats(hp = 60, attack = 62, defense = 63, spAttack = 80, spDefense = 80, speed = 60),
                genus = "たねポケモン",
                flavorText = "つぼみが　せなかに　ついていて\nようぶんを　きゅうしゅうしていくと\nおおきな　はなが　さくという。",
                habitat = "grassland",
                generation = "generation-i",
                eggGroups = listOf("monster", "grass"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 3,
                name = "venusaur",
                jaName = "フシギバナ",
                height = 20,
                weight = 1000,
                baseExperience = 263,
                types = listOf("grass", "poison"),
                abilities = listOf("overgrow"),
                hiddenAbility = "chlorophyll",
                stats = Stats(hp = 80, attack = 82, defense = 83, spAttack = 100, spDefense = 100, speed = 80),
                genus = "たねポケモン",
                flavorText = "はなから　うっとりする　かおりが\nただよい　たたかう　ものの\nきもちを　なだめてしまう。",
                habitat = "grassland",
                generation = "generation-i",
                eggGroups = listOf("monster", "grass"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 4,
                name = "charmander",
                jaName = "ヒトカゲ",
                height = 6,
                weight = 85,
                baseExperience = 62,
                types = listOf("fire"),
                abilities = listOf("blaze"),
                hiddenAbility = "solar-power",
                stats = Stats(hp = 39, attack = 52, defense = 43, spAttack = 60, spDefense = 50, speed = 65),
                genus = "とかげポケモン",
                flavorText = "うまれたときから　しっぽに\nほのおが　ともっている。ほのおが\nきえたとき　その　いのちは　おわる。",
                habitat = "mountain",
                generation = "generation-i",
                eggGroups = listOf("monster", "dragon"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 5,
                name = "charmeleon",
                jaName = "リザード",
                height = 11,
                weight = 190,
                baseExperience = 142,
                types = listOf("fire"),
                abilities = listOf("blaze"),
                hiddenAbility = "solar-power",
                stats = Stats(hp = 58, attack = 64, defense = 58, spAttack = 80, spDefense = 65, speed = 80),
                genus = "かえんポケモン",
                flavorText = "たたかいで　きぶんが　たかまると\nしっぽの　さきから　あかるい\nほのおを　ふきだす　らんぼうもの。",
                habitat = "mountain",
                generation = "generation-i",
                eggGroups = listOf("monster", "dragon"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 6,
                name = "charizard",
                jaName = "リザードン",
                height = 17,
                weight = 905,
                baseExperience = 267,
                types = listOf("fire", "flying"),
                abilities = listOf("blaze"),
                hiddenAbility = "solar-power",
                stats = Stats(hp = 78, attack = 84, defense = 78, spAttack = 109, spDefense = 85, speed = 100),
                genus = "かえんポケモン",
                flavorText = "ちきゅうじょうの　あらゆるものを\nやきつくす　ほのおを　はける。\nもりかじの　げんいんに　なる。",
                habitat = "mountain",
                generation = "generation-i",
                eggGroups = listOf("monster", "dragon"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 7,
                name = "squirtle",
                jaName = "ゼニガメ",
                height = 5,
                weight = 90,
                baseExperience = 63,
                types = listOf("water"),
                abilities = listOf("torrent"),
                hiddenAbility = "rain-dish",
                stats = Stats(hp = 44, attack = 48, defense = 65, spAttack = 50, spDefense = 64, speed = 43),
                genus = "かめのこポケモン",
                flavorText = "うまれると　せなかの　コウラが\nふくらんで　かたくなる。くちから\nいきおいよく　あわを　ふく。",
                habitat = "waters-edge",
                generation = "generation-i",
                eggGroups = listOf("monster", "water1"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 8,
                name = "wartortle",
                jaName = "カメール",
                height = 10,
                weight = 225,
                baseExperience = 142,
                types = listOf("water"),
                abilities = listOf("torrent"),
                hiddenAbility = "rain-dish",
                stats = Stats(hp = 59, attack = 63, defense = 80, spAttack = 65, spDefense = 80, speed = 58),
                genus = "かめポケモン",
                flavorText = "ながいきの　シンボルとして\nにんきが　ある。ふさふさの　みみと\nしっぽの　けが　ながいきの　しょうこ。",
                habitat = "waters-edge",
                generation = "generation-i",
                eggGroups = listOf("monster", "water1"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 9,
                name = "blastoise",
                jaName = "カメックス",
                height = 16,
                weight = 855,
                baseExperience = 265,
                types = listOf("water"),
                abilities = listOf("torrent"),
                hiddenAbility = "rain-dish",
                stats = Stats(hp = 79, attack = 83, defense = 100, spAttack = 85, spDefense = 105, speed = 78),
                genus = "こうらポケモン",
                flavorText = "こうらの　ロケットほうから\nふきだす　みずは　はがねの いたも\nぶちぬく　はかいりょくだ。",
                habitat = "waters-edge",
                generation = "generation-i",
                eggGroups = listOf("monster", "water1"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 25,
                name = "pikachu",
                jaName = "ピカチュウ",
                height = 4,
                weight = 60,
                baseExperience = 112,
                types = listOf("electric"),
                abilities = listOf("static"),
                hiddenAbility = "lightning-rod",
                stats = Stats(hp = 35, attack = 55, defense = 40, spAttack = 50, spDefense = 50, speed = 90),
                genus = "ねずみポケモン",
                flavorText = "ほっぺたの　でんきぶくろに\nでんきを　ためている。ピンチのとき\nに　ほうでんする。",
                habitat = "forest",
                generation = "generation-i",
                eggGroups = listOf("field", "fairy"),
                genderRate = 4,
                captureRate = 190,
            ),
            Pokemon(
                id = 26,
                name = "raichu",
                jaName = "ライチュウ",
                height = 8,
                weight = 300,
                baseExperience = 243,
                types = listOf("electric"),
                abilities = listOf("static"),
                hiddenAbility = "lightning-rod",
                stats = Stats(hp = 60, attack = 90, defense = 55, spAttack = 90, spDefense = 80, speed = 110),
                genus = "ねずみポケモン",
                flavorText = "でんきを　ためすぎて　こうふんすると\nかがやく。くらやみで　ひかる。",
                habitat = "forest",
                generation = "generation-i",
                eggGroups = listOf("field", "fairy"),
                genderRate = 4,
                captureRate = 75,
            ),
            Pokemon(
                id = 133,
                name = "eevee",
                jaName = "イーブイ",
                height = 3,
                weight = 65,
                baseExperience = 65,
                types = listOf("normal"),
                abilities = listOf("run-away", "adaptability"),
                hiddenAbility = "anticipation",
                stats = Stats(hp = 55, attack = 55, defense = 50, spAttack = 45, spDefense = 65, speed = 55),
                genus = "しんかポケモン",
                flavorText = "ふきそくな　いでんしを\nもっている。いしの　ほうしゃせんで\nからだが　とつぜんへんいする。",
                habitat = "urban",
                generation = "generation-i",
                eggGroups = listOf("field"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 143,
                name = "snorlax",
                jaName = "カビゴン",
                height = 21,
                weight = 4600,
                baseExperience = 189,
                types = listOf("normal"),
                abilities = listOf("immunity", "thick-fat"),
                hiddenAbility = "gluttony",
                stats = Stats(hp = 160, attack = 110, defense = 65, spAttack = 65, spDefense = 110, speed = 30),
                genus = "いねむりポケモン",
                flavorText = "いちにちに　たべものを\n400キロ　たべないと\nきが　すまない。たべおわると　ねむる。",
                habitat = "mountain",
                generation = "generation-i",
                eggGroups = listOf("monster"),
                genderRate = 1,
                captureRate = 25,
            ),
            Pokemon(
                id = 150,
                name = "mewtwo",
                jaName = "ミュウツー",
                height = 20,
                weight = 1220,
                baseExperience = 340,
                types = listOf("psychic"),
                abilities = listOf("pressure"),
                hiddenAbility = "unnerve",
                stats = Stats(hp = 106, attack = 110, defense = 90, spAttack = 154, spDefense = 90, speed = 130),
                genus = "いでんしポケモン",
                flavorText = "いでんし　そうさによって\nつくられた　ポケモン。にんげんの\nかがくりょくで　からだは　つくれても\nやさしい　こころを　つくることは　できなかった。",
                habitat = "rare",
                generation = "generation-i",
                eggGroups = listOf("undiscovered"),
                genderRate = -1,
                captureRate = 3,
            ),
            Pokemon(
                id = 151,
                name = "mew",
                jaName = "ミュウ",
                height = 4,
                weight = 40,
                baseExperience = 300,
                types = listOf("psychic"),
                abilities = listOf("synchronize"),
                hiddenAbility = null,
                stats = Stats(hp = 100, attack = 100, defense = 100, spAttack = 100, spDefense = 100, speed = 100),
                genus = "しんしゅポケモン",
                flavorText = "すべての　ポケモンの　いでんしを\nもつと　いわれている。あらゆる\nわざを　つかうことが　できる。",
                habitat = "rare",
                generation = "generation-i",
                eggGroups = listOf("undiscovered"),
                genderRate = -1,
                captureRate = 45,
            ),
            Pokemon(
                id = 249,
                name = "lugia",
                jaName = "ルギア",
                height = 52,
                weight = 2160,
                baseExperience = 340,
                types = listOf("psychic", "flying"),
                abilities = listOf("pressure"),
                hiddenAbility = "multiscale",
                stats = Stats(hp = 106, attack = 90, defense = 130, spAttack = 90, spDefense = 154, speed = 110),
                genus = "せんすいポケモン",
                flavorText = "あまりにも　つよすぎる　ちからを\nもつため　ふかい　うみのそこで\nしずかに　ときを　すごしている。",
                habitat = "sea",
                generation = "generation-ii",
                eggGroups = listOf("undiscovered"),
                genderRate = -1,
                captureRate = 3,
            ),
            Pokemon(
                id = 384,
                name = "rayquaza",
                jaName = "レックウザ",
                height = 70,
                weight = 2065,
                baseExperience = 340,
                types = listOf("dragon", "flying"),
                abilities = listOf("air-lock"),
                hiddenAbility = null,
                stats = Stats(hp = 105, attack = 150, defense = 90, spAttack = 150, spDefense = 90, speed = 95),
                genus = "てんくうポケモン",
                flavorText = "オゾンそうの　なかを　なんおくねんも\nとびつづけていた　ポケモン。\nグラードンと　カイオーガの　あらそいを\nしずめたと　いう　でんせつがある。",
                habitat = null,
                generation = "generation-iii",
                eggGroups = listOf("undiscovered"),
                genderRate = -1,
                captureRate = 45,
            ),
            Pokemon(
                id = 448,
                name = "lucario",
                jaName = "ルカリオ",
                height = 12,
                weight = 540,
                baseExperience = 184,
                types = listOf("fighting", "steel"),
                abilities = listOf("steadfast", "inner-focus"),
                hiddenAbility = "justified",
                stats = Stats(hp = 70, attack = 110, defense = 70, spAttack = 115, spDefense = 70, speed = 90),
                genus = "はどうポケモン",
                flavorText = "あらゆる　ものが　はっする\nはどうを　キャッチする　のうりょくを\nもつ。にんげんの　ことばも　わかる。",
                habitat = null,
                generation = "generation-iv",
                eggGroups = listOf("field", "human-like"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 658,
                name = "greninja",
                jaName = "ゲッコウガ",
                height = 15,
                weight = 400,
                baseExperience = 239,
                types = listOf("water", "dark"),
                abilities = listOf("torrent"),
                hiddenAbility = "protean",
                stats = Stats(hp = 72, attack = 95, defense = 67, spAttack = 103, spDefense = 71, speed = 122),
                genus = "しのびポケモン",
                flavorText = "にんじゃのように　しんしゅつきぼつ。\nこうそくいどうで　ほんろうしつつ\nみずの　しゅりけんで　きりさく。",
                habitat = null,
                generation = "generation-vi",
                eggGroups = listOf("water1"),
                genderRate = 1,
                captureRate = 45,
            ),
            Pokemon(
                id = 778,
                name = "mimikyu",
                jaName = "ミミッキュ",
                height = 2,
                weight = 7,
                baseExperience = 167,
                types = listOf("ghost", "fairy"),
                abilities = listOf("disguise"),
                hiddenAbility = null,
                stats = Stats(hp = 55, attack = 90, defense = 80, spAttack = 50, spDefense = 105, speed = 96),
                genus = "ばけのかわポケモン",
                flavorText = "さびしがりやの　ポケモン。\nピカチュウの　すがたに　ばけると\nなかよく　してもらえると　きいたのだ。",
                habitat = null,
                generation = "generation-vii",
                eggGroups = listOf("amorphous"),
                genderRate = 4,
                captureRate = 45,
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
        val allAbilities = p.abilities.map { it to false } + listOfNotNull(p.hiddenAbility?.let { it to true })
        val abilities =
            allAbilities
                .mapIndexed { i, (a, hidden) ->
                    """{"ability":{"name":"$a","url":"https://pokeapi.co/api/v2/ability/$a/"},"is_hidden":$hidden,"slot":${i + 1}}"""
                }.joinToString(",")
        val statNames = listOf("hp", "attack", "defense", "special-attack", "special-defense", "speed")
        val statValues = listOf(p.stats.hp, p.stats.attack, p.stats.defense, p.stats.spAttack, p.stats.spDefense, p.stats.speed)
        val stats =
            statNames
                .zip(statValues) { name2, value ->
                    """{"base_stat":$value,"effort":0,"stat":{"name":"$name2","url":"https://pokeapi.co/api/v2/stat/$name2/"}}"""
                }.joinToString(",")
        return """
            {
                "id":${p.id},"name":"${p.name}","height":${p.height},"weight":${p.weight},
                "base_experience":${p.baseExperience},
                "types":[$types],
                "abilities":[$abilities],
                "sprites":{"other":{"official-artwork":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${p.id}.png"}}},
                "stats":[$stats]
            }
            """.trimIndent()
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
        return """
                                {
                                    "names":[
                                        {"name":"${p.jaName}","language":{"name":"ja-Hrkt","url":"https://pokeapi.co/api/v2/language/1/"}},
                                        {"name":"${p.name.replaceFirstChar {
            it.uppercase()
        }}","language":{"name":"en","url":"https://pokeapi.co/api/v2/language/9/"}}
                                    ],
                                    "flavor_text_entries":[
                                        {"flavor_text":"${p.flavorText}","language":{"name":"ja-Hrkt","url":"https://pokeapi.co/api/v2/language/1/"},"version":{"name":"sword","url":"https://pokeapi.co/api/v2/version/33/"}},
                                        {"flavor_text":"A mock English flavor text for ${p.name}.","language":{"name":"en","url":"https://pokeapi.co/api/v2/language/9/"},"version":{"name":"sword","url":"https://pokeapi.co/api/v2/version/33/"}}
                                    ],
                                    "evolution_chain":{"url":"https://pokeapi.co/api/v2/evolution-chain/1/"},
                                    "genera":[
                                        {"genus":"${p.genus}","language":{"name":"ja-Hrkt","url":"https://pokeapi.co/api/v2/language/1/"}},
                                        {"genus":"Seed Pokémon","language":{"name":"en","url":"https://pokeapi.co/api/v2/language/9/"}}
                                    ],
                                    "egg_groups":[$eggGroupsJson],
                                    "gender_rate":${p.genderRate},
                                    "capture_rate":${p.captureRate},
                                    "habitat":$habitatJson,
                                    "generation":{"name":"${p.generation}","url":"https://pokeapi.co/api/v2/generation/${p.generation}/"}
                                }
            """.trimIndent()
    }

    fun evolutionChain(): String {
        return """
            {
                "id":1,
                "chain":{
                    "species":{"name":"bulbasaur","url":"https://pokeapi.co/api/v2/pokemon-species/1/"},
                    "evolution_details":[],
                    "evolves_to":[{
                        "species":{"name":"ivysaur","url":"https://pokeapi.co/api/v2/pokemon-species/2/"},
                        "evolution_details":[{"min_level":16,"trigger":{"name":"level-up"}}],
                        "evolves_to":[{
                            "species":{"name":"venusaur","url":"https://pokeapi.co/api/v2/pokemon-species/3/"},
                            "evolution_details":[{"min_level":32,"trigger":{"name":"level-up"}}],
                            "evolves_to":[]
                        }]
                    }]
                }
            }
            """.trimIndent()
    }

    fun ability(name: String): String {
        val jaName = abilityJaNames[name] ?: name
        return """
                                {
                                    "names":[
                                        {"name":"$jaName","language":{"name":"ja-Hrkt","url":"https://pokeapi.co/api/v2/language/1/"}},
                                        {"name":"$jaName","language":{"name":"ja","url":"https://pokeapi.co/api/v2/language/11/"}},
                                        {"name":"${name.replaceFirstChar {
            it.uppercase()
        }}","language":{"name":"en","url":"https://pokeapi.co/api/v2/language/9/"}}
                                    ]
                                }
            """.trimIndent()
    }
}
