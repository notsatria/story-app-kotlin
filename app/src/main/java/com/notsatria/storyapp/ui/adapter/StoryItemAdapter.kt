package com.notsatria.storyapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.notsatria.storyapp.R
import com.notsatria.storyapp.data.remote.response.StoryItem
import com.notsatria.storyapp.databinding.StoryItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class StoryItemAdapter(private val onItemClick: (String, String) -> Unit) :
    PagingDataAdapter<StoryItem, StoryItemAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ViewHolder(
        private val binding: StoryItemBinding,
        private val onItemClick: (String, String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryItem) {
            binding.tvItemName.text = data.name
            formatDate(binding, data)
            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .placeholder(R.drawable.ic_image)
                .into(binding.ivItemPhoto)
            val transitionName = "story_image_${data.id}"
            binding.ivItemPhoto.transitionName = transitionName
            binding.root.setOnClickListener {
                Log.d("StoryItemAdapter", "bind: item clicked ${data.id}")
                onItemClick(data.id!!, transitionName)
            }
        }

        private fun formatDate(viewBinding: StoryItemBinding, story: StoryItem) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = dateFormat.parse(story.createdAt!!)

            val readableDateFormat = SimpleDateFormat("dd MMM yy", Locale("id", "ID"))
            viewBinding.tvItemDate.text = readableDateFormat.format(date)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}