package ru.vo1dmain.buildtools

import com.android.build.api.dsl.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

internal fun Project.kotlin(configure: KotlinAndroidProjectExtension.() -> Unit) {
    extensions.configure<KotlinAndroidProjectExtension>(configure)
}

internal fun Project.android(configure: LibraryExtension.() -> Unit) {
    extensions.configure<LibraryExtension>(configure)
}

internal fun DependencyHandlerScope.coreLibraryDesugaring(notation: Any) {
    add("coreLibraryDesugaring", notation)
}

internal fun DependencyHandlerScope.implementation(notation: Any) {
    add("implementation", notation)
}

internal fun DependencyHandlerScope.debugImplementation(notation: Any) {
    add("debugImplementation", notation)
}

internal fun DependencyHandlerScope.testImplementation(notation: Any) {
    add("testImplementation", notation)
}

internal fun DependencyHandlerScope.androidTestImplementation(notation: Any) {
    add("androidTestImplementation", notation)
}