plugins {
    id("convention.feature")
}

android {
    namespace = "com.yamamuto.android_sample_mvvm.feature.list"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.paging.testing)
}
