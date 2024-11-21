plugins {
    alias(libs.plugins.buildtools.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.vo1dmain.timetables.design"
    
    defaultConfig {
        version = "0.1.0"
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    api(libs.compose.material3)
}