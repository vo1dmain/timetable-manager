package ru.vo1dmain.buildtools

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeaturePlugin : LibraryPlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            
            android {
                buildFeatures {
                    compose = true
                }
            }
            
            dependencies {
                implementation(project(":core:ui"))
                
                implementation(libs.kotlinx.serialization)
                
                implementation(libs.compose.ui.tooling.preview)
                debugImplementation(libs.compose.ui.tooling)
            }
        }
    }
}