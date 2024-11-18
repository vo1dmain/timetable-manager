package ru.vo1dmain.ttmanager.ui.extensions

inline fun <reified T : Any, reified R : T> T.cast() = this as R