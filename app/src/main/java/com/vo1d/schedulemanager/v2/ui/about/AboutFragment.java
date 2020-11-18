package com.vo1d.schedulemanager.v2.ui.about;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.textview.MaterialTextView;
import com.vo1d.schedulemanager.v2.BuildConfig;
import com.vo1d.schedulemanager.v2.R;

public class AboutFragment extends Fragment {

    private int clickCounter = 0;
    private boolean secretFound;
    private SharedPreferences preferences;
    private MaterialTextView devName;
    private MaterialTextView versionNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = requireActivity().getApplicationContext();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        secretFound = preferences.getBoolean("secretFound", false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        versionNumber = view.findViewById(R.id.app_version_number);
        devName = view.findViewById(R.id.dev_name);

        versionNumber.setText(BuildConfig.VERSION_NAME);
        versionNumber.setOnClickListener(v -> onVersionClick());

        if (secretFound) {
            devName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_heart, 0);
        }
    }

    private void onVersionClick() {
        if (!secretFound) {
            clickCounter++;
            if (clickCounter == 10) {
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean("secretFound", true);
                edit.apply();
                devName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_heart, 0);

                Toast t = Toast.makeText(requireContext(), "Люблю тебя, Ксенька!", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                versionNumber.setClickable(false);
                versionNumber.setOnClickListener(null);
            }
        }
    }
}
