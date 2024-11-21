plugins {
    alias(libs.plugins.buildtools.feature)
}

android {
    namespace = "ru.vo1dmain.timetables.settings"
    
    defaultConfig {
        version = "1.0.0"
    }
}

dependencies {
    implementation(projects.core.preferences)
}