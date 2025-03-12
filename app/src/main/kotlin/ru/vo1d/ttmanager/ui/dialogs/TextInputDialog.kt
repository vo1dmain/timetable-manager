package ru.vo1d.ttmanager.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.vo1d.ttmanager.R
import ru.vo1d.ttmanager.databinding.DialogCreateBinding
import ru.vo1d.ttmanager.ui.utils.extensions.doAfterTextChanged

class TextInputDialog(
    private val text: String,
    private val onPositiveClick: (DialogFragment) -> Unit,
    private val onNegativeClick: (DialogFragment) -> Unit = {},
    private val onDismiss: () -> Unit = {}
) : DialogFragment() {
    
    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
        val binding = DialogCreateBinding.inflate(layoutInflater, null, false)
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.root)
            .setPositiveButton(R.string.dialog_positive) { _, _ -> onPositiveClick(this) }
            .setNegativeButton(R.string.dialog_negative) { _, _ -> onNegativeClick(this) }
            .create()
        
        dialog.setOnShowListener { dialogInterface ->
            if (dialogInterface !is AlertDialog) return@setOnShowListener
            
            val positiveButton = dialogInterface.getButton(AlertDialog.BUTTON_POSITIVE)
            
            positiveButton.isEnabled = false
            binding.newWeekTitle.doAfterTextChanged {
                positiveButton.isEnabled = it.trim().isNotEmpty()
            }
            binding.newWeekTitle.setText(text)
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }
    
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }
}