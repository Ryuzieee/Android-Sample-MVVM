plugins {
    id("convention.android.library")
    id("convention.android.compose")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yamamuto.android_sample_mvvm.core"
    testFixtures {
        enable = true
    }
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("long", "CACHE_DURATION_MS", "5 * 60 * 1000L")
    }
    buildTypes {
        debug {
            buildConfigField("long", "CACHE_DURATION_MS", "1 * 60 * 1000L")
        }
        release {
            buildConfigField("long", "CACHE_DURATION_MS", "5 * 60 * 1000L")
        }
    }
}

dependencies {
    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // DI
    implementation(libs.javax.inject)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Image loading
    implementation(libs.coil.compose)

    // Logging
    implementation(libs.timber)

    // Test fixtures (testing/ package — consumed by other modules via testFixtures)
    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesImplementation(libs.androidx.compose.runtime)
    testFixturesImplementation(libs.junit)
    testFixturesImplementation(libs.kotlinx.coroutines.test)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
}
