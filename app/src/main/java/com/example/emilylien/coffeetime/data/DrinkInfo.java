package com.example.emilylien.coffeetime.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by alanyu on 5/14/18.
 */

@Entity
public class DrinkInfo implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private long drinkID;

    @ColumnInfo(name = "drink_name")
    private String drinkName;

    @ColumnInfo(name = "category")
    private int drinkCategory;

    @ColumnInfo(name = "drink_size")
    private String size;

    @ColumnInfo(name = "caffine_amount")
    private int caffineAmount;

    public DrinkInfo(String drinkName, String size, int caffineAmount, int drinkCategory){
        this.drinkName = drinkName;
        this.size = size;
        this.caffineAmount = caffineAmount;
        this.drinkCategory = drinkCategory;
    }


    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getCaffineAmount() {
        return caffineAmount;
    }

    public void setCaffineAmount(int caffineAmount) {
        this.caffineAmount = caffineAmount;
    }

    public long getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(long drinkID) {
        this.drinkID = drinkID;
    }

    public int getDrinkCategory() {
        return drinkCategory;
    }

    public void setDrinkCategory(int drinkCategory) {
        this.drinkCategory = drinkCategory;
    }
}
