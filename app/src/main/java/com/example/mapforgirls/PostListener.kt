package com.example.mapforgirls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

interface PostListener {
    fun loadPage(name : String)
    fun checkEmail()
}