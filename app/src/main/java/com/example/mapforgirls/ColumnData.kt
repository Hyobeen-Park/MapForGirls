package com.example.mapforgirls

import java.io.Serializable

data class ColumnData(
    var cover : Int? = null,
    var title : String = "",
    var author : String = "",
    var content : String = ""
) : Serializable
