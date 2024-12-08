package com.budi.caquapplicaton.ui.softskill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.budi.caquapplication.R

class SoftSkillAdapter(
    private var softSkills: List<String>
) : RecyclerView.Adapter<SoftSkillAdapter.ViewHolder>() {

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun updateData(newData: List<String>) {
        softSkills = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.softSkillName)

        init {
            view.setOnClickListener {
                onItemClickListener?.invoke(softSkills[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soft_skill, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = softSkills[position]
    }

    override fun getItemCount(): Int = softSkills.size
}
