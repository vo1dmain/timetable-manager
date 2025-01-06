plugins {
    alias(libs.plugins.buildtools.feature)
}

android {
    namespace = "ru.vo1dmain.timetables.subjects"
    
    defaultConfig {
        version = "0.1.0"
    }
}

dependencies {
    implementation(projects.core.data)
}