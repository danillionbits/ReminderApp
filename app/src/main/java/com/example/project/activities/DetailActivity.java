package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.database.AppDatabase;
import com.example.project.database.Event;
import com.example.project.databinding.ActivityDetailBinding;
import com.example.project.dialogs.DatePicker;
import com.example.project.dialogs.TimePicker;
import com.example.project.utils.DateTimeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Display single event. This activity applies to both
 * viewing an existed event or creating a new event.
 *
 * @author Dat Pham, Tran Cong Minh
 * @version 0.0.1
 * @since 2022-02-20
 */
public class DetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private AppDatabase mDatabase;
    private ActivityDetailBinding mBinding;
    private Event mEvent;
    HashMap<String, String> hmColors = new HashMap<>();
    private ArrayAdapter<String> colorAdapter;

    private static final int REQUEST_CREATE = 0;
    private static final int REQUEST_EDIT = 1;
    private static final int CHANGE_BEGIN_DATE = 0;
    private static final int CHANGE_END_DATE = 1;
    private static final int CHANGE_BEGIN_TIME = 0;
    private static final int CHANGE_END_TIME = 1;

    private int mRequest;
    private int mDateChange;
    private int mTimeChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        View mView = mBinding.getRoot();
        setContentView(mView);

        /* Initiate database and get id of event */
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        long id = getIntent().getLongExtra(MainActivity.EVENT_ID, -1);

        /* Check if this activity is for creating or editing event */
        if (id != -1) {
            mRequest = REQUEST_EDIT;
            mEvent = mDatabase.eventDao().getById(id);
            mBinding.txtAppBarTitle.setText(R.string.edit_event_title);
        } else {
            mRequest = REQUEST_CREATE;
            mEvent = new Event();
            mBinding.txtAppBarTitle.setText(R.string.add_event_title);
        }

        /* Set up toolbar */
        mBinding.ivReturn.setOnClickListener(view -> finish());

        /* Register onClickListener for Date and Time buttons */
        setUpDateTimeButtons();

        /* Set up color spinner */
        populateColorsMap();
        colorAdapter = new ArrayAdapter(this, R.layout.menu_list_item, hmColors.keySet().toArray());
        mBinding.acColor.setAdapter(colorAdapter);

        /* Set up and register onClickListener submit and delete buttons */
        setUpButtons(id);

        updateUI();
    }

    /**
     * Register onDateSet for DatePickerDialog.
     * Value is set to mEvent variable and button UI is updated
     * to the selected date accordingly.
     */
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int date) {
        String activeDate = DateTimeUtils.buildDate(year, month + 1, date);

        if (mDateChange == CHANGE_BEGIN_DATE) {
            mEvent.setBeginDate(activeDate);
            mBinding.btnBeginDate.setText(mEvent.getBeginDate());
        } else {
            mEvent.setEndDate(activeDate);
            mBinding.btnEndDate.setText(mEvent.getEndDate());
        }
    }

    /**
     * Register onTimeSet for TimePickerDialog.
     * Value is set to mEvent variable and button UI is updated
     * to the selected time accordingly.
     */
    @Override
    public void onTimeSet(android.widget.TimePicker view, int hour, int minute) {
        String activeTime = DateTimeUtils.buildTime(hour, minute);

        if (mTimeChange == CHANGE_BEGIN_TIME) {
            mEvent.setBeginTime(activeTime);
            mBinding.btnBeginTime.setText(mEvent.getBeginTime());
        } else {
            mEvent.setEndTime(activeTime);
            mBinding.btnEndTime.setText(mEvent.getEndTime());
        }
    }

    /**
     * Set up date and time picker buttons
     */
    private void setUpDateTimeButtons() {
        mBinding.btnBeginDate.setOnClickListener(view -> {
            mDateChange = CHANGE_BEGIN_DATE;
            DatePicker datePickerDialogFragment = new DatePicker();
            datePickerDialogFragment.show(getSupportFragmentManager(), "BEGIN DATE PICK");
        });

        mBinding.btnEndDate.setOnClickListener(view -> {
            mDateChange = CHANGE_END_DATE;
            DatePicker datePickerDialogFragment = new DatePicker();
            datePickerDialogFragment.show(getSupportFragmentManager(), "END DATE PICK");
        });

        mBinding.btnBeginTime.setOnClickListener(view -> {
            mTimeChange = CHANGE_BEGIN_TIME;
            TimePicker timePickerDialogFragment = new TimePicker();
            timePickerDialogFragment.show(getSupportFragmentManager(), "BEGIN TIME PICK");
        });

        mBinding.btnEndTime.setOnClickListener(v -> {
            mTimeChange = CHANGE_END_TIME;
            TimePicker timePickerDialogFragment = new TimePicker();
            timePickerDialogFragment.show(getSupportFragmentManager(), "END TIME PICK");
        });
    }

    /**
     * Declare and assign color name to its correct value with Hashmap hmColors
     */
    private void populateColorsMap() {
        hmColors.put(getString(R.string.red), "#F44336");
        hmColors.put(getString(R.string.pink), "#E91E63");
        hmColors.put(getString(R.string.orange), "#FF9800");
        hmColors.put(getString(R.string.yellow), "#FFEB3B");
        hmColors.put(getString(R.string.green), "#4CAF50");
        hmColors.put(getString(R.string.blue), "#2196F3");
        hmColors.put(getString(R.string.purple), "#9C27B0");
        hmColors.put(getString(R.string.brown), "#795548");
    }

    /**
     * Set up submit and delete buttons
     */
    private void setUpButtons(long id) {
        mBinding.btnSubmit.setOnClickListener(view -> {
            setEventValue();
            if (mEvent.getTitle().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.missing_title), Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (mRequest == REQUEST_CREATE) {
                    mDatabase.eventDao().insert(mEvent);
                } else {
                    mDatabase.eventDao().update(mEvent);
                }
                finish();
            }
        });

        if (mRequest == REQUEST_CREATE) {
            mBinding.btnDelete.setVisibility(View.GONE);
        } else {
            mBinding.btnDelete.setOnClickListener(v -> {
                mDatabase.eventDao().deleteById(id);
                finish();
            });
        }
    }

    /**
     * Update the UI of all components, including all
     * textInputLayouts and btnDate and btnTime to variable
     * mEvent's values.
     */
    private void updateUI() {
        mBinding.tilTitle.getEditText().setText(mEvent.getTitle());
        mBinding.btnBeginDate.setText(mEvent.getBeginDate());
        mBinding.btnBeginTime.setText(mEvent.getBeginTime());
        mBinding.btnEndDate.setText(mEvent.getEndDate());
        mBinding.btnEndTime.setText(mEvent.getEndTime());

        mBinding.tilLocation.getEditText().setText(mEvent.getLocation());
        int i = 0;
        for (Map.Entry<String, String> set : hmColors.entrySet()) {
            if (set.getValue().equals(mEvent.getColor())) {
                mBinding.acColor.setText(colorAdapter.getItem(i), false);
                break;
            }
            i++;
        }

        mBinding.tilAttendees.getEditText().setText(mEvent.getAttendees());
        mBinding.tilDescription.getEditText().setText(mEvent.getDescription());
    }

    /**
     * Set all values of variable mEvent to text of UI components
     */
    private void setEventValue() {
        mEvent.setTitle(String.valueOf(mBinding.tilTitle.getEditText().getText()));
        mEvent.setLocation(String.valueOf(mBinding.tilLocation.getEditText().getText()));
        String hmColorKey = String.valueOf(mBinding.tilColor.getEditText().getText());
        mEvent.setColor(hmColors.get(hmColorKey));
        mEvent.setAttendees(String.valueOf(mBinding.tilAttendees.getEditText().getText()));
        mEvent.setDescription(String.valueOf(mBinding.tilDescription.getEditText().getText()));
    }
}