plugins {
    id("convention.kotlin.library")
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.javax.inject)
}
