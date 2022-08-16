package com.example.mapforgirls.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScrapTable")
data class Scrap(var sectionName: String, var ColumnId: String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}