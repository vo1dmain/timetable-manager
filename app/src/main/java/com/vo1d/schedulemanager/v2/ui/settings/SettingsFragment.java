package com.vo1d.schedulemanager.v2.ui.settings;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.ui.TimeFormats;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String timeFormatSharedKey = "timeFormatKey";
    public static final String themeSharedKey = "themeKey";
    private SharedPreferences preferences;
    private String timeFormatKey;
    private String themeKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        preferences = PreferenceManager.getDefaultSharedPreferences(
                requireActivity().getApplicationContext()
        );
        Resources r = requireActivity().getResources();

        timeFormatKey = r.getString(R.string.time_format_preference_key);
        themeKey = r.getString(R.string.theme_preference_key);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDivider(new ColorDrawable(Color.TRANSPARENT));
        setDividerHeight(0);

        ListPreference dateTimeFormat = findPreference(timeFormatKey);

        if (dateTimeFormat != null) {
            dateTimeFormat.setOnPreferenceChangeListener((preference, newValue) -> setFormat(newValue));
        }

        ListPreference theme = findPreference(themeKey);

        if (theme != null) {
            theme.setOnPreferenceChangeListener((preference, newValue) ->
                    setTheme(newValue));
        }

        Preference about = findPreference("about");

        if (about != null) {
            about.setOnPreferenceClickListener(preference -> openAboutFragment());
        }
    }

    private boolean setTheme(Object themeName) {
        SharedPreferences.Editor edit = preferences.edit();

        String s = (String) themeName;
        switch (s) {
            case "crimson":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_Crimson);
                break;
            case "cyan":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_Cyan);
                break;
            case "deep_cyan":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_DeepCyan);
                break;
            case "deep_purple":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_DeepPurple);
                break;
            case "green":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_Green);
                break;
            case "purple":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_Purple);
                break;
            case "light_blue":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_LightBlue);
                break;
            case "teal":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_Teal);
                break;
            case "pumpkin":
                edit.putInt(themeSharedKey, R.style.Theme_ScheduleManager_Pumpkin);
                break;
        }
        edit.apply();
        requireActivity().recreate();
        return true;
    }

    private boolean setFormat(Object dateTimeFormat) {
        SharedPreferences.Editor edit = preferences.edit();

        String s = (String) dateTimeFormat;
        switch (s) {
            case "system":
                edit.putInt(timeFormatSharedKey, TimeFormats.system.ordinal());
                break;
            case "12-hour":
                edit.putInt(timeFormatSharedKey, TimeFormats.f12Hour.ordinal());
                break;
            case "24-hour":
                edit.putInt(timeFormatSharedKey, TimeFormats.f24Hour.ordinal());
                break;
        }

        edit.apply();
        return true;
    }

    private boolean openAboutFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.nav_about);
        return true;
    }
}