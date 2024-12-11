package com.budi.caquapplicaton.ui.career

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.budi.caquapplication.databinding.ItemFeedbackBinding
import com.budi.caquapplicaton.retrofit.VideoFeedback
import com.bumptech.glide.Glide

class VideoFeedbackAdapter : RecyclerView.Adapter<VideoFeedbackAdapter.FeedbackViewHolder>() {

    private var feedbackList: List<VideoFeedback> = emptyList()

    fun submitList(feedback: List<VideoFeedback>) {
        feedbackList = feedback
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        // Inflate the binding for the item layout
        val binding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedbackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        holder.bind(feedback)
    }

    override fun getItemCount(): Int = feedbackList.size

    inner class FeedbackViewHolder(private val binding: ItemFeedbackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(feedback: VideoFeedback) {
            binding.feedbackTitle.text = feedback.title

            // Load thumbnail image using Glide or Picasso
            Glide.with(binding.root.context)
                .load(feedback.thumbnails.high.url) // Assuming feedback.thumbnails.high.url contains the image URL
                .into(binding.feedbackThumbnail)

            // Set the link text (optional)
            binding.feedbackLink.text = "Watch Video"

            // Set up click listener for the link
            binding.feedbackLink.setOnClickListener {
                // Open the video URL or perform any action
                openLink(binding.root.context, feedback.videoLink)
            }
        }

        // Function to open the video link
        private fun openLink(context: Context, link: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
        }
    }
}

