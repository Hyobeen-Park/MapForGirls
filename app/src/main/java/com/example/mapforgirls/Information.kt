package com.example.mapforgirls

import java.io.Serializable

data class Information(
    var infoCover : Int? = null,
    var infoTitle : String = "",
    var infoContent : String = ""
) : Serializable
