package com.example.mapforgirls

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_certify.*

class CertifyActivity : AppCompatActivity() {
    // var isVisibility : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certify)

        certify_idNum_front_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //텍스트를 입력 후
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 전
                certify_fail_tv.text = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 중
            }
        })
        certify_idNum_back_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //텍스트를 입력 후
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 전
                certify_fail_tv.text = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 중
            }
        })

        /*
        certify_check_ibtn.setOnClickListener {
            // +이미지 변경하는 코드
            isVisibility = !isVisibility
            //certify_idNum_back_et.visibility = View.INVISIBLE
            if(isVisibility){
                certify_idNum_back_et.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
                certify_idNum_back_et.inputType = InputType.TYPE_CLASS_NUMBER
            }
            else {
                certify_idNum_back_et.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
        }
         */

        certify_btn.setOnClickListener {
            var id = certify_idNum_front_et.text.toString()+certify_idNum_back_et.text.toString()

            if(isValidFemaleRegistrationID(id)){
                Toast.makeText(this, "본인인증완료",Toast.LENGTH_LONG).show()
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
            else{
                certify_fail_tv.text = "본인인증에 실패하셨습니다."
            }
        }
    }
    fun isValidFemaleRegistrationID(id: String): Boolean {  // 주민등록번호가 유효한 여성인지 확인하는 함수
        if(id.length != 13)
            return false
        val reg = Regex("^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[2|4][0-9]{6}$")
        if(!id.matches(reg))
            return false

        var tempSum= 0
        for (i in 0 until id.length - 1) {  
            tempSum += id[i].toString().toInt() * ((i % 8) + 2)
        }

        return 11 - (tempSum % 11) == id[id.length - 1].toString().toInt()
    }
}