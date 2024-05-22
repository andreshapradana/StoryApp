package com.example.mystoryapp.view.story

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.api.ListStoryItem
import com.example.mystoryapp.databinding.ItemStoryBinding

class StoryAdapter(private var listStory: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount() = listStory.size

    fun updateStories(newList: List<ListStoryItem>) {
        listStory = newList
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvItemTitle.text = story.name
            binding.tvItemDescription.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            Log.d("StoryAdapter", "bind Success")
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, StoryDetailActivity::class.java)
                intent.putExtra("STORY", story)
                context.startActivity(intent)
            }
        }
    }
}