package com.example.mapforgirls

import java.io.Serializable

data class PharmacyData(
    var pharmacyName : String = "",
    var address : String = "",
    var phoneNum : String = "",
    var latitude : Double = 0.0,
    var longitude : Double = 0.0
) : Serializable
