package com.devpark.calculator

import android.os.Build
import androidx.room.Database
import androidx.room.RoomDatabase
import com.devpark.calculator.dao.HistoryDao
import com.devpark.calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao



}