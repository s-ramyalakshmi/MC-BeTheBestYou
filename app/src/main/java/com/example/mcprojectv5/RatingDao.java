package com.example.mcprojectv5;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RatingDao {
    @Insert
    void insertRating(Rating rating);

    @Query("SELECT * FROM ratings")
    List<Rating> getAllRatings();
}

