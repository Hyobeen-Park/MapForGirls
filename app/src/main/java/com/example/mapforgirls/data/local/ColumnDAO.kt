package com.example.mapforgirls.data.local

import androidx.room.*
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.data.entities.Scrap

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

    @Query("SELECT * FROM ScrapTable")
    fun getScrappedColumn(): List<Scrap>

    @Query("SELECT id FROM ScrapTable WHERE sectionName = :sectionName AND columnId = :columnId")
    fun isScrapedColumn(sectionName: String, columnId: String): Int?

    @Query("DELETE FROM ScrapTable")
    fun initScrapTable()
}