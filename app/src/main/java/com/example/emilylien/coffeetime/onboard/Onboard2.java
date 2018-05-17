package com.example.emilylien.coffeetime.onboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.emilylien.coffeetime.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Onboard2 extends AppCompatActivity {
    @BindView(R.id.btnOnboard2Next) Button btnOnboard2Next;
    @BindView(R.id.btnOnboard2Back) Button btnOnboard2Back;
    @BindView(R.id.etAge) MaterialEditText etAge;
    @BindView(R.id.etMaximum) MaterialEditText etMaximum;
    @BindView(R.id.etMinimum) MaterialEditText etMinimum;
    @BindView(R.id.switchPregnant) Switch switchPregnant;
    @BindView(R.id.tvRecommendedDailyLimitAnswer) TextView tvRecommendedDailyLimitAnswer;

    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board2);
        ButterKnife.bind(this);

        setEtAgeListener();
        setSwitchPregnantListener();
    }

    private void setEtAgeListener() {
        etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String ageString = etAge.getText().toString();
                if (!ageString.matches("")){
                    age = Integer.parseInt(ageString);
                    setRecommendedDailyLimitBasedOnAge(age);
                } else {
                    tvRecommendedDailyLimitAnswer.setText(R.string.adult_recommended_max);
                }

                if (switchPregnant.isChecked() && age > 18)
                    tvRecommendedDailyLimitAnswer.setText(R.string.pregnant_recommended_max);
            }
        });
    }

    private void setRecommendedDailyLimitBasedOnAge(int age) {
        if (age < 13)
            tvRecommendedDailyLimitAnswer.setText(R.string.children_recommended_max);
        else if (age <= 18)
            tvRecommendedDailyLimitAnswer.setText(R.string.teen_recommended_max);
        else
            tvRecommendedDailyLimitAnswer.setText(R.string.adult_recommended_max);
    }

    private void setSwitchPregnantListener() {
        switchPregnant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && age > 18)
                    tvRecommendedDailyLimitAnswer.setText(R.string.pregnant_recommended_max);
            }
        });
    }

    @OnClick(R.id.btnOnboard2Next) void nextClicked() {
        saveMax();
        saveMin();

        Intent intent = new Intent(Onboard2.this, Onboard3.class);
        startActivity(intent);
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

    @OnClick(R.id.btnOnboard2Back) void backClicked() {
        Intent intent = new Intent(Onboard2.this, Onboard1.class);
        startActivity(intent);
    }
}