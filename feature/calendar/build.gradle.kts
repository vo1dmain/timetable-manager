plugins {
    alias(libs.plugins.buildtools.feature)
}

android {
    namespace = "ru.vo1dmain.timetables.calendar"
    
    defaultConfig {
        version = "0.1.0"
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.datetime)
    implementation(libs.coil.compose)
}