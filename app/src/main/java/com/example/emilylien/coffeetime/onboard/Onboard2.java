package com.example.emilylien.coffeetime.onboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnOnboard2Next) void nextClicked() {
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

        int min;
        try {
            min = Integer.parseInt(etMinimum.getText().toString());
        } catch (Exception e) {
            min = 100;
        }
        editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.MIN), min);
        editor.commit();

        Intent intent = new Intent(Onboard2.this, Onboard3.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnOnboard2Back) void backClicked() {
        Intent intent = new Intent(Onboard2.this, Onboard1.class);
        startActivity(intent);
    }
}
