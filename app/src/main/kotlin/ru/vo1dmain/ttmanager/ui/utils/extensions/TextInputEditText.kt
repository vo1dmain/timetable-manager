package ru.vo1dmain.ttmanager.ui.utils.extensions

import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText


inline fun TextInputEditText.doAfterTextChanged(crossinline action: (String) -> Unit) =
    this.doAfterTextChanged { action(it?.toString() ?: "") }