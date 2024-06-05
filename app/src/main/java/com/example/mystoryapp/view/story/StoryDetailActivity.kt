package com.example.mystoryapp.view.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>("STORY")

        story?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.tvDetailPhoto)
        }
    }
}