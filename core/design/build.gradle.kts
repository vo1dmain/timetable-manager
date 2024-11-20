plugins {
    alias(libs.plugins.buildtools.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.vo1dmain.ttmanager.design"
    
    defaultConfig {
        version = "0.1.0"
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(libs.material)
    
    api(platform(libs.compose.bom))
    api(libs.compose.material3)
}