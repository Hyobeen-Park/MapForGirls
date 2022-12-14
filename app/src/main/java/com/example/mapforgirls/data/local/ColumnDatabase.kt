package com.example.mapforgirls.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.data.entities.Scrap

@Database(entities = [ColumnData::class, Scrap::class], version = 1)
abstract class ColumnDatabase: RoomDatabase() {
    abstract fun columnDao(): ColumnDAO

    companion object {
        private var instance: ColumnDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ColumnDatabase? {
            if (instance == null) {
                synchronized(ColumnDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ColumnDatabase::class.java,
                        "column-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}