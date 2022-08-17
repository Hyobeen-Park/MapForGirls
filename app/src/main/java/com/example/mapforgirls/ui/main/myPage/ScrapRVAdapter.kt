package com.example.mapforgirls.ui.main.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.databinding.ItemScrapBinding
import java.util.*

class ScrapRVAdapter (private val columnList : ArrayList<ColumnData>) :
    RecyclerView.Adapter<ScrapRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(column: ColumnData)
    }

    private lateinit var mItemClickListener : MyItemClickListener
    fun setMyItemClickListener (itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun addItem(column: ColumnData) {
        columnList.add(column)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        columnList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemScrapBinding = ItemScrapBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(columnList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(columnList[position])}
    }

    override fun getItemCount(): Int = columnList.size

    inner class ViewHolder(val binding : ItemScrapBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(column: ColumnData){
            binding.itemClipTitle.text = column.title
            binding.itemClipContent.text = column.content
        }
    }


}