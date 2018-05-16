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
    }

    private void showTimePickerSleepGoal() {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Onboard3.this, new TimePickerDialog.OnTimeSetListener() {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(Onboard3.this, new TimePickerDialog.OnTimeSetListener() {
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

    @OnClick(R.id.btnOnboard3Done) public void doneOnboarding() {
        saveSleepGoal();
        saveDays();

        Intent intent = new Intent(Onboard3.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveSleepGoal() {
        long sleepGoal = -1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh'h' mm'm'");
        Date date;

        try {
            String sleepGoalString = etSleepGoal.getText().toString();
            date = simpleDateFormat.parse(sleepGoalString);
            sleepGoal = date.getTime();
        } catch (Exception e) {
            try {
                date = simpleDateFormat.parse("7h 30m");
                sleepGoal = date.getTime();
            } catch (Exception e2) {}
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(getString(R.string.SLEEP_GOAL), sleepGoal);
        editor.commit();
    }

    private void saveDays() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date;
        String currDayString;

        for (MaterialEditText day : days) {
            long wakeUp;
            currDayString = day.getHint().toString().toUpperCase();
            try {
                String wakeUpString = day.getText().toString();
                date = dateFormat.parse(wakeUpString);
                wakeUp = date.getTime();
            } catch (Exception e) {
                wakeUp = -1;
            }

            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(currDayString, wakeUp);
            editor.commit();
        }
    }

    private String fixMinutes(int minutes) {
        return minutes < 10 ? "0" + minutes : "" + minutes;
    }
}
