package ru.vo1dmain.ttmanager.utils.extensions

import android.util.Patterns

fun String.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()