package ru.vo1dmain.timetables.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ListDialog<T>(
    @StringRes private val titleId: Int,
    private val items: List<T>,
    private val onItemSelected: (DialogInterface, Int) -> Unit,
    private val onDismiss: () -> Unit = {}
) : DialogFragment() {
    
    override fun onCreateDialog(savedInstance: Bundle?) =
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(titleId)
            .setItems(items.map { it.toString() }.toTypedArray(), onItemSelected)
            .create()
    
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }
}