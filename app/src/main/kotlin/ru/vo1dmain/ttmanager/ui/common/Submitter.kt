package ru.vo1dmain.ttmanager.ui.common

interface Submitter {
    fun submit(onResult: (Boolean) -> Unit)
}