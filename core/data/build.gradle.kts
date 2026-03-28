plugins {
    id("convention.android.library")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yamamuto.android_sample_mvvm.core.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        // デフォルト: 5分
        buildConfigField("long", "CACHE_DURATION_MS", "5 * 60 * 1000L")
    }

    buildTypes {
        debug {
            // debug: 1分（開発時はキャッシュを短くして動作確認しやすくする）
            buildConfigField("long", "CACHE_DURATION_MS", "1 * 60 * 1000L")
        }
        release {
            // release: 5分
            buildConfigField("long", "CACHE_DURATION_MS", "5 * 60 * 1000L")
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
