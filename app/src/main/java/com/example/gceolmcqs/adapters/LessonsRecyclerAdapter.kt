package com.example.gceolmcqs.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gceolmcqs.R

class LessonsRecyclerAdapter(private val lessons: List<String>): RecyclerView.Adapter<LessonsRecyclerAdapter.ViewHolder>() {
    private lateinit var context: Context
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvLesson: TextView = view.findViewById(R.id.tvLesson)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lessons_recycler_view_lo, null, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lessons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvLesson.text =  context.getString(R.string.lesson, (position + 1).toString(),lessons[position],)

    }
}