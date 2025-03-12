plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "ru.vo1d.ttmanager"
    compileSdk = 33
    buildToolsVersion = "33.0.0"
    
    defaultConfig {
        applicationId = "ru.vo1d.ttmanager"
        minSdk = 24
        targetSdk = 33
        versionCode = 3
        versionName = "3.0.0"
        
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.incremental" to "true",
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
    
    buildTypes {
        getByName("debug") {
            versionNameSuffix = "d"
        }
        getByName("release") {
            versionNameSuffix = "r"
            isMinifyEnabled = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    buildFeatures {
        viewBinding = true
    }
}

val lifecycleVersion: String by project
val navigationVersion: String by project
val roomVersion: String by project

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")
    
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    
    implementation("com.google.android.material:material:1.7.0")
    
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-process:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycleVersion")
    
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    
    implementation("androidx.fragment:fragment-ktx:1.5.4")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}