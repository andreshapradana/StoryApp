package com.example.mystoryapp.view.story

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityStoryBinding
import com.example.mystoryapp.view.StoryViewModel
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.main.MainViewModel
import com.example.mystoryapp.view.welcome.WelcomeActivity

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val mainViewModel by viewModels<MainViewModel> {
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("StoryActivity", "onResume called")
        viewModel.getStories()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Konfirmasi Logout")
            setMessage("Apakah anda ingin logout dari aplikasi?")
            setPositiveButton("Ya") { dialog, which ->
                mainViewModel.logout()
                val intent = Intent(this@StoryActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            create()
            show()
        }
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
        viewModel.listStory.observe(this) { stories ->
            if (stories != null) {
                storyAdapter = StoryAdapter(stories)
                binding.recyclerView.adapter = storyAdapter
                storyAdapter.notifyDataSetChanged()
                Log.d("StoryActivity", "Stories observed and adapter updated")
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            Log.d("StoryActivity", "Loading state changed: $isLoading")
        }
    }
}

