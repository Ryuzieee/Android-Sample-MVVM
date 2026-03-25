plugins {
    alias(libs.plugins.android.application)
    id("convention.android.compose")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yamamuto.android_sample_mvvm"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yamamuto.android_sample_mvvm"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://pokeapi.co/api/v2/\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://pokeapi.co/api/v2/\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("String", "BASE_URL", "\"https://pokeapi.co/api/v2/\"")
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"https://pokeapi.co/api/v2/\"")
        }
        create("prod") {
            dimension = "environment"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

// ビルド前に自動フォーマットを実行する
tasks.named("preBuild").configure {
    dependsOn("ktlintFormat")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.paging.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
