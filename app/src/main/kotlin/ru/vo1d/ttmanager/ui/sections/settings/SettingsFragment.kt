package ru.vo1d.ttmanager.ui.sections.settings

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import ru.vo1d.ttmanager.R
import ru.vo1d.ttmanager.ui.TimeFormat

internal class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var preferences: SharedPreferences
    private lateinit var timeFormatKey: String
    private lateinit var themeKey: String
    
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        
        timeFormatKey = resources.getString(R.string.time_format_preference_key)
        themeKey = resources.getString(R.string.theme_preference_key)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(Color.TRANSPARENT.toDrawable())
        setDividerHeight(0)
        
        findPreference<ListPreference>(timeFormatKey)?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue -> setFormat(newValue) }
        
        findPreference<ListPreference>(themeKey)?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue -> setTheme(newValue) }
        
        findPreference<Preference>("about")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { openAboutFragment() }
    }
    
    
    private fun setTheme(themeName: Any): Boolean {
        preferences.edit {
            when (themeName as String) {
                "crimson" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_Crimson)
                "cyan" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_Cyan)
                "deep_cyan" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_DeepCyan)
                "deep_purple" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_DeepPurple)
                "green" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_Green)
                "purple" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_Purple)
                "light_blue" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_LightBlue)
                "teal" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_Teal)
                "pumpkin" -> putInt(themeSharedKey, R.style.Theme_TimetableManager_Pumpkin)
                else -> return false
            }
        }
        requireActivity().recreate()
        return true
    }
    
    private fun setFormat(dateTimeFormat: Any): Boolean {
        preferences.edit {
            when (dateTimeFormat as String) {
                "system" -> putInt(timeFormatSharedKey, TimeFormat.System.ordinal)
                "12-hour" -> putInt(timeFormatSharedKey, TimeFormat.F12Hour.ordinal)
                "24-hour" -> putInt(timeFormatSharedKey, TimeFormat.F24Hour.ordinal)
                else -> return false
            }
        }
        return true
    }
    
    private fun openAboutFragment(): Boolean {
        findNavController().navigate(R.id.nav_about)
        return true
    }
    
    
    companion object {
        const val timeFormatSharedKey = "timeFormatKey"
        const val themeSharedKey = "themeKey"
    }
}