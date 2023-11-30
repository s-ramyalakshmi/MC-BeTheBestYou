package com.example.mcprojectv5;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Rating.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RatingDao ratingDao();
}

