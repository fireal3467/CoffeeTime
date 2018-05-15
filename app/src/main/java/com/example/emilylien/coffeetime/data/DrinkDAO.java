package com.example.emilylien.coffeetime.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by alanyu on 5/15/18.
 */

@Dao
public interface DrinkDAO {
    @Query("SELECT * FROM drinkinfo")
    List<DrinkInfo> getAll();

    @Query("SELECT * FROM drinkinfo WHERE category = :category")
    List<DrinkInfo> getAllForCategory(int category);

    @Insert
    long insertDrink(DrinkInfo drink);

    @Delete
    void delete(DrinkInfo drink);

    @Update
    void update(DrinkInfo drink);
}
