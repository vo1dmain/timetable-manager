package ru.vo1d.ttmanager.ui.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


inline fun <reified T : FragmentActivity> Fragment.activity() = lazy {
    requireActivity().cast<FragmentActivity, T>()
}