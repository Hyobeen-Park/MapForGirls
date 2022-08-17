package com.example.mapforgirls.ui.main.chatting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import com.example.mapforgirls.PharmacyData
import com.example.mapforgirls.R
import kotlinx.android.synthetic.main.dialog_pharmacist.*
import java.io.IOException
import java.util.*

// 약사 팝업
class DialogPharmacist(context: Context) {
    private val dialog = Dialog(context)
    lateinit var pharmacist: PharmacyData

    fun showDialog() {
        dialog.setContentView(R.layout.dialog_pharmacist)
        dialog.show()
    }

    fun setViews(pharmacy: PharmacyData) {
        this.pharmacist = pharmacy
        dialog.dialog_pharmacist_pharmacist_tv.text = pharmacy.pharmacyName + " 약사"
        dialog.dialog_pharmacist_location_tv.text = getLocation(pharmacy)
    }

    fun setOnClickListener() {
        // X버튼
        dialog.dialog_pharmacist_cancel_btn.setOnClickListener {
            dialog.dismiss()
        }

        // 1:1 상담 버튼
        dialog.dialog_pharmacist_chat_btn.setOnClickListener {
            var intent = Intent(dialog.context, ChattingDetailActivity::class.java)
            intent.putExtra("pharmacist", pharmacist)
            dialog.context.startActivity(intent)
        }
    }

    private fun getLocation(pharmacy: PharmacyData) : String {
        // 위도, 경도를 이용해 주소 받아오기
        var mGeoCoder =  Geocoder(dialog.context, Locale.KOREAN)
        var mResultList: List<Address>? = null
        try{
            mResultList = mGeoCoder.getFromLocation(
                pharmacy.latitude, pharmacy.longitude, 1
            )
            }catch(e: IOException){
                e.printStackTrace()
            }
            if(mResultList != null){
                return mResultList[0].getAddressLine(0)
            }

        return ""
    }
}