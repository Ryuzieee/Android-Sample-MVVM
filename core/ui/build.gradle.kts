plugins {
    id("convention.android.library")
    id("convention.android.compose")
}

android {
    namespace = "com.yamamuto.android_sample_mvvm.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
