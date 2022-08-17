package com.example.mapforgirls.ui.main.chatting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapforgirls.MainActivity
import com.example.mapforgirls.PharmacyData
import com.example.mapforgirls.databinding.FragmentChattingBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class ChattingFragment : Fragment()  {
    lateinit var binding: FragmentChattingBinding

    var db = FirebaseFirestore.getInstance()
    var pharmacyList = arrayListOf<PharmacyData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)

        setPharmacistData()
        try {
            val chattingRoomAdapter = ChattingRoomsRVAdapter(requireContext())
            binding.chattingRoomsRv.adapter = chattingRoomAdapter
        } catch (e : Exception) {
            Log.d("ChattingFragment", e.message.toString())
        }

        return binding.root
    }

    // 주변 인기 약사
    private fun setPharmacistData() {
        db.collection("pharmacyInfo")
            .get()
            .addOnSuccessListener { result ->
                pharmacyList.clear()
                pharmacyList.addAll(result!!.toObjects(PharmacyData::class.java))

                val pharmacistAdapter = PharmacistRVAdapter(pharmacyList)
                binding.chattingPharmacistRv.adapter = pharmacistAdapter

                pharmacistAdapter.setMyItemClickListener(object :
                PharmacistRVAdapter.MyItemClickListener {
                    override fun onItemClick(pharmacist: PharmacyData) {
                        var dialog = DialogPharmacist(requireContext())
                        dialog.showDialog()
                        dialog.setViews(pharmacist)
                        dialog.setOnClickListener()
                    }
                })
            }
            .addOnFailureListener { e ->
                if (e != null) {
                    Log.w("Result", "Listen failed", e)
                }
            }
    }

    // 채팅방
    private fun setChattingRoomsData() {
        val chattingRoomAdapter = ChattingRoomsRVAdapter(requireContext())
        binding.chattingRoomsRv.adapter = chattingRoomAdapter
    }

}