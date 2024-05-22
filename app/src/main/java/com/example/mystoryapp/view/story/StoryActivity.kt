package com.example.mystoryapp.view.story

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityStoryBinding
import com.example.mystoryapp.view.StoryViewModel
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.main.MainActivity

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("StoryActivity", "onCreate called")
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeStories()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.fabOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
        observeStories()
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@StoryActivity)
            adapter = storyAdapter
        }
        Log.d("StoryActivity", "RecyclerView set up")
    }

    private fun observeStories() {
        viewModel.getStories().observe(this, Observer { result ->
            Log.d("StoryActivity", "observeStories called")
            result.fold(
                onSuccess = { storyResponse ->
                    if (storyResponse.listStory.isNotEmpty()) {
                        storyAdapter.updateStories(storyResponse.listStory)
                    } else {
                        Log.e("StoryActivity", "Story list is empty")
                    }
                },
                onFailure = { throwable ->
                    Log.e("StoryActivity", "Error fetching stories", throwable)
                }
            )
        })
    }
}