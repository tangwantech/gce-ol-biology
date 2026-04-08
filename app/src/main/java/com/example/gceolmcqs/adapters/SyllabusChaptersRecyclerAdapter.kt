package com.example.gceolmcqs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gceolmcqs.R

class SyllabusChaptersRecyclerAdapter(private val chapters: List<String>, private val listener: SyllabusChapterItemClickLister): RecyclerView.Adapter<SyllabusChaptersRecyclerAdapter.ViewHolder>() {


    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val tvItem: TextView = view.findViewById(R.id.tvItem)
        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_lo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItem.text = chapters[position]
    }
}
interface SyllabusChapterItemClickLister {
    fun onItemClick(itemIndex: Int)
}