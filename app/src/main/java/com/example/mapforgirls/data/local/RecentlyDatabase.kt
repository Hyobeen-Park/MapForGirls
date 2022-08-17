package com.example.mapforgirls.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.data.entities.RecentlyData
import com.example.mapforgirls.data.entities.Scrap
import kotlinx.coroutines.internal.synchronized

@Database(entities = [RecentlyData::class], version = 2)
abstract class RecentlyDatabase: RoomDatabase() {
    abstract fun recentlyDao(): ColumnDAO

    companion object {
        private var instance: RecentlyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RecentlyDatabase? {
            if(instance == null) {
                kotlin.synchronized(RecentlyDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecentlyDatabase::class.java,
                        "recently-database"
                    ).build()
                }
            }
            return instance
        }
    }
}