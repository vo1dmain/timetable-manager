plugins {
    alias(libs.plugins.buildtools.library)
}

android {
    namespace = "ru.vo1dmain.timetables.prefs"
    
    defaultConfig {
        version = "0.1.0"
    }
}

dependencies {
    implementation(libs.appcompat)
    api(libs.datastore.preferences)
}