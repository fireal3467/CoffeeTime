package com.example.emilylien.coffeetime.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by alanyu on 5/15/18.
 */

@Entity
public class TakenDrink {

    @PrimaryKey(autoGenerate = true)
    private long drinkID;

    @ColumnInfo(name = "caffine_amount")
    private int caffineAmount;

    @ColumnInfo(name = "time")
    private String time;

    public TakenDrink(int caffineAmount, String time){
        this.caffineAmount = caffineAmount;
        this.time = time;
    }

    public long getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(long drinkID) {
        this.drinkID = drinkID;
    }

    public int getCaffineAmount() {
        return caffineAmount;
    }

    public void setCaffineAmount(int caffineAmount) {
        this.caffineAmount = caffineAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
