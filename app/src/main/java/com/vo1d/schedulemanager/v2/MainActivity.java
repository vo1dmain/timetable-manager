package com.vo1d.schedulemanager.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

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
import com.vo1d.schedulemanager.v2.data.days.DaysOfWeek;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectTypes;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static ActionMode actionMode;
    private AppBarConfiguration appBarConfiguration;

    public static ActionMode getActionMode() {
        return actionMode;
    }

    public static void setActionMode(ActionMode actionMode) {
        MainActivity.actionMode = actionMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("themeId", R.style.Theme_ScheduleManager);
        setTheme(themeId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SubjectTypes.setResources(getResources());
        DaysOfWeek.setResources(getResources());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.nav_schedule, R.id.nav_subjects, R.id.nav_lecturers, R.id.nav_preferences)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (actionMode != null) {
                actionMode.finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}