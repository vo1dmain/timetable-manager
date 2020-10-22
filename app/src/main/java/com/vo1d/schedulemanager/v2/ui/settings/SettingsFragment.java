package com.vo1d.schedulemanager.v2.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.vo1d.schedulemanager.v2.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private final String idKey = "themeId";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListPreference theme = findPreference("theme");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity().getApplicationContext());
        SharedPreferences.Editor edit = preferences.edit();

        if (theme != null) {
            theme.setOnPreferenceChangeListener((preference, newValue) -> {
                String s = (String) newValue;
                switch (s) {
                    case "crimson":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_Crimson);
                        break;
                    case "cyan":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_Cyan);
                        break;
                    case "deep_cyan":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_DeepCyan);
                        break;
                    case "deep_purple":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_DeepPurple);
                        break;
                    case "green":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_Green);
                        break;
                    case "purple":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_Purple);
                        break;
                    case "light_blue":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_LightBlue);
                        break;
                    case "teal":
                        edit.putInt(idKey, R.style.Theme_ScheduleManager_Teal);
                        break;
                }
                edit.apply();
                requireActivity().recreate();
                return true;
            });
        }
    }
}