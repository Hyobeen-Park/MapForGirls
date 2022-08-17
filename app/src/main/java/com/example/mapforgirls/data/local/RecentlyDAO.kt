package com.example.mapforgirls.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.mapforgirls.data.entities.RecentlyData

@Dao
interface RecentlyDAO {
    @Insert
    fun insert(recently: RecentlyData)

    @Update
    fun update(recently: RecentlyData)

    @Delete
    fun delete(recently: RecentlyData)


}