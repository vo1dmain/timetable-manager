plugins {
    alias(libs.plugins.buildtools.library)
}

android {
    namespace = "ru.vo1dmain.ttmanager.ui"
    
    defaultConfig {
        version = "0.1.0"
    }
    
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.lifecycle.livedata.ktx)
    api(libs.lifecycle.runtime.ktx)
    
    api(libs.navigation.fragment.ktx)
    
    api(libs.constraintlayout)
    api(libs.recyclerview.selection)
    
    api(libs.fragment.ktx)
    api(libs.appcompat)
    api(libs.material)
}