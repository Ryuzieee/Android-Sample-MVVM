plugins {
    id("convention.android.application")
    id("convention.android.compose")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yamamuto.android_sample_mvvm"

    defaultConfig {
        applicationId = "com.yamamuto.android_sample_mvvm"
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BASE_URL", "\"https://pokeapi.co/api/v2/\"")
        buildConfigField("boolean", "IS_MOCK", "false")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("mock") {
            dimension = "environment"
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
            buildConfigField("boolean", "IS_MOCK", "true")
        }
        create("prod") {
            dimension = "environment"
        }
    }
}

// ビルド前に自動フォーマットを実行する
tasks.named("preBuild").configure {
    dependsOn("ktlintFormat")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature"))

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Android
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Networking (AppModule DI)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Logging
    implementation(libs.timber)
}
