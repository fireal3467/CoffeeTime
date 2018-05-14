package com.example.emilylien.coffeetime.data;

/**
 * Created by alanyu on 5/14/18.
 */

public class DrinkInfo {

    private String drinkName;

    private String size;

    private int caffineAmount;

    public DrinkInfo(String name, String size, int amount){
        this.drinkName = name;
        this.size = size;
        this.caffineAmount = amount;
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
}
