package com.example.gceolmcqs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gceolmcqs.R
import com.example.gceolmcqs.UserDataManager
import com.example.gceolmcqs.fragments.TableOfContentsFragment

class SimpleRecyclerAdapter(private val chapters: List<String>, private val listener: TableOfContentsFragment.OnTableOfContentClickLister): RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvItem: TextView = view.findViewById(R.id.tvRecyclerViewItem)
        val root: LinearLayout = view.findViewById(R.id.recyclerItemLayout)
        init {
            root.setOnClickListener {
                listener.onTableOfContentItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItem.text = chapters[position]
    }
}