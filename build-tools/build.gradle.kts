plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    implementation(libs.android.tools.build.gradle)
    implementation(libs.kotlin.gradle)
    
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
        register("feature") {
            id = "ru.vo1dmain.buildtools.feature"
            implementationClass = "ru.vo1dmain.buildtools.FeaturePlugin"
        }
        register("library") {
            id = "ru.vo1dmain.buildtools.library"
            implementationClass = "ru.vo1dmain.buildtools.LibraryPlugin"
        }
    }
}