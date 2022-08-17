package com.example.mapforgirls.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecentlyTable")
data class RecentlyData (
    var pharmacyName: String = "",
    var address: String = "",
    var phoneNum: String = ""
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}