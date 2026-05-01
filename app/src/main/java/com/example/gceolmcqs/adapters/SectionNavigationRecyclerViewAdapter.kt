package com.example.gceolmcqs.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gceolmcqs.R

class SectionNavigationRecyclerViewAdapter(
    private val context: Context,
    private val listSections: Array<Bundle>,
    private val listener: OnRecyclerItemClickListener,
    private val sectionsAnswered: List<Boolean>,
    private var sectionScores: List<Int> = emptyList()
) :
    RecyclerView.Adapter<SectionNavigationRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvSectionNavItem: TextView = view.findViewById(R.id.tvSectionNavItem)
        val tvSectionNumberOfQuestions: TextView = view.findViewById(R.id.tvSectionNumberOfQuestions)
        val sectionNavItemLayout: LinearLayout = view.findViewById(R.id.sectionNavItemLayout)
        val scoreLo: LinearLayout = view.findViewById(R.id.scoreLo)
        val tvSectionScore: TextView = view.findViewById(R.id.tvSectionScore)

        init{
            sectionNavItemLayout.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRecyclerItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.section_nav_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = listSections[position]
        holder.tvSectionNavItem.text = section.getString("sectionName")
        val numQuestions = section.getString("numberOfQuestions") ?: "0"
        holder.tvSectionNumberOfQuestions.text = "Number of questions: $numQuestions"

        if(position < sectionsAnswered.size && sectionsAnswered[position]){
            val numberOfQuestionsInSection = numQuestions.toIntOrNull() ?: 1
            val sectionScore = sectionScores.getOrNull(position) ?: 0
            val scorePercentage = ((sectionScore.toDouble() / numberOfQuestionsInSection.toDouble()) * 100).toInt()

            holder.scoreLo.visibility = View.VISIBLE
            holder.tvSectionScore.text = "$sectionScore/$numberOfQuestionsInSection"

            if (scorePercentage >= 50){
                holder.tvSectionScore.setTextColor(context.resources.getColor(R.color.color_green))
            }else{
                holder.tvSectionScore.setTextColor(context.resources.getColor(R.color.color_red))
            }
        } else {
            holder.scoreLo.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listSections.size
    }

    fun updateSectionScore(newScores: List<Int>){
        this.sectionScores = newScores
        notifyDataSetChanged()
    }

    interface OnRecyclerItemClickListener{
        fun onRecyclerItemClick(position: Int)
    }
}
