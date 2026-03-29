package com.yamamuto.android_sample_mvvm.network

/**
 * モックレスポンスのシナリオ。
 *
 * [MockInterceptor] が返すレスポンスを切り替えるために使用する。
 * アプリ実行中に [MockScenarioHolder.current] を変更することでシナリオを動的に切り替え可能。
 */
enum class MockScenario {
    /** 正常系レスポンスを返す。 */
    SUCCESS,

    /** 全 API で 401 Unauthorized を返す。 */
    SESSION_EXPIRED,

    /** 全 API で 426 Upgrade Required を返す。 */
    FORCE_UPDATE,

    /** 全 API でネットワークエラー（タイムアウト）をシミュレートする。 */
    NETWORK_ERROR,

    /** 全 API で 500 Internal Server Error を返す。 */
    SERVER_ERROR,
}

/** 現在のモックシナリオを保持するホルダー。 */
object MockScenarioHolder {
    @Volatile
    var current: MockScenario = MockScenario.SUCCESS
}
