package com.example.mapforgirls

import androidx.room.*

@Dao
interface ColumnDAO {
    @Insert
    fun insert(column: ColumnData)

    @Update
    fun update(column: ColumnData)

    @Delete
    fun delete(column: ColumnData)

    @Insert
    fun scrapColumn(scrap: Scrap)

    @Query("DELETE FROM ScrapTable WHERE sectionName = :sectionName AND columnId = :columnId")
    fun cancelScrap(sectionName: String, columnId: String)

    @Query("SELECT id FROM ScrapTable WHERE sectionName = :sectionName AND columnId = :columnId")
    fun isScrapedColumn(sectionName: String, columnId: String): Int?
}