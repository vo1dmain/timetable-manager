package ru.vo1d.timetablemanager.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.databinding.DialogCreateBinding

class TextInputDialog(
    private val text: String,
    private val listener: Listener
) : DialogFragment() {

    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
        val binding = DialogCreateBinding.inflate(layoutInflater, null, false)
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.root)
            .setPositiveButton(R.string.dialog_positive) { _, _ -> listener.onPositiveClick(this) }
            .setNegativeButton(R.string.dialog_negative) { _, _ -> listener.onNegativeClick(this) }
            .create()

        dialog.setOnShowListener { dialog ->
            (dialog as AlertDialog)

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            binding.newWeekTitle.doAfterTextChanged {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    it.toString().trim().isNotEmpty()
            }
            binding.newWeekTitle.setText(text)
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDismiss()
    }


    interface Listener {
        fun onPositiveClick(dialog: DialogFragment)
        fun onNegativeClick(dialog: DialogFragment)
        fun onDismiss()
    }
}