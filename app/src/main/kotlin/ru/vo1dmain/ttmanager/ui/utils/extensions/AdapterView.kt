package ru.vo1dmain.ttmanager.ui.utils.extensions

import android.view.View
import android.widget.AdapterView

internal inline fun AdapterView<*>.doOnItemSelected(
    crossinline action: (AdapterView<*>, View, Int, Long) -> Unit
) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View,
            position: Int,
            id: Long
        ) = action(parent, view, position, id)
        
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }
}