package com.example.mapforgirls.ui.main.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapforgirls.PharmacyData
import com.example.mapforgirls.databinding.ItemPharmacistBinding
import java.util.*

class PharmacistRVAdapter (private val pharmacistList : ArrayList<PharmacyData>) :
    RecyclerView.Adapter<PharmacistRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(pharmacist: PharmacyData)
    }

    private lateinit var mItemClickListener : MyItemClickListener
    fun setMyItemClickListener (itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun addItem(pharmacist: PharmacyData) {
        pharmacistList.add(pharmacist)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        pharmacistList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemPharmacistBinding = ItemPharmacistBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pharmacistList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(pharmacistList[position])}

    }

    //    override fun getItemCount(): Int = pharmacistList.size
    override fun getItemCount(): Int = 4

    inner class ViewHolder(val binding : ItemPharmacistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pharmacist: PharmacyData) {
//            binding.itemPharmacistCiv.setImageResource(pharmacistList.cover!!)
            binding.itemPharmacistTv.text = pharmacist.pharmacyName
        }
    }

}