package com.example.emilylien.coffeetime.onboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.emilylien.coffeetime.MainActivity;
import com.example.emilylien.coffeetime.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Onboard1 extends AppCompatActivity {
    @BindView(R.id.etHalflife) MaterialEditText etHalflife;
    @BindView(R.id.btnOnboard1Next) Button btnOnboard1Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board1);
        ButterKnife.bind(this);

        checkIfHasOnboarded();
    }

    private void checkIfHasOnboarded() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        boolean hasOnboarded = sharedPreferences.getBoolean(getString(R.string.DONE_ONBOARDING), false);
        if (hasOnboarded) {
            Intent intent = new Intent(Onboard1.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.btnOnboard1Next) void clickedNext() {
        float halflife;
        try {
            halflife = Float.parseFloat(etHalflife.getText().toString());
        } catch (Exception e) {
            halflife = (float) 5.0;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(getString(R.string.HALF_LIFE), halflife);
        editor.commit();

        Intent intent = new Intent(Onboard1.this, Onboard2.class);
        startActivity(intent);
    }
}
