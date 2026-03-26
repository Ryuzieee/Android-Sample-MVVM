plugins {
    id("convention.kotlin.library")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
}
