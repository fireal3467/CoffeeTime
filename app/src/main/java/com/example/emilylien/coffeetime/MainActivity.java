package com.example.emilylien.coffeetime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emilylien.coffeetime.R;
import com.example.emilylien.coffeetime.data.AppDatabase;
import com.example.emilylien.coffeetime.data.DrinkInfo;
import com.example.emilylien.coffeetime.data.TakenDrink;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.example.emilylien.coffeetime.AddDrinkActivity.CHOSEN_DRINK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.tvHowMuchMore) TextView tvHowMuchMore;
    @BindView(R.id.tvCurrInYou) TextView tvCurrInYou;

    private List<TakenDrink> takenDrinks;
    private List<DrinkInfo> recentDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initDrawerSettings(toolbar);
        initFabSettings();

        initLists();

        //LOAD SAVED PREFERENCES
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        float halflife = sharedPreferences.getFloat(getString(R.string.HALF_LIFE), -1);
        int min = sharedPreferences.getInt(getString(R.string.MIN), -1);
        int max = sharedPreferences.getInt(getString(R.string.MAX), -1);
        long sleepGoal = sharedPreferences.getLong(getString(R.string.SLEEP_GOAL), -1);
        long monday = sharedPreferences.getLong("MONDAY", -2); //-1 is stored in saved preferences when there is no wakeup time
        long tuesday = sharedPreferences.getLong("TUESDAY", -2);
        long wednesday = sharedPreferences.getLong("WEDNESDAY", -2);
        long thursday = sharedPreferences.getLong("THURSDAY", -2);
        long friday = sharedPreferences.getLong("FRIDAY", -2);
        long saturday = sharedPreferences.getLong("SATURDAY", -2);
        long sunday = sharedPreferences.getLong("SUNDAY", -2);

        Log.d("testing", "halflife: " + halflife);
        Log.d("testing", "min: " + min);
        Log.d("testing", "max: " + max);
        Log.d("testing", "sleep goal: " + sleepGoal);
        Log.d("testing", "monday: " + monday);
        Log.d("testing", "tuesday: " + tuesday);
        Log.d("testing", "wednesday: " + wednesday);
        Log.d("testing", "thursday: " + thursday);
        Log.d("testing", "friday: " + friday);
        Log.d("testing", "saturday: " + saturday);
        Log.d("testing", "sunday: " + sunday);

        // TIME STUFF
        //get current time
//        DateTime now = new DateTime(DateTimeZone.getDefault()); // 5:40 PM
//        int nowHours = now.getHourOfDay();
//        int nowMins = now.getMinuteOfHour();
        Calendar now = Calendar.getInstance();
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        int nowMins = now.get(Calendar.MINUTE);

        //get sleep goal time
        DateTimeFormatter sleepGoalFormat = DateTimeFormat.forPattern("hh'h' mm'm'");
        DateTime sleepGoalDate = sleepGoalFormat.parseDateTime("10h 30m");
        int sleepGoalHours = sleepGoalDate.getHourOfDay();
        int sleepGoalMins = sleepGoalDate.getMinuteOfHour();

        //get wakeup time
        DateTimeFormatter wakeUpFormat = DateTimeFormat.forPattern("hh:mm a");
        DateTime wakeupDate = wakeUpFormat.parseDateTime("7:30 AM");
        int wakeupHours = wakeupDate.getHourOfDay();
        int wakeupMins = wakeupDate.getMinuteOfHour();

        //calculate bedtime
        int bedTimeHour = wakeupDate.minusHours(sleepGoalHours).getHourOfDay();
        String AM_PM = "";
        if (bedTimeHour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
            bedTimeHour -= 12;
        }
        String bedTimeMins = fixMinutes(wakeupDate.minusMinutes(sleepGoalMins).getMinuteOfHour());

        Log.d("sleepsleep", "now: " + nowHours + ":" + nowMins);
        Log.d("sleepsleep", "sleepGoal: " + sleepGoalHours + "h " + sleepGoalMins + "m");
        Log.d("sleepsleep", "wakeup: " + wakeupHours + ":" + wakeupMins);
        Log.d("sleepsleep", "Bedtime: " + bedTimeHour + ":" + bedTimeMins + AM_PM);
    }

    private void initLists(){
        new Thread(){
            @Override
            public void run() {
                final List<TakenDrink> takenDrinksList =
                        AppDatabase.getAppDatabase(MainActivity.this).takenDrinkDAO().getAllTakenDrinks();

                final List<DrinkInfo> recentDrinksList =
                        AppDatabase.getAppDatabase(MainActivity.this).drinkDAO().getAllForCategory(-1);

                takenDrinks = takenDrinksList;
                recentDrinks = recentDrinksList;

                for(int i = 0; i < takenDrinks.size();i++){
                    System.out.println(Integer.toString(takenDrinks.get(i).getCaffineAmount()));
                    System.out.println(recentDrinks.get(i).getDrinkName());
                }

            }
        }.start();
    }


    private void initDrawerSettings(Toolbar toolbar){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFabSettings() {
        final FabSpeedDial fabSpeedDial = findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.fab_add_drink) {
                    startAddDrinkActivity();
                } else if (id == R.id.recent_one) {

                } else if (id == R.id.recent_two) {

                } else if (id == R.id.recent_three) {

                }
                fabSpeedDial.closeMenu();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_add_drink) {
            startAddDrinkActivity();
        } else if (id == R.id.nav_share) {
            //TODO - we need stuff here
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startAddDrinkActivity(){
        Intent intent = new Intent(MainActivity.this, AddDrinkActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            DrinkInfo drink = (DrinkInfo) bundle.getSerializable(CHOSEN_DRINK);
            addDrink(drink);
        }
    }


    public void addDrink(DrinkInfo drink){
        long seconds = System.currentTimeMillis();
        TakenDrink takenDrink = new TakenDrink(drink.getCaffineAmount(), seconds);
        takenDrinks.add(takenDrink);
        drink.setDrinkCategory(-1);
        recentDrinks.add(drink);
        saveDrinks(takenDrink, drink);


        Toast.makeText(this, drink.getDrinkName(), Toast.LENGTH_SHORT).show();

//        TODO - do something about updating UI and calculating time to set the images
    }

    private void saveDrinks(final TakenDrink takenDrink, final DrinkInfo drinkInfo){
        new Thread(){
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(MainActivity.this).
                        drinkDAO().insertDrink(drinkInfo);
                drinkInfo.setDrinkID(id);

                id = AppDatabase.getAppDatabase(MainActivity.this).
                        takenDrinkDAO().insertDrink(takenDrink);
                takenDrink.setDrinkID(id);

                //TODO - have to Update UI, maybe do it here in runUIThread
            }
        }.start();
    }


    public void setTvCurrInYouText(String currInYouText) {
        this.tvCurrInYou.setText(currInYouText + "mg in body");
    }

    public void setTvHowMuchMore(String howMuchMoreText) {
        this.tvHowMuchMore.setText("Can take in " + howMuchMoreText + "mg");
    }

    public int extractCurrInYou() {
        return extractNumber(tvCurrInYou.getText().toString());
    }

    public int extractHowMuchMore() {
        return extractNumber(tvHowMuchMore.getText().toString());

    }

    public int extractNumber(String string) {
        String onlyNumString = string.replaceAll("\\D+", "");
        return Integer.parseInt(onlyNumString);
    }

    private String fixMinutes(int minutes) {
        return minutes < 10 ? "0" + minutes : "" + minutes;
    }
}
