package com.example.emilylien.coffeetime.onboard;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.emilylien.coffeetime.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Onboard1 extends AppCompatActivity {
    @BindView(R.id.btnOnboard1Next) Button btnOnboard1Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board1);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnOnboard1Next) void clickedNext() {
        Intent intent = new Intent(Onboard1.this, Onboard2.class);
        startActivity(intent);
    }
}
