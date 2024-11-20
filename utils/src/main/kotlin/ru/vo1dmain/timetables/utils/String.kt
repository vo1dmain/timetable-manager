package ru.vo1dmain.timetables.utils

import android.util.Patterns

fun String.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()