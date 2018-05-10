package com.example.emilylien.coffeetime.onboard;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.emilylien.coffeetime.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Onboard1 extends AppCompatActivity {
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tabDots) TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board1);
        ButterKnife.bind(this);

        tabLayout.setupWithViewPager(viewPager, true);
    }
}
