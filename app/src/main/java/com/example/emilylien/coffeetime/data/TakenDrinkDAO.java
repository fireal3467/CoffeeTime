package com.example.emilylien.coffeetime.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by alanyu on 5/16/18.
 */

@Dao
public interface TakenDrinkDAO {
    @Query("SELECT * FROM takendrink")
    List<TakenDrink> getAllTakenDrinks();

    @Insert
    long insertDrink(TakenDrink drink);

    @Delete
    void deleteDrink(TakenDrink drink);

    @Update
    void updateDrink(TakenDrink drink);
}
