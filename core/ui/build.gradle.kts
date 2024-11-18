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
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    
    api(libs.androidx.navigation.fragment.ktx)
    
    api(libs.androidx.constraintlayout)
    api(libs.androidx.recyclerview.selection)
    
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
}