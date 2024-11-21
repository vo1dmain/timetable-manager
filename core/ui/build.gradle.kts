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
    api(projects.core.design)
    
    api(libs.activity.compose)
    
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.lifecycle.livedata.ktx)
    api(libs.lifecycle.runtime.ktx)
    
    api(libs.navigation.fragment.ktx)
    api(libs.navigation.ui.ktx)
    
    api(libs.constraintlayout)
    api(libs.recyclerview.selection)
    
    api(libs.fragment.ktx)
    api(libs.appcompat)
    api(libs.material)
}