package ru.vo1dmain.ttmanager.ui

interface Submitter {
    fun submit(onResult: (Boolean) -> Unit)
}