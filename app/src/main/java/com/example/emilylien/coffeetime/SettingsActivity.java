package com.example.emilylien.coffeetime;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.emilylien.coffeetime.onboard.Onboard3;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.etHalflifeSettings) MaterialEditText etHalfLife;
    @BindView(R.id.etMaximumSettings) MaterialEditText etMaximum;
    @BindView(R.id.etMinimumSettings) MaterialEditText etMinimum;
    @BindView(R.id.etSleepGoal) MaterialEditText etSleepGoal;
    @BindView(R.id.etMonday) MaterialEditText etMonday;
    @BindView(R.id.etTuesday) MaterialEditText etTuesday;
    @BindView(R.id.etWednesday) MaterialEditText etWednesday;
    @BindView(R.id.etThursday) MaterialEditText etThursday;
    @BindView(R.id.etFriday) MaterialEditText etFriday;
    @BindView(R.id.etSaturday) MaterialEditText etSaturday;
    @BindView(R.id.etSunday) MaterialEditText etSunday;

    @BindView(R.id.btnSave) Button btnSave;

    MaterialEditText[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        loadHalfLife(sharedPreferences);
        loadMin(sharedPreferences);
        loadMax(sharedPreferences);
        loadDays(sharedPreferences);

        setUpEtDays();

        setUpSleepGoal(sharedPreferences);
    }

    private void setUpSleepGoal(SharedPreferences sharedPreferences) {
        String sleepGoal = sharedPreferences.getString(getString(R.string.SLEEP_GOAL), getString(R.string.ERROR));
        if (!sleepGoal.equals(getString(R.string.ERROR)))
            etSleepGoal.setText(sleepGoal);

        setUpEtSleepGoalListener();
    }

    private void loadDays(SharedPreferences sharedPreferences) {
        loadMonday(sharedPreferences);
        loadTuesday(sharedPreferences);
        loadWednesday(sharedPreferences);
        loadThursday(sharedPreferences);
        loadFriday(sharedPreferences);
        loadSaturday(sharedPreferences);
        loadSunday(sharedPreferences);
    }

    private void loadSunday(SharedPreferences sharedPreferences) {
        String sunday = sharedPreferences.getString(getString(R.string.SUNDAY), getString(R.string.ERROR));
        if (!sunday.equals(getString(R.string.ERROR)))
            etSunday.setText(sunday);
    }

    private void loadSaturday(SharedPreferences sharedPreferences) {
        String saturday = sharedPreferences.getString(getString(R.string.SATURDAY), getString(R.string.ERROR));
        if (!saturday.equals(getString(R.string.ERROR)))
            etSaturday.setText(saturday);
    }

    private void loadFriday(SharedPreferences sharedPreferences) {
        String friday = sharedPreferences.getString(getString(R.string.FRIDAY), getString(R.string.ERROR));
        if (!friday.equals(getString(R.string.ERROR)))
            etFriday.setText(friday);
    }

    private void loadThursday(SharedPreferences sharedPreferences) {
        String thursday = sharedPreferences.getString(getString(R.string.THURSDAY), getString(R.string.ERROR));
        if (!thursday.equals(getString(R.string.ERROR)))
            etThursday.setText(thursday);
    }

    private void loadWednesday(SharedPreferences sharedPreferences) {
        String wednesday = sharedPreferences.getString(getString(R.string.WEDNESDAY), getString(R.string.ERROR));
        if (!wednesday.equals(getString(R.string.ERROR)))
            etWednesday.setText(wednesday);
    }

    private void loadTuesday(SharedPreferences sharedPreferences) {
        String tuesday = sharedPreferences.getString(getString(R.string.TUESDAY), getString(R.string.ERROR));
        if (!tuesday.equals(getString(R.string.ERROR)))
            etTuesday.setText(tuesday);
    }

    private void loadMonday(SharedPreferences sharedPreferences) {
        String monday = sharedPreferences.getString(getString(R.string.MONDAY), getString(R.string.ERROR)); //-1 is stored in saved preferences when there is no wakeup time
        if (!monday.equals(getString(R.string.ERROR)))
            etMonday.setText(monday);
    }

    private void loadMin(SharedPreferences sharedPreferences) {
        int min = sharedPreferences.getInt(getString(R.string.MIN), -1);
        if (min != -1)
            etMinimum.setText(Integer.toString(min));
    }

    private void loadMax(SharedPreferences sharedPreferences) {
        int max = sharedPreferences.getInt(getString(R.string.MAX), -1);
        if (max != -1)
            etMaximum.setText(Integer.toString(max));
    }

    private void loadHalfLife(SharedPreferences sharedPreferences) {
        float halflife = sharedPreferences.getFloat(getString(R.string.HALF_LIFE), -1);
        if (halflife != -1)
            etHalfLife.setText(Float.toString(halflife));
    }

    private void setUpEtSleepGoalListener() {
        etSleepGoal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    disableKeyboard(etSleepGoal, event);
                    showTimePickerSleepGoal();
                }
                return true; // consume touch event
            }
        });
    }

    private void showTimePickerSleepGoal() {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etSleepGoal.setText(hourOfDay + getString(R.string.h_with_space) + fixMinutes(minute) + getString(R.string.m));
            }
        }, hours, mins, true);
        timePickerDialog.show();
    }

    private void showTimePickerDay(final MaterialEditText day) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                day.setText(turnToTimeString(hourOfDay, minute));
            }
        }, hours, mins, false);
        timePickerDialog.show();
    }

    private String turnToTimeString(int hourOfDay, int minute) {
        String AM_PM;
        if (hourOfDay > 12) {
            AM_PM = getString(R.string.PM);
            hourOfDay -= 12;
        } else {
            AM_PM = getString(R.string.AM);
        }

        return hourOfDay + getString(R.string.semicolon) + fixMinutes(minute) + " " + AM_PM;
    }

    private String fixMinutes(int minutes) {
        return minutes < 10 ? "0" + minutes : "" + minutes;
    }

    private void saveSleepGoal() {
        String sleepGoal;
        if (etSleepGoal.getText().toString().matches("")) {
            sleepGoal = etSleepGoal.getHint().toString();
        } else {
            sleepGoal = etSleepGoal.getText().toString();
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.SLEEP_GOAL), sleepGoal);
        editor.commit();
    }

    private void saveDays() {
        for (MaterialEditText day : days) {
            String wakeupTime = getString(R.string.SLEEP_IN);
            String currDayString = day.getHint().toString().toUpperCase();
            String dayText = day.getText().toString();
            if (!dayText.matches(""))
                wakeupTime = dayText;

            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(currDayString, wakeupTime);
            editor.commit();
        }
    }

    @OnClick(R.id.btnSave) public void saveBtn() {
        saveHalfLife();
        saveMax();
        saveMin();
        saveSleepGoal();
        saveDays();

        finish();
    }

    private void saveHalfLife() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);

        String onlyHalfLife = etHalfLife.getText().toString().replaceAll("[^\\d.]", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(getString(R.string.HALF_LIFE), Float.parseFloat(onlyHalfLife));
        editor.commit();
    }

    private void saveMax() {
        int max;
        try {
            max = Integer.parseInt(etMaximum.getText().toString());
        } catch (Exception e) {
            max = 400;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.MAX), max);
        editor.commit();
    }

    private void saveMin() {
        int min;
        try {
            min = Integer.parseInt(etMinimum.getText().toString());
        } catch (Exception e) {
            min = 100;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.MIN), min);
        editor.commit();
    }

    private void disableKeyboard(MaterialEditText etText, MotionEvent event) {
        int inType = etText.getInputType(); // backup the input type
        etText.setInputType(InputType.TYPE_NULL); // disable soft input
        etText.onTouchEvent(event); // call native handler
        etText.setInputType(inType); // restore input type
    }

    private void setUpEtDays() {
        days = new MaterialEditText[] {etMonday, etTuesday, etWednesday, etThursday, etFriday, etSaturday, etSunday};
        for (final MaterialEditText day : days) {
            setUpEtDaysListener(day);
        }
    }

    private void setUpEtDaysListener(final MaterialEditText day) {
        day.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    disableKeyboard(day, event);
                    showTimePickerDay(day);
                }
                return true; // consume touch event
            }
        });
    }
}
