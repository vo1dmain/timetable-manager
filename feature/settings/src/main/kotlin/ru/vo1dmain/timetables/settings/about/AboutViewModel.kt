package ru.vo1dmain.timetables.settings.about

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel

internal class AboutViewModel(application: Application) : AndroidViewModel(application) {
    val versionName: String
    val versionCode: Number
    
    init {
        val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
        
        versionName = packageInfo.versionName.toString()
        versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode
        }
    }
}