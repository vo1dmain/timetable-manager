plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
    
    compilerOptions {
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
}

android {
    namespace = "ru.vo1dmain.timetables"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()
    
    defaultConfig {
        applicationId = "ru.vo1dmain.timetables"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 4
        versionName = "2.0.0"
    }
    
    buildTypes {
        release {
            versionNameSuffix = "r"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-project.pro")
            isDebuggable = false
        }
        debug {
            versionNameSuffix = "d"
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    
    buildFeatures {
        buildConfig = true
        compose = true
    }
}


dependencies {
    coreLibraryDesugaring(libs.android.tools.desugarJdkLibs)
    
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.datetime)
    implementation(projects.core.preferences)
    
    implementation(projects.feature.settings)
    implementation(projects.feature.subjects)
    implementation(projects.feature.teachers)
}