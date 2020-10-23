package com.vo1d.schedulemanager.v2.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vo1d.schedulemanager.v2.data.days.DaysOfWeek;

import java.util.LinkedList;
import java.util.List;

public class ListDialog extends DialogFragment {
    private final int titleId;
    private final DialogListener mListener;
    private final String[] availableDays;

    public ListDialog(int titleId, List<DaysOfWeek> availableDays, DialogListener listener) {
        this.titleId = titleId;
        mListener = listener;

        List<String> list = new LinkedList<>();

        for (DaysOfWeek d :
                availableDays) {
            list.add(d.toString());
        }

        this.availableDays = list.toArray(new String[0]);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
        builder.setTitle(titleId)
                .setItems(availableDays, mListener::onItemSelect);

        return builder.create();
    }

    public interface DialogListener {
        void onItemSelect(DialogInterface dialog, int itemNumber);
    }
}
