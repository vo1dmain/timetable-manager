package com.vo1d.schedulemanager.v2.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vo1d.schedulemanager.v2.R;

public class ConfirmationDialog extends DialogFragment {
    public static final int DELETE_ALL = 0;
    public static final int DELETE_SELECTED = 1;

    public static final DialogListener DEFAULT_LISTENER = new DialogListener() {
        @Override
        public void onDialogPositiveClick(DialogFragment dialog) {
            dialog.dismiss();
        }

        @Override
        public void onDialogNegativeClick(DialogFragment dialog) {
            dialog.dismiss();
        }
    };

    private ConfirmationDialog.DialogListener mListener;

    private int titleId;

    private String message;

    public ConfirmationDialog(int titleId, String message) {
        this.titleId = titleId;
        this.message = message;
    }

    public void setDialogListener(DialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
        builder.setTitle(titleId)
                .setPositiveButton(R.string.dialog_positive, (dialog, id) -> mListener.onDialogPositiveClick(this))
                .setNegativeButton(R.string.dialog_negative, (dialog, id) -> mListener.onDialogNegativeClick(this))
                .setMessage(message);

        return builder.create();
    }

    public interface DialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
