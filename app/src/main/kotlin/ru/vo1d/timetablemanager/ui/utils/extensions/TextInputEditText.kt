package ru.vo1d.timetablemanager.ui.utils.extensions

import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText


inline fun TextInputEditText.afterTextChanged(crossinline action: (String) -> Unit) =
    doAfterTextChanged { action(it?.toString() ?: "") }