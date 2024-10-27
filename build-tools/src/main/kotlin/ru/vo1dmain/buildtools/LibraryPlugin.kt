package ru.vo1dmain.buildtools

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            
            kotlin {
                compilerOptions {
                    freeCompilerArgs.set(listOf("-Xstring-concat=inline"))
                }
                
                jvmToolchain(libs.versions.jdk.get().toInt())
            }
            
            android {
                compileSdk = libs.versions.targetSdk.get().toInt()
                buildToolsVersion = libs.versions.buildTools.get()
                
                defaultConfig {
                    minSdk = libs.versions.minSdk.get().toInt()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
                
                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                    debug {
                        isMinifyEnabled = false
                    }
                }
                
                compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                }
            }
            
            dependencies {
                coreLibraryDesugaring(libs.android.jdk.desugaring)
                testImplementation(libs.junit)
                
                androidTestImplementation(libs.androidx.junit)
                androidTestImplementation(libs.androidx.espresso.core)
            }
        }
    }
}