plugins {
    alias(libs.plugins.buildtools.library)
    alias(libs.plugins.compose.compiler)
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
    
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
}