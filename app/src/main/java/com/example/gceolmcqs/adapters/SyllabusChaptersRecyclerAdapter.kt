package com.example.gceolmcqs.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gceolmcqs.R
import com.example.gceolmcqs.databinding.ChapterRecyclerViewItemLoBinding

class SyllabusChaptersRecyclerAdapter(private val chapters: List<String>, private val listener: SyllabusChapterItemClickLister): RecyclerView.Adapter<SyllabusChaptersRecyclerAdapter.ViewHolder>() {


    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val cardLo: CardView = view.findViewById(R.id.cardLo)
        val tvChapter: TextView = view.findViewById(R.id.tvChapter)
        init {
            cardLo.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chapter_recycler_view_item_lo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvChapter.text = chapters[position]
    }
}
interface SyllabusChapterItemClickLister {
    fun onItemClick(itemIndex: Int)
}