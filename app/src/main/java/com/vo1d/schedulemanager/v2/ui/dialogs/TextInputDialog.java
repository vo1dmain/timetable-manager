package com.vo1d.schedulemanager.v2.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vo1d.schedulemanager.v2.R;

import java.util.Objects;

public class TextInputDialog extends DialogFragment {
    private final Listener mListener;
    private String mText = "";

    public TextInputDialog(Listener listener) {
        mListener = listener;
    }

    public void setText(String text) {
        this.mText = text;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
        builder.setView(View.inflate(requireActivity(), R.layout.dialog_create, null))
                .setPositiveButton(R.string.dialog_positive, (dialog, id) -> mListener.onPositiveClick(this))
                .setNegativeButton(R.string.dialog_negative, (dialog, id) -> mListener.onNegativeClick(this));
        AlertDialog d = builder.create();
        try {
            d.setOnShowListener(dialog -> {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                EditText et = d.findViewById(R.id.new_week_title);

                assert et != null;
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!s.toString().trim().isEmpty());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                et.setText(mText);
            });

            Objects.requireNonNull(d.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onDismiss();
    }

    public interface Listener {
        void onPositiveClick(DialogFragment dialog);

        void onNegativeClick(DialogFragment dialog);

        void onDismiss();
    }
}