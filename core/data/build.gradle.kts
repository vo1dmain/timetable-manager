plugins {
    alias(libs.plugins.buildtools.library)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "ru.vo1dmain.ttmanager.data"
    
    defaultConfig {
        version = "0.1.0"
    }
}

ksp {
    arg("room.incremental", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.expandProjection", "true")
}

dependencies {
    implementation(project(":core:datetime"))
    
    api(libs.room.ktx)
    ksp(libs.room.compiler)
}