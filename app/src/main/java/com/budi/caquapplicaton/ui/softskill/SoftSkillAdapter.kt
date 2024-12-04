package com.budi.caquapplicaton.ui.softskill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.budi.caquapplication.R

class SoftSkillAdapter(
    private var softSkillList: List<String>
) : RecyclerView.Adapter<SoftSkillAdapter.SoftSkillViewHolder>() {

    inner class SoftSkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewSoftSkill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoftSkillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soft_skill, parent, false)
        return SoftSkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoftSkillViewHolder, position: Int) {
        holder.textView.text = softSkillList[position]
    }

    override fun getItemCount(): Int = softSkillList.size

    fun updateData(newSoftSkillList: List<String>) {
        softSkillList = newSoftSkillList
        notifyDataSetChanged()
    }
}
