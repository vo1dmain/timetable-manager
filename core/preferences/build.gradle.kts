plugins {
    alias(libs.plugins.buildtools.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.vo1dmain.ttmanager.prefs"
    
    defaultConfig {
        version = "0.1.0"
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:datetime"))
    
    api(libs.preference.ktx)
    api(libs.datastore.preferences)
}