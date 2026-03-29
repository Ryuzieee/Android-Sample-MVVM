package com.yamamuto.android_sample_mvvm.network.mock

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val CONTENT_TYPE_JSON = "application/json"
private const val MOCK_DELAY_MS = 300L

/**
 * モックフレーバー用 OkHttp Interceptor。
 *
 * [MockScenarioHolder.current] に応じてモックレスポンスまたはエラーを返す。
 * IS_MOCK = false のビルドでは OkHttpClient に追加されないため、本番には影響しない。
 */
@Singleton
class MockInterceptor
    @Inject
    constructor(
        private val mockData: MockData,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Thread.sleep(MOCK_DELAY_MS)

            val request = chain.request()
            val path = request.url.encodedPath

            return when (val scenario = MockScenarioHolder.current) {
                is MockScenario.Success -> successResponse(chain, path)
                is MockScenario.NetworkError -> throw IOException("Mock: ネットワークエラー")
                is MockScenario.CustomError -> customErrorResponse(chain, scenario)
            }
        }

        private fun successResponse(
            chain: Interceptor.Chain,
            path: String,
        ): Response {
            val json = routeMockResponse(path)
            return chain
                .request()
                .newResponse(200, json)
        }

        private fun customErrorResponse(
            chain: Interceptor.Chain,
            error: MockScenario.CustomError,
        ): Response {
            return Response
                .Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(error.code)
                .message(error.message)
                .apply {
                    error.headers.forEach { (key, value) -> header(key, value) }
                }.body(error.body.toResponseBody(CONTENT_TYPE_JSON.toMediaType()))
                .build()
        }

        private fun Request.newResponse(
            code: Int,
            body: String,
        ): Response {
            return Response
                .Builder()
                .request(this)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message(if (code == 200) "OK" else "Error")
                .body(body.toResponseBody(CONTENT_TYPE_JSON.toMediaType()))
                .build()
        }

        private fun routeMockResponse(path: String): String {
            val normalized = path.trimEnd('/')
            return when {
                normalized == "/api/v2/pokemon" -> mockData.pokemonList()
                normalized.matches(Regex("/api/v2/pokemon/[^/]+")) -> {
                    val name = normalized.substringAfterLast("/")
                    mockData.pokemonDetail(name)
                }
                normalized.matches(Regex("/api/v2/pokemon-species/[^/]+")) -> {
                    val name = normalized.substringAfterLast("/")
                    mockData.pokemonSpecies(name)
                }
                normalized.matches(Regex("/api/v2/evolution-chain/[^/]+")) -> {
                    val chainId = normalized.substringAfterLast("/").toIntOrNull() ?: 1
                    mockData.evolutionChain(chainId)
                }
                normalized.matches(Regex("/api/v2/ability/[^/]+")) -> {
                    val name = normalized.substringAfterLast("/")
                    mockData.ability(name)
                }
                else -> """{"error": "Unknown mock route: $path"}"""
            }
        }
    }
