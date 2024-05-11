package com.notsatria.storyapp.ui.adapter

import com.bumptech.glide.Glide
import com.notsatria.storyapp.R
import com.notsatria.storyapp.data.remote.response.StoryItem
import com.notsatria.storyapp.databinding.StoryItemBinding
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.text.SimpleDateFormat
import java.util.Locale

class StoryItemAdapter(
    private val storyItem: StoryItem,
    private val onItemClick: (storyId: String) -> Unit
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val viewBinding = StoryItemBinding.bind(this)
            viewBinding.tvItemName.text = storyItem.name
            formatDate(viewBinding)
            Glide.with(this)
                .load(storyItem.photoUrl)
                .placeholder(R.drawable.ic_image)
                .into(viewBinding.ivItemPhoto)
        }
        viewHolder.root.setOnClickListener {
            onItemClick(storyItem.id!!)
        }
    }

    override fun getLayout(): Int = R.layout.story_item

    private fun formatDate(viewBinding: StoryItemBinding) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = dateFormat.parse(storyItem.createdAt!!)

        val readableDateFormat = SimpleDateFormat("dd MMM yy", Locale("id", "ID"))
        viewBinding.tvItemDate.text = readableDateFormat.format(date)
    }
}