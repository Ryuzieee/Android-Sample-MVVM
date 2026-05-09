@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.network.mock

import com.yamamuto.android_sample_mvvm.data.api.response.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.response.EvolutionChainResponse.ChainLink
import com.yamamuto.android_sample_mvvm.data.api.response.EvolutionChainResponse.EvolutionDetail
import com.yamamuto.android_sample_mvvm.data.api.response.EvolutionChainResponse.Species
import com.yamamuto.android_sample_mvvm.data.api.response.EvolutionChainResponse.Trigger
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json

private fun species(
    name: String,
    id: Int,
) = Species(name = name, url = "https://pokeapi.co/api/v2/pokemon-species/$id/")

private fun levelUp(level: Int) = EvolutionDetail(minLevel = level, trigger = Trigger(name = "level-up"))

private fun useItem(itemName: String) = EvolutionDetail(minLevel = null, trigger = Trigger(name = "use-item"))

private fun friendship() = EvolutionDetail(minLevel = null, trigger = Trigger(name = "level-up"))

private fun leaf(
    species: Species,
    evolvesTo: List<ChainLink> = emptyList(),
    details: List<EvolutionDetail> = emptyList(),
) = ChainLink(species = species, evolvesTo = evolvesTo, evolutionDetails = details)

/**
 * 進化チェーンのマスタ定義。chain ID → ChainLink のマップ。
 * 各ポケモンの [MockPokemon.evolutionChainId] と対応する。
 */
private val EVOLUTION_CHAINS: Map<Int, EvolutionChainResponse> =
    mapOf(
        // フシギダネ → フシギソウ → フシギバナ
        1 to
            EvolutionChainResponse(
                id = 1,
                chain =
                    leaf(
                        species = species("bulbasaur", 1),
                        evolvesTo =
                            listOf(
                                leaf(
                                    species = species("ivysaur", 2),
                                    evolvesTo =
                                        listOf(
                                            leaf(species("venusaur", 3), details = listOf(levelUp(32))),
                                        ),
                                    details = listOf(levelUp(16)),
                                ),
                            ),
                    ),
            ),
        // ヒトカゲ → リザード → リザードン
        2 to
            EvolutionChainResponse(
                id = 2,
                chain =
                    leaf(
                        species = species("charmander", 4),
                        evolvesTo =
                            listOf(
                                leaf(
                                    species = species("charmeleon", 5),
                                    evolvesTo =
                                        listOf(
                                            leaf(species("charizard", 6), details = listOf(levelUp(36))),
                                        ),
                                    details = listOf(levelUp(16)),
                                ),
                            ),
                    ),
            ),
        // ゼニガメ → カメール → カメックス
        3 to
            EvolutionChainResponse(
                id = 3,
                chain =
                    leaf(
                        species = species("squirtle", 7),
                        evolvesTo =
                            listOf(
                                leaf(
                                    species = species("wartortle", 8),
                                    evolvesTo =
                                        listOf(
                                            leaf(species("blastoise", 9), details = listOf(levelUp(36))),
                                        ),
                                    details = listOf(levelUp(16)),
                                ),
                            ),
                    ),
            ),
        // ピチュー → ピカチュウ → ライチュウ
        10 to
            EvolutionChainResponse(
                id = 10,
                chain =
                    leaf(
                        species = species("pichu", 172),
                        evolvesTo =
                            listOf(
                                leaf(
                                    species = species("pikachu", 25),
                                    evolvesTo =
                                        listOf(
                                            leaf(species("raichu", 26), details = listOf(useItem("thunder-stone"))),
                                        ),
                                    details = listOf(friendship()),
                                ),
                            ),
                    ),
            ),
        // イーブイ → 8 分岐進化
        67 to
            EvolutionChainResponse(
                id = 67,
                chain =
                    leaf(
                        species = species("eevee", 133),
                        evolvesTo =
                            listOf(
                                leaf(species("vaporeon", 134), details = listOf(useItem("water-stone"))),
                                leaf(species("jolteon", 135), details = listOf(useItem("thunder-stone"))),
                                leaf(species("flareon", 136), details = listOf(useItem("fire-stone"))),
                                leaf(species("espeon", 196), details = listOf(friendship())),
                                leaf(species("umbreon", 197), details = listOf(friendship())),
                                leaf(species("leafeon", 470), details = listOf(useItem("leaf-stone"))),
                                leaf(species("glaceon", 471), details = listOf(useItem("ice-stone"))),
                                leaf(species("sylveon", 700), details = listOf(friendship())),
                            ),
                    ),
            ),
        // ゴンベ → カビゴン
        72 to
            EvolutionChainResponse(
                id = 72,
                chain =
                    leaf(
                        species = species("munchlax", 446),
                        evolvesTo =
                            listOf(
                                leaf(species("snorlax", 143), details = listOf(friendship())),
                            ),
                    ),
            ),
        // ミュウツー (進化なし)
        77 to
            EvolutionChainResponse(
                id = 77,
                chain = leaf(species = species("mewtwo", 150)),
            ),
        // ミュウ (進化なし)
        78 to
            EvolutionChainResponse(
                id = 78,
                chain = leaf(species = species("mew", 151)),
            ),
        // ルギア (進化なし)
        131 to
            EvolutionChainResponse(
                id = 131,
                chain = leaf(species = species("lugia", 249)),
            ),
        // レックウザ (進化なし)
        209 to
            EvolutionChainResponse(
                id = 209,
                chain = leaf(species = species("rayquaza", 384)),
            ),
        // リオル → ルカリオ
        232 to
            EvolutionChainResponse(
                id = 232,
                chain =
                    leaf(
                        species = species("riolu", 447),
                        evolvesTo =
                            listOf(
                                leaf(species("lucario", 448), details = listOf(friendship())),
                            ),
                    ),
            ),
        // ケロマツ → ゲコガシラ → ゲッコウガ
        331 to
            EvolutionChainResponse(
                id = 331,
                chain =
                    leaf(
                        species = species("froakie", 656),
                        evolvesTo =
                            listOf(
                                leaf(
                                    species = species("frogadier", 657),
                                    evolvesTo =
                                        listOf(
                                            leaf(species("greninja", 658), details = listOf(levelUp(36))),
                                        ),
                                    details = listOf(levelUp(16)),
                                ),
                            ),
                    ),
            ),
        // ミミッキュ (進化なし)
        403 to
            EvolutionChainResponse(
                id = 403,
                chain = leaf(species = species("mimikyu", 778)),
            ),
    )

internal fun buildMockEvolutionResponse(
    json: Json,
    chainId: Int,
): String {
    val response = EVOLUTION_CHAINS[chainId] ?: EVOLUTION_CHAINS.values.first()
    return json.encodeToString(response)
}
