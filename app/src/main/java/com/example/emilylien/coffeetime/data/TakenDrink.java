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
    private long time;

    public TakenDrink(int caffineAmount, long time){
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
