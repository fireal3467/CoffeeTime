package com.example.emilylien.coffeetime.onboard;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.emilylien.coffeetime.MainActivity;
import com.example.emilylien.coffeetime.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Onboard3 extends AppCompatActivity {
    @BindView(R.id.btnOnboard3Done) Button btnOnboard3Done;
    @BindView(R.id.btnOnboard3Back) Button btnOnboard3Back;

    @BindView(R.id.etSleepGoal) MaterialEditText etSleepGoal;

    MaterialEditText[] days;
    @BindView(R.id.etMonday) MaterialEditText etMonday;
    @BindView(R.id.etTuesday) MaterialEditText etTuesday;
    @BindView(R.id.etWednesday) MaterialEditText etWednesday;
    @BindView(R.id.etThursday) MaterialEditText etThursday;
    @BindView(R.id.etFriday) MaterialEditText etFriday;
    @BindView(R.id.etSaturday) MaterialEditText etSaturday;
    @BindView(R.id.etSunday) MaterialEditText etSunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board3);
        ButterKnife.bind(this);

        setUpEtDays();
        setUpEtSleepGoalListener();
    }

    private void disableKeyboard(MaterialEditText etText, MotionEvent event) {
        int inType = etText.getInputType(); // backup the input type
        etText.setInputType(InputType.TYPE_NULL); // disable soft input
        etText.onTouchEvent(event); // call native handler
        etText.setInputType(inType); // restore input type
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

    private void showTimePickerSleepGoal() {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Onboard3.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etSleepGoal.setText(hourOfDay + getString(R.string.h) + " " + fixMinutes(minute) + getString(R.string.m));
            }
        }, hours, mins, true);
        timePickerDialog.show();
    }

    private void showTimePickerDay(final MaterialEditText day) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Onboard3.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = turnToTimeString(hourOfDay, minute);
                day.setText(time);
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

    @OnClick(R.id.btnOnboard3Done) public void doneOnboarding() {
        saveSleepGoal();
        saveDays();
        saveDoneOnBoarding();

        Intent intent = new Intent(Onboard3.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveDoneOnBoarding() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.DONE_ONBOARDING), true);
        editor.commit();
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

    private String fixMinutes(int minutes) {
        return minutes < 10 ? "0" + minutes : "" + minutes;
    }
}
