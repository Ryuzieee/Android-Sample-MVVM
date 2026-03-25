plugins {
    id("convention.android.library")
    id("convention.android.compose")
}

android {
    namespace = "com.yamamuto.android_sample_mvvm.core.ui"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
