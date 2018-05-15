package com.example.emilylien.coffeetime;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.emilylien.coffeetime.adapter.AddDrinkAdapter;
import com.example.emilylien.coffeetime.data.AppDatabase;
import com.example.emilylien.coffeetime.data.DrinkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDrinkActivity extends AppCompatActivity implements AddDrinkTypeDialog.AddDrinkTypeInterface{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private AddDrinkAdapter addDrinkAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        addDrinkAdapter = new AddDrinkAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(addDrinkAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void addDrinkType(final DrinkInfo drink, final int sectionNumber) {
        Toast.makeText(this, "Added a New Drink Type", Toast.LENGTH_SHORT).show();
        new Thread(){
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(AddDrinkActivity.this).
                        drinkDAO().insertDrink(drink);
                drink.setDrinkID(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addDrinkAdapter.addDrinkType(drink,sectionNumber);
                    }
                });
            }
        }.start();
    }
}
