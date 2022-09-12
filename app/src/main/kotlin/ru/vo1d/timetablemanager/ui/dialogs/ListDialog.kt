package ru.vo1d.timetablemanager.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.datetime.DayOfWeek

class ListDialog(
    @StringRes private val titleId: Int,
    private val availableDays: List<DayOfWeek>,
    private val listener: Listener
) : DialogFragment() {

    override fun onCreateDialog(savedInstance: Bundle?) =
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(titleId)
            .setItems(availableDays.map { it.toString() }.toTypedArray(), listener::onItemSelect)
            .create()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDismiss()
    }


    interface Listener {
        fun onItemSelect(dialog: DialogInterface, itemNumber: Int)
        fun onDismiss()
    }
}