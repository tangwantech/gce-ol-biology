package com.example.gceolmcqs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gceolmcqs.R
import com.example.gceolmcqs.databinding.DictionaryItemBinding

class DictionaryActivityRecyclerAdapter(private val list: ArrayList<String>, private val listener: OnItemClickListener): RecyclerView.Adapter<DictionaryActivityRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val tvItem: TextView = view.findViewById(R.id.tvItem)
        init {
            tvItem.setOnClickListener {
                listener.onItemClick(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dictionary_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItem.text = list[position]
    }


    interface OnItemClickListener{
        fun onItemClick(keyWord: String)
    }


}