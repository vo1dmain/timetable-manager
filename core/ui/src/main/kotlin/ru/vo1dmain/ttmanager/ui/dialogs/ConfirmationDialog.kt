package ru.vo1dmain.ttmanager.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.vo1dmain.ttmanager.ui.R

class ConfirmationDialog(
    @StringRes private val title: Int,
    @StringRes private val message: Int,
    private val onPositiveClick: () -> Unit,
    private val onNegativeClick: (DialogFragment) -> Unit = { it.dismiss() },
    private val onDismiss: () -> Unit = {}
) : DialogFragment() {
    
    override fun onCreateDialog(savedInstance: Bundle?) =
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setPositiveButton(R.string.dialog_positive) { _, _ -> onPositiveClick() }
            .setNegativeButton(R.string.dialog_negative) { _, _ -> onNegativeClick(this) }
            .setMessage(message)
            .create()
    
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }
}