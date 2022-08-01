package com.example.mapforgirls

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapforgirls.databinding.ItemInfoBinding

class InfoRVAdapter (private val infoList : ArrayList<Information>) :
    RecyclerView.Adapter<InfoRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(info: Information)
    }

    private lateinit var mItemClickListener : MyItemClickListener
    fun setMyItemClickListener (itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun addItem(info: Information) {
        infoList.add(info)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        infoList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemInfoBinding = ItemInfoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(infoList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(infoList[position])}

    }

    override fun getItemCount(): Int = infoList.size

    inner class ViewHolder(val binding : ItemInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: Information) {
            binding.itemInfoMainIv.setImageResource(info.infoCover!!)
            binding.itemInfoTitleTv.text = info.infoTitle
        }
    }


}