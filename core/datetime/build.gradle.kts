plugins {
    alias(libs.plugins.buildtools.library)
}

android {
    namespace = "ru.vo1dmain.ttmanager.datetime"
    
    defaultConfig {
        version = "0.1.0"
    }
}

dependencies {
    api(libs.kotlinx.datetime)
}