package ru.vo1dmain.ttmanager.ui.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


inline fun <reified T : FragmentActivity> Fragment.activity() = lazy {
    requireActivity().cast<FragmentActivity, T>()
}