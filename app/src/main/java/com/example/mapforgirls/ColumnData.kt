package com.example.mapforgirls

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ColumnTable")
data class ColumnData(
    var sectionName : String? = "",
    var columnId : String? = "00",
    var cover : Int? = null,
    var title : String = "",
    var author : String = "",
    var content : String = "",
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) :Serializable
