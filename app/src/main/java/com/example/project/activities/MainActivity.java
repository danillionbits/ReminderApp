package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.R;
import com.example.project.adapters.DateAdapter;
import com.example.project.adapters.EventAdapter;
import com.example.project.database.AppDatabase;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.utils.CalendarUtils;

/**
 * Display calendar with upcoming events. Register add button and
 * navigation drawer to change calendar view's type
 *
 * @author Dat Pham, Tran Cong Minh, Santosh Nyaupane
 * @version 0.0.1
 * @since 2022-02-20
 */
public class MainActivity extends AppCompatActivity {
    private AppDatabase mDatabase;
    private ActivityMainBinding mBinding;
    private CalendarUtils mCalendarUtils;
    private float downX;

    public static final String EVENT_ID = "eid";
    protected static final float CLICK_DRAG_TOLERANCE = 20;
    public static final String PREF_FILE_NAME = "pref_file";
    public static final String PREF_CAL_TYPE = "cal_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View mView = mBinding.getRoot();
        setContentView(mView);

        /* Init database */
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        /* Init CalendarUtils */
        mCalendarUtils = new CalendarUtils(this);

        /* Set up calendar */
        setUpCalendar();

        /* Set up toolbar */
        mBinding.ivOpenNav.setOnClickListener(view -> {
            mBinding.drawerLayout.open();
        });

        mBinding.navView.setNavigationItemSelectedListener(menuItem -> {
            SharedPreferences sp = getSharedPreferences(PREF_FILE_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sp.edit();

            switch (menuItem.getItemId()) {
                case R.id.it_monthly:
                    spEditor.putInt(PREF_CAL_TYPE, CalendarUtils.CAL_MONTHLY);
                    break;
                case R.id.it_weekly:
                    spEditor.putInt(PREF_CAL_TYPE, CalendarUtils.CAL_WEEKLY);
                    break;
            }
            spEditor.apply();
            mBinding.drawerLayout.closeDrawers();
            mCalendarUtils.initTodayDateList();
            updateUI();
            return true;
        });

        mBinding.ivCalendarToday.setOnClickListener(view -> {
            mCalendarUtils.initTodayDateList();
            updateUI();
        });

        /* Set up Float action button */
        mBinding.fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCalendarUtils.initTodayDateList();
        updateUI();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    /**
     * Set up the calendar.
     * Register onClickListener for btnPrevious and btnNext.
     * Call populateCalendarDates to display dates in calendar view type.
     * Register onTouchListener for swiping left/right of calendar.
     */
    private void setUpCalendar() {
        mBinding.btnPrevious.setOnClickListener(view -> {
            mCalendarUtils.flushDate(20);
            updateUI();
        });

        mBinding.btnNext.setOnClickListener(view -> {
            mCalendarUtils.flushDate(-20);
            updateUI();
        });

        populateCalendarDates();

        /* Swiping left or right to previous/next month */
        mBinding.rvDates.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = motionEvent.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = motionEvent.getX() - downX;

                    if (Math.abs(deltaX) > CLICK_DRAG_TOLERANCE && downX != 0) {
                        mCalendarUtils.flushDate(deltaX);
                        downX = 0;
                        updateUI();
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        });
    }

    /**
     * Init recyclerView in linearLayout for upcoming events.
     * An eventAdapter instance is created and assigned to recyclerView component.
     * Passing context, database and calendarUtils as params for adapter
     */
    private void populateEvents() {
        EventAdapter eventAdapter = new EventAdapter(this, mDatabase, mCalendarUtils);
        mBinding.rvEvents.setAdapter(eventAdapter);
        mBinding.rvEvents.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Set the calendar title to its correct month and year.
     * Init recyclerView in gridLayout for calendar.
     * A dateAdapter instance is created and assigned to recyclerView component.
     * Passing context, calendarUtils and arrayList of DateEntity as params for adapter
     */
    private void populateCalendarDates() {
        mBinding.txtMonthYear.setText(mCalendarUtils.getCurrentMonthYear());

        DateAdapter dateAdapter = new DateAdapter(this, mCalendarUtils, mCalendarUtils.getDateEntities());
        mBinding.rvDates.setAdapter(dateAdapter);
        mBinding.rvDates.setLayoutManager(new GridLayoutManager(this, 7));
    }

    /**
     * Update the UI by calling populateCalendarDates and
     * populateEvents functions.
     */
    public void updateUI() {
        populateCalendarDates();
        populateEvents();
    }
}