plugins {
    alias(libs.plugins.kotlin.android) apply true
    alias(libs.plugins.android.application) apply true
    alias(libs.plugins.androidx.navigation.safeargs) apply true
    alias(libs.plugins.devtools.ksp) apply true
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())

    compilerOptions {
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.jdk.get().toInt())
    }
}

ksp {
    arg("room.incremental", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.expandProjection", "true")
}

android {
    namespace = "ru.vo1d.ttmanager"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    defaultConfig {
        applicationId = "ru.vo1d.ttmanager"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 4
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            versionNameSuffix = "r"
            isMinifyEnabled = true
        }
        debug {
            versionNameSuffix = "d"
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}


dependencies {
    coreLibraryDesugaring(libs.android.jdk.desugaring)

    implementation(libs.kotlinx.datetime)

    implementation(libs.material)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview.selection)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.datastore.preferences)
}