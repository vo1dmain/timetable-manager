pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    
    val navigationVersion: String by settings
    plugins {
        kotlin("android") version "1.7.10"
        kotlin("kapt") version "1.7.10"
        id("com.android.application") version "8.9.0"
        id("androidx.navigation.safeargs.kotlin") version navigationVersion
    }
}

rootProject.name = "timetable-manager"
include(":app")