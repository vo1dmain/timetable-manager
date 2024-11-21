plugins {
    alias(libs.plugins.buildtools.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.vo1dmain.timetables.ui"
    
    defaultConfig {
        version = "0.1.0"
    }
    
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    api(platform(libs.compose.bom))
    
    api(libs.appcompat)
    
    api(libs.activity.compose)
    
    api(libs.lifecycle.viewmodel.compose)
    api(libs.lifecycle.viewmodel.savedstate)
    
    api(libs.lifecycle.runtime.compose)
    
    api(libs.navigation.compose)
    
    api(libs.compose.material.navigation)
    
    api(projects.core.design)
}