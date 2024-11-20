pluginManagement {
    includeBuild("build-tools")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "timetables"
include(":app")

include(":core:data")
include(":core:datetime")
include(":core:design")
include(":core:preferences")
include(":core:ui")
include(":utils")
