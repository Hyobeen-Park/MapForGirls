package com.example.mapforgirls.ui.main.columns

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.databinding.ItemColumnBinding
import java.util.*

class ColumnsRVAdapter (private val columnList : ArrayList<ColumnData>) :
    RecyclerView.Adapter<ColumnsRVAdapter.ViewHolder>() {

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
        val binding : ItemColumnBinding = ItemColumnBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(columnList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(columnList[position])}

    }

    override fun getItemCount(): Int = columnList.size

    inner class ViewHolder(val binding : ItemColumnBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(column: ColumnData) {
            binding.itemColumnMainIv.setImageResource(column.cover!!)
            binding.itemColumnTitleTv.text = column.title
        }
    }

}