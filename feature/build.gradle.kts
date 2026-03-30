plugins {
    id("convention.feature")
}

dependencies {
    implementation(project(":core"))
    implementation(libs.timber)
}
