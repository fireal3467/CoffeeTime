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
import android.text.format.DateUtils;
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
    private List<TakenDrink> testTakenDrinks = new ArrayList<>();
    private List<DrinkInfo> recentDrinks;

    private int sleepTimeMinutes;
    private float halflifeMinutes;
    private int caffineCurrentlySystem;
    private int maxCaffine;
    private boolean sleepIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSleepTimeMinutes();
        setHalflifeMinutes();
        setMaxCaffine();


        initDrawerSettings(toolbar);
        initFabSettings();

        initLists();

        calcCaffineInCurrSystem();
        maxCaffineCanTake();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setSleepTimeMinutes();
        calcCaffineInCurrSystem();
        maxCaffineCanTake();

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

                setSleepTimeMinutes();
                calcCaffineInCurrSystem();
                maxCaffineCanTake();


                //TODO - update current caffine amount;

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
                    //TODO - call add drink here for the most recent drinks
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        } else if (id == R.id.drawer_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
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
        Calendar now = Calendar.getInstance();
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        int nowMins = now.get(Calendar.MINUTE);

        int nowMinuteOfDay =  60*nowHours + nowMins;

        String AM_PM = "";
        if (nowHours < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
            nowHours -= 12;
        }

        TakenDrink takenDrink = new TakenDrink(drink.getCaffineAmount(), nowHours+":" + fixMinutes(nowMins) + " " + AM_PM); //military time
        takenDrinks.add(takenDrink);
        testTakenDrinks.add(takenDrink);
        drink.setDrinkCategory(-1);
        recentDrinks.add(drink);
        saveDrinks(takenDrink, drink);

        calcCaffineInCurrSystem();
        maxCaffineCanTake();

        Toast.makeText(this, drink.getDrinkName(), Toast.LENGTH_SHORT).show();

//        TODO - do something about updating UI and updating the images with the proper things
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

    //calculates how much caffine each drink gives
    public int calcCaffineContributionAtSleepTime(int takenTime, int caffineAmount){ //cafAmount is 100mg
        int bedTimeByTotalMinutes = sleepTimeMinutes;
        if(bedTimeByTotalMinutes < 720){
            bedTimeByTotalMinutes += 1440; //if its in the am, add minutes
        }
        int timeDifferenceDrinkSleep = bedTimeByTotalMinutes - takenTime; //6 hour , 12 hours before bedtime
        float numHalflives = timeDifferenceDrinkSleep/halflifeMinutes; //2 half lives
        int caffineContribution = (int) (caffineAmount/(2 * numHalflives)); //25mg

        return caffineContribution;
    }

    public void calcCaffineInCurrSystem(){ //cafAmount is 100mg
        Calendar now = Calendar.getInstance();
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        int nowMins = now.get(Calendar.MINUTE);
        int currTimeMinutes = 60*nowHours + nowMins;
        int currCafLevel = 0;

        if (takenDrinks != null) {
            for (TakenDrink drink : takenDrinks) {
                DateTimeFormatter formatAmPm = DateTimeFormat.forPattern("hh:mm a");
                DateTime drankTime = formatAmPm.parseDateTime(drink.getTime());

                int drankMinute = drankTime.getMinuteOfDay();
                int timeDifferenceDrinkSleep = currTimeMinutes - drankMinute; //6 hour , 12 hours before bedtime
                float numHalflives = timeDifferenceDrinkSleep/halflifeMinutes; //2 half lives
                if (numHalflives == 0.0)
                    currCafLevel = drink.getCaffineAmount();
                else
                    currCafLevel = (int) (drink.getCaffineAmount()/(2 * numHalflives)); //25mg
            }
        }

        caffineCurrentlySystem = currCafLevel;
        setTvCurrInYouText(Integer.toString(caffineCurrentlySystem));
    }

    public void maxCaffineCanTake() {
        if (!sleepIn) {
            int bedTimeByTotalMinutes = sleepTimeMinutes;

            if (bedTimeByTotalMinutes < 720) {
                bedTimeByTotalMinutes += 1440; //if its in the am, add minutes
            }

            //TODO - this needs to be fixed with the right time
            DateTime now = new DateTime();
            int timeDifferenceNowSleep = bedTimeByTotalMinutes - now.getMinuteOfDay(); //how much time left before bedtime
            float numHalflives = timeDifferenceNowSleep / halflifeMinutes; //12 hours until bed, 6 hours is halftime, 2 half lives
            float maxCaffineCanTake = (maxCaffine - caffineCurrentlySystem) / (2 * numHalflives); //you can take 400mg, subtract the 100mg in you then multiply it by two half lives
            maxCaffine = (int) maxCaffineCanTake;

            if (maxCaffineCanTake > 0) {
                String displayMaxCaffiene = Integer.toString(maxCaffine);
                setTvHowMuchMore(displayMaxCaffiene);
            } else {
                tvHowMuchMore.setText("Time to stop drinking");
            }
        } else {
            tvHowMuchMore.setText("Sleeping in");
        }
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

    private void setSleepTimeMinutes() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        String sleepGoal = sharedPreferences.getString(getString(R.string.SLEEP_GOAL), "ERROR");
        String tomorrowsWakeup = tomorrowsWakeup();

        if (!tomorrowsWakeup.equals("SLEEP_IN")) {
            sleepTimeMinutes = getBedTimeInMinutes(sleepGoal, tomorrowsWakeup);
        } else {
            sleepIn = true;
            tvHowMuchMore.setText("Sleeping in tomorrow");
        }
    }

    private int getBedTimeInMinutes(String sleepGoal, String tomorrowsWakeup) {
        //get sleep goal time
        DateTimeFormatter sleepGoalFormat = DateTimeFormat.forPattern("hh'h' mm'm'");
        DateTime sleepGoalDate = sleepGoalFormat.parseDateTime(sleepGoal);
        int sleepGoalHours = sleepGoalDate.getHourOfDay();

        //get wakeup time
        DateTimeFormatter wakeUpFormat = DateTimeFormat.forPattern("hh:mm a");
        DateTime wakeupDate = wakeUpFormat.parseDateTime(tomorrowsWakeup);

        //calculate bedtime
        return wakeupDate.minusHours(sleepGoalHours).getMinuteOfDay();
    }

    private String tomorrowsWakeup() {
        DateTime now = new DateTime();
        int day = now.getDayOfWeek();
        String wakeupTime = "";

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        switch (day) {
            case 0: // Sunday, so get monday
                wakeupTime = sharedPreferences.getString("MONDAY", "ERROR");
                break;

            case 1:
                wakeupTime = sharedPreferences.getString("TUESDAY", "ERROR");
                break;

            case 2:
                wakeupTime = sharedPreferences.getString("WEDNESDAY", "ERROR");
                break;

            case 3:
                wakeupTime = sharedPreferences.getString("THURSDAY", "ERROR");
                break;

            case 4:
                wakeupTime = sharedPreferences.getString("FRIDAY", "ERROR");
                break;

            case 5:
                wakeupTime = sharedPreferences.getString("SATURDAY", "ERROR");
                break;

            case 6:
                wakeupTime = sharedPreferences.getString("SUNDAY", "ERROR");
                break;
        }

        return wakeupTime;
    }

    private void setHalflifeMinutes() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
        float halflife = sharedPreferences.getFloat(getString(R.string.HALF_LIFE), -1);
        halflifeMinutes = 60 * halflife;
    }

    private void setMaxCaffine() {
        if (maxCaffine == 0) {
            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.USER_SETTINGS), Context.MODE_PRIVATE);
            maxCaffine = sharedPreferences.getInt(getString(R.string.MAX), -1);
        }
    }
}
