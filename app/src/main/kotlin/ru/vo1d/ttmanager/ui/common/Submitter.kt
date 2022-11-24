package ru.vo1d.ttmanager.ui.common

interface Submitter {
    fun submit(onResult: (Boolean) -> Unit)
}