package com.notsatria.storyapp

import com.notsatria.storyapp.data.remote.response.StoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryItem> {
        val items: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItem(
                id = i.toString(),
                name = "Story $i",
                description = "Description $i",
                photoUrl = "https://asset.kompas.com/crops/vmDriSafVxhO05gc2dSOXby53Mc=/13x7:700x465/750x500/data/photo/2021/09/24/614dc6865eb24.jpg",
                createdAt = "Created At $i"
            )
            items.add(story)
        }
        return items
    }

}