package com.vo1d.schedulemanager.v2;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vo1d.schedulemanager.v2.data.courses.CourseTypes;
import com.vo1d.schedulemanager.v2.data.days.DaysOfWeek;
import com.vo1d.schedulemanager.v2.ui.settings.SettingsFragment;

import java.util.Objects;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends AppCompatActivity {

    public static final SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm", ULocale.getDefault());
    public static final SimpleDateFormat f12Hour = new SimpleDateFormat("hh:mm aa", ULocale.getDefault());

    private static ActionMode actionMode;
    public Spinner weeksSpinner;
    private AppBarConfiguration appBarConfiguration;

    public static ActionMode getActionMode() {
        return actionMode;
    }

    public static void setActionMode(ActionMode actionMode) {
        MainActivity.actionMode = actionMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getInt(SettingsFragment.themeSharedKey, R.style.Theme_ScheduleManager);
        setTheme(themeId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weeksSpinner = findViewById(R.id.weeks_spinner);

        CourseTypes.setResources(getResources());
        DaysOfWeek.setResources(getResources());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.nav_schedule, R.id.nav_courses, R.id.nav_instructors, R.id.nav_preferences)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (actionMode != null) {
                actionMode.finish();
            }
            weeksSpinner.setVisibility(View.GONE);
            hideKeyboard();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        hideKeyboard();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void hideKeyboard() {
        InputMethodManager imm = getSystemService(InputMethodManager.class);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}