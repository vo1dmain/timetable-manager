package ru.vo1d.timetablemanager.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.vo1d.timetablemanager.R

class ConfirmationDialog(
    private val titleId: Int,
    private val message: String,
    private val listener: Listener
) : DialogFragment() {

    override fun onCreateDialog(savedInstance: Bundle?) =
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(titleId)
            .setPositiveButton(R.string.dialog_positive) { _, _ -> listener.onPositiveClick() }
            .setNegativeButton(R.string.dialog_negative) { _, _ -> listener.onNegativeClick(this) }
            .setMessage(message)
            .create()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDismiss()
    }


    interface Listener {
        fun onPositiveClick()
        fun onNegativeClick(dialog: DialogFragment)
        fun onDismiss() = Unit
    }

    companion object {
        const val DELETE_ALL = 0
        const val DELETE_SELECTED = 1
    }
}