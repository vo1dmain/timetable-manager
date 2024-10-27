plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "ru.vo1dmain.build-tools"

dependencies {
    implementation(libs.android.tools.build)
    implementation(libs.kotlin.gradle.plugin)
    
    // Workaround for version catalog working inside precompiled scripts
    // Issue - https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("library") {
            id = "ru.vo1dmain.ttmanager.library"
            implementationClass = "ru.vo1dmain.buildtools.LibraryPlugin"
        }
    }
}