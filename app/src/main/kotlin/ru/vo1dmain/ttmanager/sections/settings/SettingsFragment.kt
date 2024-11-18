package ru.vo1dmain.ttmanager.sections.settings

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import ru.vo1dmain.ttmanager.R
import ru.vo1dmain.ttmanager.datetime.TimeFormat
import ru.vo1dmain.ttmanager.datetime.R as DatetimeR
import ru.vo1dmain.ttmanager.design.R as DesignR

internal class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var preferences: SharedPreferences
    private lateinit var timeFormatKey: String
    private lateinit var themeKey: String
    
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        
        timeFormatKey = resources.getString(DatetimeR.string.time_format_preference_key)
        themeKey = resources.getString(R.string.theme_preference_key)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
        setDividerHeight(0)
        
        findPreference<ListPreference>(timeFormatKey)?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue -> setFormat(newValue) }
        
        findPreference<ListPreference>(themeKey)?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue -> setTheme(newValue) }
        
        findPreference<Preference>("about")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { openAboutFragment() }
    }
    
    
    private fun setTheme(themeName: Any): Boolean {
        val edit = preferences.edit()
        when (themeName as String) {
            "crimson" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_Crimson)
            "cyan" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_Cyan)
            "deep_cyan" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_DeepCyan)
            "deep_purple" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_DeepPurple)
            "green" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_Green)
            "purple" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_Purple)
            "light_blue" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_LightBlue)
            "teal" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_Teal)
            "pumpkin" -> edit.putInt(THEME_SHARED_KEY, DesignR.style.Theme_TimetableManager_Pumpkin)
            else -> return false
        }
        edit.apply()
        requireActivity().recreate()
        return true
    }
    
    private fun setFormat(dateTimeFormat: Any): Boolean {
        val edit = preferences.edit()
        when (dateTimeFormat as String) {
            "system" -> edit.putInt(TIME_FORMAT_SHARED_KEY, TimeFormat.System.ordinal)
            "12-hour" -> edit.putInt(TIME_FORMAT_SHARED_KEY, TimeFormat.F12Hour.ordinal)
            "24-hour" -> edit.putInt(TIME_FORMAT_SHARED_KEY, TimeFormat.F24Hour.ordinal)
            else -> return false
        }
        edit.apply()
        return true
    }
    
    private fun openAboutFragment(): Boolean {
        findNavController().navigate(R.id.nav_about)
        return true
    }
    
    
    companion object {
        const val TIME_FORMAT_SHARED_KEY = "timeFormatKey"
        const val THEME_SHARED_KEY = "themeKey"
    }
}