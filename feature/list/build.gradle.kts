plugins {
    id("convention.feature")
}

dependencies {
    implementation(project(":core:data"))
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.paging.testing)
}
