package com.budi.caquapplicaton.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.budi.caquapplication.R
import com.budi.caquapplicaton.room.CareerRecommendationEntity

class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val recommendations = mutableListOf<CareerRecommendationEntity>()

    fun setData(newData: List<CareerRecommendationEntity>) {
        recommendations.clear()
        recommendations.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(recommendations[position])
    }

    override fun getItemCount(): Int = recommendations.size

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.item_text)

        fun bind(recommendation: CareerRecommendationEntity) {
            textView.text = recommendation.recommendation
        }
    }
}
