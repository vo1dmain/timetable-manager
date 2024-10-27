plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
    
    compilerOptions {
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
}

android {
    namespace = "ru.vo1dmain.ttmanager"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()
    
    defaultConfig {
        applicationId = "ru.vo1dmain.ttmanager"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 4
        versionName = "0.1.0"
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
        viewBinding = true
        compose = true
    }
}


dependencies {
    coreLibraryDesugaring(libs.android.jdk.desugaring)
    
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview.selection)
    
    implementation(libs.androidx.fragment.ktx)
    
    implementation(project(":core:data"))
    implementation(project(":core:datetime"))
    implementation(project(":core:design"))
    implementation(project(":core:preferences"))
}