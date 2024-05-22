package com.example.mystoryapp.view.story

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.api.ListStoryItem
import com.example.mystoryapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the story object passed from the adapter
        val story = intent.getParcelableExtra<ListStoryItem>("STORY")

        // Display the story details
        story?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.tvDetailPhoto)
        }
    }
}