package ru.vo1d.ttmanager.ui.utils.extensions

inline fun <reified T : Any, reified R : T> T.cast() = this as R