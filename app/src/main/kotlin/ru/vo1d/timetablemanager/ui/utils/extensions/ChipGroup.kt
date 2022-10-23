package ru.vo1d.timetablemanager.ui.utils.extensions

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

inline fun <reified I> ChipGroup.onItemChipSelected(crossinline callback: (I?) -> Unit) {
    setOnCheckedStateChangeListener { _, checked ->
        val selectedChip = findViewById<Chip>(checked.first())
        val item = selectedChip?.tag as? I
        callback(item)
    }
}