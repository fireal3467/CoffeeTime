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
        float halflife = sharedPreferences.getFloat(getString(R.string.HALF_LIFE), -1);
        int min = sharedPreferences.getInt(getString(R.string.MIN), -1);
        int max = sharedPreferences.getInt(getString(R.string.MAX), -1);
        String sleepGoal = sharedPreferences.getString(getString(R.string.SLEEP_GOAL), "ERROR");
        String monday = sharedPreferences.getString("MONDAY", "ERROR"); //-1 is stored in saved preferences when there is no wakeup time
        String tuesday = sharedPreferences.getString("TUESDAY", "ERROR");
        String wednesday = sharedPreferences.getString("WEDNESDAY", "ERROR");
        String thursday = sharedPreferences.getString("THURSDAY", "ERROR");
        String friday = sharedPreferences.getString("FRIDAY", "ERROR");
        String saturday = sharedPreferences.getString("SATURDAY", "ERROR");
        String sunday = sharedPreferences.getString("SUNDAY", "ERROR");

        days = new MaterialEditText[] {etMonday, etTuesday, etWednesday, etThursday, etFriday, etSaturday, etSunday};

        for (final MaterialEditText day : days) {
            day.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int inType = day.getInputType(); // backup the input type
                        day.setInputType(InputType.TYPE_NULL); // disable soft input
                        day.onTouchEvent(event); // call native handler
                        day.setInputType(inType); // restore input type

                        showTimePickerDay(day);
                    }
                    return true; // consume touch event
                }
            });
        }

        etSleepGoal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int inType = etSleepGoal.getInputType(); // backup the input type
                    etSleepGoal.setInputType(InputType.TYPE_NULL); // disable soft input
                    etSleepGoal.onTouchEvent(event); // call native handler
                    etSleepGoal.setInputType(inType); // restore input type

                    showTimePickerSleepGoal();
                }
                return true; // consume touch event
            }
        });

        if (halflife != -1)
            etHalfLife.setText(Float.toString(halflife));

        if (max != -1)
            etMaximum.setText(Integer.toString(max));

        if (min != -1)
            etMinimum.setText(Integer.toString(min));

        if (!sleepGoal.equals("ERROR"))
            etSleepGoal.setText(sleepGoal);

        if (!monday.equals("ERROR"))
            etMonday.setText(monday);

        if (!tuesday.equals("ERROR"))
            etTuesday.setText(tuesday);

        if (!wednesday.equals("ERROR"))
            etWednesday.setText(wednesday);

        if (!thursday.equals("ERROR"))
            etThursday.setText(thursday);

        if (!friday.equals("ERROR"))
            etFriday.setText(friday);

        if (!saturday.equals("ERROR"))
            etSaturday.setText(saturday);

        if (!sunday.equals("ERROR"))
            etSunday.setText(sunday);
    }

    private void showTimePickerSleepGoal() {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etSleepGoal.setText(hourOfDay + "h " + fixMinutes(minute) + "m");
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
                String AM_PM;
                if (hourOfDay > 12) {
                    AM_PM = "PM";
                    hourOfDay -= 12;
                } else {
                    AM_PM = "AM";
                }

                day.setText(hourOfDay + ":" + fixMinutes(minute) + " " + AM_PM);
            }
        }, hours, mins, false);
        timePickerDialog.show();
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
            String wakeupTime = "SLEEP_IN";
            String currDayString = day.getHint().toString().toUpperCase();
            String dayText = day.getText().toString();
            if (!dayText.matches("")) {
                wakeupTime = day.getText().toString();
            }

            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(currDayString, wakeupTime);
            editor.commit();
        }
    }

    @OnClick(R.id.btnSave) public void saveBtn() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);

        String onlyHalfLife = etHalfLife.getText().toString().replaceAll("[^\\d.]", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(getString(R.string.HALF_LIFE), Float.parseFloat(onlyHalfLife));
        editor.commit();

        int max;
        try {
            max = Integer.parseInt(etMaximum.getText().toString());
        } catch (Exception e) {
            max = 400;
        }

        editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.MAX), max);
        editor.commit();

        int min;
        try {
            min = Integer.parseInt(etMinimum.getText().toString());
        } catch (Exception e) {
            min = 100;
        }
        editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.MIN), min);
        editor.commit();

        saveSleepGoal();
        saveDays();

        finish();
    }
}
