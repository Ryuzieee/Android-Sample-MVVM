plugins {
    id("convention.feature")
    alias(libs.plugins.roborazzi.plugin)
}

dependencies {
    implementation(libs.timber)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.rule)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.compose.ui)
    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.material3)
}
