plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.devtools.ksp) apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
}