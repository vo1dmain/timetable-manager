package com.vo1d.schedulemanager.v2;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.os.IBinder;
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
    /**
     * Описывает формат записи времени в виде текста в 24-часовом формате.
     * Используется для преобразования времени, записанного в форме текста
     * в экземпляр класса java.util.Date и обратно.
     */
    public static final SimpleDateFormat f24Hour =
            new SimpleDateFormat("HH:mm", ULocale.getDefault());
    /**
     * Описывает формат записи времени в виде текста в 12-часовом формате.
     * Используется для преобразования времени, записанного в форме текста
     * в экземпляр класса java.util.Date и обратно.
     */
    public static final SimpleDateFormat f12Hour =
            new SimpleDateFormat("hh:mm aa", ULocale.getDefault());
    /**
     * Статическая ссылка на экземпляр режима действий.
     * Необходима для централизованного доступа
     * и управления режимом действий из любого фрагмента.
     */
    private static ActionMode actionMode;
    /**
     * Ссылка на экземпляр выпадающего списка.
     * Необходима для управления видимостью данного элемента
     * при отображении различных фрагментов.
     */
    private Spinner weeksSpinner;

    /**
     * Ссылка на экземпляр конфигурации навигации.
     * Необходима для доступа к одному и тому же экземпляру
     * из различных методов внутри класса.
     */
    private AppBarConfiguration appBarConfiguration;

    /**
     * @return Текущий экземпляр режима действий.
     */
    public static ActionMode getActionMode() {
        return actionMode;
    }

    /**
     * Задаёт новый экземпляр режима действий.
     *
     * @param actionMode новый экземпляр режима действий.
     */
    public static void setActionMode(ActionMode actionMode) {
        MainActivity.actionMode = actionMode;
    }

    /**
     * Вызывается в самом начале жизненного цикла Activity.
     * Внутри него задаётся разметка и необходимые элементы представления,
     * а также осуществляется стартовая настройка Activity.
     *
     * @param savedInstanceState сохранённое состояние экземпляра.
     *                           Позволяет передавать данные о состоянии предыдущего экземпляра новому.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Получение id текущей темы,
        //хранящейся в файлах конфигурации приложения.
        //По-умолчанию – тёмно-голубая.
        int themeId = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getInt(SettingsFragment.themeSharedKey,
                        R.style.Theme_ScheduleManager_DeepCyan);

        //Установка текущей темы для Activity.
        setTheme(themeId);

        //Вызов базовой версии метода для выполнения логики,
        //заданной в базовом классе.
        //Необходимо для корректной инициализации Activity.
        super.onCreate(savedInstanceState);
        //Установка ресурса разметки интерфейса
        //в качестве содержимого Activity.
        setContentView(R.layout.activity_main);

        //Получение ссылки на верхнюю панель инструментов.
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        //Установка панели инструментов в качестве панели действий
        //для текущего Activity.
        //Необходимо для привязки контроллера навигации к верхней панели.
        setSupportActionBar(toolbar);

        //Получение ссылки на выпадающий список недель.
        setWeeksSpinner(findViewById(R.id.weeks_spinner));

        //Передача ссылки на ресурсы приложения
        //в перечисления CourseTypes и DaysOfWeek
        //для корректного приведения значений к строкам
        //в зависимости от текущего языка системы.
        CourseTypes.setResources(getResources());
        DaysOfWeek.setResources(getResources());

        //Получение ссылки на фрагмент-контейнер,
        //выступающий корневым элементом навигации.
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        //Получение ссылки на нижнюю панель навигации.
        BottomNavigationView navigationView =
                findViewById(R.id.bottom_navigation);
        //Получение контроллера навигации из фрагмента-контейнера.
        NavController navController =
                Objects.requireNonNull(navHostFragment)
                        .getNavController();
        //Создание конфигурации навигации для нижней панели навигации
        //и верхней панели действий.
        appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.nav_schedule,
                R.id.nav_courses,
                R.id.nav_instructors,
                R.id.nav_preferences)
                .build();
        //Привязка конфигурации и контроллера навигации
        //к верхней панели действий данной Activity.
        NavigationUI.setupActionBarWithNavController(this,
                navController,
                appBarConfiguration);
        //Привязка конфигурации и контроллера навигации
        //к нижней панели навигации данной Activity.
        NavigationUI.setupWithNavController(navigationView,
                navController);

        //Регистрация слушателя на событие смены раздела приложения.
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    //Отключение режима действий
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                    //Отключение выпадающего списка недель
                    getWeeksSpinner().setVisibility(View.GONE);
                    //Отключение экранной клавиатуры
                    hideKeyboard();
                });
    }

    /**
     * Вызывается при нажатии на системную кнопку «Назад»
     * или кнопку «Назад» верхней панели действий.
     *
     * @return true, если навигация вверх по графу была осуществлена успешно.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this,
                        R.id.nav_host_fragment);
        hideKeyboard();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Отвечает за скрытие экранной клавиатуры.
     */
    private void hideKeyboard() {
        InputMethodManager imm =
                getSystemService(InputMethodManager.class);
        if (imm != null) {
            IBinder token = getWindow().getDecorView().getWindowToken();
            imm.hideSoftInputFromWindow(token, 0);
        }
    }

    /**
     * @return Текущий экземпляр выпадающего списка недель.
     */
    public Spinner getWeeksSpinner() {
        return weeksSpinner;
    }

    /**
     * Задаёт новый экземпляр списка недель.
     *
     * @param weeksSpinner новый экземпляр списка недель.
     */
    private void setWeeksSpinner(Spinner weeksSpinner) {
        this.weeksSpinner = weeksSpinner;
    }
}