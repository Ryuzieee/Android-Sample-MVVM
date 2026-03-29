plugins {
    id("convention.feature")
}

dependencies {
    implementation(project(":core"))
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.timber)
    testImplementation(libs.androidx.paging.testing)
}
